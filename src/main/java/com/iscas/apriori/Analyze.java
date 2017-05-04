package com.iscas.apriori;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Summer on 2017/1/13.
 * 分析Apriori算法挖掘出的频繁项集
 * 给出部署策略
 */
@RestController
@RequestMapping("/analyze")
@EnableAutoConfiguration
public class Analyze {

    @RequestMapping(value = "/getPlan",method = RequestMethod.POST)
    public String getPlan(@RequestParam(value = "rulesPath") String rulesPath){

        StringBuilder res = new StringBuilder();
        Set<String> allServices = new HashSet<String>();
        Map<String,Integer> support = new HashMap<>();
        Map<String,Double> confidence = new HashMap<>();
        try {
            File file = new File(rulesPath);
            if(file.isFile()&&file.exists()){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTXT = null;
                while((lineTXT=bufferedReader.readLine())!=null){
                    String [] strs = lineTXT.split("、");
                    support.put(strs[0],Integer.parseInt(strs[1]));
                    if(strs.length>2){
                        confidence.put(strs[0],Double.parseDouble(strs[2]));
                    }else{
                        //support.put(strs[0],Integer.parseInt(strs[1]));
                        confidence.put(strs[0],0.0);
                    }
                   // trans.add(new Apriori.ItemSet(strs));
                }
            }else{
                System.out.println("找不到指定文件！");
            }
        }catch (Exception e){
            System.out.println("读取文件出错！");
            e.printStackTrace();
        }
        //把所有服务存入allServices
        for(String key:support.keySet()){
            key = key.substring(1,key.length()-1);
            String[] strs = key.split(",");
            for(int i=0;i<strs.length;i++){
                if(i==0){
                    allServices.add(strs[i]);
                }else{
                    allServices.add(strs[i].substring(1));
                }
            }
        }
        //从两个map中给出部署策略
//        int nodeIndex = 1;
//        Map<String,String> map = getNodeAddress();
        while((!support.isEmpty())){
            //找出支持度计数最大的服务集合
            Map<String,Integer> maxSupport = new HashMap<>();
            int count = 0,max = -1;
            for(String key:support.keySet()){
                int i = support.get(key);
                if(i>max){
                    max = i;
                    count=1;
                    maxSupport.clear();
                    maxSupport.put(key,max);
                }else if(i==max){
                    count++;
                    maxSupport.put(key,max);
                }
            }
            String services = "";
            if(maxSupport.size()==1){
                for(String key:maxSupport.keySet()){
                    services = key;
                }
//                services = maxSupport.keySet().toString();
            }else{
                double maxConfidence = 0.0;
                for(String key:maxSupport.keySet()){
                    double c = confidence.get(key);
                    if(c>=maxConfidence){
                        maxConfidence = c;
                        services = key;
                    }
                }
            }
            res.append(services + "\n");
//            map.remove(nodeIndex);
//            nodeIndex++;
            //剪枝
            services = services.substring(1,services.length()-1);
            String[] strs = services.split(",");
            Set<String> set = new HashSet<String>();
            for(int i=0;i<strs.length;i++){
                if(i==0){
                    set.add(strs[i]);
                    allServices.remove(strs[i]);
                }else {
                    set.add(strs[i].substring(1));
                    allServices.remove(strs[i].substring(1));
                }
            }

            Map<String,Integer> copySupport = new HashMap<>();
            copySupport.putAll(support);
            for(String key:copySupport.keySet()){
                String s = key.substring(1,key.length()-1);
                String[] ss = s.split(",");
                for(int i=0;i<ss.length;i++){
                    if(i==0){
                        if(set.contains(ss[i])){
                            support.remove(key);
                            confidence.remove(key);
                            break;
                        }
                    }else{
                        if(set.contains(ss[i].substring(1))){
                            support.remove(key);
                            confidence.remove(key);
                            break;
                        }
                    }
                }
            }
//            if(map.size()<=1)
//                break;
        }
        //System.out.print(res);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        //System.out.print("[");
        for(String s:allServices){
            //System.out.print(s+", ");
            sb.append(s+", ");
        }
        String sss = sb.substring(0,sb.length()-2);
        //System.out.print(sss+"]");
        return res.toString()+sss+"]";
    }
}
