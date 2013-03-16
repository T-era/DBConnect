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
 * SQL���w�肳�ꂽ�t�@�C���ɏ������݂܂��B
 * @author y-terada
 *
 */
final class SQLSingleFileWriter implements SQLWriter{
	private final BufferedWriter bw;
	private final MessageObserver messageObserver;
	private final Configure config;
	/**
	 * �t�@�C�����w�肷��R���X�g���N�^
	 * <p/>���̎����ł́A�R���X�g���N�g�̃^�C�~���O�Ŏw�肳�ꂽ�t�@�C���ɑ΂���
	 * �X�g���[�����J���܂��B
	 * @param outputFile �����ݑΏۃt�@�C��
	 * @throws IOMessagingException
	 */
	SQLSingleFileWriter(File outputFile, MessageObserver messageObserver, Configure config) throws IOException{
		if (! outputFile.exists()){
			boolean resCreate = outputFile.createNewFile();
			if (!resCreate)
				throw new IOException("�t�@�C�����쐬�ł��܂���ł����B");
		}
		this.messageObserver = messageObserver;
		this.config = config;
		bw = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(outputFile)) );
	}

	/**
	 * �t�@�C���Ɏw�肳�ꂽ���e���������݂܂��B
	 */
	@Override
	public void write(LabeledSql sql) throws ActionInterruptException{
		write(sql.getLabel(), sql.getSql());
	}

	/**
	 * �t�@�C���Ɏw�肳�ꂽ���e���������݂܂��B
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
			messageObserver.setFatalMessage("�t�@�C�����o�����ɃG���[���������܂����B", e);
			throw new ActionInterruptException("�t�@�C�����o�����ɃG���[���������܂����B", e);
		}
	}

	/**
	 * �X�g���[������܂��B
	 * <p/>���̎����ł́A�X�g���[�����J���̂̓R���X�g���N�^�Ȃ̂ŁA
	 * ���̃N���X�̂��ׂẴI�u�W�F�N�g�͂��̃��\�b�h�ŃX�g���[����
	 * �������s���K�v������܂��B
	 */
	@Override
	public void close() throws ActionInterruptException{
		try{
			bw.close();
		}catch(IOException e){
			messageObserver.setFatalMessage("���o���X�g���[���̌㏈���Ɏ��s���܂����B", e);
			throw new ActionInterruptException("���o���X�g���[���̌㏈���Ɏ��s���܂����B", e);
		}
	}
}
