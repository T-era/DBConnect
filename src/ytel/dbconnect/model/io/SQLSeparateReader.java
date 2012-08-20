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
 * ������SQL���A�t�@�C�������؂��ēǂݍ���Reader�B
 * <p/>���̃N���X�̎����ł́A�ǂݍ��ݏ����͑S�ăR���X�g���N�^�ōs���܂��B<br/>
 * {@link #SQLSeparateReader(File, Configure)}�R���X�g���N�^�Ńt�@�C��������e���擾���Ă���A{@link #hasMore()}��
 * {@link #read()}���\�b�h�œ��e���擾���܂��B<br/>
 * {@link #close()}���\�b�h�͉����s���܂���B(�X�g���[���̔j���́A�ǂݍ��ݒ���ɃR���X�g���N�^���Ŋ������܂��B)
 * <p/>SQL�̋�؂蕶���ƁA�R�����g�A�E�g�̋L�����A�v���P�[�V����({@link Configure})�̐ݒ肩��擾���܂��B
 *
 * @author y-terada
 *
 */
final class SQLSeparateReader implements SQLReader{
	/**
	 * �ǂݍ��݌��ʂ�SQL
	 */
	private final Iterator<LabeledSql> labeles;
	private final Configure config;

	/**
	 * �w�肵���t�@�C������ASQL�����擾���܂��B<p/>
	 * ���̃N���X�̎����ł́A���̃R���X�g���N�^�����ŁA�t�@�C���̒��g���p�[�X����
	 * {@link #labeles}�ɕێ����܂��B
	 * @param file �ǂݍ��ݑΏۂ̃t�@�C��
	 * @throws IOMessagingException
	 */
	SQLSeparateReader(File file, Configure config) throws IOException{
		this.config = config;
		if (! file.exists()){
			throw new IOException("�ǂݍ��ݑΏۂ̃t�@�C�������݂��܂���");
		}

		this.labeles = getIterator(file);
	}

	/**
	 * �t�@�C����Ǎ��݁A���̓��e��Iterator�Ƃ��ĕԂ��܂��B
	 * @param file �ǂݍ��ݑΏۂ̃t�@�C��
	 * @return SQL�̈ꗗ
	 * @throws IOMessagingException
	 */
	private Iterator<LabeledSql> getIterator(final File file) throws IOException{
		// �ǂݍ��݌��ʂ�ێ����邽�߂�List
		List<LabeledSql> list = new ArrayList<LabeledSql>();
		StringBuilder sql = new StringBuilder();
		String label = "TEMP";

		BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(file)) );
		try{
			String aLine = null;

			int counter = 0;

			// SQL���̍ŏ��̍s�̏ꍇ�̂�True(SQL�̕����L����true�ɂ���)
			boolean newLine = true;
			while( ( aLine = br.readLine() ) !=null ){
				if (newLine){
					// �ŏ��̍s���R�����g�Ȃ�A���̍s�����x���ɂ���
					if (aLine.startsWith(config.user.getSqlCommentOut())){
						label = aLine.replaceFirst(config.user.getSqlCommentOut(), "");
						continue;
					}else{
						label = file.getName() + counter++;
					}
				}

				if (aLine.endsWith(";")) {
					// �s����";"�̏ꍇ�́A�s�ŋ�؂��ĕ�Pane��SQL��\������B

					// �s����";"�͎�菜��(JDBC�o�R�ł�SQL���s���ɃG���[�ɂȂ邽��)
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
				// �Ō�̍s��;���Ȃ��ꍇ
				list.add(new LabeledSqlImpl(label, new String(sql)));
			}
		}finally{
			br.close();
		}
		return list.iterator();
	}

	/**
	 * ���̃N���X�̎����ł́AIterator�ɂ܂��v�f�����邩�ǂ�����Ԃ��܂��B
	 */
	@Override
	public boolean hasMore() {
		return this.labeles.hasNext();
	}

	/**
	 * ���̃N���X�̎����ł́AIterator�̎��̗v�f��Ԃ��܂��B
	 */
	@Override
	public LabeledSql read() throws ActionInterruptException{
		return labeles.next();
	}

	/**
	 * ���̃N���X�̎����ł́A�������܂���B�Q������܂���B
	 */
	@Override
	public void close() throws ActionInterruptException{
	}
}
