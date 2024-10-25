package faang.school.achievement.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.goal.GoalSetEventDto;
import faang.school.achievement.handler.goal.CollectorAchievementHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalEventListenerTest {

    @InjectMocks
    private GoalEventListener goalEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private List<CollectorAchievementHandler> handlers;

    @Mock
    private Message message;

    private GoalSetEventDto goalSetEventDto;

    @BeforeEach
    void setUp() {
        goalSetEventDto = GoalSetEventDto.builder().build();
    }

    @Test
    @DisplayName("Handle goal event and pass to all handlers")
    void whenJsonPassedThenPassItToAllHandlers() throws IOException {
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(GoalSetEventDto.class))).thenReturn(goalSetEventDto);

        goalEventListener.onMessage(message, null);

        verify(objectMapper).readValue(any(byte[].class), eq(GoalSetEventDto.class));
        verify(handlers).forEach(any());
    }

    @Test
    @DisplayName("Throw RuntimeException if IOException occurs during deserialization")
    void whenIOExceptionOccursThenThrowsException() throws IOException {
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(GoalSetEventDto.class))).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> goalEventListener.onMessage(message, null));
    }
}
