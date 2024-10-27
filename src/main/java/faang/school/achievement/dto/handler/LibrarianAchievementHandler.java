package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.AlbumCreatedEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LibrarianAchievementHandler extends AchievementEventHandler<AlbumCreatedEvent> {

    public LibrarianAchievementHandler(CacheService<String> cacheService,
                                       CacheService<Achievement> achievementCacheService,
                                       @Qualifier("AchievementServiceImpl") AchievementService achievementService) {
        super(cacheService, achievementCacheService, achievementService);
    }

    @Override
    protected String getAchievementName() {
        return "LIBRARIAN";
    }

    @Override
    protected long getUserIdFromEvent(AlbumCreatedEvent event) {
        return event.getUserId();
    }

    @Override
    protected Class<AlbumCreatedEvent> getEventClass() {
        return AlbumCreatedEvent.class;
    }

    @Override
    protected Class<?> getHandlerClass() {
        return this.getClass();
    }
}
