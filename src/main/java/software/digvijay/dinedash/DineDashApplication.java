package software.digvijay.dinedash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class DineDashApplication {

    public static void main(String[] args) {
        SpringApplication.run(DineDashApplication.class, args);
    }

    @Bean
    public PlatformTransactionManager mongodbFactory(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public RestTemplate abc() {
        return new RestTemplate();
    }
}
