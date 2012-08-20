package ytel.dbconnect.model.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;

import ytel.dbconnect.model.ActionInterruptException;
import ytel.dbconnect.model.DataTableWriter;
import ytel.dbconnect.model.MessageObserver;

/**
 * 表形式のデータをCSVに出力するための、ファイル書込みモジュール
 * @author y-terada
 *
 */
public class CsvWriter implements DataTableWriter{
	private final File targetFile;
	private final MessageObserver messageObserver;

	public CsvWriter(File targetFile, boolean takeRecurssiveBackup, MessageObserver messageObserver) {
		this.targetFile = targetFile;
		this.messageObserver = messageObserver;

		if (targetFile.exists() && takeRecurssiveBackup) {
			backup(targetFile);
		}
	}

	/**
	 * 指定されたファイルをバックアップします。<p/>
	 * バックアップとは、元ファイルと同じディレクトリに、"_bk"という接尾語をつけてリネームすることです。
	 * バックアップ先のファイル名(元ファイル名+"_bk")が既に存在する場合、再帰的にバックアップを繰り返します。
	 * @param file バックアップ対象のファイル
	 */
	private static void backup(File file){
		File toFile = new File(file.getPath() + "_bk");
		if (toFile.exists()){
			backup(toFile);
		}
		file.renameTo(toFile);
	}

	@Override
	public void write(Object[] titles, Object[][] values) throws ActionInterruptException{
		// 入力チェック
		for (int i = 0; i < values.length; i ++) {
			if (titles.length != values[i].length) {
				throw new ActionInterruptException(MessageFormat.format("タイトルの数と、合致しない列がある(Line:{0})", new Object[]{i}), null);
			}
		}

		try {
			writeIo(titles, values);
			messageObserver.setMessage("書出し完了");
		} catch (IOException e) {
			messageObserver.setFatalMessage("CSVファイルの書き出しに失敗しました。", e);
			throw new ActionInterruptException("CSVファイルの書き出しに失敗しました。", e);
		}
	}

	/**
	 * ファイルに、一覧表の内容をCSV形式で出力します。
	 * @param titles 一覧表のタイトル
	 * @param values 一覧表の内容
	 * @throws IOException
	 */
	private void writeIo(Object[] titles, Object[][] values) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
		try{
			boolean isFirstElement = true;
			for (Object title: titles){
				if (isFirstElement) {
					isFirstElement = false;
				}else{
					writer.write(",");
				}

				if (title != null){
					writer.write(title.toString());
				}
			}
			writer.newLine();

			for (Object[] aLine: values){
				isFirstElement = true;
				for (Object value: aLine){
					if (isFirstElement) {
						isFirstElement = false;
					}else{
						writer.write(",");
					}
					if (value != null){
						writer.write(value.toString());
					}
				}
				writer.newLine();
			}
		}finally{
			writer.close();
		}
	}
}
