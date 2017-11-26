# Discovery Service

## Requirements

* Crypt engine to verify signatures

## Task definition

The discovery service is a service where a entity can query for a provider. 
This is done by mapping the ethereum address to a Public-key, IP-Address and Port.
With this information the entity can request the defined ProviderAPI to communicate
off-blockchain with other providers. Each provider shell on boot-up register himself
to this discovery service to participate in the network.

In the first version this Service shell be implemented centralized, but in that way
that it can be replaced or migrated to an decentralized component. 

## Components

### Discovery Service

* Implement hash-map
  * mapping ethAddress to public key, IP (Domain) and port
* validate new/updated provider address
* provide API where user can find proider IP based on the ethereum address

### User

#### Logic

* implement logic to query discovery service

### Provider

##### Logic

* implement logic to register to discovery service
