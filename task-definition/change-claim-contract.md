## Pre-requirements:
* Authorized Address Dictionary
* Authorized Key Dictionary
* Existing:
  * database
  * basic frontend
  * ethereum client
  * off-chain communication API

## Task Description
Define an API endpoint in the off-chain communication API to send a change attribute request to. This API endpoint processes the received information and accesses the "change-attribute" smart contract.
This smart-contract is used to verify the attribute change. After the user verifies this smart-contract there can be no doubt, that the attribute change is verified and accepted. A failed attribute-change has no repercussions for third parties, because the provider entity only changes the attribute if the contract is verified by the user and the change is also allowed by the provider.

## Components

### User

#### Frontend

* Provide change attribute view
  * List of linked entities (**that user has data with**) for easy access (maybe read provider entries from database and match with distributed table)
    * List of attributes to be changed
    * New value field
* Main view
  * Display status of change-attribute

#### Database
* Replace jsonblobs on updates
  * _Verify that only requested attribute was changed?_
* Provide list of provider-entities user has data with and "position" in database

#### Logic
* Forward change-attribute_DATA jsonblob to Database
  * _Verify that only requested attribute was changed?_
* Forward change-attribute_REQUEST to Frontend

####PrivComm API
* Send change-attribute request

#### Blockchain client
* Approve attribute-change contract
* Poll for new contracts

### Provider

#### Database
* Change attribute for requesting user to received values
* Attribute whether an attribute is changeable itself (**user should only be able to change certain attributes himself**)

#### Logic
* Forward change-attribute_OK to Database

#### Blockchain client
* Create attribute-change contract
* Issue a reasonable amount
* Poll for updates

#### PrivComm API
* Offer change_attribute endpoint
