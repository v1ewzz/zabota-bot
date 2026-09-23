function getWebApp() {
  return typeof window !== 'undefined' ? window.WebApp || null : null
}

export function getWebAppInstance() {
  return getWebApp()
}

export function isMax() {
  return Boolean(getWebApp())
}

export function ready() {
  try {
    getWebApp()?.ready?.()
  } catch {}
}

export function getInitData() {
  try {
    return getWebApp()?.initData || ''
  } catch {
    return ''
  }
}

export function getInitDataUnsafe() {
  try {
    return getWebApp()?.initDataUnsafe || null
  } catch {
    return null
  }
}

export function getMaxPlatform() {
  try {
    return getWebApp()?.platform || null
  } catch {
    return null
  }
}

export function getMaxVersion() {
  try {
    return getWebApp()?.version || null
  } catch {
    return null
  }
}

export function getMaxDeviceName() {
  try {
    return getWebApp()?.deviceName || null
  } catch {
    return null
  }
}

export function getLaunchContext() {
  try {
    return getWebApp()?.getLaunchContext?.() || null
  } catch {
    return null
  }
}

export function haptic(type = 'light') {
  try {
    getWebApp()?.HapticFeedback?.impactOccurred?.(type)
  } catch {}
}

export function openLink(url) {
  if (!url || !/^https?:\/\//i.test(url)) return false

  try {
    const webApp = getWebApp()
    if (webApp?.openLink) {
      webApp.openLink(url)
      return true
    }
  } catch {}

  try {
    const opened = window.open(url, '_blank', 'noopener,noreferrer')
    return Boolean(opened)
  } catch {
    return false
  }
}

export function showBackButton(handler) {
  try {
    const button = getWebApp()?.BackButton
    if (!button) return false
    button.offClick?.(handler)
    button.onClick?.(handler)
    button.show?.()
    return true
  } catch {
    return false
  }
}

export function hideBackButton(handler) {
  try {
    const button = getWebApp()?.BackButton
    if (!button) return
    if (handler) button.offClick?.(handler)
    button.hide?.()
  } catch {}
}
