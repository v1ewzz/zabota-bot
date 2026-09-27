import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { ChoiceGroup, ProgressBar, Select, ToggleRow } from '../components/ui'
import { DICT, MUNICIPALITIES, REGIONS } from '../api/mockData'
import { IDS, hasBackendId } from '../api/ids'
import { calcAge, plural, todayStr } from '../utils'

const BASE_STEPS = [
  {
    id: 'role',
    title: 'Кому нужна поддержка?',
    subtitle: 'Выберите вашу роль по отношению к военнослужащему. Это влияет на состав доступных мер.',
  },
  {
    id: 'military',
    title: 'Статус военнослужащего',
    subtitle: 'Укажите актуальный статус военнослужащего или участника СВО.',
  },
  {
    id: 'location',
    title: 'Где вы живёте?',
    subtitle: 'Регион и муниципалитет нужны для региональных и муниципальных мер.',
  },
  {
    id: 'children',
    title: 'Дети в семье',
    subtitle: 'Укажите детей, для которых тоже может быть доступна поддержка.',
  },
  {
    id: 'health',
    title: 'Здоровье и особые условия',
    subtitle: 'Ранение и инвалидность могут менять состав доступных мер.',
  },
  {
    id: 'home',
    title: 'Жильё и быт',
    subtitle: 'Эти ответы используются только там, где они нужны конкретной мере.',
  },
  {
    id: 'income',
    title: 'Доход и занятость',
    subtitle: 'Ориентир по доходу помогает учитывать адресные меры.',
  },
  {
    id: 'additional',
    title: 'Дополнительные условия',
    subtitle: 'Уточните обстоятельства, которые могут быть важны для отдельных мер поддержки.',
  },
]

const CHILD_EDUCATION_TYPES = [
  { code: 'PRESCHOOL', label: 'Детский сад' },
  { code: 'SCHOOL', label: 'Школа' },
  { code: 'COLLEGE', label: 'Колледж' },
  { code: 'UNIVERSITY', label: 'Вуз' },
  { code: 'NONE', label: 'Учреждение не указано' },
]

function childMinBirthDate() {
  return '1900-01-01'
}

function validChildBirthDate(value) {
  if (!value || !/^\d{4}-\d{2}-\d{2}$/.test(value)) return false
  const [year, month, day] = value.split('-').map(Number)
  const date = new Date(year, month - 1, day)
  return Number.isInteger(year)
    && Number.isInteger(month)
    && Number.isInteger(day)
    && date.getFullYear() === year
    && date.getMonth() === month - 1
    && date.getDate() === day
    && value >= childMinBirthDate()
    && value <= todayStr()
}

const ADDITIONAL_FIELDS = {
  loanExists: false,
  businessPlan: false,
  jobSeeker: false,
  socialServiceNeed: false,
  legalHelpNeeded: false,
  legalIssueCategory: null,
  legalIssueCategoryId: null,
}

const CHILD_EXTRA_FIELDS = {
  applicantBirthDate: '',
  applicantEducation: 'SCHOOL',
  applicantFullTime: true,
  orphan: false,
}

const EMPTY = {
  familyRelation: null,
  militaryStatus: null,
  region: null,
  municipality: null,
  children: [],
  injury: false,
  disability: false,
  disabilityGroup: null,
  housingProblem: false,
  gasificationNeeded: false,
  pregnancy: false,
  incomeRange: null,
  incomeCategoryId: null,
  employmentStatusId: null,
  ...CHILD_EXTRA_FIELDS,
  ...ADDITIONAL_FIELDS,
}

function suggestEducation(birthDate) {
  const age = calcAge(birthDate)
  if (age == null) return {}
  if (age < 7) return { educationLevel: hasBackendId('educationLevel', 'PRESCHOOL') ? 'PRESCHOOL' : null, grade: null }
  if (age <= 17) return { educationLevel: hasBackendId('educationLevel', 'SCHOOL') ? 'SCHOOL' : null, grade: Math.min(11, Math.max(1, age - 6)) }
  if (hasBackendId('educationLevel', 'COLLEGE')) return { educationLevel: 'COLLEGE', grade: null }
  if (hasBackendId('educationLevel', 'UNIVERSITY')) return { educationLevel: 'UNIVERSITY', grade: null }
  return { educationLevel: null, grade: null }
}

