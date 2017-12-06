# Setup

## Requirements
* setup registrar epic
* setup meta epic
* setup discovery service

## Task description

The goal of this task is to enable the closure functionality.
This includes:

* the creation of closure
* their belonging claims (and the creation of those claims),
* the definition, where to find the closure and who is authorized to sign the corresponding closure,
* the creation of a closure request,
* the storing of closures in the database on client side and the creation on provider side
* the interaction between the user and at least one provider 
	* the storing of the closure request within the blockchain and how the user get's the request
	* storing the approval to create a closure and also how the provider get's the approval
* the possibility for the user to accept or approve the creation of a closure
* and the possibility for the user to see all closures regarding him

## Components

### Provider (who wants a closure)

#### Logic
* define endpoint to initialize closure request
* handle user's request which either contains a closure or the address of a provider who verifies the closure
	* if no closure, send closure request to provider address


### User

#### Frontend
* create a view where the user sees all his closures
* create a view where the user sees all the closure requests and where he can deny or accept those requests

#### Database
* save closure
* save claims
* define queries regarding which provider provides which closures

#### Logic
* manage interaction
	* handle response and identify necessary closures
	* if closure exists send closure, if not send verifying provider adresse
	* provide interface which receive a closures
* push user claims and closures into database
* get user claims and closures from the db and forward it to the FE
* manage user decision if want's to accept or deny a closure request 

#### Blockchain client
* create approval response and forward it to blockchain
* implement pulling of closure request from blockchain


### Provider (provider who verifies data)

#### Database
* Query users attributes
* maybe safe closure of certain user

#### Blockchain provider

* create closure request and forward it to blockchain (contract)
* implement pulling of approval response from blockchain (contract)

#### Logic

* provide interface where other providers can send their closure requests
* verifiy closure request (is user >18) an therefore get data from db
* check user's response whether he agreed on the closure request
* create closure with [userid, condition, result, timestamp]
* send closure to user / alternatively provide api where user can get closure
