# Money Talks Project — Gateway

Единственная внешняя точка входа backend-системы **Money Talks Project**.
Сервис принимает клиентские запросы, проверяет JWT, проксирует запросы в
доменные микросервисы и проставляет доверенный заголовок `X-User-ID`.

## Ответственность

- маршрутизация `/categories/**`, `/accounts/**`, `/users/**`, `/transactions/**`
  в соответствующие сервисы;
- регистрация (`POST /auth/register`) и вход (`POST /auth/login`);
- выпуск и проверка JWT (HS256);
- передача идентификатора пользователя во внутренние сервисы;
- изоляция внутренней топологии: наружу опубликованы только явно объявленные
  маршруты.

Бизнес-данных gateway не хранит: собственной БД у сервиса нет. Пользователи
хранятся в `mtp-backend-ms-user`, счета — в `mtp-backend-ms-account`,
категории — в `mtp-backend-ms-cathegories`, транзакции — в
`mtp-backend-ms-transaction`.

## Стек

- Java 21, Spring Boot 4.0.1, Spring Cloud 2025.1.3;
- Spring Cloud Gateway Server WebMVC (функциональные маршруты);
- Spring Security + JJWT 0.12.3;
- OpenFeign + OkHttp, Resilience4j CircuitBreaker;
- PostgreSQL-драйвер (подключение к БД не используется);
- Maven, Spring Boot Test.

## Маршруты

Маршруты описаны в `GatewayConfig`: gateway принимает GET и POST по префиксам и
передаёт запрос в целевой сервис, добавляя `X-User-ID` из SecurityContext.

| Публичный путь | Целевой сервис | Переменная адреса | Порт по умолчанию |
|---|---|---|---|
| `/categories/**` | `mtp-backend-ms-cathegories` | `CATEGORY_SERVICE_URI` | 8081 |
| `/accounts/**` | `mtp-backend-ms-account` | `ACCOUNT_SERVICE_URI` | 8082 |
| `/users/**` | `mtp-backend-ms-user` | `ACCOUNT_SERVICE_URI`* | 8083 |
| `/transactions/**` | `mtp-backend-ms-transaction` | `TRANSACTION_SERVICE_URI` | 8084 |

\* В текущем `application.yml` адрес пользовательского сервиса и Feign-клиента
`users-client` читается из `ACCOUNT_SERVICE_URI`. При развёртывании это нужно
учитывать; значения по умолчанию рассчитаны на локальный запуск.

## Аутентификация

| Метод | Путь | Назначение |
|---|---|---|
| `POST` | `/auth/register` | Создать пользователя и, при `SECURITY_AUTO_LOGIN=true`, сразу вернуть JWT |
| `POST` | `/auth/login` | Проверить логин и пароль, вернуть JWT |

Порядок работы:

1. Gateway принимает `{login, password}`.
2. Пароль хэшируется BCrypt и передаётся в `mtp-backend-ms-user`
   (`POST /users/new`); сам пароль в открытом виде дальше не уходит.
3. При входе gateway загружает пользователя через Feign (`GET /users/by-login`)
   и проверяет пароль через `AuthenticationManager`.
4. JWT подписывается общим секретом (`JWT_SECRET`) и содержит `sub`/`uid` с
   идентификатором пользователя и `exp`.
5. На защищённых маршрутах `JwtRequestFilter` проверяет токен, загружает
   пользователя по ID и помещает его ID в `SecurityContext` и MDC.
6. `GatewayConfig` добавляет `X-User-ID` в запрос к целевому сервису.

Все маршруты, кроме `/auth/**`, требуют аутентификации (`FilterChainConfig`).
Сессий нет: `SessionCreationPolicy.STATELESS`, CSRF и CORS отключены.

## Конфигурация

| Переменная | Назначение | По умолчанию |
|---|---|---|
| `CATEGORY_SERVICE_URI` | Адрес сервиса категорий | `http://localhost:8081` |
| `ACCOUNT_SERVICE_URI` | Адрес сервиса счетов (и пользователей, см. примечание выше) | `http://localhost:8082` |
| `TRANSACTION_SERVICE_URI` | Адрес сервиса транзакций | `http://localhost:8084` |
| `SECURITY_AUTO_LOGIN` | Выдавать JWT сразу после регистрации | `true` |
| `JWT_SECRET` | Секрет подписи JWT; в коде есть только dev-значение | обязателен для окружений |
| `JWT_ACCESS_LIFETIME` | Время жизни токена | `1d` |
| `LOGGING_LEVEL` | Уровень логирования пакета `b4e.ru.mtp.backend` | `debug` |

Локальные значения `JWT_SECRET` и пароля БД в `application.yml` предназначены
только для разработки. В stage/prod секреты передаются через окружение или
секретное хранилище.

## Запуск

Требуется JDK 21. Родительский POM и `mtp-backend-commons` должны быть
установлены в локальный Maven-репозиторий.

```bash
cd ../mtp-backend-root && ./mvnw install
cd ../mtp-backend-commons && ./mvnw install
./mvnw spring-boot:run
```

Проверка:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"login":"user","password":"password"}'

curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"user","password":"password"}'
```

Для работы маршрутов должны быть запущены целевые сервисы и PostgreSQL.

Тесты:

```bash
./mvnw test
```

## Связанные репозитории

- [Money-Talks-Project](https://github.com/Sun1tAr/Money-Talks-Project) — обзор, архитектура и сценарии;
- [mtp-backend-root](https://github.com/Sun1tAr/mtp-backend-root);
- [mtp-backend-commons](https://github.com/Sun1tAr/mtp-backend-commons);
- [mtp-backend-ms-user](https://github.com/Sun1tAr/mtp-backend-ms-user);
- [mtp-backend-ms-account](https://github.com/Sun1tAr/mtp-backend-ms-account);
- [mtp-backend-ms-cathegories](https://github.com/Sun1tAr/mtp-backend-ms-cathegories);
- [mtp-backend-ms-transaction](https://github.com/Sun1tAr/mtp-backend-ms-transaction).
