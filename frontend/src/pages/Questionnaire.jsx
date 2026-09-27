import { Link } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import {
  DICT,
  MUNICIPALITIES,
  REGIONS,
} from '../api/mockData'
import {
  calcAge,
  formatDate,
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

function dictLabel(
  list,
  code,
  fallback = 'Не указано'
) {
  return (
    list?.find(
      (item) =>
        item.code === code
    )?.label
    || (
      code
        ? String(code)
        : fallback
    )
  )
}

function Section({
  title,
  children,
}) {
  return (
    <section className="card pdetails">
      <div className="pdetails__title">
        {title}
      </div>

      <div className="stack">
        {children}
      </div>
    </section>
  )
}

function Tile({
  title,
  value,
  wide = false,
}) {
  return (
    <div
      className={
        `ptile${
          wide
            ? ' questionnaire-wide'
            : ''
        }`
      }
    >
      <span>{title}</span>
      <b>{value || 'Не указано'}</b>
    </div>
  )
}

function BoolTile({
  title,
  value,
}) {
  return (
    <div className="ptile">
      <span>{title}</span>
      <b>
        {value ? 'Да' : 'Нет'}
      </b>
    </div>
  )
}

function educationLabel(child) {
  if (
    child?.educationType
    && EDUCATION_LABELS[
      child.educationType
    ]
  ) {
    return EDUCATION_LABELS[
      child.educationType
    ]
  }

  if (
    child?.educationLevel
    && EDUCATION_LABELS[
      child.educationLevel
    ]
  ) {
    return EDUCATION_LABELS[
      child.educationLevel
    ]
  }

  return dictLabel(
    DICT.EDUCATION_LEVEL,
    child?.educationLevel
  )
}

export default function Questionnaire() {
  const { profile } =
    useApp()

  const regionName =
    REGIONS.find(
      (item) =>
        item.id === profile?.region
    )?.name

  const municipalityName =
    MUNICIPALITIES[
      profile?.region
    ]?.find(
      (item) =>
        item.id ===
        profile?.municipality
    )?.name

  const children =
    Array.isArray(
      profile?.children
    )
      ? profile.children
      : []

  const fullName =
    [
      profile?.firstName,
      profile?.lastName,
    ]
      .filter(Boolean)
      .join(' ')

  return (
    <div className="page">
      <section className="card profile-card">
        <div className="profile-card__top">
          <div className="profile-card__avatar">
            {(fullName || 'З')
              .split(/\s+/)
              .filter(Boolean)
              .slice(0, 2)
              .map(
                (part) =>
                  part[0]
                    ?.toUpperCase()
              )
              .join('')}
          </div>

          <div>
            <div className="profile-card__eyebrow">
              Сохранённые ответы
            </div>

            <h1>
              Ваша анкета
            </h1>

            <span>
              Просмотр ничего не изменяет.
            </span>
          </div>
        </div>

        <div className="profile-card__role">
          {dictLabel(
            DICT.FAMILY_RELATION,
            profile?.familyRelation,
            'Роль не указана'
          )}
        </div>
      </section>

      <Section title="Личные данные">
        <div className="ptiles">
          <Tile
            title="Имя"
            value={
              profile?.firstName
            }
          />

          <Tile
            title="Фамилия"
            value={
              profile?.lastName
            }
          />
        </div>
      </Section>

      <Section title="Ситуация">
        <div className="ptiles">
          <Tile
            title="Роль"
            value={dictLabel(
              DICT.FAMILY_RELATION,
              profile?.familyRelation
            )}
            wide
          />

          <Tile
            title="Статус"
            value={dictLabel(
              DICT.MILITARY_STATUS,
              profile?.militaryStatus
            )}
            wide
          />

          <Tile
            title="Регион"
            value={
              regionName
            }
          />

          <Tile
            title="Город / район"
            value={
              municipalityName
            }
          />

          <Tile
            title="Доход семьи"
            value={dictLabel(
              DICT.INCOME_RANGE,
              profile?.incomeRange
            )}
          />

          <Tile
            title="Занятость"
            value={dictLabel(
              DICT.EMPLOYMENT_STATUS,
              profile?.employmentStatus,
              'Не указана'
            )}
          />
        </div>
      </Section>

      <Section title="Здоровье и условия">
        <div className="ptiles">
          <BoolTile
            title="Ранение / контузия"
            value={
              profile?.injury
            }
          />

          <BoolTile
            title="Инвалидность"
            value={
              profile?.disability
            }
          />

          <BoolTile
            title="Жилищный вопрос"
            value={
              profile?.housingProblem
            }
          />

          <BoolTile
            title="Нужна газификация"
            value={
              profile?.gasificationNeeded
            }
          />

          <BoolTile
            title="Беременность"
            value={
              profile?.pregnancy
            }
          />

          {profile?.disability && (
            <Tile
              title="Группа инвалидности"
              value={dictLabel(
                DICT.DISABILITY_GROUP,
                profile?.disabilityGroup
              )}
            />
          )}
        </div>
      </Section>

      {profile?.familyRelation
        === 'CHILD' && (
        <Section title="Данные о себе">
          <div className="ptiles">
            <Tile
              title="Дата рождения"
              value={
                profile?.applicantBirthDate
                  ? formatDate(
                    profile.applicantBirthDate
                  )
                  : null
              }
            />

            <Tile
              title="Обучение"
              value={dictLabel(
                DICT.EDUCATION_LEVEL,
                profile?.applicantEducation
              )}
            />

            <BoolTile
              title="Учусь очно"
              value={
                profile?.applicantFullTime
              }
            />

            <BoolTile
              title="Статус сироты / без попечения"
              value={
                profile?.orphan
              }
            />
          </div>
        </Section>
      )}

      <Section
        title={
          `Дети · ${children.length} ${
            plural(
              children.length,
              [
                'ребёнок',
                'ребёнка',
                'детей',
              ]
            )
          }`
        }
      >
        {children.length === 0 ? (
          <div className="empty-hint">
            Дети в анкете не указаны.
          </div>
        ) : (
          <div className="stack">
            {children.map(
              (child, index) => {
                const age =
                  calcAge(
                    child.birthDate
                  )

                const ageText =
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

                    <div style={{ flex: 1 }}>
                      <div className="pchild__name">
                        Ребёнок {index + 1}
                      </div>

                      <div className="pchild__meta">
                        {ageText}
                      </div>

                      <div className="stack">
                        <div className="ptiles">
                          <Tile
                            title="Учреждение"
                            value={
                              educationLabel(
                                child
                              )
                            }
                          />

                          {child.grade && (
                            <Tile
                              title="Класс"
                              value={
                                `${child.grade} класс`
                              }
                            />
                          )}

                          {(
                            child.educationType
                              === 'COLLEGE'
                            || child.educationType
                              === 'UNIVERSITY'
                          ) && (
                            <BoolTile
                              title="Очная форма"
                              value={
                                child.fullTime
                              }
                            />
                          )}

                          <BoolTile
                            title="Инвалидность"
                            value={
                              child.disability
                            }
                          />

                          {child.disability
                            && child.disabilityGroup && (
                            <Tile
                              title="Группа инвалидности"
                              value={dictLabel(
                                DICT.DISABILITY_GROUP,
                                child.disabilityGroup
                              )}
                            />
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                )
              }
            )}
          </div>
        )}
      </Section>

      <div className="stack">
        <Link
          className="btn btn--primary btn--lg"
          to="/survey?reset=1"
        >
          Заполнить анкету заново
        </Link>

        <Link
          className="btn btn--ghost btn--lg"
          to="/account"
        >
          В профиль
        </Link>
      </div>
    </div>
  )
}