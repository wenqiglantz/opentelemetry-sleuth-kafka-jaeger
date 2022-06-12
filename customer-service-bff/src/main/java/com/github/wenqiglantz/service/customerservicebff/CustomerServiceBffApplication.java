package com.github.wenqiglantz.service.customerservicebff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerServiceBffApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication app = new SpringApplication(CustomerServiceBffApplication.class);
    app.run();
  }
}
