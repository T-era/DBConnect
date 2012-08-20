package ytel.dbconnect.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ユーザ設定を管理します。
 * <p>
 * このクラスの実装では、ユーザ設定は指定フォルダの特定のサフィックスのファイル全てが読み出し対象になります。<br>
 * ユーザ設定のファイルが１つの場合には、その設定ファイルが設定として反映します。
 * ユーザ設定のファイルが複数ある場合には、そのうちの一つを選択して、アプリケーションに反映します。<br>
 * 選択の方法は、{@link ConfigureChooser}の実装により、ダイアログ表示です。
 * @author y-terada
 *
 */
class UserConfigureManager {
	private List<NamedConfigure> configureList;

	/**
	 * ディレクトリとファイル名サフィックスを指定して、ユーザ設定ファイルの読み出しを行います。
	 * <p>指定の条件に合致するファイルに、フォーマットが異なるものが含まれていた場合、または条件に合致するフォルダが存在
	 * する場合の動作は保障しません。
	 * </p>
	 * @param confFileDirectory
	 * @param filaNameSufix
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	UserConfigureManager(String confFileDirectory, final String filaNameSufix) throws FileNotFoundException, IOException {
		File directory = new File(confFileDirectory);
		configureList = new ArrayList<NamedConfigure>();
		FilenameFilter filter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(filaNameSufix);
			}
		};
		for (File file: directory.listFiles(filter)) {
			String name = file.getName().replaceAll(filaNameSufix, "");
			configureList.add(new NamedConfigure(name, new PropertyReader(file)));
		}
	}

	/**
	 * ユーザ設定ファイルを選択します。<br>
	 * 選択肢が一つの場合はレスポンスは瞬間に起こります。複数の選択肢がある場合には、このメソッドの完了する時間は
	 * 不定です。
	 * @return ユーザ設定ファイル
	 * @throws InterruptedException
	 */
	UserConfigure chooseOne() throws InterruptedException {
		if (configureList.size() == 1) {
			return configureList.get(0).getConfigure();
		} else {
			return userChooseOne();
		}
	}

	private UserConfigure userChooseOne() throws InterruptedException {
		ConfigureChooser chooser = new ConfigureChooser(configureList);
		while (! chooser.isDone()) {
			Thread.sleep(500);
		}

		return chooser.getSelectedItem().getConfigure();
	}
}
