# Забота: Личный Кабинет — фронт мини-приложения MAX

Мини-приложение для семей участников СВО: короткий опрос → персональный подбор мер
поддержки (rules-engine) → карточки мер с документами и основаниями → личный кабинет
со статусами оформления (NOT_APPLIED / SUBMITTED / APPROVED / RECEIVED) → PDF-сводка → обращение в МФЦ.

## Запуск (демо-режим, без бэкенда)

    npm install
    npm run dev          # http://localhost:5173

В демо-режиме (VITE_USE_MOCK=true) работает встроенный каталог мер и клиентский
rules-engine (AND внутри condition_group, OR между группами).

## Переменные окружения

| Переменная         | Значение                                   |
|--------------------|--------------------------------------------|
| VITE_API_BASE_URL  | Базовый URL бэкенда (пусто — не нужен)     |
| VITE_USE_MOCK      | true — моки; false — ходить в бэкенд       |

## Контракт API бэкенда

POST /api/match — тело = анкета (familyRelation, militaryStatus, region,
municipality, children[{birthDate, educationLevel, grade, disability, disabilityGroup, fullTime}],
injury, disability, disabilityGroup, housingProblem, gasificationNeeded, pregnancy,
incomeCategory, employmentStatus).
Ответ: { matched: Measure[], skipped: [{ measureId, name, missing[] }] }.

Measure: { id, name, description, supportType, level, recipient, amount, frequency,
applicationRequired, channel, actionUrl, documents[], validTo, npa[{name,url}], urgency, reason }.

POST /api/mfc-request — { measureId, measureName, name, phone, comment } → { ok, requestId }.

## Docker

    docker build -t zabota-front .
    docker run -p 8080:80 zabota-front

## Ограничения

- Демо-данные каталога тестовые; суммы/условия сверять с первоисточниками.
- Сервис не заменяет официальную проверку права на меры (решение принимает ведомство).
- Напоминания и генерация PDF на сервере — этап после MVP.