function isQuestionnaireOptionConfigured(kind, code) {
  return hasBackendId(kind, code)
}

function withAvailability(options, kind) {
  return options.filter((option) => isQuestionnaireOptionConfigured(kind, option.code))
}

export default function Survey() {
  const { profile, saveProfile, runMatch, showToast } = useApp()
  const nav = useNavigate()
  const [draft, setDraft] = useState(() => ({ ...EMPTY, ...(profile || {}) }))
  const [stepIndex, setStepIndex] = useState(0)
  const [saving, setSaving] = useState(false)
  const [dirty, setDirty] = useState(false)

  const steps = useMemo(() => {
    const base = BASE_STEPS.filter((step) => !(step.id === 'children' && draft.familyRelation === 'CHILD'))
    base.push({
      id: 'finish',
      title: 'Проверьте ответы',
      subtitle: 'Проверьте основные данные. После подтверждения приложение выполнит подбор мер поддержки.',
    })
    return base
  }, [draft.familyRelation])

  const step = steps[stepIndex] || steps[steps.length - 1]
  const familyRelationOptions = useMemo(() => withAvailability(DICT.FAMILY_RELATION, 'familyRelation'), [])
  const militaryStatusOptions = useMemo(
    () => withAvailability(DICT.MILITARY_STATUS, 'militaryStatus').filter((option) => option.code !== 'CONSCRIPT'),
    []
  )
  const educationOptions = useMemo(() => withAvailability(DICT.EDUCATION_LEVEL, 'educationLevel'), [])
  const childEducationTypeOptions = useMemo(() => CHILD_EDUCATION_TYPES.map((option) => ({
    ...option,
    disabled: option.code !== 'SCHOOL' && option.code !== 'NONE' && !hasBackendId('educationLevel', option.code),
    disabledHint: option.code !== 'SCHOOL' && option.code !== 'NONE' && !hasBackendId('educationLevel', option.code)
      ? 'Этот вариант пока не подключён к справочнику подбора.'
      : '',
  })), [])
  const legalIssueOptions = useMemo(() => DICT.LEGAL_ISSUE_CATEGORY || [], [])
  const parentRelationOptions = useMemo(
    () => familyRelationOptions.filter((option) => option.code === 'MOTHER' || option.code === 'FATHER'),
    [familyRelationOptions]
  )
  const disabilityOptions = useMemo(() => DICT.DISABILITY_GROUP.filter((option) => hasBackendId('disabilityGroup', option.code)), [])
  const hasAnyDisabilityGroup = disabilityOptions.length > 0
  const applicantAge = calcAge(draft.applicantBirthDate)

  const set = (patch) => {
    setDraft((current) => ({ ...current, ...patch }))
    setDirty(true)
  }

  const changeFamilyRelation = (value) => {
    set({
      familyRelation: value,
      ...(value === 'CHILD'
        ? { children: [] }
        : {
          applicantBirthDate: '',
          applicantEducation: 'SCHOOL',
          applicantFullTime: true,
          orphan: false,
        }),
    })
  }

  const valid = useMemo(() => {
    if (!step) return false

    switch (step.id) {
      case 'role':
        return Boolean(draft.familyRelation) && (
          draft.familyRelation !== 'CHILD' || Boolean(draft.applicantBirthDate)
        )
      case 'military':
        return Boolean(draft.militaryStatus)
      case 'location':
        return Boolean(draft.region && draft.municipality)
      case 'children':
        return draft.children.every((child) => Boolean(child.birthDate)
          && validChildBirthDate(child.birthDate)
          && (child.educationType === 'NONE' || Boolean(child.educationLevel)))
      case 'health':
        return !draft.disability || !hasAnyDisabilityGroup || Boolean(draft.disabilityGroup)
      case 'income':
        return Boolean(draft.incomeRange)
      case 'additional':
        return !draft.legalHelpNeeded || Boolean(draft.legalIssueCategory || draft.legalIssueCategoryId)
      default:
        return true
    }
  }, [step, draft, hasAnyDisabilityGroup])

  const next = () => {
    if (!valid) {
      if (step.id === 'children') {
        showToast('Проверьте дату рождения и образование детей')
      } else if (step.id === 'additional') {
        showToast('Укажите категорию юридического вопроса')
      } else {
        showToast('Заполните обязательные поля')
      }
      return
    }

    if (stepIndex < steps.length - 1) {
      setStepIndex((value) => value + 1)
    }
  }

  const prev = () => {
    if (stepIndex > 0) {
      setStepIndex((value) => value - 1)
      return
    }

    nav('/')
  }

  const finish = async () => {
    setSaving(true)

    try {
      saveProfile(draft)
      await runMatch(draft)
      showToast('Подбор готов')
      nav('/results')
    } catch (error) {
      console.error(error)
      showToast(error.message || 'Не удалось выполнить подбор')
    } finally {
      setSaving(false)
      setDirty(false)
    }
  }

  const addChild = () => set({
    children: [
      ...draft.children,
      {
        id: `c-${Date.now()}`,
        birthDate: '',
        educationLevel: null,
        educationType: null,
        grade: null,
        disability: false,
        disabilityGroup: null,
        fullTime: true,
      },
    ],
  })

  const updateChild = (id, patch) => set({
    children: draft.children.map((child) => (
      child.id === id
        ? { ...child, ...patch, ...(patch.disability === false ? { disabilityGroupId: null, disabilityGroup: null } : {}) }
        : child
    )),
  })

  const deleteChild = (id) => set({
    children: draft.children.filter((child) => child.id !== id),
  })

  return (
    <div className="page survey">
      <div className="survey__head">
        <div className="survey__meta-row">
          <span>Шаг {stepIndex + 1} из {steps.length}</span>
          {dirty && <span className="survey__draft">есть изменения в анкете</span>}
        </div>

        <ProgressBar value={stepIndex + 1} max={steps.length} />
        <h1 className="survey__title">{step.title}</h1>
        <p className="survey__sub">{step.subtitle}</p>
      </div>

      {step.id === 'role' && (
        <div className="stack">
          <ChoiceGroup
            options={familyRelationOptions.filter((option) => option.code !== 'MOTHER' && option.code !== 'FATHER')}
            value={draft.familyRelation}
            onChange={changeFamilyRelation}
          />

          {parentRelationOptions.length > 0 && (
            <div className="card role-parent">
              <div className="role-parent__head">
                <div>
                  <strong>Родитель военнослужащего</strong>
                  <span>Для матери и отца используются одинаковые вопросы анкеты.</span>
                </div>
              </div>

              <div className="role-parent__actions">
                {parentRelationOptions.map((option) => (
                  <button
                    key={option.code}
                    type="button"
                    className={`role-parent__btn${draft.familyRelation === option.code ? ' on' : ''}`}
                    onClick={() => changeFamilyRelation(option.code)}
                  >
                    {option.label.split(' военнослужащего')[0]}
                  </button>
                ))}
              </div>
            </div>
          )}

          {draft.familyRelation === 'CHILD' && (
            <div className="card form-section">
              <div className="section-title">Если вы ребёнок военнослужащего</div>
              <p className="p muted">Укажите дату рождения. Для совершеннолетних детей дополнительно учитываются обучение и специальные условия.</p>

              <label className="field">
                <span>Дата рождения</span>
                <input
                  className="input"
                  type="date"
                  max={todayStr()}
                  value={draft.applicantBirthDate}
                  onChange={(event) => set({ applicantBirthDate: event.target.value })}
                />
              </label>

              <Select
                label="Ваше обучение"
                value={draft.applicantEducation}
                options={educationOptions}
                onChange={(value) => set({ applicantEducation: value })}
              />

              {applicantAge != null && applicantAge >= 18 && (
                <ToggleRow
                  label="Учусь очно"
                  hint="Для отдельных мер после 18 лет это может быть важно."
                  checked={draft.applicantFullTime}
                  onChange={(value) => set({ applicantFullTime: value })}
                />
              )}

              <ToggleRow
                label="Есть статус сироты / ребёнка, оставшегося без попечения"
                hint="Ответ нужен для отдельных сценариев после потери родителя."
                checked={draft.orphan}
                onChange={(value) => set({ orphan: value })}
              />
            </div>
          )}
        </div>
      )}

      {step.id === 'military' && (
        <div className="stack">
          {militaryStatusOptions.length > 0 ? (
            <ChoiceGroup
              options={militaryStatusOptions}
              value={draft.militaryStatus}
              onChange={(value) => set({ militaryStatus: value })}
            />
          ) : (
            <div className="banner banner--warn">Варианты статуса временно недоступны.</div>
          )}
        </div>
      )}

      {step.id === 'location' && (
        <div className="stack">
          <Select
            label="Регион"
            value={draft.region}
            options={REGIONS.filter((region) => IDS.region[region.id])}
            onChange={(value) => set({ region: value, municipality: null })}
          />

          <Select
            label="Город / район"
            value={draft.municipality}
            options={draft.region
              ? (MUNICIPALITIES[draft.region] || []).filter((municipality) => IDS.municipality[`${draft.region}:${municipality.id}`])
              : []}
            onChange={(value) => set({ municipality: value })}
            placeholder={draft.region ? 'Выберите…' : 'Сначала выберите регион'}
          />
        </div>
      )}

      {step.id === 'children' && (
        <div className="stack">
          {draft.children.map((child, index) => {
            const age = calcAge(child.birthDate)

            return (
              <div className="card child" key={child.id}>
                <div className="child__head">
                  <div>
                    <b>Ребёнок {index + 1}</b>
                    {age != null && <span className="chip">{age} {plural(age, ['год', 'года', 'лет'])}</span>}
                  </div>
                  <button
                    className="icon-btn icon-btn--danger"
                    type="button"
                    onClick={() => deleteChild(child.id)}
                    aria-label="Удалить ребёнка"
                  >
                    ×
                  </button>
                </div>

                <label className="field">
                  <span>Дата рождения</span>
                  <input
                    className="input"
                    type="date"
                    min={childMinBirthDate()}
                    max={todayStr()}
                    value={child.birthDate}
                    onChange={(event) => updateChild(child.id, {
                      birthDate: event.target.value,
                      ...suggestEducation(event.target.value),
                      educationType: suggestEducation(event.target.value).educationLevel,
                    })}
                  />
                  {child.birthDate && !validChildBirthDate(child.birthDate) && (
                    <span className="field__error">Укажите реальную дату рождения ребёнка в допустимом диапазоне.</span>
                  )}
                </label>

                <Select
                  label="Образовательное учреждение"
                  value={child.educationType || child.educationLevel}
                  options={childEducationTypeOptions}
                  onChange={(value) => updateChild(child.id, {
                    educationType: value,
                    educationLevel: value === 'NONE'
                      ? child.educationLevel
                      : (hasBackendId('educationLevel', value) ? value : child.educationLevel),
                    grade: value === 'SCHOOL'
                      ? (child.grade ?? Math.max(1, (calcAge(child.birthDate) || 7) - 6))
                      : null,
                  })}
                  placeholder="Выберите…"
                />

                {child.educationType === 'NONE' && (
                  <div className="banner banner--info">Учреждение не привязывается к анкете. Такой ребёнок не используется в правилах подбора, связанных с образовательной организацией.</div>
                )}

                {child.educationType && child.educationType !== 'NONE' && !hasBackendId('educationLevel', child.educationType) && (
                  <div className="banner banner--warn">Этот вариант уже доступен в интерфейсе, но пока не подключён к backend-справочнику и не влияет на расчёт мер.</div>
                )}

                {child.educationLevel === 'SCHOOL' && (
                  <Select
                    label="Класс"
                    value={child.grade}
                    options={Array.from(
                      { length: 11 },
                      (_, index) => ({ code: String(index + 1), label: `${index + 1} класс` })
                    )}
                    onChange={(value) => updateChild(child.id, { grade: Number(value) })}
                  />
                )}

                {(child.educationLevel === 'COLLEGE' || child.educationLevel === 'UNIVERSITY') && (
                  <ToggleRow
                    label="Очная форма обучения"
                    checked={child.fullTime}
                    onChange={(value) => updateChild(child.id, { fullTime: value })}
                  />
                )}

                <ToggleRow
                  label="Инвалидность"
                  checked={child.disability}
                  onChange={(value) => updateChild(child.id, {
                    disability: value,
                    disabilityGroup: value ? child.disabilityGroup : null,
                    disabilityGroupId: null,
                  })}
                />

                {child.disability && hasAnyDisabilityGroup && (
                  <Select
                    label="Группа инвалидности"
                    value={child.disabilityGroup}
                    options={disabilityOptions}
                    onChange={(value) => updateChild(child.id, { disabilityGroup: value, disabilityGroupId: null })}
                  />
                )}
              </div>
            )
          })}

          {draft.children.length < 8 && (
            <button className="btn btn--ghost" type="button" onClick={addChild}>
              + Добавить ребёнка
            </button>
          )}

          {draft.children.length === 0 && (
            <div className="empty-hint">
              Если детей нет — просто продолжите. Анкета подберёт меры только для вашей роли.
            </div>
          )}
        </div>
      )}

      {step.id === 'health' && (
        <div className="stack">
          <ToggleRow
            label="Есть ранение или контузия"
            hint="Может влиять на отдельные выплаты."
            checked={draft.injury}
            onChange={(value) => set({ injury: value })}
          />

          <ToggleRow
            label="Установлена инвалидность"
            hint="Для некоторых мер важно указать группу инвалидности."
            checked={draft.disability}
            onChange={(value) => set({
              disability: value,
              disabilityGroup: value ? draft.disabilityGroup : null,
              disabilityGroupId: null,
            })}
          />

          {draft.disability && hasAnyDisabilityGroup ? (
            <Select
              label="Группа инвалидности"
              value={draft.disabilityGroup}
              options={disabilityOptions}
              onChange={(value) => set({ disabilityGroup: value, disabilityGroupId: null })}
            />
          ) : null}

          {draft.disability && !hasAnyDisabilityGroup && (
            <div className="banner banner--warn">
              Сейчас можно указать сам факт инвалидности. Для отдельных мер группа инвалидности может потребоваться отдельно.
            </div>
          )}
        </div>
      )}

      {step.id === 'home' && (
        <div className="stack">
          <ToggleRow
            label="Жилищный вопрос актуален"
            hint="Например, существенная нагрузка по оплате жилья и ЖКУ."
            checked={draft.housingProblem}
            onChange={(value) => set({ housingProblem: value })}
          />

          <ToggleRow
            label="Нужна газификация"
            hint="Для мер, связанных с подключением газа."
            checked={draft.gasificationNeeded}
            onChange={(value) => set({ gasificationNeeded: value })}
          />

          {draft.familyRelation === 'WIFE' && draft.militaryStatus !== 'DECEASED_MILITARY' && (
            <ToggleRow
              label="Ожидается ребёнок"
              hint="Используется только в сценариях, где беременность влияет на подбор."
              checked={draft.pregnancy}
              onChange={(value) => set({ pregnancy: value })}
            />
          )}
        </div>
      )}

      {step.id === 'income' && (
        <div className="stack">
          <div className="banner banner--info">
            Укажите примерный совокупный ежемесячный доход всей семьи. Точная сумма не нужна: достаточно диапазона.
          </div>

          <ChoiceGroup
            options={DICT.INCOME_RANGE}
            value={draft.incomeRange}
            onChange={(value) => set({ incomeRange: value })}
          />

          <Select
            label="Занятость"
            value={draft.employmentStatus}
            options={DICT.EMPLOYMENT_STATUS}
            onChange={(value) => set({ employmentStatus: value, employmentStatusId: null })}
            placeholder="Не указывать"
          />
        </div>
      )}

      {step.id === 'additional' && (
        <div className="stack">
          <ToggleRow
            label="Есть действующий кредит или ипотека"
            hint="Нужно для мер, связанных с кредитными обязательствами участников СВО."
            checked={draft.loanExists}
            onChange={(value) => set({ loanExists: value })}
          />

          <ToggleRow
            label="Есть бизнес-план для открытия или развития своего дела"
            hint="Может использоваться для мер поддержки предпринимательства и самозанятости."
            checked={draft.businessPlan}
            onChange={(value) => set({ businessPlan: value })}
          />

          <ToggleRow
            label="Сейчас ищете работу"
            hint="Уточнение для мер содействия занятости."
            checked={draft.jobSeeker}
            onChange={(value) => set({ jobSeeker: value })}
          />

          <ToggleRow
            label="Нужна социальная помощь или социальное обслуживание"
            hint="Ответ используется для отдельных социальных услуг."
            checked={draft.socialServiceNeed}
            onChange={(value) => set({ socialServiceNeed: value })}
          />

          <ToggleRow
            label="Нужна юридическая помощь"
            hint="Выберите категорию вопроса, чтобы уточнить юридические меры поддержки."
            checked={draft.legalHelpNeeded}
            onChange={(value) => set({
              legalHelpNeeded: value,
              ...(value ? {} : { legalIssueCategory: null, legalIssueCategoryId: null }),
            })}
          />

          {draft.legalHelpNeeded && (
            <Select
              label="Категория юридического вопроса"
              value={draft.legalIssueCategory}
              options={legalIssueOptions}
              onChange={(value) => set({
                legalIssueCategory: value,
                legalIssueCategoryId: legalIssueOptions.find((option) => option.code === value)?.id || null,
              })}
              placeholder="Выберите…"
            />
          )}
        </div>
      )}

      {step.id === 'finish' && (
        <div className="stack">
          <div className="card review">
            <ReviewRow title="Кто вы" value={DICT.FAMILY_RELATION.find((item) => item.code === draft.familyRelation)?.label} />
            <ReviewRow title="Статус" value={DICT.MILITARY_STATUS.find((item) => item.code === draft.militaryStatus)?.label} />
            <ReviewRow
              title="Регион"
              value={[
                REGIONS.find((item) => item.id === draft.region)?.name,
                MUNICIPALITIES[draft.region]?.find((item) => item.id === draft.municipality)?.name,
              ].filter(Boolean).join(', ')}
            />
            <ReviewRow
              title="Дети"
              value={draft.familyRelation === 'CHILD'
                ? 'Вы — ребёнок военнослужащего'
                : `${draft.children.length} ${plural(draft.children.length, ['ребёнок', 'ребёнка', 'детей'])}`}
            />
            <ReviewRow title="Доход" value={DICT.INCOME_RANGE.find((item) => item.code === draft.incomeRange)?.label} />
            <ReviewRow title="Кредит / ипотека" value={draft.loanExists ? 'Есть' : 'Нет'} />
            <ReviewRow title="Бизнес-план" value={draft.businessPlan ? 'Есть' : 'Нет'} />
            <ReviewRow title="Поиск работы" value={draft.jobSeeker ? 'Ищу работу' : 'Не ищу'} />
            <ReviewRow title="Социальная помощь" value={draft.socialServiceNeed ? 'Нужна' : 'Не нужна'} />
            <ReviewRow
              title="Юридическая помощь"
              value={draft.legalHelpNeeded
                ? (legalIssueOptions.find((item) => item.code === draft.legalIssueCategory)?.label || 'Категория не указана')
                : 'Не нужна'}
            />
            <ReviewRow title="Профиль" value={profile?.fullName || '—'} />
          </div>

          <div className="banner banner--warn">
            Подбор является предварительным. Право на конкретную меру подтверждает уполномоченный орган по официальным документам и условиям.
          </div>
        </div>
      )}

      <div className="survey__nav">
        <button className="btn btn--ghost" type="button" onClick={prev}>
          Назад
        </button>

        {step.id === 'finish' ? (
          <button className="btn btn--primary" type="button" disabled={saving} onClick={finish}>
            {saving ? 'Подбираем…' : 'Подобрать меры'}
          </button>
        ) : (
          <button className="btn btn--primary" type="button" onClick={next}>
            Далее
          </button>
        )}
      </div>
    </div>
  )
}

function ReviewRow({ title, value }) {
  return (
    <div className="review__row">
      <span>{title}</span>
      <strong>{value || '—'}</strong>
    </div>
  )
}
