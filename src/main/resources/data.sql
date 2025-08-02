
INSERT INTO users (
    user_id, created_at, updated_at, phone_number, user_type, name, email, refresh_token, password
) VALUES
      (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-1234-5678', 'BUYER', '홍길동', 'buyer2@example.com', NULL, 'buyerpass123'),
      (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-8765-4321', 'BUYER', '이순신', 'farmer2@example.com', NULL, 'farmerpass456'),
      (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-8765-2222', 'BUYER', '이영재', 'dudwo@example.com', NULL, 'dudwo1234'),
      (4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-8765-3333', 'BUYER', '조장호', 'wkdgh@example.com', NULL, 'wkdgh1234'),
      (5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-8765-4444', 'BUYER', '이예도', 'dPeh@example.com', NULL, 'dPeh1234'),
      (13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-0000-1013', 'FARMER', '김민수', 'user13@example.com', NULL, 'user13pass'),
      (14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-0000-1014', 'FARMER', '박철호', 'user14@example.com', NULL, 'user14pass'),
      (15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-0000-1015', 'FARMER', '최옥자', 'user15@example.com', NULL, 'user15pass'),
      (16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-0000-1016', 'FARMER', '조성환', 'user16@example.com', NULL, 'user16pass'),
      (17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '010-0000-1017', 'FARMER', '강성구', 'user17@example.com', NULL, 'user17pass');

-- FARMERS 테이블 초기 데이터
INSERT INTO farmers (
    id, created_at, updated_at, user_id, farm_location, operation_experience, phone_number, profile, cultivation_method, delivery_system
) VALUES
    (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 13, '경상북도 의성군', '10년 이상', '010-1111-4321', 'https://example.com/profile.jpg',
     '{"유기농": true, "스마트팜": false}',
     '{"택배": true, "방문수령": false}'),
    (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 14, '충청남도 천안시', '8년 이상', '010-3333-1234', 'https://example.com/farmer2.jpg',
     '{"유기농": false, "스마트팜": true}',
     '{"택배": true, "방문수령": true}'),
    (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 15, '강원도 원주시', '6년 이상', '010-3333-5678', 'https://example.com/farmer3.jpg',
     '{"유기농": true, "스마트팜": false}',
     '{"택배": false, "방문수령": true}'),
    (4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 16, '전라남도 여수시', '12년 이상', '010-4444-9101', 'https://example.com/farmer4.jpg',
     '{"유기농": true, "스마트팜": true}',
     '{"택배": true, "방문수령": false}'),
    (5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 17, '부산광역시 해운대구', '4년 이상', '010-5555-1212', 'https://example.com/farmer5.jpg',
     '{"유기농": false, "스마트팜": true}',
     '{"택배": true, "방문수령": true}');

-- FARM_PROJECTS 테이블 초기 데이터
INSERT INTO farm_projects (
    id, name, created_at, updated_at, farmer_id
) VALUES
      -- farmers.id = 1
      (1,  '의성 황금 감자 수확 대축제',            CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
      (2,  '의성 들녘 배추 정성 보존 프로젝트',       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
      (3,  '의성 과수원 사과 첫 열매 맞이',           CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),

      -- farmers.id = 2
      (4,  '천안 토마토 향기 가득 스마트팜',         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
      (5,  '천안 청양고추 매운맛 체험 농장',         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
      (6,  '천안 온실 배추 사랑 담은 재배',         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),

      -- farmers.id = 3
      (7,  '원주 숲속 표고버섯 품은 생태농장',        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
      (8,  '원주 햇살 고구마 달콤이 스토리',         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
      (9,  '원주 토종콩 흙내음 가득 프로젝트',       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),

      -- farmers.id = 4
      (10, '여수 바닷바람 상추 신선함',             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),
      (11, '여수 바다 속 미역 품은 농장',           CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),
      (12, '여수 유채꽃 꿀벌 행복지기',             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),

      -- farmers.id = 5
      (13, '해운대 아쿠아포닉스 그린하우스',         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),
      (14, '해운대 햇살 방울토마토 온실',            CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),
      (15, '해운대 허브향 가득 허니팜',             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

-- products 테이블

INSERT INTO products (
    id, name, description, price, stock, max_quantity, delivery_fee,
    funding_deadline, nutrition_info, growing_schedules, image_urls,
    created_at, updated_at, farm_project_id
) VALUES
-- 1. 유기농 감자
(
    1, '유기농 감자 5kg', '신선한 유기농 감자입니다.', 15000.00, 100, 5, 2500.00,
    '2025-08-31', '비타민C, 칼륨 풍부',
    '{"봄": "파종", "여름": "성장", "가을": "수확"}',
    '["https://example.com/image1.jpg", "https://example.com/image2.jpg"]',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1
),

-- 2. 방울토마토
(
    2, '방울토마토 3kg', '달콤한 방울토마토입니다.', 22000.00, 80, 4, 3000.00,
    '2025-09-10', '라이코펜, 비타민A 풍부',
    '{"봄": "모종 심기", "여름": "수확", "가을": "마무리"}',
    '["https://example.com/image3.jpg", "https://example.com/image4.jpg"]',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1
),

-- 3. 친환경 상추
(
    3, '친환경 상추 2kg', '아삭하고 신선한 상추입니다.', 10000.00, 120, 3, 2000.00,
    '2025-08-20', '식이섬유, 미네랄 풍부',
    '{"봄": "파종", "여름": "수확", "가을": "재배 종료"}',
    '["https://example.com/image5.jpg", "https://example.com/image6.jpg"]',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1
);



-- ORDERS 테이블 초기 데이터
INSERT INTO orders (
    id, total_price, address, delivery_status, order_status,
    created_at, updated_at, shipped_at, delivered_at, user_id
) VALUES
      (
          1, 37000.00, '서울시 강남구 테헤란로 123',
          'PREPARING', 'PAYMENT_COMPLETED',
          CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
          NULL, NULL,
          1
      ),
      (
          2, 15000.00, '부산시 해운대구 센텀중앙로 456',
          'DELIVERED', 'PAYMENT_COMPLETED',
          CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
          CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
          2
      );


-- order_items
INSERT INTO order_items ( id, quantity, unit_price, created_at, updated_at, order_id, product_id )
VALUES
    (1, 2, 15000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
    (2, 1, 22000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 2);

-- DIARIES 테이블 초기 데이터 삽입
INSERT INTO diaries (
    id, product_id, content, status, image_urls, tags,
    created_at, updated_at
) VALUES
(
    1,
    1,
    '감자가 잘 자라고 있어요! 오늘은 물을 듬뿍 줬습니다.',
    'GROWING',
    '["https://example.com/growing1.jpg", "https://example.com/growing2.jpg"]',
    '["성장", "물주기"]',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    2,
    1,
    '수확 완료! 포장 준비 중입니다.',
    'HARVESTED',
    '["https://example.com/harvest.jpg"]',
    '["수확", "포장준비"]',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);


-- REVIEWS 테이블 초기 데이터
INSERT INTO reviews (
    id, rating, comment, image_urls, created_at, updated_at, product_id, user_id
) VALUES
      (
          1, 4.5, '아주 신선하고 맛있어요!',
          '["https://example.com/review1.jpg", "https://example.com/review2.jpg"]',
          CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
          1, 1
      ),
      (
          2, 5.0, '포장도 깔끔하고 배송도 빨랐습니다.',
          '["https://example.com/review3.jpg"]',
          CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
          1, 2
      );

ALTER TABLE USERS ALTER COLUMN user_id RESTART WITH 18;
ALTER TABLE DIARIES ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE FARMERS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE FARM_PROJECTS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE ORDERS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE ORDER_ITEMS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE PAYMENTS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE PRODUCTS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE REVIEWS ALTER COLUMN ID RESTART WITH 100;

