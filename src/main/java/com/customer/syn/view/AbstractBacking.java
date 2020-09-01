package com.customer.syn.view;

import static java.lang.String.join;
import static java.lang.String.format;
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
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.model.BaseEntity;
import com.customer.syn.model.ViewMeta;
import com.customer.syn.service.BaseRepositoryImpl;


public abstract class AbstractBacking<E extends BaseEntity<I>, I extends Number> {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private FacesContext fc;
    
    @Inject
    private ExternalContext ec;
    
    protected I Id;
    protected E currentEntity;
    protected E currentSelected;

    protected String firstName;
    @NotNull
    protected String lastName;
    
    protected String page;
    protected String searchOption;
    protected LocalDate searchDateTo;
    @PastOrPresent
    protected LocalDate searchDateFrom;
    
    private final Class<?> entityClass;
    
    protected List<E> values;
    protected List<E> entities;
    protected List<ColumnModel> tableColumns;
    protected Map<String, String> formFields;
    protected Map<String, String> attributeNames;
    
    private static final String NO_RECORDS = "No Records Found.";
    private static final String UPDATE_MSG = "%s with Id: %d has been updated.";
    private static final String DELETE_MSG = "%s with Id: %d has been deleted.";
    private static final String CREATE_MSG = "New %s has been created successfully.";
    
    
    // ---------------------------------------------------------- constructors
    
    protected AbstractBacking(Class<?> clazz) { 
        this.entityClass = clazz;
    }
    
    
    protected AbstractBacking() {
        try {
            ParameterizedType type = (ParameterizedType) this.getClass()
                    .getGenericSuperclass();
            this.entityClass = (Class<?>) type.getActualTypeArguments()[0];
        } 
        catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
    
    
    // ---------------------------------------------------------- abstract methods
    
    protected abstract BaseRepositoryImpl<E, I> getService();

    
    @PostConstruct
    public void setup() {
        entities = getService().fetchAll();
        attributeNames = getViewMeta(getEntityClass());
        formFields = getFormFieldsMap(getEntityClass()); 
        createTableColumns(getEntityClass());
        setPage("list");
    }
    

    public void edit(E e) {
        setCurrentEntity(e);
        if (log.isDebugEnabled())
            log.debug("edit invoked on {}", e);
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
    
    
    public void delete(E e) {
        getService().deleteById(e.getId());
        values.remove(e);
        addMsg(format(DELETE_MSG, getEntityName(), e.getId()));
    }
    
    
    public void search() {
        switch (searchOption) {
        case "searchByName":
            if (firstName != null && !firstName.trim().isEmpty() && !lastName.trim().isEmpty())
                values = getService().findByFullName(firstName.toUpperCase(), lastName.toUpperCase());
            else
                values = getService().findByLastName(lastName.toUpperCase());
            break;
        case "searchByID":
            values = null;
            E entity = getService().findByID(Id);
            if (entity != null) values = new ArrayList<>(Arrays.asList(entity));
            break;
        case "fetchAll":
            values = entities;
            break;
        case "searchByDate":
            values = getService().findByCreatedDateRange(searchDateFrom, searchDateTo);
            break;
        }
        
        if (values == null || values.size() < 1)
            addMsg(NO_RECORDS);
    }
    
    
    // ---------------------------------------------------------- private methods
    
    private String getEntityName() {
        return entityClass.getSimpleName();
    }
    
    
    private Class<?> getEntityClass() {
        return entityClass;
    }
    
    
    private void addMsg(String msg) {
        ec.getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(msg);
        fc.addMessage(null, message);
    }

    
    private void createTableColumns(final Class<?> bean) {
        tableColumns = new ArrayList<>();
        for (Map.Entry<String, String> e : getAttributeNames().entrySet()) {
            tableColumns.add(new ColumnModel(e.getValue(), e.getKey()));
        }
    }
    
    
    private Map<String, String> getFormFieldsMap(final Class<?> bean) {
        Map<String, String> map = attributeNames;
        return getViewMetaforForm(bean).stream().filter(map::containsKey)
                .collect(Collectors.toMap(Function.identity(), 
                        map::get, (u, v) -> { 
                            throw new IllegalStateException();
                            }, LinkedHashMap::new));
    }
    
    
    private static Map<String, String> getViewMeta(final Class<?> bean) {
        List<Field> fields = getSortedFields(bean);  
        Map<String, String> map = new LinkedHashMap<>();
        for (Field f : fields) {
            if (f.getAnnotation(ViewMeta.class) != null) {
                map.put(f.getName(), capitalize(join(" ", 
                        splitByCharacterTypeCamelCase(f.getName()))));
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
    

    
    // ---------------------------------------------------------- setters and getters
    
    public I getId() {
        return Id;
    }

    public void setId(I Id) {
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

    public E getCurrentSelected() {
        return currentSelected;
    }

    public void setCurrentSelected(E currentSelected) {
        this.currentSelected = currentSelected;
    }
    

}
