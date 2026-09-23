import * as mock from './mock'
import * as backend from './backend'

const USE_MOCK = String(import.meta.env.VITE_USE_MOCK ?? 'false').toLowerCase() === 'true'

export default {
  runMatch: USE_MOCK ? mock.runMatch : backend.runMatch,
  updateUserSupport: USE_MOCK
    ? async () => null
    : backend.updateUserSupport,
  getProfile: USE_MOCK
    ? async () => null
    : backend.getProfile,
  requestMfc: async (payload) => {
    if (USE_MOCK) return mock.requestMfc(payload)
    return backend.requestMfc(payload)
  },
}
