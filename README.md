 Jeu d’échecs multijoueurs — Full Stack Test

 
 Description

Prototype fonctionnel d’un jeu d’échecs multijoueurs en temps réel développé avec :

Frontend : Angular

Backend : Spring Boot

Communication : WebSockets

Base de données : PostgreSQL (ou MySQL selon config)

 Fonctionnalités
 Niveau 1 — Base (obligatoire)

Authentification : création de compte + connexion

Affichage des joueurs en ligne

Invitation d’un joueur

Acceptation/refus d’une invitation

 Niveau 2 — Fonctionnel (attendu)

Création automatique d’une partie après acceptation

Plateau d’échecs 8×8 affiché en Angular

Synchronisation des coups en temps réel (WebSockets)

Persistance : sauvegarde de chaque coup en base

Reprise de partie après reconnexion

 Niveau 3 — Bonus (optionnel)

Relecture d’une partie (rejouer coups séquentiellement)

Validation simple des coups (éviter déplacements impossibles)

Historique des coups affiché dans un panneau latéral

Stack technique

Backend : Spring Boot 3, WebSockets, Spring Security (JWT)

Frontend : Angular 17, RxJS, HTML/CSS, TypeScript

Database : PostgreSQL (ou MySQL)

Build/Run : Maven, Node.js

 Installation & Lancement
Backend (Spring Boot)
# Cloner le repo
git clone https://github.com/chalbimalek/Jeu_echec_multijoueurs.git

# Configurer la base (application.properties)
spring.datasource.url=jdbc:mysql://localhost:5432/chessdb
spring.datasource.username=root
spring.datasource.password=your_password

# Lancer le backend
mvn spring-boot:run


 Le backend démarre sur http://localhost:8080

2️ Frontend (Angular)
cd ../Jeu_echec_multijoueurs_Front

# Installer dépendances
npm install

# Lancer l’app Angular
ng serve


 Le frontend sera accessible sur http://localhost:4200
