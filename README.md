## Présentation

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
Dossier "client" contient le client<br />
Dossier "serveur" contient le serveur<br />
Lancement :<br />
ClientUI.java<br />
ServeurUI.java<br />


## Informations complémentaires

Commande de base si les ports sont déjà occupés :<br />
netstat -ano | find ":2222" & netstat -ano | find ":3333" <br />
TASKKILL /PID 10856 -f <br />

## Capture Serveur + Client
![alt tag](https://github.com/Erozbliz/Serveur_Client_Hoc_V3/blob/master/src/img/c.jpg?raw=true)



