# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v2:
story gate passed (non-deploy story): pipeline + quality gate green. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 27 model sessions.

- Outcome: story gate passed (non-deploy story): pipeline + quality gate green
- Supervisor version: a05691f2; run base: 67a1f0bff82d79a20ac9cd989bdf3afbaf566af7
- Orchestrator: custom:maas-m2/minimax-m2; worker: qwen27b/qwen3-6-27b

## Sessions

| session | seconds | rc |
|---|---|---|
| m3-lint-a1p0 | 133 | rc=0 |
| m3-lint-sfix-w | 271 | rc=0 |
| m3-lint-sfix-r1 | 368 | rc=0 |
| T-003-a1p0 | 254 | rc=0 |
| m5-evaluate-a1p0 | 204 | rc=130 |
| m5-evaluate-a1p0 | 321 | rc=0 |
| preflightfix-r1-a1p0 | 153 | rc=137 |
| preflightfix-r1-a1p0 | 508 | rc=137 |
| retro | 117 | rc=0 |
| T-004-sfix-w | 332 | rc=0 |
| T-004-sfix-r1 | 902 | rc=124 |
| T-008-a1p0 | 56 | rc=0 |
| T-008-sfix-w | 849 | rc=143 |
| T-008-sfix-r1 | 901 | rc=124 |
| m5-evaluate-a1p0 | 225 | rc=0 |
| m5-evaluate-a1p1 | 689 | rc=0 |
| preflightfix-r1-a1p0 | 167 | rc=0 |
| preflightfix-r2-a1p0 | 707 | rc=130 |
| preflightfix-r2-a2p0 | 37 | rc=137 |
| retro | 53 | rc=0 |
| T-003-sfix-w | 317 | rc=0 |
| T-003-sfix-r1 | 518 | rc=137 |
| m5-evaluate-a1p0 | 136 | rc=0 |
| preflightfix-r1-a1p0 | 310 | rc=0 |
| preflightfix-r2-a1p0 | 903 | rc=124 |
| deployfix-r1-a1p0 | 210 | rc=130 |
| deployfix-r1-a2p0 | 193 | rc=137 |

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
      9 success
      7 preflight_red
      6 rule:javax-to-jakarta-import-00001
      6 no_commit
      5 escalation_cause
      4 sfix_worker_first
      4 sfix_minimax_rescue
      4 sensor_red_post_commit
      4 already_complete
      3 story_gate_pass
      3 pipeline_succeeded
      2 style_autofix
      2 sfix_committed_still_red
      2 rule:springboot-annotations-to-quarkus-00002
      2 quota
      2 mechanical_commit
      2 debt_recorded
      1 worker_wedge_class
      1 pipeline_failed
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| `javax-to-jakarta-import-00001` | already_complete, already_complete, mechan, worker_green, mechan, worker_green |
| `springboot-annotations-to-quarkus-00002` | already_complete, already_complete |
