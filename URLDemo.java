package com.cj.sbasic.util.reptileUtil;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author： 刘磊
 * @Description: 爬虫
 * @date： 2019/7/10 10:31
 **/
public class URLDemo {


//    @Autowired
//    private static CsresMapper csresMapper;
    //提取的数据存放到该目录下
    private static String savepath="F:/测试/";
    //等待爬取的url
    private static List<String> allwaiturl=new ArrayList<>();
    //爬取过的url
    private static Set<String> alloverurl=new HashSet<>();
    //记录所有url的深度进行爬取判断
    private static Map<String,Integer> allurldepth=new HashMap<>();
    //爬取得深度
    private static int maxdepth=2;
    //生命对象，帮助进行线程的等待操作
    private static Object obj=new Object();
    //记录总线程数5条
    private static int MAX_THREAD=5;
    //记录空闲的线程数
    private static int count=0;
    //记录网站数
    private static int num=0;

    public static void main(String args[]){
        //确定爬取的网页地址，此处为当当网首页上的图书分类进去的网页
        //网址为        http://book.dangdang.com/
        //String strurl="http://search.dangdang.com/?key=%BB%FA%D0%B5%B1%ED&act=input";
        //String strurl="http://www.csres.com/sort/chsort.jsp";
        //String strurl = "http://www.csres.com/sort/chsortdetail/P.html";
        //String strurl = "http://www.csres.com/sort/Chtype/P97_20.html";
//        String strurl = "http://www.csres.com/sort/chsortdetail/Q.html";
        String strurl = "http://www.csres.com/sort/Chtype/Q73_1.html";
        //设置ip
        System.getProperties().setProperty("http.proxyHost", "218.108.175.15");
        System.getProperties().setProperty("http.proxyPort", "80");
        //workurl(strurl,1);
        addurl(strurl,0);
//        addurl(strurl2,0);
        for(int i=0;i<MAX_THREAD;i++){
            new URLDemo().new MyThread().start();
        }
    }
    /**
     * 网页数据爬取
     * @param strurl
     * @param depth
     */
    public static void workurl(String strurl,int depth){
        //判断当前url是否爬取过
        if(!(alloverurl.contains(strurl)||depth>maxdepth)){
            //检测线程是否执行
            System.out.println("当前执行："+Thread.currentThread().getName()+" 爬取线程处理爬取："+strurl+"深度："+depth);
            //建立url爬取核心对象
            try {
                URL url=new URL(strurl);
                //通过url建立与网页的连接
                URLConnection conn=url.openConnection();
                //通过链接取得网页返回的数据
                InputStream is=conn.getInputStream();
                //提取text类型的数据
                if(conn.getContentType().startsWith("text")){

                }
                System.out.println("---->"+conn.getContentEncoding());
                //一般按行读取网页数据，并进行内容分析
                //因此用BufferedReader和InputStreamReader把字节流转化为字符流的缓冲流
                //进行转换时，需要处理编码格式问题
                BufferedReader br=new BufferedReader(new InputStreamReader(is,"GB2312"));

                //按行读取并打印
                String line=null;
                //正则表达式的匹配规则提取该网页的链接
                Pattern pu=Pattern.compile("<a href=('|\")/sort/Chtype/[A-Z]{1,2}[0-9]{2}_[0-9]{1,2}.html('|\") class=((lan)|(\"sh14lian\"))>",Pattern.CASE_INSENSITIVE);
                //匹配数据
                Pattern ps1=Pattern.compile("<td.*><font color=\".*\">");
                Pattern ps2=Pattern.compile("<td.*><font color=\".*\">.*");
                //建立一个输出流，用于保存文件,文件名为执行时间，以防重复
                PrintWriter pw = null;
                if(strurl.indexOf("Chtype")!=-1){
                    //时间戳System.currentTimeMillis()
                    pw = new PrintWriter(new File(savepath+strurl.substring(strurl.indexOf("Chtype/")+7,strurl.indexOf(".html"))+".txt"));
                }
                boolean bt=false;
                boolean bn=false;
                int i = 0;
                while((line=br.readLine())!=null){
//                    System.out.println(line);
                    //编写正则，匹配超链接地址
                    // 输出需要数据
                    if(strurl.indexOf("Chtype")!=-1) {
                        if(bt){
                            pw.print(line+"', '");
                            bt=false;
                        }
                        if(bn){
                            if(line.indexOf("<")!=-1){
                                line = line.substring(0, line.indexOf("<"));
                                pw.print(line+"', '");
                                bn = false;
                            }else{
                                pw.print(line);
                            }
                        }
//                        pw.println(line);
                        //ms1.find() 匹配正则表达式 有返回true
                        //ms2.group() 获取匹配上的字段
                        Matcher ms1 = ps1.matcher(line);
                        if (ms1.find()) {
                            Matcher ms2 = ps2.matcher(line);
                            if (ms2.find()) {
                                String s = ms2.group();
                                s = s.substring(s.indexOf("0\">")+3);
                                if(s.indexOf("<")!=-1){
                                    s = s.substring(0, s.indexOf("<"));
                                }else{
                                    if(i==1){
                                        bn = true;
                                    }
                                }
                                if(i==0){
                                    pw.print("INSERT INTO " +
                                            "csres " +
                                            "(csres_num," +
                                            "csres_name," +
                                            "csres_department," +
                                            "csres_date," +
                                            "csres_state)" +
                                            "VALUES('"+s+"', '");
                                }else if(i==1){
                                    if(bn){
                                        pw.print(s);
                                    }else{
                                        pw.print(s+"', '");
                                    }
                                }else if(i==4){
                                    pw.println(s+"');");
                                }else if(i==2){
                                    bt=true;
                                }else {
                                    pw.print(s+"', '");
                                }
                                i++;
                            }
                        }
                        if(i==5){
                            i=0;
                        }

                    }


                    Matcher m=pu.matcher(line);

                    while(m.find()){
                        String href=m.group();
//                        System.out.println("---->"+href);
                        //找到超链接地址并截取字符串
                        //有无引号
                        if(href.indexOf("href=")!=-1){
                            href=href.substring(href.indexOf("href="));
                        }else{
                            href=href.substring(href.indexOf("HREF="));
                        }
                        if(href.charAt(5)=='\"'||href.charAt(5)=='\''){
                            href=href.substring(6);
                        }else{
                            href=href.substring(5);
                        }
                        //截取到引号或者空格或者到">"结束
                        try{
                            if(href.indexOf("\"")==-1){
                                href=href.substring(0,href.indexOf("\'"));
                            }else{
                                href=href.substring(0,href.indexOf("\""));
                            }
                        }catch(Exception e){
                            try{
                                href=href.substring(0,href.indexOf(" "));
                            }catch(Exception e1){
                                href=href.substring(0,href.indexOf(">"));
                            }
                        }
                        href = "http://www.csres.com"+href;
                        if(href.startsWith("http:")||href.startsWith("https:")){
                    /*
                    //输出该网页存在的链接
                    //System.out.println(href);
                    //将url地址放到队列中
                    allwaiturl.add(href);
                    allurldepth.put(href,depth+1);
                    */
//                            System.out.println(href);
                            //调用addurl方法
                            addurl(href,depth);
                        }

                    }

                }
                pw.close();
                br.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
            //将当前url归列到alloverurl中
            alloverurl.add(strurl);
            System.out.println(strurl+"网页爬取完成，已爬取数量："+alloverurl.size()+"，剩余爬取数量："+allwaiturl.size()+"数据页："+num);
        }
        /*
        //用递归的方法继续爬取其他链接
        String nexturl=allwaiturl.get(0);
        allwaiturl.remove(0);
        workurl(nexturl,allurldepth.get(nexturl));
        */
        if(allwaiturl.size()>0){
            synchronized(obj){
                obj.notify();
            }
        }else{
            System.out.println("爬取结束......."+num);
        }

    }
    /**
     * 将获取的url放入等待队列中，同时判断是否已经放过
     * @param href
     * @param depth
     */
    public static synchronized void addurl(String href,int depth){
        //将url放到队列中
        allwaiturl.add(href);
        //判断url是否放过
        if(!allurldepth.containsKey(href)){
            allurldepth.put(href, depth+1);
            if(href.indexOf("Chtype")!=-1){
                num++;
            }
        }
    }
    /**
     * 移除爬取完成的url，获取下一个未爬取得url
     * @return
     */
    public static synchronized String geturl(){
        String nexturl=allwaiturl.get(0);
        allwaiturl.remove(0);
        return nexturl;
    }
    /**
     * 线程分配任务
     */
    public class MyThread extends Thread{

        @Override
        public void run(){
            //设定一个死循环，让线程一直存在
            while(true){
                //判断是否新链接，有则获取
                if(allwaiturl.size()>0){
                    //获取url进行处理
                    String url=geturl();
                    //调用workurl方法爬取
                    workurl(url,allurldepth.get(url));
                }else{
                    System.out.println("当前线程准备就绪，等待连接爬取："+this.getName());
                    count++;
                    //建立一个对象，让线程进入等待状态，即wait（）
                    synchronized(obj){
                        try{
                            obj.wait();
                        }catch(Exception e){

                        }
                    }
                    count--;
                }
            }
        }

    }
}