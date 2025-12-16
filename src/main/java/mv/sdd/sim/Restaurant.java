package mv.sdd.sim;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import mv.sdd.io.Action;
import mv.sdd.utils.Logger;
import mv.sdd.io.ActionType;
import mv.sdd.model.*;
import mv.sdd.sim.thread.Cuisinier;
import mv.sdd.utils.Constantes;
import mv.sdd.utils.Formatter;

public class Restaurant {
    private final Logger logger;
    // TODO : Ajouter les attributs nécessaires ainsi que les getters et les setters
    private final Horloge horloge;
    private final Stats stats;
    private final Map<Integer, Client> clients;
    private final Queue<Commande> fileCuisine;
    private final List<Commande> enPreparation;
    private Cuisinier cuisinier;
    private int dureeMax;
    private boolean serviceActif = false;

    // TODO : Ajouter le(s) constructeur(s)
    // public Restaurant(Logger logger) {
    // this.logger = logger;
    // }
    public Restaurant(Logger logger, Horloge horloge, Stats stats, Map<Integer, Client> clients,
            Queue<Commande> fileCuisine, List<Commande> enPreparation, Cuisinier cuisinier, int dureeMax,
            boolean serviceActif) {
        this.logger = logger;
        this.horloge = horloge;
        this.stats = stats;
        this.clients = clients;
        this.fileCuisine = fileCuisine;
        this.enPreparation = enPreparation;
        this.cuisinier = cuisinier;
        this.dureeMax = dureeMax;
        this.serviceActif = serviceActif;
    }

    public Logger getLogger() {
        return logger;
    }

    public Horloge getHorloge() {
        return horloge;
    }

    public Stats getStats() {
        return stats;
    }

    public Map<Integer, Client> getClients() {
        return clients;
    }

    public Queue<Commande> getFileCuisine() {
        return fileCuisine;
    }

    public List<Commande> getEnPreparation() {
        return enPreparation;
    }

    public Cuisinier getCuisinier() {
        return cuisinier;
    }

    public void setCuisinier(Cuisinier cuisinier) {
        this.cuisinier = cuisinier;
    }

    public int getDureeMaxService() {
        return dureeMax;
    }

    public void setDureeMaxService(int dureeMax) {
        this.dureeMax = dureeMax;
    }

    public boolean isServiceActif() {
        return serviceActif;
    }

    public void setServiceActif(boolean serviceActif) {
        this.serviceActif = serviceActif;
    }

    /**
     * exécute une action que on reçoit du fichier
     * 
     * @param action action a exécuter
     */
    // TODO : implémenter les méthodes suivantes
    // Méthode appelée depuis App pour chaque action

    public void executerAction(Action action) {
        ActionType type = action.getType();

        if (type == ActionType.DEMARRER_SERVICE) {
            int dureeMax = action.getParam1();
            int nbCuisiniers = action.getParam2();
            demarrerService(dureeMax, nbCuisiniers);
        } else if (type == ActionType.AJOUTER_CLIENT) {
            int idClient = action.getParam1();
            String nom = action.getParam3();
            int patience = action.getParam2();
            ajouterClient(idClient, nom, patience);
        } else if (type == ActionType.PASSER_COMMANDE) {
            int idClient = action.getParam1();
            String codePlat = action.getParam3();
            MenuPlat plat = MenuPlat.valueOf(codePlat);
            passerCommande(idClient, plat);
        } else if (type == ActionType.AVANCER_TEMPS) {
            int minutes = action.getParam1();
            avancerTemps(minutes);
        } else if (type == ActionType.AFFICHER_ETAT) {
            afficherEtat();
        } else if (type == ActionType.AFFICHER_STATS) {
            afficherStats();
        } else if (type == ActionType.QUITTER) {
            arreterService();
        }
    }

    /**
     * démarre le service du resto et crée le cuisinier
     * 
     * @param dureeMax     minutes max de service
     * @param nbCuisiniers nombre de cuisiniers
     */
    public void demarrerService(int dureeMax, int nbCuisiniers) {
        this.dureeMax = dureeMax;
        this.serviceActif = true;
        String message = String.format(
                Constantes.DEMARRER_SERVICE,
                dureeMax, nbCuisiniers);
        logger.logLine(message);
        this.cuisinier = new Cuisinier(this, logger);
        cuisinier.start();
    }

