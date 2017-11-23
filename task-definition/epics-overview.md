# Overview of epics

## Mandatory

1. meta epic
2. discovery serice
3. registrar contract
4. permission contract
5. closure registrar contract
6. change claim contract

## Optional

* device-loss / recovery (quorum device contract)
* (approval contract ?)

## General definements

* Security definments
  * SHA 256 for hashing
  * 2048-Bit RSA key crypto
  * User information needs to be encrypted
  * Information must be stored in a secure way
  * Secure against replay attacks

-------------

# eID MockUp

## Prerequirements:

* Client
  * has encrypted signed message
  * signed with private key of government
  * has public key of government
  * knows his PIN
* Government
  * Has private/public key

## Setup:

* Client
  * Signed (private Key of Government)
    * message ("It's me!")
  * symmetric encrypted (PIN) attributes

* eID Verification Service
  * private key (Government)

## Flow

1. Client de-encrypts information with PIN
2. Client generates nonce
3. Client encrypts nonce and Signed message with public key of Goverment
4. Government decrypts message
5. verifies that nonce was not yet used
6. verifies signature
7. Sends success message

