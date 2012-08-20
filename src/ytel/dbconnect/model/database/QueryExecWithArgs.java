package ytel.dbconnect.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.ConnectionFactory;
import ytel.dbconnect.model.MessageObserver;
import ytel.dbconnect.model.QueryResultView;
import ytel.dbconnect.model.database.queryresultview.NullViewInfo;
import ytel.dbconnect.model.database.queryresultview.QueryResultViewImpl;

/**
 * 置き換え文字列アリで、SQLを実行します。
 * @author y-terada
 *
 */
class QueryExecWithArgs implements QueryExecuter{
	private List<String>parameters;
	private final ConnectionFactory connectionFactory;

	QueryExecWithArgs(ConnectionFactory connectionFactory){
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 指定されたSQLのパラメータ引き換え文字の数を取得します。
	 * 
	 * # OracleがConnection#getParameterMetaDataをサポートしないせいで、構文解析が必要。
	 * # 現状ではコメントや文字列リテラルに?を含むSQLはうまく動作しない。
	 * @param connectionFactory
	 * @param sql
	 * @return SQL実行時に必要となるパラメータの数
	 * @throws SQLException
	 */
	static int getParameterCount(ConnectionFactory connectionFactory, String sql) throws SQLException{
		return sql.replaceAll("[^?]", "").length();
		/*
		TODO Connection#getParameterMetaDataをサポートするJDBCドライバでは、以下の方がスマートか。
		
		Connection con = connectionFactory.getConnection();
		PreparedStatement stmt = con.prepareStatement(sql);
		try{
			return stmt.getParameterMetaData().getParameterCount();
		}finally{
			stmt.close();
		}*/
	}

	void setParameter( List<String> parameters ){
		this.parameters = Collections.unmodifiableList(parameters);
	}

	@Override
	public QueryResultView exec(String sql, MessageObserver messageObserver) throws ActionInterruptException{
		try{
			Connection con = connectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			for (int index = 0; parameters != null && index < parameters.size(); index ++){
				stmt.setString(index + 1, parameters.get(index));
			}
			try{
				return execSQL(stmt, sql, messageObserver);
			}finally{
				stmt.close();
			}
		} catch (SQLException e) {
			messageObserver.setErrorMessage("SQL実行失敗", e);
			throw new ActionInterruptException("SQL実行失敗", e);
		}
	}

	/**
	 * Statementに対してSQLの実行をし、結果を{@link QueryResultView}として返します。
	 * @param stmt クエリを実行するDBステートメント
	 * @param sql 実行するSQL
	 * @param messageObserver 実行時メッセージを受取るオブザーバ
	 * @return 検索結果
	 * @throws SQLException
	 */
	private QueryResultView execSQL( PreparedStatement stmt, String sql, MessageObserver messageObserver ) throws SQLException{
		ResultSet rslt = null;

		if( stmt.execute() ){
			System.gc();
			long start = System.currentTimeMillis();
			rslt = stmt.getResultSet();
			long sqlExectTime = System.currentTimeMillis() - start;

			try{
				return QueryResultViewImpl.parseViewInfo(rslt, sqlExectTime);
			}finally{
				rslt.close();
				if (messageObserver != null) {
					messageObserver.setMessage("検索実行");
				}
			}
		}else{
			if (messageObserver != null) {
				messageObserver.setMessage("更新実行");
			}
			System.gc();
			long start = System.currentTimeMillis();
			int updateCount = stmt.getUpdateCount();
			long sqlExectTime = System.currentTimeMillis() - start;

			return new NullViewInfo("更新実行", updateCount, sqlExectTime);
		}
	}
}
