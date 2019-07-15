package com.cj.sbasic.util.reptileUtil;
import java.io.*;
/**
 * @author： 刘磊
 * @Description: txt文件合并
 * @date： 2019/7/11 11:45
 **/

public class FileCombine {
    public static  void  main(String[] args) throws IOException {
        //定义输出目录
        String FileOut="F:\\1.txt";
        BufferedWriter bw=new BufferedWriter(new FileWriter(FileOut));

        //读取目录下的每个文件或者文件夹，并读取文件的内容写到目标文字中去
        File[] list = new File("F:\\测试").listFiles();
        int fileCount = 0;
        int folderConut= 0;
        for(File file : list)
        {
            if(file.isFile())
            {
                fileCount++;
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while((line=br.readLine())!=null) {
                    bw.write(line);
                    bw.newLine();
                }
                br.close();
            }else {
                folderConut++;
            }
        }
        bw.close();
        System.out.println("输入目录下文件个数为"+fileCount);
        System.out.println("输入目录下文件夹个数为"+folderConut);

    }

}
