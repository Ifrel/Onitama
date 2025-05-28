package Vue.Configuration;

import Vue.Adaptateurs.AdaptateurCouleur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import static Global.Config.*;

/**
 * Classe gérant la configuration de l'interface utilisateur du jeu
 * Implémente le pattern Singleton pour assurer une instance unique
 */
public class InfosDeConfigUI implements ConfigurationUI {
    public static final Path CHEMIN_FICHIER_CONFIG = Path.of(
            Optional.ofNullable(InfosDeConfigUI.class.getResource("/vue/config_ui.json"))
                    .map(URL::getPath)
                    .orElse(System.getProperty("user.dir") + "/res/vue/config_ui.json")
    );

    private static final Logger LOGGER = Logger.getLogger(InfosDeConfigUI.class.getName());

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Color.class, new AdaptateurCouleur())
            .create();
    private final Map<Integer, ConfigurationCouleurJoueur> configurationParJoueur;

    private static final InfosDeConfigUI INSTANCE = new InfosDeConfigUI();


    /**
     * Constructeur privé pour le pattern Singleton
     * Initialise la configuration soit depuis le fichier, soit avec les valeurs par défaut
     */
    private InfosDeConfigUI() {
        this.configurationParJoueur = new HashMap<>();
        if (!chargerConfiguration()) {
            initialiserConfigurationParDefaut();
        }
        enregistrerSauvegardeFermeture();
    }

    public static InfosDeConfigUI getInstance() {
        return INSTANCE;
    }

    /**
     * Enregistre un hook pour sauvegarder la configuration à la fermeture de l'application
     */
    private void enregistrerSauvegardeFermeture() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::sauvegarderConfiguration));
    }


    /**
     * Initialise la configuration par défaut avec des couleurs aléatoires pour chaque joueur
     */
    private void initialiserConfigurationParDefaut() {
        configurationParJoueur.put(
                ID_JOUEUR_1,
                new ConfigurationCouleurJoueur(
                        TypeCouleur.BLEU,
                        COULEUR_CASE_MAITRE_JOUEUR_1,
                        COULEUR_CASE_ELEVE_JOUEUR_1
                )
        );
        configurationParJoueur.put(
                ID_JOUEUR_2,
                new ConfigurationCouleurJoueur(
                        TypeCouleur.ROUGE,
                        COULEUR_CASE_MAITRE_JOUEUR_2,
                        COULEUR_CASE_ELEVE_JOUEUR_2
                )
        );
    }

    /**
     * Charge la configuration depuis le fichier JSON
     *
     * @return true si le chargement a réussi, false sinon
     */
    private synchronized boolean chargerConfiguration() {
        try {
            if (!Files.exists(CHEMIN_FICHIER_CONFIG)) {
                return false;
            }
            String jsonConfig = Files.readString(CHEMIN_FICHIER_CONFIG);
            ConfigurationSauvegardee config = GSON.fromJson(jsonConfig, ConfigurationSauvegardee.class);
            if (config == null || config.getConfigurationParJoueur() == null) {
                LOGGER.warning("Configuration invalide dans le fichier");
                return false;
            }
            configurationParJoueur.clear();
            configurationParJoueur.putAll(config.getConfigurationParJoueur());
            return true;
        } catch (JsonSyntaxException e) {
            LOGGER.warning("Format JSON invalide : " + e.getMessage());
            return false;
        } catch (IOException e) {
            LOGGER.warning("Erreur de lecture du fichier : " + e.getMessage());
            return false;
        }
    }

    /**
     * Sauvegarde la configuration actuelle dans le fichier JSON
     */
    private synchronized void sauvegarderConfiguration() {
        try {
            Files.createDirectories(CHEMIN_FICHIER_CONFIG);
            ConfigurationSauvegardee config = new ConfigurationSauvegardee(configurationParJoueur);
            String jsonConfig = GSON.toJson(config);
            Files.writeString(CHEMIN_FICHIER_CONFIG, jsonConfig);
        } catch (IOException e) {
            LOGGER.severe("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    @Override
    public String getNomCouleurPionJoueur(int idJoueur) {
        ConfigurationCouleurJoueur config = configurationParJoueur.get(idJoueur);
        if (config == null) {
            throw new IllegalArgumentException("ID joueur invalide : " + idJoueur);
        }
        return config.nomCouleurPion;
    }

    @Override
    public Color getCouleurPionJoueur(int idJoueur) {
        ConfigurationCouleurJoueur config = configurationParJoueur.get(idJoueur);
        if (config == null) {
            throw new IllegalArgumentException("ID joueur invalide : " + idJoueur);
        }
        return config.couleurPion;
    }

    @Override
    public Color getCouleurCaseMaitreJoueur(int idJoueur) {
        ConfigurationCouleurJoueur config = configurationParJoueur.get(idJoueur);
        if (config == null) {
            throw new IllegalArgumentException("ID joueur invalide : " + idJoueur);
        }
        return config.couleurCaseMaitre;
    }


    // Méthodes d'accès aux données de configuration

    @Override
    public void setCouleurCaseMaitreJoueur(int idJoueur, Color couleur) {
        ConfigurationCouleurJoueur config = configurationParJoueur.get(idJoueur);
        if (config == null) {
            throw new IllegalArgumentException("ID joueur invalide : " + idJoueur);
        }
        if (couleur == null) {
            throw new IllegalArgumentException("La couleur ne peut pas être null");
        }
        config.couleurCaseMaitre = couleur;
    }

    @Override
    public Color getCouleurCaseEleveJoueur(int idJoueur) {
        ConfigurationCouleurJoueur config = configurationParJoueur.get(idJoueur);
        if (config == null) {
            throw new IllegalArgumentException("ID joueur invalide : " + idJoueur);
        }
        return config.couleurCaseEleve;
    }

    @Override
    public void setCouleurCaseEleveJoueur(int idJoueur, Color couleur) {
        ConfigurationCouleurJoueur config = configurationParJoueur.get(idJoueur);
        if (config == null) {
            throw new IllegalArgumentException("ID joueur invalide : " + idJoueur);
        }
        if (couleur == null) {
            throw new IllegalArgumentException("La couleur ne peut pas être null");
        }
        config.couleurCaseMaitre = couleur;
    }

    @Override
    public void setCouleurPion(int idJoueur, String nomCouleurPionJoueur) {
        ConfigurationCouleurJoueur config = configurationParJoueur.get(idJoueur);
        if (config == null) {
            throw new IllegalArgumentException("ID joueur invalide : " + idJoueur);
        }
        if (nomCouleurPionJoueur == null) {
            throw new IllegalArgumentException("Le nom ne peut pas être null");
        }
        config.nomCouleurPion = nomCouleurPionJoueur;
    }

    @Override
    public void reinitialiserCouleurs() {
        // Vider la map actuelle
        configurationParJoueur.clear();

        // Réinitialiser avec les valeurs par défaut
        List<TypeCouleur> couleurs = new ArrayList<>(Arrays.asList(TypeCouleur.values()));

        // Configuration Joueur 1
        configurationParJoueur.put(ID_JOUEUR_1, new ConfigurationCouleurJoueur(
                TypeCouleur.BLEU,  // Couleur pion par défaut pour joueur 1
                COULEUR_CASE_MAITRE_JOUEUR_1,
                COULEUR_CASE_ELEVE_JOUEUR_1
        ));

        // Configuration Joueur 2
        configurationParJoueur.put(ID_JOUEUR_2, new ConfigurationCouleurJoueur(
                TypeCouleur.ROUGE,  // Couleur pion par défaut pour joueur 2
                COULEUR_CASE_MAITRE_JOUEUR_2,
                COULEUR_CASE_ELEVE_JOUEUR_2
        ));

        // Supprimer le fichier de configuration
        try {
            Files.deleteIfExists(CHEMIN_FICHIER_CONFIG);
        } catch (IOException ignored) {
        }
    }

    /**
     * Retourne une représentation textuelle de la configuration
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        configurationParJoueur.forEach((id, config) -> {
            sb.append("\nJoueur ").append(id).append(" :");
            sb.append("\n   Couleur pion : ").append(config.nomCouleurPion);
            sb.append("\n   Couleur case maître : ").append(config.couleurCaseMaitre);
            sb.append("\n   Couleur case élève : ").append(config.couleurCaseEleve);
        });
        return sb.toString();
    }

    /**
     * Énumération définissant les couleurs disponibles pour les joueurs
     */
    private enum TypeCouleur {
        BLEU(new Color(26, 67, 104), "bleu"),
        ROUGE(new Color(200, 85, 27), "rouge");

        private final Color couleur;
        private final String nom;

        TypeCouleur(Color couleur, String nom) {
            this.couleur = couleur;
            this.nom = nom;
        }
    }

    /**
     * Classe interne représentant la configuration des couleurs pour un joueur
     */
    private static class ConfigurationCouleurJoueur {
        private final Color couleurPion;
        private final Color couleurCaseEleve;
        private String nomCouleurPion;
        private Color couleurCaseMaitre;

        ConfigurationCouleurJoueur(TypeCouleur typeCouleur, Color couleurCaseMaitre, Color couleurCaseEleve) {
            this.couleurPion = typeCouleur.couleur;
            this.nomCouleurPion = typeCouleur.nom;
            this.couleurCaseMaitre = couleurCaseMaitre;
            this.couleurCaseEleve = couleurCaseEleve;
        }
    }

    /**
     * Classe utilisée pour la sérialisation/désérialisation de la configuration
     */
    private static class ConfigurationSauvegardee {
        private final Map<Integer, ConfigurationCouleurJoueur> configurationParJoueur;

        public ConfigurationSauvegardee(Map<Integer, ConfigurationCouleurJoueur> configurationParJoueur) {
            this.configurationParJoueur = configurationParJoueur;
        }

        public Map<Integer, ConfigurationCouleurJoueur> getConfigurationParJoueur() {
            return configurationParJoueur;
        }
    }
}