# Protocol 29.10.17

## Internal group meeting 

### Done:
* Oskar:
  * read MedRec
  * found another MedRec source
  * BlockStack
  
### Summaries

* allgemein
* M: distributed hashtables -> in unserem Use-Case unpassend
  * interessant wegen dezentralisierung
* O: Hawk - hoher Crypto standard (universal composability)
* M: identity management system
    * CMS in ethereum
    * identity authentication mit public key und ethereum account adress
* T: guter Überblick mit allgemeinem Use Case
* T: technisch, Use Cases nicht empfehlenswert
* T / M: linux hyperledger foundation doku schlecht, transaktionen dürfen nur bei berechtigung durchgeführt werden, nicht direkt anwendbar
* M: Self-KEY muss App verwenden, private key generiert und daten in json blocks kann man verschicken
* M: Estland, GuardTime macht alles privat, Central Trusted Authority
* MedRec:
    * Components wurden beschrieben
    * Transaction System
    * 3 Use Cases
    * 1 fehlt (jemand anderes darf es verändern)
    * loss of private key leads to loss of private information
    * revoke / change keys ?
### Agenda
* summarize papers
* collect new insights
* eher grob oder im Detail klären
* what are possible architectures / approaches?
* rough concept definition
* rough implementation sketch
* status: presentation?
* prepare external meeting

### Decisions
* usage of Ethereum
* use case: bankkonto eröffnen... Umzug, Heirat, Einfrierung
* use case: medical records management, record sharing, adding
* rough concept: make drawings of actual use case

### Preparation for external meeting
* beide use cases vorstellen
* Konzept kurz vorstellen
* MedRec mehr oder weniger kopieren
* Grafiken vorbereiten mit "Workflow"

#### Next internal meeting
* T: paper lesen
* M: konzept ausarbeiten
* M: Evaluation
* M + O: Präsentation

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
