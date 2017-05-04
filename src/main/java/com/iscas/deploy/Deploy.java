package com.iscas.deploy;

import com.iscas.apriori.Analyze;
import com.iscas.consul.Consul;
import com.iscas.docker.Docker;
import com.iscas.kong.Kong;
import com.iscas.utils.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Summer on 2017/1/19.
 * 根据给出的部署策略进行优化部署
 */
@RestController
@RequestMapping("/deploy")
@EnableAutoConfiguration
public class Deploy {
//    @Value("${application.consul.address}")
//    String consulAddress;
//    @Value("${application.kong.address}")
//    String kongAddress;

    @Autowired
    private Config config;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void test(){
        //Config config = new Config();
        System.out.println(config.getConsulAddress());
        System.out.println(config.getKongAddress());
        System.out.println(config.getCleanLogPath());
        System.out.println(config.getFrequentSetPath());
        System.out.println(config.getMCONF());
        System.out.println(config.getMSF());
        System.out.println(config.getRulesPath());

        //System.out.print(consulAddress);
    }

    @RequestMapping(value = "/doDeploy",method = RequestMethod.POST)
    public void doDeploy(@RequestParam(value = "rulesPath") String rulesPath)throws IOException {
        Analyze analyze = new Analyze();
        Docker docker = new Docker();
        Consul consul = new Consul();
        Kong kong = new Kong();

        String consulAddress = config.getConsulAddress();
        String kongAddress = config.getKongAddress();

        String deployPlan = analyze.getPlan(rulesPath);
        //解析[H, Q] [A, O] [T]
        String[] strings = deployPlan.split("\n");
        Map<String,String> map = getNodeAddress();
        for(int i=0;i<strings.length;i++){
            int nodeIndex = i+1;
            String nodeServices = strings[i].substring(1,strings[i].length()-1);
            String[] services = nodeServices.split(", ");
            //String ip = consul.query(services[0]+"-tomcat9",consulAddress);
            String ip = map.get(nodeIndex+"");
            //for(int j=1;j<services.length;j++){
            for(int j=0;j<services.length;j++){
                String serviceName = services[j];
                String serviceAddress = consul.query(serviceName+"-tomcat9",consulAddress);
                if(!serviceAddress.equals(ip)){
                    docker.stopContainer(serviceAddress,serviceName);//关闭其他节点的服务
                    docker.startContainer(ip,serviceName);//重启服务，再部署
                    try {
                        String upstream_url = getUpstreamUrl(serviceName,ip);
                        kong.modifyAPIs(serviceName,upstream_url,kongAddress);//修改注册在API网关kong上的URL
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //System.out.println(strings.length);
        System.out.println(deployPlan);
    }
    /**
     * 获取修改后的服务地址：upstream_url
     */
    public String getUpstreamUrl(String serviceName,String serviceAddress) {
        Map<String,String> map = new HashMap<String,String>();
        //for(int i=0;i<5)
        map.put("account","8085");
        map.put("holding","8084");
        map.put("order","8083");
        map.put("quote","8082");
        map.put("trade","8081");

        String servicePort=map.get(serviceName);
        String serviceProject = serviceName+"-1.0";
        return "http://"+serviceAddress+":"+servicePort+"/"+serviceProject;
    }

    /**
     * 保存所有可部署服务的节点
     */
    public Map<String,String> getNodeAddress(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("1","133.133.10.28");
        map.put("2","133.133.10.30");
        map.put("3","133.133.10.31");
        return map;
    }
}
