package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@RequiredArgsConstructor
public class AchievementPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic achievementTopic;
    private final ObjectMapper objectMapper;

    public void publish(AchievementEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(achievementTopic.getTopic(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
