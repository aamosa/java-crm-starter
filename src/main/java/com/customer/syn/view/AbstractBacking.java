package com.customer.syn.view;

import static java.lang.String.join;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.validation.constraints.PastOrPresent;

import org.primefaces.model.menu.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.component.ColumnModel;
import com.customer.syn.model.BaseEntity;
import com.customer.syn.model.ViewMeta;
import com.customer.syn.service.BaseRepositoryImpl;


/** Base backing class with common functionality */
public abstract class AbstractBacking<E extends BaseEntity<I>, I extends Number> {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject private FacesContext fc;
    @Inject private ExternalContext ec;
    @Inject private SearchManager searchManager;

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
    protected List<ColumnModel> tableColumns;
    // protected Map<String, String> formFields;
    protected List<String> formFields;
    protected Map<String, String> attributeNames;
    protected List<SearchModel.Field> searchFields;
    protected List<SearchModel.SelectModel> searchOptions;

    private static final Class ANNOTATED_CLASS = ViewMeta.class;
    private static final String EDIT_LOG = "[edit invoked, entity = {}]";
    private static final String UPDATE_MSG = "%s with Id: %d has been updated.";
    private static final String DELETE_MSG = "%s with Id: %d has been deleted.";
    protected static final String NO_SELECTION = "Please make a selection first.";
    private static final String CREATE_MSG = "New %s has been created successfully.";

    
    // ---------------------------------------------------------- constructors
    protected AbstractBacking() {
        try {
            ParameterizedType type = (ParameterizedType) this.getClass()
                    .getGenericSuperclass();
            this.entityClass = (Class<?>) type.getActualTypeArguments()[0];
            if (log.isDebugEnabled()) {
                log.debug("[{} constructor initialized]", getClass().getSimpleName());
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
    protected abstract <R extends BaseRepositoryImpl<E, I>> R getService();

    protected abstract void doSearch(String value);


    @PostConstruct
    public void setup() {
        entities = getService().fetchAll(); // TODO: add paging and lazy loading
        searchOptions = searchManager.getSearchOptions(getEntityName());
        searchFields = searchManager.getSearchFields(getEntityName());

        attributeNames = attributesMapping(getEntityClass(), ANNOTATED_CLASS);
        formFields = formFieldPropertyNames(getEntityClass(), ANNOTATED_CLASS);

        log.debug("[ attributesNames = {} ]", attributeNames.toString());
        log.debug("[ formFields = {} ]", formFields);

        createTableColumns(getEntityClass());
        setPage("list");
    }


    public void menuChange(ActionEvent event) {
        try {
            MenuItem item = (MenuItem) event.getSource();
            log.debug("[value = {}]", item.getValue());
        }
        catch (Exception e) {
            log.error("{}", e);
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
        getService().deleteById(e.getId());
        values.remove(e);
        addMsg(format(DELETE_MSG, getEntityName(), e.getId()));
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
                values = getService().findByCreatedDateRange(searchDateFrom, searchDateTo);
                break;
            case "searchDisplayAll":
                values = getEntities();
                break;
            default:
                // delegate to subclass for entity specific search
                doSearch(getSearchOption());
                break;
        }
    }


    // ---------------------------------------------------------- private methods
    private Class<?> getEntityClass() {
        return entityClass;
    }

    private String getEntityName() {
        return entityClass.getSimpleName().toLowerCase();
    }


    private void createTableColumns(final Class<?> bean) {
        tableColumns = new ArrayList<>();
        for (Map.Entry<String, String> e : getAttributeNames().entrySet()) {
            tableColumns.add(new ColumnModel(e.getValue(), e.getKey()));
        }
    }


    private List<String> formFieldPropertyNames(final Class<?> clazz,
                                                final Class<ViewMeta> aclazz) {
        return getAnnotatedFields(clazz, aclazz).stream()
                .filter(f -> f.getAnnotation(aclazz).formField())
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    // create an object attribute name mappping with user-friendly names
    private Map<String, String> attributesMapping(final Class<?> clazz,
                                                  final Class<? extends Annotation> aclazz) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Field field : sortFields(getAnnotatedFields(clazz, aclazz))) {
            map.put(field.getName(), capitalize(join(" ",
                    splitByCharacterTypeCamelCase(field.getName()))));
        }
        return map;
    }

    // sort fields annotated with @ViewMeta using it's order field TODO:
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
    public static List<Field> getAnnotatedFields(final Class<?> clazz,
                                                 final Class<? extends Annotation> aClazz) {
        List list = new ArrayList<>();
        for (Field field : getClassFields(clazz)) {
            if (field.getAnnotation(aClazz) != null) {
                list.add(field);
            }
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

    public Map<String, String> getAttributeNames() {
        return attributeNames;
    }

    public List<ColumnModel> getTableColumns() {
        return tableColumns;
    }

    public List<String> getFormFields() {
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
