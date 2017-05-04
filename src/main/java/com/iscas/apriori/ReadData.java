package com.iscas.apriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Summer on 2017/1/4.
 * 从频繁通信数据集中读取数据，将序列以List的形式存储起来，方便Apriori算法处理
 */
public class ReadData {

    public List<Set<String>> read(String filePath){

        // 初始化事务集
        List<Set<String>> trans = new LinkedList<Set<String>>();
        try {
            File file = new File(filePath);
            if(file.isFile()&&file.exists()){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTXT = null;
                while((lineTXT=bufferedReader.readLine())!=null){
                   // String [] strs = lineTXT.split("->");
                    String [] strs = lineTXT.split("  ");
                    trans.add(new Apriori.ItemSet(strs));
                }
            }else{
                System.out.println("找不到指定文件！");
            }
        }catch (Exception e){
            System.out.println("读取文件出错！");
            e.printStackTrace();
        }
        return trans;
    }
}
