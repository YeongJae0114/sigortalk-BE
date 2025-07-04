package app.sigorotalk.backend.domain.mentor;


import app.sigorotalk.backend.common.entity.BaseTimeEntity;
import app.sigorotalk.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_mentor")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mentor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String shortDescription;

    @Lob
    private String detailedDescription;

    @Column(length = 100)
    private String expertise;

    @Column(length = 100)
    private String region;

    private String profileImageUrl;

    @Column(precision = 2, scale = 1)
    private BigDecimal averageRating;

    private Integer reviewCount;
}
