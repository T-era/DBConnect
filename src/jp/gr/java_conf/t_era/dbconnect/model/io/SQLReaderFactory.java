package jp.gr.java_conf.t_era.dbconnect.model.io;

import java.io.File;
import java.io.IOException;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.SQLReader;

/**
 * File�ǎ��̃I�u�W�F�N�g���Ǘ����܂��B
 * �v���ɉ����āA�C���X�^���X�𐶐����܂��B
 * @author y-terada
 *
 */
public class SQLReaderFactory {
	/**
	 * �P��SQL���t�@�C������ǂݏo�����߂�{@link SQLReader}�C���X�^���X��Ԃ��܂��B
	 * @param inputFile �ǂݏo���t�@�C��
	 * @param messageObserver �t�@�C�����쒆�̃��b�Z�[�W���Ď�����I�u�U�[�o
	 * @return �ǂݏo���p�I�u�W�F�N�g
	 * @throws IOException
	 */
	public static SQLReader getSingleSQLReader(File inputFile, MessageObserver messageObserver, Configure config) throws IOException{
		return new SQLSingleReader(inputFile, messageObserver, config);
	}

	/**
	 * ����SQL���t�@�C������ǂݏo�����߂�{@link SQLReader}�C���X�^���X��Ԃ��܂��B
	 * @param inputFile �ǂݏo���t�@�C��
	 * @param messageObserver �t�@�C�����쒆�̃��b�Z�[�W���Ď�����I�u�U�[�o
	 * @return �ǂݏo���p�I�u�W�F�N�g
	 * @throws IOException
	 */
	public static SQLReader getSeparateSQLReader(File inputFile, MessageObserver messageObserver, Configure config) throws IOException{
		return new SQLSeparateReader(inputFile, config);
	}
}
