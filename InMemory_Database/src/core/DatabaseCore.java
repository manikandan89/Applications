package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import util.DatabaseUtil;

public class DatabaseCore {

	private HashMap<String,String> database;
	private HashMap<String,Integer> numEqualToMap;
	private Stack<List<String>> stackOfTransactions; 
	private List<String> transaction;
	private DatabaseUtil util;
	private String result;
	private boolean begin,rollback;
	
	// Constructor for Database Core
	public DatabaseCore() {
		database = new HashMap<String,String>();
		numEqualToMap = new HashMap<String,Integer>();
		util = new DatabaseUtil();
		result = null;
		stackOfTransactions = new Stack<List<String>>();
		transaction = new ArrayList<String>();
		begin = rollback = false;
	}
	
	// Handles the given statement and routes it for the task depending
	// on the keyword specified
	public String handler(String statement) {
		String[] splitStatements = statement.split(" ");
		if (statement.length() == 0) {
			return "";
		}
		if (splitStatements.length > 3 && !DatabaseUtil.isKeyword(splitStatements[0])) {
			result = "INVALID INPUT";
			return result;
		}
		
		String keyword = splitStatements[0];
		if (keyword.equalsIgnoreCase("SET") && (splitStatements.length == 3)) {
			set(splitStatements[1],splitStatements[2]);
			result = "";
			return result;
		} else if (keyword.equalsIgnoreCase("GET") && (splitStatements.length == 2)) {
			result = get(splitStatements[1]) + "\n";
			return result;
		} else if (keyword.equalsIgnoreCase("NUMEQUALTO") && (splitStatements.length == 2)) { 
			int numEqualToResult = retrieveNumEqualTo(splitStatements[1]);
			result = String.valueOf(numEqualToResult) + "\n";
			return result;
		} else if (keyword.equalsIgnoreCase("UNSET") && (splitStatements.length == 2)) {
			unset(splitStatements[1]);
			result = "";
			return result;
		} else if (keyword.equalsIgnoreCase("BEGIN") && (splitStatements.length == 1)) {
			begin();
			result = "";
			return result;
		} else if (keyword.equalsIgnoreCase("ROLLBACK") && (splitStatements.length == 1)) {
			result = rollback();
			if (result.equalsIgnoreCase("NO TRANSACTION"))
			   result = result + "\n";
			return result;
		}else if (keyword.equalsIgnoreCase("COMMIT") && (splitStatements.length == 1)) {
			commit();
			result = "";
			return result;
		}
		
		else {
			return "";
		}
		
	}
	
	// Sets the value for the variable in the database
	public void set(String variable, String value) {
		
		String previousValue = database.get(variable);
		int previousValueCount = retrieveNumEqualTo(previousValue);
		int currentValueCount = retrieveNumEqualTo(value);
		StringBuilder unsetCurrentValueForRollback = new StringBuilder("UNSET ");
		StringBuilder setPreviousValueForRollback = new StringBuilder("SET ");
		if (previousValue != null) {
			database.put(variable, value);
			numEqualToMap.put(previousValue, previousValueCount-1);
			numEqualToMap.put(value, currentValueCount+1);
			setPreviousValueForRollback.append(variable);
			setPreviousValueForRollback.append(" ");
			setPreviousValueForRollback.append(previousValue);
			if (begin && !rollback) {
				transaction.add(setPreviousValueForRollback.toString());
			}
		} else {
			database.put(variable, value);
			numEqualToMap.put(value, currentValueCount+1);
			unsetCurrentValueForRollback.append(variable);
			if (begin && !rollback) {
				transaction.add(unsetCurrentValueForRollback.toString());
			}
		}
	}
	
	// Retrieves the value for the given variable
	public String get(String variable) {
		
		String value = "";
		value = database.get(variable);
		
		if (value != null) {
			return value;
		}
		return value;
	}
	
	// Retrieves the count of the number of variables to which the
	// given value is assigned in the database.
	public int retrieveNumEqualTo(String value) {
	
		if (numEqualToMap.get(value) == null) {
			return 0;
		} else {
			return numEqualToMap.get(value);
		}
		
	}
	
	// Unsets the value of the variable in the database
	public void unset(String variable) {
		
        String value = database.get(variable);
        int count = retrieveNumEqualTo(value);
        StringBuilder setForRollback =  new StringBuilder("SET ");
        
        if (value != null) {
        	database.remove(variable);
        	numEqualToMap.put(value, count-1);
        	if (begin && !rollback) {
        		setForRollback.append(variable);
            	setForRollback.append(" ");
            	setForRollback.append(value);
            	transaction.add(setForRollback.toString());
        	}
        } else {
        	return;
        }
	}
	
	// Begins a new transaction
	public void begin() {
	  if (begin) {
		  if (!transaction.isEmpty()) {
			  stackOfTransactions.push(transaction);
			  transaction = new ArrayList<String>();
		  }
	  } else {
		  begin = true;
	  }
	}
	
	// Rolls back the last open transaction block
	public String rollback() {
		String result = "";
		if (begin) {
			rollback = true;
			Collections.reverse(transaction);
			for (String statement : transaction) {
				handler(statement);
			}
			rollback = false;
			if (!stackOfTransactions.isEmpty()) {
				transaction = stackOfTransactions.pop();
				return result;
			} else if (stackOfTransactions.isEmpty() && transaction.isEmpty()) {
				result = "NO TRANSACTION";
				return result;
			} else {
				transaction = new ArrayList<String>();
				begin = false;
				return result;
			}
		} else {
			result = "NO TRANSACTION";
			return result;
		}
		
	}
	
	// Commits all the open transaction blocks.
	public void commit() {
		begin = false;
		stackOfTransactions.removeAllElements();
	}
}
