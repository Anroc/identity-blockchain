Date: ```23.11.2017```

# Attendance
  * Timo
  * Marvin
  * Oskar
  * Michael

# Portainer
  * Docker container to manage all other running containers through web
    * Easy management of containers (restarting, reseting to previous state etc.)

# Gradle
  * Small introduction to gradle project structure and functionality

# Tests
## Unit Tests
  * Stick to a single package when testing functionality
    * e.g.: If you want to test your database repackaging function, test against a mock endpoint so as not to test functionality of depending packages
  * All tests should assert something or verify
  * Basic functionality has to be tested

# Database
  * Couchbase as database
    * Couchbase lite has no springboot compatibility
      * Couchbase developers are working on converging Couchbase server and Couchbase lite
  * Abstract model that is valid for each provider
    * contains meta information and a claim/closure list
      * provider write their information into this list

# ToDo
    * missing so far:
      * FrontEnd Docker container
      * Database server as docker container
        * No logic just database itself
