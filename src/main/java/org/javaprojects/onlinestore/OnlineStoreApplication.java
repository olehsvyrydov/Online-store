package org.javaprojects.onlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.javaprojects.onlinestore")
@EntityScan("org.javaprojects.onlinestore.entities")
public class OnlineStoreApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OnlineStoreApplication.class, args);
    }
}
