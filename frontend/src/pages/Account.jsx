import { useMemo, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { DICT, LABELS, MUNICIPALITIES, REGIONS } from '../api/mockData'
import { Money, StatusBadge, StatusSwitch } from '../components/ui'
import { calcAge, plural } from '../utils'

const STATUSES = ['NOT_APPLIED', 'SUBMITTED', 'APPROVED', 'RECEIVED']

function dictLabel(list, code) {
  if (!code) return null
  return list?.find((d) => d.code === code)?.label || code
}

export default function Account() {
  const { profile, results, support, updateSupport } = useApp()
  const nav = useNavigate()
  const [filter, setFilter] = useState('ALL')
  const [detailsOpen, setDetailsOpen] = useState(true)

  const stale = !results || results.version !== JSON.stringify(profile)
  const items = useMemo(() => (results?.matched || []).filter((m) => support[m.id]), [results, support])

  const counts = { ALL: items.length }
  STATUSES.forEach((s) => { counts[s] = items.filter((m) => support[m.id].status === s).length })
  const list = filter === 'ALL' ? items : items.filter((m) => support[m.id].status === filter)
  const inProgress = counts.SUBMITTED + counts.APPROVED

  // --- профиль ---
  const fullName = profile?.fullName || ''
  const initials = fullName.split(' ').filter(Boolean).slice(0, 2).map((w) => w[0].toUpperCase()).join('')
  const regionName = REGIONS.find((r) => r.id === profile?.region)?.name
  const cityName = profile?.region
    ? MUNICIPALITIES[profile.region]?.find((m) => m.id === profile.municipality)?.name
    : null
  const incomeUnknown = !profile?.incomeCategory || profile.incomeCategory === 'UNKNOWN'
  const kids = profile?.children || []

  const flags = [
    profile?.injury && 'Ранение',
    profile?.disability && `Инвалидность${dictLabel(DICT.DISABILITY_GROUP, profile.disabilityGroup) ? ' · ' + dictLabel(DICT.DISABILITY_GROUP, profile.disabilityGroup) : ''}`,
    profile?.housingProblem && 'Жилищный вопрос',
    profile?.gasificationNeeded && 'Газификация',
    profile?.pregnancy && 'Ожидается ребёнок',
  ].filter(Boolean)

  return (
    <div className="page">
      <div className="acc__top">
        <button className="btn btn--ghost" onClick={() => nav('/survey')}>✏️ Изменить анкету</button>
        <button className="btn btn--ghost" onClick={() => nav('/print')}>📄 PDF-сводка</button>
      </div>

      {/* ===== Профиль ===== */}
      <div className="card pcard">
        <div className="pcard__top">
          <div className="pcard__ava">{initials || '👤'}</div>
          <div className="pcard__id">
            <div className="pcard__name">{fullName || 'Имя не указано'}</div>
            <div className="pcard__phone">{profile?.phone || 'Телефон не указан'}</div>
          </div>
        </div>
        <span className="pcard__role">💜 {dictLabel(DICT.FAMILY_RELATION, profile?.familyRelation) || 'Роль не указана'}</span>
        <div className="pcard__stats">
          <div className="pcard__stat"><b>{counts.ALL}</b><span>мер найдено</span></div>
          <div className="pcard__stat"><b>{inProgress}</b><span>в работе</span></div>
          <div className="pcard__stat"><b>{counts.RECEIVED}</b><span>получено</span></div>
        </div>
      </div>

      {/* ===== Параметры подбора ===== */}
      <div className="card pdetails">
        <div className="pdetails__head" onClick={() => setDetailsOpen((v) => !v)}>
          <span className="pdetails__title">Параметры подбора</span>
          <span className={'pdetails__chev' + (detailsOpen ? ' up' : '')}>⌃</span>
        </div>

        {detailsOpen && (
          <>
            <div className="ptiles">
              <div className="ptile"><span>📍 Проживание</span><b>{[cityName, regionName].filter(Boolean).join(', ') || '—'}</b></div>
              <div className="ptile"><span>🎖 Военнослужащий</span><b>{dictLabel(DICT.MILITARY_STATUS, profile?.militaryStatus) || '—'}</b></div>
              <div className="ptile"><span>💰 Доход семьи</span><b className={incomeUnknown ? 'warn' : ''}>{dictLabel(DICT.INCOME_CATEGORY, profile?.incomeCategory) || 'Не указан'}</b></div>
              <div className="ptile"><span>💼 Занятость</span><b>{dictLabel(DICT.EMPLOYMENT_STATUS, profile?.employmentStatus) || 'Не указана'}</b></div>
            </div>

            {flags.length > 0 && (
              <div className="pflags">
                {flags.map((f) => <span className="chip chip--flag" key={f}>⚠️ {f}</span>)}
              </div>
            )}

            <div className="pchild__wrap">
              <div className="pdetails__title" style={{ marginBottom: 8 }}>
                Дети {kids.length > 0 && `· ${kids.length} ${plural(kids.length, ['ребёнок', 'ребёнка', 'детей'])}`}
              </div>
              {kids.length === 0 ? (
                <p className="p muted">Не указаны</p>
              ) : (
                <div className="stack" style={{ gap: 8 }}>
                  {kids.map((c) => {
                    const age = calcAge(c.birthDate)
                    const edu = dictLabel(DICT.EDUCATION_LEVEL, c.educationLevel)
                    const grade = c.educationLevel === 'SCHOOL' && c.grade ? `, ${c.grade} класс` : ''
                    return (
                      <div className="pchild" key={c.id}>
                        <div className="pchild__ava">🧒</div>
                        <div style={{ flex: 1, minWidth: 0 }}>
                          <div className="pchild__name">{age != null ? `${age} ${plural(age, ['год', 'года', 'лет'])}` : 'Возраст не указан'}</div>
                          <div className="pchild__meta">{[edu && edu + grade, c.fullTime && c.educationLevel !== 'SCHOOL' && c.educationLevel !== 'PRESCHOOL' && 'очная форма'].filter(Boolean).join(' · ') || '—'}</div>
                        </div>
                        {c.disability && <span className="chip">инвалидность</span>}
                      </div>
                    )
                  })}
                </div>
              )}
            </div>
          </>
        )}
      </div>

      {stale && (
        <div className="banner banner--warn">
          Анкета менялась — обновите подбор, чтобы список был актуален.
          <Link className="banner__link" to="/results">Обновить</Link>
        </div>
      )}

      {/* ===== Меры ===== */}
      <div className="sec__title" style={{ marginTop: 4 }}>Меры поддержки</div>

      <div className="tabs">
        <button className={'tab' + (filter === 'ALL' ? ' tab--on' : '')} onClick={() => setFilter('ALL')}>
          Все · {counts.ALL}
        </button>
        {STATUSES.map((s) => (
          <button key={s} className={'tab' + (filter === s ? ' tab--on' : '')} onClick={() => setFilter(s)}>
            {LABELS.statusShort[s]} · {counts[s]}
          </button>
        ))}
      </div>

      <div className="stack">
        {list.map((m) => {
          const st = support[m.id]
          return (
            <article key={m.id} className="card acc__item" onClick={() => nav(`/measure/${m.id}`)}>
              <div className="mcard__top">
                <span className="chip chip--blue">{LABELS.recipient[m.recipient]}</span>
                {st.reminder && <span title="Напоминание включено">🔔</span>}
                <StatusBadge status={st.status} />
              </div>
              <h3 className="mcard__title">{m.name}</h3>
              <Money amount={m.amount} frequency={m.frequency} />
              <div onClick={(e) => e.stopPropagation()}>
                <StatusSwitch value={st.status} onChange={(v) => updateSupport(m.id, { status: v })} />
              </div>
            </article>
          )
        })}

        {list.length === 0 && (
          <div className="card" style={{ textAlign: 'center', padding: 28 }}>
            <p className="p muted">Здесь появятся меры после опроса.</p>
            <button className="btn btn--primary" style={{ marginTop: 12 }} onClick={() => nav('/survey')}>
              Пройти опрос
            </button>
          </div>
        )}
      </div>
    </div>
  )
}