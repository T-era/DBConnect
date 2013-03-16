package jp.gr.java_conf.t_era.dbconnect.model.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.model.LabeledSql;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.SQLWriter;

/**
 * SQLを指定されたファイルに書き込みます。
 * @author y-terada
 *
 */
final class SQLSingleFileWriter implements SQLWriter{
	private final BufferedWriter bw;
	private final MessageObserver messageObserver;
	private final Configure config;
	/**
	 * ファイルを指定するコンストラクタ
	 * <p/>この実装では、コンストラクトのタイミングで指定されたファイルに対する
	 * ストリームを開きます。
	 * @param outputFile 書込み対象ファイル
	 * @throws IOMessagingException
	 */
	SQLSingleFileWriter(File outputFile, MessageObserver messageObserver, Configure config) throws IOException{
		if (! outputFile.exists()){
			boolean resCreate = outputFile.createNewFile();
			if (!resCreate)
				throw new IOException("ファイルを作成できませんでした。");
		}
		this.messageObserver = messageObserver;
		this.config = config;
		bw = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(outputFile)) );
	}

	/**
	 * ファイルに指定された内容を書き込みます。
	 */
	@Override
	public void write(LabeledSql sql) throws ActionInterruptException{
		write(sql.getLabel(), sql.getSql());
	}

	/**
	 * ファイルに指定された内容を書き込みます。
	 */
	@Override
	public void write(String label, String entry) throws ActionInterruptException{
		try{
			bw.write(config.user.getSqlCommentOut());
			bw.write(label);
			bw.newLine();
	
			bw.write(entry);
			bw.write(";");
			bw.newLine();
		}catch(IOException e){
			messageObserver.setFatalMessage("ファイル書出し中にエラーが発生しました。", e);
			throw new ActionInterruptException("ファイル書出し中にエラーが発生しました。", e);
		}
	}

	/**
	 * ストリームを閉じます。
	 * <p/>この実装では、ストリームを開くのはコンストラクタなので、
	 * このクラスのすべてのオブジェクトはこのメソッドでストリームの
	 * 処理を行う必要があります。
	 */
	@Override
	public void close() throws ActionInterruptException{
		try{
			bw.close();
		}catch(IOException e){
			messageObserver.setFatalMessage("書出しストリームの後処理に失敗しました。", e);
			throw new ActionInterruptException("書出しストリームの後処理に失敗しました。", e);
		}
	}
}
