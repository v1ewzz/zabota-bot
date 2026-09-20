import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { LABELS } from '../api/mockData'
import { Money, Sheet, StatusSwitch } from '../components/ui'
import { formatDate } from '../utils'
import { openLink } from '../bridge/max'

export default function MeasureDetail() {
  const { id } = useParams()
  const nav = useNavigate()
  const { profile, results, support, updateSupport, requestMfc, showToast } = useApp()
  const [sheetOpen, setSheetOpen] = useState(false)

  // Данные обращения предзаполняются из анкеты (шаг «Ваши данные»)
  const [form, setForm] = useState(() => ({
    name: profile?.fullName || '',
    phone: profile?.phone || '',
    comment: '',
  }))

  const m = results?.matched.find((x) => x.id === id)

  if (!m) {
    return (
      <div className="page">
        <div className="card" style={{ textAlign: 'center', padding: 28 }}>
          <p className="p">Мера не найдена. Возможно, подбор устарел.</p>
          <button className="btn btn--primary" style={{ marginTop: 12 }} onClick={() => nav('/results')}>
            К результатам
          </button>
        </div>
      </div>
    )
  }

  const st = support[m.id]
  const status = st?.status || 'NOT_APPLIED'
  const prefilled = Boolean(profile?.fullName || profile?.phone)

  const toggleReminder = () => {
    const on = !st?.reminder
    updateSupport(m.id, { reminder: on })
    showToast(on ? 'Напомним за 7 дней до срока (демо)' : 'Напоминание отключено')
  }

  const apply = () => {
    if (m.channel === 'MFC' || !m.actionUrl) { setSheetOpen(true); return }
    openLink(m.actionUrl)
    if (status === 'NOT_APPLIED') {
      updateSupport(m.id, { status: 'SUBMITTED' })
      showToast('Отметили как «Подано» — статус можно изменить в кабинете')
    }
  }

  const submitMfc = async (e) => {
    e.preventDefault()
    try {
      await requestMfc({ measureId: m.id, measureName: m.name, ...form })
      setSheetOpen(false)
      showToast('Обращение сохранено. Специалист свяжется с вами')
      if (status === 'NOT_APPLIED') updateSupport(m.id, { status: 'SUBMITTED' })
    } catch (err) {
      showToast('Не удалось отправить обращение')
    }
  }

  return (
    <div className="page detail">
      <div className="mcard__top">
        <span className="chip chip--level">{LABELS.level[m.level]}</span>
        <span className="chip">{LABELS.supportType[m.supportType]}</span>
        <span className="chip chip--blue">{LABELS.recipient[m.recipient]}</span>
      </div>
      <h1 className="detail__h1">{m.name}</h1>

      <div className="card sec">
        <div className="sec__title">Что предоставляется</div>
        <p className="p">{m.description}</p>
        {m.amount != null
          ? <Money amount={m.amount} frequency={m.frequency} />
          : <p className="p muted">Размер — согласно НПА (см. «Основание»)</p>}
      </div>

      {m.reason && (
        <div className="card sec">
          <div className="sec__title">Почему подошла</div>
          <div className="chips">
            {m.reason.split(' · ').map((r, i) => <span className="chip" key={i}>{r}</span>)}
          </div>
        </div>
      )}

      <div className="card sec">
        <div className="sec__title">Статус оформления</div>
        <StatusSwitch value={status} onChange={(v) => updateSupport(m.id, { status: v })} />
        <button className="btn btn--ghost btn--sm" onClick={toggleReminder} style={{ alignSelf: 'flex-start' }}>
          {st?.reminder ? '🔔 Напоминание включено' : '🔕 Напомнить о сроке'}
        </button>
      </div>

      {m.documents?.length > 0 && (
        <div className="card sec">
          <div className="sec__title">Что подготовить</div>
          <ul className="docs">{m.documents.map((d) => <li key={d}>▢ {d}</li>)}</ul>
        </div>
      )}

      <div className="card sec">
        <div className="sec__title">Куда обращаться</div>
        <p className="p">{LABELS.channel[m.channel]}</p>
        {m.actionUrl && (
          <button className="btn btn--primary" onClick={() => openLink(m.actionUrl)}>
            Открыть {LABELS.channel[m.channel]}
          </button>
        )}
        {m.channel === 'MFC' && <p className="p muted">Потребуется личный визит: возьмите документы из списка выше.</p>}
      </div>

      {m.npa?.length > 0 && (
        <div className="card sec">
          <div className="sec__title">Основание</div>
          <div className="npa">
            {m.npa.map((n, i) => (
              <a key={i} href={n.url} onClick={(e) => { e.preventDefault(); openLink(n.url) }}>
                {n.name} ↗
              </a>
            ))}
          </div>
        </div>
      )}

      <div className="card sec">
        <div className="sec__title">Срок</div>
        <p className="p">{m.validTo ? `Подать до ${formatDate(m.validTo)}` : 'Бессрочно (пока действует мера)'}</p>
      </div>

      <p className="disclaimer">
        «Забота» не заменяет официальную проверку права на меру. Окончательное решение принимает ведомство.
      </p>

      <div className="actionbar">
        <button className="btn btn--primary" onClick={apply}>
          {m.channel === 'MFC' || !m.actionUrl ? 'Записаться в МФЦ' : 'Подать'}
        </button>
      </div>

      <Sheet open={sheetOpen} onClose={() => setSheetOpen(false)} title="Обращение к специалисту МФЦ">
        <form className="sheet__form" onSubmit={submitMfc}>
          <p className="p muted">По мере «{m.name}» (демо-сценарий: обращение сохраняется и обрабатывается вручную).</p>
          {prefilled && (
            <div className="banner banner--info">
              ФИО и телефон подставлены из вашей анкеты — проверьте и поправьте при необходимости.
            </div>
          )}
          <input className="input" required placeholder="ФИО" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
          <input className="input" required type="tel" placeholder="Телефон" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
          <textarea className="input" rows={3} placeholder="Вопрос (необязательно)" value={form.comment} onChange={(e) => setForm({ ...form, comment: e.target.value })} />
          <button className="btn btn--primary btn--lg" type="submit">Отправить обращение</button>
        </form>
      </Sheet>
    </div>
  )
}