export function calcAge(dateString, now = new Date()) {
  if (!dateString) return null

  const d = new Date(`${dateString}T00:00:00`)
  if (Number.isNaN(d.getTime())) return null

  let age = now.getFullYear() - d.getFullYear()
  const monthDiff = now.getMonth() - d.getMonth()

  if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < d.getDate())) {
    age--
  }

  return age >= 0 ? age : null
}

export function todayStr(date = new Date()) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

export function plural(n, forms) {
  const value = Math.abs(n) % 100
  const last = value % 10

  if (value > 10 && value < 20) return forms[2]
  if (last === 1) return forms[0]
  if (last >= 2 && last <= 4) return forms[1]
  return forms[2]
}

export function formatDate(value) {
  if (!value) return '—'

  if (typeof value === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(value)) {
    const [year, month, day] = value.split('-').map(Number)
    return new Intl.DateTimeFormat('ru-RU').format(new Date(year, month - 1, day))
  }

  const date = value instanceof Date ? value : new Date(value)
  if (Number.isNaN(date.getTime())) return '—'

  return new Intl.DateTimeFormat('ru-RU').format(date)
}

export function money(value) {
  if (value == null || value === '') return '—'

  const numeric = Number(value)
  if (!Number.isFinite(numeric)) return '—'

  return `${new Intl.NumberFormat('ru-RU').format(numeric)} ₽`
}
