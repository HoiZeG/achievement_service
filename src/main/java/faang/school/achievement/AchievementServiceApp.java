package faang.school.achievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@PropertySource(value = "classpath:resource_achievement/achievement.properties", encoding = "UTF-8")
@EnableFeignClients("faang.school.achievement.client")
@EnableAsync
@EnableRetry
public class AchievementServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AchievementServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
