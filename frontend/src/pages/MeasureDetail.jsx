import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { LABELS } from '../api/mockData'
import { Money, Sheet, StatusSwitch } from '../components/ui'
import { formatDate } from '../utils'
import { openLink } from '../bridge/max'

const CHANNEL_HELP = {
  GOSUSLUGI: 'Заявление подаётся онлайн через Госуслуги.',
  SFR: 'Обратитесь в Социальный фонд России по способу оформления, указанному для этой меры.',
  MFC: 'Обратитесь лично в МФЦ с документами из списка выше.',
  SCHOOL: 'Обратитесь в школу или детский сад, который посещает ребёнок. Универсальной ссылки для этого канала нет.',
  EDUCATIONAL_ORGANIZATION: 'Обратитесь в образовательную организацию, где учится ребёнок. Универсальной ссылки для этого канала нет.',
  FNS: 'Оформление проводится через налоговый орган или личный кабинет ФНС.',
  BANK: 'Уточните порядок оформления непосредственно в банке.',
}

export default function MeasureDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { results, support, updateSupport, showToast } = useApp()
  const [sheetOpen, setSheetOpen] = useState(false)

  const measure = results?.matched?.find((item) => String(item.id) === String(id))

  useEffect(() => {
    if (!measure) return undefined

    const handleEscape = (event) => {
      if (event.key === 'Escape') setSheetOpen(false)
    }

    window.addEventListener('keydown', handleEscape)
    return () => window.removeEventListener('keydown', handleEscape)
  }, [measure])

  if (!measure) {
    return (
      <div className="page">
        <div className="empty card">
          <h3>Мера не найдена</h3>
          <p className="p muted">Вернитесь к результатам и выполните подбор ещё раз.</p>
          <button className="btn btn--primary" type="button" onClick={() => navigate('/results')}>К результатам</button>
        </div>
      </div>
    )
  }

  const state = support[measure.id] || { status: 'NOT_APPLIED', reminder: false }
  const channelLabel = LABELS.channel[measure.channel] || measure.channel || 'Официальный канал'
  const channelHelp = CHANNEL_HELP[measure.channel] || 'Уточните способ оформления по условиям конкретной меры.'

  const apply = () => {
    if (measure.actionUrl) {
      const opened = openLink(measure.actionUrl)
      if (opened) {
        showToast('Открыли официальный источник. Перед обращением проверьте актуальные условия.')
      } else {
        showToast('Не удалось открыть официальный источник. Попробуйте открыть ссылку ещё раз.')
      }
      return
    }

    setSheetOpen(true)
  }

  return (
    <div className="page detail">
      <div className="mcard__top">
        {measure.level && <span className="chip chip--level">{LABELS.level[measure.level] || measure.level}</span>}
        {measure.supportType && <span className="chip">{LABELS.supportType[measure.supportType] || measure.supportType}</span>}
        <span className="chip chip--blue">{LABELS.recipient[measure.recipient] || 'Семье'}</span>
      </div>

      <h1 className="detail__h1">{measure.name}</h1>

      <div className="card sec">
        <div className="sec__title">Что предоставляется</div>
        <p className="p">{measure.description}</p>
        <Money amount={measure.amount} frequency={measure.frequency} />
      </div>

      <div className="card sec">
        <div className="sec__title">Куда обращаться</div>
        <div className="route-card">
          <div className="route-card__icon">↗</div>
          <div>
            <strong>{channelLabel}</strong>
            <span>{channelHelp}</span>
          </div>
        </div>
      </div>

      {measure.documents?.length > 0 && (
        <div className="card sec">
          <div className="sec__title">Что подготовить</div>
          <ul className="docs">
            {measure.documents.map((document) => (
              <li key={document}><span className="check-box">□</span>{document}</li>
            ))}
          </ul>
        </div>
      )}

      {measure.npa?.length > 0 && (
        <div className="card sec">
          <div className="sec__title">Основание</div>
          <div className="npa">
            {measure.npa.map((item, index) => (
              <button
                className="link-row"
                type="button"
                key={`${item.name}-${index}`}
                onClick={() => {
                  if (!openLink(item.url)) showToast('Ссылка на источник недоступна.')
                }}
              >
                {item.name}
                <span>↗</span>
              </button>
            ))}
          </div>
        </div>
      )}

      {measure.validTo && (
        <div className="card sec">
          <div className="sec__title">Срок</div>
          <p className="p">Подать до {formatDate(measure.validTo)}</p>
        </div>
      )}

      <div className="card sec">
        <div className="sec__title">Статус в кабинете</div>
        <StatusSwitch value={state.status} onChange={(value) => updateSupport(measure.id, { status: value })} />
        <button
          className="btn btn--ghost btn--sm"
          type="button"
          onClick={() => updateSupport(measure.id, { reminder: !state.reminder })}
        >
          {state.reminder ? 'Напоминание включено' : 'Напомнить о сроке'}
        </button>
      </div>

      <p className="disclaimer">Окончательное решение о предоставлении меры принимает уполномоченный орган.</p>

      <div className="actionbar">
        <button className="btn btn--primary" type="button" onClick={apply}>
          {measure.actionUrl ? 'Открыть официальный источник' : 'Как оформить'}
        </button>
      </div>

      <Sheet open={sheetOpen} onClose={() => setSheetOpen(false)} title="Как оформить">
        <div className="sheet__form">
          <p className="p">Документы и заявление подаются по официальному каналу. Подготовьте документы из списка выше и следуйте инструкции для выбранной организации.</p>
          <ol className="steps-list">
            <li>Подготовьте документы, перечисленные в карточке меры.</li>
            <li>{channelHelp}</li>
            <li>После подачи вернитесь в профиль и установите актуальный статус меры.</li>
          </ol>
          <button className="btn btn--primary btn--lg" type="button" onClick={() => setSheetOpen(false)}>Понятно</button>
        </div>
      </Sheet>
    </div>
  )
}
