package mv.sdd.model;

import java.util.ArrayList;
import java.util.List;

import mv.sdd.utils.Constantes;

public class Commande {
    private int id;
    private static int nbCmd = 0;
    private final Client client;
    private EtatCommande etat = EtatCommande.EN_ATTENTE;
    private int tempsRestant; // en minutes simulées
    // TODO : ajouter l'attribut plats et son getter avec le bon type et le choix de
    // la SdD adéquat
    // DONE
    private final List<Plat> plats = new ArrayList<>();

    /**
     * constructeur pour créer une commande pour un client avec un plat
     * 
     * @param client le client qui commande
     * @param plat le type de plat
     */
    // TODO : Ajout du ou des constructeur(s) nécessaires ou compléter au besoin
    public Commande(Client client, MenuPlat plat) {
        id = ++nbCmd;
        this.client = client;
        this.tempsRestant = 0;
        ajouterPlat(plat);

    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public int getTempsRestant() {
        return tempsRestant;
    }

    public List<Plat> getPlats() {
        return plats;
    }

    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }

    /**
     * ajoute un plat a la commande 
     * 
     * @param idPlat le type de plat a ajouter
     */
    // TODO : Ajoutez la méthode ajouterPlat
    // DONE
    public void ajouterPlat(MenuPlat idPlat) {
        Plat plat = Constantes.MENU.get(idPlat);
        if (plat != null) {
            plats.add(plat);
        }
    }

    /**
     * démarre la préparation de la commande
     * calcule le temps et change l'état
     */
    // TODO : Ajoutez la méthode demarrerPreparation
    // DONE
    public void demarrerPreparation() {
        tempsRestant = calculerTempsPreparationTotal();
        etat = EtatCommande.EN_PREPARATION;
    }

    /**
     * enlève les minutes de temps de préparation restant
     * 
     * @param minutes - combien de minutes enlever
     */
    // TODO : Ajoutez la méthode decrementerTempsRestant
    // DONE
    public void decrementerTempsRestant(int minutes) {
        tempsRestant = tempsRestant - minutes;
        if (tempsRestant < 0) {
            tempsRestant = 0;
        }
    }

    /**
     * check si la commande est fini par le temps
     * 
     * @return vrai si c'est terminé, sinon faux
     */
    // TODO : Ajoutez la méthode estTermineeParTemps
    // DONE
    public boolean estTermineeParTemps() {
        return tempsRestant <= 0 && etat == EtatCommande.EN_PREPARATION;
    }

    /**
     * calcule le temps maximum pour préparer tous les plats
     * 
     * @return le temps 
     */
    // TODO : Ajoutez la méthode calculerTempsPreparationTotal
    // DONE
    public int calculerTempsPreparationTotal() {
        int tempsMax = 0;
        for (Plat element : plats) {
            int tempsPlat = element.getTempsPreparation();
            if (tempsPlat > tempsMax) {
                tempsMax = tempsPlat;
            }
        }
        return tempsMax;
    }

    /**
     * calcule le prix total de tous les plats
     * 
     * @return le montant total
     */
    // TODO : Ajoutez la méthode calculerMontant
    // DONE
    public double calculerMontant() {
        double total = 0;
        for (Plat plat : plats) {
            double prix = plat.getPrix();
            total = total + prix;
        }
        return total;
    }
}
