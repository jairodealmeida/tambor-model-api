package br.com.database.dao.statement.operation;


import java.util.ArrayList;

import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.TransferObject;
import br.com.database.dao.statement.transacts.Deletable;

public class DeleteStatement implements Deletable {
    
	private TransferObject to;
    
    public DeleteStatement(TransferObject feature){
        this.to = feature;
    }
    public StringBuilder createStatement() {
        if(to!=null){
             StringBuilder result = new StringBuilder();
             ArrayList<FieldTO> pks = to.getPrimaryKeys();
             if(pks!=null){
                 StringBuilder cws = where.createWhereStatement(pks);
                 if(cws!=null && !cws.toString().equalsIgnoreCase("")){
                    String tableName = to.getTableName();
                     if(tableName!=null && !tableName.equalsIgnoreCase("")){
                    	 result.append("DELETE ");
                    	 result.append("FROM ");
                         result.append(tableName);
                         result.append(this.getParameters());
                         result.append(cws);
                         return result;
                     }else{
                         throw new NullPointerException("invalid table name = '" + tableName + "'");
                     }
                 }else{
                     throw new NullPointerException("invalid where clause");
                 }
             }else{
                 throw new NullPointerException("impossible make the where " +
                                                "clause without primary keys");
             }
        }
        return null;
    }
    public StringBuilder getParameters(){
        StringBuilder result = new StringBuilder();
        return result;
    }

}
