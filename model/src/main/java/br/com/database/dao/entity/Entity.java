package br.com.database.dao.entity;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import br.com.database.dao.entity.annotation.GPAEntity;
import br.com.database.dao.entity.annotation.GPAField;
import br.com.database.dao.entity.annotation.GPAFieldBean;
import br.com.database.dao.entity.annotation.GPAPrimaryKey;
import br.com.database.dao.model.FieldTO;
import br.com.database.dao.model.TransferObject;
import br.com.util.EntityUtil;

public abstract class Entity implements Serializable{
	
	public static final int VARCHAR = 1;
	public static final int INTEGER = 2;
	public static final int LONG = 3;
	public static final int FLOAT = 4;
	public static final int DATE =  5;
	public static final int BLOB =  6;
	public static final int BEAN =  7;
	public static final int DOUBLE = 8;
	public static final int BOOLEAN = 9;
	
	public Long id;
	
	private boolean checked = false;
	

	public abstract void setId(Long id);
	public abstract Long getId();
	public Entity(){
		
	}

	@GPAField(name="status",type = Entity.VARCHAR)
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String thumbnail;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * metodo que gera um ID randomico 
	 * @return Long - chave  unica
	 * @throws NoSuchAlgorithmException 
	 */
	public static Long getIdRandomico() {
		long random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG").nextInt(99999999);
			return random;
		} catch (NoSuchAlgorithmException e) {
			Log.e("ERROR",e.getLocalizedMessage(),e); 
		}
		return null;
	}
	@Deprecated //move to rest api renamed EntitySerialization.parse
	public String getXML(){
		try {
			StringBuilder xml = new StringBuilder();
			Field[] fields = getDeclaredFields();
			String className = this.getClass().getSimpleName();
			xml.append("<").append(className.toLowerCase()).append(">");
			for(int i=0; i<fields.length; i++){
				Field reflectionField = fields[i];
				String name = reflectionField.getName();
				Object value = reflectionField.get(this);
				xml.append(xml.append("<").append(name).append(">"));
				xml.append(value);
				xml.append(xml.append("</").append(name).append(">"));
			}
			xml.append("</").append(className.toLowerCase()).append(">");
			return xml.toString();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void valuable(TransferObject to){
		//try {
			Field[] fields = getDeclaredFields();
			for(int i=0; i<fields.length; i++){
				Field reflectionField = fields[i];
				reflectionField.setAccessible(true);
				Annotation annoField = reflectionField.getAnnotation(GPAField.class);
				Annotation annoFieldBean = reflectionField.getAnnotation(GPAFieldBean.class);
				Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
				try {
				/* 
				 ainda falta validar a chave primria do objeto
				 por enquanto so esta prevendo pk usando sequence no banco
				 objeto id sempre  gerado no banco por uma sequence
				*/
				if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
					GPAPrimaryKey pk = (GPAPrimaryKey)annoFieldPK;
					String name = pk.name();
					Object value = to.getValue(name);
					if(value!=null){
						reflectionField.set(this, value);
					}
					continue;
				}
				if(annoField!=null && annoField instanceof GPAField){
					GPAField field = (GPAField)annoField;
					String name = field.name();
					Object value = to.getValue(name);
					if(value!=null){
						reflectionField.set(this, value);
					}
					continue;
				}
				if(annoFieldBean!=null && annoFieldBean instanceof GPAFieldBean){
					GPAFieldBean field = (GPAFieldBean)annoFieldBean;
					String name = field.name();
					Long value = (Long)to.getValue(name);
				
					if(value!=null && value>0){
							Entity newInstance = (Entity)reflectionField.getType().newInstance();
							
							newInstance.setId((Long)value );
							reflectionField.set(this, newInstance);
					}
					continue;
				}
				
				} catch (Exception e) {
					String name = null;
					if(annoFieldPK!=null) name = ((GPAPrimaryKey)annoFieldPK).name();
					if(annoField!=null) name = ((GPAField)annoField).name();
					if(annoFieldBean!=null) name = ((GPAFieldBean)annoFieldBean).name();
					Log.e("ERROR","name = " + name + " " + e.getLocalizedMessage(),e); 
				}
			}

		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
	}
	/**
	 * method used to get a table name of entity
	 * based in entity class name 
	 * @return String table name
	 */
	public String getTableName(){
		GPAEntity annoTable = (GPAEntity) this.getClass().getAnnotation(GPAEntity.class);
		String tableName = annoTable.name();
		return tableName;
	}
	public void printInfo(){
		Field[] fields = getDeclaredFields();
		
		for(int i=0; i<fields.length; i++){
			Field reflectionField = fields[i];
			reflectionField.setAccessible(true);
			String fieldName = reflectionField.getName();
			Log.i("Info", String.format("Column name : {0}, index : {1}", fieldName, i));
		}
	}
	public boolean isPkInformed(){
		boolean pkInfomed = (getId()!=null);
		return pkInfomed;
	}
	public List<FieldTO> getPKFieldTos(){
		try {
			Field[] fields = getDeclaredFields();
			ArrayList<FieldTO> fieldTos = new ArrayList<FieldTO>();
			for(int i=0; i<fields.length; i++){
				Field reflectionField = fields[i];
				reflectionField.setAccessible(true);
				Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
				if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
					GPAPrimaryKey pk = (GPAPrimaryKey)annoFieldPK;
					//String name = pk.name();
					//Object value = to.getValue(name);
					//if(value!=null){
					//	reflectionField.set(this, value);
					//}
					
					String fieldName = reflectionField.getName();
					Object fieldValue = reflectionField.get(this);
					fieldTos.add(new FieldTO(fieldName, fieldValue));
					
					continue;
				}
				
				
				
			}
			return fieldTos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public Field[] getDeclaredFields(){
		return EntityUtil.getDeclaredFields(true,this.getClass());
	}


	public List<FieldTO> getFieldTos() {
		try {
			Field[] fields = getDeclaredFields();
			ArrayList<FieldTO> fieldTos = new ArrayList<FieldTO>();
			for(int i=0; i<fields.length; i++){
				Field reflectionField = fields[i];
				reflectionField.setAccessible(true);
				String fieldName = reflectionField.getName();
				Object fieldValue = reflectionField.get(this);
				fieldTos.add(new FieldTO(fieldName, fieldValue));
			}
			return fieldTos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public FieldTO getFieldByName(String fieldColName) {
		try {
			Field[] fields = getDeclaredFields();
			
			for(int i=0; i<fields.length; i++){
				Field reflectionField = fields[i];
				reflectionField.setAccessible(true);
				String fieldName = reflectionField.getName();
				if(fieldName.equalsIgnoreCase(fieldColName)){
					return getField(i);
				}else{
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public FieldTO getField(int fieldCol) {
		try {
			Field[] fields = getDeclaredFields();
			Field reflectionField = fields[fieldCol];
			reflectionField.setAccessible(true);
			String fieldName = reflectionField.getName();
			Object fieldValue = reflectionField.get(this);
			return new FieldTO(fieldName, fieldValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	 @Override
     public boolean equals(Object obj) {
		 Entity other = (Entity) obj;
		 if(this.getId()==other.getId()){
			 return true;
		 }else{
			 return false;
		 }
	 }
	 
}
