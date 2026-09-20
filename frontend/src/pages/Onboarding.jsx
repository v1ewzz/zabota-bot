import { useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'

export default function Onboarding() {
  const { consent, setConsent, profile, reset } = useApp()
  const nav = useNavigate()

  return (
    <div className="page onb">
      <div className="onb__hero">
        <div className="onb__logo">💜</div>
        <h1>Забота: Личный кабинет</h1>
        <p>Узнаем, какие меры поддержки положены вашей семье, что для них нужно и куда обращаться.</p>
      </div>

      <ul className="onb__list">
        <li>❓ Короткий опрос — до 3 минут</li>
        <li>📋 Персональный список мер: вам, военнослужащему и детям</li>
        <li>📁 Документы, сроки и кнопки подачи — в одном месте</li>
        <li>🔔 Напоминания о сроках и статусы оформления</li>
      </ul>

      <label className="consent">
        <input type="checkbox" checked={consent} onChange={(e) => setConsent(e.target.checked)} />
        <span>
          Я согласен(на) на обработку указанных данных для подбора мер поддержки (152-ФЗ).
          Данные используются только для этой цели и не передаются третьим лицам.
        </span>
      </label>

      <button className="btn btn--primary btn--lg" disabled={!consent} onClick={() => nav('/survey')}>
        {profile ? 'Пройти опрос заново' : 'Начать опрос'}
      </button>

      {profile && (
        <button className="btn btn--ghost btn--lg" onClick={() => nav('/account')}>
          Перейти в кабинет
        </button>
      )}

      <p className="disclaimer">
        Сервис не заменяет официальную проверку права на меры: окончательное решение принимают ведомства.
        В демо-режиме используются тестовые данные каталога.
      </p>

      {profile && (
        <button className="btn btn--danger" onClick={reset}>Сбросить данные и начать заново</button>
      )}
    </div>
  )
}
