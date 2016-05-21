package com.whippy.tas.premier.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	
	public static 	List<String> getNames(String file, Class clazz) throws IOException {
		InputStream in = clazz.getClassLoader().getResourceAsStream(clazz.getPackage().getName().replace(".","/")+"/" +file);
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		List<String> names = new ArrayList<String>();
		String line;
		while ((line=r.readLine()) != null) {
			names.add(line);
		}
		return names;
	}
}
