package br.com.database.dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.database.DatabaseDAOImpl;
import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.TransferObject;
import br.com.database.dao.statement.StatementFactory;
import br.com.database.dao.statement.operation.StatementArguments;
import br.com.util.Logger;

/**
 * Implentation to data tranfer objects
 * This practice ar modified to create a EASY-GPA Library
 */
public class DataTransferObject extends DatabaseDAOImpl {
 
	static Logger log = Logger.getLogger(DataTransferObject.class);
    private Map<Integer,String> transaction = new HashMap<Integer,String>();

    public DataTransferObject(){
    	
    }
    /**
     * Method to get entities from ResultSet 
     * @param tableName - database table name
     * @param rs - ResultSet from statement executed 
     * @return ArrayList<TransferObject> entities from database and statement
     * @throws SQLException
     * TODO modify ArrayList<TransferObject> to TransferObjectColection implemented to easy-gpa
     */
    private ArrayList<TransferObject> getEntities( String tableName, ResultSet rs) throws SQLException{
    	ArrayList<TransferObject> entitys = new ArrayList<TransferObject>();
        while(rs.next()){
        	ArrayList<String> columns = this.getColumnNames(rs);
        	ArrayList<FieldTO> fields = new ArrayList<FieldTO>();
        	for (String columnLabel : columns) {
        		Object columnValue = rs.getObject(columnLabel);
        		fields.add(new FieldTO(columnLabel, columnValue)); 
			}
           	TransferObject to = new TransferObject(
           			tableName, 
           			null,fields,null, 
           			TransferObject.READ_TYPE);
           	entitys.add(to);
        }
        return entitys;
    }
    /**
     * Method to get entity from ResultSet 
     * @param tableName - database table name
     * @param rs - ResultSet from statement executed 
     * @return TransferObject entity from database and statement
     * @throws SQLException
     * TODO modify ArrayList<TransferObject> to TransferObjectColection implemented to easy-gpa
     */
    private TransferObject getEntity( String tableName, ResultSet rs) throws SQLException{
        while(rs.next()){
        	ArrayList<String> columns = this.getColumnNames(rs);
        	ArrayList<FieldTO> fields = new ArrayList<FieldTO>();
        	
        	//EntityUtil.prepareFields(nentity, primaryKeyTos, fieldTos, foreignKeyTos, false);
        	for (String columnLabel : columns) {
        		Object columnValue = rs.getObject(columnLabel);
        		fields.add(new FieldTO(columnLabel, columnValue)); 
			}
           	TransferObject to = new TransferObject(
           			tableName, 
           			null,fields,null, 
           			TransferObject.READ_TYPE);
           	return to;
        }
        return null;
    }
    /**
     * Method to get columns from result set query
     * @param rs - ResultSet from statement
     * @return ArrayList<String> - Column names
     * @throws SQLException
     */
    private ArrayList<String> getColumnNames(ResultSet rs) throws SQLException {
        if (rs == null) {
          return null;
        }
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();

        // get the column names; column indexes start from 1
        ArrayList<String> columns = new ArrayList<String>();
        for (int i = 1; i < numberOfColumns + 1; i++) {
          String columnName = rsMetaData.getColumnName(i);
          // Get the name of the column's table name
          //String tableName = rsMetaData.getTableName(i);
          //System.out.println("column name=" + columnName + " table=" + tableName + "");
          columns.add(columnName);
        }
		return columns;
      }
    
    /**
     * transaction method to represents a client EditFeature
     * @param tos
     * @return success or fail
     */
	public String transact(ArrayList<TransferObject> tos) { 
                try {
                    return this.transactEntitys(tos);
                } catch (Exception e) {
                	log.error(e);
                    return "fail";
                }
	} 
	/**
	 * transact entity's in database instance 
	 * @param tos - Transfer Objects in database
	 * @return "transact" or "fail"
	 * @throws Exception
	 */
    private String transactEntitys(ArrayList<TransferObject> tos) throws Exception {
        if(tos!=null){
        	int rows=0;
        	transaction.clear();
            for(; rows<tos.size();rows++){
                TransferObject to = tos.get(rows);
                String executedSql = transact(to);
                transaction.put(rows, executedSql);
                log.info( "transact rows : " + rows );
                log.info( "transact sql  : " + executedSql );
                log.info( "transact to   : " + to.toString() );
            }
            log.info("success to update " + rows + " rolls");
            return "success";
        }else{
            log.error("features are null", new NullPointerException("features are null"));
            return "fail";
        }	
    }
    /**
     * Persistence to Transact Object
     * @param to - Transact Object
     * @return "success" or "fail"
     * @throws Exception
     */
    public String transact(TransferObject to) throws Exception {
            StatementFactory statement = new StatementFactory(to);
            StringBuilder sqls = statement.createStatementSQL();
            if(sqls!=null){
                PreparedStatement pstm = super.prepareStatement(sqls.toString());
                statement.prepareStatement( pstm );  
                try  {
                    if(pstm.executeUpdate()<1){
                        throw new NullPointerException("transact error ...");
                    }
                    String nativeSql =super.nativeSQL(sqls.toString());
                    log.info(nativeSql);
                    return "success";
                } catch (Exception ex)  {
                	log.error(sqls.toString());
                    log.error(ex.getMessage(), ex); 
                }
            }else{
                log.error("prepare sql fail", new NullPointerException("prepare sql fail"));
            } 
            return "fail";
    }
    /**
     * Method to execute a select statement into database
     * @param tableName - name of database table
     * @param whereClause - where database clause
     * @return ArrayList<Entity> - Entitys from database 
     * 							   into where and table clauses
     * TODO statement.prepareStatement( pstm ) create a method to work with prepared statement 
     */
    public ArrayList<TransferObject> select(String tableName, String whereClause){
    	try {	
    		StatementArguments arguments = new StatementArguments(tableName);
    		arguments.setWhereClause(whereClause);
            StatementFactory statement = new StatementFactory(arguments);
            StringBuilder sqls = statement.createStatementSQL(arguments);
            if(sqls!=null){
                PreparedStatement pstm = super.prepareStatement(sqls.toString()); 
                ResultSet rs = pstm.executeQuery();
                ArrayList<TransferObject> entities = this.getEntities(tableName, rs);
                return entities;
            }else{
                log.error("prepare sql fail", new NullPointerException("prepare sql fail"));
            } 		
		} catch (Exception e) {
			log.error(e);
		} 
		return null;
    }
    /**
     * Method to execute a select statement into database
     * @param tableName - name of database table

     * @return ArrayList<Entity> - Entitys from database 
     * 							   into where and table clauses
     * TODO statement.prepareStatement( pstm ) create a method to work with prepared statement 
     */
    public TransferObject selectMax(String tableName, String maxField){
    	try {	
    		StatementArguments arguments = new StatementArguments(tableName);
            arguments.setMaxField(maxField);
            StatementFactory statement = new StatementFactory(arguments);
            StringBuilder sqls = statement.createStatementSQL(arguments);
            if(sqls!=null){
                PreparedStatement pstm = super.prepareStatement(sqls.toString()); 
                ResultSet rs = pstm.executeQuery();
                TransferObject entity = this.getEntity(tableName, rs);
                return entity;
            }else{
                log.error("prepare sql fail", new NullPointerException("prepare sql fail"));
            } 		
		} catch (Exception e) {
			log.error(e);
		} 
		return null;
    }
    
}
 
