package mv.sdd.model;

import mv.sdd.utils.Constantes;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private Horloge horloge;
    private int totalClients = 0;
    private int nbServis = 0;
    private int nbFaches = 0;

    private double chiffreAffaires = 0;
    // TODO : remplacer Object par le bon type et initilaliser l'attribut avec la
    // bonne valeur
    // et ajuster les getters et les setters
    // DONE
    private final Map<MenuPlat, Integer> ventesParPlat = new HashMap<>();

    /**
     * créé les stats avec l'horloge
     * 
     * @param horloge l'horloge de la simulation
     */
    // TODO: au besoin ajuster le constructeur et/ou ajouter d'autres
    public Stats(Horloge horloge) {
        this.horloge = horloge;
        // TODO : compléter le code manquant
        // DONE
        ventesParPlat.put(MenuPlat.PIZZA, 0);
        ventesParPlat.put(MenuPlat.BURGER, 0);
        ventesParPlat.put(MenuPlat.FRITES, 0);

    }

    public Map<MenuPlat, Integer> getVentesParPlat() {
        return ventesParPlat;
    }

    /**
     * ajoute 1 au nombre total de clients
     */
    public void incrementerTotalClients() {
        totalClients++;
    }

    /**
     * ajoute 1 au nombre de clients servi
     */
    public void incrementerNbServis() {
        nbServis++;
    }

    public void incrementerNbFaches() {
        nbFaches++;
    }

    public void incrementerChiffreAffaires(double montant) {
        this.chiffreAffaires += montant;
    }

    public static String statsPlatLine(MenuPlat codePlat, int quantite) {
        return "\n" + "\t\t" + codePlat + " : " + quantite;
    }

    // TODO : ajouter incrementerVentesParPlat(MenuPlat codePlat) et autres méthodes
    // au besoin
    // DONE
    public void incrementerVentesParPlat(MenuPlat idPlat) {
        int quantInitiale = ventesParPlat.get(idPlat);
        int nouvelleQuant = quantInitiale + 1;
        ventesParPlat.put(idPlat, nouvelleQuant);
    }

    public String toString() {
        String chaine = String.format(
                Constantes.STATS_GENERAL,
                horloge.getTempsSimule(),
                totalClients,
                nbServis,
                nbFaches,
                chiffreAffaires);

        // TODO : ajouter le code pour concaténer avec statsPlatLines les lignes des
        // quantités vendus par plat (à l'aide de ventesParPlat),
        // sachant que la méthode statsPlatLine sert à formater une ligne et retourne
        // une chaine
        // DONE
        for (MenuPlat plat : MenuPlat.values()) {
            int quantite = ventesParPlat.get(plat);
            chaine = chaine + statsPlatLine(plat, quantite);
        }

        return chaine;
    }
}
