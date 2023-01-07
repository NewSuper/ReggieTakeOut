package cn.tqxd.reggie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan   //扫描 LoginCheckFilter 的过滤器
@EnableTransactionManagement   //允许事务
public class ReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
    }

}
