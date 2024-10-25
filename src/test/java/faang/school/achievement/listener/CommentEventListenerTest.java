package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.comment.CommentEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @Spy
    private ObjectMapper objectMapper;

    private List<AbstractEventHandler<CommentEventDto>> handlers;

    private CommentEventListener listener;

    private Message message;

    @BeforeEach
    void setUp() {
        message = new DefaultMessage("test".getBytes(),
                "{\"authorId\":1,\"postId\":1,\"commentId\":1,\"content\":\"test\"}".getBytes());
        handlers = List.of(
                Mockito.mock(CommentEventHandler.class),
                Mockito.mock(CommentEventHandler.class));
        listener = new CommentEventListener(handlers, objectMapper);
    }

    @Test
    @DisplayName("Handle event")
    void commentEventListenerTest_handleEvent() throws IOException {
        listener.onMessage(message, null);

        verify(objectMapper).readValue(message.getBody(), CommentEventDto.class);
        handlers.forEach(handler -> verify(handler).handle(Mockito.any()));
    }

    @Test
    @DisplayName("Handle event without handlers")
    void commentEventListenerTest_handleEventWithoutHandlers() throws IOException{
        handlers = new ArrayList<>();

        assertDoesNotThrow(() -> listener.onMessage(message, null));
        verify(objectMapper).readValue(message.getBody(), CommentEventDto.class);
    }

    @Test
    @DisplayName("Handle event with exception")
    void commentEventListenerTest_handleEventWithException() throws IOException {
        doThrow(new IOException("error")).when(objectMapper).readValue(message.getBody(), CommentEventDto.class);

        assertThrows(RuntimeException.class, () -> listener.onMessage(message, null));
    }
}
