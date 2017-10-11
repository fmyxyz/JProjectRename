package fmy.leaves;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 输入参数：
 * 
 * -path 项目根目录
 * 
 * -old 原项目名
 * 
 * -new 现项目名
 * 
 * 对Java项目更名。
 * 
 * 例如：
 * 
 * * -path=D:\Workspaces\aaa\ -old=aaa -new=bbb * * @author fmy-leaves *
 */
public class ProjectReame {
	public static void main(String[] args) {
		String msg = "输入参数：\n" + "\t-path\t项目根目录\n" + "\t-old\t原项目名\n" + "\t-new\t现项目名\n" + "对Java项目更名。\n"
				+ "例如：\n\t -path=D:\\Workspaces\\aaa\\ -old=aaa -new=bbb";
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
		System.out.println("更名根目录：" + root.getAbsolutePath());
		folderRename(root, old, newf);
	}

	/**
	 * 递归文件目录更名
	 * 
	 * @param root
	 * @param old
	 * @param newf
	 */
	private static void folderRename(File root, String old, String newf) {
		if (root.isDirectory()) {
			File[] cfs = root.listFiles();
			for (int i = 0; i < cfs.length; i++) {
				folderRename(cfs[i], old, newf);
			}
			if (root.getName().contains(old)) {
				System.out.println("更名前：" + root.getAbsolutePath());
				String deststr = root.getAbsolutePath();
				deststr = deststr.substring(0, deststr.lastIndexOf(root.getName()));
				deststr += root.getName().replace(old, newf);
				root.renameTo(new File(deststr));
				System.out.println("更名后：" + deststr);
			}
		} else {
			fileContentChange(root, old, newf);
		}
	}

	/**
	 * java xml 内容更改内容
	 * 
	 * @param root
	 * @param old
	 * @param newf
	 */
	private static void fileContentChange(File root, String old, String newf) {
		String name = root.getName();
		Pattern pattern = Pattern.compile(".*((\\.properties)|(\\.xml)|(\\.java))$");
		Matcher m = pattern.matcher(name);
		if (m.matches()) {
			System.out.println("需更新的文件：" + root.getAbsolutePath());
			FileWriter fw = null;
			BufferedReader br = null;
			BufferedReader sbr = null;
			try {
				// 取文件中的文本数据
				StringBuffer sb = new StringBuffer();
				br = new BufferedReader(new FileReader(root));
				String brl = br.readLine();
				while (brl != null) {
					sb.append(brl + "\n");
					brl = br.readLine();
				}
				// 替换后写回源文件
				fw = new FileWriter(root);
				sbr = new BufferedReader(new StringReader(sb.toString()));
				String sbrl = sbr.readLine();
				while (sbrl != null) {
					if (sbrl.contains(old)) {
						String nrl = sbrl.replace(old, newf);
						fw.write(nrl + "\n");
					} else {
						fw.write(sbrl + "\n");
					}
					sbrl = sbr.readLine();
				}
				fw.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					sbr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
