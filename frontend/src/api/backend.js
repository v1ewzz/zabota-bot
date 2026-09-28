import { apiFetch } from './client'
import { IDS, requireBackendId, requireMunicipalityId } from './ids'

function optionalId(kind, code, explicitId) {
  return explicitId || (code ? IDS[kind]?.[code] : null) || null
}

function cleanMeasureDescription(value) {
  if (typeof value !== 'string' || !value.trim()) return ''

  return value
    .replace(
      /\s*Основные критерии для алгоритма\s*:\s*.*?(?=\s+(?:Что даёт|Кто получает|Размер\s*\/\s*вид поддержки|Размер|Заявление|Куда обращаться|Документы|Статус|Основание|Срок)\s*:|$)/gis,
      ' '
    )
    .replace(/\s{2,}/g, ' ')
    .trim()
}

function incomeBroadCode(range) {
  if (!range || range === 'UNKNOWN') return null
  if (range === 'UNDER_27' || range === '27_40') return 'LOW'
  if (range === '40_60' || range === '60_100') return 'MIDDLE'
  return 'HIGH'
}

function normalizeDocuments(value) {
  if (Array.isArray(value)) {
    return value.filter((item) => typeof item === 'string' && item.trim())
  }

  if (typeof value !== 'string' || !value.trim()) return []

  return value
    .split(/\r?\n|;/)
    .map((item) => item.trim())
    .filter(Boolean)
}

function normalizeMeasure(raw) {
  return {
    id: raw?.supportId ?? raw?.id ?? null,
    userSupportId: raw?.userSupportId ?? null,
    name: typeof raw?.name === 'string' ? raw.name.trim() : '',
    description: cleanMeasureDescription(raw?.description),
    amount: normalizeAmount(raw?.amount),
    frequency: normalizeFrequency(raw),
    applicationRequired: Boolean(raw?.applicationRequired),
    channel: raw?.applicationChannelCode || normalizeChannel(raw?.applicationChannel),
    channelLabel: raw?.applicationChannelName || raw?.applicationChannel || null,
    actionUrl: normalizeUrl(raw?.actionUrl),
    supportType: raw?.supportTypeCode || null,
    supportTypeLabel: raw?.supportTypeName || null,
    level: raw?.levelCode || null,
    levelLabel: raw?.levelName || null,
    recipient: raw?.recipientTypeCode || 'FAMILY',
    recipientLabel: raw?.recipientTypeName || null,
    documents: normalizeDocuments(raw?.documents),
    statusId: raw?.statusId ?? null,
    status: raw?.statusCode || 'NOT_APPLIED',
    statusName: raw?.statusName || null,
    selectedForAction: Boolean(raw?.selectedForAction),
    note: raw?.note || null,
    checkedAt: raw?.checkedAt || null,
    submittedAt: raw?.submittedAt || null,
    receivedAt: raw?.receivedAt || null,
    validTo: raw?.validTo || null,
  }
}

function normalizeFrequency(raw) {
  return raw?.frequencyCode || raw?.frequency || null
}

function normalizeChannel(value) {
  if (value == null) return null
  const normalized = String(value).trim().toUpperCase()
  return normalized || null
}

function normalizeUrl(value) {
  if (typeof value !== 'string') return null

  const url = value.trim()
  return /^https?:\/\//i.test(url) ? url : null
}

function normalizeAmount(value) {
  if (value == null || value === '') return null

  const amount = Number(value)
  return Number.isFinite(amount) ? amount : null
}

function toUserRequest(profile) {
  if (!profile) {
    throw new Error('Профиль не найден. Вернитесь к анкете и заполните её заново.')
  }

  const regionId = requireBackendId(
    'region',
    profile.region,
    'выбранный регион'
  )

  const municipalityId = requireMunicipalityId(
    profile.region,
    profile.municipality
  )

  const children = (profile.children || [])
    .filter((child) => child.educationType !== 'NONE')
    .map((child) => ({
      birthDate: child.birthDate,
      educationLevelId: requireBackendId(
        'educationLevel',
        child.educationLevel,
        'уровень образования ребёнка'
      ),
      grade: child.grade ?? null,
      disability: Boolean(child.disability),
      disabilityGroupId: optionalId(
        'disabilityGroup',
        child.disabilityGroup,
        child.disabilityGroupId
      ),
      fullTime: Boolean(child.fullTime),
    }))

  return {
    firstName: String(profile.firstName || '').trim(),
    lastName: String(profile.lastName || '').trim(),
    regionId,
    municipalityId,
    familyRelationId: optionalId(
      'familyRelation',
      profile.familyRelation,
      profile.familyRelationId
    ),
    militaryStatusId: optionalId(
      'militaryStatus',
      profile.militaryStatus,
      profile.militaryStatusId
    ),
    pregnancy: Boolean(profile.pregnancy),
    injury: Boolean(profile.injury),
    disability: Boolean(profile.disability),
    disabilityGroupId: optionalId(
      'disabilityGroup',
      profile.disabilityGroup,
      profile.disabilityGroupId
    ),
    housingProblem: Boolean(profile.housingProblem),
    gasificationNeeded: Boolean(profile.gasificationNeeded),
    employmentStatusId: optionalId(
      'employmentStatus',
      profile.employmentStatus,
      profile.employmentStatusId
    ),
    incomeCategoryId: optionalId(
      'incomeCategory',
      incomeBroadCode(profile.incomeRange),
      profile.incomeCategoryId
    ),
    loanExists: Boolean(profile.loanExists),
    businessPlan: Boolean(profile.businessPlan),
    jobSeeker: Boolean(profile.jobSeeker),
    socialServiceNeed: Boolean(profile.socialServiceNeed),
    legalIssueCategoryId: profile.legalHelpNeeded
      ? (profile.legalIssueCategoryId || null)
      : null,
    children,
  }
}

