package br.com.database.dao.model;

import java.io.Serializable;

import java.util.ArrayList;

public class TransferObject implements Serializable{
 
    public static final int INSERT_TYPE = 1;
    public static final int UPDATE_TYPE = 2;
    public static final int DELETE_TYPE = 3;
    public static final int READ_TYPE = 4;
    
	private String tableName;
	private ArrayList<FieldTO> primaryKeys;
    private ArrayList<FieldTO> fields;
    private ArrayList<FieldTO> foreignKeys;
	private int transactionType;
     
	public TransferObject(String tableName, ArrayList<FieldTO> primaryKeys, ArrayList<FieldTO> fields, ArrayList<FieldTO> foreignKeys , int transactionType){
				this.tableName = tableName;
				this.primaryKeys = primaryKeys;
				this.foreignKeys = foreignKeys;
				this.fields = fields;
				this.transactionType = transactionType;               
	}
    /*public TransferObject(String tableName,
    					 ArrayList<FieldTO> primaryKeys,
                         ArrayList<FieldTO> fields,
                         int transactionType){
           this.tableName = tableName;
           this.primaryKeys = primaryKeys;
           this.fields = fields;
           this.transactionType = transactionType;               
    }
    
    public TransferObject(String tableName,
            			  ArrayList<FieldTO> fields,
            			  int transactionType){
			this.tableName = tableName;
			this.fields = fields;
			this.transactionType = transactionType;
	}*/

    public void setPrimaryKeys(ArrayList<FieldTO> primaryKeys) {
            this.primaryKeys = primaryKeys;
	}
	 
	public ArrayList<FieldTO> getPrimaryKeys() {
            return primaryKeys;
	}
	 
	public void setTableName(String tableName) {
            this.tableName = tableName;
	}
	 
	public String getTableName() {
            return tableName;
	}
	 
	public ArrayList<FieldTO> getFields() {
            return fields;
	}
	 
	public void setFields(ArrayList<FieldTO> fields) {
            this.fields = fields;
	}
	 
	public boolean isModified() {
            return (transactionType==TransferObject.UPDATE_TYPE);
	}
	 
	public boolean isDeleted() {
            return (transactionType==TransferObject.DELETE_TYPE);
	}
	 
	public boolean isInserted() {
            return (transactionType==TransferObject.INSERT_TYPE);
	}	
        
    public ArrayList<FieldTO> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(ArrayList<FieldTO> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	public int getTransactionType(){
        return transactionType;
    }
    public String toString(){
    	StringBuilder result = new StringBuilder();
    	if(fields!=null){
    		result.append("values = ");
    		for (int i = 0; i < fields.size(); i++) {
				FieldTO field = fields.get(i);
				result.append( field.getValue() );
				result.append( ":" );
			}
    		return result.toString();
    	}
    	return "";
    }
    public Integer getInteger(String fieldName){
    	 try {
    		 return Integer.parseInt( getValue(fieldName).toString() );	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    	 
    }
    public Object getValue(String fieldName){
      	if(fields!=null){
    		for (int i = 0; i < fields.size(); i++) {
				FieldTO field = fields.get(i);
				if(field!=null &&
				   field.getName()!=null &&
				   field.getName().equalsIgnoreCase(fieldName)){
					return field.getValue();
				}
			}
    	}
      	return null;
    }
        
}//end of TranferObject
 
