package br.com.database.dao.delegate;



import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import br.com.database.dao.DataTransferObject;
import br.com.database.dao.entity.Entity;
import br.com.database.dao.entity.annotation.GPAEntity;
import br.com.database.dao.entity.annotation.GPAField;
import br.com.database.dao.entity.annotation.GPAPrimaryKey;
import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.TransferObject;
import br.com.util.Logger;

/**
 * Class to open the database methods to use
 * Connect to database
 * Execute a SQL statements
 * And close a database connection
 * @author jairo.almeida
 * 
 */
public class DataTransferDelegate {
	/** class of works with database instance */
	private DataTransferObject dao = null;

	@Deprecated
	private ArrayList<FieldTO> primaryKeyTos;
	 @Deprecated
	private ArrayList<FieldTO> fieldTos;
	
	static Logger log = Logger.getLogger(DataTransferDelegate.class);
	
	public DataTransferDelegate(){
		dao = new DataTransferObject();
	}
	/**
	 * method used to get a table name of entity
	 * based in entity class name 
	 * TODO alter by annotation class name
	 * @return String table name
	 */
	@Deprecated
	private String getTableName(Entity entity){
		GPAEntity annoTable = (GPAEntity) entity.getClass().getAnnotation(GPAEntity.class);
		String tableName = annoTable.name();
		return tableName;
	}
	@Deprecated
	private void setPrimaryKeyTos(ArrayList<FieldTO> primaryKeyTos) {
		this.primaryKeyTos = primaryKeyTos;
	}
	@Deprecated
	private ArrayList<FieldTO> getPrimaryKeyTos() {
		return primaryKeyTos;
	}
	@Deprecated
	private void setFieldTos(ArrayList<FieldTO> fieldTos) {
		this.fieldTos = fieldTos;
	}
	@Deprecated
	private ArrayList<FieldTO> getFieldTos() {
		return fieldTos;
	}
	
	/**
	 * method will use to insert objects
	 * @return "success or fail"
	 */	
	public String insert(Entity entity){
		try {
			this.prepareFields(entity, false);
			String tableName = this.getTableName(entity);
			TransferObject to = new TransferObject(
						tableName, 
						primaryKeyTos, fieldTos, null, 
						TransferObject.INSERT_TYPE);
			return transact(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}
	/**
	 * method will use to update objects
	 * @return "success or fail"
	 */
	public String update(Entity entity){
		try {
			this.prepareFields(entity, true);
			String tableName = this.getTableName(entity);
			TransferObject to = new TransferObject(
						tableName,
						primaryKeyTos,fieldTos,null, 
						TransferObject.UPDATE_TYPE);
			return transact(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}
	/**
	 * method will use to delete objects
	 * @return "success or fail"
	 */
	public String delete(Entity entity){
		try {
			this.prepareFields(entity, true);
			String tableName = this.getTableName(entity);
			TransferObject to = new TransferObject(
						tableName,
						primaryKeyTos, fieldTos, null, 
						TransferObject.DELETE_TYPE);
			return transact(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}

	/**
	 * Method to transact objects into database
	 * @param features - Instantiate transfer objects
	 * @return String success or fail
	 * @throws Exception
	 */
	private String transact(ArrayList<TransferObject> features)  throws Exception {
	    long start, end;
	    start = (new java.util.Date()).getTime(); 
            if(features!=null && features.size()>0){
                dao.connect( true );
                dao.setAutoCommit(false);
                String result = dao.transact(features);
                if(result.equals("success")){
                    dao.commit();
                }else{
                    dao.rollback();
                }
                dao.setAutoCommit(true);
                dao.close();
                end = (new java.util.Date()).getTime();
                log.info("Time to query: " + (end - start) + " ms");
                return result;        
            }else{
                throw new NullPointerException("don't have features to transact");
            }
            
    }
	/**
	 * Method to transact object into database
	 * @param feature - Instantiate transfer object
	 * @return String success or fail
	 * @throws Exception
	 */
	private String transact(TransferObject feature)  throws Exception {
	    long start, end;
	    start = (new java.util.Date()).getTime(); 
            if(feature!=null){
                dao.connect( true );
                dao.setAutoCommit(false);
                String result = dao.transact(feature);
                if(result.equals("success")){
                    dao.commit();
                }else{
                    dao.rollback();
                }
                dao.setAutoCommit(true);
                dao.close();
                end = (new java.util.Date()).getTime();
                log.info("Time to query: " + (end - start) + " ms");
                return result;        
            }else{
                throw new NullPointerException("don't have features to transact");
            }	
	}
	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax
	 * @param tableName - Table name
	 * @param whereClause - Where clause
	 * @return ArrayList<TransferObject> select entities result from database
	 */
	public ArrayList<TransferObject> select(String tableName, 
											String whereClause){
		try {
		    long start, end;
		    start = (new java.util.Date()).getTime(); 
            dao.connect( true );
			ArrayList<TransferObject> entities = dao.select(tableName, whereClause);
            dao.close();
            end = (new java.util.Date()).getTime();
            log.info("Time to query: " + (end - start) + " ms");
			return entities;
		} catch (Exception e) {
			log.error(e.getMessage(), e); 
		}
		return null;
	}

	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax
	 * @param tableName - Table name
	 * @param whereClause - Where clause
	 * @return ArrayList<TransferObject> select entities result from database
	 */
	public TransferObject selectMax(String tableName, String maxField){
		try {
		    long start, end;
		    start = (new java.util.Date()).getTime(); 
            dao.connect( true );
			TransferObject entity = dao.selectMax(tableName, maxField);
            dao.close();
            end = (new java.util.Date()).getTime();
            log.info("Time to query: " + (end - start) + " ms");
			return entity;
		} catch (Exception e) {
			log.error(e.getMessage(), e); 
		}
		return null;
	}

	/**
	 * method to prepare the statments by transact entity persistences
	 * using to get a fields of entitys and setting a obfuscate values
	 * in statement sql
	 * Translate a entity class to FieldTO (field transfer objects)
	 * this objects TO are using in persistence atributs of tables
	 * TODO alter by annotation field name
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void prepareFields(Entity entity, boolean usePrimaryKey) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		primaryKeyTos = new ArrayList<FieldTO>();
		fieldTos = new ArrayList<FieldTO>();
		Field[] fields = entity.getClass().getDeclaredFields();	
		
		//trunk entity to persistence
		for(int i=0; i<fields.length; i++){
			Field reflectionField = fields[i];
			if(reflectionField!=null){
				reflectionField.setAccessible(true);
				Annotation annoField = reflectionField.getAnnotation(GPAField.class);
				Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
				/* 
				 ainda falta validar a chave primaria do objeto
				 por enquanto so esta prevendo pk usando sequence no banco
				 objeto id sempre  gerado no banco por uma sequence
				*/
				if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
					GPAPrimaryKey pk = (GPAPrimaryKey)annoFieldPK;
					//if(pk.ignore() == true){
					//	continue;
					//}else{
					String name = pk.name();
					Object value = reflectionField.get(entity);
					primaryKeyTos.add(new FieldTO(name, value));
					continue;
					//}
				}
				if(annoField!=null && annoField instanceof GPAField){
					GPAField field = (GPAField)annoField;
					String name = field.name();
					Object value = reflectionField.get(entity);
					fieldTos.add(new FieldTO(name, value));
					continue;
				}
			}
		}
	}
}
 
