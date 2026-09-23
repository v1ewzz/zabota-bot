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
```

Flyway автоматически выполняет новые миграции при запуске Spring Boot.

Не следует изменять уже применённые миграции. Для изменения существующей схемы необходимо создавать новую версию миграции.

## Запуск проекта

1. Запустить PostgreSQL:

   docker start postgres

2. Проверить наличие базы `zabota_bot_db`.

3. Проверить настройки подключения в `application.properties`.

4. Запустить Spring Boot:

   mvn spring-boot:run

Также приложение можно запускать из IntelliJ IDEA через `ZabotaBotApplication`.

При успешном запуске приложение доступно на:

```
http://localhost:8080
```

## Конфигурация

Основная конфигурация находится в:

```
src/main/resources/application.properties
```

Конфигурация должна содержать параметры подключения к PostgreSQL и настройки MAX Bot API.

Секретные значения, такие как токен бота и пароль базы данных, не должны храниться в Git.

Для командной разработки рекомендуется использовать переменные окружения или локальный конфигурационный файл, который добавлен в `.gitignore`.

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

## Текущее состояние проекта

На текущем этапе реализованы:

* Spring Boot проект;
* PostgreSQL;
* подключение к базе данных;
* Flyway;
* структура базы данных;
* 12 SQL-миграций;
* JPA Entity;
* Spring Data JPA Repository;
* глобальная обработка ошибок;
* DTO слой.

Следующие этапы:

* Mapper;
* Service;
* REST Controller;
* Swagger / OpenAPI;
* интеграция с MAX;
* сценарии взаимодействия бота;
* алгоритм подбора мер социальной поддержки;
* заполнение справочников и нормативной базы;
* тестирование.

## Важные правила разработки

Не изменять уже применённые Flyway-миграции.

Не хранить секреты в Git.

Не использовать Entity напрямую в REST API.

Не помещать бизнес-логику в Controller.

Не обращаться к Repository напрямую из Controller.

Интеграцию MAX держать отдельно от основной бизнес-логики.

Точные зависимости и их версии определяются `pom.xml`.
