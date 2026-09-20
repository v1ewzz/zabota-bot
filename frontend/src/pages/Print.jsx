import { Link } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { LABELS, MUNICIPALITIES, REGIONS } from '../api/mockData'
import { formatDate, money } from '../utils'

export default function Print() {
  const { profile, results, support } = useApp()
  const items = results?.matched || []
  const region = REGIONS.find((r) => r.id === profile?.region)?.name || '—'
  const city = profile?.region
    ? MUNICIPALITIES[profile.region]?.find((m) => m.id === profile.municipality)?.name
    : ''

  return (
    <div className="print">
      <div className="no-print" style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
        <button className="btn btn--primary" onClick={() => window.print()}>Печать / Сохранить в PDF</button>
        <Link to="/account"><button className="btn btn--ghost">Назад</button></Link>
      </div>

      <div className="pr-head">
        <h1>Забота: персональный список мер поддержки</h1>
        <p className="pr-meta">
          {[city, region].filter(Boolean).join(', ') || '—'} · сформировано {formatDate(new Date())} · всего мер: {items.length}
        </p>
      </div>

      <div className="pr-list">
        {items.map((m, i) => {
          const st = support[m.id]?.status || 'NOT_APPLIED'
          return (
            <div className="pr-item" key={m.id}>
              <div className="pr-item__num">{i + 1}</div>
              <div className="pr-item__body">
                <div className="pr-item__title">{m.name}</div>
                <div className="pr-item__chips">
                  <span className="chip chip--blue">{LABELS.recipient[m.recipient]}</span>
                  <span className="chip">{LABELS.level[m.level]}</span>
                  <span className={'pr-status s-' + st}>{LABELS.status[st]}</span>
                </div>
                <div className="pr-item__row">
                  {m.amount != null && (
                    <b>{money(m.amount)} ₽{LABELS.frequency[m.frequency] ? ' ' + LABELS.frequency[m.frequency] : ''} · </b>
                  )}
                  {LABELS.channel[m.channel]}
                </div>
                {m.documents?.length > 0 && (
                  <div className="pr-item__docs">
                    <span>Документы: </span>{m.documents.join('; ')}
                  </div>
                )}
              </div>
            </div>
          )
        })}
      </div>

      <p className="pr-foot">
        Демо-данные. «Забота» не заменяет официальную проверку права на меры;
        суммы и условия сверяйте с первоисточниками.
      </p>
    </div>
  )
}