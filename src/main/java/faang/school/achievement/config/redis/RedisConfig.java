package faang.school.achievement.config.redis;

import faang.school.achievement.listener.LikeEventListener;
import faang.school.achievement.listener.ProfileEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    MessageListenerAdapter profileEventAdapter(ProfileEventListener profileEventListener) {
        return new MessageListenerAdapter(profileEventListener);
    }

    @Bean(value = "likeChannelTopic")
    ChannelTopic likeChannelTopic(@Value("${spring.data.redis.channel.like-channel.name}") String likeChannelName) {
        return new ChannelTopic(likeChannelName);
    }

    @Bean(value = "profileChannelTopic")
    ChannelTopic profileChannelTopic(@Value("${spring.data.redis.channel.profile-channel.name}") String profilePicChannelName) {
        return new ChannelTopic(profilePicChannelName);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            LikeEventListener likeEventListener,
            ProfileEventListener profileEventListener,
            @Qualifier("likeChannelTopic") ChannelTopic likeChannelTopic,
            @Qualifier("profileChannelTopic") ChannelTopic profileChannelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventListener, likeChannelTopic);
        container.addMessageListener(profileEventListener, profileChannelTopic);
        return container;
    }
}
