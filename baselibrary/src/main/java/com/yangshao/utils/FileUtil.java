/*
 * 
 * Copyright (c) 2015, alipay.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yangshao.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
  * @author: gyymz1993
  * 创建时间：2017/3/27 20:19
  * @version
  *
 **/
public class FileUtil {

	/**
	 * 
	 * copy file
	 * 
	 * @param src
	 *            source file
	 * @param dest
	 *            target file
	 * @throws IOException
	 */
	public static void copyFile(File src, File dest) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			if (!dest.exists()) {
				dest.createNewFile();
			}
			inChannel = new FileInputStream(src).getChannel();
			outChannel = new FileOutputStream(dest).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	/**
	 * delete file
	 * 
	 * @param file
	 *            file
	 * @return true if delete success
	 */
	public static boolean deleteFile(File file) {
		if (!file.exists()) {
			return true;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteFile(f);
			}
		}
		return file.delete();
	}

	/**
	 * 得到一个文件输出路径
	 * */
	public  static File getImageOutputFile(){
		File imageFileDir = new File(Environment.getExternalStorageDirectory()
			+ "/Android/data/"
			+ "/Files");
		if(!imageFileDir.exists()){
			if(!imageFileDir.mkdirs()){
				return null;
			}
		}
		String currentTime=new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
		String mImageName="MI_"+ currentTime +".jpg";
		return new File(imageFileDir.getPath() + File.separator + mImageName);
	}

	public  static File getTempFile(){
		String iTempFileNameString = "temp";
		File dir = Environment.getExternalStorageDirectory();
		File tempFile = null;
		try {
			tempFile = File.createTempFile(iTempFileNameString, ".jpg", dir);
			if(!tempFile.exists()){
				if(!tempFile.mkdirs()){
					return null;
				}
			}
			return tempFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempFile;
	}

}
