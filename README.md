## Presentation

Votre objectif est de construire le système HockeyLive permettant de suivre "en direct", sur un téléphone cellulaire 
Android, le déroulement d’un match de hockey et de parier en-ligne. Vous anticipez avoir 200 000 clients connectés
chaque soir. Pour ce faire, vous aurez trois parties à développer : un serveur Java et un client Java (TP1), un client
Android (TP2) et un client Ajax (TP3). Les services disponibles seront donc :

* Un service de suivi de matchs. Sur le téléphone, l’usager aura accès à la liste des matchs disponibles dans
l’écran d’entrée de l’application. Il choisira un match et pourra suivre son déroulement. Le rafraîchissement
de la page de suivi se fera toutes les deux minutes ou sur demande via un bouton « update ». La page de
suivi affichera ces informations:

> noms des deux équipes

> le chronomètre

> le pointage et les compteurs

> les pénalités en cours

* Un service de paris sur le match. Lorsque l’usager pariera sur le match en cours, il devra absolument
recevoir une confirmation que son pari a été enregistré par le système. Les paris pourront être enregistrés
jusqu'à la fin de la 2e période. Il ne sera pas possible de parier en 3e période. Le service des paris tient le
décompte des sommes pariées sur un match. Le montant gagné est égal à 75% du montant total parié réparti
au pro rata de leur mise entre les gagnants.


## Prérequis


## Installation


## Informations complémentaires

Commande de base si les ports sont déjà occupés :<br />
netstat -ano | find ":2222" & netstat -ano | find ":3333" <br />
TASKKILL /PID 10856 -f <br />


## Grille de correction

| SERVEUR                                                                        | CHECK                               |
| -------------------------------------------------------------------------------|                       -------------:|
| **`Implémentation`**				                                             |                                     |
| Protocole de communication : UDP vs TCP                                        | 		                               |
| Définition du format des messages                                              | 		                               |
| **`Suivi de matchs`**				                                             |                                     |
| Thread-per-request ou Thread-per-connection                                    | 		                               |
| Création d'un pool de thread                                                   | 		                               |
| Désérialisation - réception des requêtes                                       | 		                               |
| Lancement / récupérer un ou le bon thread pour traiter la session              |                                     |
| Sérialisation - envoi des réponses                                             | 		                               |
| Mise-à-jour aux deux minutes                                                   | 		                               |
| Mise-à-jour sur demande (update)                                               | 		                               |
| Informations disponibles : équipes, chronomètre, pointage, compteurs, pénalités| 		                               |
| **`Paris`**		                                                             |                                     |
| Implémentation de l'approche Thread-per-object                                 | 		                               |
| Un tampon des requêtes par object distant - ListeDesMatchs                     | 		                               |
| Un tampon des requêtes par objet distant - Match                               | 		                               |
| Un tampon des requêtes par objet distant - Paris                               | 		                               |
| Les paris sont acceptés en 1ère et 2e période                                  | 		                               |
| Les paris ne sont pas acceptés en 3e période                                   | 		                               |
| Informer les parieurs de leur gain à la fin du match                           | 		                               |
| **`Objets distans`**				                                             |                                     |
| ListeDesMatchs - accès thread-safe                                             |                                     |
| Fil d'éxecution qui s'occupe de la mise-à-jour du temps                        |                                     |
| Fil d'éxecution qui s'occupe de la mise-à-jour du événements liés au match     |                                     |
| Match - accès thread-safe - temps                                              |                                     |
| Match - accès thread-safe - buts et pénalités                                  |                                     |
| Match : tenir le décompte des sommes pariées                                   |                                     |
| Paris, accès thread-safe                                                       |                                     |
| Paris, enregistrement des paris sur les matchs                                 |                                     |
| **`Autres éléments remarquables`**				                             |                                     |

## Docs

