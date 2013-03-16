package jp.gr.java_conf.t_era.dbconnect.model;

/**
 * ラベル（タイトル）付きのSQL<br/>
 * @author y-terada
 *
 */
public interface LabeledSql {
	/**
	 * ラベル（タイトル）を返します
	 * @return ラベル
	 */
	String getLabel();
	/**
	 * SQLを返します。
	 * @return SQL(文字列型)
	 */
	String getSql();
}
