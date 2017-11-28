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
  * 
2. [Feature] Logic/Frontend: Forward claim information to Frotnend
  * Map Intern representation into DTO
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
    * Response object:
    ```JSON
    {
      claims: [
        {
            "value": "mustermann",
            "mac": "HASH_OVER_VALUE",
            "claimId": "FAMILY_NAME_CLAIM"
        },
      ...
      ]
    }
    ```
6. [Feature] Logic: Get EthID from EBA
7. [Feature] Logic: Query the Discovery Service for connection information  

Permission Contract

1. [Feature] Logic: Implement PPR request
  **Third Party**
  * Define DTO
  * Create client to communicate with Provider

  **Provider**
  * Create API to receive PPR request from thrid parties

6. [Feature] Logic: Trigger EBA to create PPR Contract
  **Provider**
  * Define interface to trigger EBA with needed paramter

  **User**
  * Define Subscriber to get triggered if contracts received

2. [Feature] Logic: Generate Signed Query from user
  **User**
  * [ ] Provide possibilty to push events to frontend
  * [ ] Initizalise PPR to frontend (let user select attributes)
  * [ ] Receive query attributes from frotnend
3. [Feature] Logic: Generate Signed Query from database
  **User**
  * Define interface to database
  * forward attributes to database
  * receive query
  * sign it
5. [Feature] Logic: Forward PPR object to EBA
  **User**
  * Define interface
  * Request object:
    ```JSON
    {
      "ppr": {	
        "query": "SELECT * FROM $DATABASE;",
        "mac": "MAC_VALUE"
      }
      "provider": "PROVIDER_ETH_ADDR",
      "reuse": true
    }
    ```
  **Thrid Party**
  * Define subscriber that gets triggered if signed query is found in 
    blockchain
6. [Feature] Logic: Define query API
  **Provider**
  * Define an API that can receive signed queries and executes them
    on the database
  * Sign result
  * Return encrypted result
  * Additional: Save that the provider queried against the database
    Use this information to check if the provider already queried 
    and deny him if the resuse flag was not set
  **Thrid Party**
  * Define client that sends signed query

Change Claim Contract

1. [Feature] Logic: Provide endpoint to receive ChangeClaim requests
  **User**
  * Define API to frontend
  * Map and forward request to Provider
  * Sign request
  * Encrypt request

  **Provider**
  * Define API on Provider API
  * Check if request is authorized to do so (e.g. if it is the user)
  * Check if request is valid
    * Check condition to be a verified request

2. [Feature] Logic: Create Change Claim Contract
  **Provider**
  * Define interface to request EBA to create a change claim contract
    with the defined claim ID
  * Define interface to receive response from EBA
  * evaluate response --> on approval adapt change in DB

  **User**
  * Receive information about change from EBA
  * Forward information to frontend
  * Define API to receive approval/denial
  * Forward response to EBA

3. [Feature] Logic: Send user updated claims
  **User**
  * Requests provider to receive new updated claims
  * If this fails retry

4. [Feature] Logic: Thridparty queries provider again
  **Thrid party**
  * The thrid party notice update via EBA
  * uses signed query to query provider again

Clousur Register Contract

1. [Feature] Logic: Request new clouser
  **Provider**
  * Provide API to receive request
  * Save who made this request
  * Define interface to EBA with needed information

  **Usser**
  * Receive clouser request from EBA
  * Forward request to frontend
  * Receive approval/denial
  * Push response to EBA

2. [Feature] Logic: Create Clouser
  * on successfull approval of clouser contract
  * generate new clouser
  * With
    * Timestamp
    * ethID
    * mac
    * Condition
    * result
  * Provide Clouser for user
  * Save clouser in database
  * **TODO:** Refine: flow is wired

3. [Feature] Logic: Provide API to request clouser for users
  **Provider**
  * Define API that is able to provide clousers for users on request

  **User**
  * Extend client to query for clousers




