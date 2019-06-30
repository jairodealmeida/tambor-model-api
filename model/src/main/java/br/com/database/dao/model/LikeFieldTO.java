package br.com.database.dao.model;

public class LikeFieldTO extends FieldTO{

	public LikeFieldTO(String fieldName) {
		super(fieldName);
	}
	public LikeFieldTO(String fieldName, Object fieldValue) {
		super(fieldName,fieldValue);
    }
    public LikeFieldTO(String fieldName, Object fieldValue, int fieldType) {
    	super(fieldName,fieldValue,fieldType);
    }
}
