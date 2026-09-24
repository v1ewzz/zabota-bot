import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import MeasureCard from '../components/MeasureCard'
import { EmptyState } from '../components/ui'
import { LABELS } from '../api/mockData'

export default function Results() {
  const { profile, results, runMatch } = useApp()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [tab, setTab] = useState('ALL')

  const version = useMemo(() => JSON.stringify(profile), [profile])
  const stale = !results || results.version !== version
  const matched = results?.matched || []
  const recipients = useMemo(
    () => [...new Set(matched.map((measure) => measure.recipient || 'FAMILY'))],
    [matched]
  )

  useEffect(() => {
    if (!recipients.includes(tab) && tab !== 'ALL') setTab('ALL')
  }, [recipients, tab])

  useEffect(() => {
    if (!stale || !profile) return undefined

    let active = true
    setLoading(true)
    setError(null)

    runMatch(profile)
      .catch((matchError) => {
        if (active) setError(matchError.message || 'Не удалось выполнить подбор.')
      })
      .finally(() => {
        if (active) setLoading(false)
      })

    return () => {
      active = false
    }
  }, [stale, profile, runMatch])

  if (loading) {
    return (
      <div className="page">
        <div className="loading-card card">
          <div className="loader" />
          <strong>Подбираем меры поддержки</strong>
          <span>Проверяем условия доступных мер поддержки.</span>
        </div>
        {[0, 1, 2].map((index) => <div className="card skeleton" key={index} style={{ height: 150 }} />)}
      </div>
    )
  }

  if (error) {
    return (
      <div className="page">
        <EmptyState
          title="Не удалось выполнить подбор"
          text={error}
          action={(
            <div className="stack stack--compact">
              <Link className="btn btn--primary btn--lg" to="/survey">Проверить анкету</Link>
              <button className="btn btn--ghost btn--lg" type="button" onClick={() => window.location.reload()}>Повторить</button>
            </div>
          )}
        />
      </div>
    )
  }

  const list = tab === 'ALL'
    ? matched
    : matched.filter((measure) => (measure.recipient || 'FAMILY') === tab)

  return (
    <div className="page results-page">
      <section className="results-hero">
        <div className="eyebrow">Персональный подбор</div>
        <div className="results-hero__num">{matched.length}</div>
        <h1>{matched.length === 1 ? 'мера поддержки найдена' : 'мер поддержки найдено'}</h1>
        <p>Результат рассчитан по вашей анкете. Откройте меру, чтобы посмотреть условия и порядок оформления.</p>
      </section>

      <div className="result-tools">
        <Link className="btn btn--ghost btn--sm" to="/survey">Изменить ответы</Link>
        <Link className="btn btn--ghost btn--sm" to="/account">Открыть профиль</Link>
      </div>

      {matched.length > 0 && (
        <div className="tabs" role="tablist" aria-label="Получатель меры">
          <button className={`tab${tab === 'ALL' ? ' tab--on' : ''}`} type="button" onClick={() => setTab('ALL')} aria-pressed={tab === 'ALL'}>
            Все · {matched.length}
          </button>
          {recipients.map((recipient) => {
            const count = matched.filter((measure) => (measure.recipient || 'FAMILY') === recipient).length
            return (
              <button
                className={`tab${tab === recipient ? ' tab--on' : ''}`}
                type="button"
                key={recipient}
                onClick={() => setTab(recipient)}
                aria-pressed={tab === recipient}
              >
                {LABELS.recipient[recipient] || 'Семье'} · {count}
              </button>
            )
          })}
        </div>
      )}

      {matched.length === 0 ? (
        <EmptyState
          title="Подходящих мер пока не найдено"
          text="По текущим ответам подходящих мер не найдено. Проверьте регион, статус и данные о семье."
          action={<Link className="btn btn--primary" to="/survey">Изменить анкету</Link>}
        />
      ) : (
        <div className="stack">
          {list.map((measure) => <MeasureCard key={measure.id} measure={measure} />)}
        </div>
      )}

      <div className="disclaimer">Подбор носит справочный характер. Окончательное право и размер меры определяет уполномоченный орган.</div>
    </div>
  )
}
