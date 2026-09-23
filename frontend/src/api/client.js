const BASE = String(import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '')
const REQUEST_TIMEOUT_MS = 20_000

export async function apiFetch(path, options = {}) {
  const controller = new AbortController()
  const timer = setTimeout(() => controller.abort(), REQUEST_TIMEOUT_MS)

  try {
    const response = await fetch(`${BASE}/api${path}`, {
      ...options,
      signal: options.signal || controller.signal,
      headers: {
        Accept: 'application/json',
        ...(options.body ? { 'Content-Type': 'application/json' } : {}),
        ...(options.headers || {}),
      },
    })

    const text = await response.text().catch(() => '')

    if (!response.ok) {
      let message = text || response.statusText || 'Неизвестная ошибка сервера'

      try {
        const data = JSON.parse(text)
        message = data.message || data.error || message
      } catch {}

      throw new Error(`Сервер вернул ошибку ${response.status}: ${message}`)
    }

    if (response.status === 204 || !text) return null

    try {
      return JSON.parse(text)
    } catch {
      throw new Error('Сервер вернул некорректный JSON-ответ.')
    }
  } catch (error) {
    if (error?.name === 'AbortError') {
      throw new Error('Сервер не ответил вовремя. Проверьте, что backend запущен на порту 8080.')
    }

    if (error instanceof TypeError) {
      throw new Error('Не удалось подключиться к backend. Проверьте, что backend запущен и доступен.')
    }

    throw error
  } finally {
    clearTimeout(timer)
  }
}
