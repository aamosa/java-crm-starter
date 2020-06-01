package com.customer.syn.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    
    
    private Utils() {}
    
/**    
    public void getBeanFromEL() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ContactBacking bean = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{contactBacking}", ContactBacking.class);
        log.info("Bean is: " + bean);
        Map map = facesContext.getExternalContext().getRequestMap();
        log.info("Contains key: " + map.containsKey("bb"));
        Map map = facesContext.getViewRoot().getViewMap();
        map.forEach((k, v) -> log.info("key: " + k + " value: " + v));
    }
*/
    
//  FacesContext facesContext = FacesContext.getCurrentInstance();
//  Map m = facesContext.getExternalContext().getRequestMap();
//  Map<String, Object> pMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
//  String val = (String) pMap.get("bean");
//  log.info("request map : " + val);
    
//  UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent("ciform");
//  comp.setRendered(false);
    
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
        } catch (IntrospectionException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return fields;
    }
    
    
    
    public static String[] nonFinalorNonStaticFieldnames(Class<?> bean) {
        Field[] fields = bean.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        int i = 0;

        for (Field field : fields) {
            if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                fieldNames[i] = field.getName();
                i++;
            }
        }
        return fieldNames;
    } 
    
    
    
    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
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
    
    
}
