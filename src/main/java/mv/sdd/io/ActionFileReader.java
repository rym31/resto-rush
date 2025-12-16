package mv.sdd.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionFileReader {
    
    /**
     * lit le fichier texte et retourne une liste d'action
     * 
     * @param filePath le chemin du fichier
     * @return une liste avec toutes les actions lu du fichier
     */
    public static List<Action> readActions(String filePath) throws IOException {
        List<Action> actions = new ArrayList<>();

        // TODO : Ajouter le code qui permet de lire et parser un fichier d'actions
        // DONE
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String parseLigne = ligne.trim();
                
                if (!parseLigne.isEmpty()) {
                    Action action = ActionParser.parseLigne(parseLigne);
                    actions.add(action);
                }
            }
        }   catch (IOException e) {
            e.printStackTrace();
        }

        return actions;
    }
}
