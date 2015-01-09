package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import core.DatabaseCore;

public class DatabaseTest {

public static void main(String[] args) {
		
	    // Reads the path of the input file from property
	    Properties prop = new Properties();
	    
		try {
	    InputStream input = new FileInputStream("src/config.properties");
	    prop.load(input);
	    String path = prop.getProperty("filepath");
	    if (path == null) {
	    	System.out.println("ERROR: Unable to load from property file");
	    	return;
	    }
	    File file = new File(path);
	    FileReader fr;
	    fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		DatabaseCore core = new DatabaseCore();
		String result;
		List<String> inputStatements = new ArrayList();
			String statement = br.readLine();
			while (!statement.equalsIgnoreCase("END")) {
				inputStatements.add(statement);
				statement = br.readLine();
			}
			
			for (String command : inputStatements) {
				System.out.print(core.handler(command));
			}

	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
  }

}
