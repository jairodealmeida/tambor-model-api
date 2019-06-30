package br.com.database.dao.statement;

import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.TransferObject;
import br.com.database.dao.statement.operation.DeleteStatement;
import br.com.database.dao.statement.operation.InsertStatement;
import br.com.database.dao.statement.operation.SelectStatement;
import br.com.database.dao.statement.operation.StatementArguments;
import br.com.database.dao.statement.operation.UpdateStatement;
import br.com.database.dao.statement.transacts.Selectable;
import br.com.database.dao.statement.transacts.Transactionable;
import br.com.util.Logger;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Class to statement factory
 * Before execute the stamements the library GPA needs to create a statements
 * using two statements abstractios Transactionable and Selectable
 * Transactionable - SQL Inserts, Updates and Deletes
 * Selectable - SQL Selects
 * @author jairo.almeida
 */
public class StatementFactory {
	    static Logger log = Logger.getLogger(StatementFactory.class);
        private TransferObject to;
        private StatementArguments arguments;
        private String whereClause;
        private StringBuilder fieldValues = new StringBuilder();
        
        public StatementFactory(TransferObject to){
            this.to = to;
        }
        public StatementFactory(StatementArguments arguments){
            this.arguments = arguments;
        }
        /**
         * Method to create a SQL Statement
         * To transact statements use Transactionable interface
         * To select statements use Selectable interface
         * @return StringBuilder - Statement to database execute
         * @throws Exception
         */
        public StringBuilder createStatementSQL() throws Exception{
        	
        	if(to!=null){
        		Transactionable sql = this.getInstance(to);
           		if(sql!=null){
        			return sql.createStatement();
        		}else{
        			throw new NullPointerException("transactionable item is null");
        		}
        	}else{
        		Selectable sql = this.getInstance(arguments);
           		if(sql!=null){
        			return sql.createStatement();
        		}else{
        			throw new NullPointerException("transactionable item is null");
        		}
        	}
 
        }
        /**
         * Method to create a SQL Statement 

         * @return Statement sql
         * @throws Exception
         */
        public StringBuilder createStatementSQL(StatementArguments arguments) throws Exception{
            Selectable sql = this.getInstance(arguments);
            if(sql!=null){
                return sql.createStatement();
            }else{
                throw new NullPointerException("transactionable item is null");
            }
        }
        /**
         * Method used to prepared statement
         * @param pstm - PreparedStatement instantied
         * @throws SQLException
         */
        public void prepareStatement(PreparedStatement pstm ) throws SQLException {   
            ArrayList<FieldTO> allFields = this.getClauseFields(to);
            this.preparedFields(pstm, allFields);
        }
        /**
         * Method to prepare statements  
         * @param pstm - PreparedStatement instantied
         * @param clauseFields - ArrayList<FieldTO> fields used in prepared statement
         * @throws SQLException
         */
        private void preparedFields(PreparedStatement pstm , ArrayList<FieldTO> clauseFields ) throws SQLException {
            if(clauseFields!=null){
                for(int countFields=0; countFields < clauseFields.size(); countFields++){
                    FieldTO field = clauseFields.get(countFields);
                    if(field!=null){
                        int pstmPos = countFields+1;
                        Object value = field.getValue();
                        int type = field.getFieldType();
                        this.setValue(pstmPos,value,type,pstm);
                        fieldValues.append(":" + pstmPos + " = '" + value + "'");
                        if(countFields < clauseFields.size()-1){
                            fieldValues.append(", ");
                        }
                    }else{
                        throw new NullPointerException("field is null");
                    }
                }
                log.info("fields..: " + fieldValues);
            }
        }
        /**
         * Get fields to make a where clause
         * @param to - TransferObject transact object
         * @return ArrayList<FieldTO> - Fields to make where
         * @throws SQLException
         */
        private ArrayList<FieldTO> getClauseFields(TransferObject to) throws SQLException {
            ArrayList<FieldTO> clauseFields = new ArrayList<FieldTO>();
            //attributs to transact
            if(!to.isDeleted()){ //no append atributes
                clauseFields.addAll( to.getFields() );
            }
            if(!to.isInserted()){ //where clause
                clauseFields.addAll( to.getPrimaryKeys() );
            }
            return clauseFields;
        }
        /**
         * Method to set a prepared statement values before defined to "?"
         * @param index - index to set value in statement
         * @param value - value to setting
         * @param type - type of value defined
         * @param pstm - Prepared statement used
         * @throws SQLException
         */
        private void setValue(int index, 
                              Object value, 
                              int type,
                              PreparedStatement pstm) throws SQLException {
            Connection connection = pstm.getConnection();
            if(connection!=null){
                if(value==null){
                    pstm.setObject(index, null) ;
                    return;
                }
                pstm.setObject(index, value) ;
            }else{
                throw new NullPointerException("connection is null");
            }
        }
        /**
         * Method to get a instance from transactionable statements 
         * @param to - TransferObject
         * @return Transactionable object
         * @throws Exception
         */
        private Transactionable getInstance(TransferObject to) throws Exception{
        	if(to!=null){
	            int transactionType = to.getTransactionType();
	            switch (transactionType)  {
	                case TransferObject.INSERT_TYPE: 
	                    return new InsertStatement(to);
	                case TransferObject.UPDATE_TYPE: 
	                    return new UpdateStatement(to);
	                case TransferObject.DELETE_TYPE: 
	                    return new DeleteStatement(to);
	                default: 
	                    throw new NullPointerException("transaction type not exist");
	            }
            }else{
            	return null;
            }
        }
        /**
         *  Method to get a instance from selectable statements 

         * @return Selectable Object
         * @throws Exception
         */
	    private Selectable getInstance(StatementArguments arguments) throws Exception{
	    	Selectable selectable = new SelectStatement(arguments);
	    	return selectable;
	    }
	    /**
	     * get field values to where clause
	     * @return StringBuilder string to values where clause
	     */
	    public StringBuilder getFieldValues() {
	        return fieldValues;
	    }


}

