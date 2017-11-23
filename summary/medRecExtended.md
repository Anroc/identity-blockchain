# MedRec

### Motivation

* unify patient access (clear and easy) to health data 
across providers and treatment sites
    * first step in improving ability to make smart decisions
* facilitate patient-initiated data-sharing
    * patients could authorize 3rd party to view their information
* anonymized, aggregated "big data"
    * incentive system for healthcare industry stakeholders
    * provide data as mining rewards and secure via Proof of Work
    
### Goal

* no novel EHR system
* use already existent data structures 
(e.g. string queries, protocols)
    * achieved with database gatekeeper module
* overall a backend system design


### Why Blockchain

* transaction based state-machine
* Ethereum is first attempt of full implementation of this idea
    * Turing-complete instruction set
        * important property for EHR management
    * smart contracts contain metadata about ownership, permissions,
    and data integrity

### Why Ethereum

* MedRec is a fork of ethereum
* a privatized, small-scale blockchain with extensive APIs built
on top
* problem because of "hard" forks

Fokus auf wie die smart contracts aussehen.

### Implementation

#### 4 mayor issues:

* fragmented, slow access to own medical data
* lack of system interoperability
* lack of patient agency over health care data
* need for improved data quality and quantity for medical research

* hashed pointers are in the ledger
* on-chain-permissioning and data integrity logic
* not the panacea but an innovative approach with blockchain

* block content:
    * data ownership
    * viewership pemissions
    * smart contracts allow tracking of changing viewership rights
    * essentially data pointers to external databases
    * providers can add medical records and patient can authorize
        * keeps participants informed in their evolution of their data
    *
* DNS-like implementation, map widely accepted ID with Ethereum adress
* syncing algo handles data exchange between provider and patient "off-chain"

### MedRec System Architecture

#### Registrar Contract

* policies coded into the the contract can regulate registering
new identities or changing the mapping of existent ones
* restricted to certified institutions
* maps identity strings to an address on the blockchain (summary contract)

#### Patient-Provider Relationship Contract

* one node stores & manages medical records for the other
* defines data pointers and associated access permissions
    * each pointer consists of query string which should
    affix the hash of his data subset for guarantee of non-alteration
* also hostname and port
* queries are added and modified by care provider
* hash table maps viewers' addresses to a list of additional query strings

#### Summary Contract

* bread crumb trail for participants in the system
* holds references to PPRs, representing all previous and current relations
* providers have references to patients they serve and third-parties with 
whom data-sharing is enabled    
* as long as there are nodes in the network, the blockchain is maintained

#### System Node Description

* 4 components:
    * backend library
    * Ethereum Client    
    * Database Gatekeeper
    * EHR Manager
* patient nodes contain same components as providers
* can be executed on local PC and phone
* missing data can be retrieved from the network by following the
summary contract 
* Figure 3-4

#### Primary System Components

##### Backend API

* p 38 ???

##### Ethereum Client

* handles broad set of tasks (connecting to network, encoding and sending 
transactions )
* PyEthereum and PyEthApp client

* RemoteSQLiteService key of Database Gatekeeper
    * defines network syncing service, that allows transfer of content
    of SQLite instances
        * first checks identity by requesting information from blockchain
        and its access rights
        * there restricting queries occur (currently very open-ended)
        * after confirming access, syncing service returns requested data from
        source database
        * script is SQLite specific, whereas logic is encapsulated (you can
        use other databases if needed)
        * RPyC to establish connections and transfer data
* SQLiteSyncingClient other half
    * invoke methods and sync returned records into local app
        * separated by wheter we writing to "owners" or 3rd party
        viewers' records
    * updates are handled, new data retrieved
* Setup__init creates test patients and generates sample data
    
    
    
    
    











