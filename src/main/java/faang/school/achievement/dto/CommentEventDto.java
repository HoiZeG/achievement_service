package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentEventDto(

        Long authorId,

        Long postId,

        @JsonProperty("id")
        Long commentId,

        String content) {
}
