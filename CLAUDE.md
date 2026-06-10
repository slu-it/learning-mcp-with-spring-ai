# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
./gradlew build        # compile + test
./gradlew test         # run tests only
./gradlew bootRun      # start the MCP server on localhost:8080
./gradlew clean build  # clean then full build
```

Requires Java 21. Start Keycloak before running the server:

```bash
docker compose up -d   # Keycloak on localhost:9000
```

## Architecture

This is a Spring Boot 4 / Spring AI learning project that exposes an OAuth2-secured MCP server over HTTP.

**Request flow**: Claude Code → `POST /mcp` → Spring Security (JWT validation via Keycloak) → MCP dispatcher → `@McpTool`-annotated methods in `WhatsAppTools`

### Key components

- `tools/WhatsAppTools.kt` — The only business logic. Two `@McpTool` methods: `get_phone_number_of_contact` (in-memory map) and `send_whatsapp_message`. Both log the authenticated user's JWT claims.
- `config/SecurityConfiguration.kt` — Two filter chains: actuator chain (permit-all for `/.well-known/**`, `/error`) and MCP chain (requires `SCOPE_mcp:tools` on `/mcp/**`). OAuth2 resource server trusts Keycloak at `http://localhost:9000/realms/master`.
- `application.yml` — Sets MCP transport to `STATELESS` (HTTP, not SSE) and wires OAuth2 issuer/JWK URIs.

### Keycloak setup (first-time only)

Per the README: create a client scope named `mcp:tools` (type: Default), add an audience mapper for `http://localhost:8080`, disable client URI matching, and add trusted hosts.

Register with Claude Code:
```bash
claude mcp add --transport http local-testserver http://localhost:8080/mcp
```

## Stack

- Kotlin + Spring Boot 4.0 + Spring AI 1.1 (MCP server WebMVC starter)
- Gradle 9 with Kotlin DSL (`build.gradle.kts`)
- OAuth2 Resource Server (JWT) backed by Keycloak
- Single smoke test (`ApplicationSmokeTests.kt`) that loads the Spring context
