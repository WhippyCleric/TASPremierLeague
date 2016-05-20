import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	
	public static 	List<String> getNames(String file) throws IOException {
		InputStream in = Generator.class.getClassLoader().getResourceAsStream(file);
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		List<String> names = new ArrayList<String>();
		String line;
		while ((line=r.readLine()) != null) {
			names.add(line);
		}
		return names;
	}
}
