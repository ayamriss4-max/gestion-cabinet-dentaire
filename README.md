# Cabinet dentaire - Projet Java 2026-2027 (EMI)

Application JavaFX de gestion des rendez-vous d'un cabinet dentaire.

## Equipe
- Meryem : rendez-vous, gestionnaire de donnees, planning colore, export PDF
- Salma : utilisateurs, authentification, espace patient, rapport
- Aya : dentistes, actes, creneaux, reservation administrateur, fiche et impression

## Prerequisites
- JDK 17
- Eclipse avec Maven (m2e), ou Maven en ligne de commande

## Lancer
- Eclipse : clic droit sur cabinet.Launcher > Run As > Java Application
- Terminal : mvn javafx:run

## Comptes de demonstration
- Administrateur : admin / admin123
- Patient : patient1 / patient123

## Donnees
Les fichiers CSV sont dans le dossier donnees/, cree au premier lancement.

## Tests
mvn test

## Organisation Git
- main : version stable
- une branche par membre (meryem, salma, aya) ; fusion dans main apres test
- chaque tache de code est relue par un autre membre avant la fusion