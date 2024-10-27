package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BloggerAchievementHandler extends AchievementEventHandler<FollowerEvent> {

    public BloggerAchievementHandler(CacheService<String> cacheService,
                                     CacheService<Achievement> achievementCacheService,
                                     @Qualifier("AchievementServiceImpl") AchievementService achievementService) {
        super(cacheService, achievementCacheService, achievementService);
    }

    @Override
    protected String getAchievementName() {
        return "BLOGGER";
    }

    @Override
    protected long getUserIdFromEvent(FollowerEvent event) {
        return event.getUserId();
    }

    @Override
    protected Class<FollowerEvent> getEventClass() {
        return FollowerEvent.class;
    }

    @Override
    protected Class<?> getHandlerClass() {
        return this.getClass();
    }
}
