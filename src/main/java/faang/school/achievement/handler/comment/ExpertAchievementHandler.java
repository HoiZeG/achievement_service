package faang.school.achievement.handler.comment;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Component
public class ExpertAchievementHandler extends CommentEventHandler {

    @Value("${achievements.comment-achievements.expert.name}")
    private String title;

    public ExpertAchievementHandler(
            AchievementService achievementService,
            AchievementCache achievementCache,
            AchievementProgressHandler achievementProgressHandler) {
        super(achievementService, achievementCache, achievementProgressHandler);
    }

    @Override
    protected String getAchievementTitle() {
        return title;
    }
}
