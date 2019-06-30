package br.com.database.dao.statement.operation;

public class StatementArguments {
	private String tableName;
	private String[] collumns;
	private String whereClause;
	private String maxField;
	private String orderByClause;
	
	public StatementArguments(String tableName){
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getCollumns() {
		return collumns;
	}

	public void setCollumns(String[] collumns) {
		this.collumns = collumns;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getMaxField() {
		return maxField;
	}

	public void setMaxField(String maxField) {
		this.maxField = maxField;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}
}
