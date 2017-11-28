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
  * List of linked entities (**that user has data with**) for easy access (maybe read provider entries from database and match with distributed table).
  Match each attribute to the provider who sent this claim (probably to the domain). Since after registration we receive a jsonblob with all attributes a provider has, we should be able to link all of these attributes to this entity, to act as a filter for the frontend
    * List of attributes to be changed
    * New value field
* Main view
  * *Optional: Display status of change-attribute. Asynchronous process on the side of the provider and user should be informed whether the change is still ongoing, has been accepted or denied*

#### Database
* Replace jsonblobs on updates
* *Have to define the model for each separate jsonblob ourselves*

#### Logic
* Forward change-attribute_DATA jsonblob to Database
  * verify that only requested attribute was changed?
  * verify changed attribute
  * verify signiture of provider, who changed the data
* Forward change-attribute_REQUEST to Frontend

#### Provider API
* Send change-attribute request

#### Blockchain client
* Approve attribute-change contract
* Poll for new change attribute contracts

### Provider

#### Database
* Change attributes for requesting user to received values

#### Logic
* verify if change is valid (e.g.: for change name, there should be a "heiratsurkunde" in the "bürgeramt")
* Forward change-attribute_OK to Database
* check if user is authorzied to change certain attributes (**user should only be able to change certain attributes himself**)
* trigger blockchain client to set up change attribute contract

#### Blockchain client
* Create attribute-change contract
* Issue a reasonable amount
* Poll for updates

#### Provider API
* Offer change_attribute endpoint
