package com.customer.syn.view;

import static com.customer.syn.view.SearchModel.SelectModel.BASE_CLASS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class SearchManager {

    private static final Logger log = LoggerFactory.getLogger(SearchManager.class);

    @PostConstruct
    public void init() {
        SearchParser parser = new SearchParser("/searchfields.xml");
        try {
            // parse config file and create search model
            parser.parse();

            log.debug("[{} postconstruct initialize]", getClass().getSimpleName());

            // Debugging
            log.debug("[selectMap size = {}]", SearchParser.selectMap.size());
            for (SearchModel.SelectModel s : getSearchOptions(BASE_CLASS))
                log.debug("[base search option, class = {} value = {}]", s.getClazz(), s.getValue());

            for (SearchModel.Field f : getSearchFields(BASE_CLASS))
                log.debug("[base search field, name = {} value = {} renderFor = {}]", f.getName(), f.getValue(),
                        f.getRenderFor());
        }
        catch (Exception e) {
            log.error("{}", e);
        }
    }

    public List<SearchModel.SelectModel> getSearchOptions(String className) {
        // Debugging
        if (log.isDebugEnabled()) {
            log.debug("[getSearchOptions className = {}]", className);
        }
        return SearchParser.selectMap.get(className.toLowerCase());
    }


    public List<SearchModel.Field> getSearchFields(String className) {
        List<SearchModel.SelectModel> list = SearchParser.selectMap.get(className.toLowerCase());
        return list.stream().filter(s -> s.getRenderFields().size() > 0)
                .map(SearchModel.SelectModel::getRenderFields).flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public SearchModel.DataType[] getDataTypes() {
        return SearchModel.DataType.values();
    }

    // public Map<String, String> getSearchOptions(String className) {
    //
    //    Map<String, String> bso = new LinkedHashMap<>();
    //    for (SearchModel.SelectModel sm : SearchParser.selectList) {
    //        if (className == null) {
    //            if (sm.getClazz() == null)
    //                bso.put(sm.getLabel(), sm.getValue());
    //        }
    //        else {
    //
    //        }
    //    }
    //    return bso;
    //}

    //public List<SearchModel.Field> getSearchFields(String className) {
    //    List<SearchModel.Field> fields = new ArrayList<>();
    //    for (SearchModel.SelectModel sm : SearchParser.selectList) {
    //        if (sm.getClazz() == null
    //                && sm.getRenderFields().size() > 0) {
    //            for (SearchModel.Field f : sm.getRenderFields())
    //                fields.add(f);
    //        }
    //    }
    //    return fields;
    //}

    //public List<FormField> setUpBaseSearchFields() {
    //    List<FormField> searchFields = new ArrayList<>();
    //    searchFields.add(new FormField("First Name", "firstName", "firstName", "text", "searchByName", true));
    //    searchFields.add(new FormField("Last Name", "lastName", "lastName", "text", "searchByName", true));
    //    searchFields.add(new FormField("From", "fromDate", "searchDateFrom", "date", "searchByDate"));
    //    searchFields.add(new FormField("To", "toDate", "searchDateTo", "date", "searchByDate"));
    //    searchFields.add(new FormField("ID", "id", "id", "number", "searchByID"));
    //    return searchFields;
    // }


    // -------------------------------------------------- nested classes

    // ---------------------------------------------------------- private nested class
    private static class SearchParser {
        private static final String TYPE = "type";
        private static final String LABEL = "label";
        private static final String VALUE = "value";
        private static final String FIELD = "field";
        private static final String SELECT = "select";
        private static final String DEFAULT = "default";
        private static final String ENTITY_CLASS = "entityClass";
        private static final Map<String, List<SearchModel.SelectModel>> selectMap = new HashMap<>();

        private final String file;
        private final XMLInputFactory xif;


        // ------------------------------------------------------ constructor
        public SearchParser(String file) {
            this.file = file;
            xif = XMLInputFactory.newInstance();
        }


        public void parse() throws IOException, XMLStreamException {
            Deque<String> stack = new ArrayDeque<>();
            SearchModel.SelectModel selectModel = null;
            try (InputStream stream = this.getClass().getResourceAsStream(file)) {
                XMLEventReader reader = xif.createXMLEventReader(stream);
                while (reader.hasNext()) {  // parse the tags
                    XMLEvent event = reader.nextEvent();
                    if (event.isStartElement()) {
                        StartElement element = event.asStartElement();
                        String name = element.getName().getLocalPart();
                        if (name.equals(SELECT)) {
                            selectModel = parseSelect(element);
                            if (selectMap.containsKey(selectModel.getClazz())) {
                                selectMap.get(selectModel.getClazz()).add(selectModel);
                            }
                            else {
                                List<SearchModel.SelectModel> list = new ArrayList<>();
                                list.add(selectModel);
                                selectMap.put(selectModel.getClazz(), list);
                            }
                            stack.push(name);
                        }
                        else if (name.equals(FIELD)) {
                            if (stack.size() > 0) {
                                SearchModel.Field f = parseField(element);
                                selectModel.addFieldToRender(f);
                                f.setRenderFor(selectModel.getValue());
                            }
                        }
                    }
                    if (event.isEndElement()
                            && event.asEndElement().getName().getLocalPart()
                                .equals(SELECT)) {
                        if (stack.size() > 0) { stack.poll(); }
                    }
                }
            }
        }


        private SearchModel.SelectModel parseSelect(final StartElement tag) {
            String label = getAttrValue(tag, LABEL);
            String entity = getAttrValue(tag, ENTITY_CLASS);
            SearchModel.SelectModel select = new SearchModel.SelectModel(label);
            if (entity != null) {
                select.setClazz(entity);
            }
            if (log.isDebugEnabled()) {
                log.debug("[label = {} entityclass = {}]", label, entity);
            }
            return select;
        }


        private SearchModel.Field parseField(final StartElement tag) {
            String label, value, type;
            boolean isDefault = false;
            label = value = type =  null;
            Iterator<Attribute> attrs = tag.getAttributes();
            while (attrs.hasNext()) {
                Attribute attr = attrs.next();
                switch (attr.getName().toString()) {
                    case LABEL:
                        label = attr.getValue();
                        break;
                    case VALUE:
                        value = attr.getValue();
                        break;
                    case TYPE:
                        type = attr.getValue();
                        break;
                    case DEFAULT:
                        isDefault = Boolean.parseBoolean(attr.getValue());
                        break;
                }
            }
            SearchModel.Field field = new SearchModel.Field(value, label,
                    SearchModel.DataType.valueOf(type.toUpperCase()), isDefault);
            if (log.isDebugEnabled()) {
                log.debug("[label = {} value = {} type = {} default = {}]",
                        label, value, type, isDefault);
            }
            return field;
        }

        // helper method
        private static String getAttrValue(StartElement tag, String value) {
            Attribute a = tag.getAttributeByName(QName.valueOf(value));
            return a != null ? a.getValue() : null;
        }

    }

}
