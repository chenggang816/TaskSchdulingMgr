package com.tools;

import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;

public class JSONHelper {

	public static String toJSONString(JSONObject jsonObj){
		if(jsonObj == null) return null;
		StringWriter out = new StringWriter();
		try {
			jsonObj.writeJSONString(out);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	    return out.toString();
	}

}
