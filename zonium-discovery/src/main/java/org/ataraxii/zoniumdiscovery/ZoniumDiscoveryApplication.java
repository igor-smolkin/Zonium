package org.ataraxii.zoniumdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ZoniumDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZoniumDiscoveryApplication.class, args);
    }

}
