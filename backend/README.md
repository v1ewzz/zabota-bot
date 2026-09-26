# Забота — MAX Bot Backend

Backend для чат-бота социальной поддержки, реализованного для платформы MAX.

Приложение предоставляет REST API, работает с PostgreSQL и содержит бизнес-логику для хранения профиля пользователя и подбора подходящих мер социальной поддержки.

## Технологический стек

* Java 21
* Spring Boot 4.1.1
* Maven
* Spring Web
* Spring Data JPA
* Hibernate
* PostgreSQL
* Flyway
* Docker
* MAX Bot API

Точные версии зависимостей Spring, PostgreSQL Driver, Flyway и остальных библиотек указаны в `pom.xml`.

## Требования

Для запуска проекта необходимы:

* JDK 21
* Maven
* Docker
* запущенный контейнер PostgreSQL
* доступ к MAX Bot API для работы бота

Проверить Java:

```
java -version
```

Проверить Maven:

```
mvn -version
```

Проверить Docker:

```
docker --version
```

## База данных

Проект использует PostgreSQL.

Рабочая база:

```
zabota_bot_db
```

Подключение приложения:

```
jdbc:postgresql://localhost:5432/zabota_bot_db
```

Пользователь базы данных и пароль задаются в конфигурации приложения и не должны добавляться в Git в открытом виде.

PostgreSQL запускается в Docker.

Пример проверки контейнера:

```
docker ps
```

## Миграции базы данных

Структура базы данных управляется Flyway.

Миграции находятся в:

```
src/main/resources/db/migration/
```

Текущая структура создаётся последовательностью:

```
V1__create_region.sql
V2__create_dictionary_type.sql
V3__create_dictionary_value.sql
V4__create_municipality.sql
V5__create_user.sql
V6__create_user_child.sql
V7__create_npa.sql
V8__create_support_measure.sql
V9__create_support_rule.sql
V10__create_support_npa.sql
V11__create_support_municipality.sql
V12__create_user_support.sql
V13__seed_dictionary_data.sql
V14__seed_tatarstan_region.sql
V15__seed_federal_npa.sql
V16__seed_support_measures.sql
V17__seed_support_rules.sql
V18__seed_support_npa.sql
V19__complete_user_profile.sql
V20__persist_matched_amount.sql
```

Flyway автоматически выполняет новые миграции при запуске Spring Boot.

Не следует изменять уже применённые миграции. Для изменения существующей схемы необходимо создавать новую версию миграции.

## Запуск проекта

1. Запустить PostgreSQL:

   docker start postgres

2. Создать локальный файл `.env` рядом с `pom.xml` по примеру `.env.example` и указать реальный пароль PostgreSQL. Файл `.env` добавлен в `.gitignore`.

3. Проверить наличие базы `zabota_bot_db`.

4. Запустить Spring Boot:

   mvn spring-boot:run

Также приложение можно запускать из IntelliJ IDEA через `ZabotaBotApplication`.

При успешном запуске приложение доступно на:

```
http://localhost:8080
```

### PowerShell

Для разового запуска без `.env` можно передать пароль переменной окружения:

```powershell
$env:DB_PASSWORD="your_password"
.\mvnw.cmd spring-boot:run
```

## Конфигурация

Основная конфигурация находится в:

```
src/main/resources/application.properties
```

Подключение к PostgreSQL настраивается через переменные `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, а порт — через `SERVER_PORT`. Локальный `.env` подключается автоматически через `spring.config.import`.

Секретные значения, такие как пароль базы данных и токен бота, не должны храниться в Git.

Для командной разработки используется `.env`, который добавлен в `.gitignore`.

## Архитектура

Backend построен по классической многослойной архитектуре:

```
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
PostgreSQL
```

Основные пакеты:

```
ru.zabota.bot
├── controller
├── dto
├── entity
├── repository
├── service
├── mapper
├── exception
├── max
└── config
```

### Entity

JPA Entity соответствуют таблицам PostgreSQL.

Основные сущности:

* Region
* Municipality
* DictionaryType
* DictionaryValue
* User
* UserChild
* Npa
* SupportMeasure
* SupportRule
* SupportNpa
* SupportMunicipality
* UserSupport

### Repository

Используется Spring Data JPA.

На базовом уровне репозитории наследуются от `JpaRepository`.

Отдельные сложные запросы добавляются только там, где они необходимы бизнес-логике.

### DTO

DTO используются для API-контрактов и не передаются напрямую между клиентом и Entity.

Для DTO используются отдельные Request и Response модели.

### Mapper

Mapper отвечает за преобразование:

```
Entity ↔ DTO
```

### Exception

Ошибки приложения обрабатываются централизованно через:

```
GlobalExceptionHandler
```

Используются типовые HTTP-статусы:

```
400 Bad Request
404 Not Found
409 Conflict
422 Unprocessable Entity
500 Internal Server Error
```

## API личного кабинета

Основные endpoint-ы сквозного сценария:

```
POST  /api/users
GET   /api/users/{userId}
GET   /api/users/{userId}/profile
PUT   /api/users/{userId}
DELETE /api/users/{userId}

