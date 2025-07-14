package app.sigorotalk.backend.domain.review;

import lombok.Getter;

@Getter
public class ReviewCreatedEvent {
    private final Long mentorId;

    public ReviewCreatedEvent(Long mentorId) {
        this.mentorId = mentorId;
    }
}