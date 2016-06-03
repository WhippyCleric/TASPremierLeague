package com.whippy.tas.premier.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

	private static Random fRandom = new Random();
	
	public static 	List<String> getNames(String file, Class clazz) throws IOException {
		InputStream in = clazz.getClassLoader().getResourceAsStream(clazz.getPackage().getName().replace(".","/")+"/" +file);
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		List<String> names = new ArrayList<String>();
		String line;
		while ((line=r.readLine()) != null) {
			names.add(line.toUpperCase());
		}
		return names;
	}
	


	public static int getGaussian(double mean , double variance){
		int result = (int) Math.floor( mean + fRandom.nextGaussian() * variance);
		result = result > 100 ? 100 : result;
		result = result < 0 ? 0 : result;
		return result;
	}
		
}
