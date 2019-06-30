package br.com.database.dao.model;

import java.io.Serializable;


public class FieldTO implements Serializable{
	
    private String name;
    private Object value;
    private int fieldType;
    public FieldTO(String fieldName) {
        this.setName(fieldName);
       
    }
    public FieldTO(String fieldName, Object fieldValue) {
        this.setName(fieldName);
        this.setValue(fieldValue);
    }
    public FieldTO(String fieldName, Object fieldValue, int fieldType) {
        this.setName(fieldName);
        this.setValue(fieldValue);
        this.setFieldType(fieldType);
    }

    public String getName() {
        return name;
    }

    public void setName(String fieldName) {
        this.name = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object fieldValue) {
        this.value = fieldValue;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }
}
