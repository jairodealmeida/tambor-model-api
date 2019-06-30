package br.com.database.dao.statement.transacts;


public interface Insertable extends Transactionable{

    public StringBuilder getParameters();

}
