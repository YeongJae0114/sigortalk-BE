package app.sigorotalk.backend.domain.user;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.order.OrderRepository;
import app.sigorotalk.backend.domain.user.dto.MyDashboardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public MyDashboardResponseDto getDashboardInfo(Long userId) {
        // 1. 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 2. 사용자가 참여한 모든 프로젝트를 중복 없이 조회하여 DTO 리스트로 변환
        List<MyDashboardResponseDto.DashboardProjectDto> myProjects = orderRepository.findAllByUser(user).stream()
                .flatMap(order -> order.getOrderItems().stream()) // 주문 목록을 주문 아이템 스트림으로 변환
                .map(orderItem -> orderItem.getProduct().getFarmProject()) // 각 아이템에서 프로젝트를 추출
                .distinct() // 동일한 프로젝트가 여러 번 나올 수 있으므로 중복 제거
                .map(MyDashboardResponseDto.DashboardProjectDto::from) // 프로젝트를 DTO로 변환
                .collect(Collectors.toList());

        // 3. 최종 응답 DTO를 생성하여 반환
        return MyDashboardResponseDto.of(user, myProjects);
    }
}