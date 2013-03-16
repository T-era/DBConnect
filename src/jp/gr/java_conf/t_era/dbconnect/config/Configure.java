package jp.gr.java_conf.t_era.dbconnect.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * アプリケーションでの設定項目を管理します。
 * @author y-terada
 *
 */
public class Configure {
	/**
	 * ユーザ指定の設定項目
	 */
	public final UserConfigure user;
	/**
	 * システム設定
	 */
	public final SystemConfigure system;

	public Configure(String confFileDirectory, String confFileSufix) throws FileNotFoundException, IOException, InterruptedException {
		UserConfigureManager manager = new UserConfigureManager(confFileDirectory, confFileSufix);
		this.user = manager.chooseOne();
		this.user.getDataBaseName();
		this.system = new SimpleSystemConfigure();
	}

	private static class SimpleSystemConfigure implements SystemConfigure{
		private final String alertMessage;
		private final String appTitle;
		private final boolean defaultOverWriteSqlFile;
		private final String errorLabel;
		private final String labelNameTemplate;
		private final String nullLabel;
		private final String resultLabel;
		private final String versionInfoFileName;

		private SimpleSystemConfigure() {
			ResourceBundle bundle = ResourceBundle.getBundle("ytel.dbconnect.config.system");
			this.alertMessage = bundle.getString("ALERT_MESSAGE");
			this.appTitle = bundle.getString("APP_TITLE");
			this.defaultOverWriteSqlFile = Boolean.parseBoolean(bundle.getString("DEFAULT_OVER_WRITE_SQL_FILE"));
			this.errorLabel= bundle.getString("ERROR_LABEL");
			this.labelNameTemplate = bundle.getString("LABEL_NAME_TEMPLATE");
			this.nullLabel = bundle.getString("NULL_RESULT_LABEL");
			this.resultLabel = bundle.getString("RESULT_LABEL");
			this.versionInfoFileName = bundle.getString("VERSION_INFO");
		}

		@Override
		public String getAlertMessage() {
			return alertMessage;
		}

		@Override
		public String getAppTitle() {
			return appTitle;
		}

		@Override
		public boolean getDefaultOverWriteSqlFile() {
			return defaultOverWriteSqlFile;
		}

		@Override
		public String getErrorLabel() {
			return errorLabel;
		}

		@Override
		public String getLabelNameTemplate() {
			return labelNameTemplate;
		}

		@Override
		public String getNullLabel() {
			return nullLabel;
		}

		@Override
		public String getResultLabel() {
			return resultLabel;
		}

		@Override
		public String getVersionInfoFileName() {
			return versionInfoFileName;
		}
	}
}
