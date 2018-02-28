# BlockID

Blockchain-Identity is a project to manage your digital identity in the ethereum blockchain. 

### Checking out the source code

To check out the source code clone the project and access the `/source` directory. Here you find the overview about all implemented modules:

Web-Client:
* Client-Forntend: The frontend is using an node server and is powered react.

Andorid-Client (Approval application):
* BlockIDClientQRScanner: Android application build for Android Lollipop (SDK-27) is an application to scan the QR-Code from the user and approve the user in the government database.

Backend:
* Core-Logic: Saves the shared component between provider and user module.
* lib: Utility functions like error handling and exception processing. 
* Crypt-Engine: Spring boot independent cryptographic engine to encrypt, decrypt, building a SHA-256 hash over various objects. Supports symmetric as well as asymmetric crypto.
* Provider: Bootable service containing the logic of the provider (e.g. government, or other provider)
* User: Bootable service containing the logic of the user
* DiscoveryService: Bootable service containing the message router and heart beat logic.

### Building and Run

#### Requirements

We are organizing our components in docker container. Please download and install docker to use them https://www.docker.com/.

##### Backend

* **Java 8** The project is implemented in **Java 8**. It will not work with Java 9 since we included Lombok. Lombok is a plug-in that reduces the boilerplate code in java, like setter,
getter, constructors, toString-, hashCodes- or equal-methods. It is still lacking support for Java 9 so don't try to compile it with Java 9. 
* **Lombok** However, to install [Lombok](https://projectlombok.org/) using IntelliJ IDEA simply install the IntelliJ plug-in (search for lombok). Also enable annotation processing in the IDEA settings.
To install Lombok on eclipse see the installation section of [Lombok](https://projectlombok.org/).
* **Gradle** Further the project is bootstrapped with Gradle. You need a Gralde version higher then **4.3**. If you don't want to install Gradle use the Gradle-wrapper instead (`gradlew`, `gradlew.bat`).

##### Android Application

* **Android SDK 27** To compile the android application you need the android SDK version 27 (min 23) as well as Java 8 and Gradle. 

##### Web-Frontend

* **NodeJS** If you want to start or build the frontend you need node in a version 7.8 or higher. 

#### Building
##### Backend
To build the backend simply run `gradle bootRepackage` this will build the project without running the tests. On successful compiling the `./build/libs` directory will contains a runnable spring boot jar. 
However this jars (user and provider) are bootable but will likely fail to initialize since they need:

1. a running DiscoveryService at http://srv01.snet.tu-berlin.de:8101 
2. a local couchbase instance,
3. a running ethereum entry point at http://srv01.snet.tu-berlin.de:7111. 

The discovery services does not need any dependencies except couchbase.

To configure the services have a look at the `./src/main/resouces/application.yml`. 
All included varibales can also be configured by environment variables. 
Here an example overview about the default configurations:

```yaml
spring:
  profiles: default                      # name of the profile
  couchbase:
    bootstrap-hosts: localhost           # where to look for the couchbase environment
    bucket:
      name: default                      # name of the bucket (needs to be same as the user name in couchbase)
      password: password                 # password of the bucket
blockchain.identity:
  core:
    port: 8080                           # used for discovery of this client 
    address: ${user.name}                # provider need to put the URL here under which they are accessable
  discoveryService:
    port: 8101                           # port on which the discovery service is running
    address: srv01.snet.tu-berlin.de     # URL of the discovery service
  ethereum:
    port: 7111                           # port of the Test-RPC (can be any JSON-RPC ethereum interface)
    address: srv01.snet.tu-berlin.de     # URL of the Test-RPC
````

###### Couchbase (locally)
To boot couchbase locally referrer to this documentation: https://hub.docker.com/_/couchbase/

Please create a default bucket with the name `default` and password `password` and create a user with full access rights to this bucket. The user needs to have
the same name as the bucket and the same password set. 

We further need two indexes. A `_class` index to efficient query every document based on its `_class` attribute and a `primary` index for general querying. 
The primary index is quite slow, but since we were restricted on memory usage on our VM we are not able to create other indexes to improve query time.  

Create this indexes with the following commands:

* `Primary index`: `CREATE PRIMARY INDEX ON default;`
* `_class index`: ``CREATE INDEX `CLASS_INDEX` ON `default`(`_class`)``

Keep in mind that you can also setup a volume to save the couchbase configuration. 

###### Test-RPC (locally/remote)

The test RPC can be booted also via a docker container. Please refer to this [documentation](https://github.com/trufflesuite/ganache-cli#docker).
We further use the following parameter to ensure 0 gas price per transaction see and since we are creating own accounts we do not need the default 
accounts. Account 0 is the coinbase account and is used in the code. 

```bash
docker run -d -p 7111:8545 --name TestRPC-dev trufflesuite/ganache-cli:latest -a 1 --debug --gasPrice 0
````

You would have to further adapt the `application.yml` to use the local Test-RPC.
If you don't want to boot the test-RPC locally you can use the port 7111 (which is configured in the `application.yml` per default) on http://srv01.snet.tu-berlin.de.

##### Frontend

Running the frontend is very easy. 

1. Run `npm install`. This will take a while.
2. Run `npm start`. Node is now listening on port http://localhost:3000

The frontend is default configured to talk to the user service at http://srv01.snet.tu-berlin.de:1112 and the provider service 
at http://srv01.snet.tu-berlin.de:8102

##### Android App

We recommend using the Android Studios to run the android application. Due to incompatibilities between of web3j we didn't manage to bootstrap
a standalone application. So you will need to run the application from [Android Studios](https://developer.android.com/studio/index.html) directly.

### Deployed docker container

It consist of 7 components each running on http://srv01.snet.tu-berlin.de :

* User-Service (port 1112): Is the instance that can run locally on the client machine. However, the frontend is currently hard coded to the server URL. 
* Government-Service (port 8100): Holding the signed version of the user claims.
* Provider-Service (port 8102): Representing an provider requesting claims from the government service. 
* Discovery-Service (port 8101): Message router and public key holder. Can be used to discover new services and has a present-knowledge.
* Frontend (port 1111): Running the npm instance of the frontend server. Serves as the UI for bank or user. 
* TestRPC (port 7111): Here the Test-RPC is running. It will provide the JSON-RPC interface that also ethereum is providing.
* Couchbase (port 8091): Couchbase is our database storage for all the User, Government, and Provider service. 

Currently out of service:
* Portainer (port 9000): Portainer provides a GUI to manage all running docker container. 
* TestRPC-test (port random > 10000): When Jenkins tests are triggered, Jenkins will create a new TestRPC instance on a random port and run its test against it.

### Building docker container

In each modul in `/source` is a docker file. This can be used to build the docker container locally. 
You can also use the script `restart_infrastructure.sh` in the `/script` directory to build and boot them. 
This script does not boot couchbase or Test-RPC. You need to boot them manually. 

This script will futher use the `production` profile, which expects to be run on the VM at srv01.snet.tu-berlin.de .

### Project usage

Access the web pages for the components via: 

* User: http://srv01.snet.tu-berlin.de:1111/user
* Provider: http://srv01.snet.tu-berlin.de:1111/bank
* Government: http://srv01.snet.tu-berlin.de:8100/swagger-ui.html
* Discovery-Service: http://srv01.snet.tu-berlin.de:8101

As well as an overview about the exposed rest endpoints: 
If you are promt to enter a username and password triggered by a basic authentication use the username "admin" and password "password".

* User-Service: http://srv01.snet.tu-berlin.de:1112/swagger-ui.html
* Government-Service: http://srv01.snet.tu-berlin.de:8100/swagger-ui.html
* Provider-Service: http://srv01.snet.tu-berlin.de:8101/swagger-ui.html
* Discovery-Service: http://srv01.snet.tu-berlin.de:8102/swagger-ui.html

The database can be configured under: 

* Database: http://srv01.snet.tu-berlin.de:8091
  * Username: `admin`
  * Password: `yLxCr8kU\?,~2#d*`


### Docker usage on the VM

Due to memory problems, on each redeploy it need to be checked whether all components are booted correctly. 
Sometimes a couchbase timeout may orruce. Simply reboot the affected container. 

`docker ps` will list all running docker container. 

It should output something like:
```
jenkins@srv01:~$ docker ps
CONTAINER ID        IMAGE                                              COMMAND                  CREATED             STATUS              PORTS                                                                                               NAMES
e78fddd2186b        blockchain-identity/core-logic-user:latest         "/bin/sh -c 'java -j…"   About an hour ago   Up About an hour    0.0.0.0:1112->8080/tcp                                                                              CoreLogic-User
df7b6f8032f3        blockchain-identity/core-logic-provider:latest     "/bin/sh -c 'java -j…"   About an hour ago   Up About an hour    0.0.0.0:8102->8080/tcp                                                                              CoreLogic-prov
db99521175ee        blockchain-identity/core-logic-government:latest   "/bin/sh -c 'java -j…"   About an hour ago   Up About an hour    0.0.0.0:8100->8080/tcp                                                                              CoreLogic-gov
6984206b9601        blockchain-identity/frontend:latest                "/bin/sh -c 'npm sta…"   About an hour ago   Up About an hour    0.0.0.0:1111->3000/tcp                                                                              Frontend
74a34cdc68a6        blockchain-identity/discovery-service:latest       "/bin/sh -c 'java -j…"   About an hour ago   Up About an hour    0.0.0.0:8101->8080/tcp                                                                              DiscoveryService
03d1ef804bca        trufflesuite/ganache-cli:latest                    "node ./build/cli.no…"   4 hours ago         Up 4 hours          0.0.0.0:7111->8545/tcp                                                                              TestRPC-dev
2d344aa1d39d        couchbase/server                                   "/entrypoint.sh couc…"   4 hours ago         Up 4 hours          0.0.0.0:8091-8094->8091-8094/tcp, 11207/tcp, 11211/tcp, 0.0.0.0:11210->11210/tcp, 18091-18094/tcp   couchbase
````

Displaying the exposed, forwarded port. 
Attach to the logs of different container by using:

```bash
docker logs -f <CONTAINER_NAME>
``` 
