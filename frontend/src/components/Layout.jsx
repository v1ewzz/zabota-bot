import { useEffect } from 'react'
import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'

const TITLES = {
  '/survey': 'Анкета',
  '/results': 'Результаты',
  '/account': 'Личный кабинет',
  '/print': 'PDF-сводка',
}

const ICONS = {
  star: <svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 3.2l2.5 5.2 5.7.8-4.1 4 1 5.6L12 16.1l-5.1 2.7 1-5.6-4.1-4 5.7-.8z" /></svg>,
  list: <svg viewBox="0 0 24 24" fill="currentColor"><path d="M4 5.5h16v2.6H4zM4 10.7h16v2.6H4zM4 15.9h16v2.6H4z" /></svg>,
  pen: <svg viewBox="0 0 24 24" fill="currentColor"><path d="M3 17.2V21h3.8L17.9 9.9l-3.8-3.8L3 17.2zM20.7 7.1a1 1 0 000-1.4L18.3 3.3a1 1 0 00-1.4 0L15.1 5.1l3.8 3.8 1.8-1.8z" /></svg>,
}

const TABS = [
  { to: '/results', label: 'Результаты', icon: ICONS.star },
  { to: '/account', label: 'Кабинет', icon: ICONS.list },
  { to: '/survey', label: 'Анкета', icon: ICONS.pen },
]

export default function Layout({ children }) {
  const { toast } = useApp()
  const { pathname } = useLocation()
  const nav = useNavigate()

  const showNav = pathname === '/results' || pathname === '/account'
  const isHome = pathname === '/'
  const title = TITLES[pathname] || 'Мера поддержки'

  useEffect(() => { window.scrollTo(0, 0) }, [pathname])

  return (
    <div className="app">
      {!isHome && (
        <header className="header">
          {showNav ? <div style={{ width: 40 }} /> : <button className="icon-btn" onClick={() => nav(-1)} aria-label="Назад">←</button>}
          <div className="header__title">{title}</div>
          <div style={{ width: 40 }} />
        </header>
      )}
      <main style={{ flex: 1 }}>{children}</main>

      {toast && <div className="toast" key={toast.at}>{toast.text}</div>}

      {showNav && (
        <nav className="nav">
          {TABS.map((t) => (
            <NavLink key={t.to} to={t.to} className={({ isActive }) => 'nav__item' + (isActive ? ' nav__item--on' : '')}>
              {t.icon}
              <span>{t.label}</span>
            </NavLink>
          ))}
        </nav>
      )}
    </div>
  )
}
