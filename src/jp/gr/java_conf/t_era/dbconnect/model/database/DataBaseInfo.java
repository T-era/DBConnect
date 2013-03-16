package jp.gr.java_conf.t_era.dbconnect.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.ConnectionFactory;
import jp.gr.java_conf.t_era.dbconnect.model.database.queryresultview.QueryResultViewImpl;

/**
 * テーブルとカラム情報の取得を行うクラス
 * @author y-terada
 *
 */
public class DataBaseInfo extends QueryExecNoArgs{
	private final Configure config;

	/**
	 * コネクションのファクトリを指定するコンストラクタ
	 * @param connectionFactory
	 */
	public DataBaseInfo(ConnectionFactory connectionFactory, Configure config){
		super( connectionFactory );
		this.config = config;
	}

	/**
	 * テーブル一覧を取得します。
	 * @return テーブル一覧
	 * @throws SQLException
	 */
	public QueryResultViewImpl getTableInfo() throws SQLException{
		Connection con = connectionFactory.getConnection();
		Statement stmt = con.createStatement();
		try{
			System.gc();
			long start = System.currentTimeMillis();
			ResultSet rslt = stmt.executeQuery(config.user.getTableInfoQuery());
			long sqlExectTime = System.currentTimeMillis() - start;
			try{
				return QueryResultViewImpl.parseViewInfo(rslt, sqlExectTime);
			}finally{
				rslt.close();
			}
		}finally{
			stmt.close();
		}
	}

	/**
	 * カラム一覧を取得します。
	 * @param tableName カラム一覧を取得する対象のテーブル名
	 * @return カラム一覧
	 * @throws SQLException
	 */
	public QueryResultViewImpl getColumnInfo( String tableName ) throws SQLException{
		Connection con = connectionFactory.getConnection();
		String sql = config.user.getColumnInfoQuery().replaceAll(config.user.getColumnInfoQuery_TableNameReplacer(), tableName);
		PreparedStatement stmt = con.prepareStatement(sql);
		try{
			System.gc();
			long start = System.currentTimeMillis();
			ResultSet rslt = stmt.executeQuery();
			long sqlExectTime = System.currentTimeMillis() - start;
			try{
				return QueryResultViewImpl.parseViewInfo(rslt, sqlExectTime);
			}finally{
				rslt.close();
			}
		}finally{
			stmt.close();
		}
	}
}
