# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v2:
story gate passed (non-deploy story): pipeline + quality gate green. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 8 model sessions.

- Outcome: story gate passed (non-deploy story): pipeline + quality gate green
- Supervisor version: 240c4386; run base: aa320bd6b266fe1b56d2184dd624adb23c391e61
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

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
      4 success
      2 preflight_red
      2 no_commit
      1 story_gate_pass
      1 sfix_worker_first
      1 sfix_minimax_rescue
      1 sensor_red_post_commit
      1 pipeline_succeeded
      1 escalation_cause
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| _(none recorded)_ | |
