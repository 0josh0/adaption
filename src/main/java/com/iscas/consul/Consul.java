package com.iscas.consul;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iscas.docker.Docker;
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
 * Created by Summer on 2017/1/12.
 */
@RestController
@RequestMapping("/consul")
@EnableAutoConfiguration
public class Consul {
//    @Value("${application.consul.address}")
//    String consulAddress;
//    public Consul(String consulAddress){
//        this.consulAddress = consulAddress;
//    }
    /**
     * 根据微服务名字获取服务地址
     */
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public String query(@RequestParam(value = "serviceName") String serviceName,@RequestParam(value = "consulAddress") String consulAddress) throws IOException {
        // 读url
        ClassLoader cl = Docker.class.getClassLoader();
        String api = "/v1/catalog/service/" + serviceName;
        URL url = new URL(consulAddress + api);
        // 打开restful链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 提交模式
        conn.setRequestMethod("GET");// POST GET PUT DELETE
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
        if(sb==null||sb.equals("")){
            return "no such service!";
        }else {
            JSONArray jsonArray = JSONArray.parseArray(sb.toString());
//        int size = jsonArray.size();
//        System.out.println("Size: " + size);
//        for(int  i = 0; i < size; i++){
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            System.out.println("[" + i + "]Node=" + jsonObject.get("Node"));
//            System.out.println("[" + i + "]Address=" + jsonObject.get("Address"));
//            System.out.println("[" + i + "]ServiceID=" + jsonObject.get("ServiceID"));
//            System.out.println("[" + i + "]ServiceName=" + jsonObject.get("ServiceName"));
//            System.out.println("[" + i + "]ServiceTags=" + jsonObject.get("ServiceTags"));
//            System.out.println("[" + i + "]ServiceAddress=" + jsonObject.get("ServiceAddress"));
//            System.out.println("[" + i + "]ServicePort=" + jsonObject.get("ServicePort"));
//        }
            //System.out.println(sb);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String serviceAddress = jsonObject.get("ServiceAddress").toString();
            reader.close();
            conn.disconnect();
            return serviceAddress;
        }
    }
}
