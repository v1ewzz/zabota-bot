import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'

function Icon({ type }) {
  const paths = {
    search: (
      <>
        <circle
          cx="10.5"
          cy="10.5"
          r="6.5"
        />
        <path d="m16 16 5 5" />
      </>
    ),

    user: (
      <>
        <circle
          cx="12"
          cy="8"
          r="3.5"
        />
        <path d="M5 20c.8-3.4 3.1-5.2 7-5.2s6.2 1.8 7 5.2" />
      </>
    ),
  }

  return (
    <svg
      viewBox="0 0 24 24"
      aria-hidden="true"
    >
      {paths[type]}
    </svg>
  )
}

export default function Home() {
  const navigate =
    useNavigate()

  const {
    consent,
    setConsent,
    profile,
    saveProfile,
    reset,
  } = useApp()

  const [
    firstName,
    setFirstName,
  ] = useState(
    () => profile?.firstName || ''
  )

  const [
    lastName,
    setLastName,
  ] = useState(
    () => profile?.lastName || ''
  )

  const [
    deleteConfirmOpen,
    setDeleteConfirmOpen,
  ] = useState(false)

  const hasProfile =
    Boolean(
      profile?.firstName
      && profile?.lastName
    )

  const fullName =
    useMemo(
      () =>
        [
          firstName.trim(),
          lastName.trim(),
        ]
          .filter(Boolean)
          .join(' '),
      [
        firstName,
        lastName,
      ]
    )

  const canContinue =
    Boolean(
      firstName.trim()
      && lastName.trim()
      && consent
    )

  const start = () => {
    if (!canContinue) {
      return
    }

    saveProfile({
      firstName:
        firstName.trim(),

      lastName:
        lastName.trim(),

      fullName,
    })

    navigate('/survey')
  }

  const clearProfile = () => {
    reset()
    setFirstName('')
    setLastName('')
    setDeleteConfirmOpen(false)
  }

  return (
    <div className="page home">
      <section className="hero">
        <div className="brand-mark">
          З
        </div>

        <div className="eyebrow">
          Мини-приложение для MAX
        </div>

        <h1>
          Забота
        </h1>

        <p>
          Подберём меры поддержки
          по вашей ситуации и покажем,
          что делать дальше.
        </p>
      </section>

      <section
        className="card identity-card"
        aria-labelledby="identity-title"
      >
        <div className="identity-card__head">
          <span className="access-card__icon">
            <Icon type="user" />
          </span>

          <div>
            <strong id="identity-title">
              {
                hasProfile
                  ? 'Ваш профиль'
                  : 'Создайте профиль'
              }
            </strong>

            <span>
              Укажите только имя и фамилию.
              Эти данные используются внутри приложения
              и сохраняются локально.
            </span>
          </div>
        </div>

        <div className="identity-form">
          <label className="field">
            <span>
              Имя
            </span>

            <input
              className="input"
              value={firstName}
              onChange={(event) =>
                setFirstName(
                  event.target.value
                )
              }
              placeholder="Иван"
              autoComplete="given-name"
              maxLength={60}
            />
          </label>

          <label className="field">
            <span>
              Фамилия
            </span>

            <input
              className="input"
              value={lastName}
              onChange={(event) =>
                setLastName(
                  event.target.value
                )
              }
              placeholder="Иванов"
              autoComplete="family-name"
              maxLength={80}
            />
          </label>
        </div>
      </section>

      <label className="consent">
        <input
          type="checkbox"
          checked={consent}
          onChange={(event) =>
            setConsent(
              event.target.checked
            )
          }
        />

        <span>
          Я согласен(на) на использование
          указанных данных и ответов анкеты
          для подбора мер поддержки.
        </span>
      </label>

      <button
        className="btn btn--primary btn--lg"
        type="button"
        disabled={!canContinue}
        onClick={start}
      >
        {
          hasProfile
            ? 'Продолжить'
            : 'Создать профиль и продолжить'
        }
      </button>

      {hasProfile && (
        <button
          className="btn btn--ghost btn--lg"
          type="button"
          onClick={() =>
            navigate('/account')
          }
        >
          Открыть профиль
        </button>
      )}

      <section
        className="feature-grid"
        aria-label="Возможности"
      >
        <div className="feature">
          <span className="feature__icon--green">
            <Icon type="search" />
          </span>

          <strong>
            Подбор по ситуации
          </strong>

          <small>
            учитываем статус, регион,
            детей и обстоятельства
          </small>
        </div>

        <div className="feature">
          <span>
            1
          </span>

          <strong>
            Один профиль
          </strong>

          <small>
            имя и фамилия используются
            внутри приложения
          </small>
        </div>

        <div className="feature">
          <span>
            2
          </span>

          <strong>
            Повторный подбор
          </strong>

          <small>
            можно изменить ответы
            и пересчитать результат
          </small>
        </div>
      </section>

      <p className="disclaimer">
        Информация носит справочный характер.
        Перед оформлением проверяйте актуальные
        условия в официальном источнике.
      </p>

      {hasProfile && (
        <button
          className="btn btn--danger"
          type="button"
          onClick={() =>
            setDeleteConfirmOpen(true)
          }
        >
          Удалить локальный профиль
        </button>
      )}

      {deleteConfirmOpen && (
        <div
          className="confirm-overlay"
          onClick={() =>
            setDeleteConfirmOpen(false)
          }
        >
          <div
            className="confirm-card"
            role="dialog"
            aria-modal="true"
            aria-labelledby="delete-profile-title"
            onClick={(event) =>
              event.stopPropagation()
            }
          >
            <div className="confirm-card__icon">
              !
            </div>

            <h3 id="delete-profile-title">
              Удалить локальный профиль?
            </h3>

            <p>
              Будут удалены сохранённые ответы анкеты,
              результаты подбора и статусы мер
              на этом устройстве.
            </p>

            <div className="confirm-card__actions">
              <button
                className="btn btn--ghost"
                type="button"
                onClick={() =>
                  setDeleteConfirmOpen(false)
                }
              >
                Отмена
              </button>

              <button
                className="btn btn--danger"
                type="button"
                onClick={clearProfile}
              >
                Удалить
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}