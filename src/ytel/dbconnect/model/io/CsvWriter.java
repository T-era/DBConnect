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
 * �\�`���̃f�[�^��CSV�ɏo�͂��邽�߂́A�t�@�C�������݃��W���[��
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
	 * �w�肳�ꂽ�t�@�C�����o�b�N�A�b�v���܂��B<p/>
	 * �o�b�N�A�b�v�Ƃ́A���t�@�C���Ɠ����f�B���N�g���ɁA"_bk"�Ƃ����ڔ�������ă��l�[�����邱�Ƃł��B
	 * �o�b�N�A�b�v��̃t�@�C����(���t�@�C����+"_bk")�����ɑ��݂���ꍇ�A�ċA�I�Ƀo�b�N�A�b�v���J��Ԃ��܂��B
	 * @param file �o�b�N�A�b�v�Ώۂ̃t�@�C��
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
		// ���̓`�F�b�N
		for (int i = 0; i < values.length; i ++) {
			if (titles.length != values[i].length) {
				throw new ActionInterruptException(MessageFormat.format("�^�C�g���̐��ƁA���v���Ȃ��񂪂���(Line:{0})", new Object[]{i}), null);
			}
		}

		try {
			writeIo(titles, values);
			messageObserver.setMessage("���o������");
		} catch (IOException e) {
			messageObserver.setFatalMessage("CSV�t�@�C���̏����o���Ɏ��s���܂����B", e);
			throw new ActionInterruptException("CSV�t�@�C���̏����o���Ɏ��s���܂����B", e);
		}
	}

	/**
	 * �t�@�C���ɁA�ꗗ�\�̓��e��CSV�`���ŏo�͂��܂��B
	 * @param titles �ꗗ�\�̃^�C�g��
	 * @param values �ꗗ�\�̓��e
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
