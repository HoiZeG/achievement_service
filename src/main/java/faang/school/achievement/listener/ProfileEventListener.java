package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.ProfileEventDto;
import faang.school.achievement.handler.AbstractEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProfileEventListener extends AbstractEventListener<ProfileEventDto> {

    public ProfileEventListener(
            List<AbstractEventHandler<ProfileEventDto>> abstractEventHandlers,
            ObjectMapper objectMapper) {
        super(abstractEventHandlers, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, ProfileEventDto.class);
    }
}
