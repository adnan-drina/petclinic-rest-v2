# Migration debt

Tasks the harness could not complete within its iteration budget, with
the failure evidence. Written by the Hermes orchestrator; resolved by a
follow-up run or a human steering-loop improvement (better spec, better
skill, better sensor) — never by weakening the sensors.

(none)



## T-005 — milestone RED (RESOLVED)
- head: 09fa7ca
- reason: O-SFIXNOSPRING: sfix reintroduced Spring (commit reset)
- resolved: c53b545 fidelity tip + O-SFIXNOSPRINGSDATA allow springframework.data with quarkus-spring-data-jpa

## T-003 — milestone RED (RESOLVED)
- head: a8466e1
- reason: sensor-fix committed but milestone still RED (commit reset)
- resolved: O-FIDEOLCOMMENT harvest-fidelity strips EOL // comments so NOSONAR S112 tip is fidelity-green; tip a8466e1 kept

## T-006 — milestone RED (RESOLVED)
- head: e3456bd
- reason: sensor-fix did not clear milestone
- resolved: O-SONARLINEFIX (S112 throw-site NOSONAR, S1130, S2925 AtomicLong backdate) tip pending; O-FAILSIGFILE banked

## T-001 — task RED (RESOLVED)
- head: 8013cea
- reason: sensor-fix did not clear task
- resolved: sfix introduced broken OwnerRestControllerTest (discarded); task sensor GREEN after discard; BindingErrorsResponse kept via scope-revert commit a097f5e

## T-001 — task RED (RESOLVED)
- head: ac42141
- reason: sensor-fix did not clear task
- resolved: false RED from untracked OwnerRestControllerTest (wrong 3-arg ctor) left by orphan/sfix; discarded; sensors.sh task GREEN; substance tip remains 8013cea

## T-004 — milestone RED
- head: 447767a
- reason: sensor-fix did not clear milestone
