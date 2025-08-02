
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
-- 농부 1
    (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 13, '경상북도 의성군', '10년 이상', '010-1111-4321', 'https://example.com/profile.jpg',
    '유기농',
    '택배'),

-- 농부 2
    (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 14, '충청남도 천안시', '8년 이상', '010-3333-1234', 'https://example.com/farmer2.jpg',
    '스마트팜',
    '택배,방문수령'),

-- 농부 3
    (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 15, '강원도 원주시', '6년 이상', '010-3333-5678', 'https://example.com/farmer3.jpg',
    '유기농',
    '방문수령'),

-- 농부 4
    (4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 16, '전라남도 여수시', '12년 이상', '010-4444-9101', 'https://example.com/farmer4.jpg',
    '유기농,스마트팜',
    '택배'),

-- 농부 5
    (5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 17, '부산광역시 해운대구', '4년 이상', '010-5555-1212', 'https://example.com/farmer5.jpg',
    '스마트팜',
    '택배,방문수령');


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
    funding_deadline, nutrition_info, image_url,
    created_at, updated_at, farm_project_id
) VALUES
      (1, '의성 황금 감자 5kg', '의성에서 수확한 신선한 유기농 감자입니다.', 15000.00, 100, 5, 2500.00,
       '2025-08-31', '비타민C, 칼륨 풍부',
       'https://i.ibb.co/MDBJcT0T/potatoes-1585075-1280.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),

      (2, '무안 배추 3포기', '무안에서 유기농으로 재배한 배추입니다.', 12000.00, 80, 3, 3000.00,
       '2025-08-25', '식이섬유, 비타민A',
       'https://i.ibb.co/Rpf4nQhQ/chinese-cabbage-5798137-1280.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),

      (3, '청송 사과 4kg', '청송 사과를 엄선하여 보내드립니다.', 20000.00, 150, 4, 2500.00,
       '2025-09-10', '비타민C, 폴리페놀',
       'https://i.ibb.co/d4Vrdwyv/apples-6947409-1280.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),

      (4, '토마토 세트 3kg', '신선한 토마토를 담은 건강한 세트입니다.', 14000.00, 120, 4, 2000.00,
       '2025-08-20', '라이코펜, 비타민A',
       'https://i.ibb.co/cKVnmXX2/tomatoes-1280859-1280.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),

      (5, '제주 고추 1kg', '제주도에서 재배한 매운맛 고추입니다.', 11000.00, 60, 2, 2500.00,
       '2025-08-18', '캡사이신, 비타민C',
       'https://example.com/images/chili1.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),

      (6, '완주 절임배추 10kg', '김장철을 위한 절임 배추입니다.', 30000.00, 200, 10, 4000.00,
       '2025-11-01', '식이섬유, 수분',
       'https://example.com/images/cabbage3.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6),

      (7, '표고버섯 세트', '국산 표고버섯을 선별하여 담았습니다.', 17000.00, 90, 3, 3000.00,
       '2025-09-15', '비타민D, 셀레늄',
       'https://example.com/images/shiitake1.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 7),

      (8, '해남 고구마 5kg', '해남에서 재배한 달콤한 고구마입니다.', 16000.00, 100, 5, 2500.00,
       '2025-09-30', '식이섬유, 베타카로틴',
       'https://example.com/images/sweetpotato1.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 8),

      (9, '토종콩 3kg 세트', '전통 방식으로 재배한 원주 토종콩입니다.', 18000.00, 100, 3, 3000.00,
       '2025-10-10', '단백질, 이소플라본',
       'https://example.com/images/bean1.jpg',
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9);

INSERT INTO growing_schedules (id, date, title, content, product_id) VALUES
-- 감자 (product_id = 1)
(1, '2025-03-01', '파종', '봄철에 씨감자를 심습니다.', 1),
(2, '2025-06-01', '성장', '여름에는 감자가 빠르게 자랍니다.', 1),
(3, '2025-09-01', '수확', '가을에 감자를 수확합니다.', 1),

-- 배추 (product_id = 2)
(4, '2025-03-15', '육묘', '배추 씨앗을 키웁니다.', 2),
(5, '2025-06-15', '이식', '배추를 밭에 옮겨 심습니다.', 2),
(6, '2025-09-15', '수확', '잘 자란 배추를 수확합니다.', 2),

-- 사과 (product_id = 3)
(7, '2025-04-01', '꽃 피우기', '사과나무가 꽃을 피웁니다.', 3),
(8, '2025-07-01', '성장', '사과가 자라고 익습니다.', 3),
(9, '2025-10-01', '수확', '익은 사과를 수확합니다.', 3),

-- 방울토마토 (product_id = 4)
(10, '2025-04-10', '모종 심기', '모종을 준비하고 심습니다.', 4),
(11, '2025-07-01', '성장 및 수확', '토마토가 자라며 일부는 수확이 시작됩니다.', 4),
(12, '2025-09-01', '마무리', '재배를 마무리합니다.', 4),

-- 청양고추 (product_id = 5)
(13, '2025-03-20', '파종', '고추 씨앗을 심습니다.', 5),
(14, '2025-07-10', '성장 및 수확', '고추가 자라며 수확이 시작됩니다.', 5),
(15, '2025-09-30', '정리', '작업을 마무리하고 밭을 정비합니다.', 5),

-- 온실 배추 (product_id = 6)
(16, '2025-03-10', '육묘', '온실에서 배추를 키웁니다.', 6),
(17, '2025-06-10', '이식', '육묘한 배추를 온실에 이식합니다.', 6),
(18, '2025-09-20', '수확', '온실에서 배추를 수확합니다.', 6),

-- 표고버섯 (product_id = 7)
(19, '2025-03-05', '균 배양', '균을 배양하여 준비합니다.', 7),
(20, '2025-06-05', '성장', '표고버섯이 자라납니다.', 7),
(21, '2025-09-25', '수확', '수확 적기에 맞춰 수확합니다.', 7),

-- 고구마 (product_id = 8)
(22, '2025-03-01', '파종', '고구마 순을 심습니다.', 8),
(23, '2025-07-01', '성장', '고구마가 자라고 뿌리가 굵어집니다.', 8),
(24, '2025-10-01', '수확', '고구마를 캐서 수확합니다.', 8),

-- 토종콩 (product_id = 9)
(25, '2025-03-15', '파종', '콩 씨앗을 밭에 심습니다.', 9),
(26, '2025-07-01', '관리', '잡초 제거와 물 관리를 합니다.', 9),
(27, '2025-10-10', '수확', '익은 콩을 수확합니다.', 9);


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
    id, product_id, content, status, image_url, tag,
    created_at, updated_at
) VALUES
-- 제품 1
(1, 1, '감자가 잘 자라고 있어요! 오늘은 물을 듬뿍 줬습니다.', 'GROWING',
 'https://example.com/images/potato-grow1.jpg', '성장,물주기',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, '수확 완료! 감자를 선별하고 있습니다.', 'HARVESTED',
 'https://example.com/images/potato-harvest.jpg', '수확,선별',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 2
(3, 2, '배추가 튼튼하게 자라고 있어요. 벌레도 없고 상태 좋아요.', 'GROWING',
 'https://example.com/images/cabbage-grow1.jpg', '성장,건강상태',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 2, '배추를 모두 수확하고 정리 중입니다.', 'HARVESTED',
 'https://example.com/images/cabbage-harvest.jpg', '수확,정리중',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 3
(5, 3, '사과가 잘 열리고 있어요! 햇빛이 아주 좋아요.', 'GROWING',
 'https://example.com/images/apple-grow1.jpg', '꽃,햇빛',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 3, '첫 수확 완료! 당도가 아주 높아요.', 'HARVESTED',
 'https://example.com/images/apple-harvest.jpg', '수확,첫수확',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 4
(7, 4, '방울토마토가 탐스럽게 익어가고 있어요.', 'GROWING',
 'https://example.com/images/tomato-grow1.jpg', '성숙,빛관리',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 4, '당도 체크 후 수확 시작했습니다!', 'HARVESTED',
 'https://example.com/images/tomato-harvest.jpg', '수확,선별',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 5
(9, 5, '청양고추가 매운 향을 뿜고 있습니다.', 'GROWING',
 'https://example.com/images/chili-grow1.jpg', '성장,매운맛',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 5, '오늘 아침 수확한 고추를 세척 중입니다.', 'HARVESTED',
 'https://example.com/images/chili-harvest.jpg', '수확,세척',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 6
(11, 6, '온실 배추가 아주 싱싱해요. 곧 수확할 예정입니다.', 'GROWING',
 'https://example.com/images/greenhouse-grow1.jpg', '성장,온실',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 6, '깨끗하게 포장 준비 중입니다.', 'HARVESTED',
 'https://example.com/images/greenhouse-harvest.jpg', '포장,출고준비',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 7
(13, 7, '표고버섯이 촉촉하게 잘 자라고 있어요.', 'GROWING',
 'https://example.com/images/shiitake-grow1.jpg', '성장,습도관리',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 7, '표고버섯 수확 완료. 향이 아주 좋습니다.', 'HARVESTED',
 'https://example.com/images/shiitake-harvest.jpg', '수확,품질검사',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 8
(15, 8, '고구마 줄기가 무성하게 뻗어 있어요!', 'GROWING',
 'https://example.com/images/sweetpotato-grow1.jpg', '성장,줄기관리',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 8, '땅속 고구마 수확 완료, 아주 알이 굵어요.', 'HARVESTED',
 'https://example.com/images/sweetpotato-harvest.jpg', '수확,정리',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 제품 9
(17, 9, '토종콩이 꽃을 피우기 시작했어요.', 'GROWING',
 'https://example.com/images/bean-grow1.jpg', '개화,건강상태',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(18, 9, '콩 수확 완료 후 건조 중입니다.', 'HARVESTED',
 'https://example.com/images/bean-harvest.jpg', '수확,건조',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- REVIEWS 테이블 초기 데이터
INSERT INTO reviews (
    id, rating, comment, image_url, created_at, updated_at, product_id, user_id
) VALUES
-- 제품 1
(1, 4.5, '아주 신선하고 맛있어요!', 'https://example.com/review1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
(2, 5.0, '포장도 깔끔하고 배송도 빨랐습니다.', 'https://example.com/review3.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 2),

-- 제품 2
(3, 4.0, '배추 상태도 좋고 신선해요.', 'https://example.com/review4.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 3),
(4, 4.8, '국 끓이니 정말 맛있네요!', 'https://example.com/review5.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 4),

-- 제품 3
(5, 5.0, '사과가 달고 아삭해서 만족합니다.', 'https://example.com/review6.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 1),
(6, 4.2, '크기도 좋고 상태도 만족스러워요.', 'https://example.com/review7.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 5),

-- 제품 4
(7, 4.7, '토마토가 정말 달아요!', 'https://example.com/review8.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4, 2),
(8, 4.5, '싱싱하고 맛있습니다. 재구매 예정입니다.', 'https://example.com/review9.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4, 3),

-- 제품 5
(9, 3.8, '생각보다 좀 맵긴 했지만 좋았어요.', 'https://example.com/review10.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 4),
(10, 4.6, '배송도 빠르고 향도 좋아요.', 'https://example.com/review11.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 1),

-- 제품 6
(11, 4.0, '배추가 아삭해서 맛있습니다.', 'https://example.com/review12.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 2),
(12, 4.3, '사이즈가 조금 작았지만 상태는 좋아요.', 'https://example.com/review13.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 5),

-- 제품 7
(13, 4.9, '표고버섯 향이 진하고 식감도 훌륭해요.', 'https://example.com/review14.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 7, 3),
(14, 4.6, '요리에 활용하니 향이 살아납니다.', 'https://example.com/review15.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 7, 4),

-- 제품 8
(15, 5.0, '고구마가 정말 달고 부드러워요!', 'https://example.com/review16.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 8, 1),
(16, 4.7, '에어프라이어에 구워 먹기 딱 좋아요.', 'https://example.com/review17.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 8, 2),

-- 제품 9
(17, 4.4, '토종콩이라 그런지 고소하고 좋습니다.', 'https://example.com/review18.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 5),
(18, 4.8, '포장도 정갈하고 품질도 훌륭해요.', 'https://example.com/review19.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 3);

ALTER TABLE USERS ALTER COLUMN user_id RESTART WITH 100;
ALTER TABLE DIARIES ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE FARMERS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE FARM_PROJECTS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE ORDERS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE ORDER_ITEMS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE PAYMENTS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE PRODUCTS ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE REVIEWS ALTER COLUMN ID RESTART WITH 100;

