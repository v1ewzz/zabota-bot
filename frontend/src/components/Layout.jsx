import { useEffect } from 'react'
import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import { useApp } from '../store/AppContext'
import { hideBackButton, isMax, showBackButton } from '../bridge/max'

const TITLES = {
  '/survey': 'Анкета',
  '/results': 'Результаты',
  '/account': 'Профиль',
  '/questionnaire': 'Анкета',
  '/print': 'Сводка',
}

function BackIcon() {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true">
      <path d="M19 12H5M11 18l-6-6 6-6" />
    </svg>
  )
}

function ResultsIcon() {
  return <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 5h16v14H4zM7.5 15l2.8-3 2.2 2 4-5" /></svg>
}

function AccountIcon() {
  return <svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="8" r="3.2" /><path d="M5.5 19c.8-3 3-4.7 6.5-4.7s5.7 1.7 6.5 4.7" /></svg>
}

function SurveyIcon() {
  return <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M6 4h12v16H6zM9 8h6M9 12h6M9 16h4" /></svg>
}

export default function Layout({ children }) {
  const { toast, profile } = useApp()
  const { pathname } = useLocation()
  const navigate = useNavigate()

  const isHome = pathname === '/'
  const isAccount = pathname === '/account'
  const title = TITLES[pathname] || 'Мера поддержки'
  const backTarget = pathname.startsWith('/measure/')
    ? '/results'
    : pathname === '/print' || pathname === '/questionnaire'
      ? '/account'
      : '/'
  const showBottomNav = Boolean(profile?.firstName && profile?.lastName)
    && ['/results', '/account', '/questionnaire'].includes(pathname)

  const handleBack = () => navigate(backTarget)

  useEffect(() => {
    window.scrollTo(0, 0)
  }, [pathname])

  useEffect(() => {
    if (!isMax() || isHome || isAccount) {
      hideBackButton()
      return undefined
    }

    showBackButton(handleBack)
    return () => hideBackButton(handleBack)
  }, [pathname, backTarget, isHome, isAccount])

  return (
    <div className={`app${showBottomNav ? ' app--with-nav' : ''}`}>
      {!isHome && !isAccount && (
        <header className="header">
          <button className="back-btn" type="button" onClick={handleBack} aria-label="Назад">
            <BackIcon />
          </button>
          <div className="header__title">{title}</div>
          <div className="header__spacer" aria-hidden="true" />
        </header>
      )}

      <main className="main">{children}</main>
      {toast && <div className="toast" role="status" key={toast.at}>{toast.text}</div>}

      {showBottomNav && (
        <nav className="nav" aria-label="Основная навигация">
          <NavLink to="/results" className={({ isActive }) => `nav__item${isActive ? ' nav__item--on' : ''}`}>
            <ResultsIcon />
            <span>Результаты</span>
          </NavLink>
          <NavLink to="/account" className={({ isActive }) => `nav__item${isActive ? ' nav__item--on' : ''}`}>
            <AccountIcon />
            <span>Профиль</span>
          </NavLink>
          <NavLink to="/survey" className={({ isActive }) => `nav__item${isActive ? ' nav__item--on' : ''}`}>
            <SurveyIcon />
            <span>Анкета</span>
          </NavLink>
        </nav>
      )}
    </div>
  )
}
