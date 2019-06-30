package br.com.database.dao.statement.operation;


import br.com.database.dao.statement.transacts.Selectable;

/**
 * Class to make a selection SQL statement
 * @author jairo.almeida
 * @since 0.0.1	
 */
public class SelectStatement implements Selectable {
	
	private StatementArguments arguments;
	
	public SelectStatement(StatementArguments arguments){
        this.arguments = arguments;
    }

	//TODO creating a create statements to select
	public StringBuilder createStatement() {
		if(arguments!=null ){
			StringBuilder result = new StringBuilder();
			result.append("SELECT ");
			if(arguments.getMaxField()!=null){
				result.append("MAX(").append(arguments.getMaxField()).append(") ");	
			}else{
				if(arguments.getCollumns()!=null && 
				   arguments.getCollumns().length>0	){
					
				
						for (int i=0 ; i < arguments.getCollumns().length; i++) {
							String collumnName = arguments.getCollumns()[i];
							result.append(collumnName);
							if(i<=arguments.getCollumns().length-1){
								result.append(",");
							}
						}
					
				}else{
					result.append("* ");
				}
			}
			result.append("FROM ");
			result.append(arguments.getTableName());
			if(arguments.getWhereClause()!=null){
				//result.append(" WHERE ");
				result.append(arguments.getWhereClause());	
			}
			if(arguments.getOrderByClause()!=null){
				result.append(arguments.getOrderByClause());	
			}
			return result;
		}else{
			throw new NullPointerException("table name is null");
		}
	}


}
