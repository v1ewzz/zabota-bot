import { useNavigate } from 'react-router-dom'
import { LABELS } from '../api/mockData'
import { Money, StatusBadge } from './ui'

const CHANNEL_LABELS = {
  GOSUSLUGI: 'Госуслуги',
  SFR: 'Социальный фонд России',
  MFC: 'МФЦ',
  SCHOOL: 'Школа / детский сад',
  EDUCATIONAL_ORGANIZATION: 'Образовательная организация',
  FNS: 'ФНС России',
  BANK: 'Банк',
  SOCIAL_PROTECTION: 'Орган социальной защиты',
  EMPLOYMENT_SERVICE: 'Служба занятости',
  EMPLOYMENT_CENTER: 'Центр занятости',
  CULTURE_INSTITUTION: 'Учреждение культуры',
  SOCIAL_SERVICE_ORGANIZATION: 'Организация социального обслуживания',
  CREDITOR: 'Кредитор / банк / МФО',
  HOUSING_AUTHORITY: 'Уполномоченный жилищный орган',
  EMPLOYER: 'Работодатель',
}

function channelLabel(measure) {
  return CHANNEL_LABELS[measure.channel]
    || measure.channelLabel
    || measure.channel
    || 'Не указан'
}

function pluralDocuments(count) {
  if (count % 100 >= 11 && count % 100 <= 19) return 'документов'
  const last = count % 10
  if (last === 1) return 'документ'
  if (last >= 2 && last <= 4) return 'документа'
  return 'документов'
}

export default function MeasureCard({ measure }) {
  const nav = useNavigate()
  const recipient = LABELS.recipient[measure.recipient] || measure.recipientLabel || measure.recipient || 'Семье'
  const status = measure.status || 'NOT_APPLIED'

  return (
    <button type="button" className="mcard card" onClick={() => nav(`/measure/${measure.id}`)}>
      <div className="mcard__top">
        {measure.level && <span className="chip chip--level">{LABELS.level[measure.level] || measure.levelLabel || measure.level}</span>}
        {measure.supportType && <span className="chip">{LABELS.supportType[measure.supportType] || measure.supportTypeLabel || measure.supportType}</span>}
        <span className="chip chip--blue">{recipient}</span>
      </div>

      <h3 className="mcard__title">{measure.name}</h3>
      <p className="mcard__description">{measure.description || 'Описание меры поддержки не указано.'}</p>

      <div
        aria-label="Основная информация о мере"
        style={{ display: 'grid', gap: 7, padding: 10, borderRadius: 13, background: '#f7f8fc' }}
      >
        <div style={{ display: 'flex', justifyContent: 'space-between', gap: 10, alignItems: 'flex-start' }}>
          <span className="small-note">Размер</span>
          <span style={{ textAlign: 'right' }}><Money amount={measure.amount} frequency={measure.frequency} /></span>
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between', gap: 10, alignItems: 'flex-start' }}>
          <span className="small-note">Куда обращаться</span>
          <strong style={{ textAlign: 'right', fontSize: 12.5, lineHeight: 1.35 }}>{channelLabel(measure)}</strong>
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between', gap: 10, alignItems: 'center' }}>
          <span className="small-note">Статус</span>
          <StatusBadge status={status} />
        </div>
        {measure.documents?.length > 0 && (
          <div style={{ display: 'flex', justifyContent: 'space-between', gap: 10, alignItems: 'center' }}>
            <span className="small-note">Документы</span>
            <strong style={{ fontSize: 12.5 }}>{measure.documents.length} {pluralDocuments(measure.documents.length)}</strong>
          </div>
        )}
      </div>

      <div className="mcard__foot">
        <span>Подробнее о мере</span>
        <span className="arrow">→</span>
      </div>
    </button>
  )
}
