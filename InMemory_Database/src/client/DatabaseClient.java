package client;

import core.*;
import util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseClient {

	public static void main(String[] args) {
		
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);
		DatabaseCore core = new DatabaseCore();
		String result;
		List<String> inputStatements = new ArrayList();
		try {
			String statement = br.readLine();
			// Program terminates when END statement is provided
			while (!statement.equalsIgnoreCase("END")) {
				inputStatements.add(statement);
				statement = br.readLine();
			}
			// The statements are executed line by line
			for (String command : inputStatements) {
				System.out.print(core.handler(command));
			}
	
	} catch (IOException e1) {
		e1.printStackTrace();
	}

  }
}

