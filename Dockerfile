# ===== STAGE 1: Build Frontend =====
FROM node:20-alpine AS frontend-builder

WORKDIR /app/frontend

COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci

COPY frontend/ ./
RUN npm run build

# ===== STAGE 2: Build Backend =====
FROM maven:3.9-eclipse-temurin-21 AS backend-builder

WORKDIR /app/backend

COPY backend/pom.xml backend/mvnw ./
COPY backend/.mvn ./.mvn
RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline -B || true

COPY backend/src ./src
RUN ./mvnw clean package -DskipTests -B

# ===== STAGE 3: Runtime =====
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN apk add --no-cache nginx

COPY --from=backend-builder /app/backend/target/*.jar /app/backend/app.jar
COPY --from=frontend-builder /app/frontend/dist /usr/share/nginx/html

# Upstream жёстко зашит в самом шаблоне (http://127.0.0.1:8080, а не localhost:
# в alpine localhost резолвится в ::1 первым). Подстановки через envsubst нет
# намеренно — файл копируется как есть, ${API_UPSTREAM} в нём не
# используется. Amvera не передаёт переменные окружения на этапе сборки;
# SERVER_PORT в amvera.yaml не выставлять — дефолт 8080 совпадает.
COPY frontend/nginx.conf.template /etc/nginx/nginx.conf
EXPOSE 80

CMD ["sh", "-c", "nginx -t && nginx && exec java -XX:MaxRAMPercentage=70 -XX:+ExitOnOutOfMemoryError -jar /app/backend/app.jar"]
