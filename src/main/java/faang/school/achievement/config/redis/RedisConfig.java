package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.listener.AchievementEventListener;
import faang.school.achievement.listener.CommentEventListener;
import faang.school.achievement.listener.LikeEventListener;
import faang.school.achievement.listener.MentorshipEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            Map<String, MessageListenerAdapter> listenerAdapters,
            Map<String, ChannelTopic> topics) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(listenerAdapters.get("likeEventAdapter"), topics.get("likeChannelTopic"));
        container.addMessageListener(listenerAdapters.get("mentorshipEventAdapter"), topics.get("mentorshipEventChannel"));
        return container;
    }

    @Bean
    MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    MessageListenerAdapter mentorshipEventAdapter(MentorshipEventListener mentorshipEventListener) {
        return new MessageListenerAdapter(mentorshipEventListener);
    }

    @Bean(value = "likeChannelTopic")
    ChannelTopic likeChannelTopic(@Value("${spring.data.redis.channel.like-channel.name}") String likeChannelName) {
        return new ChannelTopic(likeChannelName);
    }

    @Bean
    public RedisTemplate<String, AchievementEvent> achievementEventRedisTemplate() {
        RedisTemplate<String, AchievementEvent> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, AchievementEvent.class));
        return template;
    }

    @Bean
    ChannelListenerAdapter likeChannelListenerAdapter(
            LikeEventListener likeEventListener,
            @Value("${spring.data.redis.channels.like-channel.name}") String likeChannelName) {
        return new ChannelListenerAdapter(likeEventListener, likeChannelName);
    }

    @Bean
    ChannelListenerAdapter achievementChannelListenerAdapter(
            AchievementEventListener achievementEventListener,
            @Value("${spring.data.redis.channels.achievement-channel.name}") String achievementChannelName) {
        return new ChannelListenerAdapter(achievementEventListener, achievementChannelName);
    }

    @Bean
    ChannelListenerAdapter commentChannelListenerAdapter(
            CommentEventListener commentEventListener,
            @Value("${spring.data.redis.channels.comment-channel.name}") String commentChannelName) {
        return new ChannelListenerAdapter(commentEventListener, commentChannelName);
    }

    @Bean
    public RedisMessageListenerContainer container(List<ChannelListenerAdapter> channelListenerAdapters) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        channelListenerAdapters.forEach(
                listener -> container.addMessageListener(listener.getListenerAdapter(), listener.getTopic()));
        return container;
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            LikeEventListener likeEventListener,
            @Qualifier("likeChannelTopic") ChannelTopic likeChannelTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventListener, likeChannelTopic);

        return container;
    }

    @Bean(value = "mentorshipEventChannel")
    ChannelTopic mentorshipEventTopic(@Value("${spring.data.redis.channels.mentorship-channel.name}") String name) {
        return new ChannelTopic(name);
    }
}
