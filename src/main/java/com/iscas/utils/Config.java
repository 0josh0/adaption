package com.iscas.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Summer on 2017/4/11.
 * 读取所有配置文件
 */
//@RestController
//@RequestMapping("/deploy")
//@EnableAutoConfiguration
//@Configuration
//@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = true)
//@ConfigurationProperties(locations = "classpath:my-config.properties")
@Component
public class Config {

    @Value("${key}")
    private String key;

    @Value("${consulAddress}")
    private String consulAddress;
    @Value("${kongAddress}")
    private String kongAddress;
    @Value("${mergeLogPath}")
    private String mergeLogPath;
    @Value("${cleanLogPath}")
    private String cleanLogPath;
    @Value("${frequentSetPath}")
    private String frequentSetPath;
    @Value("${rulesPath}")
    private String rulesPath;
    @Value("${MSF}")
    private String MSF;
    @Value("${MCONF}")
    private String MCONF;

    public String getConsulAddress() {
        return consulAddress;
    }

    public void setConsulAddress(String consulAddress) {
        this.consulAddress = consulAddress;
    }

    public String getKongAddress() {
        return kongAddress;
    }

    public void setKongAddress(String kongAddress) {
        this.kongAddress = kongAddress;
    }

    public String getMergeLogPath() {
        return mergeLogPath;
    }

    public void setMergeLogPath(String mergeLogPath) {
        this.mergeLogPath = mergeLogPath;
    }

    public String getCleanLogPath() {
        return cleanLogPath;
    }

    public void setCleanLogPath(String cleanLogPath) {
        this.cleanLogPath = cleanLogPath;
    }

    public String getFrequentSetPath() {
        return frequentSetPath;
    }

    public void setFrequentSetPath(String frequentSetPath) {
        this.frequentSetPath = frequentSetPath;
    }

    public String getRulesPath() {
        return rulesPath;
    }

    public void setRulesPath(String rulesPath) {
        this.rulesPath = rulesPath;
    }

    public String getMSF() {
        return MSF;
    }

    public void setMSF(String MSF) {
        this.MSF = MSF;
    }

    public String getMCONF() {
        return MCONF;
    }

    public void setMCONF(String MCONF) {
        this.MCONF = MCONF;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

//    public void test(){
//        System.out.println(environment.getProperty("key"));
//    }
}
