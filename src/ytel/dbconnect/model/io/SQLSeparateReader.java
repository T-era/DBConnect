package ytel.dbconnect.model.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ytel.dbconnect.config.Configure;
import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.LabeledSql;
import ytel.dbconnect.model.SQLReader;

/**
 * 複数のSQLを、ファイルから区切って読み込むReader。
 * <p/>このクラスの実装では、読み込み処理は全てコンストラクタで行います。<br/>
 * {@link #SQLSeparateReader(File, Configure)}コンストラクタでファイルから内容を取得してから、{@link #hasMore()}と
 * {@link #read()}メソッドで内容を取得します。<br/>
 * {@link #close()}メソッドは何も行いません。(ストリームの破棄は、読み込み直後にコンストラクタ内で完了します。)
 * <p/>SQLの区切り文字と、コメントアウトの記号をアプリケーション({@link Configure})の設定から取得します。
 *
 * @author y-terada
 *
 */
final class SQLSeparateReader implements SQLReader{
	/**
	 * 読み込み結果のSQL
	 */
	private final Iterator<LabeledSql> labeles;
	private final Configure config;

	/**
	 * 指定したファイルから、SQL情報を取得します。<p/>
	 * このクラスの実装では、このコンストラクタ内部で、ファイルの中身をパースして
	 * {@link #labeles}に保持します。
	 * @param file 読み込み対象のファイル
	 * @throws IOMessagingException
	 */
	SQLSeparateReader(File file, Configure config) throws IOException{
		this.config = config;
		if (! file.exists()){
			throw new IOException("読み込み対象のファイルが存在しません");
		}

		this.labeles = getIterator(file);
	}

	/**
	 * ファイルを読込み、その内容をIteratorとして返します。
	 * @param file 読み込み対象のファイル
	 * @return SQLの一覧
	 * @throws IOMessagingException
	 */
	private Iterator<LabeledSql> getIterator(final File file) throws IOException{
		// 読み込み結果を保持するためのList
		List<LabeledSql> list = new ArrayList<LabeledSql>();
		StringBuilder sql = new StringBuilder();
		String label = "TEMP";

		BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(file)) );
		try{
			String aLine = null;

			int counter = 0;

			// SQL中の最初の行の場合のみTrue(SQLの文末記号でtrueにする)
			boolean newLine = true;
			while( ( aLine = br.readLine() ) !=null ){
				if (newLine){
					// 最初の行がコメントなら、その行をラベルにする
					if (aLine.startsWith(config.user.getSqlCommentOut())){
						label = aLine.replaceFirst(config.user.getSqlCommentOut(), "");
						continue;
					}else{
						label = file.getName() + counter++;
					}
				}

				if (aLine.endsWith(";")) {
					// 行末が";"の場合は、行で区切って別PaneにSQLを表示する。

					// 行末の";"は取り除く(JDBC経由でのSQL実行時にエラーになるため)
					sql.append( aLine.substring(0, aLine.length()-1) );

					list.add(new LabeledSqlImpl(label, new String(sql)));

					sql = new StringBuilder();
					newLine = true;
				}else{
					sql.append( aLine );
					sql.append( System.getProperty("line.separator") );

					newLine = false;
				}
			}
			if (sql.length() != 0){
				// 最後の行に;がない場合
				list.add(new LabeledSqlImpl(label, new String(sql)));
			}
		}finally{
			br.close();
		}
		return list.iterator();
	}

	/**
	 * このクラスの実装では、Iteratorにまだ要素があるかどうかを返します。
	 */
	@Override
	public boolean hasMore() {
		return this.labeles.hasNext();
	}

	/**
	 * このクラスの実装では、Iteratorの次の要素を返します。
	 */
	@Override
	public LabeledSql read() throws ActionInterruptException{
		return labeles.next();
	}

	/**
	 * このクラスの実装では、何もしません。害もありません。
	 */
	@Override
	public void close() throws ActionInterruptException{
	}
}
