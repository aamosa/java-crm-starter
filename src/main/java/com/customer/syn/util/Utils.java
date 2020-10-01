package com.customer.syn.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.syn.model.BaseEntity;


public final class Utils {
    
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    private Utils() { /* prevent instantiation :p */ }

    
    // ------------------------------------------------------- util methods
    public static List<String> fieldNames(Class<?> clazz) {
        List<String> fields = new ArrayList<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals("class"))
                    continue;
                if (pd.getReadMethod() != null) {
                    Field field = getField(clazz, pd.getName());
                    fields.add(field.getName());
                }
            }
        } 
        catch (IntrospectionException 
                | SecurityException 
                | NoSuchFieldException e) {
            log.error("{}", e);
        }
        return fields;
    }

    
    public static String[] nonFinalorNonStaticFieldnames(Class<?> bean) {
        Field[] fields = bean.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        int i = 0;
        for (Field field : fields) {
            if (!Modifier.isFinal(field.getModifiers()) 
                    && !Modifier.isStatic(field.getModifiers())) {
                fieldNames[i] = field.getName();
                i++;
            }
        }
        return fieldNames;
    } 

    
    public static Field getField(Class<?> clazz, String name) 
            throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        } 
        catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return superClass.getDeclaredField(name);
            }
        }
    }

    
    public static ArrayList<String> fieldNamesList(Class<?> clazz, EntityManager em) {
        ArrayList<String> list = new ArrayList<>();
        Metamodel metamodel = em.getMetamodel();
        ManagedType<?> type = metamodel.managedType(clazz);
        
        for (Attribute<?, ?> attr : type.getAttributes()) {
            list.add(attr.getName());
        }
        return list;
    }


    public static <T extends BaseEntity<Number>, I> T findInListById(List<T> list, I Id) {
        return list.stream().filter(i -> i.getId().equals(Id)).findFirst().orElse(null);
    }


    public static <T, ID> T findById(Class<T> type, ID id, String name) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(name);
            em = emf.createEntityManager();
            return em.find(type, id);
        } 
        catch (Exception e) {
            throw new PersistenceException(e.getCause());
        } 
        finally {
            if (em.isOpen()) {
                try {
                    em.close();
                } catch (Exception e) { /* ignore */ }
            }
            if (emf.isOpen()) {
                try {
                    emf.close();
                } catch (Exception e) { /* ignore */ }
            }
        }
    }
    
    
    /*
        public void getBeanFromEL() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ContactBacking bean = facesContext.getApplication()
                .evaluateExpressionGet(facesContext, "#{contactBacking}", ContactBacking.class);
            log.info("Bean is: " + bean);
            Map map = facesContext.getExternalContext().getRequestMap();
            Map viewMap = facesContext.getViewRoot().getViewMap();
            viewMap.forEach((k, v) -> log.info("key: " + k + " value: " + v));
        }
    */
    
}
