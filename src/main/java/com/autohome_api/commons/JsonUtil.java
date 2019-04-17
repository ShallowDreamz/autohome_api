package com.autohome_api.commons;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;


public class JsonUtil {
	static String jsonData = "";
	
	public static String MapToJson(Map<String, Object> obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonData = mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonData;
	}

	public static JSONObject JsonToJsonObject(String json){
		JSONObject jObject = new JSONObject(json);
		return jObject;
	}
	
	public static String resultSetToJson(ResultSet rs){
        // 
        JSONArray array = new JSONArray();

        //
        ResultSetMetaData metaData = null;
		try {
			metaData = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        int columnCount = 0;
		try {
			columnCount = metaData.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        try {
			while (rs.next()) {
			    JSONObject jsonObj = new JSONObject();

			    // 
			    for (int i = 1; i <= columnCount; i++) {
			        String columnName = metaData.getColumnLabel(i);
			        String value = rs.getString(columnName);
			        jsonObj.put(columnName, value);
			    }
			    array.put(jsonObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

        return array.toString();
    }

}
