package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.ProfilePicEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ProfilePicEventListener extends AbstractEventListener<ProfilePicEvent> implements MessageListener {

    public ProfilePicEventListener(ObjectMapper objectMapper, List<EventHandler<ProfilePicEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProfilePicEvent profilePicEvent = objectMapper.readValue(message.getBody(), ProfilePicEvent.class);
            handleEvent(profilePicEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
