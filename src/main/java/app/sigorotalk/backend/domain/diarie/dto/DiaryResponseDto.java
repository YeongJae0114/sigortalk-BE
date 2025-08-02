package app.sigorotalk.backend.domain.diarie.dto;

import app.sigorotalk.backend.domain.diarie.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DiaryResponseDto {

    private final Long diaryId;
    private final String content;
    private final String status;
    private final String imageUrl;
    private final String tag;
    private final LocalDateTime createdAt;

    public static DiaryResponseDto from(Diary diary) {
        return DiaryResponseDto.builder()
                .diaryId(diary.getId())
                .content(diary.getContent())
                .status(diary.getStatus())
                .imageUrl(diary.getImageUrl())
                .tag(diary.getTag())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}