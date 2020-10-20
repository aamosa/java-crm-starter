package com.customer.syn.view;

import com.customer.syn.model.BaseEntity;
import com.customer.syn.model.FormInputType;
import com.customer.syn.model.Role;
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

import static com.customer.syn.model.FormInputType.TEXT;
import static com.customer.syn.model.FormInputType.TEXTBOX;
import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

/** Base backing class with common functionality */
public abstract class AbstractBacking<E extends BaseEntity<I>, I extends Number> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject private FacesContext fc;
    @Inject private ExternalContext ec;
    @Inject private SearchManager searchManager;
    @PersistenceContext private EntityManager em;

    protected I Id;
    protected E currentEntity;
    protected E currentSelected;
    private final Class<?> entityClass;

    protected String page;
    protected String searchOption;
    protected LocalDate searchDateTo;
    @PastOrPresent protected LocalDate searchDateFrom;

    protected List<E> values;
    protected List<E> entities;
    protected List<FormModel> formFields;
    protected List<ColumnModel> columnList;
    protected List<SearchModel.Field> searchFields;
    protected List<SearchModel.SelectModel> searchOptions;

    private static final Class ANNOTATED_CLASS = ViewMeta.class;
    private static final String EDIT_LOG = "[edit invoked, entity = {}]";
    private static final String UPDATE_MSG = "%s with Id %d has been updated.";
    private static final String DELETE_MSG = "%s with Id %d has been deleted.";
    protected static final String NO_SELECTION = "Please make a selection first.";
    private static final String CREATE_MSG = "New %s has been created successfully.";

    private static final Map<Class<?>, List<FormModel>> META_MAPPING = new ConcurrentHashMap<>();

    // ---------------------------------------------------------- constructors
    protected AbstractBacking() {
        try {
            ParameterizedType type = (ParameterizedType) this.getClass()
                    .getGenericSuperclass();
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

    @PostConstruct
    public void setup() {
        // TODO: add paging and lazy loading
        entities = getService().fetchAll();
        // backing data for entity specific search menu
        searchOptions = searchManager.getSearchOptions(getEntityName());
        searchFields = searchManager.getSearchFields(getEntityName());
        // backing data for form fields
        formFields = setFormFields();
        // dynamic columns for datatable component
        columnList = setColumns();
        setPage("list");
        log.debug("<< formfields: >>: {}", formFields);
        // log.debug("META_MAPPING {}", META_MAPPING);
    }

    private List<FormModel> setFormFields() {
        List<FormModel> list = META_MAPPING.computeIfAbsent(getEntityClass(),
            k -> createModel(k, ANNOTATED_CLASS));
        return list.stream().filter(f -> f.isField())
            .collect(Collectors.toList());
    }

    private List<ColumnModel> setColumns() {
        List<ColumnModel> list = new ArrayList<>();
        for (FormModel model : META_MAPPING.get(getEntityClass())) {
            // trust me
            list.add(new ColumnModel(model.getLabel(), (String) model.getValue()));
        }
        return list;
    }

    private List<FormModel> createModel(Class<?> clazz, Class<ViewMeta> annotated) {
        List<FormModel> list = new ArrayList<>();
        for (Field f : sortFields(getAnnotatedFields(clazz, annotated))) {
            FormModel model = new FormModel();
            model.setLabel(splitCamelCase(f.getName()));
            model.setValue(f.getName());
            model.setField(f.getAnnotation(ViewMeta.class).formField());
            if (f.getAnnotation(ViewMeta.class).formField()) {
                // create form field for property
                processInputType(f, model);
            }
            list.add(model);
        }
        return list;
    }

    private void processInputType(Field field, FormModel model) {
        FormInputType type = field.getAnnotation(ViewMeta.class).type();
        if (EnumSet.<FormInputType>range(TEXT, TEXTBOX).contains(type)) {
            // simple property
            model.setType(type);
        }
        else {
            // collection-valued property
            model.setType(type);
            processCollectionType(field, model);
        }
    }

    private void processCollectionType(Field field, FormModel model) {
        if (field.getAnnotation(ManyToOne.class) != null) {
            // ManyToOne
            processManyToOne(field, model);
        }
        else if (field.getAnnotation(ManyToMany.class) != null) {
            // ManyToMany collection-valued property
            processManyToMany(field, model);
        }
        else if (field.getAnnotation(ElementCollection.class) != null) {
            // TODO: parent / child embeddables collection
        }
        else if (field.getAnnotation(OneToMany.class) != null) {
            // TODO: parent / child entities collection
        }
    }

    private void processManyToOne(Field field, FormModel model) {
        Class<?> type = field.getType();
        log.debug("reference type: {}", type);
        // get all referenced entities for select
        List list = em.createQuery("from " + type.getName(), type).getResultList();
        model.setReferencedValue(list);
    }

    private void processManyToMany(Field field, FormModel model) {
        Class<?> collectionFieldType = getCollectionAttribute(field);
        Class<?> clazz = getImplClass(field);
        // Set entitySet =  getService().entitySet(collectionFieldType);
        List list = em.createQuery("from " + collectionFieldType.getName(), collectionFieldType)
            .getResultList();
        model.setCollectionType(clazz.getCanonicalName());
        model.setReferencedValue(list);
        log.debug("[collection field: {}]", collectionFieldType);
        // testing only
        // Iterator it = set.iterator();
        // while (it.hasNext()) {
        //     Object o = it.next();
        //     log.debug("set object: {}", o);
        //     Role role = (Role) o;
        //     log.debug("after downcasting: {}", role.getId());
        // }
    }


    public void menuChange(ActionEvent event) {
        try {
            MenuItem item = (MenuItem) event.getSource();
        } catch (Exception e) { /* ignore */ }
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

    public void search() {
        switch (getSearchOption()) {
            case "searchId":
                E entity = getService().findByID(Id);
                if (entity != null)
                    values = Arrays.asList(entity);
                else
                    values = Collections.EMPTY_LIST;
                break;
            case "searchCreatedDate":
                values = getService().findByCreatedDateRange(searchDateFrom,
                    searchDateTo);
                break;
            case "searchDisplayAll":
                values = getEntities();
                break;
            default:
                doSearch(getSearchOption()); // delegate to subclass
                break;
        }
    }

    // ---------------------------------------------------------- private methods
    private <T extends BaseEntity> Class<T> getCollectionAttribute(Field field) {
        EntityType<?> type = em.getMetamodel().entity(getEntityClass());
        Attribute<?, ?> attribute = type.getAttribute(field.getName());
        if (attribute.isCollection()) {
            PluralAttribute pa = (PluralAttribute) attribute;
            return pa.getElementType().getJavaType();
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
            if (log.isDebugEnabled()) {
                log.debug("[entity: {} field implementation class: {}]", entity.getClass(),
                    implClazz);
            }
            field.setAccessible(false);
            return implClazz;
        }
        catch (Exception e) {
            throw new FacesException(e);
        }
    }

    // sort fields annotated with @ViewMeta using its order field TODO:
    private List<Field> sortFields(List<Field> list) {
        Collections.sort(list, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                ViewMeta  s1 = o1.getAnnotation(ViewMeta.class);
                ViewMeta s2 = o2.getAnnotation(ViewMeta.class);
                if (s1 != null && s2 != null)
                    return Integer.compare(s1.order(), s2.order());
                else
                    return 0;
            }
        });
        return list;
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
        return entityClass.getSimpleName().toLowerCase();
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
        return Collections.unmodifiableList(META_MAPPING.get(getEntityClass()));
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
