package priv.wangao.ElectionAlgorithm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XML {
	private static String configPath = "src/main/resources/config.xml";
	
	public static void setConfigPath(String config) {
		configPath = config;
	}
	
	public Map<String, Object> nodeConf() {
		Map<String, Object> conf = new HashMap<String, Object>();
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(new File(configPath));
			Element root = document.getRootElement();
			Element nodeList = root.element("nodeList");
			List<String> ipportList = new ArrayList<String>();
			for (@SuppressWarnings("unchecked")
			Iterator<Element> i = nodeList.elementIterator(); i.hasNext(); ) {
				Element e = i.next();
				ipportList.add(e.getText());
			}
			conf.put("nodeList", ipportList);
			conf.put("nodeID", root.element("nodeID").getText());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conf;
	}
}
