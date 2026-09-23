import * as mock from './mock'
import * as backend from './backend'

const USE_MOCK = String(import.meta.env.VITE_USE_MOCK ?? 'false').toLowerCase() === 'true'

export default {
  runMatch: USE_MOCK ? mock.runMatch : backend.runMatch,
  requestMfc: async (payload) => {
    if (USE_MOCK) return mock.requestMfc(payload)
    throw new Error('MFC request endpoint is not part of the current backend contract')
  },
}
