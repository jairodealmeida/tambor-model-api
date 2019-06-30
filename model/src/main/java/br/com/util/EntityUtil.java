package br.com.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;

import br.com.database.dao.entity.Entity;
import br.com.database.dao.entity.annotation.GPAField;
import br.com.database.dao.entity.annotation.GPAFieldBean;
import br.com.database.dao.entity.annotation.GPAPrimaryKey;
import br.com.database.dao.model.FieldTO;

public class EntityUtil {
	public static void castEntities(List<?> listFrom, List<Entity> castList){
		for (Object object : listFrom) {
			castList.add((Entity)object);
		}
	}
	public static void castClazz(List<Entity> listFrom, List<Object> castList){
		for (Object object : listFrom) {
			castList.add( object );
		}
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
	public static void prepareFields(Entity entity,
			ArrayList<FieldTO> primaryKeyTos,
			ArrayList<FieldTO> fieldTos,
			ArrayList<FieldTO> foreignKeyTos,
			boolean usePrimaryKey) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		//primaryKeyTos = new ArrayList<FieldTO>();
		//fieldTos = new ArrayList<FieldTO>();
		//foreignKeyTos = new ArrayList<FieldTO>();
		Field[] fields = entity.getClass().getDeclaredFields();	
		
		//trunk entity to persistence
		for(int i=0; i<fields.length; i++){
			Field reflectionField = fields[i];
			if(reflectionField!=null){
				reflectionField.setAccessible(true);
				Annotation annoField = reflectionField.getAnnotation(GPAField.class);
				Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
				Annotation annoFieldBean = reflectionField.getAnnotation(GPAFieldBean.class);
				/* 
				 ainda falta validar a chave primaria do objeto
				 por enquanto so esta prevendo pk usando sequence no banco
				 objeto id sempre a gerado no banco por uma sequence
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
				if(annoFieldBean!=null && annoFieldBean instanceof GPAFieldBean){
					GPAFieldBean field = (GPAFieldBean)annoFieldBean;
					String name = field.name();
					//TODO use this to instance FK
					Class<?> clazz = field.clazz();
					Object value = reflectionField.get(entity);
					foreignKeyTos.add(new FieldTO(name, value));
					continue;
				}
			}
		}
	}
	
	
	public static void parseFieldsToContentValues(List<FieldTO> fields, ContentValues cv ){
		 for (int i=0; i<fields.size();i++) {
		    	FieldTO fieldTO = fields.get(i);
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
			    	if(value instanceof Date)		{cv.put(name, DateUtil.parseString((Date)value, DateUtil.DD_MM_YYYY_HH_MM_SS));}
			    	if(value instanceof Entity)	{
			    		if(((Entity)value).getId()==null){ //is new
			    			((Entity)value).setId(Entity.getIdRandomico()); 
			    		}
			    		cv.put(name, ((Entity)value).getId()) ;
			    	}
		    	}
		    }
	}
	public static void getAnnotationFields(Field[] fields, List<Field> annoFields){
		for (Field field : fields) {
			field.setAccessible(true);
			if( field.isAnnotationPresent(GPAField.class) ||
					field.isAnnotationPresent(GPAPrimaryKey.class) ||
					field.isAnnotationPresent(GPAFieldBean.class)){
				annoFields.add(field);
			}
		}
	}
	public static void getAnnotationFields(List<Field> fields, List<Field> annoFields){
		for (Field field : fields) {
			field.setAccessible(true);
			if( field.isAnnotationPresent(GPAField.class) ||
					field.isAnnotationPresent(GPAPrimaryKey.class) ||
					field.isAnnotationPresent(GPAFieldBean.class)){
				annoFields.add(field);
			}
		}
	}
	public static Field[] getDeclaredFields(boolean defaultFields,Class<?> entity){
		if(defaultFields){
			List<Field> fields = new ArrayList<Field>();
			fields.addAll( Arrays.asList(entity.getDeclaredFields() ));
			fields.addAll( Arrays.asList(entity.getSuperclass().getDeclaredFields() ));
			List<Field> annoFields = new ArrayList<Field>();
			EntityUtil.getAnnotationFields(fields,annoFields);
			return annoFields.toArray(new Field[annoFields.size()]);
		}else{
			List<Field> annoFields = new ArrayList<Field>();
			EntityUtil.getAnnotationFields(entity.getDeclaredFields(),annoFields);
			return annoFields.toArray(new Field[annoFields.size()]);
		}
	}
	public enum STATUS {
		NEWER,
		DOWNLOADED,
		UPLOADED,
		UPDATED,
		DELETED,
		ERROR;

		public String toString() {
			return this.name();
		}

	}
}
