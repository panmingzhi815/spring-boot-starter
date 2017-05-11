package org.pan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by panmingzhi on 2017/5/3.
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "org.pan.mapper")
public class ApplicationStart {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationStart.class, args);
        System.out.println("http://127.0.0.1:8080/swagger-ui.html");
    }
}
