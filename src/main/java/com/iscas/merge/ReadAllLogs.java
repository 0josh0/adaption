package com.iscas.merge;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2017/4/24.
 */
@RestController
@RequestMapping("/mergelogs")
@EnableAutoConfiguration
public class ReadAllLogs {
    //读取一个文件夹下所有文件及子文件夹下的所有文件
    @RequestMapping(value = "",method = RequestMethod.POST)
    public void ReadAll(@RequestParam(value = "foldPath") String foldPath, @RequestParam(value = "mergeLog") String mergeLog) {
        //System.out.println("Read All logs……");
        File f = null;
        f = new File(foldPath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        List<File> list = new ArrayList<File>();
        for (File file : files) {
            if(file.isDirectory()) {
                //如何当前路劲是文件夹，则循环读取这个文件夹下的所有文件
                ReadAll(file.getAbsolutePath(),mergeLog);
            } else {
                list.add(file);
            }
        }
        for(File file : files) {
            MergeLog.merge(file.getAbsolutePath(),mergeLog);
            System.out.println(file.getAbsolutePath());
        }
    }

    //读取一个文件夹下的所有文件夹和文件
    public void ReadFile(String filePath) {
        File f = null;
        f = new File(filePath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        List<File> list = new ArrayList<File>();
        for (File file : files) {
            list.add(file);
        }
        for(File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
