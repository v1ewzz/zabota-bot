import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import MeasureCard from '../components/MeasureCard'
import { LABELS } from '../api/mockData'

export default function Results() {
  const { profile, results, runMatch } = useApp()
  const [loading, setLoading] = useState(false)
  const [tab, setTab] = useState('ALL')

  const version = useMemo(() => JSON.stringify(profile), [profile])
  const stale = !results || results.version !== version

  useEffect(() => {
    if (!stale) return
    let alive = true
    setLoading(true)
    runMatch(profile).finally(() => { if (alive) setLoading(false) })
    return () => { alive = false }
  }, [stale, profile, runMatch])

  if (loading) {
    return (
      <div className="page">
        <div className="banner banner--info">Сопоставляем вашу ситуацию с каталогом мер…</div>
        {[0, 1, 2].map((i) => <div key={i} className="card skeleton" style={{ height: 130 }} />)}
      </div>
    )
  }

  const matched = results?.matched || []
  const skipped = results?.skipped || []
  const recipients = ['USER', 'SPOUSE', 'CHILD', 'FAMILY'].filter((r) => matched.some((m) => m.recipient === r))
  const list = tab === 'ALL' ? matched : matched.filter((m) => m.recipient === tab)
  const missing = [...new Set(skipped.flatMap((s) => s.missing))]

  return (
    <div className="page">
      <div className="card summary">
        <div className="summary__num">{matched.length}</div>
        <div>мер поддержки найдено для вашей семьи</div>
        <div className="summary__sub">Все меры уже сохранены в кабинете — отмечайте статусы оформления там</div>
      </div>

      {missing.length > 0 && (
        <div className="banner banner--warn">
          ⚠️ Ещё {skipped.length} мер(ы) не проверено: не хватает данных — {missing.join(', ')}.
          <Link className="banner__link" to="/survey?step=6">Указать</Link>
        </div>
      )}

      <div className="banner banner--info">
        Демо-режим: данные каталога тестовые. Суммы и условия сверяйте с первоисточниками.
      </div>

      <div className="tabs">
        <button className={'tab' + (tab === 'ALL' ? ' tab--on' : '')} onClick={() => setTab('ALL')}>
          Все · {matched.length}
        </button>
        {recipients.map((r) => (
          <button key={r} className={'tab' + (tab === r ? ' tab--on' : '')} onClick={() => setTab(r)}>
            {LABELS.recipient[r]} · {matched.filter((m) => m.recipient === r).length}
          </button>
        ))}
      </div>

      <div className="stack">
        {list.map((m) => <MeasureCard key={m.id} m={m} />)}
        {list.length === 0 && <p className="p muted" style={{ textAlign: 'center' }}>В этой категории мер нет.</p>}
      </div>
    </div>
  )
}
