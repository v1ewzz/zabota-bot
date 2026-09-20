import { useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { LABELS } from '../api/mockData'
import { Money, StatusBadge } from './ui'

export default function MeasureCard({ m }) {
  const nav = useNavigate()
  const { support } = useApp()
  const st = support[m.id]

  return (
    <article className="card mcard" onClick={() => nav(`/measure/${m.id}`)}>
      <div className="mcard__top">
        <span className="chip chip--level">{LABELS.level[m.level]}</span>
        <span className="chip">{LABELS.supportType[m.supportType]}</span>
        {st && <StatusBadge status={st.status} />}
      </div>
      <h3 className="mcard__title">{m.name}</h3>
      <Money amount={m.amount} frequency={m.frequency} />
      {m.reason && <div className="mcard__reason">Подходит: {m.reason}</div>}
      <div className="mcard__foot">
        <button className="btn btn--ghost btn--sm" onClick={(e) => { e.stopPropagation(); nav(`/measure/${m.id}`) }}>Подробнее</button>
        {m.applicationRequired && (m.actionUrl || m.channel === 'MFC') && (
          <button className="btn btn--primary btn--sm" onClick={(e) => { e.stopPropagation(); nav(`/measure/${m.id}`) }}>
            {m.channel === 'MFC' ? 'Как подать' : 'Подать'}
          </button>
        )}
      </div>
    </article>
  )
}
