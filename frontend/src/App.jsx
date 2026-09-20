import { Routes, Route, Navigate } from 'react-router-dom'
import { AppProvider, useApp } from './store/AppContext'
import Layout from './components/Layout'
import Onboarding from './pages/Onboarding'
import Survey from './pages/Survey'
import Results from './pages/Results'
import MeasureDetail from './pages/MeasureDetail'
import Account from './pages/Account'
import Print from './pages/Print'

function Guard({ children }) {
  const { profile } = useApp()
  if (!profile) return <Navigate to="/" replace />
  return children
}

export default function App() {
  return (
    <AppProvider>
      <Layout>
        <Routes>
          <Route path="/" element={<Onboarding />} />
          <Route path="/survey" element={<Survey />} />
          <Route path="/results" element={<Guard><Results /></Guard>} />
          <Route path="/measure/:id" element={<Guard><MeasureDetail /></Guard>} />
          <Route path="/account" element={<Guard><Account /></Guard>} />
          <Route path="/print" element={<Guard><Print /></Guard>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </AppProvider>
  )
}
