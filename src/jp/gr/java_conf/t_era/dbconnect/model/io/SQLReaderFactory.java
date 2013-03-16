package jp.gr.java_conf.t_era.dbconnect.model.io;

import java.io.File;
import java.io.IOException;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.SQLReader;

/**
 * File読取りのオブジェクトを管理します。
 * 要求に応じて、インスタンスを生成します。
 * @author y-terada
 *
 */
public class SQLReaderFactory {
	/**
	 * 単一SQLをファイルから読み出すための{@link SQLReader}インスタンスを返します。
	 * @param inputFile 読み出すファイル
	 * @param messageObserver ファイル操作中のメッセージを監視するオブザーバ
	 * @return 読み出し用オブジェクト
	 * @throws IOException
	 */
	public static SQLReader getSingleSQLReader(File inputFile, MessageObserver messageObserver, Configure config) throws IOException{
		return new SQLSingleReader(inputFile, messageObserver, config);
	}

	/**
	 * 複数SQLをファイルから読み出すための{@link SQLReader}インスタンスを返します。
	 * @param inputFile 読み出すファイル
	 * @param messageObserver ファイル操作中のメッセージを監視するオブザーバ
	 * @return 読み出し用オブジェクト
	 * @throws IOException
	 */
	public static SQLReader getSeparateSQLReader(File inputFile, MessageObserver messageObserver, Configure config) throws IOException{
		return new SQLSeparateReader(inputFile, config);
	}
}
