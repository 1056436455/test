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
	  * ZIP压缩文件（夹） 
	  * @param sourcePath 要压缩的文件（夹）路径 
	  * @param zipName 生成的ZIP文件名 
	  * @throws FileNotFoundException 文件不存在 
	  * @throws Exception 文件操作异常 
	  */  
	 public static void zip(String sourcePath,String zipName) throws FileNotFoundException, Exception{  
	     ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipName));  
	     File inputFile = new File(sourcePath);  
	     zip(out,inputFile,"");  
	     out.close();  
	 }  
	  
	 /** 
	  * ZIP压缩文件（夹） 
	  * @param out ZIP文件格式输出流 
	  * @param file 要压缩的文件 
	  * @param basePath 基础路径 
	  * @throws Exception 文件操作异常 
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
	  * 解压ZIP文件 
	  * @param zipFilePath ZIP文件路径 
	  * @param dest 解压后文件存放路径 
	  * @throws FileNotFoundException ZIP文件不存在 
	  * @throws IOException 文件操作异常 
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