async function saveUser(profile) {
  const request = toUserRequest(profile)
  const userId = profile?.backendUserId

  const response = userId
    ? await apiFetch(`/users/${userId}`, {
      method: 'PUT',
      body: JSON.stringify(request),
    })
    : await apiFetch('/users', {
      method: 'POST',
      body: JSON.stringify(request),
    })

  if (!response?.userId) {
    throw new Error('Backend не вернул идентификатор пользователя.')
  }

  return {
    userId: response.userId,
    profile: {
      ...profile,
      backendUserId: response.userId,
    },
  }
}

const DETAIL_CONCURRENCY = 4

async function mapWithConcurrency(items, limit, mapper) {
  const results = new Array(items.length)
  let cursor = 0

  async function worker() {
    while (cursor < items.length) {
      const index = cursor
      cursor += 1
      results[index] = await mapper(items[index], index)
    }
  }

  const workers = Array.from({ length: Math.min(limit, items.length) }, worker)
  await Promise.all(workers)
  return results
}

async function getUserSupports(userId, options = {}) {
  const response = await apiFetch(`/users/${userId}/supports`, {
    signal: options.signal,
  })

  if (!Array.isArray(response)) return []

  const items = response.map(normalizeMeasure)

  const enriched = await mapWithConcurrency(items, DETAIL_CONCURRENCY, async (item) => {
    if (!item.id) return item

    try {
      const detail = await apiFetch(`/support-measures/${item.id}`, {
        signal: options.signal,
      })
      return {
        ...normalizeMeasure(detail),
        ...item,
        id: item.id,
        userSupportId: item.userSupportId,
        name: item.name || detail?.name || '',
        description: item.description || detail?.description || '',
        amount: item.amount ?? normalizeAmount(detail?.amount),
        frequency: detail?.frequencyCode || item.frequency || null,
        applicationRequired: item.applicationRequired,
        channel: detail?.applicationChannelCode || item.channel,
        channelLabel: detail?.applicationChannelName || item.channelLabel,
        actionUrl: item.actionUrl || normalizeUrl(detail?.actionUrl),
        supportType: detail?.supportTypeCode || item.supportType || null,
        supportTypeLabel: detail?.supportTypeName || item.supportTypeLabel || null,
        level: detail?.levelCode || item.level || null,
        levelLabel: detail?.levelName || item.levelLabel || null,
        recipient: detail?.recipientTypeCode || item.recipient || 'FAMILY',
        recipientLabel: detail?.recipientTypeName || item.recipientLabel || null,
        documents: normalizeDocuments(detail?.documents),
        validTo: detail?.validTo || item.validTo || null,
      }
    } catch (error) {
      if (error?.name === 'AbortError') throw error
      return item
    }
  })

  return enriched
}

export async function getProfile(userId) {
  return apiFetch(`/users/${userId}/profile`)
}

export async function runMatch(profile, options = {}) {
  const saved = await saveUser(profile)
  const userId = saved.userId

  await apiFetch(`/users/${userId}/supports/search`, {
    method: 'POST',
    signal: options.signal,
  })

  const matched = await getUserSupports(userId, options)

  return {
    userId,
    profile: saved.profile,
    matched,
    skipped: [],
  }
}

const STATUS_ID = {
  NOT_APPLIED: '77f72989-cbe1-4061-b7da-d921f1b46d40',
  SUBMITTED: '7f5fa255-f16b-46eb-861c-2ad3bbd516ad',
  APPROVED: '53be9cd0-92cf-4d0d-b791-87890478dd76',
  RECEIVED: '3079f54e-7848-48ec-817d-437d20b8c215',
}

export async function updateUserSupport(userId, userSupportId, patch) {
  const request = {
    ...(patch.status ? { statusId: STATUS_ID[patch.status] || patch.statusId || null } : {}),
    ...(typeof patch.selectedForAction === 'boolean'
      ? { selectedForAction: patch.selectedForAction }
      : {}),
    ...(Object.prototype.hasOwnProperty.call(patch, 'note')
      ? { note: patch.note }
      : {}),
  }

  if (!request.statusId && typeof request.selectedForAction !== 'boolean' && !Object.prototype.hasOwnProperty.call(request, 'note')) {
    throw new Error('Не указаны изменения для меры поддержки.')
  }

  return apiFetch(`/users/${userId}/supports/${userSupportId}`, {
    method: 'PATCH',
    body: JSON.stringify(request),
  })
}

export async function requestMfc(payload) {
  const response = await apiFetch('/mfc/requests', {
    method: 'POST',
    body: JSON.stringify(payload || {}),
  })

  return {
    ok: true,
    requestId: response?.requestId ?? response?.id ?? null,
    ...response,
  }
}

export async function requestPdfSummary(userId, payload) {
  if (!userId) {
    throw new Error('Профиль ещё не сохранён на сервере. Пройдите анкету заново.')
  }

  const response = await apiFetch(`/users/${userId}/pdf-summary`, {
    method: 'POST',
    body: JSON.stringify(payload || {}),
  })

  return {
    ok: true,
    ...response,
  }
}
