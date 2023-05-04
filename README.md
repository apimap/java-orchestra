Apimap.io Orchestra API (Account and Access Management)
===

🎉 **Welcome** 🎉

This is the home of the Apimap.io project, a freestanding solution to keep track of all functionality a company
provides through an API. It is a push based system, connected with your build pipeline or manually updated using our CLI.

> **Application programming interface (API)**: Point of functional integration between two or more systems connected
> through commonly known standards

**Why is this project useful?** Lost track of all the API functionality provided inside your organization? Don't want
to be tied to an API proxy or management solution? The Apimap.io project uploads, indexes and enables discoverability of all
your organizations APIs. We care about the source code, removing the limitation of where the API is hosted and how your
network is constructed.

## Table of Contents

* [Project Components](#project-components)
* [Run](#run)
* [Contributing](#contributing)

I want to know more of the technical details and implementation guides: [DEVELOPER.md](DEVELOPER.md)

## Project Components
___
This is a complete software solution consisting of a collection of freestanding components. Use only the components you
find useful, create the rest to custom fit your organization.

- A **Developer Portal** with wizards and implementation information
- A **Discovery Portal** to display APIs and filter search results
- An **API** to accommodate all the information
- An **Orchestra API** to manage accounts and resource access
- A **CLI** to enable manual information uploads

Plugins
- A **GitHub Action** to automate information parsing and upload
- A **Jenkins plugin** to automate information parsing and upload
- An **IntelliJ plugin** to make it easier to write metadata and taxonomy files

## Run
___
[![Artifact Hub](https://img.shields.io/endpoint?url=https://artifacthub.io/badge/repository/apimap)](https://artifacthub.io/packages/search?repo=apimap)

We primarily recommend the following two methods of running the application:
- Locally using bootRun
- From our published Docker image

### Locally using bootRun

Based on Spring Boot, all the usual targets exist. The easiest way to get started is using **bootRun**

> gradlew bootRun

#### From our published Docker image

It is possible to use the image "as-is", although a mongodb compatible database is required.

> docker run -p 8080:8080 apimap/orchestra

#### Requirements

MongoDB compatible database. We use Azure Cosmos DB and MongoDB 6.0.0

##### Public and Private Key

The JWTs issued from this service has to be signed with a public/private key pair. 

**IMPORTANT:  Remember to define the algorithm used in the config file**

#### Configuring the Docker Image

We love "build once deploy anywhere" and all configuration is done using the Spring configuration properties system.

You can find our default configuration in the application.yaml file, and we recommend that you override using environment variables.
More information about Spring and the configuration system is available at [docs.spring.io](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)

Setting faq and support urls using **JSON Application Properties**
> docker run -p 8080:8080 --env SPRING_APPLICATION_JSON='<content>' apimap/orchestra

```json
{
  "mongodb.enabled": "true",
  "mongodb.database-name": "apimap",
  "mongodb.uri": "mongodb://apimapUser:<password>@localhost:27017/apimap",
  "apimap.metadata.copyright": "The Apimap.io project",
  "apimap.metadata.faq": "https://www.apimap.io",
  "apimap.metadata.support": "https://www.apimap.io",
  "apimap.host-identifier.enabled": "true",
  "apimap.openapi.enabled": "true",
  "apimap.zeroconf.endpoints.orchestra": "<url>",
  "apimap.zeroconf.endpoints.api": "<url>",
  "apimap.oidc.issuer": "https://apimap.io",
  "apimap.oidc.algorithm.public-key-file": "public_key.der",
  "apimap.oidc.algorithm.private-Key-file": "private_key.der",
  "apimap.oidc.algorithm.algorithm": "RS256",
  "apimap.oidc.issuers": [
    {
      "issuer": "https://token.actions.githubusercontent.com",
      "openid-configuration": "https://token.actions.githubusercontent.com/.well-known/openid-configuration",
      "jwks": "https://token.actions.githubusercontent.com/.well-known/jwks",
      "audience": "<audience defined in github settings>",
      "decoder": "github",
      "allowed-projects": []
    },
    {
      "issuer": "https://apimap.io",
      "openid-configuration": "<url>/.well-known/openid-configuration",
      "audience": "apimap",
      "jwks": "<url>/.well-known/jwks.json",
      "decoder": "apimap",
      "allowed-projects": []
    }
  ],
  "apimap.oidc.idps": [
    {
      "idp": "github",
      "authorize-endpoint": "https://github.com/login/oauth/authorize",
      "access-token-endpoint": "https://github.com/login/oauth/access_token",
      "secret": "<secret provided by github>",
      "client-id": "<client-id provided by github>",
      "scope": "read:user",
      "default-provider": "true"
    }
  ]
}
```

## Contributing
___

Read [howto contribute](CONTRIBUTING.md) to this project.