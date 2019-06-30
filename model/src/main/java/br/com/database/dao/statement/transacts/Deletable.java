package br.com.database.dao.statement.transacts;


import br.com.database.dao.statement.operation.WhereStatement;

public interface Deletable extends Transactionable{
    public WhereStatement where = new WhereStatement();
}
