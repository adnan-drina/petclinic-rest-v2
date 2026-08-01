# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v2:
story gate passed (non-deploy story): pipeline + quality gate green. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 19 model sessions.

- Outcome: story gate passed (non-deploy story): pipeline + quality gate green
- Supervisor version: 240c4386; run base: db2f12778b1c52b27d128950d6150534ef665fa5
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

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
      7 success
      6 rule:javax-to-jakarta-import-00001
      4 preflight_red
      4 no_commit
      4 already_complete
      3 sfix_worker_first
      3 sfix_minimax_rescue
      3 sensor_red_post_commit
      2 story_gate_pass
      2 rule:springboot-annotations-to-quarkus-00002
      2 pipeline_succeeded
      2 escalation_cause
      1 style_autofix
      1 sfix_committed_still_red
      1 quota
      1 mechanical_commit
      1 debt_recorded
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| `javax-to-jakarta-import-00001` | already_complete, already_complete, mechan, worker_green, mechan, worker_green |
| `springboot-annotations-to-quarkus-00002` | already_complete, already_complete |
