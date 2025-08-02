package app.sigorotalk.backend.domain.product;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrowingScheduleDto implements Serializable {
    private Long id;       // 일정 순서 (1, 2, 3...)
    private LocalDate date;     // 날짜
    private String title;       // 예: 파종
    private String content;     // 설명글
}
