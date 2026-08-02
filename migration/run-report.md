# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v2:
success: shipped, route 200, 6 _array. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 27 model sessions.

- Outcome: success: shipped, route 200, 6 _array
- Supervisor version: 7eb38eb9; run base: 2d413fe
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
| retro | 53 | rc=0 |
| T-002-a1p0 | 66 | rc=130 |
| T-002-a2p0 | 6 | rc=137 |
| T-007-a1p0 | 112 | rc=130 |
| T-001-a1p0 | 111 | rc=130 |
| m5-evaluate-a1p0 | 354 | rc=0 |

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
     12 escalation_cause
     10 rule:springboot-web-to-quarkus-00000
     10 rule:springboot-di-to-quarkus-00003
      9 rule:springboot-security-to-quarkus-00000
      9 no_commit
      8 success
      6 style_autofix
      6 sensor_red_post_commit
      4 worker_wedge_class
      4 rule:oracle2openjdk-00006
      4 already_complete
      3 sfix_worker_first
      3 pipeline_succeeded
      3 debt_recorded
      2 sfix_minimax_rescue
      2 sensor_gate_refuse_checkpoint
      2 scope_violation
      2 quota
      2 preflight_red
      2 orphan_worker
      2 debt_retained
      2 acceptance_pass
      1 sfixscope_reset
      1 sfix_worker_green
      1 rule:springboot-webmvc-to-quarkus-00000
      1 rule:springboot-metrics-to-quarkus-0200
      1 rule:springboot-jmx-to-quarkus-00001
      1 rule:springboot-di-to-quarkus-00002
      1 mechanical_commit
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| `oracle2openjdk-00006` | mechan, escw, worker_green, worker_green |
| `springboot-di-to-quarkus-00002` | worker_green |
| `springboot-di-to-quarkus-00003` | mechan, escw, escalation, worker_green, worker_green, escalation, worker_green, worker_green, exhausted, escalation |
| `springboot-jmx-to-quarkus-00001` | escalation |
| `springboot-metrics-to-quarkus-0200` | already_complete |
| `springboot-security-to-quarkus-00000` | worker_green, exhausted, escw, already_complete, worker_green, worker_green, already_complete, already_complete, worker_green |
| `springboot-web-to-quarkus-00000` | mechan, escw, escalation, worker_green, worker_green, escalation, worker_green, worker_green, exhausted, escalation |
| `springboot-webmvc-to-quarkus-00000` | worker_green |
