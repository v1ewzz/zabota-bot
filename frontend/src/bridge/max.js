const bridge =
  typeof window !== 'undefined'
    ? window.MaxBridge || window.maxBridge || window.__MAX_BRIDGE__
    : null

export const inMax = Boolean(bridge)

export function ready() {
  try { bridge?.ready?.() } catch (e) {}
}

export function haptic(type = 'light') {
  try { bridge?.haptic?.({ type }) } catch (e) {}
}

export function openLink(url) {
  try {
    if (bridge?.openLink) return bridge.openLink({ url })
  } catch (e) {}
  window.open(url, '_blank', 'noopener')
}

export function share(text, url) {
  try {
    if (bridge?.share) return bridge.share({ text, url })
  } catch (e) {}
  if (navigator.share) navigator.share({ text, url }).catch(() => {})
}

export function getUser() {
  try {
    const raw =
      bridge?.initData ||
      window.__MAX_INIT_DATA__ ||
      null
    if (!raw) return null

    let data = raw
    if (typeof raw === 'string') {
      if (raw.trim().startsWith('{')) data = JSON.parse(raw)
      else data = Object.fromEntries(new URLSearchParams(raw))
    }
    const u = data.user
      ? (typeof data.user === 'string' ? JSON.parse(data.user) : data.user)
      : data

    return {
      id: u.id ?? u.user_id ?? null,
      firstName: u.first_name ?? u.firstName ?? '',
      lastName: u.last_name ?? u.lastName ?? '',
      username: u.username ?? '',
      raw: data,
    }
  } catch {
    return null
  }
}