Apimap.io Orchestra API (Account and Access Management)
=====

🥳 **Happy Coding** 🥳

This section is targeted to developers that want to communicate with the API directly.

## Table of Contents

* [Introduction](#introduction)
* [Getting Started](#getting-started)
* [Common Workflows](#common-workflows)
* [MongoDB Setup](MONGODB.md)
* [Other Resources](#other-resources)

## Introduction

## Getting Started

### Build and Run

Based on Spring Boot, all the usual targets exist. The easiest way to get started is using **bootRun**

> gradlew bootRun

#### Build JAR

Based on Spring Boot, all the usual targets exist. The easiest way to build the artifacts is using **build**

> gradlew build

#### Build Docker Image

Building the Docker Image is a two-step process based on the [official Spring Boot documentation](https://docs.spring.io/spring-cloud-dataflow-admin-cloudfoundry/docs/1.2.x/reference/html/_deploying_docker_applications.html
). Fist we have to unpack the jar file, then build the image itself.

> **Step 1:** mkdir -p build/dependency && (cd build/dependency; cp ../libs/api.jar .; jar -xf api.jar)

> **Step 2:** docker build -t apimap/orchestra .

#### Requirements

- MongoDB 
- Public/Private Keys

## Common Workflows
___

### Integration with GitHub and GitHub Actions

This system supports OpenID Connect integration with the following SaaS providers:
- GitHub

### SSO

This system supports SSO integration with the following SaaS providers:
- GitHub

### JWT (Api-api)

The different parts of the Apimap solution used JWT (JSON Web Token) to manage scopes and access permissions to the APIs

## API Errors

When errors occur the server will respond with a status code and a json. The json has the error object array and contain multiple feedbacks to explain what failed.

```json
{
  "links":{},
  "meta":{},
  "jsonapi":{
    "version":"1.1"
  },
  "errors":[
    {
      "status":"409 CONFLICT",
      "title":"The resource exists already"
    }
  ]
}
```


## Other Resources
___

- [Hypermedia as the Engine of Application State (HATEOAS) ](https://en.wikipedia.org/wiki/HATEOAS)
- [JSON:API — A specification for building APIs in JSON](https://jsonapi.org/)