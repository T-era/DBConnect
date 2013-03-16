package jp.gr.java_conf.t_era.dbconnect.model.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.ActionInterruptException;
import jp.gr.java_conf.t_era.dbconnect.model.LabeledSql;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.SQLReader;

/**
 * ファイルの内容を区切らずに、一つのStringとして読み込むReader
 * @author y-terada
 *
 */
final class SQLSingleReader implements SQLReader{
	/**
	 * 読み込み対象のファイル
	 */
	private final File file;
	private final MessageObserver messageObserver;
	private final Configure config;

	SQLSingleReader(File inputFile, MessageObserver messageObserver, Configure config) throws IOException{
		if (! inputFile.exists()){
			throw new IOException("読み込み対象のファイルが存在しません");
		}
		this.file = inputFile;
		this.messageObserver = messageObserver;
		this.config = config;
	}

	/**
	 * この実装では、何も行いません。害もありません。
	 */
	@Override
	public void close() throws ActionInterruptException {
	}

	private boolean theFirst = true;
	/**
	 * 最初の一回のみtrueを返します。
	 * <p/>このクラスの実装はファイルの内容を1レコードとして返すため、
	 * 最初の一回以外は常にfalseです。
	 */
	@Override
	public boolean hasMore() {
		if (theFirst){
			theFirst = false;
			return true;
		}else{
			return false;
		}
	}

	/**
	 * ファイルの内容を読み込みます。
	 * {@link #hasMore()}の結果に関わらず、ファイルの内容全てを返します。
	 */
	@Override
	public LabeledSql read() throws ActionInterruptException {
		StringBuilder sql = new StringBuilder();
		String label = file.getName();

		try{
			BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(file)) );
			try{
				String aLine = null;

				boolean firstLine = true;
				while( ( aLine = br.readLine() ) !=null ){
					if (firstLine){
						if (aLine.startsWith(config.user.getSqlCommentOut())){
							label = aLine.replaceFirst(config.user.getSqlCommentOut(), "");
						}
						firstLine = false;
					}
					sql.append( aLine );
					sql.append( System.getProperty("line.separator") );
				}
			}finally{
				br.close();
			}
		}catch(IOException e){
			messageObserver.setErrorMessage("ファイル読込み中にエラーが発生しました。", e);
			throw new ActionInterruptException("ファイル読込み中にエラーが発生しました。", e);
		}
		return new LabeledSqlImpl(label, new String(sql));
	}
}