    public void avancerTemps(int minutes) {
        String message = Constantes.AVANCER_TEMPS + " " + minutes;
        logger.logLine(message);
        for (int i = 0; i < minutes; i++) {
            horloge.avancerTempsSimule(1);
            tick();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * arrête le service en arrêtant le cuisinier
     */
    public void arreterService() {
        this.serviceActif = false;
        if (cuisinier != null) {
            cuisinier.arreter();
            try {
                if (cuisinier != null) {
                    cuisinier.join();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // TODO : Déclarer et implémenter les méthodes suivantes
    /**
     * la méthode tick qui met à jour tout chaque minute
     * décrémente temps, check les clients fâchés, serve les clients
     */
    public void tick() {
        if (!serviceActif)
            return;

        for (Commande commande : enPreparation) {
            commande.decrementerTempsRestant(1);
        }

        for (int i = 0; i < enPreparation.size(); i++) {
            Commande commande = enPreparation.get(i);
            boolean estTerminee = commande.estTermineeParTemps();
            if (estTerminee) {
                commande.setEtat(EtatCommande.PRETE);
                String message = String.format(
                        Constantes.EVENT_CMD_TERMINEE,
                        horloge.getTempsSimule(),
                        commande.getId(),
                        commande.getClient().getNom());
                logger.logLine(message);
            }
        }

        for (Client client : clients.values()) {
            if (client.getEtat() == EtatClient.EN_ATTENTE) {
                client.diminuerPatience(1);
            }
        }

        for (Client client : clients.values()) {
            if (client.getEtat() == EtatClient.PARTI_FACHE) {
                stats.incrementerNbFaches();
                String message = String.format(
                        Constantes.EVENT_CLIENT_FACHE,
                        horloge.getTempsSimule(),
                        client.getNom());
                logger.logLine(message);
            }
        }


        for (Client client : clients.values()) {
            if (client.getEtat() == EtatClient.EN_ATTENTE &&
                    client.getCommande() != null &&
                    client.getCommande().getEtat() == EtatCommande.PRETE) {

                client.setEtat(EtatClient.SERVI);
                client.getCommande().setEtat(EtatCommande.LIVREE);
                stats.incrementerNbServis();
                stats.incrementerChiffreAffaires(client.getCommande().calculerMontant());

                for (Plat plat : client.getCommande().getPlats()) {
                    stats.incrementerVentesParPlat(plat.getCode());
                }
            }
        }

    }

    /**
     * affiche l'état actuel du resto
     */
    public void afficherEtat() {
        int nbrServis = 0;
        int nbrFaches = 0;
        for (Client client : clients.values()) {
            if (client.getEtat() == EtatClient.SERVI) {
                nbrServis++;
            } else if (client.getEtat() == EtatClient.PARTI_FACHE) {
                nbrFaches++;
            }
        }

        int nbrEnFile = fileCuisine.size();
        int nbrEnPrep = enPreparation.size();
        String message = Formatter.resumeEtat(horloge.getTempsSimule(), clients.size(), nbrServis, nbrFaches, nbrEnFile,
                nbrEnPrep);
        logger.logLine(message);
    }

    /**
     * affiche les stats globales
     */
    public void afficherStats() {
        logger.logLine(Constantes.HEADER_AFFICHER_STATS);
        logger.logLine(stats.toString());
    }

    /**
     * ajoute un nouveau client au resto
     * 
     * @param id               l'id du client
     * @param nom              le nom
     * @param patienceInitiale sa patience au depart
     * @return le client créé
     */
    public Client ajouterClient(int id, String nom, int patienceInitiale) {
        Client client = new Client(id, nom, patienceInitiale);
        clients.put(id, client);
        stats.incrementerTotalClients();
        String message = String.format(
                Constantes.EVENT_ARRIVEE_CLIENT,
                horloge.getTempsSimule(),
                id, nom, patienceInitiale);
        logger.logLine(message);

        return client;

    }

    /**
     * créé une commande pour un client
     * 
     * @param idClient l'id du client
     * @param codePlat le type de plat
     * @return la commande ou null si client existe pas
     */
    public Commande passerCommande(int idClient, MenuPlat codePlat) {
        Client client = clients.get(idClient);

        if (client == null)
            return null;
        Commande commande = new Commande(client, codePlat);

        client.setCommande(commande);
        fileCuisine.add(commande);

        String message = String.format(
                Constantes.EVENT_CMD_CREE,
                horloge.getTempsSimule(),
                commande.getId(),
                Constantes.MENU.get(codePlat).getNom(),
                client.getNom());
        logger.logLine(message);

        return commande;

    }

    /**
     * récupère la prochaine commande de la file d'attente
     * 
     * @return la commande null si vide
     */
    public Commande retirerProchaineCommande() {
        return fileCuisine.poll();
    }

    /**
     * marque une commande comme terminée
     * 
     * @param commande la commande a marquer
     */
    public void marquerCommandeTerminee(Commande commande) {
        commande.demarrerPreparation();
        enPreparation.add(commande);
        String message = String.format(
                Constantes.EVENT_CMD_DEBUT,
                horloge.getTempsSimule(),
                commande.getId(),
                commande.getTempsRestant());
        logger.logLine(message);
    }

    public Client creerClient(int id, String nom, int patienceInitiale) {
        Client client = new Client(id, nom, patienceInitiale);

        return client;

    }

    public Commande creerCommandePourClient(Client client, MenuPlat plat) {
        Commande commande = new Commande(client, plat);
        return commande;
    }

    // TODO : implémenter d'autres sous-méthodes qui seront appelées par les
    // méthodes principales
    // pour améliorer la lisibilité des méthodes en les découpant au besoin (éviter
    // les trés longues méthodes)
    // exemple : on peut avoir une méthode diminuerPatienceClients()
    // qui permet de diminuer la patience des clients (appelée par tick())
}