POST  /api/users/{userId}/supports/search
GET   /api/users/{userId}/supports
GET   /api/users/{userId}/supports/{userSupportId}
PATCH /api/users/{userId}/supports/{userSupportId}

POST  /api/supports/search

GET   /api/dictionaries/by-code/{code}
GET   /api/dictionaries/by-code/{code}/values
```

`/api/users/{userId}/supports/search` — основной endpoint персонального подбора. Он не принимает повторно анкету: данные берутся из сохранённых `user` и `user_child`, после чего результат синхронизируется с `user_support`.

`/api/users/{userId}/profile` возвращает сохранённые данные пользователя, детей и персональные меры поддержки.

`PATCH /api/users/{userId}/supports/{userSupportId}` позволяет изменить `statusId`, `selectedForAction` и `note`. При переходе в `SUBMITTED`, `APPROVED` или `RECEIVED` соответствующая дата фиксируется автоматически.

## MAX

MAX является внешним интерфейсом приложения.

Архитектурно интеграция с MAX отделена от основной бизнес-логики:

```
MAX
  ↓
MAX integration
  ↓
Business Service
  ↓
Repository
  ↓
PostgreSQL
```

Сценарий взаимодействия пользователя с ботом не должен содержать непосредственную работу с JPA Entity или PostgreSQL.

MAX отвечает за взаимодействие с пользователем, а backend — за хранение данных и выполнение бизнес-логики.

## Основной функциональный поток

Пользователь взаимодействует с ботом в MAX.

Backend получает параметры пользователя и формирует его профиль.

После этого выполняется подбор мер социальной поддержки на основе:

* региона;
* муниципального образования;
* семейного положения;
* военного статуса;
* наличия детей;
* инвалидности;
* трудового статуса;
* категории дохода;
* других параметров профиля.

Подбор выполняется на основании правил, хранящихся в `support_rule`, и связанных мер социальной поддержки.

## Сквозной пользовательский сценарий

Backend поддерживает полный сценарий личного кабинета:

```
POST /api/users
    ↓
user + user_child
    ↓
POST /api/users/{userId}/supports/search
    ↓
SupportMatchingService
    ↓
user_support
    ↓
GET /api/users/{userId}/profile
GET /api/users/{userId}/supports
    ↓
PATCH /api/users/{userId}/supports/{userSupportId}
```

`POST /api/supports/search` сохраняется как stateless API для прямого запуска rules engine. Новый endpoint под пользователем запускает тот же алгоритм уже на данных, сохранённых в PostgreSQL, и фиксирует персональный результат в `user_support`.

При повторном подборе существующие статусы, выбор меры и заметки не сбрасываются. Для новых результатов устанавливается `NOT_APPLIED`. Устаревшие результаты удаляются только если они ещё не были оформлены. Фактически подобранная сумма сохраняется в `user_support.matched_amount`, поэтому результат не зависит от последующего изменения базовой суммы меры.

## Текущее состояние проекта

Реализованы:

* Spring Boot и PostgreSQL;
* Flyway и воспроизводимая начальная БД;
* единый слой справочников;
* JPA Entity, Repository, Mapper и Service;
* CRUD регионов, муниципалитетов, НПА, мер, правил и пользователей;
* rules engine персонального подбора;
* сохранение ФИО пользователя и детей;
* сохранение персональных результатов в `user_support`;
* API личного кабинета и изменения статуса меры;
* глобальная обработка ошибок;
* Swagger / OpenAPI;
* unit-тесты существующих сервисов и контроллеров, а также тесты нового сценария личного кабинета.

Следующий слой — подключение frontend к новым `/api/users/.../supports` endpoint-ам, после чего можно завершать интеграцию с MAX.

## Важные правила разработки

Не изменять уже применённые Flyway-миграции.

Не хранить секреты в Git.

Не использовать Entity напрямую в REST API.

Не помещать бизнес-логику в Controller.

Не обращаться к Repository напрямую из Controller.

Интеграцию MAX держать отдельно от основной бизнес-логики.

Точные зависимости и их версии определяются `pom.xml`.
