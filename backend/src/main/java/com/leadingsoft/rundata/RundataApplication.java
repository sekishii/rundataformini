package com.leadingsoft.rundata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class RundataApplication {

  public static void main(String[] args) {
    SpringApplication.run(RundataApplication.class, args);
  }

}
