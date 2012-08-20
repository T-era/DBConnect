package ytel.dbconnect.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * ユーザ設定項目をプロパティから取得するクラス。
 * @author y-terada
 *
 */
class PropertyReader implements UserConfigure{
	private final Properties prop;

	private static MessageFormat TEMPLATE_FILE_NAME = new MessageFormat(".\\{0}_template");
	private static MessageFormat LOADING_LOG = new MessageFormat("Loading... {0} => {1}");
	/**
	 * 指定されたファイルの内容でプロパティを初期化します。
	 * @param configFile ユーザ設定ファイル
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public PropertyReader(File configFile) throws FileNotFoundException, IOException {
		prop = new Properties();
		prop.load(new FileInputStream(configFile));
		if (prop.containsKey("DATABASE")) {
			String template = prop.getProperty("DATABASE");
			if (template != null) {
				String templateFileName = TEMPLATE_FILE_NAME.format(new Object[] { template });
				System.out.println(LOADING_LOG.format(new Object[] {configFile.getName(), templateFileName}));
				prop.load(new FileInputStream(templateFileName));
			}
		}
	}

	@Override
	public String getColumnInfoQuery() {
		return prop.getProperty("COLUMN_INFO_QUERY");
	}
	@Override
	public String getConnectionCheckSql() {
		return prop.getProperty("CONNECTION_CHECK_SQL");
	}
	@Override
	public boolean getDefaultAutoCommit() {
		return Boolean.parseBoolean(prop.getProperty("DEFAULT_AUTO_COMMIT"));
	}
	@Override
	public String getDefaultPassword() {
		return prop.getProperty("DEFAULT_PASSWORD");
	}
	@Override
	public boolean getDefaultReadOnly() {
		return Boolean.parseBoolean(prop.getProperty("DEFAULT_READ_ONLY"));
	}
	@Override
	public String getDefaultResultCsvFileName() {
		return prop.getProperty("DEFAULT_RESULT_CSV_FILE_NAME");
	}
	@Override
	public String getDefaultSql() {
		return prop.getProperty("DEFAULT_SQL");
	}
	@Override
	public String getDefaultSqlFileName() {
		return prop.getProperty("DEFAULT_SQL_FILE_NAME");
	}
	@Override
	public String getDefaultUrl() {
		return prop.getProperty("DEFAULT_URL");
	}
	@Override
	public String getDefaultUserName() {
		return prop.getProperty("DEFAULT_USER_NAME");
	}
	@Override
	public String getDriverName() {
		return prop.getProperty("DRIVER_NAME");
	}
	@Override
	public String getSqlCommentOut() {
		return prop.getProperty("SQL_COMMENT_OUT");
	}
	@Override
	public String getTableInfoQuery() {
		return prop.getProperty("TABLE_INFO_QUERY");
	}
	@Override
	public String getColumnInfoQuery_TableNameReplacer() {
		return prop.getProperty("COLUMN_INFO_QUERY.TABLE_NAME_REPLACER");
	}
	@Override
	public String getDataBaseName() {
		return prop.getProperty("DATABASE");
	}
}
