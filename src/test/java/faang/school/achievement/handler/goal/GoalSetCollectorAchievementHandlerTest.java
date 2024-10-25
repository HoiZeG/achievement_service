package faang.school.achievement.handler.goal;

import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.goal.GoalSetEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalSetCollectorAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private GoalSetCollectorAchievementHandler goalSetCollectorAchievementHandler;

    private Achievement achievement;
    private GoalSetEventDto goalSetEvent;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .id(1L)
                .title("Collector")
                .description("Goal Collector")
                .points(10)
                .build();
        goalSetEvent = GoalSetEventDto.builder()
                .userId(1L)
                .goalId(1L)
                .build();
        progress = AchievementProgress.builder()
                .currentPoints(0)
                .build();

        ReflectionTestUtils.setField(goalSetCollectorAchievementHandler, "goalName", "Collector");
        ReflectionTestUtils.setField(goalSetCollectorAchievementHandler, "pointsNeededToGetCollectorAchievement", 5L);
    }

    @Test
    @DisplayName("Verify achievement without reaching the required points")
    void verifyAchievementWithoutReachingRequiredPoints() {
        when(achievementCache.getAchievement("Collector")).thenReturn(achievement);
        when(achievementService.hasAchievement(goalSetEvent.getUserId(), achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(goalSetEvent.getUserId(), achievement.getId())).thenReturn(progress);

        goalSetCollectorAchievementHandler.verifyAchievement(goalSetEvent);

        assertEquals(1, progress.getCurrentPoints());
        verify(achievementCache).getAchievement("Collector");
        verify(achievementService).hasAchievement(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService).createProgressIfNecessary(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService).getProgress(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService, times(0)).giveAchievement(any(UserAchievement.class));
    }

    @Test
    @DisplayName("Verify achievement when reaching the required points")
    void verifyAchievementWhenReachingRequiredPoints() {
        progress.setCurrentPoints(4);
        when(achievementCache.getAchievement("Collector")).thenReturn(achievement);
        when(achievementService.hasAchievement(goalSetEvent.getUserId(), achievement.getId())).thenReturn(false);
        when(achievementService.getProgress(goalSetEvent.getUserId(), achievement.getId())).thenReturn(progress);

        goalSetCollectorAchievementHandler.verifyAchievement(goalSetEvent);

        assertEquals(5, progress.getCurrentPoints());
        verify(achievementCache).getAchievement("Collector");
        verify(achievementService).hasAchievement(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService).createProgressIfNecessary(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService).getProgress(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService).giveAchievement(any(UserAchievement.class));
    }

    @Test
    @DisplayName("Verify achievement when user already has the achievement")
    void verifyAchievementWhenUserAlreadyHasAchievement() {
        when(achievementCache.getAchievement("Collector")).thenReturn(achievement);
        when(achievementService.hasAchievement(goalSetEvent.getUserId(), achievement.getId())).thenReturn(true);

        goalSetCollectorAchievementHandler.verifyAchievement(goalSetEvent);

        verify(achievementCache).getAchievement("Collector");
        verify(achievementService).hasAchievement(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService, times(0)).createProgressIfNecessary(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService, times(0)).getProgress(goalSetEvent.getUserId(), achievement.getId());
        verify(achievementService, times(0)).giveAchievement(any(UserAchievement.class));
    }
}
