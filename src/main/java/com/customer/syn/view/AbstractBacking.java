package com.customer.syn.view;

import com.customer.syn.model.Address;
import com.customer.syn.model.BaseEntity;
import com.customer.syn.model.FormInputType;
import com.customer.syn.model.ViewMeta;
import com.customer.syn.service.BaseService;
import org.primefaces.model.menu.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.validation.constraints.PastOrPresent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.customer.syn.model.FormInputType.*;
import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

/** Base backing class with common functionality */
public abstract class AbstractBacking<E extends BaseEntity<I>, I extends Number> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private FacesContext fc;

    @Inject
    private ExternalContext ec;

    @Inject
    private SearchManager searchManager;

    @PersistenceContext
    private EntityManager em;

    protected I Id;
    protected E currentEntity;
    protected E currentSelected;
    protected String page;
    protected String searchOption;
    protected LocalDate searchDateTo;
    private final Class<?> entityClass;

    @PastOrPresent
    protected LocalDate searchDateFrom;

    protected List<E> values;
    protected List<E> entities;
    protected List<FormModel> formFields;
    protected List<ColumnModel> columnList;
    protected List<SearchModel.Field> searchFields;
    protected List<SearchModel.SelectModel> searchOptions;

    protected List<List<FormModel>> childFormModelsList = new ArrayList<>();

    private static final String EDIT_LOG = "[ Edit invoked on entity: {} ]";
    private static final String UPDATE_MSG = "%s with Id %d has been updated.";
    private static final String DELETE_MSG = "%s with Id %d has been deleted.";
    private static final String CREATE_MSG = "New %s has been created successfully.";
    protected static final String NO_SELECTION = "Please make a selection first.";
    private static final Class<ViewMeta> ANNOTATED_CLASS = ViewMeta.class;

    private static final Map<Class<?>, List<FormModel>> META_MAPPING = new ConcurrentHashMap<>();


    // ---------------------------------------------------------- constructors
    protected AbstractBacking() {
        try {
            ParameterizedType type =
                (ParameterizedType) this.getClass().getGenericSuperclass();
            this.entityClass = (Class<?>) type.getActualTypeArguments()[0];
            if (log.isDebugEnabled()) {
                log.debug("[{} constructor initialized]", getClass());
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected AbstractBacking(Class<?> clazz) {
        this.entityClass = clazz;
    }

    // ---------------------------------------------------------- abstract methods
    protected abstract BaseService<E, I> getService();

    protected abstract void doSearch(String value);


    // ---------------------------------------------------------- init
    @PostConstruct
    public void setup() {
        // TODO: add paging and lazy loading
        entities = getService().fetchAll();
        // backing data for entity specific search menu
        searchOptions = searchManager.getSearchOptions(getEntityName());
        searchFields = searchManager.getSearchFields(getEntityName());
        // backing data for form fields
        META_MAPPING.computeIfAbsent(Address.class, this::createModel); // TODO:
        formFields = setFormFields();
        // dynamic columns for datatables
        columnList = setColumns();
        setPage("list");
        // log.debug("{}", formFields);
        // log.debug("{} \n\n", META_MAPPING);
    }


    private List<FormModel> setFormFields() {
        List<FormModel> formModels = META_MAPPING.computeIfAbsent(entityClass, this::createModel);
        return
            formModels.stream()
                .filter(FormModel::isField)
                .collect(Collectors.toList());
    }


    private List<ColumnModel> setColumns() {
        return
            META_MAPPING.get(entityClass).stream()
                .map(f -> new ColumnModel(f.getLabel(), (String) f.getValue()))
                .collect(Collectors.toList());
    }


    private List<FormModel> createModel(Class<?> clazz) {
        List<FormModel> formModels = new ArrayList<>();
        for (Field field : sortFields(getAnnotatedFields(clazz, ANNOTATED_CLASS))) {
            ViewMeta annotation = field.getAnnotation(ANNOTATED_CLASS);
            FormModel model = new FormModel();
            if (annotation.type() != ELEMENTCOLLECTION) {
                model.setLabel(splitCamelCase(field.getName()));
            }
            model.setValue(field.getName());
            model.setIsField(annotation.formField());
            if (model.isField()) {
                FormInputType type = annotation.type();
                model.setType(type);
                if (!EnumSet.<FormInputType>range(TEXT, TEXTBOX).contains(type)) {
                    // create multi-value form field
                    multiValueField(field, model);
                }
            }
            formModels.add(model);
        }
        return formModels;
    }


    private void multiValueField(Field field, FormModel model) {
        if (field.getAnnotation(ManyToOne.class) != null) {
            processManyToOne(field, model);
        }
        else if (field.getAnnotation(ManyToMany.class) != null) {
            processManyToMany(field, model);
        }
        else if (field.getAnnotation(ElementCollection.class) != null) {
            processElementCollection(field, model);
        }
        else if (field.getAnnotation(OneToMany.class) != null) {
            processOneToMany(field, model);
        }
    }


    private void processElementCollection(Field field, FormModel model) {
        Class<?> refType = getCollectionAttribute(field);
        model.setReferencedType(refType);
        model.setCollectionType(getImplClass(field).getCanonicalName());
        log.debug("[ Element collection reference type: {} ]", refType);
        log.debug("[ Element collection child formModel: {} ]", childFormModel(refType));
    }


    private void processOneToMany(Field field, FormModel model) {
        // TODO:
    }


    private void processManyToOne(Field field, FormModel model) {
        Class<?> refType = field.getType();
        log.debug("[ Reference type: {} ]", refType);
        // get all referenced entities for select
        List<?> entities = em.createQuery("from " + refType.getName(), refType)
            .getResultList();
        model.setReferencedValue(entities);
    }


    private void processManyToMany(Field field, FormModel model) {
        Class<?> referencedType = getCollectionAttribute(field);
        Class<?> clazz = getImplClass(field);
        assert referencedType != null;
        log.debug("[ Collection referenced type: {} ]", referencedType.getName());

        List<?> entities = em.createQuery("from " + referencedType.getName(),
            referencedType).getResultList();

        model.setReferencedValue(entities);
        model.setReferencedType(referencedType);
        model.setCollectionType(clazz.getCanonicalName());
    }


    public List<FormModel> childFormModel(Class<?> clazz) {
        return
            META_MAPPING.get(clazz);
    }


    public void addChild(Class<?> type) {
        log.debug("addChild called");
        childFormModelsList.add(childFormModel(type));
    }


    public void menuChange(ActionEvent event) {
        try {
            MenuItem item = (MenuItem) event.getSource();
        }
        catch (Exception e) { /* ignore */ }
    }


    public void search() {
        switch (getSearchOption()) {
            case "searchId":
                E entity = getService().findByID(Id);
                if (entity != null)
                    values = Arrays.asList(entity);
                else
                    values = Collections.emptyList();
                break;
            case "searchCreatedDate":
                values = getService().findByCreatedDateRange(searchDateFrom, searchDateTo);
                break;
            case "searchDisplayAll":
                values = getEntities();
                break;
            default:
                doSearch(getSearchOption()); // delegate to subclass
                break;
        }
    }

    // ---------------------------------------------------------- crud operations
    public void view() {
        if (isSelected()) {
            setCurrentEntity(getCurrentSelected());
            setPage("detail");
        }
        else {
            addMsg(NO_SELECTION);
        }
    }


    public void edit() {
        if (isSelected()) {
            edit(getCurrentSelected());
            setPage("edit");
        }
        else {
            addMsg(NO_SELECTION);
        }
    }


    protected void edit(E e) {
        setCurrentEntity(e);
        if (log.isDebugEnabled())
            log.debug(EDIT_LOG, e);
    }


    public void save(E e) {
        getService().save(e);
        addMsg(format(CREATE_MSG, getEntityName()));
    }


    public String update(E e) {
        getService().update(e);
        addMsg(format(UPDATE_MSG, getEntityName(), e.getId()));
        return null;
    }


    public void delete() {
        if (isSelected()) {
            delete(getCurrentSelected());
        }
        else {
            addMsg(NO_SELECTION);
        }
    }


    public void delete(E e) {
        try {
            getService().deleteById(e.getId());
            values.remove(e);
            addMsg(format(DELETE_MSG, getEntityName(), e.getId()));
        }
        catch (EJBException ex) {
            addMsg(ex.getCause().toString());
        }
    }

    // ---------------------------------------------------------- private methods
    @SuppressWarnings("unchecked")
    private <T extends BaseEntity<?>> Class<T> getCollectionAttribute(Field field) {
        EntityType<?> entityType = em.getMetamodel().entity(getEntityClass());
        Attribute<?, ?> attribute = entityType.getAttribute(field.getName());
        if (attribute.isCollection()) {
            PluralAttribute<?, ?, T> pa = (PluralAttribute<?, ?, T>) attribute;
            return
                pa.getElementType().getJavaType();
        }
        return null;
    }

    // get the actual collection implementation class of the field
    private Class<?> getImplClass(Field field)
    throws FacesException {
        try {
            Object entity = Class.forName(getEntityClass().getName()).newInstance();
            field.setAccessible(true);
            Class<?> implClazz = field.get(entity).getClass();
            log.debug("[ entity: {}, getImplClass: {} ]", entity.getClass(),
                implClazz);
            field.setAccessible(false);
            return
                implClazz;
        }
        catch (Exception e) {
            throw new FacesException(e);
        }
    }

    // sort fields annotated with @ViewMeta using its order field
    private List<Field> sortFields(List<Field> fields) {
        fields.sort(Comparator.comparingInt(o ->
            o.getAnnotation(ANNOTATED_CLASS).order()));
        return
            fields;
    }

    // ---------------------------------------------------------- helper methods
    // split JavaBean style string
    private static String splitCamelCase(String value) {
        return capitalize(join(" ", splitByCharacterTypeCamelCase(value)));
    }

    // get list of fields with the specified annotation
    public static List<Field> getAnnotatedFields(final Class<?> clazz,
        final Class<? extends Annotation> aClazz) {
        List<Field> list = new ArrayList<>();
        for (Field field : getClassFields(clazz)) {
            if (field.getAnnotation(aClazz) != null)
                list.add(field);
        }
        return list;
    }

    // get all of the class object's declared fields including inherited fields
    public static List<Field> getClassFields(final Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> node = clazz;
        while(node != Object.class || node.getSuperclass() != null) {
            fields.addAll(Arrays.asList(node.getDeclaredFields()));
            node = node.getSuperclass();
        }
        return fields;
    }

    // TODO: make it a utility method
    public void addMsg(String msg) {
        ec.getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(msg);
        fc.addMessage(null, message);
    }

    // ---------------------------------------------------------- setters and getters
    public List<List<FormModel>> getChildFormModelsList() {
        return childFormModelsList;
    }

    public I getId() {
        return Id;
    }

    public void setId(I Id) {
        this.Id = Id;
    }

    private Class<?> getEntityClass() {
        return entityClass;
    }

    private String getEntityName() {
        return
            entityClass.getSimpleName().toLowerCase();
    }

    public List<E> getValues() {
        return values;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
    }

    public LocalDate getSearchDateTo() {
        return searchDateTo;
    }

    public void setSearchDateTo(LocalDate searchDateTo) {
        this.searchDateTo = searchDateTo;
    }

    public LocalDate getSearchDateFrom() {
        return searchDateFrom;
    }

    public void setSearchDateFrom(LocalDate searchDateFrom) {
        this.searchDateFrom = searchDateFrom;
    }

    public List<E> getEntities() {
        return entities;
    }

    public List<FormModel> getAttributeNames() {
        // value should be mapped since it was loaded in postconstruct
        return
            Collections.unmodifiableList(META_MAPPING.get(getEntityClass()));
    }

    public List<ColumnModel> getColumnList() {
        return columnList;
    }

    public List<FormModel> getFormFields() {
        return formFields;
    }

    public E getCurrentEntity() {
        return currentEntity;
    }

    public void setCurrentEntity(E currentEntity) {
        this.currentEntity = currentEntity;
    }

    public E getCurrentSelected() {
        return this.currentSelected;
    }

    public void setCurrentSelected(E currentSelected) {
        this.currentSelected = currentSelected;
    }

    public boolean isSelected() {
        return this.currentSelected != null;
    }

    public List<SearchModel.SelectModel> getSearchOptions() {
        return this.searchOptions;
    }

    public List<SearchModel.Field> getSearchFields() {
        return this.searchFields;
    }


}
