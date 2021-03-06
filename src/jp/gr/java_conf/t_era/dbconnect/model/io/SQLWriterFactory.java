package jp.gr.java_conf.t_era.dbconnect.model.io;

import java.io.File;
import java.io.IOException;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.SQLWriter;

/**
 * File書出しのオブジェクトを管理します。
 * 要求に応じて、インスタンスを生成します。
 * @author y-terada
 *
 */
public class SQLWriterFactory {
	/**
	 * 単一ファイルに書出しを行う{@link SQLWriter}インスタンスを返します。
	 * @param outputFile 書出し対象のファイル
	 * @param messageObserver 書出し処理中のメッセージを監視するオブザーバ
	 * @return 書出し用オブジェクト
	 * @throws IOException
	 */
	public static SQLWriter getSingleFileSQLWriter(File outputFile, MessageObserver messageObserver, Configure config) throws IOException{
		return new SQLSingleFileWriter(outputFile, messageObserver, config);
	}
}
