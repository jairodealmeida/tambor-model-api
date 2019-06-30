package br.com.database.dao.statement.transacts;

import br.com.database.dao.statement.operation.WhereStatement;

public interface Selectable {
	public WhereStatement where = new WhereStatement();
	public StringBuilder createStatement();
	
}
