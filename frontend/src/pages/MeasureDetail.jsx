import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { LABELS } from '../api/mockData'
import { Money, Sheet, StatusSwitch } from '../components/ui'
import { formatDate } from '../utils'
import { openLink } from '../bridge/max'

const CHANNEL_LABELS = {
  GOSUSLUGI: 'Госуслуги',
  SFR: 'Социальный фонд России',
  MFC: 'МФЦ',
  SCHOOL: 'Школа / детский сад',
  EDUCATIONAL_ORGANIZATION: 'Образовательная организация',
  FNS: 'ФНС России',
  BANK: 'Банк',
  SOCIAL_PROTECTION: 'Орган социальной защиты',
  MILITARY_UNIT: 'Воинская часть',
  MILITARY_COMMISSARIAT: 'Военкомат',
  UNIVERSITY: 'Университет',
  EMPLOYMENT_SERVICE: 'Служба занятости',
  CULTURE_INSTITUTION: 'Учреждение культуры',
  SOCIAL_SERVICE_ORGANIZATION: 'Организация социального обслуживания',
  CREDITOR: 'Кредитор / банк / МФО',
  HOUSING_AUTHORITY: 'Жилищный орган',
  EMPLOYER: 'Работодатель',
}

const CHANNEL_HELP = {
  GOSUSLUGI: 'Заявление можно подать через Госуслуги.',
  SFR: 'Обратитесь в Социальный фонд России по указанному порядку оформления.',
  MFC: 'Обратитесь лично в МФЦ с необходимыми документами.',
  SCHOOL: 'Обратитесь в школу или детский сад.',
  EDUCATIONAL_ORGANIZATION: 'Обратитесь в образовательную организацию.',
  FNS: 'Оформление проводится через налоговый орган или личный кабинет ФНС.',
  BANK: 'Уточните порядок оформления непосредственно в банке.',
}

const LABEL_ALIASES = [
  { key: 'what', labels: ['Что даёт', 'Что дает', 'Что предоставляется'] },
  { key: 'who', labels: ['Кто получает', 'Получатели'] },
  {
    key: 'criteria',
    labels: ['Основные критерии для алгоритма', 'Критерии из источника', 'Условия получения', 'Условия'],
  },
  { key: 'amountText', labels: ['Размер / вид поддержки', 'Размер', 'Размер выплаты'] },
  { key: 'application', labels: ['Заявление', 'Подача заявления'] },
  { key: 'where', labels: ['Куда обращаться', 'Куда обратиться'] },
]

const LABEL_PATTERN_RE = new RegExp(
  `(${LABEL_ALIASES
    .flatMap((group) => group.labels)
    .sort((a, b) => b.length - a.length)
    .map((label) => label.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'))
    .join('|')})\\s*:`,
  'gi'
)

function normalizeText(value) {
  if (typeof value !== 'string') return ''

  return value
    .replace(/\$z\$/g, '')
    .replace(/\r\n/g, '\n')
    .replace(/[ \t]+/g, ' ')
    .trim()
}

function findLabelInfo(label) {
  const normalized = label.trim().toLowerCase()

  for (const group of LABEL_ALIASES) {
    for (const alias of group.labels) {
      if (alias.toLowerCase() === normalized) return group
    }
  }

  return null
}

function parseDescription(value) {
  const text = normalizeText(value)

  const result = {
    what: '',
    who: '',
    criteria: '',
    amountText: '',
    application: '',
    where: '',
  }

  if (!text) return result

  const matches = [...text.matchAll(LABEL_PATTERN_RE)]

  if (matches.length === 0) {
    result.what = text
    return result
  }

  const textBeforeFirstLabel = text
    .slice(0, matches[0].index)
    .trim()
    .replace(/^[,;.\s]+|[,;.\s]+$/g, '')

  if (textBeforeFirstLabel) {
    result.what = textBeforeFirstLabel
  }

  for (let index = 0; index < matches.length; index += 1) {
    const match = matches[index]
    const label = match[1]
    const group = findLabelInfo(label)

    if (!group) continue

    const start = match.index + match[0].length
    const end = index + 1 < matches.length ? matches[index + 1].index : text.length

    const content = text
      .slice(start, end)
      .replace(/^[,;.\s]+|[,;.\s]+$/g, '')
      .trim()

    if (!content) continue

    if (result[group.key]) {
      result[group.key] = `${result[group.key]}; ${content}`
    } else {
      result[group.key] = content
    }
  }

  return result
}

