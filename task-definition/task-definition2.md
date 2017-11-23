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




-----

Setup:

1. [Feature] Logic: Implement crypt engine
  1. move crypt engine from other project into here
    * Implement tests
    * Improve error handling
    * Implement possible of signing JSONs
  2. Implement syncron encryption/decryption
  3. Save key pairs in file

2. [Feature] Logic: Implement Error handler (springBoot)
  1. Implement Exception and error handler

3. [Task] Database/Logic: Discuss Database interface 
  * @Micha

4. [Feature] ProviderAPI: Define user claim endpoint
  1. `GET: /ethereum/{ethID}/` Provides all information about an adress 
      * `?closure=false` (True|Flase) if a closer shell be generated
  2. `GET: /ethereum/{ethID}/claims/{claimId}` Provides all information about a specific claim of a speciic ethereumId
      * `?closure=false` (True|Flase) if a closer shell be generated

Discovery Serivce:

1. [Feature] Discovery-Service: Implement Key-Value Service
  1. Implement hashmap listening on defined port 
    * Mapping ethAddress to public key, IP and port
    * Rest API
    * new Springboot micro service
  2. Define endpoint `GET: /provider/{ethID}` 
    * Return object 
    ```JSON
    {
      "ethID": "ETHEREUM_ID_OF_PROVIDER",
      "publicKey": "PUBLICKYE_VALUE_BASE64",
      "ip": "127.0.0.1",
      "port": 3001,
      "mac": "HASH_OF_ALL_VALUES_SIGNED"
    }
    ```
  3. Define endpoint `POST: /provider`
   * Request object:
   ```JSON
    {
      "ethID": "ETHEREUM_ID_OF_PROVIDER",
      "publicKey": "PUBLICKYE_VALUE_BASE64",
      "ip": "127.0.0.1",
      "port": 3001,
      "mac": "HASH_OF_ALL_VALUES_SIGNED"
    }
    ```
  3. Define endpoint `PUT: /provider/{ethID}`
   * Request object:
   ```JSON
    {
      "ethID": "ETHEREUM_ID_OF_PROVIDER",
      "publicKey": "PUBLICKYE_VALUE_BASE64",
      "ip": "127.0.0.1",
      "port": 3001,
      "mac": "HASH_OF_ALL_VALUES_SIGNED"
    }
    ```
    Each field is optional (except mac)

Register Contract

1. [Feature] Logic: Implement rest client to request provider API
2. [Feature] Logic/Frontend: Forward claim information to Frotnend
3. [Feature] Logic/Database: Save user claims
4. [Feature] Logic/Database: Query user claims by ethId
5. [Feature] Logic: (eID) Receive ethID, public key
  * save them in database
  * define endpoint `POST: /users`
    * Request object:
    ```JSON
    {
      "ethID": "ETHEREUM_ID_OF_USER",
      "publicKey": "PUBLICKEY_OF_USER",
      "mac": "HASH_OVER_ALL_VALUES"
    }
    ```


