package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * GUIのイベント処理中に、モジュール内で発生した例外を通知します。
 * <p>
 * 例外に対する適切な処理(例えば、UIへの通知表示など)は、モジュール側が責任を持ちます。
 * この例外は、アクションの(例外発生以降の)処理が継続して行われないことを保障するためのマーカです。
 * <p>
 * UI側では、この例外を握りつぶすことができます。<br>
 * ただし、原則的にそれはListener内のアクション処理メソッドの末尾で行うべきです。
 * <p>
 * <strong>
 * このクラスが、causeやmessageをコンストラクタで要求するのは、純粋にデバッグの際の利便性のためです。
 * このクラスのgetMessage()やgetCause()を使用するコードは、原則的に誤りと考えるべきです。
 * </strong>
 *
 * @author y-terada
 *
 */
public class ActionInterruptException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ActionInterruptException(String message, Throwable cause){
		super(message, cause);
	}

	@Override
	@Deprecated
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	@Deprecated
	public Throwable getCause() {
		return super.getCause();
	}

}
