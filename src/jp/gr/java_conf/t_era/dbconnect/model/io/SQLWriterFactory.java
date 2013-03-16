package jp.gr.java_conf.t_era.dbconnect.model.io;

import java.io.File;
import java.io.IOException;

import jp.gr.java_conf.t_era.dbconnect.config.Configure;
import jp.gr.java_conf.t_era.dbconnect.model.MessageObserver;
import jp.gr.java_conf.t_era.dbconnect.model.SQLWriter;

/**
 * File���o���̃I�u�W�F�N�g���Ǘ����܂��B
 * �v���ɉ����āA�C���X�^���X�𐶐����܂��B
 * @author y-terada
 *
 */
public class SQLWriterFactory {
	/**
	 * �P��t�@�C���ɏ��o�����s��{@link SQLWriter}�C���X�^���X��Ԃ��܂��B
	 * @param outputFile ���o���Ώۂ̃t�@�C��
	 * @param messageObserver ���o���������̃��b�Z�[�W���Ď�����I�u�U�[�o
	 * @return ���o���p�I�u�W�F�N�g
	 * @throws IOException
	 */
	public static SQLWriter getSingleFileSQLWriter(File outputFile, MessageObserver messageObserver, Configure config) throws IOException{
		return new SQLSingleFileWriter(outputFile, messageObserver, config);
	}
}
