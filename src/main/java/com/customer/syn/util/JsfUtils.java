package com.customer.syn.util;

public class JsfUtils {
    
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
}
