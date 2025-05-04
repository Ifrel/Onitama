package Global;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * <h1>📄 LogManagerSetup</h1>
 * <p><strong>Configuration centralisée du système de log pour une application Java Swing.</strong></p>
 *
 * <h2>⚙️ Fonctionnalités</h2>
 * <ul>
 *   <li>Création automatique du dossier <code>.log/</code> dans le répertoire courant.</li>
 *   <li>Génération d’un fichier de log par jour : <code>.log/yyyy-MM-dd.log</code>.</li>
 *   <li>Écriture des messages de log dans ce fichier selon le niveau défini.</li>
 *   <li>Affichage dans la console avec un niveau distinct (paramétrable).</li>
 *   <li>Formatage simple et lisible via <code>SimpleFormatter</code>.</li>
 * </ul>
 *
 * <h2>🛠️ Utilisation</h2>
 * <p>
 * Appeler une seule fois au lancement de l'application (dans la méthode <code>main()</code>) :
 * </p>
 * <pre>
 *   LogManagerSetup.setupLogger(Level.INFO, Level.ALL);
 * </pre>
 *
 * <p>
 * Ensuite, dans toutes les autres classes, il suffit d'utiliser :
 * </p>
 * <pre>
 *   private static final Logger logger = Logger.getLogger(NomDeClasse.class.getName());
 * </pre>
 *
 * <p>Tous les loggers configurés de cette manière utiliseront la même configuration (fichier + console).</p>
 *
 * <h2>🧠 Note importante — hiérarchie des loggers</h2>
 * <p>
 * Le système <code>java.util.logging</code> fonctionne avec une hiérarchie de noms :
 * </p>
 * <pre>
 *   Logger.getLogger("com.example.MaClasse")
 *     → parent = Logger.getLogger("com.example")
 *     → parent = Logger.getLogger("com")
 *     → parent = Logger.getLogger("")
 * </pre>
 *
 * <p>
 * Tous les loggers transmettent leurs messages à leur parent jusqu'au <strong>logger racine</strong> (<code>""</code>).
 * Si on configure uniquement ce logger racine, tous les autres loggers en hériteront automatiquement :
 * </p>
 *
 * <ul>
 *   <li>Pas besoin de configurer chaque logger manuellement.</li>
 *   <li>Centralisation de la gestion des logs (console et fichier).</li>
 * </ul>
 *
 * <hr>
 * <p style="color:gray;"><em>Conçu pour des projets Swing mais réutilisable dans tout projet Java.</em></p>
 */

public class LogManagerSetup {

    /**
     * Configure le système de log :
     * @param consoleLevel Niveau de log à afficher dans la console (ex: Level.INFO)
     * @param fileLevel Niveau de log à enregistrer dans le fichier (ex: Level.ALL)
     */
    public static void setupLogger(Level consoleLevel, Level fileLevel) {
        try {
            // 1. Créer le dossier .log s’il n’existe pas
            File logDir = new File(".log");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // 2. Générer un nom de fichier avec la date du jour (ex: .log/2025-05-04.log)
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String logFilePath = ".log/" + dateStr + ".log";

            // 3. Créer un FileHandler pour le fichier log
            FileHandler fileHandler = new FileHandler(logFilePath, true); // append = true
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(fileLevel);

            // 4. Créer un ConsoleHandler avec son propre niveau
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(consoleLevel);

            // 5. Obtenir le logger global (root)
            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(Level.ALL); // On capte tout
            // Supprimer les handlers existants pour éviter les doublons
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler h : handlers) {
                rootLogger.removeHandler(h);
            }

            // 6. Ajouter les nouveaux handlers
            rootLogger.addHandler(fileHandler);
            rootLogger.addHandler(consoleHandler);

        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Erreur de configuration du log", e);
        }
    }
}
