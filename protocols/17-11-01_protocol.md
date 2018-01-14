# Protocol 29.10.17

## Internal group meeting 

### Done:
* Registrar Contract:
  1. Client erstellt Ethereum Adress und ppk pair (digital identity)
  2. Create Registrar Contract mit public key signiert
  3. User logged sich beim Provider / Gov ein und lässt dadurch seine digital identity verifizieren
  4. Dadurch wird Registrar Contract abgeschlossen / approved
  5. Gov stellt User daten bereit, um diese in seiner localen db abzuspeichern
  
* Request Permission
  1. Third Party requests permission from Provider
  2. Provider creates new message that third party asked for x
  3. Provider writes that message in the blockchain
  4. Client gets blockchain message
  5. Client then approves or declines (self-sovereignty)
  6. Third Party gets hash and approve (signed by user)
  7. geht mit signierter query zum provider
  8. provider checkt query mit blockchain
  9. executed query
  10. sendet encryptete response zur third party
  
* offene Punkte
  1. user soll Rechte zeitlich limitieren können oder hash ändert sich
  2. 
  
### Evaluation
* discuss next meeting
    * create interesting test cases
        * rejected party 
        * suck out of fingers
        
### Presentation
* Inspirationen vorgeschlagen
* Treffen Samstag

### Agenda
* Timo: paper lesen
* Marvin: Konzept ausarbeiten
* Marvin: Evaluation
* Micha + Oskar: Präsentation

### Decisions
* usage of Ethereum
* use case: bankkonto eröffnen... Umzug, Heirat, Einfrierung
* use case: medical records management, record sharing, adding
* rough concept: make drawings of actual use case

### Preparation for external meeting
* beide use cases vorstellen
* Konzept kurz vorstellen
* MedRec mehr oder weniger zusammenfassen
* Grafiken vorbereiten mit "Workflow"

#### Next internal meeting
* smart contracts verinnerlichen

#### Presentation
* Status to...
    * [ ] updated Gantt chart @Timo
    * [ ] Problem description
    * [ ] Definition Digital identity & Definition block chain & Advent ages block chain @Timo
    * [ ] Folien Design @Oskar @Micha
    * [ ] Concept (MedRec (Block-chain as transaction database), Plain (no privacy))
    * [ ] Evaluation @Marvin
    * [ ] Organization @Micha
    * [ ] Use Cases (--> Attributes, --> Actors)
    * [ ] Sketch of Implementation system (Interfaces)
