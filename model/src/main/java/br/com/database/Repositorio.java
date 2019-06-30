package br.com.database;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.database.dao.entity.Entity;
import br.com.database.dao.entity.annotation.GPAEntity;
import br.com.database.dao.entity.annotation.GPAField;
import br.com.database.dao.entity.annotation.GPAPrimaryKey;
import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.TransferObject;
import br.com.util.Logger;

@Deprecated // use the Repository class
public class Repositorio  {
	
	@Deprecated
	private ArrayList<FieldTO> primaryKeyTos;
	 @Deprecated
	private ArrayList<FieldTO> fieldTos;
	
	private SQLiteDatabase db;  
	private Class<?> entity;
	
	public String getTableName() {
		Annotation annoClass = entity.getAnnotation(GPAEntity.class);
		GPAEntity anno = (GPAEntity)annoClass;
		String tableName = anno.name();
		return tableName;
	}
	/**
	 * 
	 * @param ctx
	 * @param entity
	 * @param databaseName
	 * @param version
	 * @throws Exception
	 */
	public Repositorio(Context ctx,
						Class<?> entity,
						String databaseName,
						int version) throws Exception{
		if(db!=null){
			db.close();
		}
		this.entity = entity;
        SQLiteHelper dbHelper = new SQLiteHelper(ctx,  
                databaseName, 
                version,   
                createScript(), 
                deleteScript());  
        db = dbHelper.getWritableDatabase();  
	}
	/**
	 * 
	 */
	public void close(){
        db.close();
    }
	/**
	 * 
	 * @return
	 */
	public SQLiteDatabase getDb(){
		return db;
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createScript() throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append("create table ");
		sb.append(getTableName());
		sb.append(createStatementParams(entity,true));
		return sb.toString();
	}
	/**
	 * 
	 * @return
	 */
	public String deleteScript(){
		StringBuilder sb = new StringBuilder();
		sb.append("drop table if exists ");
		sb.append(getTableName() + ";");
		return sb.toString();
	}
	/**
	 * method to prepare the statments by transact entity persistences
	 * using to get a fields of entitys and setting a obfuscate values
	 * in statement sql
	 * Translate a entity class to FieldTO (field transfer objects)
	 * this objects TO are using in persistence atributs of tables
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String createStatementParams(Class<?> entity, boolean usePrimaryKey) 
			throws Exception{
		
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Field[] fields = entity.getDeclaredFields();	
		
		//trunk entity to persistence
		for(int i=0; i<fields.length; i++){
			Field reflectionField = fields[i];
			if(reflectionField!=null){
				reflectionField.setAccessible(true);
				Annotation annoField = reflectionField.getAnnotation(GPAField.class);
				Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
				/* 
				 ainda falta validar a chave prim�ria do objeto
				 por enquanto so esta prevendo pk usando sequence no banco
				 objeto id sempre  gerado no banco por uma sequence
				*/
				if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
					GPAPrimaryKey pk = (GPAPrimaryKey)annoFieldPK;
					String name = pk.name();
					sb.append( name + " integer primary key");
					if(pk.ignore() == true){
						sb.append(" autoincrement ");
					}
					if(i<fields.length-1){
						sb.append(",");
					}
					continue;
				}
				if(annoField!=null && annoField instanceof GPAField){
					GPAField field = (GPAField)annoField;
					String name = field.name();
					Class<?> type = reflectionField.getType();
					sb.append( name + " " + getSqLiteType(type));
					if(i<fields.length-1){
						sb.append(",");
					}
					continue;
				}
			}
		}
		sb.append(");");
		return sb.toString();
	}
	/**
	 * method to get SqLite data types
	 * http://www.sqlite.org/datatype3.html
	 */
	private String getSqLiteType(Class<?> value){
		String sn = value.getSimpleName();
		if(sn.equalsIgnoreCase("String"))
			return "text";
		if(sn.equalsIgnoreCase("int") || sn.equalsIgnoreCase("Integer") || 
		   sn.equalsIgnoreCase("long") || sn.equalsIgnoreCase("Long") || 
		   sn.equalsIgnoreCase("BigDecimal")){
			return "integer";
		}
		if(sn.equalsIgnoreCase("double") || sn.equalsIgnoreCase("Double") || 
		   sn.equalsIgnoreCase("float") || sn.equalsIgnoreCase("Float")){
			return "integer";
		}
		if(sn.equalsIgnoreCase("byte[]") || sn.equalsIgnoreCase("Byte[]")){
			return "blob";
		}
		throw new NullPointerException("type not found " + sn);
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
				 ainda falta validar a chave primria do objeto
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
	/**
	 * method will use to insert objects
	 * @return "success or fail"
	 */	
	public long insert(Entity entity){
		try {
			this.prepareFields(entity, false);
			String tableName = this.getTableName();
			TransferObject to = new TransferObject(
						tableName, 
						primaryKeyTos, fieldTos, null, 
						TransferObject.INSERT_TYPE);
			return transact(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * method will use to update objects
	 * @return "success or fail"
	 */
	public long update(Entity entity){
		try {
			this.prepareFields(entity, true);
			String tableName = this.getTableName();
			TransferObject to = new TransferObject(
						tableName,
						primaryKeyTos, fieldTos, null, 
						TransferObject.UPDATE_TYPE);
			return transact(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * method will use to delete objects
	 * @return "success or fail"
	 */
	public long delete(Entity entity){
		try {
			this.prepareFields(entity, true);
			String tableName = this.getTableName();
			TransferObject to = new TransferObject(
						tableName,
						primaryKeyTos, fieldTos, null, 
						TransferObject.DELETE_TYPE);
			return transact(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax
	 * @param tableName - Table name
	 * @param whereClause - Where clause
	 * @return List<TransferObject> select entities result from database
	 */
	@Deprecated
	public List<TransferObject> select(String tableName, 
											String whereClause){
	    long start, end;
	    start = (new java.util.Date()).getTime(); 
	    List<TransferObject> items = new ArrayList<TransferObject>();
	    Cursor c = null;
		try {
	        c = getDb().query(
	        	tableName,null,null,null,null,null,null);  
	        c.moveToFirst();  
	        if(!c.isAfterLast()){  
	        	TransferObject bean = this.fill(c);
	        	items.add( bean ); 
	        } 
	        return items;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage()); 
		}finally{
			   if(c!=null){c.close();}
	           end = (new java.util.Date()).getTime();
	           Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	public List<TransferObject> selectAll(Class<?> entity){
		long start, end;
		start = (new java.util.Date()).getTime(); 
		List<TransferObject> items = new ArrayList<TransferObject>();
		Cursor c = null;
		try {
			String tableName = this.getTableName();
			c = getDb().query(
				tableName, null, null, null, null, null, null);  
				c.moveToFirst();  
				if(!c.isAfterLast()){  
					TransferObject bean = this.fill(c);
					items.add( bean ); 
				} 
			return items;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage()); 
		}finally{
			if(c!=null){c.close();}
			end = (new java.util.Date()).getTime();
			Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	@Deprecated
	public List<TransferObject> select(Class<?> entity, 
			String whereClause){
			long start, end;
			start = (new java.util.Date()).getTime(); 
			List<TransferObject> items = new ArrayList<TransferObject>();
			Cursor c = null;
			try {
				String tableName = this.getTableName();
				c = getDb().query(
					tableName,null,null,null,null,null,null);  
					c.moveToFirst();  
					if(!c.isAfterLast()){  
					TransferObject bean = this.fill(c);
					items.add( bean ); 
			} 
			return items;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage()); 
		}finally{
			if(c!=null){c.close();}
			end = (new java.util.Date()).getTime();
			Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	
	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax
	 * @param tableName - Table name

	 * @return ArrayList<TransferObject> select entities result from database
	 */
	public TransferObject selectMax(String tableName, String maxField){
	    long start, end;
	    start = (new java.util.Date()).getTime(); 
	    Cursor c = null;
		try {
	        c = getDb().query(
	        	tableName, 
	        	new String[]{"max( " + maxField + " )"},   
	        	null, 
	        	null, 
	        	null, 
	        	null,
	            null);  
	        c.moveToFirst();  
	        if(!c.isAfterLast()){  
	        	TransferObject bean = this.fill(c);
	            return bean; 
	        }      
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage()); 
		}finally{
			   if(c!=null){c.close();}
	           end = (new java.util.Date()).getTime();
	           Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	
	/**
	 * Method to transact object into database
	 * @param feature - Instantiate transfer object
	 * @return String success or fail
	 * @throws Exception
	 */
	private long transact(TransferObject feature)  throws Exception {
	    ContentValues cv = new ContentValues();  
	    List<FieldTO> fields = feature.getFields();
	    for (FieldTO fieldTO : fields) {
	    	String name = fieldTO.getName();
	    	Object value = fieldTO.getValue();
	    	if(value ==null){cv.putNull(name);}
	    	else{
	    		if(value instanceof String)		{cv.put(name, (String) value );}
		    	if(value instanceof Integer)	{cv.put(name, (Integer) value);}
		    	if(value instanceof Long)		{cv.put(name, (Long) value);} 
		    	if(value instanceof BigDecimal) {cv.put(name, (Long) value);}
		    	if(value instanceof Double) 	{cv.put(name, (Double) value);}
		    	if(value instanceof Float)		{cv.put(name, (Float) value);}
		    	if(value instanceof byte[])		{cv.put(name, (byte[]) value );} 
		    	if(value instanceof Short)		{cv.put(name, (Short) value);}
	    	}
		}
	    return getDb().insert(getTableName(), null, cv);
	}
	
	/**
	 * Method to transact objects into database
	 * @param features - Instantiate transfer objects
	 * @return String success or fail
	 * @throws Exception
	 */
	private long transact(ArrayList<TransferObject> features)  throws Exception {
	    long start, end;
	    start = (new java.util.Date()).getTime(); 
        if(features!=null && features.size()>0){
        	db.beginTransaction();
        	for (TransferObject transferObject : features) {
        		if(transact(transferObject)<=0){
        			throw new NullPointerException("Insert error");
        		}
			}
        	db.setTransactionSuccessful();
        	db.endTransaction();
            end = (new java.util.Date()).getTime();
            Log.i("GPALOG","Time to query: " + (end - start) + " ms");
            return features.size();        
        }else{
            throw new NullPointerException("don't have features to transact");
        } 
    }
	private TransferObject fill(Cursor c){
		try {
			List<FieldTO> result = new ArrayList<FieldTO>();
			//List<FieldTO> fields =  fields.getFields();
			Field[] fields = entity.getDeclaredFields();	
		    for (Field field : fields) {
		    	field.setAccessible(true);
				Annotation annoField = field.getAnnotation(GPAField.class);
				Annotation annoFieldPK = field.getAnnotation(GPAPrimaryKey.class);
				if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
					GPAPrimaryKey pk = (GPAPrimaryKey)annoFieldPK;
					String name = pk.name();
					result.add( new FieldTO(name, c.getLong( c.getColumnIndexOrThrow(name) ) ));
					continue;
				}
				if(annoField!=null && annoField instanceof GPAField){
					GPAField anno = (GPAField)annoField;
					String name = anno.name();
					Object type = field.getType();
		    		if(type instanceof String)	{result.add( new FieldTO(name, c.getString( c.getColumnIndexOrThrow(name) ) ));}
			    	if(type instanceof Integer)	{result.add( new FieldTO(name, c.getInt( c.getColumnIndexOrThrow(name) )));}
			    	if(type instanceof Long)	{result.add( new FieldTO(name, c.getLong( c.getColumnIndexOrThrow(name) )));} 
			    	if(type instanceof BigDecimal) {result.add( new FieldTO(name, c.getLong( c.getColumnIndexOrThrow(name) )));}
			    	if(type instanceof Double) 	{result.add( new FieldTO(name, c.getDouble( c.getColumnIndexOrThrow(name) )));}
			    	if(type instanceof Float)	{result.add( new FieldTO(name, c.getFloat( c.getColumnIndexOrThrow(name) )));}
			    	if(type instanceof byte[])	{result.add( new FieldTO(name, c.getBlob( c.getColumnIndexOrThrow(name) ) ));} 
			    	if(type instanceof Short)	{result.add( new FieldTO(name, c.getShort( c.getColumnIndexOrThrow(name) )));}
					continue;
				}
		    	//Object value = field.get(field);
		    	//Object type = field.getType();
				/*
		    	if(value !=null){
		    		if(type instanceof String)	{result.add( new FieldTO(name, (String) value ));}
			    	if(type instanceof Integer)	{result.add( new FieldTO(name, (Integer) value));}
			    	if(type instanceof Long)	{result.add( new FieldTO(name, (Long) value));} 
			    	if(type instanceof BigDecimal) {result.add( new FieldTO(name, (Long) value));}
			    	if(type instanceof Double) 	{result.add( new FieldTO(name, (Double) value));}
			    	if(type instanceof Float)	{result.add( new FieldTO(name, (Float) value));}
			    	if(type instanceof byte[])	{result.add( new FieldTO(name, (byte[]) value ));} 
			    	if(type instanceof Short)	{result.add( new FieldTO(name, (Short) value));}
		    	}*/
			}
			TransferObject to = new TransferObject(
					getTableName(),
					primaryKeyTos, fieldTos, null,
					TransferObject.READ_TYPE);
			return to;
		} catch (Exception e) {
			Logger.error(e);
		}
		return null;
	}

}
