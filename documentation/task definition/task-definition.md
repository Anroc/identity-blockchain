# eID MockUp

## Prerequirements:

* Client
  * has encrypted signed message
  * signed with private key of goverment
  * has public key of goverment
  * knows his PIN
* Goverment
  * Has private/public key

## Setup:

* Client
  * Signed (private Key of Goverment)
    * message ("It's me!")
  * symmetic encrypted (PIN) attributes

* eID Verification Service
  * private key (Goverment)

## Flow

1. Client dencryptes information with PIN
2. Client generates nonce
3. Client encrypts nonce and Signed message with public key of Goverment
4. Goverment decrypts message
5. verifies that nonce was not yet used
6. verifies signature
7. Sends success message

# Register Contract

## Prerequirements:

* Authorized Adress Dictionary
* Authorized Key Dictionary

* Exisiting
  * database
  * provider API
  * ethereum client
  * eID verification mechanism
  * basic fontend


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

* Create Public/Private key pair
* Receive user information from the IDP
* Verify signature
* Dencrypt response
* create eID login

#### Blockchain client

* Create eth Addres
* use private key pair from logic
* Create the smart contract
  * IDP can only approve
* Issue a reasonable amout

### Provider

#### Database

* Query users attributes
* Save users identificators (ethID, pubKey)

#### Logic

* Create public/private key pair
* Verify eID of client
* Verify Signature of request
* Encrypt response
* Send user claims to user

#### Blockchain client

* Create eth Address
* use private key pair from logic
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

* Securty definments
  * SHA 256 for hashing
  * 2048-Bit RSA key cryto
  * User information needs to be encrypted
  * Information must be stored in a secure way
  * Secure against replay attacks
