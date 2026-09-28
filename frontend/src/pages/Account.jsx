import { useMemo, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import {
  DICT,
  LABELS,
  MUNICIPALITIES,
  REGIONS,
} from '../api/mockData'
import {
  EmptyState,
  Money,
  StatusBadge,
  StatusSwitch,
} from '../components/ui'
import {
  calcAge,
  plural,
} from '../utils'

const EDUCATION_LABELS = {
  PRESCHOOL: 'Детский сад',
  SCHOOL: 'Школа',
  COLLEGE: 'Колледж',
  SPO: 'Колледж / СПО',
  UNIVERSITY: 'Вуз',
  BACHELOR: 'Бакалавриат',
  SPECIALIST: 'Специалитет',
  NONE: 'Отсутствует',
}

const STATUSES = [
  'NOT_APPLIED',
  'SUBMITTED',
  'APPROVED',
  'RECEIVED',
]

function dictLabel(list, code) {
  return code
    ? (
      list.find(
        (item) => item.code === code
      )?.label || code
    )
    : null
}

export default function Account() {
  const {
    profile,
    results,
    support,
    updateSupport,
    reset,
  } = useApp()

  const navigate = useNavigate()

  const [filter, setFilter] =
    useState('ALL')

  const [detailsOpen, setDetailsOpen] =
    useState(true)

  const [deleteConfirmOpen, setDeleteConfirmOpen] =
    useState(false)

  const items = useMemo(
    () =>
      (results?.matched || []).filter(
        (measure) =>
          support[measure.id]
      ),
    [results, support]
  )

  const counts = useMemo(() => {
    const next = {
      ALL: items.length,
    }

    for (const status of STATUSES) {
      next[status] = items.filter(
        (item) =>
          support[item.id]?.status === status
      ).length
    }

    return next
  }, [items, support])

  const list =
    filter === 'ALL'
      ? items
      : items.filter(
        (item) =>
          support[item.id]?.status === filter
      )

  const fullName =
    profile?.fullName || 'Профиль'

  const initials =
    fullName
      .split(/\s+/)
      .filter(Boolean)
      .slice(0, 2)
      .map(
        (part) =>
          part[0]?.toUpperCase()
      )
      .join('') || 'З'

  const regionName =
    REGIONS.find(
      (region) =>
        region.id === profile?.region
    )?.name

  const cityName =
    MUNICIPALITIES[
      profile?.region
    ]?.find(
      (municipality) =>
        municipality.id === profile?.municipality
    )?.name

  const children =
    profile?.children || []

  const clearProfile = () => {
    reset()
    setDeleteConfirmOpen(false)
    navigate('/')
  }

  return (
    <div className="page account-page">
      <section className="profile-card">
        <div className="profile-card__top">
          <div className="profile-card__avatar">
            {initials}
          </div>

          <div>
            <div className="profile-card__eyebrow">
              Профиль
            </div>

            <h1>
              {fullName}
            </h1>
          </div>
        </div>

        <div className="profile-card__role">
          {dictLabel(
            DICT.FAMILY_RELATION,
            profile?.familyRelation
          ) || 'Роль ещё не выбрана'}
        </div>

        <div className="profile-stats">
          <div>
            <strong>
              {counts.ALL}
            </strong>

            <span>
              мер найдено
            </span>
          </div>

          <div>
            <strong>
              {counts.SUBMITTED + counts.APPROVED}
            </strong>

            <span>
              в работе
            </span>
          </div>

          <div>
            <strong>
              {counts.RECEIVED}
            </strong>

            <span>
              получено
            </span>
          </div>
        </div>
      </section>

      <div className="action-grid">
        <Link
          className="btn btn--ghost"
          to="/questionnaire"
        >
          Посмотреть анкету
        </Link>

        <Link
          className="btn btn--ghost"
          to="/survey?reset=1"
        >
          Заполнить анкету заново
        </Link>

        <Link
          className="btn btn--ghost"
          to="/print"
        >
          Сводка
        </Link>
      </div>

      <section className="card pdetails">
        <button
          className="pdetails__head"
          type="button"
          onClick={() =>
            setDetailsOpen(
              (value) => !value
            )
          }
          aria-expanded={
            detailsOpen
          }
        >
          <span className="pdetails__title">
            Параметры подбора
          </span>

          <span
            className={
              `pdetails__chev${
                detailsOpen
                  ? ' up'
                  : ''
              }`
            }
          >
            ⌄
          </span>
        </button>

        {detailsOpen && (
          <div className="stack">
            <div className="ptiles">
              <div className="ptile">
                <span>
                  Проживание
                </span>

                <b>
                  {
                    [
                      cityName,
                      regionName,
                    ]
                      .filter(Boolean)
                      .join(', ')
                    || '—'
                  }
                </b>
              </div>

              <div className="ptile">
                <span>
                  Статус
                </span>

                <b>
                  {
                    dictLabel(
                      DICT.MILITARY_STATUS,
                      profile?.militaryStatus
                    ) || '—'
                  }
                </b>
              </div>

              <div className="ptile">
                <span>
                  Доход семьи
                </span>

                <b>
                  {
                    DICT.INCOME_RANGE.find(
                      (item) =>
                        item.code ===
                        profile?.incomeRange
                    )?.label
                    || 'Не указан'
                  }
                </b>
              </div>

              <div className="ptile">
                <span>
                  Занятость
                </span>

                <b>
                  {
                    dictLabel(
                      DICT.EMPLOYMENT_STATUS,
                      profile?.employmentStatus
                    ) || 'Не указана'
                  }
                </b>
              </div>
            </div>

            <div className="pflags">
              {[
                ['injury', 'Ранение'],
                ['disability', 'Инвалидность'],
                ['housingProblem', 'Жилищный вопрос'],
                ['gasificationNeeded', 'Газификация'],
                ['pregnancy', 'Беременность'],
              ]
                .filter(
                  ([key]) =>
                    profile?.[key]
                )
                .map(
                  ([, label]) => (
                    <span
                      className="chip chip--flag"
                      key={label}
                    >
                      {label}
                    </span>
                  )
                )}
            </div>

            <div className="pchild__wrap">
              <div className="pdetails__title">
                Дети · {children.length}{' '}
                {
                  plural(
                    children.length,
                    [
                      'ребёнок',
                      'ребёнка',
                      'детей',
                    ]
                  )
                }
              </div>

              {children.length === 0 ? (
                <p className="p muted">
                  Не указаны
                </p>
              ) : (
                <div className="stack">
                  {children.map(
                    (
                      child,
                      index
                    ) => {
                      const age =
                        calcAge(
                          child.birthDate
                        )

                      return (
                        <div
                          className="pchild"
                          key={
                            child.id
                            || `${child.birthDate}-${index}`
                          }
                        >
                          <div className="pchild__ava">
                            {index + 1}
                          </div>

                          <div>
                            <div className="pchild__name">
                              {
                                age == null
                                  ? 'Возраст не указан'
                                  : `${age} ${
                                    plural(
                                      age,
                                      [
                                        'год',
                                        'года',
                                        'лет',
                                      ]
                                    )
                                  }`
                              }
                            </div>

                            <div className="pchild__meta">
                              {
                                EDUCATION_LABELS[child.educationType]
                                || EDUCATION_LABELS[child.educationLevel]
                                || child.educationType
                                || child.educationLevel
                                || 'Обучение не указано'
                              }

                              {child.grade
                                ? ` · ${child.grade} класс`
                                : ''}
                            </div>
                          </div>

                          {child.disability && (
                            <span className="chip">
                              инвалидность
                            </span>
                          )}
                        </div>
                      )
                    }
                  )}
                </div>
              )}
            </div>
          </div>
        )}
      </section>

      <div className="sec__title">
        Статусы мер
      </div>

      <div
        className="tabs"
        role="tablist"
        aria-label="Статус меры"
      >
        {[
          'ALL',
          ...STATUSES,
        ].map(
          (status) => (
            <button
              key={status}
              type="button"
              className={
                `tab${
                  filter === status
                    ? ' tab--on'
                    : ''
                }`
              }
              onClick={() =>
                setFilter(status)
              }
              aria-pressed={
                filter === status
              }
            >
              {status === 'ALL'
                ? `Все · ${counts.ALL}`
                : `${LABELS.statusShort[status]} · ${counts[status]}`}
            </button>
          )
        )}
      </div>

      {list.length > 0 ? (
        <div className="stack">
          {list.map(
            (measure) => {
              const state =
                support[measure.id]

              return (
                <article
                  key={measure.id}
                  className="card account-measure"
                  tabIndex="0"
                  role="link"
                  onClick={() =>
                    navigate(
                      `/measure/${measure.id}`
                    )
                  }
                  onKeyDown={
                    (event) => {
                      if (
                        event.key === 'Enter'
                        || event.key === ' '
                      ) {
                        event.preventDefault()
                        navigate(
                          `/measure/${measure.id}`
                        )
                      }
                    }
                  }
                >
                  <div className="mcard__top">
                    <span className="chip chip--blue">
                      {
                        LABELS.recipient[
                          measure.recipient
                        ] || 'Семье'
                      }
                    </span>

                    {state.reminder && (
                      <span className="chip">
                        Напоминание
                      </span>
                    )}

                    <StatusBadge
                      status={
                        state.status
                      }
                    />
                  </div>

                  <h3 className="mcard__title">
                    {measure.name}
                  </h3>

                  <Money
                    amount={
                      measure.amount
                    }
                    frequency={
                      measure.frequency
                    }
                  />

                  <div
                    onClick={(event) =>
                      event.stopPropagation()
                    }
                  >
                    <StatusSwitch
                      value={
                        state.status
                      }
                      onChange={(value) =>
                        updateSupport(
                          measure.id,
                          {
                            status: value,
                          }
                        )
                      }
                    />
                  </div>
                </article>
              )
            }
          )}
        </div>
      ) : (
        <EmptyState
          title="В этой категории пока нет мер"
          text={
            results?.matched?.length
              ? 'Выберите другой статус или откройте все меры.'
              : 'Сначала заполните анкету и выполните подбор.'
          }
          action={
            <Link
              className="btn btn--primary"
              to="/survey?reset=1"
            >
              Заполнить анкету
            </Link>
          }
        />
      )}

      <button
        className="btn btn--ghost btn--lg"
        type="button"
        onClick={() =>
          navigate('/')
        }
      >
        Выйти из профиля
      </button>

      <button
        className="btn btn--danger btn--lg"
        type="button"
        onClick={() =>
          setDeleteConfirmOpen(true)
        }
      >
        Удалить локальный профиль
      </button>

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
              результаты подбора и статусы мер на этом устройстве.
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