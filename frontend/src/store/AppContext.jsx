import { createContext, useCallback, useContext, useEffect, useState } from 'react'
import api from '../api'
import { haptic } from '../bridge/max'

const AppCtx = createContext(null)
export const useApp = () => useContext(AppCtx)

const KEY = (k) => 'zabota:' + k
function load(k, fallback) {
  try { const v = localStorage.getItem(KEY(k)); return v ? JSON.parse(v) : fallback } catch { return fallback }
}
function persist(k, v) { try { localStorage.setItem(KEY(k), JSON.stringify(v)) } catch {} }

export function AppProvider({ children }) {
  const [consent, setConsentState] = useState(() => load('consent', false))
  const [profile, setProfile] = useState(() => load('profile', null))
  const [results, setResults] = useState(() => load('results', null))
  const [support, setSupport] = useState(() => load('support', {}))
  const [toast, setToast] = useState(null)

  useEffect(() => persist('consent', consent), [consent])
  useEffect(() => persist('profile', profile), [profile])
  useEffect(() => persist('results', results), [results])
  useEffect(() => persist('support', support), [support])

  useEffect(() => {
    if (!toast) return
    const t = setTimeout(() => setToast(null), 3400)
    return () => clearTimeout(t)
  }, [toast])

  const showToast = useCallback((text) => setToast({ text, at: Date.now() }), [])

  const syncSupport = useCallback((matched) => {
    setSupport((prev) => {
      const next = { ...prev }
      for (const m of matched) {
        if (!next[m.id]) next[m.id] = { status: 'NOT_APPLIED', reminder: false, updatedAt: Date.now() }
      }
      return next
    })
  }, [])

  const runMatch = useCallback(async (p) => {
    const res = await api.runMatch(p)
    setResults({ version: JSON.stringify(p), ...res, at: Date.now() })
    syncSupport(res.matched)
    return res
  }, [syncSupport])

  const updateSupport = useCallback((id, patch) => {
    setSupport((prev) => ({
      ...prev,
      [id]: { ...(prev[id] || { status: 'NOT_APPLIED', reminder: false }), ...patch, updatedAt: Date.now() },
    }))
    haptic('light')
  }, [])

  const requestMfc = useCallback((payload) => api.requestMfc(payload), [])
  const setConsent = useCallback((v) => setConsentState(v), [])
  const saveProfile = useCallback((p) => setProfile(p), [])

  const reset = useCallback(() => {
    ;['consent', 'profile', 'results', 'support'].forEach((k) => localStorage.removeItem(KEY(k)))
    setConsentState(false); setProfile(null); setResults(null); setSupport({})
  }, [])

  return (
    <AppCtx.Provider value={{
      consent, setConsent, profile, saveProfile,
      results, runMatch, support, updateSupport, requestMfc,
      reset, toast, showToast,
    }}>
      {children}
    </AppCtx.Provider>
  )
}
