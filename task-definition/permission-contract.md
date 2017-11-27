# Permission Contract

## Setup

### Requirements
_please add new task description and complete requirements_

## Requirements

* Meta-Epic
    * Plain frontend setup
    * Existing database
    * Communication with database
    * Key generation
    * Blockchain client
    * REST-API for off-blockchain communication

### Task description

Scope of the permission contract is to set up everything mandatory for both
requesting and answering permission.
The permission request process means that a 3rd party asks the provider 
for the permission to use the user's data. The provider then forwards the request
to the user through the blockchain.

The permission request will be visible in the blockchain and logged in all parties'
databases.

The answering process involves the user approving or denying the request and
sending that answer off-blockchain to the provider, who will then forward the
information to the provider.


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

* Use other party's url
* _what do you mean with party_
* polling permission request
* Permission Approval
    * Create smart contract answering
    permission request
    * establish connection to 3rd party directly
    * transfer demanded information


### Provider

#### Frontend

* none

#### Database

* save 3rd party's permission request
* save user's permission answer
* (save potential query results)

#### Blockchain client

* Permission request:
    * Create smart contract asking for user's permission of sharing certain data
* poll user permission response

#### Logic

1. Set up
    * Define permission specific REST-API
    * Implement smart contract with asking for permission
    * Implement database querying interface
2. Connect it
    * Trigger permission contract when 3rd party asks for data and save
        it to db
    * Asynchronously wait for user's answer
    * When answer is denial, forward denial to 3rd party
    * When answer is approval, query database and send results together with
        approval to 3rd party

### 3rd party

#### Frontend

* give option to request permission from user (from own database)
    to simulate 3rd party
* section where answer is displayed