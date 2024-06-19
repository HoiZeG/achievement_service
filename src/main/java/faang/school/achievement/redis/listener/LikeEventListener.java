package faang.school.achievement.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.LikeEventDto;
import faang.school.achievement.redis.handler.LikeEventHandler;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final List<LikeEventHandler> handlers;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEventDto likeEvent;
        try {
            likeEvent = objectMapper.readValue(message.getBody(), LikeEventDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        handlers.forEach(handler -> executorService.submit(() -> handler.handleEvent(likeEvent)));
    }

    @PreDestroy
    public void close() {
        executorService.shutdown();
    }
}
