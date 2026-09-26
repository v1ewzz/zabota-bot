# ===== STAGE 1: Build Frontend =====
FROM node:20-alpine AS frontend-builder

WORKDIR /app/frontend

COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci --only=production

COPY frontend/ .
RUN npm run build

# ===== STAGE 2: Build Backend =====
FROM maven:3.9-eclipse-temurin-17 AS backend-builder

WORKDIR /app/backend

COPY backend/pom.xml backend/mvnw ./
COPY backend/.mvn ./.mvn
RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline -B || true

COPY backend/ .
RUN ./mvnw clean package -DskipTests -B

# ===== STAGE 3: Runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install runtime deps
RUN apk add --no-cache nginx

# Copy backend JAR
COPY --from=backend-builder /app/backend/target/*.jar /app/backend/app.jar

# Copy frontend build + nginx config
COPY --from=frontend-builder /app/frontend/dist /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/nginx.conf

# Expose ports
EXPOSE 80 8080

# Start both services
CMD ["sh", "-c", "nginx && java -jar /app/backend/app.jar"]