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
 * �t�@�C���̓��e����؂炸�ɁA���String�Ƃ��ēǂݍ���Reader
 * @author y-terada
 *
 */
final class SQLSingleReader implements SQLReader{
	/**
	 * �ǂݍ��ݑΏۂ̃t�@�C��
	 */
	private final File file;
	private final MessageObserver messageObserver;
	private final Configure config;

	SQLSingleReader(File inputFile, MessageObserver messageObserver, Configure config) throws IOException{
		if (! inputFile.exists()){
			throw new IOException("�ǂݍ��ݑΏۂ̃t�@�C�������݂��܂���");
		}
		this.file = inputFile;
		this.messageObserver = messageObserver;
		this.config = config;
	}

	/**
	 * ���̎����ł́A�����s���܂���B�Q������܂���B
	 */
	@Override
	public void close() throws ActionInterruptException {
	}

	private boolean theFirst = true;
	/**
	 * �ŏ��̈��̂�true��Ԃ��܂��B
	 * <p/>���̃N���X�̎����̓t�@�C���̓��e��1���R�[�h�Ƃ��ĕԂ����߁A
	 * �ŏ��̈��ȊO�͏��false�ł��B
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
	 * �t�@�C���̓��e��ǂݍ��݂܂��B
	 * {@link #hasMore()}�̌��ʂɊւ�炸�A�t�@�C���̓��e�S�Ă�Ԃ��܂��B
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
			messageObserver.setErrorMessage("�t�@�C���Ǎ��ݒ��ɃG���[���������܂����B", e);
			throw new ActionInterruptException("�t�@�C���Ǎ��ݒ��ɃG���[���������܂����B", e);
		}
		return new LabeledSqlImpl(label, new String(sql));
	}
}
