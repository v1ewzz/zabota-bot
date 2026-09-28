import { runMatchEngine } from './engine'
import { CATALOG } from './mockData'

const delay = (ms = 450) => new Promise((resolve) => setTimeout(resolve, ms))

export async function runMatch(profile) {
  await delay(650)
  return { userId: null, ...runMatchEngine(profile, CATALOG) }
}

export async function requestMfc(payload) {
  await delay(350)
  console.info('[mock] MFC action stored locally:', payload)
  return { ok: true, requestId: `demo-${Date.now()}` }
}

export async function requestPdfSummary(userId, payload) {
  await delay(350)
  console.info('[mock] PDF summary requested for chat delivery:', userId, payload)
  return { ok: true, requestId: `demo-pdf-${Date.now()}` }
}
