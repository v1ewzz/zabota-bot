# «Забота» — MAX Mini App frontend

React + Vite frontend для мини-приложения «Забота».

Приложение рассчитано на запуск:

- внутри MAX как Mini App;
- локально в обычном браузере для разработки;
- в production через Docker + Nginx.

## MAX Bridge

MAX Bridge подключается в `index.html` через официальный CDN:

`https://st.max.ru/js/max-web-app.js`

После загрузки приложение работает с `window.WebApp` через `src/bridge/max.js`.

Используются:

- определение режима MAX;
- `initData` для передачи технических данных запуска на backend через `X-MAX-INIT-DATA`;
- платформа, версия и устройство;
- кнопка «Назад» MAX;
- открытие официальных ссылок через MAX Bridge;
- haptic feedback.

`initDataUnsafe` не используется для доверенной идентификации пользователя. По документации MAX данные `initData` предназначены для серверной валидации, а `initDataUnsafe` нельзя использовать для проверки подлинности.

## Профиль пользователя

ФИО не берётся автоматически из MAX-аккаунта. Пользователь самостоятельно вводит имя и фамилию в приложении.

`initData` используется только как технический контекст запуска Mini App. Сам backend-профиль создаётся через `/api/users`.

## Backend contract

Frontend работает с текущим Spring Boot backend:

`POST /api/users`

создание профиля.

`PUT /api/users/{userId}`

обновление профиля.

`POST /api/users/{userId}/supports/search`

повторный подбор по сохранённому профилю.

`GET /api/users/{userId}/supports`

получение сохранённых персональных мер.

`PATCH /api/users/{userId}/supports/{userSupportId}`

изменение статуса персональной меры.

## Локальный запуск

```powershell
npm ci
npm run dev
```

Локальный `.env`:

```text
VITE_API_BASE_URL=http://localhost:8080
VITE_USE_MOCK=false
```

## Production build

```powershell
npm ci
npm run build
```

Результат будет в `dist/`.

## Docker

Сборка:

```powershell
docker build -t zabota-front .
```

Запуск вместе с локальным backend:

```powershell
docker run --rm -p 8081:80 -e API_UPSTREAM=http://host.docker.internal:8080 zabota-front
```

Открыть:

`http://localhost:8081`

В Docker frontend использует относительный путь `/api`, а Nginx проксирует запросы на значение `API_UPSTREAM`. Это позволяет в production обслуживать Mini App и API с одного HTTPS-источника и не требовать CORS между ними.

## Production host

Для MAX Mini App приложение должно быть размещено на публичном HTTPS-адресе. MAX указывает, что перед подключением Mini App необходимо загрузить HTML/CSS/JS и необходимые файлы на хостинг и убедиться, что приложение работает по HTTPS.

После размещения нужно указать URL Mini App в настройках бота MAX.

Если frontend и backend находятся на одном HTTPS-домене через reverse proxy, оставьте `VITE_API_BASE_URL` пустым.

Если frontend размещается отдельно, установите на этапе сборки:

```text
VITE_API_BASE_URL=https://api.example.ru
VITE_USE_MOCK=false
```

В этом случае backend должен разрешать запросы с origin frontend или должен быть настроен reverse proxy на стороне хостинга.

## Mock mode

Для автономной демонстрации без backend:

```text
VITE_API_BASE_URL=
VITE_USE_MOCK=true
```

Основной режим проекта — `VITE_USE_MOCK=false`.
