package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * メッセージの表示を扱うインターフェース。
 * <p>
 * メッセージ表示を伴うUIは、このインターフェースを実装した表示部分を作ります。<br>
 * 例外/アプリケーションエラー/処理状況等表示を要求するモジュールは、このインターフェースに向けてメッセージを
 * 投げかけることで、表示実体を意識せずにメッセージを送出します。
 * 
 * @author y-terada
 *
 */
public interface MessageObserver {
	/**
	 * いわゆるINFOレベルの情報を表示します。
	 * @param message
	 */
	void setMessage(String message);
	/**
	 * いわゆるERRORレベルの情報を表示します。
	 * @param message
	 */
	void setErrorMessage(String message);
	/**
	 * いわゆるERRORレベルの情報を表示します。
	 * 例外を受け取り、例外に含まれる(スタックトレースなどの)必要な情報を表示に加えます。
	 * @param message
	 * @param t 発生した例外
	 */
	void setErrorMessage(String message, Throwable t);
	/**
	 * いわゆるFATALレベルの情報を表示します。
	 * 例外を受け取り、例外に含まれる(スタックトレースなどの)必要な情報を表示に加えます。
	 * @param message
	 */
	void setFatalMessage(String message);
	/**
	 * いわゆるFATALレベルの情報を表示します。
	 * 例外を受け取り、例外に含まれる(スタックトレースなどの)必要な情報を表示に加えます。
	 * @param message
	 * @param t 発生した例外
	 */
	void setFatalMessage(String message, Throwable t);
}
