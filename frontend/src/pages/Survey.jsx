import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { ChoiceGroup, ProgressBar, Select, ToggleRow } from '../components/ui'
import { DICT, MUNICIPALITIES, REGIONS } from '../api/mockData'
import { calcAge, plural, todayStr } from '../utils'
import { haptic, getUser } from '../bridge/max'

const STEPS = [
  { title: 'Кто вы?', subtitle: 'Так мы поймём, для кого подбирать меры' },
  { title: 'Статус военнослужащего', subtitle: 'От этого зависят федеральные и региональные меры' },
  { title: 'Где вы живёте?', subtitle: 'Региональные меры зависят от места проживания' },
  { title: 'Дети в семье', subtitle: 'Добавьте всех детей до 23 лет' },
  { title: 'Здоровье военнослужащего', subtitle: 'Ранение и инвалидность влияют на выплаты' },
  { title: 'Жильё и быт', subtitle: 'Пара коротких вопросов' },
  { title: 'Доход и занятость', subtitle: 'Необязательно: без дохода часть мер не попадёт в подбор' },
  { title: 'Ваши данные', subtitle: 'ФИО — для документов, телефон — чтобы специалист МФЦ мог связаться с вами' },
]

const EMPTY = {
  familyRelation: null, militaryStatus: null, region: null, municipality: null,
  children: [], injury: false, disability: false, disabilityGroup: null,
  housingProblem: false, gasificationNeeded: false, pregnancy: false,
  incomeCategory: null, employmentStatus: null,
  fullName: '',
  phone: '',
}

function suggestEducation(birthDate) {
  const age = calcAge(birthDate)
  if (age == null) return {}
  if (age < 7) return { educationLevel: 'PRESCHOOL', grade: null }
  if (age <= 17) return { educationLevel: 'SCHOOL', grade: Math.min(11, Math.max(1, age - 6)) }
  if (age <= 22) return { educationLevel: 'COLLEGE', grade: null }
  return { educationLevel: 'UNIVERSITY', grade: null }
}

