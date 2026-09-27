const env = import.meta.env

export const IDS = {
  region: {
    TATARSTAN: '2cfa3160-48ad-48e8-9a71-7d7ca7dbdca0',
  },

  municipality: {
    'TATARSTAN:KAZAN': '5b7e9c2a-3a34-4d8f-9c5f-2e7a1b6d4f20',
  },

  familyRelation: {
    WIFE: '68b8d814-39a0-4a5c-9e53-d7cfffa58327',
    SELF: env.VITE_FAMILY_RELATION_SELF_ID || null,
    MOTHER: env.VITE_FAMILY_RELATION_MOTHER_ID || null,
    FATHER: env.VITE_FAMILY_RELATION_FATHER_ID || null,
    CHILD: env.VITE_FAMILY_RELATION_CHILD_ID || null,
  },

  militaryStatus: {
    MOBILIZED: '70cd165d-6cac-435f-ae56-5f13c84d3cf5',
    CONTRACT_SVO: '212cd8e2-b0de-41b6-8dbf-436f9c323fa0',
    CONSCRIPT: '4b541886-f163-45fc-9c87-cc3fc6bb96ff',
    SVO_PARTICIPANT: '77ccb961-28b6-42c0-9bba-791dcf37709c',
    VETERAN_BD: '17d0c090-c071-4fd0-aada-025a16e92799',
    DECEASED_MILITARY: '70a2e5ec-3391-4063-bc53-090083c471d7',
    VOLUNTEER_SVO: '43b47d65-16cd-4e03-999e-acb732b3acde',
    CONTRACT: '212cd8e2-b0de-41b6-8dbf-436f9c323fa0',
    VETERAN: '17d0c090-c071-4fd0-aada-025a16e92799',
    DECEASED: '70a2e5ec-3391-4063-bc53-090083c471d7',
  },

  disabilityGroup: {
    I: env.VITE_DISABILITY_GROUP_I_ID || null,
    II: env.VITE_DISABILITY_GROUP_II_ID || null,
    III: env.VITE_DISABILITY_GROUP_III_ID || null,
  },

  incomeCategory: {
    LOW: env.VITE_INCOME_LOW_ID || null,
    MIDDLE: env.VITE_INCOME_MIDDLE_ID || null,
    HIGH: env.VITE_INCOME_HIGH_ID || null,
  },

  employmentStatus: {
    EMPLOYED: env.VITE_EMPLOYMENT_EMPLOYED_ID || null,
    UNEMPLOYED: env.VITE_EMPLOYMENT_UNEMPLOYED_ID || null,
    SELF: env.VITE_EMPLOYMENT_SELF_ID || null,
    STUDENT: env.VITE_EMPLOYMENT_STUDENT_ID || null,
    PENSIONER: env.VITE_EMPLOYMENT_PENSIONER_ID || null,
    UNKNOWN: env.VITE_EMPLOYMENT_UNKNOWN_ID || null,
  },

  educationLevel: {
    SCHOOL: '3fd49a30-e781-446f-a85e-619fed1026f8',
    PRESCHOOL: env.VITE_EDUCATION_LEVEL_PRESCHOOL_ID || null,
    COLLEGE: env.VITE_EDUCATION_LEVEL_COLLEGE_ID || '417f4f9d-e78e-4317-95ff-04a27ec0dabc',
    UNIVERSITY: env.VITE_EDUCATION_LEVEL_UNIVERSITY_ID || '32b94279-f374-485c-85c8-58225b8536a3',
    SPO: '417f4f9d-e78e-4317-95ff-04a27ec0dabc',
    BACHELOR: '32b94279-f374-485c-85c8-58225b8536a3',
    SPECIALIST: 'cb523a01-8530-4f57-b298-8c861ae0af30',
  },
}

export function getBackendId(kind, code, explicitId = null) {
  return explicitId || (code ? IDS[kind]?.[code] : null) || null
}

export function hasBackendId(kind, code, explicitId = null) {
  return Boolean(getBackendId(kind, code, explicitId))
}

export function requireBackendId(kind, code, label) {
  const id = getBackendId(kind, code)
  if (!id) {
    throw new Error(`Не удалось определить «${label || code}». Выберите другой вариант или обновите справочники.`)
  }
  return id
}

export function municipalityId(region, municipality) {
  return IDS.municipality[`${region}:${municipality}`] || null
}

export function requireMunicipalityId(region, municipality) {
  const id = municipalityId(region, municipality)
  if (!id) {
    throw new Error('Для выбранного города или района пока нет подключённого значения. Выберите Казань или другой доступный вариант.')
  }
  return id
}