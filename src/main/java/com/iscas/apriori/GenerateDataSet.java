package com.iscas.apriori;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Summer on 2017/1/4.
 * 产生频繁通信数据集，理论上来说该数据集是从日志文件中清洗得来的
 */
@RestController
@RequestMapping("/apriori")
@EnableAutoConfiguration
public class GenerateDataSet {

    @RequestMapping(value = "/generateDataSet",method = RequestMethod.GET)
    public void generate(){
        File dataset = new File("E:/dataset.txt");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<100;i++){
            sb.append("uA uT uH"+"\r\n");
        }
        for(int i=0;i<100;i++){
            sb.append("uT uA uH uQ"+"\r\n");
        }
        for(int i=0;i<200;i++){
            sb.append("uA uO"+"\r\n");
        }
        for(int i=0;i<300;i++){
            sb.append("uH uQ"+"\r\n");
        }
        for(int i=0;i<400;i++){
            sb.append("uQ"+"\r\n");
        }
        for(int i=0;i<100;i++){
            sb.append("uA"+"\r\n");
        }
        for(int i=0;i<100;i++){
            sb.append("uT"+"\r\n");
        }
        String content = sb.toString();
        try (FileOutputStream fop = new FileOutputStream(dataset)) {
            if(!dataset.exists()){
                dataset.createNewFile();
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
    }
}
