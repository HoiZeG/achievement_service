package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.handler.AbstractEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {
    public CommentEventListener(
            List<AbstractEventHandler<CommentEventDto>> abstractEventHandlers,
            ObjectMapper objectMapper) {
        super(abstractEventHandlers, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, CommentEventDto.class);
    }
}