function splitItems(value) {
  if (!value) return []

  return value
    .split(/(?:\r?\n)+|;\s*/)
    .map((item) => item.replace(/^[•●▪◦\-]+\s*/, '').trim())
    .filter(Boolean)
}

function uniqueItems(items) {
  return [...new Set(items.filter(Boolean))]
}

function getStructuredDescription(measure) {
  const raw = normalizeText(measure?.rawDescription || measure?.sourceDescription || '')
  const parsedRaw = parseDescription(raw)
  const existing = measure?.descriptionParts || {}
  const parsedDescription = parseDescription(measure?.description)

  return {
    what: parsedRaw.what || existing.what || parsedDescription.what || '',
    who: parsedRaw.who || existing.who || parsedDescription.who || '',
    criteria: parsedRaw.criteria || existing.criteria || parsedDescription.criteria || '',
    amountText: parsedRaw.amountText || existing.amountText || parsedDescription.amountText || '',
    application: parsedRaw.application || existing.application || parsedDescription.application || '',
    where: parsedRaw.where || existing.where || parsedDescription.where || '',
  }
}

function DetailBlock({ title, children }) {
  return (
    <div className="card sec">
      <div className="sec__title">{title}</div>
      {children}
    </div>
  )
}

export default function MeasureDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { results, support, updateSupport, showToast } = useApp()
  const [sheetOpen, setSheetOpen] = useState(false)

  const measure = results?.matched?.find((item) => String(item.id) === String(id))

  const details = useMemo(() => getStructuredDescription(measure), [measure])
  const conditions = useMemo(() => splitItems(details.criteria), [details.criteria])
  const amountDetails = useMemo(() => splitItems(details.amountText), [details.amountText])
  const whereItems = useMemo(
    () => uniqueItems(details.where ? details.where.split(/\s*\/\s*/).map((item) => item.trim()) : []),
    [details.where]
  )

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
          <button className="btn btn--primary" type="button" onClick={() => navigate('/results')}>
            К результатам
          </button>
        </div>
      </div>
    )
  }

  const state = support[measure.id] || { status: 'NOT_APPLIED', reminder: false }

  const channelLabel = CHANNEL_LABELS[measure.channel]
    || measure.channelLabel
    || LABELS.channel[measure.channel]
    || measure.channel
    || 'Официальный канал'

  const channelHelp = CHANNEL_HELP[measure.channel]
    || 'Уточните порядок оформления по условиям конкретной меры.'

  const apply = () => {
    if (measure.actionUrl) {
      const opened = openLink(measure.actionUrl)

      if (opened) {
        showToast('Открыли официальный источник. Перед обращением проверьте актуальные условия.')
      } else {
        showToast('Не удалось открыть официальный источник.')
      }

      return
    }

    setSheetOpen(true)
  }

  return (
    <div className="page detail">
      <div className="mcard__top detail__chips">
        {measure.level && (
          <span className="chip chip--level">{LABELS.level[measure.level] || measure.level}</span>
        )}

        {measure.supportType && (
          <span className="chip">{LABELS.supportType[measure.supportType] || measure.supportTypeLabel || measure.supportType}</span>
        )}

        <span className="chip chip--blue">
          {LABELS.recipient[measure.recipient] || measure.recipient || 'Семье'}
        </span>
      </div>

      <h1 className="detail__h1">{measure.name}</h1>

      {details.what && (
        <DetailBlock title="Что предоставляется">
          <div className="detail-copy">{details.what}</div>
        </DetailBlock>
      )}

      {(measure.amount != null || amountDetails.length > 0) && (
        <DetailBlock title="Размер">
          {measure.amount != null && (
            <Money amount={measure.amount} frequency={measure.frequency} />
          )}

          {amountDetails.length > 0 && (
            <div className="ptiles" style={{ marginTop: measure.amount != null ? '8px' : '0' }}>
              {amountDetails.map((item, index) => (
                <div className="ptile" key={`${item}-${index}`}>
                  <span>Размер / вид поддержки</span>
                  <b>{item}</b>
                </div>
              ))}
            </div>
          )}
        </DetailBlock>
      )}

      {(details.who || conditions.length > 0) && (
        <DetailBlock title="Условия получения">
          <div className="ptiles">
            {details.who && (
              <div className="ptile questionnaire-wide">
                <span>Кому положено</span>
                <b>{details.who}</b>
              </div>
            )}
          </div>

          {conditions.length > 0 && (
            <ul className="docs">
              {conditions.map((condition, index) => (
                <li key={`${condition}-${index}`}>
                  <span className="check-box">•</span>
                  <span>{condition}</span>
                </li>
              ))}
            </ul>
          )}
        </DetailBlock>
      )}

      <DetailBlock title="Куда обращаться">
        <div className="route-card">
          <div className="route-card__icon">↗</div>
          <div>
            <strong>{channelLabel}</strong>
            <span>{whereItems.length > 0 ? whereItems.join(' · ') : channelHelp}</span>
          </div>
        </div>
      </DetailBlock>

      {details.application && (
        <DetailBlock title="Подача заявления">
          <div className="detail-copy">{details.application}</div>
        </DetailBlock>
      )}

      {measure.documents?.length > 0 && (
        <DetailBlock title="Что подготовить">
          <ul className="docs">
            {measure.documents.map((document) => (
              <li key={document}>
                <span className="check-box">□</span>
                <span>{document}</span>
              </li>
            ))}
          </ul>
        </DetailBlock>
      )}

      {measure.npa?.length > 0 && (
        <DetailBlock title="Правовое основание">
          <div className="npa">
            {measure.npa.map((item, index) => (
              <button
                className="link-row"
                type="button"
                key={`${item.name}-${index}`}
                onClick={() => {
                  if (!openLink(item.url)) {
                    showToast('Ссылка на источник недоступна.')
                  }
                }}
              >
                <span>{item.name}</span>
                <span>↗</span>
              </button>
            ))}
          </div>
        </DetailBlock>
      )}

      {measure.validTo && (
        <DetailBlock title="Срок">
          <p className="p">Подать до {formatDate(measure.validTo)}</p>
        </DetailBlock>
      )}

      <DetailBlock title="Статус в кабинете">
        <StatusSwitch
          value={state.status}
          onChange={(value) => updateSupport(measure.id, { status: value })}
        />

        <button
          className="btn btn--ghost btn--sm"
          type="button"
          onClick={() => updateSupport(measure.id, { reminder: !state.reminder })}
        >
          {state.reminder ? 'Напоминание включено' : 'Напомнить о сроке'}
        </button>
      </DetailBlock>

      <p className="disclaimer">
        Окончательное решение о предоставлении меры принимает уполномоченный орган.
      </p>

      <div className="actionbar">
        <button className="btn btn--primary" type="button" onClick={apply}>
          {measure.actionUrl ? 'Открыть официальный источник' : 'Как оформить'}
        </button>
      </div>

      <Sheet open={sheetOpen} onClose={() => setSheetOpen(false)} title="Как оформить">
        <div className="sheet__form">
          <p className="p">
            Документы и заявление подаются по официальному каналу.
            Подготовьте документы из списка выше и следуйте инструкции.
          </p>

          <ol className="steps-list">
            <li>Подготовьте документы, перечисленные в карточке меры.</li>
            <li>{details.where || channelHelp}</li>
            <li>После подачи вернитесь в профиль и установите актуальный статус меры.</li>
          </ol>

          <button className="btn btn--primary btn--lg" type="button" onClick={() => setSheetOpen(false)}>
            Понятно
          </button>
        </div>
      </Sheet>
    </div>
  )
}
