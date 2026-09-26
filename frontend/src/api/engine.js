import { calcAge } from '../utils'

function factValue(facts, param) {
  switch (param) {
    case 'familyRelation': return facts.familyRelation
    case 'militaryStatus': return facts.militaryStatus
    case 'region': return facts.region
    case 'municipality': return facts.municipality
    case 'injury': return facts.injury
    case 'disability': return facts.disability
    case 'disabilityGroup': return facts.disabilityGroup
    case 'housingProblem': return facts.housingProblem
    case 'gasification': return facts.gasificationNeeded
    case 'pregnancy': return facts.pregnancy
    case 'incomeCategory': return facts.incomeCategory === 'UNKNOWN' ? undefined : facts.incomeCategory
    case 'employmentStatus': return facts.employmentStatus === 'UNKNOWN' ? undefined : facts.employmentStatus
    case 'orphan': return facts.orphan
    case 'applicant_age': return facts.applicantAge
    case 'applicant_education': return facts.applicantEducation
    case 'applicant_full_time': return facts.applicantFullTime
    default: return undefined
  }
}

function testCondition(condition, value) {
  switch (condition.op) {
    case 'EQUALS':
      return String(value) === String(condition.value)
    case 'IN':
      return String(condition.value)
        .split(',')
        .map((item) => item.trim())
        .filter(Boolean)
        .includes(String(value))
    case 'BETWEEN': {
      const number = Number(value)
      return Number.isFinite(number)
        && number >= Number(condition.valueFrom)
        && number <= Number(condition.valueTo)
    }
    case 'GTE': return Number(value) >= Number(condition.valueFrom)
    case 'LTE': return Number(value) <= Number(condition.valueTo)
    case 'GT': return Number(value) > Number(condition.valueFrom)
    case 'LT': return Number(value) < Number(condition.valueTo)
    case 'TRUE': return value === true
    case 'FALSE': return value === false
    default: return false
  }
}

function isChildParam(param) {
  return typeof param === 'string' && (param === 'child' || param.startsWith('child_'))
}

function childValue(child, param) {
  switch (param) {
    case 'child_age': return child.age
    case 'child_grade': return child.grade
    case 'child_education': return child.educationLevel
    case 'child_disability': return child.disability
    default: return undefined
  }
}

function childMatchesAll(child, conditions) {
  return conditions.every((condition) => {
    const value = childValue(child, condition.param)
    return value !== undefined && value !== null && testCondition(condition, value)
  })
}

function evalGroup(group, facts) {
  const userConditions = (group.items || []).filter((item) => !isChildParam(item.param))
  const childConditions = (group.items || []).filter((item) => isChildParam(item.param))
  const missing = []

  for (const condition of userConditions) {
    const value = factValue(facts, condition.param)

    if (value === undefined || value === null) {
      if (condition.op !== 'FALSE') missing.push(condition.param)
      return { ok: false, missing }
    }

    if (!testCondition(condition, value)) {
      return { ok: false, missing }
    }
  }

  if (childConditions.length) {
    const existsCondition = childConditions.find(
      (condition) => condition.param === 'child' && condition.op === 'EXISTS'
    )
    const realChildConditions = childConditions.filter((condition) => condition.param !== 'child')

    if (existsCondition && facts.children.length === 0) {
      return { ok: false, missing }
    }

    if (realChildConditions.length > 0) {
      const matched = facts.children.some((child) => childMatchesAll(child, realChildConditions))
      if (!matched) return { ok: false, missing }
    }
  }

  return { ok: true, missing }
}

function matchMeasure(measure, facts) {
  const missingAll = []

  for (const group of measure.rules || []) {
    const result = evalGroup(group, facts)
    missingAll.push(...result.missing)

    if (result.ok) {
      return {
        ok: true,
        reason: (group.items || []).map((item) => item.label).filter(Boolean).join(' · '),
        amountOverride: group.amountOverride ?? null,
      }
    }
  }

  return {
    ok: false,
    missing: [...new Set(missingAll)],
  }
}

const MISSING_LABEL = {
  incomeCategory: 'доход семьи',
  municipality: 'муниципалитет',
  region: 'регион',
  disabilityGroup: 'группа инвалидности',
  applicant_age: 'возраст заявителя',
  applicant_full_time: 'форма обучения',
}

function toDto(measure, result) {
  return {
    ...measure,
    amount: result.amountOverride ?? measure.amount,
    reason: result.reason,
  }
}

export function runMatchEngine(profile, catalog) {
  const facts = {
    ...profile,
    applicantAge: calcAge(profile.applicantBirthDate),
    applicantEducation: profile.applicantEducation || null,
    applicantFullTime: Boolean(profile.applicantFullTime),
    children: (profile.children || []).map((child) => ({
      ...child,
      age: calcAge(child.birthDate),
    })),
  }

  const matched = []
  const skipped = []

  for (const measure of catalog) {
    const result = matchMeasure(measure, facts)

    if (result.ok) {
      matched.push(toDto(measure, result))
    } else if (result.missing.length) {
      skipped.push({
        measureId: measure.id,
        name: measure.name,
        missing: result.missing.map((param) => MISSING_LABEL[param] || param),
      })
    }
  }

  return { matched, skipped }
}
