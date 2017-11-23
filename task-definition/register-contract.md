# Register Contract

## Pre-requirements:

* Authorized Address Dictionary
* Authorized Key Dictionary
* Existing:
  * database
  * provider API
  * Ethereum client
  * eID verification mechanism
  * basic frontend


## Task Description

Define a Smart Contract that issues a valid and verified user ID.
After this contract is successfully executed there should be no 
doubt that the given user ID refereed in this contract is a valid
person in the manner of the European (German) law.
If the contract fails the user ID verification is taken as failure
and can not taken as a verified entity in the system.
The Issuer shell be the government or an similar, verified Identity Provider.

## Components

### User

#### Frontend

* Provide register view
  * Password to encrypt database and access client
* Main view
  * Display success/failure of claim verification


#### Database

* Able to save verified user claims

#### Logic

* Receive user information from the IDP
* create eID login

#### Blockchain client

* Create the smart contract
  * IDP can only approve
* Issue a reasonable amount

### Provider

#### Database

* Query users attributes
* Save users' identificators (ethID, pubKey)

#### Logic

* Verify eID of client
* Send user claims to user

#### Blockchain client

* Approve Register Contract
