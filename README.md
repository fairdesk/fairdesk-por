# Proof of Reserves
## Background

Fairdesk launches Proof of Reserve (PoR) to improve the security and transparency of user assets. These tools will allow you to independently audit Fairdesk’s Proof of Reserves as well as verify that Fairdesk’s reserves have exceed the exchange’s known liabilities to all users to confirm Fairdesk’s solvency.

## Introduction
### Build from source

[Download] (https://www.oracle.com/java/technologies/downloads/)Install JDK(Java Development Kit)  
[Download] (https://maven.apache.org/download.cgi.)Install Maven build tool

The minimum prerequisite to build this project requires Java version >= 8, Maven version >= 3.8.4

### Package and compile source code
#### Enter the path for the project
`cd ~/Downloads/fairdesk-por`

#### Install dependencies
`mvn clean package`

#### Start up
`java -jar target/faidesk-merkletree-validator.jar path/to/binfile yourhash`
