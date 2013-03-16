package jp.gr.java_conf.t_era.dbconnect.model;

import java.io.File;
import java.util.List;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.database.DataBaseInfo;
import jp.gr.java_conf.t_era.dbconnect.model.database.QueryExecuter;

/**
 * DBConnect�̃��W���[���p�[�g�́AUI(View)�p�[�g�ɑ΂��Č��J����C���^�[�t�F�[�X�ł��B
 * 
 * @author y-terada
 *
 */
public interface MainModule {
	/**
	 * �e�[�u���ꗗ����Ԃ��܂��B
	 * @return �e�[�u���ꗗ���
	 * @throws ActionInterruptException
	 */
	QueryResultView getTableInfo() throws ActionInterruptException;
	/**
	 * �w�肳�ꂽ�e�[�u���̃J�����ꗗ����Ԃ��܂��B
	 * @param tableName �e�[�u����
	 * @return �J�����ꗗ���
	 * @throws ActionInterruptException
	 */
	QueryResultView getColumnInfo(String tableName) throws ActionInterruptException;
	/**
	 * �f�[�^�x�[�X�ւ̐ڑ������X�V���܂��B
	 * @param newUrl �ڑ�URL
	 * @param newUserName �ڑ����[�U��
	 * @param newPassword �ڑ��p�X���[�h
	 * @param autoCommit �����R�~�b�g�ݒ�
	 * @param readOnly �Ǎ��ݐ�p�ݒ�
	 */
	void setDataBaseInfo(String newUrl, String newUserName, String newPassword, boolean autoCommit, boolean readOnly);
	/**
	 * DB�ڑ����X�V���܂��B<br>
	 * �L����DB�ڑ����Ȃ��ꍇ�A�����I�ɃR�l�N�V�����̎擾���s���܂��B
	 * �L����DB�ڑ�������ꍇ�̓���ɂ��ẮA�e�����ɂ��܂��B
	 * @throws ActionInterruptException
	 */
	void refleshConnection() throws ActionInterruptException;
	/**
	 * �w�肳�ꂽSQL�̒u�����p�����[�^�̐���Ԃ��܂��B
	 * @param sql SQL
	 * @return �u�����p�����[�^�̐�
	 * @throws ActionInterruptException
	 */
	int getParameterCount(String sql) throws ActionInterruptException;
	/**
	 * SQL���s���W���[����Ԃ��܂��B
	 * @param parameters �u�����p�����[�^<br>�u�������Ȃ��ꍇ�Anull����List���w�肵�܂��B<br>
	 * �v�f�̐��́A{@link #getParameterCount(String)}�ƈ�v����K�v������܂��B
	 * @return SQL���s���W���[��
	 */
	QueryExecuter getQueryExecter(List<String> parameters);
	/**
	 * ���̃��W���[�����ێ�����X�g���[��/�R�l�N�V��������Close���܂��B<br>
	 * ���̃��\�b�h�́A���̃��\�b�h���Ă΂ꂽ��ɃI�u�W�F�N�g���K�x�[�W�R���N�g���ꂽ�ꍇ�A
	 * �S�Ẵ��\�[�X�ɂ��ă��[�N���N���Ȃ����Ƃ�ۏႵ�܂��B
	 */
	void close();
	/**
	 * DB�̏���Ԃ��܂��B
	 * @return DB�̏��
	 */
	DataBaseInfo getDataBaseInfo();
	/**
	 * �ڑ��p�̏���Ԃ��܂��B
	 * @return �ڑ��p�̏��
	 */
	ConnectionInfo getConnectionInfo();
	/**
	 * DB���R�~�b�g���܂��B
	 * @throws ActionInterruptException
	 */
	void commit() throws ActionInterruptException;
	/**
	 * DB�����[���o�b�N���܂��B
	 * @throws ActionInterruptException
	 */
	void rollback() throws ActionInterruptException;
	/**
	 * �R�l�N�V�����̍Ď擾���Ď�����I�u�U�[�o��ǉ����܂��B
	 * @param newObserver �R�l�N�V�����̍Ď擾�ɑ΂���I�u�U�[�o
	 */
	void addConnectionObserver(ConnectionObserver newObserver);
	/**
	 * SQL�𕪂��ēǂݏo��{@link SQLReader}�C���X�^���X��Ԃ��܂��B
	 * @param inputFile �ǂݏo���Ώۂ̃t�@�C��
	 * @return SQL�ǂݏo���I�u�W�F�N�g
	 * @throws ActionInterruptException
	 */
	SQLReader getSeparateSQLReader(File inputFile) throws ActionInterruptException;
	/**
	 * �t�@�C������A��SQL�Ƃ��ēǂݏo��{@link SQLReader}�C���X�^���X��Ԃ��܂��B
	 * @param inputFile �ǂݏo���Ώۂ̃t�@�C��
	 * @return SQL�ǂݏo���I�u�W�F�N�g
	 * @throws ActionInterruptException
	 */
	SQLReader getSingleSQLReader(File inputFile) throws ActionInterruptException;
	/**
	 * �t�@�C����SQL�������o��{@link SQLWriter}�C���X�^���X��Ԃ��܂��B
	 * @param outputFile ���o���Ώۂ̃t�@�C��
	 * @return SQL���o���I�u�W�F�N�g
	 * @throws ActionInterruptException
	 */
	SQLWriter getSingleFileSQLWriter(File outputFile) throws ActionInterruptException;
	/**
	 * �w�肳�ꂽ���e�ŁA�L���Ȑڑ����擾�ł��邩�𔻒肵�܂��B
	 * @param driverName JDBC�h���C�o�̃N���X��
	 * @param url DB�̐ڑ�URL
	 * @param userName DB�̐ڑ����[�U��
	 * @param password DB�̐ڑ��p�X���[�h
	 * @param connectionCheckSql �ڑ����m�F���邽�߂Ɏ��s����ȒP��SQL
	 * @return �L���Ȑڑ����擾�ł�����̏ꍇ�Atrue
	 */
	boolean checkConnectInfo(String driverName, String url, String userName, String password, String connectionCheckSql);
	/**
	 * �w�肳�ꂽ�t�@�C���Ƀf�[�^�������o�����߂́A���o�����W���[���̃C���X�^���X�𐶐����A�Ԃ��܂��B
	 * @param targetFile ���o���Ώۂ̃t�@�C��
	 * @param takeRecursiveBackup true:���o���Ώۂ̃t�@�C�����A���ɑ��݂���t�@�C���̏ꍇ�o�b�N�A�b�v�����܂��B
	 * �@�o�b�N�A�b�v�t�@�C���́A"[���̖��O]+_bk"�ƂȂ�܂��B
	 * �@�o�b�N�A�b�v�̎擾�����ɂ��Ă��A�����o���t�@�C���������̏ꍇ�A�ċA�I�Ƀo�b�N�A�b�v���s���܂��B
	 * @return ���o�����W���[���̃C���X�^���X
	 */
	DataTableWriter getCsvWriter(File targetFile, boolean takeRecursiveBackup);

	Configure getConfigure();
}
