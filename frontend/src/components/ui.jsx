import { LABELS } from '../api/mockData'

export function ProgressBar({ value, max }) {
  const safeMax = Math.max(1, Number(max) || 1)
  const pct = Math.min(100, Math.max(0, Math.round((Number(value) / safeMax) * 100)))

  return (
    <div
      className="progress"
      role="progressbar"
      aria-valuenow={pct}
      aria-valuemin="0"
      aria-valuemax="100"
    >
      <div className="progress__fill" style={{ width: `${pct}%` }} />
    </div>
  )
}

export function ChoiceGroup({ options, value, onChange }) {
  return (
    <div className="choice">
      {options.map((option) => (
        <button
          key={option.code}
          type="button"
          className={`choice__item${value === option.code ? ' on' : ''}`}
          onClick={() => !option.disabled && onChange(option.code)}
          disabled={Boolean(option.disabled)}
          aria-pressed={value === option.code}
          title={option.disabledHint || ''}
        >
          <span className="choice__dot" aria-hidden="true" />
          <span className="choice__copy">
            <span className="choice__label">{option.label}</span>
            {(option.hint || option.disabledHint) && (
              <span className="choice__hint">{option.disabled ? option.disabledHint : option.hint}</span>
            )}
          </span>
        </button>
      ))}
    </div>
  )
}

export function ToggleRow({ label, hint, checked, onChange }) {
  return (
    <div className="trow">
      <div className="trow__copy">
        <div className="trow__label">{label}</div>
        {hint && <div className="trow__hint">{hint}</div>}
      </div>
      <label className="switch">
        <input
          type="checkbox"
          checked={Boolean(checked)}
          onChange={(event) => onChange(event.target.checked)}
        />
        <span className="switch__ui" />
      </label>
    </div>
  )
}

export function Select({ label, value, onChange, options, placeholder = 'Выберите…' }) {
  return (
    <label className="field">
      {label && <span>{label}</span>}
      <select
        className="select"
        value={value ?? ''}
        onChange={(event) => onChange(event.target.value)}
      >
        <option value="" disabled>{placeholder}</option>
        {options.map((option) => {
          const val = option.code ?? option.id
          const text = option.label ?? option.name
          return (
            <option key={val} value={val} disabled={Boolean(option.disabled)}>
              {text}
            </option>
          )
        })}
      </select>
    </label>
  )
}

export function StatusBadge({ status }) {
  return <span className={`badge badge--${status}`}>{LABELS.status[status] || status}</span>
}

const STATUSES = ['NOT_APPLIED', 'SUBMITTED', 'APPROVED', 'RECEIVED']

export function StatusSwitch({ value, onChange }) {
  return (
    <div className="status-switch" role="group" aria-label="Статус меры">
      {STATUSES.map((status) => (
        <button
          key={status}
          type="button"
          className={value === status ? 'on' : ''}
          onClick={() => onChange(status)}
          aria-pressed={value === status}
        >
          {LABELS.statusShort[status]}
        </button>
      ))}
    </div>
  )
}

export function Money({ amount, frequency }) {
  if (amount == null) {
    return <div className="money money--muted">Размер зависит от условий меры</div>
  }

  return (
    <div className="money">
      {new Intl.NumberFormat('ru-RU').format(amount)} ₽
      {LABELS.frequency[frequency] ? ` ${LABELS.frequency[frequency]}` : ''}
    </div>
  )
}

export function Sheet({ open, onClose, title, children }) {
  if (!open) return null

  return (
    <div className="sheet-overlay" onClick={onClose}>
      <div
        className="sheet"
        role="dialog"
        aria-modal="true"
        aria-label={title}
        onClick={(event) => event.stopPropagation()}
      >
        <button className="sheet__close" type="button" onClick={onClose} aria-label="Закрыть">
          ×
        </button>
        <div className="sheet__title">{title}</div>
        {children}
      </div>
    </div>
  )
}

export function EmptyState({ title, text, action }) {
  return (
    <div className="empty card">
      <div className="empty__icon">—</div>
      <h3>{title}</h3>
      <p className="p muted">{text}</p>
      {action}
    </div>
  )
}
