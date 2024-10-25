package faang.school.achievement.handler.comment;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementService;

public abstract class CommentEventHandler extends AbstractEventHandler<CommentEventDto> {
    public CommentEventHandler(
            AchievementService achievementService,
            AchievementCache achievementCache,
            AchievementProgressHandler achievementProgressHandler) {
        super(achievementService, achievementCache, achievementProgressHandler);
    }

    @Override
    public void handle(CommentEventDto event) {
        handleAchievement(event.authorId(), getAchievementTitle());
    }
}
