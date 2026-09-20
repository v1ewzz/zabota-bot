import { LABELS } from '../api/mockData'

export function ProgressBar({ value, max }) {
  const pct = Math.round((value / max) * 100)
  return (
    <div className="progress" role="progressbar" aria-valuenow={pct}>
      <div className="progress__fill" style={{ width: `${pct}%` }} />
    </div>
  )
}

export function ChoiceGroup({ options, value, onChange }) {
  return (
    <div className="choice">
      {options.map((o) => (
        <button
          type="button"
          key={o.code}
          className={'choice__item' + (value === o.code ? ' on' : '')}
          onClick={() => onChange(o.code)}
        >
          <span className="choice__dot" />
          <span>{o.label}</span>
        </button>
      ))}
    </div>
  )
}

export function ToggleRow({ label, hint, checked, onChange }) {
  return (
    <div className="trow">
      <div>
        <div className="trow__label">{label}</div>
        {hint && <div className="trow__hint">{hint}</div>}
      </div>
      <label className="switch">
        <input type="checkbox" checked={!!checked} onChange={(e) => onChange(e.target.checked)} />
        <span className="switch__ui" />
      </label>
    </div>
  )
}

export function Select({ label, value, onChange, options, placeholder = 'Выберите…' }) {
  return (
    <label className="field">
      {label && <span>{label}</span>}
      <select className="select" value={value ?? ''} onChange={(e) => onChange(e.target.value)}>
        <option value="" disabled>{placeholder}</option>
        {options.map((o) => {
          const val = o.code ?? o.id
          const text = o.label ?? o.name
          return <option key={val} value={val}>{text}</option>
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
    <div className="status-switch">
      {STATUSES.map((s) => (
        <button key={s} type="button" className={value === s ? 'on' : ''} onClick={() => onChange(s)}>
          {LABELS.statusShort[s]}
        </button>
      ))}
    </div>
  )
}

export function Money({ amount, frequency }) {
  if (amount == null) return null
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
      <div className="sheet" onClick={(e) => e.stopPropagation()}>
        <div className="sheet__title">{title}</div>
        {children}
      </div>
    </div>
  )
}
