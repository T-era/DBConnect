package jp.gr.java_conf.t_era.dbconnect.model.database;

import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.QueryResultView;

/**
 * クエリ実行モジュールが公開するインターフェースです。
 * 
 * @author y-terada
 *
 */
public interface QueryExecuter {
	/**
	 * クエリを実行し、実行結果を返します。<br>
	 * クエリの実行時に例外が発生する場合、例外の処理はこのメソッド内で完結します。
	 * @param sql 実行するクエリ
	 * @param messageObserver 実行時メッセージを受取るオブザーバ。例外等の情報は、このオブジェクトに通知されます。
	 * @return クエリ実行結果
	 * @throws ActionInterruptException UIのアクションが中断されるべき場合、スローされます。
	 * 　UIは、アクションの残りの処理を行ってはいけません。
	 */
	QueryResultView exec(String sql, MessageObserver messageObserver) throws ActionInterruptException;
}
