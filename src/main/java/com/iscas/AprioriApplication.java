package com.iscas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableConfigurationProperties(Config.class)
public class AprioriApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AprioriApplication.class, args);

//		Apriori apriori = new Apriori();
//		apriori.doApriori();
//		Kong kong = new Kong();
//		kong.getAPIName("account","http://133.133.10.28:8001/apis");
	}
}
