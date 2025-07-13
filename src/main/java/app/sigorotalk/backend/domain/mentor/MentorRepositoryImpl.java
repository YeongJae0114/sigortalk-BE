package app.sigorotalk.backend.domain.mentor;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static app.sigorotalk.backend.domain.mentor.QMentor.mentor;
import static app.sigorotalk.backend.domain.user.QUser.user;


@RequiredArgsConstructor
public class MentorRepositoryImpl implements MentorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Mentor> findByFilters(String region, String expertise, Pageable pageable) {
        List<Mentor> content = queryFactory
                .selectFrom(mentor)
                .join(mentor.user, user).fetchJoin() // N+1 문제 해결을 위한 fetchJoin
                .where(
                        regionEq(region),
                        expertiseEq(expertise)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(mentor.id.desc()) // 정렬 기준 추가
                .fetch();

        Long total = queryFactory
                .select(mentor.count())
                .from(mentor)
                .where(
                        regionEq(region),
                        expertiseEq(expertise)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    // region 파라미터가 있으면 해당 조건 반환, 없으면 null 반환
    private BooleanExpression regionEq(String region) {
        return StringUtils.hasText(region) ? mentor.region.eq(region) : null;
    }

    // expertise 파라미터가 있으면 해당 조건 반환, 없으면 null 반환
    private BooleanExpression expertiseEq(String expertise) {
        return StringUtils.hasText(expertise) ? mentor.expertise.contains(expertise) : null;
    }
}