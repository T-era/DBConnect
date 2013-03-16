package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * SQLの読み込みを行うクラスのインターフェース
 * @author y-terada
 *
 */
public interface SQLReader {
	/**
	 * SQL({@link LabeledSql})を読込み、読み込み結果を返します。
	 * <p/>このメソッドを呼ぶ前に、{@link #hasMore()}メソッドで検査を行います。
	 * @return 読み込み結果
	 * @throws ActionInterruptException
	 */
	LabeledSql read() throws ActionInterruptException;
	/**
	 * オブジェクトが保持するストリームを閉じます。
	 * @throws ActionInterruptException
	 */
	void close() throws ActionInterruptException;
	/**
	 * さらに読込み可能なSQLがあるかどうかを判定します。
	 * <p/>この結果がtrueの場合、{@link #read()}メソッドでSQLを取得できることを意味します。
	 * @return 読込み可能ならtrue
	 */
	boolean hasMore();
}
