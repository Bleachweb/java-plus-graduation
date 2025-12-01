package ru.practicum.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.api.request.RequestApi;
import ru.practicum.exception.ServiceInteractionException;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class RequestClientAbstractHelper {

    protected final RequestApi requestApiClient;

    // Confirmed Requests Map - by EventId List
    public Map<Long, Long> retrieveConfirmedRequestsMapByEventIdList(Collection<Long> eventIdList) {
        try {
            return requestApiClient.getConfirmedRequestsByEventIds(eventIdList);
        } catch (RuntimeException e) {
            log.warn("Service Interaction Error: caught " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return eventIdList.stream().collect(Collectors.toMap(id -> id, id -> -1L));
        }
    }

    // Participation Check

    public boolean passedParticipationCheck(Long userId, Long eventId) {
        try {
            return requestApiClient.checkParticipation(userId, eventId);
        } catch (FeignException.NotFound e) {
            return false;
        } catch (RuntimeException e) {
            for (Throwable cause = e.getCause(); cause != null; cause = cause.getCause()) {
                if (cause instanceof FeignException.NotFound) {
                    return false;
                }
            }
            log.warn("Service Interaction Error: caught {} - {}",
                    e.getClass().getSimpleName(), e.getMessage());
            throw new ServiceInteractionException(
                    "Unable to confirm participation of user " + userId + " in event " + eventId
            );
        }
    }

}