package faang.school.achievement.handler.comment;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpertAchievementHandlerTest {

    @Mock
    private AchievementServiceImpl achievementService;

    @Mock
    private AchievementCache achievementCache;

    private AchievementProgressHandler achievementProgressHandler;

    private ExpertAchievementHandler expertAchievementHandler;

    private Achievement achievement;
    private CommentEventDto event;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .id(1L)
                .title("Expert")
                .description("Expert")
                .points(10)
                .build();
        event = CommentEventDto.builder()
                .authorId(1L)
                .commentId(1L)
                .postId(1L)
                .build();
        progress = AchievementProgress.builder()
                .currentPoints(0)
                .build();
        achievementProgressHandler = Mockito.spy(new AchievementProgressHandler(achievementService));
        expertAchievementHandler = new ExpertAchievementHandler(
                achievementService,
                achievementCache,
                achievementProgressHandler);
        expertAchievementHandler.setTitle("Expert");
    }

    @Test
    @DisplayName("Handle achievement with progress increment")
    void expertAchievementHandlerTest_handleAchievementWithProgressIncrement() {
        when(achievementCache.getByTitle(expertAchievementHandler.getAchievementTitle())).thenReturn(achievement);
        when(achievementService.getProgress(event.authorId(), achievement.getId())).thenReturn(progress);

        expertAchievementHandler.handle(event);

        assertEquals(1, progress.getCurrentPoints());
        verify(achievementCache).getByTitle(expertAchievementHandler.getAchievementTitle());
        verify(achievementService).hasAchievement(event.authorId(), achievement.getId());
        verify(achievementProgressHandler).handleAchievementProgress(event.authorId(), achievement);
        verify(achievementService).createProgressIfNecessary(event.authorId(), achievement.getId());
        verify(achievementService).getProgress(event.authorId(), achievement.getId());
        verify(achievementService).updateProgress(progress);
    }

    @Test
    @DisplayName("Handle achievement with giving achievement")
    void expertAchievementHandlerTest_handleAchievementWithGivingAchievement() {
        achievement.setPoints(1);
        when(achievementCache.getByTitle(expertAchievementHandler.getAchievementTitle())).thenReturn(achievement);
        when(achievementService.getProgress(event.authorId(), achievement.getId())).thenReturn(progress);

        expertAchievementHandler.handle(event);

        verify(achievementCache).getByTitle(expertAchievementHandler.getAchievementTitle());
        verify(achievementService).hasAchievement(event.authorId(), achievement.getId());
        verify(achievementProgressHandler).handleAchievementProgress(event.authorId(), achievement);
        verify(achievementService).createProgressIfNecessary(event.authorId(), achievement.getId());
        verify(achievementService).getProgress(event.authorId(), achievement.getId());
        verify(achievementService).giveAchievement(event.authorId(), achievement.getId());
    }

    @Test
    @DisplayName("Handle achievement with already given achievement")
    void expertAchievementHandlerTest_handleAchievementWithAlreadyGivenAchievement() {
        when(achievementCache.getByTitle(expertAchievementHandler.getAchievementTitle())).thenReturn(achievement);
        when(achievementService.hasAchievement(event.authorId(), achievement.getId())).thenReturn(true);

        expertAchievementHandler.handle(event);

        verify(achievementCache).getByTitle(expertAchievementHandler.getAchievementTitle());
        verify(achievementService).hasAchievement(event.authorId(), achievement.getId());
        verify(achievementProgressHandler, times(0)).handleAchievementProgress(event.authorId(), achievement);
    }
}
