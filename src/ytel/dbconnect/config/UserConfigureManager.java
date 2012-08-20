package ytel.dbconnect.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ���[�U�ݒ���Ǘ����܂��B
 * <p>
 * ���̃N���X�̎����ł́A���[�U�ݒ�͎w��t�H���_�̓���̃T�t�B�b�N�X�̃t�@�C���S�Ă��ǂݏo���ΏۂɂȂ�܂��B<br>
 * ���[�U�ݒ�̃t�@�C�����P�̏ꍇ�ɂ́A���̐ݒ�t�@�C�����ݒ�Ƃ��Ĕ��f���܂��B
 * ���[�U�ݒ�̃t�@�C������������ꍇ�ɂ́A���̂����̈��I�����āA�A�v���P�[�V�����ɔ��f���܂��B<br>
 * �I���̕��@�́A{@link ConfigureChooser}�̎����ɂ��A�_�C�A���O�\���ł��B
 * @author y-terada
 *
 */
class UserConfigureManager {
	private List<NamedConfigure> configureList;

	/**
	 * �f�B���N�g���ƃt�@�C�����T�t�B�b�N�X���w�肵�āA���[�U�ݒ�t�@�C���̓ǂݏo�����s���܂��B
	 * <p>�w��̏����ɍ��v����t�@�C���ɁA�t�H�[�}�b�g���قȂ���̂��܂܂�Ă����ꍇ�A�܂��͏����ɍ��v����t�H���_������
	 * ����ꍇ�̓���͕ۏႵ�܂���B
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
	 * ���[�U�ݒ�t�@�C����I�����܂��B<br>
	 * �I��������̏ꍇ�̓��X�|���X�͏u�ԂɋN����܂��B�����̑I����������ꍇ�ɂ́A���̃��\�b�h�̊������鎞�Ԃ�
	 * �s��ł��B
	 * @return ���[�U�ݒ�t�@�C��
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
