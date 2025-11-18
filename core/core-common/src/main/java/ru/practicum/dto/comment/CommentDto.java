package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventCommentDto;
import ru.practicum.dto.user.UserDto;

import java.time.LocalDateTime;

import static ru.practicum.constants.Constants.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String text;

    private UserDto author;

    private EventCommentDto event;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime patchTime;

    private Boolean approved;

}