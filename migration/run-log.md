# Harness run log

Appended by the Hermes orchestrator after every task (see
`.hermes/skills/migration-harness/`). One line per task.

| Task | Class | Attempts | Result | Files |
|---|---|---|---|---|
| M5 evaluate: Final findings delta analysis and POM rule resolution | evaluate | 1 | COMPLETED | migration/findings-delta.txt |
| | | | | |
| **M5 EVALUATION DETAILS:** | | | | |
| RESOLVED (13 rules - story credit): | | | | |
| - javaee-pom-to-quarkus-00060, javax-to-jakarta-dependencies-00001/00003 | | | | |
| - spring-components-00001/00002, springboot-* 8 rules | | | | |
| - All show evidence in src/main/java AND absent in after-scan | | | | |
| | | | | |
| ABSENT-NOT-LANDED (13 rules - NO credit): | | | | |
| - hibernate-00005, javax-to-jakarta-import-00001, localhost-jdbc-00002 | | | | |
| - oracle2openjdk-00006, persistence-to-quarkus-00010 | | | | |
| - springboot-* 7 rules (annotations, di, jmx, properties, webmvc, transaction) | | | | |
| - No src/main/java evidence - deferred to later stories per O-M5EVALHARVEST | | | | |
| | | | | |
| SCAFFOLD-PRESATISFIED (9 rules - NO credit): | | | | |
| - javaee-pom-to-quarkus-00010/00020/00040, springboot-* 6 rules | | | | |
| - Destination already satisfied by existing Quarkus config | | | | |
| | | | | |
| REMAINING (2 rules - RESOLVED IN THIS EVAL): | | | | |
| - javaee-pom-to-quarkus-00030 (Maven Compiler with -parameters) | | | | |
| - javaee-pom-to-quarkus-00050 (Maven Failsafe integration) | | | | |
| | | | | |
| NEW IN AFTER (2 rules - out of scope): | | | | |
| - demo-env-integration-00001, jakarta-jaxrs-to-quarkus-00010 | | | | |
| - Not related to this story's Owns (src/main/java, pom, props) | | | | |
| | | | | |
| METRICS: src_main_java=0 src_test_java=0 residual_incidents=6 | | | | |
| Honest resolve: 13/28 = 46.4% (excludes absent-not-landed, scaffold) | | | | |
| | | | | |
| POM rule resolution: Added compilerArgs -parameters to maven-compiler-plugin | | | | |
| POM rule resolution: Added maven-failsafe-plugin with native image support | | | | |
| | | | | |
| **PREFLIGHT SENSORS:** | | | | |
| - task sensor: GREEN (clean test, isolated repo) | | | | |
| - fidelity sensor: GREEN (harvest fidelity GREEN) | | | | |
| - sonar sensor: TIMED OUT (60s limit exceeded) | | | | |
| - Build verification: mvn -q clean verify PASSED | | | | |