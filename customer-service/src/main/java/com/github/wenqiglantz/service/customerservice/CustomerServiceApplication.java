package com.github.wenqiglantz.service.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerServiceApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication app = new SpringApplication(CustomerServiceApplication.class);
    app.run();
  }
}
