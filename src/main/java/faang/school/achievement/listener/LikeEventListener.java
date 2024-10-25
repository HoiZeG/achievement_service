package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.LikeEventDto;
import faang.school.achievement.handler.like_event.LikeEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener implements MessageListener {

    private final List<LikeEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(), LikeEventDto.class);
            handlers.forEach(handler -> handler.handle(likeEventDto));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