export default function Survey() {
  const { profile, saveProfile, runMatch, showToast } = useApp()
  const nav = useNavigate()
  const [params, setParams] = useSearchParams()
  const [step, setStep] = useState(() => Number(params.get('step')) || 0)
  const [draft, setDraft] = useState(() => {
  const base = profile ? { ...EMPTY, ...profile } : EMPTY
    if (!base.fullName) {
      const u = getUser()
      if (u && (u.firstName || u.lastName)) {
        base.fullName = [u.lastName, u.firstName].filter(Boolean).join(' ')
      }
    }
    return base
  })
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    const s = params.get('step')
    if (s != null) {
      setStep(Number(s) || 0)
      setParams({}, { replace: true })
    }
  }, [params, setParams])

  const set = (patch) => setDraft((d) => ({ ...d, ...patch }))

  const valid = [
    Boolean(draft.familyRelation),
    Boolean(draft.militaryStatus),
    Boolean(draft.region && draft.municipality),
    draft.children.every((c) => c.birthDate && c.educationLevel),
    !draft.disability || Boolean(draft.disabilityGroup),
    true,
    true,
    draft.fullName.trim().length >= 2,
  ][step]

  const finish = async () => {
    setSaving(true)
    try {
      saveProfile(draft)
      await runMatch(draft)
      showToast('Подбор готов и сохранён в кабинете')
      nav('/results')
    } catch (e) {
      showToast('Ошибка подбора, попробуйте ещё раз')
    } finally {
      setSaving(false)
    }
  }

  const addChild = () =>
    set({ children: [...draft.children, { id: 'c' + Date.now(), birthDate: '', educationLevel: null, grade: null, disability: false, disabilityGroup: null, fullTime: true }] })
  const updChild = (cid, patch) =>
    set({ children: draft.children.map((c) => (c.id === cid ? { ...c, ...patch } : c)) })
  const delChild = (cid) => set({ children: draft.children.filter((c) => c.id !== cid) })

  return (
    <div className="page">
      <div className="survey__head">
        <ProgressBar value={step + 1} max={STEPS.length} />
        <div className="survey__step">Шаг {step + 1} из {STEPS.length}</div>
        <h1 className="survey__title">{STEPS[step].title}</h1>
        <p className="survey__sub">{STEPS[step].subtitle}</p>
      </div>

      {step === 0 && (
        <ChoiceGroup options={DICT.FAMILY_RELATION} value={draft.familyRelation} onChange={(v) => set({ familyRelation: v })} />
      )}

      {step === 1 && (
        <ChoiceGroup options={DICT.MILITARY_STATUS} value={draft.militaryStatus} onChange={(v) => set({ militaryStatus: v })} />
      )}

      {step === 2 && (
        <div className="stack">
          <Select label="Регион" value={draft.region} options={REGIONS} onChange={(v) => set({ region: v, municipality: null })} />
          <Select
            label="Город / район"
            value={draft.municipality}
            options={draft.region ? MUNICIPALITIES[draft.region] : []}
            onChange={(v) => set({ municipality: v })}
            placeholder={draft.region ? 'Выберите…' : 'Сначала выберите регион'}
          />
        </div>
      )}

      {step === 3 && (
        <div className="stack">
          {draft.children.map((c, i) => {
            const age = calcAge(c.birthDate)
            return (
              <div className="card child" key={c.id}>
                <div className="child__head">
                  <b>Ребёнок {i + 1}</b>
                  {age != null && <span className="chip">{age} {plural(age, ['год', 'года', 'лет'])}</span>}
                  <button type="button" className="icon-btn" style={{ width: 32, height: 32, boxShadow: 'none' }} onClick={() => delChild(c.id)}>✕</button>
                </div>
                <label className="field">
                  <span>Дата рождения</span>
                  <input
                    type="date"
                    className="input"
                    value={c.birthDate}
                    max={todayStr()}
                    onChange={(e) => updChild(c.id, { birthDate: e.target.value, ...suggestEducation(e.target.value) })}
                  />
                </label>
                <Select
                  label="Где учится"
                  value={c.educationLevel}
                  options={DICT.EDUCATION_LEVEL}
                  onChange={(v) => updChild(c.id, { educationLevel: v, grade: v === 'SCHOOL' ? (c.grade ?? Math.max(1, (calcAge(c.birthDate) || 7) - 6)) : null })}
                />
                {c.educationLevel === 'SCHOOL' && (
                  <Select
                    label="Класс"
                    value={c.grade}
                    options={Array.from({ length: 11 }, (_, k) => ({ code: String(k + 1), label: `${k + 1} класс` }))}
                    onChange={(v) => updChild(c.id, { grade: Number(v) })}
                  />
                )}
                {(c.educationLevel === 'COLLEGE' || c.educationLevel === 'UNIVERSITY') && (
                  <ToggleRow label="Очная форма обучения" checked={c.fullTime} onChange={(v) => updChild(c.id, { fullTime: v })} />
                )}
                <ToggleRow label="Инвалидность" checked={c.disability} onChange={(v) => updChild(c.id, { disability: v, disabilityGroup: v ? c.disabilityGroup : null })} />
                {c.disability && (
                  <Select label="Категория" value={c.disabilityGroup} options={DICT.CHILD_DISABILITY_GROUP} onChange={(v) => updChild(c.id, { disabilityGroup: v })} />
                )}
              </div>
            )
          })}
          {draft.children.length < 8 && <button className="btn btn--ghost" onClick={addChild}>＋ Добавить ребёнка</button>}
          {draft.children.length === 0 && <p className="p muted">Если детей нет — просто нажмите «Далее».</p>}
        </div>
      )}

      {step === 4 && (
        <div className="stack">
          <ToggleRow label="Есть ранение или контузия" hint="Влияет на единовременные выплаты" checked={draft.injury} onChange={(v) => set({ injury: v })} />
          <ToggleRow label="Установлена инвалидность" hint="Связанная с военной травмой" checked={draft.disability} onChange={(v) => set({ disability: v, disabilityGroup: v ? draft.disabilityGroup : null })} />
          {draft.disability && (
            <Select label="Группа инвалидности" value={draft.disabilityGroup} options={DICT.DISABILITY_GROUP} onChange={(v) => set({ disabilityGroup: v })} />
          )}
        </div>
      )}

      {step === 5 && (
        <div className="stack">
          <ToggleRow label="Жилищный вопрос актуален" hint="Субсидии на ЖКУ, помощь с жильём" checked={draft.housingProblem} onChange={(v) => set({ housingProblem: v })} />
          <ToggleRow label="Нужна газификация" hint="Компенсация подключения газа" checked={draft.gasificationNeeded} onChange={(v) => set({ gasificationNeeded: v })} />
          {draft.familyRelation === 'WIFE' && draft.militaryStatus !== 'DECEASED' && (
            <ToggleRow label="Ожидается ребёнок" hint="Беременность учитывается в выплатах" checked={draft.pregnancy} onChange={(v) => set({ pregnancy: v })} />
          )}
        </div>
      )}

      {step === 6 && (
        <div className="stack">
          <div className="banner banner--info">
            Доход указывать не обязательно. Но без него мы не сможем проверить меры для семей с невысоким доходом — и честно сообщим об этом в результатах.
          </div>
          <ChoiceGroup options={DICT.INCOME_CATEGORY} value={draft.incomeCategory} onChange={(v) => set({ incomeCategory: v })} />
          <Select label="Занятость" value={draft.employmentStatus} options={DICT.EMPLOYMENT_STATUS} onChange={(v) => set({ employmentStatus: v })} placeholder="Не указывать" />
        </div>
      )}

      {step === 7 && (
        <div className="stack">
          {(() => {
            const u = getUser()
            return u?.firstName ? (
              <div className="banner banner--info">
                Привет, {u.firstName}! Часть данных мы взяли из вашего профиля — проверьте и поправьте при необходимости.
              </div>
            ) : null
          })()}
          <label className="field">
            <span>ФИО</span>
            <input
              className="input"
              placeholder="Иванова Елена Сергеевна"
              value={draft.fullName}
              onChange={(e) => set({ fullName: e.target.value })}
            />
          </label>
          <label className="field">
            <span>Телефон (необязательно)</span>
            <input
              className="input"
              type="tel"
              placeholder="+7 900 000-00-00"
              value={draft.phone}
              onChange={(e) => set({ phone: e.target.value })}
            />
          </label>
          <p className="p muted">
            Паспорт и СНИЛС для подбора не нужны. Телефон используется только для связи по вашему обращению в МФЦ.
          </p>
        </div>
      )}

      <div className="survey__nav">
        {step > 0 && (
          <button className="btn btn--ghost" onClick={() => { haptic(); setStep((s) => s - 1) }}>Назад</button>
        )}
        {step < STEPS.length - 1 ? (
          <button className="btn btn--primary" disabled={!valid} onClick={() => { haptic(); setStep((s) => s + 1) }}>Далее</button>
        ) : (
          <button className="btn btn--primary" disabled={saving} onClick={finish}>
            {saving ? 'Подбираем…' : 'Подобрать меры'}
          </button>
        )}
      </div>
    </div>
  )
}
