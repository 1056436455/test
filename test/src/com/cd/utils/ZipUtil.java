package com.cd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	/** 
	  * ZIPѹ���ļ����У� 
	  * @param sourcePath Ҫѹ�����ļ����У�·�� 
	  * @param zipName ���ɵ�ZIP�ļ��� 
	  * @throws FileNotFoundException �ļ������� 
	  * @throws Exception �ļ������쳣 
	  */  
	 public static void zip(String sourcePath,String zipName) throws FileNotFoundException, Exception{  
	     ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipName));  
	     File inputFile = new File(sourcePath);  
	     zip(out,inputFile,"");  
	     out.close();  
	 }  
	  
	 /** 
	  * ZIPѹ���ļ����У� 
	  * @param out ZIP�ļ���ʽ����� 
	  * @param file Ҫѹ�����ļ� 
	  * @param basePath ����·�� 
	  * @throws Exception �ļ������쳣 
	  */  
	 private static void zip(ZipOutputStream out, File file, String basePath) throws Exception {  
	     if (file.isDirectory()) {  
	         File[] fileList = file.listFiles();  
	         out.putNextEntry(new ZipEntry(basePath + "/"));  
	         basePath = basePath.length() == 0 ? "" : basePath + "/";  
	         for (int i = 0; i < fileList.length; i++) {  
	             zip(out, fileList[i], basePath + fileList[i].getName());  
	         }  
	     } else {  
	         out.putNextEntry(new ZipEntry(basePath));  
	         FileInputStream in = new FileInputStream(file);  
	         byte[] bs = new byte[10240];  
	         int b;  
	         while ((b = in.read(bs)) != -1) {  
	             out.write(bs, 0, b);  
	         }  
	         in.close();  
	     }  
	 }  
	  
	 /** 
	  * ��ѹZIP�ļ� 
	  * @param zipFilePath ZIP�ļ�·�� 
	  * @param dest ��ѹ���ļ����·�� 
	  * @throws FileNotFoundException ZIP�ļ������� 
	  * @throws IOException �ļ������쳣 
	  */  
	 public static void unzip(String zipFilePath,String dest) throws FileNotFoundException, IOException{  
	     ZipInputStream input=new ZipInputStream(new FileInputStream(zipFilePath));  
	     File destFile=new File(dest);  
	     if(destFile==null || !destFile.exists()){  
	         destFile.mkdirs();  
	     }  
	     for (ZipEntry e; (e = input.getNextEntry()) != null; input.closeEntry()) {  
	         File file = new File(dest, e.getName());  
	         if (e.isDirectory()) {  
	             file.mkdirs();  
	         } else {  
	             FileOutputStream output = new FileOutputStream(file);  
	             byte[] bs = new byte[10240];  
	             int len=0;  
	             while ((len=input.read(bs)) > 0) {  
	                 output.write(bs, 0, len);  
	             }  
	             output.close();  
	         }  
	     }  
	     input.closeEntry();  
	     input.close();  
	 }  
}
