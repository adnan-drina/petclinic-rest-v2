# S05: Service Layer Modernization - Plan

## Overview

Convert Spring `@Service` classes to Quarkus `@ApplicationScoped` CDI beans with native constructor injection. All tasks are `infer` class (judgment: DI conversion, transaction management, caching redesign).

## Findings Coverage

**springboot-di-to-quarkus-00003** — Apply Quarkus Spring DI conversion guidance:
- `ClinicServiceImpl.java:47` (`@Service`) and `:58` (`@Autowired` constructor) — owned by T-003
- `UserServiceImpl.java:10` (`@Service`) and `:13` (`@Autowired` field) — owned by T-004

Repository incidents (S04), REST controller incidents (S06), security incidents (separate story), and utility incidents (separate story) are out of scope.

## Conversion Map

| Legacy | Target | Class |
|---|---|---|
| `ClinicService` interface | `com.demo.service.ClinicService` (preserve signatures) | infer |
| `ClinicServiceImpl` | `com.demo.service.ClinicServiceImpl` (`@ApplicationScoped`, CDI constructor, `@Transactional`, thread-safe caching) | infer |
| `UserService` interface | `com.demo.service.UserService` (preserve signatures) | infer |
| `UserServiceImpl` | `com.demo.service.UserServiceImpl` (`@ApplicationScoped`, CDI constructor, `@Transactional`) | infer |

## Key Decisions

1. **Caching**: `@Cacheable(value = "vets")` on `findVets()` replaced with thread-safe **ConcurrentHashMap** cache with **refresh-guard** per architecture-profile §7
2. **Exception handling**: Spring `DataAccessException` replaced with application-level exceptions; `ObjectRetrievalFailureException`/`EmptyResultDataAccessException` caught and return `null`
3. **Transaction management**: `org.springframework.transaction.annotation.Transactional` → `jakarta.transaction.Transactional`
4. **Injection**: All constructor injection via CDI (no `@Inject` annotation needed on constructor)

## Preserve Items

- `petclinic.security.enable` — security configuration preserved as Quarkus property
- `server.servlet.context-path` — context path preserved as `quarkus.http.root-path`

## Acceptance Path

The acceptance path `/petclinic/api/vets` is deferred to the deploy story (O-M3ACCEPT / S-AC1 / G-OK).
