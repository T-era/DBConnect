package jp.gr.java_conf.t_era.dbconnect.model.database;

import java.sql.SQLException;
import java.util.List;

import jp.gr.java_conf.t_era.dbconnect.model.ConnectionFactory;

/**
 * 適切なクエリ実行オブジェクトを生成するためのクラス。
 * 
 * @author y-terada
 *
 */
public class QueryExecuterFactory {
	/**
	 * 適切なクエリ実行オブジェクトを生成して返します。
	 * @param connectionFactory 接続情報
	 * @param parameters 引数の一覧
	 * @return クエリ実行オブジェクト
	 */
	public static QueryExecuter getExecuter(ConnectionFactory connectionFactory, List<String> parameters){
		if (parameters == null) {
			return new QueryExecNoArgs(connectionFactory);
		} else {
			QueryExecWithArgs eq = new QueryExecWithArgs(connectionFactory);
			eq.setParameter(parameters);
			return eq;
		}
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
	public static int getParameterCount(ConnectionFactory connectionFactory, String sql) throws SQLException{
		return QueryExecWithArgs.getParameterCount(connectionFactory, sql);
	}
}
