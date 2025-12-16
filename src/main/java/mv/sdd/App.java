package mv.sdd;

import mv.sdd.io.Action;
import mv.sdd.io.ActionFileReader;
import mv.sdd.model.Client;
import mv.sdd.model.Commande;
import mv.sdd.model.Horloge;
import mv.sdd.model.Stats;
import mv.sdd.sim.Restaurant;
import mv.sdd.utils.Constantes;
import mv.sdd.utils.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 420-311 : STRUCTURE DE DONNÉES
 * ÉPREUVE FINALE : VOLET 1
 *
 * @author Souane Aicha-Rym
 */
public class App {
    public static void main( String[] args ) {
        if (args.length < 2) {
            System.err.println("Usage : java App <fichier_actions> <fichier_resultat>");
            return;
        }

        String actionsFile = args[0];
        String outputFile = args[1];

        try (PrintWriter out = new PrintWriter(new FileWriter(outputFile, false))) {

            Logger logger = new Logger(out , true /* pour voir aussi en console ce qui serait écrit dans le fichier */);
            logger.logLine(Constantes.HEADER_APP);
            logger.logLine(Constantes.HEADER_ACTIONS + actionsFile);
            logger.logEmpty();

            List<Action> actions = ActionFileReader.readActions(actionsFile);

            Horloge horloge = new Horloge();
            Stats stats = new Stats(horloge);
            Map<Integer, Client> clients = new HashMap<>();
            Queue<Commande> fileCuisine = new LinkedList<>();
            List<Commande> enPreparation = new LinkedList<>();
            
            Restaurant restaurant = new Restaurant(logger, horloge, stats, clients, fileCuisine, enPreparation, null, 0, false);

            for (Action action : actions) {
                restaurant.executerAction(action);
            }

            // TODO : Ajuster au besoin
            restaurant.arreterService();
            logger.logLine(Constantes.FOOTER_APP);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
