package br.com.database.dao.model;

public class ORFieldTO extends FieldTO{
	public ORFieldTO(String fieldName) {
		super(fieldName);
	}
	public ORFieldTO(String fieldName, Object fieldValue) {
		super(fieldName,fieldValue);
    }
    public ORFieldTO(String fieldName, Object fieldValue, int fieldType) {
    	super(fieldName,fieldValue,fieldType);
    }
}
