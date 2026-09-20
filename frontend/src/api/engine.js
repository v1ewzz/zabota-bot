const isChildParam = (p) => typeof p === 'string' && p.startsWith('child')

function factValue(facts, param) {
  switch (param) {
    case 'familyRelation': return facts.familyRelation
    case 'militaryStatus': return facts.militaryStatus === 'UNKNOWN' ? undefined : facts.militaryStatus
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
    default: return undefined
  }
}

function test(cond, value) {
  switch (cond.op) {
    case 'EQUALS': return String(value) === String(cond.value)
    case 'IN': return String(cond.value).split(',').map((s) => s.trim()).includes(String(value))
    case 'BETWEEN': {
      const n = Number(value)
      return Number.isFinite(n) && n >= Number(cond.valueFrom) && n <= Number(cond.valueTo)
    }
    case 'GTE': return Number(value) >= Number(cond.valueFrom)
    case 'LTE': return Number(value) <= Number(cond.valueTo)
    case 'TRUE': return value === true
    case 'FALSE': return value === false
    default: return false
  }
}

function childMatchesAll(child, conds) {
  return conds.every((c) => {
    const v =
      c.param === 'child_age' ? child.age
      : c.param === 'child_grade' ? child.grade
      : c.param === 'child_education' ? child.educationLevel
      : c.param === 'child_disability' ? child.disability
      : undefined
    return v !== undefined && v !== null && test(c, v)
  })
}

function calcAgeSafe(birthDate) {
  if (!birthDate) return null
  const b = new Date(birthDate)
  if (Number.isNaN(b.getTime())) return null
  const now = new Date()
  let age = now.getFullYear() - b.getFullYear()
  const m = now.getMonth() - b.getMonth()
  if (m < 0 || (m === 0 && now.getDate() < b.getDate())) age--
  return age
}

export function buildFacts(profile) {
  return {
    ...profile,
    children: (profile.children || []).map((c) => ({ ...c, age: calcAgeSafe(c.birthDate) })),
  }
}

function evalGroup(group, facts) {
  const userConds = group.items.filter((i) => !isChildParam(i.param))
  const childConds = group.items.filter((i) => isChildParam(i.param))
  const missing = []

  for (const c of userConds) {
    const v = factValue(facts, c.param)
    if (v === undefined || v === null) { missing.push(c.param); continue }
    if (!test(c, v)) return { ok: false, missing }
  }

  if (childConds.length) {
    const existsCond = childConds.find((c) => c.param === 'child' && c.op === 'EXISTS')
    const real = childConds.filter((c) => c.param !== 'child')
    let ok = true
    if (existsCond) ok = facts.children.length > 0
    if (ok && real.length) ok = facts.children.some((ch) => childMatchesAll(ch, real))
    if (!ok) return { ok: false, missing }
  }
  return { ok: true, missing }
}

function matchMeasure(measure, facts) {
  const missingAll = []
  for (const g of measure.rules || []) {
    const r = evalGroup(g, facts)
    missingAll.push(...r.missing)
    if (r.ok) {
      return {
        ok: true,
        reason: (g.items || []).map((i) => i.label).filter(Boolean).join(' · '),
        amountOverride: g.amountOverride ?? null,
      }
    }
  }
  return { ok: false, missing: [...new Set(missingAll)] }
}

const MISSING_LABEL = {
  incomeCategory: 'доход семьи',
  municipality: 'муниципалитет',
  region: 'регион',
  disabilityGroup: 'группа инвалидности',
}

function toDto(m, r) {
  return {
    id: m.id, name: m.name, description: m.description,
    supportType: m.supportType, level: m.level, recipient: m.recipient,
    amount: r.amountOverride ?? m.amount, frequency: m.frequency,
    applicationRequired: m.applicationRequired, channel: m.channel, actionUrl: m.actionUrl,
    documents: m.documents, validTo: m.validTo, npa: m.npa,
    urgency: m.urgency || null, reason: r.reason,
  }
}

export function runMatchEngine(profile, catalog) {
  const facts = buildFacts(profile)
  const matched = []
  const skipped = []
  for (const m of catalog) {
    const r = matchMeasure(m, facts)
    if (r.ok) matched.push(toDto(m, r))
    else if (r.missing.length) {
      skipped.push({ measureId: m.id, name: m.name, missing: r.missing.map((p) => MISSING_LABEL[p] || p) })
    }
  }
  return { matched, skipped }
}
