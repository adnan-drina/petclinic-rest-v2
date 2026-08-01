# S01: Platform Foundation

## Goal & position

Establish the Quarkus platform foundation that enables all subsequent migrations. This story sets up the proper Maven POM with Quarkus BOM, replaces Spring Boot parent, adds Quarkus plugins, and migrates basic properties. This is the necessary foundation work that must precede any class-level changes per dependency order.

## In scope

The exact legacy files this story modernizes:

- `pom.xml` — Maven configuration with Spring Boot parent and dependencies
  ```xml
  <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.6.2</version>
      <relativePath/> <!-- lookup parent from Maven repository -->
  </parent>
  ```
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```
  ```xml
  <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
  </plugin>
  ```

- `src/main/resources/application.properties` — Application configuration
  ```properties
  server.port=9966
  server.servlet.context-path=/petclinic/
  spring.profiles.active=hsqldb,spring-data-jpa
  logging.level.org.springframework=INFO
  petclinic.security.enable=false
  ```

## Out of scope

All source code files. This story only touches build configuration and properties. The application code will remain unmodernized until S02-S07.

## Class roles & target contract

No classes in scope for this story — only POM and configuration files.

## Decided target shapes

The MAPPINGS.md rows that apply:

- **javaee-pom-to-quarkus-00010**: Adopt Quarkus BOM — scaffold pom conventions: platform BOM, pinned quarkus/compiler/surefire/failsafe plugins, native profile, quarkus junit
- **javaee-pom-to-quarkus-00020**: Adopt Quarkus Maven plugin — scaffold pom conventions: platform BOM, pinned quarkus/compiler/surefire/failsafe plugins, native profile, quarkus junit
- **springboot-parent-pom-to-quarkus-00000**: Replace Spring Parent POM with Quarkus BOM
- **springboot-plugins-to-quarkus-0000**: Replace spring-boot-maven-plugin with quarkus-maven-plugin (pinned)
- **springboot-annotations-to-quarkus-00000**: Delete `@SpringBootApplication` + main class (will be handled by S02)
- **springboot-properties-to-quarkus-00002**: Replace Spring datasource properties with Quarkus equivalents
- **springboot-properties-to-quarkus-00003**: Replace Spring log level properties with Quarkus equivalents
- **javax-to-jakarta-dependencies-00001**: Replace javax groupId with jakarta in dependencies
- **javax-to-jakarta-dependencies-00003**: Replace javax.xml.bind with jakarta.xml.bind-api

## Contracts owned by this story

- **Findings**: All pom.xml and properties-related mandatory rule ids from the roadmap entry
- **Preserve**: The `preserve:` items whose configuration surfaces live in scope:
  - `petclinic.security.enable=false` mechanism to keep security disabled
  - `server.servlet.context-path=/petclinic/` to maintain API path
- **Behavioral pins**: None - this is foundation/configuration work only
- **Forbidden**: None

## Done-criteria

Checkable, story-scoped:
- Maven POM updated with Quarkus BOM and proper plugin configuration
- Properties migrated to Quarkus equivalents where applicable
- Project builds successfully with `mvn clean compile`
- No Spring Boot parent or Spring Boot dependencies remain (except compatibility mode ones)
- Recipe-executed findings (javax→jakarta) resolved for dependencies
