#!/bin/bash
set -e

CONFIG_DIR="/app/config"
CONFIG_FILE="$CONFIG_DIR/application.yml"
PROD_CONFIG_FILE="$CONFIG_DIR/application-production.yml"

mkdir -p "$CONFIG_DIR"

echo "📄 Checking for application.yml and setting spring.profiles.active..."

# spring.profiles.active 설정 추가 또는 수정
if [ ! -f "$CONFIG_FILE" ]; then
  echo "spring.profiles.active: production" > "$CONFIG_FILE"
elif grep -q "^spring.profiles.active" "$CONFIG_FILE"; then
  sed -i 's/^spring.profiles.active.*/spring.profiles.active: production/' "$CONFIG_FILE"
else
  echo "spring.profiles.active: production" >> "$CONFIG_FILE"
fi

echo "✅ spring.profiles.active=production 설정 완료"

# production 환경 설정 파일 생성
if [ -n "$DB_URL" ] && [ -n "$SERVER_PORT" ] && [ -n "$DB_DIALECT" ] && [ -n "$DB_DRIVER" ] && [ -n "$DB_USERNAME" ] && [ -n "$DB_PASSWORD" ]; then
  echo "⚙️ Generating application-production.yml with DB configuration..."
  cat > "$PROD_CONFIG_FILE" <<EOF
server:
  port: ${SERVER_PORT}

spring:
  datasource:
    url: ${DB_URL}
    driver-class-name: ${DB_DRIVER}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    properties:
      hibernate:
        dialect: ${DB_DIALECT}

server.forward-headers-strategy: native
EOF
  echo "✅ application-production.yml created"
else
  echo "⚠️ Environment variables not set. Skipping production config generation."
fi

echo "▶️ Starting Spring Boot application..."
exec java -jar app.jar --spring.config.additional-location=$CONFIG_DIR/