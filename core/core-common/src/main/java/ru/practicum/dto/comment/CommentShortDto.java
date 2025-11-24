package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserDto;

import static ru.practicum.constants.Constants.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {

    private Long id;

    private String text;

    private UserDto author;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private String createTime;

}