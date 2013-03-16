package jp.gr.java_conf.t_era.dbconnect.model;

import java.sql.Connection;

/**
 * {@link ConnectionFactoryImpl}クラス内でのコネクションの再取得を監視する、オブザーバです。<p/>
 * コネクションの再取得を検知する必要がある場合、このクラスを継承し、{@link #actionPerformed(Connection, ConnectionInfo)}
 * に再取得の際の動作を実装します。<p/>
 * @author y-terada
 *
 */
public abstract class ConnectionObserver{
	private ConnectionObserver next = null;
	/**
	 * アプリケーションが参照するDBコネクションが変更された場合に発生するイベントを
	 * 処理します。<br>
	 * 監視対象は、コネクション変更時にこのメソッドをコールします。
	 * @param newConnection 新たに取得したコネクション
	 */
	final void callAction(Connection newConnection, ConnectionInfo newInfo){
		this.actionPerformed(newConnection, newInfo);
		if (next != null){
			next.callAction(newConnection, newInfo);
		}
	}

	/**
	 * アプリケーションが参照するDBコネクションが変更された場合に発生するイベントを
	 * 記述します。
	 * @param newConnection 新たに取得したコネクション
	 */
	protected abstract void actionPerformed(Connection newConnection, ConnectionInfo newInfo);

	/**
	 * このオブジェクトに、別のオブザーバを追加します。<p/>
	 * 追加したオブザーバは、このオブジェクトがイベント処理を行った後にイベント処理を行います。<p/>
	 * @param newObserver 追加するオブザーバ
	 */
	final void add(ConnectionObserver newObserver){
		if (this.next == null){
			next = newObserver;
		} else {
			next.add(newObserver);
		}
	}
}
