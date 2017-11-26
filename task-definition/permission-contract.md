# Setup

## Requirements

* Meta-Epic
* specifially: 
    * 

## Task description

Scope of this task is it to setup all nessecary infrastructure. This includes the 
setup of a database, setup of the ethereum adapter and implementing a cypht engine.
When this epic is done it shell be possible to interact with the different components:
Logic, EBA and database. The frontend itself sell be ready to start implementing a
web view. 

## Components

### User

#### Frontend

* Add functionality for answering permission
    * approval
    * denial
    * third option?
* Add functionality for creating permission request
    * should contain:
        content what is requested
* render specific section for requesting permission
* render specific section for permission approval

* Define REST-API and define JSON layout specifically designed
    for permission requests

#### Database

* define model layout specifically for permission requests
* potential attributes might be:
    * what was asked for
    * who was involved in the request
    * what are their addresses
    * what role are you in it
    * was is approved/denied/???
    * status (e.g. resolved/pending)

#### Logic

* verify permission author
* generate permission requests
* let user interact through frontend and wait for answer
* answer permission requests
* push permission requests into database

#### Blockchain client

* Use generated ethereum address
* Use other party's url
* Use basic pulling from blockchain
* Permission Request
    * Create smart contract asking for 
    user's permission of sharing certain data
* Permission Approval
    * Create smart contract answering
    permission request
    * establish connection to 3rd party directly
    * transfer demanded information


### Provider

#### Database

* save permission request

#### Logic

* Define permission specific REST-API
