import { createContext, useCallback, useContext, useEffect, useState } from 'react'
import api from '../api'
import { haptic } from '../bridge/max'

const AppCtx = createContext(null)
export const useApp = () => useContext(AppCtx)
const KEY = (key) => `zabota:v4:${key}`
const LEGACY_KEY = (key) => `zabota:v3:${key}`

function load(key, fallback) {
  try {
    const current = localStorage.getItem(KEY(key))
    if (current) return JSON.parse(current)

    const legacy = localStorage.getItem(LEGACY_KEY(key))
    return legacy ? JSON.parse(legacy) : fallback
  } catch {
    return fallback
  }
}

function persist(key, value) {
  try {
    localStorage.setItem(KEY(key), JSON.stringify(value))
  } catch {}
}

const LEGACY_STATUS = {
  CONTRACT: 'CONTRACT_SVO',
  VETERAN: 'VETERAN_BD',
  DECEASED: 'DECEASED_MILITARY',
}

function normalizeStoredProfile(profile) {
  if (!profile || typeof profile !== 'object') return null

  const normalizedChildren = Array.isArray(profile.children)
    ? profile.children.map((child) => ({
      ...child,
      disabilityGroup: child.disabilityGroup === 'CHILD_DISABLED' ? null : child.disabilityGroup,
    }))
    : []

  return {
    ...profile,
    militaryStatus: LEGACY_STATUS[profile.militaryStatus] || profile.militaryStatus || null,
    children: normalizedChildren,
    fullName: profile.fullName || [profile.firstName, profile.lastName].filter(Boolean).join(' '),
  }
}

export function AppProvider({ children }) {
  const [consent, setConsentState] = useState(() => Boolean(load('consent', false)))
  const [profile, setProfile] = useState(() => normalizeStoredProfile(load('profile', null)))
  const [results, setResults] = useState(() => load('results', null))
  const [support, setSupport] = useState(() => load('support', {}))
  const [toast, setToast] = useState(null)

  useEffect(() => persist('consent', consent), [consent])
  useEffect(() => persist('profile', profile), [profile])
  useEffect(() => persist('results', results), [results])
  useEffect(() => persist('support', support), [support])

  useEffect(() => {
    if (!toast) return undefined
    const timer = setTimeout(() => setToast(null), 3200)
    return () => clearTimeout(timer)
  }, [toast])

  const showToast = useCallback((text) => {
    setToast({ text, at: Date.now() })
  }, [])

  const syncSupport = useCallback((matched) => {
    setSupport((previous) => {
      const next = { ...previous }

      for (const item of matched) {
        const current = next[item.id] || {
          status: 'NOT_APPLIED',
          reminder: false,
          updatedAt: Date.now(),
        }

        next[item.id] = {
          ...current,
          ...(item.status ? { status: item.status } : {}),
          ...(item.selectedForAction != null
            ? { selectedForAction: item.selectedForAction }
            : {}),
          updatedAt: current.updatedAt || Date.now(),
        }
      }

      return next
    })
  }, [])

  const runMatch = useCallback(async (draft) => {
    const response = await api.runMatch(draft)

    if (response.profile) {
      setProfile(response.profile)
    }

    const normalized = {
      ...response,
      version: JSON.stringify({ ...draft, backendUserId: response.userId }),
      at: Date.now(),
    }

    setResults(normalized)
    syncSupport(response.matched || [])
    return response
  }, [syncSupport])

  const saveProfile = useCallback((next) => {
    setProfile((current) => ({ ...(current || {}), ...next }))
  }, [])

  const setConsent = useCallback((value) => setConsentState(Boolean(value)), [])

  const updateSupport = useCallback(async (id, patch) => {
    setSupport((previous) => ({
      ...previous,
      [id]: {
        ...(previous[id] || { status: 'NOT_APPLIED', reminder: false }),
        ...patch,
        updatedAt: Date.now(),
      },
    }))

    haptic('light')

    if (!profile?.backendUserId) {
      return
    }

    const currentMeasure = results?.matched?.find(
      (item) => String(item.id) === String(id)
    )

    if (!currentMeasure?.userSupportId) {
      return
    }

    const serverPatch = {}

    if (patch.status) serverPatch.status = patch.status
    if (typeof patch.selectedForAction === 'boolean') {
      serverPatch.selectedForAction = patch.selectedForAction
    }
    if (Object.prototype.hasOwnProperty.call(patch, 'note')) {
      serverPatch.note = patch.note
    }

    if (Object.keys(serverPatch).length === 0) {
      return
    }

    try {
      await api.updateUserSupport(
        profile.backendUserId,
        currentMeasure.userSupportId,
        serverPatch
      )
    } catch (error) {
      console.error(error)
      showToast(error.message || 'Не удалось сохранить изменение меры.')
    }
  }, [profile?.backendUserId, results, showToast])

  const requestMfc = useCallback((payload) => api.requestMfc(payload), [])

  const reset = useCallback(() => {
    ;['consent', 'profile', 'results', 'support'].forEach((key) => {
      localStorage.removeItem(KEY(key))
      localStorage.removeItem(LEGACY_KEY(key))
    })

    setConsentState(false)
    setProfile(null)
    setResults(null)
    setSupport({})
    setToast(null)
  }, [])

  return (
    <AppCtx.Provider
      value={{
        consent,
        setConsent,
        profile,
        saveProfile,
        results,
        runMatch,
        support,
        updateSupport,
        requestMfc,
        reset,
        toast,
        showToast,
      }}
    >
      {children}
    </AppCtx.Provider>
  )
}
