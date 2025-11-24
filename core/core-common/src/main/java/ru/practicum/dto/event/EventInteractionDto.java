package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static ru.practicum.constants.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventInteractionDto {

    private Long id;

    private Long initiatorId;

    private Long categoryId;

    private String title;

    private String annotation;

    private String description;

    private State state;

    private LocationDto location;

    private Long participantLimit;

    private Boolean requestModeration;

    private Boolean paid;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    public static EventInteractionDto makeDummy(Long id) {
        EventInteractionDto dto = new EventInteractionDto();
        dto.setId(id);
        return dto;
    }

}