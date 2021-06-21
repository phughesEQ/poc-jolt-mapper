# Dynamic Integration Mapper

A POC of dynamic schema driven mapping for integrations using JOLT

## Getting Started

These instructions will give you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
 * [JDK 8+](https://www.oracle.com/uk/java/technologies/javase-downloads.html)
 * [Maven](https://maven.apache.org/)

### Installing
To install the dependencies run the following command
```shell
$ mvn install
```

### How To Run

In `src/main/kotlin/main.kt`, run the main function. This will start a service on port `9000` which can be called through
postman. 

**Example Health Check Request**
```json
{
  "url": "http://localhost:9000/api/healthz",
  "method": "POST"
}
```

**Example Health Check Response**
```json
{
  "Status": 200,
  "Body": "Status Okay"
}
```

**Example Json Transformation Request**
```json
{
  "url": "http://localhost:9000/api/transformJson",
  "method": "POST",
  "body": {
    "job_code": "ExampleSpec",
    "lead_data": {
      "current_policy": {
        "street_address": "123 adga",
        "occupation": "Doctor",
        "drivers": [
          {
            "first_name": "Homer",
            "last_name": "Simpson",
            "city": "Schenectady",
            "postal_state": "NY"
          }
        ]
      }
    }
  }
}
```

**Example Json Transformation Response**
```json
{
  "Status": 200,
  "Body": {
    "lead": {
      "contact": {
        "addressLine1": "123 adga",
        "city": "Schenectady",
        "fullName": "Homer Simpson",
        "occupation": "Surgeon",
        "state": "NY"
      }
    }
  }
}
```

## Running Kubernetes Locally

### Prerequisites

* [Docker](https://www.docker.com/products/docker-desktop)
* [Minikube](https://minikube.sigs.k8s.io/docs/start/) `brew install minikube`
* [Kubectl](https://kubernetes.io/docs/tasks/tools/) `brew install kubectl`
* [Octant](https://octant.dev/) `brew install octant`

### Instructions

Note: The service can be run via the main function in `src/main/kotlin/main.kt` but if you want to run with kubernetes 
run the following commands
```shell
$ minikube start --cpus 2 --memory 1800  
$ kubectl apply -f kubernetes/mapper-service.yaml
$ octant
```
Octant will launch a UI in your web browser to monitor your cluster and pods. You can retrieve the URL for the cluster in Octant

## Running The Tests

Run the follow command `mvn test` or right-click the package `src/test/kotlin` and select run all tests 

## Built With

* [Kotlin](https://kotlinlang.org/) - The language used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Jolt](https://github.com/bazaarvoice/jolt) - JSON mapping tool
* [HTTP4k](https://www.http4k.org/) - Used as a lightweight HTTP toolkit
* [JUnit](https://junit.org/junit5/docs/current/user-guide/) - Testing framework

