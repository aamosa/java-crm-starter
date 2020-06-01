package com.customer.syn.view;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.customer.syn.resource.model.BaseEntity;
import com.customer.syn.resource.model.Contact;
import com.customer.syn.resource.model.Sort;
import com.customer.syn.resource.model.User;
import com.customer.syn.service.BaseRepositoryImpl;

public abstract class AbstractBacking<E extends BaseEntity<T>, T extends Number> {

    protected T Id;
    protected String firstName;
    protected String lastName;
    protected String page;
    protected String searchOption;
    protected LocalDate searchDateTo;
    protected LocalDate searchDateFrom;
    
    protected List<E> values;
    protected List<E> entities;
    
    protected String[] fieldNames;
    protected List<String> fNames;
    
    @Inject
    private FacesContext facesContext;
    
    @Inject
    private ExternalContext ec;
    
    
    // ---------------------------------------------- constructors
    
    public AbstractBacking() { }
    
    
    @PostConstruct
    public void setup() {
        entities = getService().fetchAll();
        // fieldNames = getFieldNames(Contact.class); // TESTING :p
        // fNames = fieldNames(Contact.class);
        fNames = getAttributeNames(Contact.class);
        setPage("list");
    }

    
    protected abstract BaseRepositoryImpl<E, T> getService();
    

    /** Save */
    public String save(E entity) {
        getService().save(entity);
        return null;
    }
    
    
    /** Update */
    public void update(E e) {
        getService().update(e);
        addMsg("ID #: " + e.getId() + " has been Updated.");
    }
    
    
    /** Delete */
    public void delete(E e) {
        getService().deleteById(e.getId());
        values.remove(e);
        addMsg("ID #: " + e.getId() + " has been Deleted.");
    }
    
    
    /** Search */
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
        
        if (values == null || values.size() < 1 ) {
            addMsg("No records found.");
        }
    }
    
    
    /** FacesMessae */
    public void addMsg(String msg) {
        ec.getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(msg);
        facesContext.addMessage(null, message);
    }
    
    
    
    public List<String> fieldNames(Class<?> clazz) {
        // List<>
        List<String> fields = new ArrayList<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors(); 
            
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals("class")) continue;
                if (pd.getReadMethod() != null) {
                    Field field = getField(clazz, pd.getName());
                    fields.add(field.getName());
                }
            }
                // Field field = clazz.getDeclaredField(pd.getReadMethod().get);
//                if (field.getAnnotation(Sort.class) != null) {
//                    fields.add(pd.getReadMethod().getName());
//                }
            
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return fields;
    }
    
    
    public Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return superClass.getDeclaredField(name);
            }
        }
    }
    
    
    public List<String> sortFieldNames(final List<String> fields, Class<?> clazz) {
        Class<?> start = clazz;
        List<String> list = new ArrayList<>();
        while(start.getSuperclass() != Object.class || start != null) {
            start = start.getSuperclass();
        }
        return null;
    }
    
    
    
    public List<String> getAttributeNames(Class<?> bean) {
        List<String> fieldNames = new ArrayList<>();
        Class<?> node = bean;
        List<Field> fields = new ArrayList<>();
        while(node != Object.class || node.getSuperclass() != null) {
            fields.addAll(Arrays.asList(node.getDeclaredFields()));
            node = node.getSuperclass();
        }
        
        for (Field f : fields) {
            if (f.getAnnotation(Sort.class) != null) {
                fieldNames.add(f.getName());
            }
        }
        return fieldNames;
    }
    

//    public String[] getFieldNames(Class<?> bean) {
//        Field[] fields = bean.getDeclaredFields();
//        String[] fieldNames = new String[fields.length];
//        int i = 0;
//        
//        for (Field field : fields) {
//            if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
//                fieldNames[i] = field.getName();
//                i++;
//            }
//        }
//        return fieldNames;
//    }
    
    
    
    public ArrayList<String> getFields() {
        return getService().fieldNamesList();
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }


    public List<String> getfNames() {
        return fNames;
    }


    public String[] getFieldNames() {
        return fieldNames;
    }

}