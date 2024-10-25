package faang.school.achievement.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.goal.GoalSetEventDto;
import faang.school.achievement.handler.goal.CollectorAchievementHandler;
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
public class GoalEventListener implements MessageListener {

    private final List<CollectorAchievementHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalSetEventDto goal = objectMapper.readValue(
                    message.getBody(),
                    GoalSetEventDto.class);

            log.info("Sending goal event to all handlers");

            handlers.forEach(handler -> handler.verifyAchievement(goal));

            log.info("Event has been sent successfully");

        } catch (IOException e) {
            log.error("Something went wrong while reading object {}", e.getMessage());
            throw new RuntimeException();
        }
    }
}