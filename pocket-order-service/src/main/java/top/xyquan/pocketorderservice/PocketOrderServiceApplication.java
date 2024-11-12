package top.xyquan.pocketorderservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("top.xyquan.pocketorderservice.mapper")
public class PocketOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocketOrderServiceApplication.class, args);
    }

}
