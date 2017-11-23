# Setup

## Requirements

* None

## Task description

Scope of this task is it to setup all nessecary infrastructure. This includes the 
setup of a database, setup of the ethereum adapter and implementing a cypht engine.
When this epic is done it shell be possible to interact with the different components:
Logic, EBA and database. The frontend itself sell be ready to start implementing a
web view. 

## Components

### User

#### Frontend

* Setup a frontend server to render web requests
* Define REST-API and define JSON layout

or

* Read and understand how to setup web pages in Spring MVC

#### Database

* Evaluate database types Schema less vs Relational
* Implement SpringBoot database adapter
* Define model layout
* Need to be able to save models in Database

#### Logic

* Generate Public/Private keys
* Verify signatures
* Generate signatures
* push user claims into database

#### Blockchain client

* Create ethereum address
* Use private key of logic
* Evaluate what is possible with SmartContracts and Transactions
* Implement basic pulling from blockchain

### Provider

#### Database

* Query users attributes

#### Logic

* Define REST-API to send user claims
