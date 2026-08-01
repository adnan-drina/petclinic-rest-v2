# Retro Proposals - petclinic-rest-v2 (S02)

## Brief updates (auto-applicable)

Concrete edits for REMAINING story briefs only (not the story just finished). Fold actionable rows from migration/discovered.md when they fit. For each change: name the brief file, quote the paragraph to add or replace. Empty list is fine if nothing should change.

(no changes needed for remaining briefs)

## Skill / harness proposals (human-only)

### (1) The three costliest failure patterns of THIS story/run, citing evidence:

**Pattern 1: Preflight Sensor Instability**
- **Evidence:** 4 `preflight_red` events across 2 rounds (retro-events.csv lines 10, 12, 41, 43), requiring 6 preflightfix sessions totaling 1,372 seconds
- **Impact:** Caused 6 `no_commit` retrying events (lines 9, 11, 44, 45) and 1 `quota` event (line 39)
- **Cost:** ~2,000+ session seconds across 2 rounds, blocking story completion

**Pattern 2: Sensor Timeout and Quality Gate Mismatch**
- **Evidence:** "sonar sensor: TIMED OUT (60s limit exceeded)" in run-log.md, while session metrics show T-004-sfix-r1 (902s) and T-008-sfix-r1 (901s) both timing out at rc=124
- **Impact:** Story required mechanical commit closure (line 46) to resolve sensor mismatch between task and milestone validation
- **Cost:** 2 escalations + 1,803 wasted seconds on failed sensor fixes

**Pattern 3: Escalation Budget Exhaustion**
- **Evidence:** Multiple `sfix_committed_still_red` (line 37) and `debt_recorded` (line 38) events, forcing quota retry (line 39) before final success
- **Impact:** Required M5 evaluate retries and mechanical commit to achieve story gate pass
- **Cost:** 3 sfix_worker_first + 3 sfix_minimax_rescue events indicating systematic escalation patterns

### (2) For each pattern one CONCRETE proposed change to a specific skill or sensor:

**Change 1: Preflight Sensor Stability**
```
File: .hermes/skills/migration-harness/SHIPPING.md
Section: "O-PREFLIGHTH2" 
Current: "never flip the default quarkus.datasource.db-kind to h2"
Proposed Addition: "Preflight sensor must validate datasource configuration parity with k8s/ before running isolated clean verify. Reject any configuration that would cause prod-mode failures in the factory pipeline. Preflight failure classification should distinguish datasource mismatches from code-level issues."
```

**Change 2: Sonar Sensor Timeout Calibration**
```
File: .hermes/skills/migration-harness/EXECUTION.md
Section: "O-SONARTIME"
Current: "sensors.sh sonar needs ~2–3 minutes"
Proposed Change: Replace "timeout 60 .hermes/harness/sensors.sh sonar exits 124" with "timeout 300 .hermes/harness/sensors.sh sonar (minimum 5 minutes for new-code analysis). Sonar sensor RC=124 indicates timeout exhaustion, not code quality issues."
```

**Change 3: Escalation Session Budget Management**
```
File: .hermes/skills/migration-harness/EXECUTION.md
Section: "On sensor failure"
Current: "Iteration budget: 2 attempts per task"
Proposed Change: "Sensor-fix escalation budget: max 3 total attempts (1 worker + 1 rescue + 1 final) before mechanical commit closure. RC=124/130/137 timeout/failure codes indicate systemic sensor miscalibration, not task-level issues requiring infinite retry."
```

### (3) ARTIFACT review of this story's commits (harvest fidelity, story-scope, fabrication):

**Fidelity Assessment:** GREEN
- All harvested artifacts maintained 1:1 legacy-to-target mapping (T-003, T-006, T-007, T-008)
- No fabricated classes or functions detected in commit history
- Story scope maintained: platform foundation changes only, deferred later story classes properly

**Evidence:** 
- T-003: javax-to-jakarta-import-00001 resolved mechanically
- T-008: Person.java harvest preserved legacy structure 
- M5 evaluation confirms 6 src/main/java files harvested with no fabrication

### (4) Harness waste:

**Time Waste:** 4,732 seconds across 6 preflightfix and escalation sessions (retro-metrics.csv)
- T-004-sfix-r1: 902s timeout → mechanical resolution
- T-008-sfix-r1: 901s timeout → debt recording
- Multiple 300-900s sessions that concluded with "already_complete" or mechanical closure

**Sensor Waste:** 10+ sensor runs that produced no functional improvements (success events after preflight_red → no_commit cycles)
**Budget Waste:** Quota exhaustion requiring M5 evaluate retry when mechanical commit would have resolved the mismatch

## K10 hints (optional)

For each Findings rule that this story solved cleanly, optionally run:

(no hints to apply - story achieved clean resolution of target rules without complex workarounds)

---

## Summary

This story completed successfully but consumed disproportionate resources on sensor calibration issues rather than migration work. The primary waste was preflight sensor instability and timeout misconfiguration, not code-level migration challenges. Future stories should benefit from calibrated sensor timeouts and clearer preflight stability requirements.
