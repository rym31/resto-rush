package mv.sdd.sim.thread;

import mv.sdd.model.Commande;
import mv.sdd.sim.Restaurant;
import mv.sdd.utils.Logger;

public class Cuisinier extends Thread {
    private Restaurant restaurant;
    private Logger logger;
    private volatile boolean actif = true;

    /**
     * créé un cuisinier pour un resto
     * 
     * @param restaurant le resto du cuisinier
     * @param logger  enregistre les messages
     */
    public Cuisinier(Restaurant restaurant, Logger logger) {
        this.restaurant = restaurant;
        this.logger = logger;
    }

    /**
     * lance le cuisinier 
     */
    @Override
    public void run() {
        while (actif) {
            Commande commande = restaurant.retirerProchaineCommande();
            if (commande == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                restaurant.marquerCommandeTerminee(commande);
            }
        }
    }

    /**
     * arrête le cuisinier 
     */
    public void arreter() {
        actif = false;
        interrupt();
    }
}
