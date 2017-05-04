package com.iscas.kong;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

///import org.apache.commons.httpclient.methods.*

/**
 * Created by Summer on 2017/1/12.
 */
@RestController
@RequestMapping("/kong")
@EnableAutoConfiguration
public class Kong {
    @Value("${application.kong.address}")
    String kongAddress;

    /**
     * 修改在Kong中某个服务的所有注册API
     */
    @RequestMapping(value = "/modifyAPIs/{serviceName}",method = RequestMethod.GET)
    @ResponseBody
    public String modifyAPIs(@PathVariable String serviceName,@RequestParam(value = "upstream_url") String upstream_url,@RequestParam(value = "kongAddress") String kongAddress) throws Exception {

        List<String> apiNames = getAPIName(serviceName,kongAddress);
        for(int i=0;i<apiNames.size();i++){
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

            URI url = new URI("http://133.133.10.28:8001/apis/"+apiNames.get(i));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("upstream_url",upstream_url);

            HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(jsonObject);

            //HttpEntity<String> requestEntity = new HttpEntity<String>(upstream_url);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
            String body = responseEntity.getBody();
        }

        return "success";
    }
    /**
     * 修改在Kong中注册的API
     */
    @RequestMapping(value = "/modifyAPI/{api_name}",method = RequestMethod.GET)
    @ResponseBody
    public String modifyAPI(@PathVariable String api_name,@RequestParam(value = "upstream_url") String upstream_url) throws Exception {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        URI url = new URI("http://133.133.10.28:8001/apis/"+api_name);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("upstream_url",upstream_url);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(jsonObject);

        //HttpEntity<String> requestEntity = new HttpEntity<String>(upstream_url);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
        String body = responseEntity.getBody();
        return body;
    }
    /**
     * 向Kong中注册API
     */
    public String addAPI(){
        return "";
    }
    /**
     * 从Kong中删除API
     */
    public String deleteAPI(){

        return "";
    }
    /**
     * 根据部分API名查询要要修改的API
     * e.g. 根据account找到account service暴露的所有API：account-login-service……
     */
    public List<String> getAPIName(String serviceName,String kongAddress){
        List<String> apiNames = new ArrayList<String>();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<String, Object>();
        String apiObjectsResponse = restTemplate.getForObject(kongAddress,String.class, uriVariables);

        JSONObject jsonobj=JSONObject.parseObject(apiObjectsResponse);//将字符串转化成json对象
        String total=jsonobj.getString("total");//获取字符串。
        JSONArray array=jsonobj.getJSONArray("data");//获取数组
        //System.out.println(total);
        ObjectMapper apiObjectMapper = new ObjectMapper();
        try {
            for(int i=0;i<array.size();i++){
                APIObject apiObject = apiObjectMapper.readValue(array.getString(i),APIObject.class);
                //System.out.println(apiObject.getName());
                String apiName = apiObject.getName();
                String apiStrs[] = apiName.split("-");
                if(apiStrs[0].equals(serviceName)){
                    apiNames.add(apiName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for(int i=0;i<apiNames.size();i++){
//            System.out.println(apiNames.get(i));
//        }
        return apiNames;
    }
}
