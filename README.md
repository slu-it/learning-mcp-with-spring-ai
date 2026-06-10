# Learning: MCP with Spring AI

This repository contains code I've written to implement an MCP server using Spring AI.

## Local Setup

The MCP server is secured using OAuth2.
The setup for that is based on https://modelcontextprotocol.io/docs/tutorials/security/authorization#keycloak-setup.

Keycloak can be started using Docker Compose with [docker-compose.yml](docker-compose.yml):

* defines a volume to keep data between sessions
* server is available under localhost:9000

### First Time Setup

There are several different resources to configure to set up Keycloak.
To make things easy, there is a setup script in the form of a [HTTP client](development/keycloak-setup/initial-setup.http).
After starting Keycloak for the first time using Docker Compose, you can execute this client script ("run all") using the `dev` environment.

### Testing with Claude Code

To test if the MCP works using Claude Code, you can register it like this:

```
claude mcp add --transport http learning-mcp-with-spring-ai http://localhost:8080/mcp
```

When starting Claude afterward, the MCP server will be in the "needs authentication" state (check with the`/mcp` command).
Starting the authentication process should open a browser window where Keycloak asks you to approve the registration.
After that the MCP server should be usable.
You can test that by asking Claude "What audiobook am I currently listening to?".
