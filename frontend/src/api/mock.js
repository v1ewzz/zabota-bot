import { runMatchEngine } from './engine'
import { CATALOG } from './mockData'

const delay = (ms = 400) => new Promise((r) => setTimeout(r, ms))

export async function runMatch(profile) {
  await delay(600)
  return runMatchEngine(profile, CATALOG)
}

export async function requestMfc(payload) {
  await delay(400)
  console.info('[mock] Обращение в МФЦ сохранено:', payload)
  return { ok: true, requestId: 'demo-' + Date.now() }
}
