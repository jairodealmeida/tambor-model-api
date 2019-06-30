package br.com.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSQLiteImpl implements DatabaseDAO{

	@Override
	public void initialize(String driver, String url, String user, String pass)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean connect(boolean commit) throws SQLException,
			ClassNotFoundException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PreparedStatement prepareStatement(String statement) {
		// TODO Auto-generated method stub
		return null;
	}

}
