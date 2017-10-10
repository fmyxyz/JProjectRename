package fmy.leaves;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���������
 * 
 * -path ��Ŀ��Ŀ¼
 * 
 * -old ԭ��Ŀ��
 * 
 * -new ����Ŀ��
 * 
 * ��Java��Ŀ������
 * 
 * ���磺
 * 
 * -path=D:\Workspaces\aaa\ -old=aaa -new=bbb
 * 
 * @author fmy-leaves
 *
 */
public class ProjectReame {

	public static void main(String[] args) {
		String msg = "���������\n" + "\t-path\t��Ŀ��Ŀ¼\n" + "\t-old\tԭ��Ŀ��\n" + "\t-new\t����Ŀ��\n" + "��Java��Ŀ������\n"
				+ "���磺\n\t -path=D:\\Workspaces\\aaa\\ -old=aaa -new=bbb";
		String path = null;
		String old = null;
		String newf = null;
		if (args.length >= 3) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].contains("path")) {
					path = args[i].split("=")[1];
				}
				if (args[i].contains("old")) {
					old = args[i].split("=")[1];
				}
				if (args[i].contains("new")) {
					newf = args[i].split("=")[1];
				}
			}
			if (path == null || old == null || newf == null) {
				System.out.println(msg);
				System.exit(0);
			}
			folderRename(path, old, newf);
		} else {
			System.out.println(msg);
			System.exit(0);
		}
		System.exit(0);
	}

	private static void folderRename(String path, String old, String newf) {
		File root = new File(path);
		System.out.println("������Ŀ¼��" + root.getAbsolutePath());
		folderRename(root, old, newf);
	}

	private static void folderRename(File root, String old, String newf) {
		if (root.isDirectory()) {
			File[] cfs = root.listFiles();
			for (int i = 0; i < cfs.length; i++) {
				folderRename(cfs[i], old, newf);
			}
			if (root.getName().contains(old)) {
				System.out.println("����ǰ��" + root.getAbsolutePath());
				String deststr = root.getAbsolutePath();
				deststr = deststr.substring(0, deststr.lastIndexOf(root.getName()));
				deststr += root.getName().replace(old, newf);
				root.renameTo(new File(deststr));
				System.out.println("������" + deststr);
			}
		} else {
			fileContentChange(root, old, newf);
		}
	}

	private static void fileContentChange(File root, String old, String newf) {
		String name = root.getName();
		Pattern pattern = Pattern.compile(".*((\\.properties)|(\\.xml)|(\\.java))$");
		Matcher m = pattern.matcher(name);
		if (m.matches()) {
			System.out.println("����µ��ļ���" + root.getAbsolutePath());
			RandomAccessFile fwr = null;
			try {
				fwr = new RandomAccessFile(root, "rw");
				String rl = fwr.readLine();
				long ponit = fwr.getFilePointer();
				while (rl != null) {
					if (rl.contains(old)) {
						fwr.seek(ponit);
						String nrl = rl.replace(old, newf);
						fwr.writeBytes(nrl);
						fwr.writeBytes("                                                             ");
					}
					rl = fwr.readLine();
					ponit = fwr.getFilePointer();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fwr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
