package com.gyymz.audio.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class FileUtils {

	public static boolean write2File(byte[] source, File aimPath) {
	
		try {
			if(source==null)return false;
			OutputStream out = new FileOutputStream(aimPath);
			out.write(source);
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
return false;
	}

	public static void delFile(File dir) {
		File[] files = dir.listFiles();
		if (files != null)
			for (File file : files) {
				file.delete();
			}
	}

	public static void delFile(File dir, FilenameFilter filter) {
		File[] files = dir.listFiles(filter);
		if (files != null)
			for (File file : files) {
				boolean isDelete = file.delete();
			}
	}

	/**
	 * 复制文件
	 * 
	 * @param source
	 * @param aim
	 */
	public static void copyFile(File source, File aim) {
		try {
			byte[] buffer = new byte[512];
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(aim);
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 文件复制
	 * 
	 * @param source
	 * @param aim
	 */
	public static void copyFile(InputStream source, File aim) {
		try {
			byte[] buffer = new byte[512];
			InputStream in = source;
			OutputStream out = new FileOutputStream(aim);
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void combineFile(File dir, String outPath, int offect) {
		combineFile(dir.listFiles(), outPath, offect);
	}

	public static void combineFile(File[] files, String outPath, int offect) {
		if (new File(outPath).getParentFile().mkdirs())
			;
		FileOutputStream out = null;
		FileInputStream in = null;
		try {
			byte buff[] = new byte[512];
			out = new FileOutputStream(outPath);
			System.out.println(Arrays.toString(files));
			boolean isFirst = true;
			if (files != null)
				for (File string : files) {
					in = new FileInputStream(string);
					if (!isFirst) {
						in.skip(offect);// 跳过偏移量
					} else {
						isFirst = false;
					}
					int len = 0;
					while ((len = in.read(buff)) != -1) {
						out.write(buff, 0, len);
					}
					in.close();
				}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
