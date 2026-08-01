# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v2:
story gate passed (non-deploy story): pipeline + quality gate green. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 45 model sessions.

- Outcome: story gate passed (non-deploy story): pipeline + quality gate green
- Supervisor version: 0ca6de81; run base: 597d44fa1a071d3837fa46a122b4b6792bb5a4f8
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
| retro | 116 | rc=0 |
| T-003-sfix-w | 333 | rc=137 |
| T-003-sfix-r1 | 529 | rc=137 |
| T-005-a1p0 | 772 | rc=0 |
| T-005-sfix-w | 154 | rc=137 |
| T-005-sfix-r1 | 91 | rc=137 |
| treefix | 341 | rc=0 |
| m5-evaluate-a1p0 | 22 | rc=137 |
| m5-evaluate-a1p0 | 153 | rc=0 |
| m5-evaluate-a2p0 | 200 | rc=0 |
| preflightfix-r1-a1p0 | 177 | rc=137 |
| preflightfix-r1-a1p0 | 307 | rc=137 |
| preflightfix-r1-a2p0 | 118 | rc=130 |
| preflightfix-r2-a1p0 | 28 | rc=137 |
| preflightfix-r2-a2p0 | 16 | rc=137 |
| gatefix-r1-a1p0 | 26 | rc=137 |
| gatefix-r1-a2p0 | 43 | rc=137 |
| retro | 19 | rc=137 |

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
     15 no_commit
     11 success
     10 preflight_red
      7 escalation_cause
      6 sfix_worker_first
      6 sfix_minimax_rescue
      6 sensor_red_post_commit
      6 rule:javax-to-jakarta-import-00001
      6 already_complete
      4 style_autofix
      4 story_gate_pass
      4 rule:springboot-di-to-quarkus-00003
      4 pipeline_succeeded
      3 sensor_gate_refuse_checkpoint
      3 rule:transaction-to-quarkus-00003
      3 debt_recorded
      2 worker_wedge_class
      2 sfix_committed_still_red
      2 rule:springboot-annotations-to-quarkus-00002
      2 quota
      2 pipeline_failed
      2 mechanical_commit
      1 sfix_spring_reintro
      1 sensor_red_at_entry
      1 debt_retained
      1 commit_hygiene_reset
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| `javax-to-jakarta-import-00001` | already_complete, already_complete, mechan, worker_green, mechan, worker_green |
| `springboot-annotations-to-quarkus-00002` | already_complete, already_complete |
| `springboot-di-to-quarkus-00003` | worker_green, worker_green, mechan, already_complete |
| `transaction-to-quarkus-00003` | mechan, already_complete, already_complete |
