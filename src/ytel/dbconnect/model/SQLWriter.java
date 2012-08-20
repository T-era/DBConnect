package ytel.dbconnect.model;


/**
 * SQLの書き込みを行うクラスのインターフェース
 * @author y-terada
 *
 */
public interface SQLWriter {
	/**
	 * 書込みを行います。
	 * @param sql 書き込む内容のSQL
	 * @throws ActionInterruptException
	 */
	void write(LabeledSql sql) throws ActionInterruptException;
	/**
	 * ラベル（タイトル）と内容を指定して、書き込みを行います。
	 * @param label ラベル
	 * @param entry 内容
	 * @throws ActionInterruptException
	 */
	void write(String label, String entry) throws ActionInterruptException;
	/**
	 * オブジェクトが保持するストリームを破棄します。
	 * @throws ActionInterruptException
	 */
	void close() throws ActionInterruptException;
}
