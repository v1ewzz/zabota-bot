import { Navigate, Route, Routes } from 'react-router-dom'
import { AppProvider, useApp } from './store/AppContext'
import Layout from './components/Layout'
import Home from './pages/Home'
import Survey from './pages/Survey'
import Questionnaire from './pages/Questionnaire'
import Results from './pages/Results'
import MeasureDetail from './pages/MeasureDetail'
import Account from './pages/Account'
import Print from './pages/Print'

function hasIdentity(profile) {
  return Boolean(
    profile?.firstName?.trim()
      && profile?.lastName?.trim()
  )
}

function hasCompletedSurvey(profile) {
  return (
    hasIdentity(profile)
    && Boolean(profile.familyRelation)
    && Boolean(profile.militaryStatus)
    && Boolean(profile.region)
    && Boolean(profile.municipality)
    && Boolean(profile.incomeRange)
  )
}

function IdentityGuard({
  children,
}) {
  const { profile } =
    useApp()

  if (!hasIdentity(profile)) {
    return (
      <Navigate
        to="/"
        replace
      />
    )
  }

  return children
}

function SurveyGuard({
  children,
}) {
  const { profile } =
    useApp()

  if (
    !hasCompletedSurvey(
      profile
    )
  ) {
    return (
      <Navigate
        to="/survey"
        replace
      />
    )
  }

  return children
}

export default function App() {
  return (
    <AppProvider>
      <Layout>
        <Routes>
          <Route
            path="/"
            element={<Home />}
          />

          <Route
            path="/survey"
            element={
              <IdentityGuard>
                <Survey />
              </IdentityGuard>
            }
          />

          <Route
            path="/questionnaire"
            element={
              <IdentityGuard>
                <Questionnaire />
              </IdentityGuard>
            }
          />

          <Route
            path="/results"
            element={
              <SurveyGuard>
                <Results />
              </SurveyGuard>
            }
          />

          <Route
            path="/measure/:id"
            element={
              <SurveyGuard>
                <MeasureDetail />
              </SurveyGuard>
            }
          />

          <Route
            path="/account"
            element={
              <IdentityGuard>
                <Account />
              </IdentityGuard>
            }
          />

          <Route
            path="/print"
            element={
              <SurveyGuard>
                <Print />
              </SurveyGuard>
            }
          />

          <Route
            path="*"
            element={
              <Navigate
                to="/"
                replace
              />
            }
          />
        </Routes>
      </Layout>
    </AppProvider>
  )
}