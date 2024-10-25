package faang.school.achievement.publisher;

import faang.school.achievement.dto.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementMessagePublisher implements MessagePublisher<AchievementEvent> {

    private final RedisTemplate<String, AchievementEvent> redisTemplate;

    @Value("${spring.data.redis.channels.achievement-channel.name}")
    private String achievementChannelName;

    @Retryable(retryFor = {RuntimeException.class},
            backoff = @Backoff(delayExpression = "${spring.data.redis.publisher.delay}"))
    @Override
    public void publish(AchievementEvent event) {
        try {
            redisTemplate.convertAndSend(achievementChannelName, event);
            log.debug("Published achievement: {}", event);
        } catch (Exception e) {
            log.error("Failing to publish achievement {}", event, e);
            throw new RuntimeException(e);
        }
    }
}
