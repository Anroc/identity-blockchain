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

------------------

