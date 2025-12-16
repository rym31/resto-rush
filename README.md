# Épreuve finale – Simulation de restaurant (Structures de données & Threads)

**Repo:** https://github.com/rym31/resto-rush

Ce dépôt contient le **squelette de code** pour le premier volet de l’épreuve finale du cours de **Structures de données**.

## 1. Objectif
L’objectif est de développer une application **Java (console)** qui simule le service dans un petit restaurant pendant un “rush” de midi :

- des clients arrivent, passent commande, attendent leurs plats 🍕 ;
- un cuisinier prépare les commandes dans un **thread séparé** ;
- les clients sont soit servis 😋, soit repartent fâchés 😡 si leur patience tombe à 0 ;
- toutes les actions sont lues depuis un **fichier texte** ;
- toute la sortie est écrite dans un **fichier de logs**.

L’énoncé complet de l’épreuve (contexte, règles, format exact des sorties) est fourni séparément par l’enseignant·e.


## 2. Prérequis

- **Java** : version 21 et plus (recommandé 21).
- **Maven** installé (`mvn` disponible dans le PATH).
- IDE recommandé : **IntelliJ IDEA**

---

## 3. Cloner le projet

```bash
git clone https://github.com/rym31/resto-rush.git
cd resto-rush

## 4. Structure du projet

Le projet suit la structure standard Maven :
```text
.
├── pom.xml
└── src
    └── main
        └── java
            └── mv
                └── sdd
                    ├── App.java          # Point d'entrée (main)
                    ├── model/            # Entités métier (Client, Commande, MenuPlat, Stats, ...)
                    ├── sim/              # Simulation (Restaurant, Horloge, ...)
                    │   └── thread/       # Threads (Cuisinier, ...)
                    ├── io/               # Lecture d'actions, Logger
                    └── utils/            # Constantes, Formatter, outils divers
```
## 5. Scénarios d’exemple

Un fichier de scénario est un simple fichier texte où chaque ligne décrit une action.
Le dossier data contient deux fichiers exemples.

## 6. Compilation et exécution
### 6.1 Compiler le projet
À la racine du projet :
```bash
mvn clean package
```

Si tout se passe bien, Maven génère un .jar dans target/.

### 6.2 Exécuter l’application

L’application attend deux arguments :
1. le chemin du fichier de scénario (entrée),
2. le chemin du fichier de sortie (logs).

Exemple avec Maven :
```bash
mvn exec:java -Dexec.mainClass="mv.sdd.App" \
              -Dexec.args="data/scenario_1.txt data/sortie_1.txt"
```

> ⚠️ Adaptez mv.sdd.App si votre classe App est dans un autre package.

Après exécution, vous devriez obtenir un fichier data/sortie_1.txt contenant tous les logs de la simulation.


## 7. Travail à réaliser

À partir de ce squelette, vous devez :
* compléter les méthodes marquées par // TODO ;
* choisir et utiliser des structures de données appropriées (Map, Queue, List, etc.) ;
* implémenter la logique de :
  * gestion des clients et de leur patience,
  * gestion des commandes et de leurs états,
  * calcul et affichage des statistiques ;
* implémenter et utiliser correctement le thread Cuisinier ;
* gérer le temps simulé via une méthode tick() dans Restaurant (appelée depuis l’action AVANCER_TEMPS) ;
* produire un log conforme au format demandé (résumés, lignes clients, stats, événements).

## 8. Règles et contraintes

* Ne pas supprimer ni renommer les classes ou méthodes déjà utilisées par le squelette sans raison valable.
* Vous pouvez ajouter :
  * des méthodes privées ou utilitaires,
  * des classes supplémentaires si elles respectent l’architecture proposée.
* Respecter les conventions Java (noms de classes, de méthodes, indentation).
* Tout ce qui est affiché doit passer par le Logger (pas de System.out.println dispersés dans le code).

## 9. Versionnement (Git / GitHub)

* Votre code doit être versionné dans ce dépôt.
* Ajoutez un fichier README.md (vous pouvez vous basez sur celui-ci) et complétez-le au besoin (notes personnelles, exemples de scénarios, etc.).
* Si le dépôt est privé, pensez à inviter votre enseignant·e avec l’adresse indiquée dans l’énoncé.

## 10. Aide

* Référez-vous à l’énoncé complet (PDF ou document remis sur Léa).
* Un document d’aide complémentaire sur les threads (synchronized, wait, notifyAll) peut aussi être fourni.

Bon code, et bon service de midi au resto 🍕🍔🍟 !
