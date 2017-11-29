# Epics

# Setup & Discovery
* Basic database setup
* Connection DB and springboot
* Database layout for users and providers/third parties
  * same or different?
* *Optional: Database for discovery service*

## registrar-contract
### User
* Able to save verified user claims

### Provider
* Query users attributes and corresponding ethereum addess
* Save user's identificators (ethID, pubKey)

## permission-contract
### User
* define model layout specifically for permission requests
* potential attributes might be:
    * what was asked for
    * who was involved in the request
    * what are their addresses
    * what role are you in it
    * was is approved/denied/other_option
    * status (e.g. resolved/pending)

### Provider
* save 3rd party's permission request
* save user's permission answer
* (save potential query results)

## closure-registrar-contract
### User
* save closure
* save claims
* define queries regarding which provider provides which closures

### Provider
* Query users attributes
* maybe safe closure of certain user

## change-claim-contract
### User
* Replace jsonblobs on updates
* *Have to define the model for each separate jsonblob ourselves*

### Provider
* Change attributes for requesting user to received values

# Summary across all epics

## User
* save closures and claims in form of json files to database []
  * additional required information:
    * which provider provides which claim or closure
  * create model for each separate provider initial-data-json that is sent during the registrar-contract process
  * create an abstract model that fits all types of json-closures which are received during the closure-registrar-contract
  * define model layout specifically for permission requests:
    * what was asked for
    * who was involved in the request
    * what are their addresses
    * what role are you in it
    * was is approved/denied/other_option
    * status (e.g. resolved/pending)

## Provider
* Query user attributes and corresponding ethereum addess
* Change attributes for requesting user to: received values
* Save created closures for users
* Save 3rd party's permission request
* Save user's permission answer
  * Save potential query results
* Save user's identificators (ethID, pubKey)
