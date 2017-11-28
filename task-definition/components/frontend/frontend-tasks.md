# Frontend Tasks

## Content

1. Meta Epic
2. Registrar Contract
3. Change Claim Contract
4. Closure Registrar Contract
5. Discovery Service
6. Permission Contract

## 1. Meta Epic

* [ ] Setup a frontend server to render web requests (react, angular etc)
* [ ] Define REST-API

or

* [ ] Read and understand how to setup web pages in Spring MVC


## 2. Registrar Contract

* [ ] Provide register view
  * [ ] password field to encrypt database and access client
* [ ] Main view
  * [ ] Display success/failure of claim verification

## 3. Change Claim Contract

* [ ] Provide change attribute view
  * [ ] List of linked entities (**that user has data with**) for easy access (maybe read provider entries from database and match with distributed table).
  Match each attribute to the provider who sent this claim (probably to the domain). Since after registration we receive a jsonblob with all attributes a provider has, we should be able to link all of these attributes to this entity, to act as a filter for the frontend
    * [ ] List of attributes to be changed
    * [ ] New value field
* [ ] Main view
  * [ ] *Optional: Display status of change-attribute. Asynchronous process on the side of the provider and user should be informed whether the change is still ongoing, has been accepted or denied*


## 4. Closure Registrar Contract


* [ ] create a view where the user sees all his closures
* [ ] create a view where the user sees all the closure requests 
    and where he can deny or accept those requests


## 5. Discovery Service

* [ ] none, maybe show logs?

## 6. Permission Contract

### Sub-component User

* [ ] Add functionality for answering permission
    * [ ] give options: approval/denial/potential_third_option
* [ ] Add functionality for creating permission request
    * [ ] should contain content what is requested
* [ ] render specific section for requesting permission
* [ ] render specific section for permission approval
* [ ] Define REST-API and define JSON layout specifically 
    designed for permission requests

#### Provider

* [ ] none, maybe show logs?

#### 3rd party

* [ ] give option to request permission from user (from own database)
    to simulate 3rd party
* [ ] section where answer is displayed