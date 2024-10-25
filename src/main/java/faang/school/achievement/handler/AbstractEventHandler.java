package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final AchievementService achievementService;
    private final AchievementCache achievementCache;
    private final AchievementProgressHandler achievementProgressHandler;

    @Async("taskExecutor")
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttemptsExpression = "${retryable.max-attempts}",
            backoff = @Backoff(delayExpression = "${retryable.delay}"))
    protected void handleAchievement(Long userId, String achievementTitle) {
        Achievement achievement = achievementCache.getByTitle(achievementTitle);
        Long achievementId = achievement.getId();
        if (achievementService.hasAchievement(userId, achievementId)) {
            log.warn("User %d already has achievement %d".formatted(userId, achievement.getId()));
            return;
        }
        achievementProgressHandler.handleAchievementProgress(userId, achievement);
    }

    protected abstract String getAchievementTitle();
}
