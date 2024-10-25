package faang.school.achievement.handler.goal;

import faang.school.achievement.dto.goal.GoalSetEventDto;
import faang.school.achievement.handler.NewEventHandler;

public abstract class CollectorAchievementHandler implements NewEventHandler {
    public abstract void verifyAchievement(GoalSetEventDto goalSetEventDto);
}
