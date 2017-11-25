# Setup

## Requirements


## Task description

The goal of this task is to enable the clouser functionality.
This includes:

* the creation of clouser
* their belonging claims (and the creation of those claims),
* the definition, where to find the clouser and who authorized to sign the corresponding clouser,
* the creation of a clouser request,
* the storing of clousers in the database on client side and the creation on provider side
* the interaction between the user and at least one provider 
	* the storing of the clouser request within the blockchain and how the user get's the request
	* storing the approval to create a clouser and also how the provider get's the approval
* the possibility for the user to accept or approve the creation of a clouser
* and the possibility for the user to see all clousers regarding him

## Components


### Provider (who want's a clouser)

#### Logic
* provide services in the form of api?
* define, which kind of clouser is necessary for each service
* handle user's request which either contains a clouser or the adress of a provider who verifies the clouser
	* if no clouser, send clouser request to provider adress


### User

#### Frontend
* create an view where the user see all clouser
* create an view where the user see all the clouser requests and where he can deny or accept those requests

#### Database
* save clouser
* save claims
* define queries regarding requesting provider

#### Logic
* manage interaction
	* send service request
	* handle respsonse and identifiy necessary clouser
	* if clouser exists send clouser, if not send verifying provider adresse
	* provide interface which get's all the clousers
* push user claims and clousers into database
* get user claims and clousers from the db and provide's it for the FE
* manages users decision if want's to accept or deny a clouser request 

#### Blockchain client
* create approval response and forward it to blockchain
* implement pulling of clouser request from blockchain


### Provider (provider who verifies data)

#### Database
* Query users attributes
* maybe safe clouser of certain user

#### Blockchain provider

* create clouser request and forward it to blockchain (contract)
* implement pulling of approval response from blockchain (contract)

#### Logic

* provide interface where other providers can send their clouser requests
* verifiy clouser request (is user >18) an therefore get data from db
* check user's response whether he agreed on the clouser request
* create clouser with [userid, condition, result, timestamp]
* send clouser to user / alternatively provide api where user can get clouser
