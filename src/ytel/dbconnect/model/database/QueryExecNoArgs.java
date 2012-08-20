package ytel.dbconnect.model.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.ConnectionFactory;
import ytel.dbconnect.model.MessageObserver;
import ytel.dbconnect.model.QueryResultView;
import ytel.dbconnect.model.database.queryresultview.NullViewInfo;
import ytel.dbconnect.model.database.queryresultview.QueryResultViewImpl;

/**
 * �u������������i�V�ŁASQL�����s���܂��B
 * @author y-terada
 *
 */
class QueryExecNoArgs implements QueryExecuter {
	protected final ConnectionFactory connectionFactory;

	QueryExecNoArgs(ConnectionFactory connectionFactory){
		this.connectionFactory = connectionFactory;
	}

	@Override
	public QueryResultView exec( String sql, MessageObserver messageObserver ) throws ActionInterruptException{
		try {
			Connection con = connectionFactory.getConnection();
			Statement stmt = con.createStatement();
			try{
				QueryResultView ret = execSQL( stmt, sql, messageObserver );
	
				return ret;
			}finally{
				stmt.close();
			}
		} catch (SQLException e) {
			messageObserver.setErrorMessage("�������s", e);
			throw new ActionInterruptException("�������s", e);
		}
	}

	private QueryResultView execSQL(final Statement stmt, String sql, MessageObserver messageObserver) throws SQLException{
		ResultSet rslt = null;

		String[] sqlArray = splitSql( sql );
// TODO ������SQL�ɑΉ��������B
for(String s: sqlArray ){
	System.out.println( "**TEST**" + s );
}
		System.out.println( "DEBUG:" + sql );
		if( stmt.execute( sql ) ){
			System.gc();
			long start = System.currentTimeMillis();
			rslt = stmt.getResultSet();
			long sqlExectTime = System.currentTimeMillis() - start;
			try{
				return QueryResultViewImpl.parseViewInfo(rslt, sqlExectTime);
			}finally{
				rslt.close();
				if (messageObserver != null){
					messageObserver.setMessage("�������s");
				}
			}
		}else{
			if (messageObserver != null){
				messageObserver.setMessage("�X�V���s");
			}

			System.gc();
			long start = System.currentTimeMillis();
			int updateCount = stmt.getUpdateCount();
			long sqlExectTime = System.currentTimeMillis() - start;

			return new NullViewInfo("�X�V���s", updateCount, sqlExectTime);
		}
	}
	private static String[] splitSql( String material ) throws SQLException{
		String withoutComment = material.replaceAll("--.*[\r\n]", "");
		return withoutComment.split(";");
	}
}
