import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { LABELS, MUNICIPALITIES, REGIONS } from '../api/mockData'
import { formatDate, money } from '../utils'
import { isMax, getInitDataUnsafe } from '../bridge/max'
import api from '../api'

export default function Print() {
  const { profile, results, support, showToast } = useApp()
  const [sending, setSending] = useState(false)
  const items = results?.matched || []
  const region = REGIONS.find((item) => item.id === profile?.region)?.name || '—'
  const city = MUNICIPALITIES[profile?.region]?.find((item) => item.id === profile?.municipality)?.name || ''
  const fullName = profile?.fullName || 'Пользователь'
  const runningInMax = isMax()

  // FIX (кнопка "Печать / сохранить PDF" не работает в MAX):
  // window.print() открывает системный диалог печати браузера, а WebView
  // мессенджера MAX его не показывает (или блокирует политикой embedded-
  // браузера) — поэтому кнопка выглядела "неработающей". Внутри MAX
  // сводка теперь честно отправляется в чат ботом через backend
  // (POST /api/users/{userId}/pdf-summary — см. backend/README), а не
  // через window.print(). Если backend ещё не поддерживает этот
  // эндпоинт — пользователь увидит понятную ошибку, а не тишину.
  const handlePrintOrSend = async () => {
    if (!runningInMax) {
      window.print()
      return
    }

    if (!profile?.backendUserId) {
      showToast('Сначала пройдите анкету и получите подбор мер, чтобы сформировать сводку.')
      return
    }

    setSending(true)

    try {
      const initData = getInitDataUnsafe()
      const maxUserId = initData?.user?.id ?? null

      await api.requestPdfSummary(profile.backendUserId, { maxUserId })

      showToast('PDF-сводка отправлена вам в чат MAX.')
    } catch (error) {
      console.error(error)
      showToast(
        error?.message || 'Не удалось отправить PDF в чат. Попробуйте открыть сводку в браузере и распечатать её оттуда.'
      )
    } finally {
      setSending(false)
    }
  }

  return (
    <div className="print">
      <div className="no-print print-actions">
        <button
          className="btn btn--primary"
          type="button"
          onClick={handlePrintOrSend}
          disabled={sending}
        >
          {sending
            ? 'Отправляем…'
            : runningInMax
              ? 'Отправить PDF в чат'
              : 'Печать / сохранить PDF'}
        </button>
        <Link className="btn btn--ghost" to="/account">В кабинет</Link>
      </div>

      <div className="pr-head">
        <div className="eyebrow">Забота</div>
        <h1>Персональный список мер поддержки</h1>
        <p className="pr-meta">{fullName} · {[city, region].filter(Boolean).join(', ') || '—'}</p>
        <p className="pr-meta">Сформировано {formatDate(new Date())}</p>
      </div>

      <div className="pr-list">
        {items.length === 0 ? (
          <p className="p muted">Подходящих мер пока нет.</p>
        ) : items.map((measure, index) => {
          const status = support[measure.id]?.status || 'NOT_APPLIED'
          return (
            <div className="pr-item" key={measure.id}>
              <div className="pr-item__num">{index + 1}</div>
              <div className="pr-item__body">
                <div className="pr-item__title">{measure.name}</div>
                <div className="pr-item__chips">
                  <span className="chip chip--blue">{LABELS.recipient[measure.recipient] || 'Семье'}</span>
                  <span className={`pr-status s-${status}`}>{LABELS.status[status] || status}</span>
                </div>
                <div className="pr-item__row">Размер: <b>{money(measure.amount)}</b>{measure.frequency && LABELS.frequency[measure.frequency] ? ` · ${LABELS.frequency[measure.frequency]}` : ''}</div>
              </div>
            </div>
          )
        })}
      </div>

      <p className="pr-foot">Сводка предназначена для навигации и не заменяет официальное решение органа, предоставляющего меру поддержки.</p>
    </div>
  )
}
