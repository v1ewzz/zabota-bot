import { apiFetch } from './client'
import { IDS, requireBackendId, requireMunicipalityId } from './ids'

function incomeBroadCode(range) {
  if (!range || range === 'UNKNOWN') return null
  if (range === 'UNDER_27' || range === '27_40') return 'LOW'
  if (range === '40_60' || range === '60_100') return 'MIDDLE'
  return 'HIGH'
}

function optionalId(kind, code, explicitId) {
  return explicitId || (code ? IDS[kind]?.[code] : null) || null
}

function toSearchRequest(profile) {
  if (!profile) {
    throw new Error('Профиль не найден. Вернитесь к анкете и заполните её заново.')
  }

  const request = {
    regionId: requireBackendId('region', profile.region, 'выбранный регион'),
    municipalityId: requireMunicipalityId(profile.region, profile.municipality),
    familyRelationId: requireBackendId('familyRelation', profile.familyRelation, 'ваша роль'),
    militaryStatusId: requireBackendId('militaryStatus', profile.militaryStatus, 'статус военнослужащего'),
    pregnancy: Boolean(profile.pregnancy),
    injury: Boolean(profile.injury),
    disability: Boolean(profile.disability),
    disabilityGroupId: optionalId('disabilityGroup', profile.disabilityGroup, profile.disabilityGroupId),
    housingProblem: Boolean(profile.housingProblem),
    gasificationNeeded: Boolean(profile.gasificationNeeded),
    employmentStatusId: optionalId('employmentStatus', profile.employmentStatus, profile.employmentStatusId),
    incomeCategoryId: optionalId('incomeCategory', incomeBroadCode(profile.incomeRange), profile.incomeCategoryId),
    children: (profile.children || []).map((child) => ({
      birthDate: child.birthDate,
      educationLevelId: requireBackendId('educationLevel', child.educationLevel, 'уровень образования ребёнка'),
      grade: child.grade ?? null,
      disability: Boolean(child.disability),
      disabilityGroupId: optionalId('disabilityGroup', child.disabilityGroup, child.disabilityGroupId),
      fullTime: Boolean(child.fullTime),
    })),
  }

  return request
}

const CODE_MAPS = {
  supportType: {
    PAYMENT: 'PAYMENT',
    COMPENSATION: 'COMPENSATION',
    BENEFIT: 'BENEFIT',
    SERVICE: 'SERVICE',
  },
  level: {
    FEDERAL: 'FEDERAL',
    REGIONAL: 'REGIONAL',
    MUNICIPAL: 'MUNICIPAL',
  },
  recipient: {
    USER: 'USER',
    SPOUSE: 'SPOUSE',
    CHILD: 'CHILD',
    FAMILY: 'FAMILY',
  },
  frequency: {
    ONE_TIME: 'ONE_TIME',
    MONTHLY: 'MONTHLY',
    YEARLY: 'YEARLY',
  },
  channel: {
    GOSUSLUGI: 'GOSUSLUGI',
    SFR: 'SFR',
    MFC: 'MFC',
    SCHOOL: 'SCHOOL',
    EDUCATIONAL_ORGANIZATION: 'EDUCATIONAL_ORGANIZATION',
    FNS: 'FNS',
    BANK: 'BANK',
  },
}

function normalizeCode(value, map) {
  if (value == null) return null

  const text = String(value).trim()
  if (!text) return null

  if (map[text]) return map[text]

  const lowered = text.toLowerCase()
  const entry = Object.entries(map).find(([, code]) => code.toLowerCase() === lowered)
  if (entry) return entry[1]

  return null
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

function normalizeMeasure(raw) {
  return {
    id: raw?.supportId ?? raw?.id ?? null,
    name: typeof raw?.name === 'string' ? raw.name.trim() : '',
    description: typeof raw?.description === 'string' ? raw.description.trim() : '',
    amount: normalizeAmount(raw?.amount),
    frequency: normalizeCode(raw?.frequency, CODE_MAPS.frequency),
    applicationRequired: Boolean(raw?.applicationRequired),
    channel: normalizeCode(raw?.applicationChannel ?? raw?.channel, CODE_MAPS.channel),
    actionUrl: normalizeUrl(raw?.actionUrl),
    supportType: normalizeCode(raw?.supportType, CODE_MAPS.supportType),
    level: normalizeCode(raw?.level, CODE_MAPS.level),
    recipient: normalizeCode(raw?.recipientType ?? raw?.recipient, CODE_MAPS.recipient) || 'FAMILY',
    documents: Array.isArray(raw?.documents) ? raw.documents.filter((item) => typeof item === 'string' && item.trim()) : [],
    npa: Array.isArray(raw?.npa) ? raw.npa : [],
    reason: null,
    urgency: raw?.urgency || null,
    validTo: raw?.validTo || null,
  }
}

export async function runMatch(profile) {
  const response = await apiFetch('/supports/search', {
    method: 'POST',
    body: JSON.stringify(toSearchRequest(profile)),
  })

  return {
    userId: response?.userId ?? null,
    matched: Array.isArray(response?.measures)
      ? response.measures.map(normalizeMeasure).filter((measure) => measure.id && measure.name)
      : [],
    skipped: [],
  }
}
