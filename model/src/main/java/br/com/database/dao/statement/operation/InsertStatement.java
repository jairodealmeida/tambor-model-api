package br.com.database.dao.statement.operation;


import java.util.ArrayList;

import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.TransferObject;
import br.com.database.dao.statement.transacts.Insertable;

public class InsertStatement implements Insertable {

    private TransferObject to;
    
    public InsertStatement(TransferObject to){
        this.to = to;
    }
    public StringBuilder createStatement() {
       if(to!=null){
            StringBuilder result = new StringBuilder();
            String tableName = to.getTableName();
            if(tableName!=null && !tableName.equalsIgnoreCase("")){
                result.append("INSERT INTO ");
                result.append(tableName);
                result.append(this.getParameters());
                result.append("\n");
                return result;
            }else{
                throw new NullPointerException("invalid table name = '" + tableName + "'");
            }
       }
       return null;
    }
    public StringBuilder getParameters(){
        StringBuilder fieldNames = new StringBuilder();
        StringBuilder fieldValues = new StringBuilder();
        StringBuilder result = new StringBuilder();
        ArrayList<FieldTO> fields = to.getFields();
        if(fields!=null && fields.size()>0){
            fieldNames.append(" ( ");
            fieldValues.append(" VALUES ( ");
            for (int i = 0; i < fields.size(); i++)  {
                FieldTO field = fields.get(i);
                if(field!=null){
                    fieldNames.append(field.getName());
                    //TASK prepared statement fieldValues.append("'"+ field.getValue() + "'");
                    fieldValues.append("?");
                    if(i<fields.size()-1){
                    	fieldNames.append(", ");
                    	fieldValues.append(", ");
                    }
                }else{
                    throw new NullPointerException("fields is null");
                }
            }
            fieldNames.append(" ) ");
            fieldValues.append(" ) ");
            result.append(fieldNames);
            result.append(fieldValues);
            return result;
        }else{
            throw new NullPointerException("feature don't have fields");
        }
    }
}