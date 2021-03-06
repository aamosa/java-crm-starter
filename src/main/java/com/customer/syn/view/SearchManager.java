package com.customer.syn.view;

import com.customer.syn.model.FormInputType;
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
import java.io.InputStream;
import java.util.*;

import static com.customer.syn.view.SearchModel.SelectModel.BASE_CLASS;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class SearchManager {

	private static final Logger log = LoggerFactory.getLogger(SearchManager.class);

	@PostConstruct
	public void init() {
		// parse config and create search model
		SearchParser parser = new SearchParser("/searchfields.xml");
		if (log.isDebugEnabled()) {
			log.debug("[{} postconstruct init]", getClass());
		}
	}

	public List<SearchModel.SelectModel> getSearchOptions(String key) {
		return Collections.unmodifiableList(SearchParser.getMapping().get(key));
	}

	public List<SearchModel.Field> getSearchFields(String key) {
		List<SearchModel.SelectModel> list = SearchParser.getMapping().get(key);
		return
			list.stream().filter(s -> s.getRenderFields().size() > 0)
				.map(SearchModel.SelectModel::getRenderFields)
				.flatMap(List::stream)
				.collect(collectingAndThen(toList(),
					Collections::unmodifiableList));
	}

	public FormInputType[] getDataTypes() {
		return FormInputType.values();
	}

	// ---------------------------------------------------------- private nested class
	private static class SearchParser {
		private static final String TYPE = "type";
		private static final String LABEL = "label";
		private static final String VALUE = "value";
		private static final String FIELD = "field";
		private static final String SELECT = "select";
		private static final String DEFAULT = "default";
		private static final String ENTITY_CLASS = "entityClass";
		private static final Map<String, List<SearchModel.SelectModel>> SELECT_MAPPING = new HashMap<>();

		// ------------------------------------------------------ constructor
		public SearchParser(String file) {
			XMLEventReader reader;
			XMLInputFactory xif;
			try (InputStream in = this.getClass().getResourceAsStream(file);) {
				xif = XMLInputFactory.newInstance();
				reader = xif.createXMLEventReader(in);
				parse(reader);    // parse config
				baseify(SELECT_MAPPING);
			}
			catch (Exception e) {
				throw new IllegalStateException("Unable to construct SearchParser", e);
			}
			if (log.isDebugEnabled()) {
				log.debug("[{} instantiated]", getClass());
			}
		}

		private void parse(XMLEventReader reader) throws XMLStreamException {
			Deque<String> stack = new ArrayDeque<>();
			SearchModel.SelectModel selectModel = null;
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					StartElement element = event.asStartElement();
					String name = element.getName().getLocalPart();
					if (name.equals(SELECT)) {
						selectModel = parseSelect(element);
						SELECT_MAPPING.computeIfAbsent(selectModel.getClazz(),
							k -> new ArrayList<>()).add(selectModel);
						stack.push(name);
					}
					else if (name.equals(FIELD)) {
						// add child field tag to the parent select
						if (stack.size() > 0) {
							SearchModel.Field f = parseField(element);
							selectModel.addFieldToRender(f);
							f.setRenderFor(selectModel.getValue());
						}
					}
				}
				if (event.isEndElement()
					&& event.asEndElement().getName().getLocalPart().equals(SELECT)) {
					if (stack.size() > 0) { stack.poll(); }
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
			return
				new SearchModel.Field(value, label, FormInputType.valueOf(
				type.toUpperCase()), isDefault);
		}


		// ------------------------------------------------------ helper methods
		public static Map<String, List<SearchModel.SelectModel>> getMapping() {
			if (SELECT_MAPPING.size() > 0) {
				return Collections.unmodifiableMap(SELECT_MAPPING);
			}
			return
				Collections.EMPTY_MAP;
		}

		// add base/common model to all select model mappings
		private void baseify(Map<String, List<SearchModel.SelectModel>> map) {
			List<SearchModel.SelectModel> list = map.get(BASE_CLASS);
			map.entrySet()
				.stream().filter(e -> !e.getKey()
				.equals(BASE_CLASS))
				.forEach(e -> e.getValue().addAll(list));
		}

		// convenience wrapper
		private static String getAttrValue(StartElement tag, String value) {
			Attribute a = tag.getAttributeByName(QName.valueOf(value));
			return a != null ? a.getValue() : null;
		}

	}

}
