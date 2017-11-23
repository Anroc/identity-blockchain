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

#### Blockchain client

# Discovery Service

## Requirements

* Crypt engine to verify signatures

## Task definition

The discovery service is a service where a entity can query for provider. 
This is done by mapping the ethereum address to a Public-key, IP-Address and Port.
With this information the entity can request the defined ProviderAPI to communicate
off-blockchain with other providers. Each provider shell on boot-up register himself
to this discovery service to participate in the network.

In the first version this Service shell be implemented centralized, but in that way
that it can be replaced or migrated to an decentralized component. 

## Components

### Discovery Service

* Implement hash-map
  * mapping ethAddress to public key, IP and port

### User

#### Logic

* implement logic to query discovery service

### Provider

##### Logic

* implement logic to register to discovery service

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

-----------------

## Logic (Deeper definement)

* Define Provider API Server (*Receive user information from the IDP*)
  * RestAPI
    * `GET: /ethereum/{ethID}/` Provides all information about an adress 
      * `?closure=false` (True|Flase) if a closer shell be generated
    * `GET: /ethereum/{ethId}/claims/{claimId}` Provides all information about a specific claim of a speciic ethereumId
      * `?closure=false` (True|Flase) if a closer shell be generated
    * `POST: /ethereum/{ethId}/claims` Inizialise the claim creation process
    * `PUT: /ethereum/{ethId}/claims/{claimId}` Inizialise the claim change process

* *Create Public/Private key pair*
  * Implement Crypt Engine
    * asymmetric encryption
      * *can verify signatures*
      * *decrypt response*
      * *encrypt message*
    * symmetric encryption
      * *uses PIN of user to encrypt private key*
  * Generate strong keys
  * Save and encrypt key with users PIN in file or database

* Security definments
  * SHA 256 for hashing
  * 2048-Bit RSA key crypto
  * User information needs to be encrypted
  * Information must be stored in a secure way
  * Secure against replay attacks

------------------

# eID MockUp

## Prerequirements:

* Client
  * has encrypted signed message
  * signed with private key of government
  * has public key of government
  * knows his PIN
* Government
  * Has private/public key

## Setup:

* Client
  * Signed (private Key of Government)
    * message ("It's me!")
  * symmetric encrypted (PIN) attributes

* eID Verification Service
  * private key (Government)

## Flow

1. Client de-encrypts information with PIN
2. Client generates nonce
3. Client encrypts nonce and Signed message with public key of Goverment
4. Government decrypts message
5. verifies that nonce was not yet used
6. verifies signature
7. Sends success message
