package br.com.database.dao.statement.operation;


import java.util.ArrayList;
import java.util.List;

import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.LikeFieldTO;
import br.com.database.dao.model.ORFieldTO;


public class WhereStatement {
		private List<FieldTO> fields;
		 public WhereStatement(){
			 
		 }
		 public WhereStatement(FieldTO field){
			 this.fields = new ArrayList<FieldTO>();
		    	this.fields.add(field);
		 }
	    public WhereStatement(List<FieldTO> fields){
	    	this.fields = fields;
	    }
	   
		public StringBuilder createWhereStatement(){
			return createWhereStatement(fields);
		}
        public StringBuilder createWhereStatement(List<FieldTO> fields){
            if(fields!=null && fields.size()>0){
                StringBuilder result = new StringBuilder();
                result.append(" WHERE ");
                for (int i = 0; i < fields.size(); i++)  {
                    FieldTO pk = fields.get(i);
                    if(pk!=null){
                        result.append( pk.getName() );
                        if(pk instanceof LikeFieldTO){
                        	result.append( " like " );
                        	result.append( "?");   
                        }else{
                        	result.append( "=" );	
                        	result.append( "?");   
                        }
                        //TASK pk.getSqlType() == Field. make to validation type Prepared statement
                        //result.append( "'" + pk.getValue()  + "'");    
                          
                    }else{
                        throw new NullPointerException("primary key (" + i + ") is null");
                    }
                    if(i<fields.size()-1){
                    	if(pk instanceof FieldTO){
                    		result.append( " AND " );
                    	}else if(pk instanceof ORFieldTO){
                    		result.append( " OR " );
                    	}
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
        		if(fields.get(i) instanceof LikeFieldTO){
        			values[i] = String.valueOf( "%"+fields.get(i).getValue()+"%" );
        		}else{
        			values[i] = String.valueOf( fields.get(i).getValue() );
        		}
			}
        	return values;
        }
        
}

