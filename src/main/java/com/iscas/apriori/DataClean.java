package com.iscas.apriori;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashSet;

/**
 * Created by Summer on 2017/1/9.
 * 对记录服务通信情况的日志进行数据清洗
 * 获取Apriori算法输出规范的数据集
 */
@RestController
@RequestMapping("/apriori")
@EnableAutoConfiguration
public class DataClean {
    @RequestMapping(value = "/dataClean",method = RequestMethod.POST)
    public void doClean(String filePath, String dataSetPath){
        try {
            File file = new File(filePath);//读取日志文件
            File out = new File(dataSetPath);//输出清洗后的日志文件
            StringBuilder sb = new StringBuilder();
            if(file.isFile()&&file.exists()){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTXT = null;
                while((lineTXT=bufferedReader.readLine())!=null){
                    String [] strs = lineTXT.split("  ");
                    //把所有service放入set中，去重，能放入的为一个服务链，不能放入的为一个服务链
                    HashSet<String> set = new HashSet<String>();
                    //String str = null;
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    for(int i=1;i< strs.length;i++){
//                        StringBuilder sb1 = new StringBuilder();
//                        StringBuilder sb2 = new StringBuilder();
                        if(set.add(strs[i])){
                            sb1.append(strs[i]+"  ");
                        }else{
                            sb2.append(strs[i]+"  ");
                        }

                    }
                    if(sb2.length()!=0)
                        sb1.append("\n"+sb2);
                   // str = sb1.toString();
                    sb.append(sb1+"\n");
                }
                String content = sb.toString();
                try (FileOutputStream fop = new FileOutputStream(out)) {
                    if(!out.exists()){
                        out.createNewFile();
                    }
                    byte[] contentInBytes = content.getBytes();
                    fop.write(contentInBytes);
                    fop.flush();
                    System.out.print("Done");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("找不到指定文件！");
            }
        }catch (Exception e){
            System.out.println("读取文件出错！");
            e.printStackTrace();
        }
    }
//    public void doClean(String filePath, String dataSetPath){
//        try {
//            File file = new File(filePath);//读取日志文件
//            File out = new File(dataSetPath);//输出清洗后的日志文件
//            StringBuilder sb = new StringBuilder();
//            if(file.isFile()&&file.exists()){
//                InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(reader);
//                String lineTXT = null;
//                while((lineTXT=bufferedReader.readLine())!=null){
//                    String [] strs = lineTXT.split(":");
//                    String str = strs[strs.length-1];
//                    sb.append(str+"\n");
//                }
//                String content = sb.toString();
//                try (FileOutputStream fop = new FileOutputStream(out)) {
//                    if(!out.exists()){
//                        out.createNewFile();
//                    }
//                    byte[] contentInBytes = content.getBytes();
//                    fop.write(contentInBytes);
//                    fop.flush();
//                    System.out.print("Done");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                System.out.println("找不到指定文件！");
//            }
//        }catch (Exception e){
//            System.out.println("读取文件出错！");
//            e.printStackTrace();
//        }
//    }
}
