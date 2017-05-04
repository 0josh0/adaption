package com.iscas.apriori;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Summer on 2017/1/5.
 */
public class WriteData {
    public void write(String filePath,Map<Integer, Set<Apriori.ItemSet>> rst,Double MCONF,String rulesPath){
        //输出频繁项集
        File out = new File(filePath);
        StringBuilder sb = new StringBuilder();

        //分析数据
        File out2 = new File(rulesPath);
        StringBuilder sb2 = new StringBuilder();
        Map<Integer,String> map = new HashMap<>();

        sb.append("Frequent Item Sets:\n");
        for (Map.Entry<Integer, Set<Apriori.ItemSet>> entry : rst.entrySet()) {
            Integer itemSetSize = entry.getKey();
            sb.append("Frequent "+itemSetSize+" Item Sets:\n");
            if(itemSetSize==1){
                for (Apriori.ItemSet set : entry.getValue()) {
                    sb.append(set+","+set.frequence+"\n");
                }
            }else {
                for (Apriori.ItemSet set : entry.getValue()) {
                    sb.append(set + "," + set.frequence + "\n");

                    int hashcode = getHashCode(set);
                    map.put(hashcode,set+"、"+set.frequence);
                   // System.out.println("频繁项集："+set+"   "+"hashcode："+hashcode);
                }
            }
        }
        //输出关联规则
        Map<Apriori.ItemSet, Apriori.ItemSet> directMap = new HashMap<Apriori.ItemSet, Apriori.ItemSet>();
        for (Map.Entry<Integer, Set<Apriori.ItemSet>> entry : rst.entrySet()) {
            for (Apriori.ItemSet set : entry.getValue())
                directMap.put(set, set);
        }
        sb.append("Association Rules:\n");
        for (Map.Entry<Integer, Set<Apriori.ItemSet>> entry : rst.entrySet()) {
            for (Apriori.ItemSet set : entry.getValue()) {
                double cnt1 = directMap.get(set).frequence;
                List<Apriori.ItemSet> subSets = set.listNotEmptySubItemSets();
                for (Apriori.ItemSet subSet : subSets) {
                    int cnt2 = directMap.get(subSet).frequence;
                    double conf = cnt1 / cnt2;
                    if (cnt1 / cnt2 >= MCONF) {
                        Apriori.ItemSet remainSet = new Apriori.ItemSet();
                        remainSet.addAll(set);
                        remainSet.removeAll(subSet);
                        //System.out.printf("%s => %s, %.2f\n", subSet, remainSet, conf);
                        sb.append(subSet+", "+remainSet+", "+conf+"\n");

                        int hashcode = getHashCode(subSet)+getHashCode(remainSet);
                        //String
                        if(map.containsKey(hashcode)){
                            String value = map.get(hashcode);
                            String[] strs = value.split("、");
                            if(strs.length<3){
                                map.put(hashcode,value+"、"+conf);
                            }else{
                                double confidence = Double.parseDouble(strs[2]);
                                if(conf>confidence) confidence = conf;
                                map.put(hashcode,strs[0]+"、"+strs[1]+"、"+confidence);
                            }

                        }else{
                            System.out.println("No Exist Key ERROR!");
                        }
                       // System.out.println("关联规则："+subSet+", "+remainSet+"   "+"hashcode："+hashcode);
                    }
                }
            }
        }

        for(String value:map.values()){
            sb2.append(value+"\n");
        }
        System.out.println(sb2.toString());

        String content = sb.toString();
        try (FileOutputStream fop = new FileOutputStream(out)) {
            if(!out.exists()){
                out.createNewFile();
            }
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            System.out.println("FrequentSet Done！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content2 = sb2.toString();
        try (FileOutputStream fop2 = new FileOutputStream(out2)) {
            if(!out2.exists()){
                out2.createNewFile();
            }
            byte[] contentInBytes = content2.getBytes();
            fop2.write(contentInBytes);
            fop2.flush();
            System.out.print("Rules Done！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //计算set的hash值
    public  static int getHashCode(Apriori.ItemSet set){
        String frequentSet = set.toString().substring(1,set.toString().length()-1);
        String[] strs = frequentSet.split(",");
        int key = 0;
        for(int i=0;i<strs.length;i++){
            if(i==0){
                key = key+strs[i].hashCode();
            }else{
                key = key+strs[i].substring(1).hashCode();
            }
        }
        return key;
    }
}
