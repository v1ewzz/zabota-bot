import { useNavigate } from 'react-router-dom'
import { LABELS } from '../api/mockData'
import { Money } from './ui'

export default function MeasureCard({ measure }) {
  const nav = useNavigate()
  return (
    <button type="button" className="mcard card" onClick={() => nav(`/measure/${measure.id}`)}>
      <div className="mcard__top">
        {measure.level && <span className="chip chip--level">{LABELS.level[measure.level] || measure.level}</span>}
        {measure.supportType && <span className="chip">{LABELS.supportType[measure.supportType] || measure.supportType}</span>}
        <span className="chip chip--blue">{LABELS.recipient[measure.recipient] || measure.recipient || 'Семье'}</span>
      </div>
      <h3 className="mcard__title">{measure.name}</h3>
      <p className="mcard__description">{measure.description}</p>
      <Money amount={measure.amount} frequency={measure.frequency} />
      <div className="mcard__foot">
        <span>{measure.channel ? `Куда: ${LABELS.channel[measure.channel] || measure.channel}` : 'Подробнее'}</span>
        <span className="arrow">→</span>
      </div>
    </button>
  )
}
