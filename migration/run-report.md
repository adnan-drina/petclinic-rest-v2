# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v2:
success: shipped, route 200, 6 _array. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 21 model sessions.

- Outcome: success: shipped, route 200, 6 _array
- Supervisor version: 7eb38eb9; run base: 535589e
- Orchestrator: custom:maas-m2/minimax-m2; worker: qwen27b/qwen3-6-27b

## Sessions

| session | seconds | rc |
|---|---|---|
| T-001-a1p0 | 213 | rc=0 |
| T-001-a1p1 | 507 | rc=0 |
| T-001-sfix-w | 323 | rc=0 |
| T-001-sfix-r1 | 136 | rc=0 |
| T-001-sfix-w | 70 | rc=143 |
| T-002-a1p0 | 341 | rc=0 |
| T-004-sfix-w | 900 | rc=124 |
| T-004-sfix-r1 | 902 | rc=124 |
| T-005-a1p0 | 290 | rc=0 |
| T-007-a1p0 | 1227 | rc=0 |
| T-007-a1p1 | 785 | rc=0 |
| T-009-a1p0 | 416 | rc=0 |
| T-009-a2p0 | 105 | rc=130 |
| T-010-a1p0 | 845 | rc=130 |
| T-010-a2p0 | 57 | rc=130 |
| m5-evaluate-a1p0 | 169 | rc=0 |
| preflightfix-r1-a1p0 | 651 | rc=0 |
| preflightfix-r2-a1p0 | 19 | rc=137 |
| preflightfix-r2-a2p0 | 45 | rc=130 |
| deployfix-r1-a1p0 | 559 | rc=137 |
| deployfix-r1-a2p0 | 50 | rc=130 |

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
     10 rule:springboot-web-to-quarkus-00000
     10 rule:springboot-di-to-quarkus-00003
      7 escalation_cause
      6 success
      6 no_commit
      5 style_autofix
      5 sensor_red_post_commit
      4 rule:oracle2openjdk-00006
      3 worker_wedge_class
      3 sfix_worker_first
      3 debt_recorded
      2 sfix_minimax_rescue
      2 scope_violation
      2 quota
      2 preflight_red
      2 pipeline_succeeded
      2 orphan_worker
      1 sfixscope_reset
      1 sfix_worker_green
      1 sensor_gate_refuse_checkpoint
      1 mechanical_commit
      1 debt_retained
      1 acceptance_pass
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| `oracle2openjdk-00006` | mechan, escw, worker_green, worker_green |
| `springboot-di-to-quarkus-00003` | mechan, escw, escalation, worker_green, worker_green, escalation, worker_green, worker_green, exhausted, escalation |
| `springboot-web-to-quarkus-00000` | mechan, escw, escalation, worker_green, worker_green, escalation, worker_green, worker_green, exhausted, escalation |
