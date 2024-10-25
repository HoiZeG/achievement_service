package faang.school.achievement.handler.goal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.goal.GoalSetEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoalSetCollectorAchievementHandler extends CollectorAchievementHandler {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Value("${goal-set.name}")
    private String goalName;

    @Value("${goal-set.points-needed}")
    private long pointsNeededToGetCollectorAchievement;

    @Async
    @Override
    public void verifyAchievement(GoalSetEventDto goalSetEvent) {
        Achievement achivement = achievementCache.getAchievement(goalName);

        boolean hasAchievement = achievementService.hasAchievement(goalSetEvent.getUserId(), achivement.getId());
        if (hasAchievement) {
            log.debug("User with ID {0} already has achievement with ID {1}", goalSetEvent.getUserId(),
                    achivement.getId());
            return;
        }

        achievementService.createProgressIfNecessary(goalSetEvent.getUserId(), achivement.getId());

        AchievementProgress achievementProgress = achievementService.getProgress(
                goalSetEvent.getUserId(),
                achivement.getId());

        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() == pointsNeededToGetCollectorAchievement) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .achievement(achivement)
                    .userId(goalSetEvent.getUserId())
                    .build();

            achievementService.giveAchievement(userAchievement);
        }

    }
}
