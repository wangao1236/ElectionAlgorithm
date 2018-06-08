package priv.wangao.ElectionAlgorithm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class JSON {

	public static String ArrayListToJson(List<Object> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}
	
	public static String MapListToJson(List<Map<String, Object>> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}
	
	public static List<Object> JSONToArrayList(String json) {
		List<Object> result = new ArrayList<Object>();
		JSONArray jsonArray = JSONArray.fromObject(json);
		for (Object object : jsonArray) {
			result.add(object);
		}
		return result;
	}
}
