package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }


    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public Optional<AchievementProgress> getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public AchievementProgress createProgressIfNecessaryOrGet(long userId, long achievementId) {
        createProgressIfNecessary(userId, achievementId);
        return getProgress(userId, achievementId)
                .orElseThrow(() -> new RuntimeException("ПОМЕНЯТЬ"));
    }

    @Transactional
    public AchievementProgress incrementProgress(AchievementProgress achievementProgress) {
        achievementProgress.increment();
        return achievementProgressRepository.save(achievementProgress);
    }

//    @Async // Pool potokov create
    public UserAchievement giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        return userAchievementRepository.save(userAchievement);
    }
}