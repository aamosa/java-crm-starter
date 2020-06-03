package com.customer.syn.view;

import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;

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
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.customer.syn.resource.model.BaseEntity;
import com.customer.syn.resource.model.ViewMeta;
import com.customer.syn.service.BaseRepositoryImpl;

public abstract class AbstractBacking<E extends BaseEntity<T>, T extends Number> {
    
    private static final Logger log = Logger.getLogger(AbstractBacking.class.getName());

    protected T Id;
    protected String firstName;
    protected String lastName;
    protected String page;
    protected String searchOption;
    protected LocalDate searchDateTo;
    protected LocalDate searchDateFrom;
    
    protected List<E> values;
    protected List<E> entities;
    protected List<ColumnModel> tableColumns;
    
    protected Map<String, String> formFields;
    protected Map<String, String> attributeNames;
    
    protected E currentEntity;
    
    @Inject
    private FacesContext facesContext;
    
    @Inject
    private ExternalContext ec;
    
    
    // ---------------------------------------------- constructors
    
    public AbstractBacking() { }
    
    protected abstract BaseRepositoryImpl<E, T> getService();

    
    @PostConstruct
    public void setup() {
        log.info("AbstractBacking PostConstruct invoked.");
        entities = getService().fetchAll();
        attributeNames = getViewMeta(getChildClass());
        formFields = getFormFieldsMap(getChildClass()); 
        createTableColumns(getChildClass());
        setPage("list");
        log.info("formFields : " + formFields.toString());
    }
    

    public String save(E entity) {
        getService().save(entity);
        return null;
    }
    
    
    public void update(E e) {
        getService().update(e);
        addMsg("ID #: " + e.getId() + " has been Updated.");
    }
    
    
    public void delete(E e) {
        getService().deleteById(e.getId());
        values.remove(e);
        addMsg("ID #: " + e.getId() + " has been Deleted.");
    }
    
    
    public void addMsg(String msg) {
        ec.getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(msg);
        facesContext.addMessage(null, message);
    }
    
    
    public void initDetail(E entity) {
        log.info("initDetail invoked entity is: " + entity);
        setCurrentEntity(entity);
    }
    
    
    public Class<?> getChildClass() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }
    
    
    public void search() {
        switch (searchOption) {
        case "searchByName":
            if (!firstName.trim().isEmpty() && !lastName.trim().isEmpty())
                values = getService().findByFullName(firstName.toUpperCase(), lastName.toUpperCase());
            else
                values = getService().findByLastName(lastName.toUpperCase());
            break;
        case "searchByID":
            values = null;
            E entity = getService().findByID(Id).isPresent() ? getService().findByID(Id).get() : null;
            if (entity != null) values = new ArrayList<>(Arrays.asList(entity));
            break;
        case "fetchAll":
            values = entities;
            break;
        case "searchByDate":
            values = getService().findByDateRange(searchDateFrom, searchDateTo);
            break;
        }
        
        if (values == null || values.size() < 1 )
            addMsg("No records found.");
    }
    
 
    public void createTableColumns(final Class<?> bean) {
        tableColumns = new ArrayList<>();
        for (Map.Entry<String, String> e : getAttributeNames().entrySet()) {
            tableColumns.add(new ColumnModel(e.getValue(), e.getKey()));
        }
    }
    
    
    
    // ---------------------------------------------- private methods
    
    private Map<String, String> getFormFieldsMap(final Class<?> bean) {
        Map<String, String> map = attributeNames;
        return getViewMetaforForm(bean).stream()
                                       .filter(map::containsKey)
                                       .collect(Collectors.toMap(Function.identity(), map::get, (u, v) -> {
                                           throw new IllegalStateException();
                                           }, LinkedHashMap::new));
    }
    
    
    private static Map<String, String> getViewMeta(final Class<?> bean) {
        List<Field> fields = getSortedFields(bean);  
        Map<String, String> map = new LinkedHashMap<>();
        for (Field f : fields) {
            if (f.getAnnotation(ViewMeta.class) != null) {
                map.put(f.getName(), capitalize(join(" ", splitByCharacterTypeCamelCase(f.getName()))));
            }
        }
        return map;
    }
    
    
    private static List<String> getViewMetaforForm(final Class<?> bean) {
        List<Field> fields = getSortedFields(bean);
        List<String> formFields = new ArrayList<>();
        
        for (Field f : fields) {
            ViewMeta meta = f.getAnnotation(ViewMeta.class);
            if (meta != null && meta.formField()) {
                formFields.add(f.getName());
            }
        }
        return formFields;
    }
    
    
    private static List<Field> getSortedFields(final Class<?> bean) {
        Class<?> node = bean;
        List<Field> fields = new ArrayList<>();
        while(node != Object.class || node.getSuperclass() != null) {
            fields.addAll(Arrays.asList(node.getDeclaredFields()));
            node = node.getSuperclass();
        }
        
        Collections.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                ViewMeta s1 = o1.getAnnotation(ViewMeta.class);
                ViewMeta s2 = o2.getAnnotation(ViewMeta.class);
                if (s1 != null && s2 != null)
                    return Integer.compare(s1.order(), s2.order());
                else
                    return 0;
            }
        });
        return fields;
    }
    
    
    
    // ---------------------------------------------- setters and getters
    
    public T getId() {
        return Id;
    }

    public void setId(T Id) {
        this.Id = Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public List<E> getValues() {
        return values;
    }

    public List<E> getEntities() {
        return entities;
    }

    public Map<String, String> getAttributeNames() {
        return attributeNames;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<ColumnModel> getTableColumns() {
        return tableColumns;
    }

    public Map<String, String> getFormFields() {
        return formFields;
    }

    public E getCurrentEntity() {
        return currentEntity;
    }

    public void setCurrentEntity(E currentEntity) {
        this.currentEntity = currentEntity;
    }

}