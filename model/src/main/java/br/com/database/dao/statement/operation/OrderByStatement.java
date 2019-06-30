package br.com.database.dao.statement.operation;

import java.util.ArrayList;
import java.util.List;

import br.com.database.dao.model.FieldTO;


public class OrderByStatement {
	private List<FieldTO> fields;
	 public OrderByStatement(){
		 
	 }
   public OrderByStatement(List<FieldTO> fields){
   	this.fields = fields;
   }
   
    public OrderByStatement(FieldTO field){
    	this.fields = new ArrayList<FieldTO>();
	   	this.fields.add(field);
	}
  
	public StringBuilder createWhereStatement(){
		return createOrderByStatement(fields);
	}
	 public StringBuilder createOrderByStatement(List<FieldTO> fields){
         if(fields!=null && fields.size()>0){
             StringBuilder result = new StringBuilder();
             result.append(" ORDER BY ");
             for (int i = 0; i < fields.size(); i++)  {
                 FieldTO pk = fields.get(i);
                 if(pk!=null){
                     result.append( pk.getName() );
                 }else{
                     throw new NullPointerException("field (" + i + ") is null");
                 }
                 if(i<fields.size()-1){
                     result.append( " , " );
                 }
             }
             return result;
         }else{
             throw new NullPointerException("impossible make the where clause without primary keys");
         }
     }
	 public String[] getArguments(){
     	String[] values = new String[fields.size()];
     	for (int i = 0; i < values.length; i++) {
				values[i] = String.valueOf( fields.get(i).getValue() );
			}
     	return values;
     }
}
