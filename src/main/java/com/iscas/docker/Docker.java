package com.iscas.docker;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Summer on 2017/1/11.
 */
@RestController
@RequestMapping("/docker")
@EnableAutoConfiguration
public class Docker {

    //@Value("${application.service.address}")
    //String serviceAddress;
    /**
     * 启动容器
     */
    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public String startContainer(@RequestParam(value = "serviceAddress") String serviceAddress,@RequestParam(value = "containerIDorName") String containerIDorName) throws IOException {
        // 读url
        ClassLoader cl = Docker.class.getClassLoader();
        String api = ":2375/containers/" + containerIDorName + "/start";
        URL url = new URL("http://"+serviceAddress + api);
        // 打开restful链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 提交模式
        conn.setRequestMethod("POST");// POST GET PUT DELETE
        // 设置访问提交模式，表单提交
        conn.setRequestProperty("Content-Type", "application/form-data");
        conn.setConnectTimeout(10000);// 连接超时 单位毫秒
        conn.setReadTimeout(2000);// 读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        String s = "";
        byte[] body = s.getBytes();
        conn.getOutputStream().write(body);
        // 读取请求返回值
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        System.out.println(sb);
        reader.close();
        conn.disconnect();
        return "success";
    }

    /**
     * 关闭容器
     */
    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public String stopContainer(@RequestParam(value = "serviceAddress") String serviceAddress,@RequestParam(value = "containerIDorName") String containerIDorName) throws IOException {
        // 读url
        ClassLoader cl = Docker.class.getClassLoader();
        String api = ":2375/containers/" + containerIDorName + "/stop";
        URL url = new URL("http://"+serviceAddress + api);
        // 打开restful链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 提交模式
        conn.setRequestMethod("POST");// POST GET PUT DELETE
        // 设置访问提交模式，表单提交
        conn.setRequestProperty("Content-Type", "application/form-data");
        conn.setConnectTimeout(10000);// 连接超时 单位毫秒
        conn.setReadTimeout(2000);// 读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        String s = "";
        byte[] body = s.getBytes();
        conn.getOutputStream().write(body);
        // 读取请求返回值
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        System.out.println(sb);
        reader.close();
        conn.disconnect();
        return "success";
    }
}
