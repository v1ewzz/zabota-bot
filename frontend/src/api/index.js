import * as mock from './mock'
import { apiFetch } from './client'

const USE_MOCK = (import.meta.env.VITE_USE_MOCK ?? 'true') !== 'false'

const impl = USE_MOCK
  ? mock
  : {
      runMatch: (profile) => apiFetch('/match', { method: 'POST', body: JSON.stringify(profile) }),
      requestMfc: (payload) => apiFetch('/mfc-request', { method: 'POST', body: JSON.stringify(payload) }),
    }

export default impl
