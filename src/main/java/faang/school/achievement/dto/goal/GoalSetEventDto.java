package faang.school.achievement.dto.goal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalSetEventDto {
    private Long userId;
    private Long goalId;
}
