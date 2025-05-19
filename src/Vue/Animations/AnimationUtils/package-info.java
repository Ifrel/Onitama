package Vue.Animations.AnimationUtils;
/*
 * Vue.Animations.AnimationUtils.AnimationUtils.java
 *
 * Ce fichier contient les classes et utilitaires nécessaires pour appliquer
 * une animation de retournement de carte 3D à n'importe quel composant Swing existant,
 * notamment un JButton, sans modifier sa classe d'origine.
 *
 * Table des Matières :
 * 1. Description Générale
 * 2. Explication Détaillée du Mécanisme (JLayer)
 * 3. Complexité
 * 4. Utilisation (Comment appliquer l'animation)
 * 5. Code Source des Composants (AnimationListener, CardFlipAnimator, CardFlipLayerUI, AnimationUtils)
 * 6. Exemple d'Utilisation (Classe TestAnimationUtils)
 */

/*
 * =============================================================================
 * 1. Description Générale
 * =============================================================================
 *
 * L'objectif est d'extraire la logique d'une animation de retournement de carte
 * (rotation, mise à l'échelle pour un effet 3D) et de la rendre applicable à
 * n'importe quel composant Swing existant. Traditionnellement, cela impliquerait
 * de créer une sous-classe du composant et de surcharger sa méthode paintComponent().
 * Cette approche devient fastidieuse si l'on souhaite animer différents types de composants
 * ou des instances existantes de composants standard.
 *
 * La solution adoptée ici utilise l'API JLayer de Swing (disponible depuis Java 7).
 * Un JLayer agit comme un conteneur décorateur qui enveloppe un autre composant (la "vue").
 * Associé à un LayerUI personnalisé (ici, CardFlipLayerUI), le JLayer permet d'intercepter
 * les appels à la méthode paint() de la vue et de modifier le processus de dessin,
 * ainsi que potentiellement d'intercepter les événements.
 *
 * L'animation elle-même est gérée par une classe distincte, CardFlipAnimator,
 * qui suit la progression temporelle et calcule l'angle de rotation actuel.
 * CardFlipLayerUI obtient cet angle de l'animateur et l'utilise pour appliquer une
 * transformation affine (rotation et mise à l'échelle) au contexte graphique AVANT
 * de laisser le composant original se dessiner. Le composant se dessine alors
 * normalement, mais son contenu apparaît déformé par la transformation appliquée.
 *
 * Une classe utilitaire, AnimationUtils, est fournie pour simplifier la mise en place :
 * une simple méthode statique permet d'appliquer l'animation à un JButton existant
 * en créant l'animateur, le LayerUI, le JLayer, et en configurant les écouteurs
 * nécessaires.
 */

/*
 * =============================================================================
 * 2. Explication Détaillée du Mécanisme (JLayer)
 * =============================================================================
 *
 * Le processus se déroule comme suit :
 *
 * 1.  Initialisation : Vous créez votre JButton standard. La méthode
 * AnimationUtils.applyCardFlipAnimation() est appelée avec ce bouton.
 * Elle crée un CardFlipAnimator, un CardFlipLayerUI (lié à l'animateur),
 * et un JLayer qui enveloppe le bouton et utilise ce LayerUI. C'est ce
 * JLayer résultant que vous ajoutez à votre conteneur Swing.
 *
 * 2.  Déclenchement de l'Animation : Un ActionListener est ajouté *au bouton
 * original* (celui enveloppé). Lorsque l'utilisateur clique sur le bouton
 * visible (en réalité, le clic est intercepté par le JLayer et transmis
 * au bouton enveloppé), cet ActionListener est déclenché. Il appelle
 * startAnimation() sur le CardFlipAnimator.
 *
 * 3.  Progression de l'Animation : Le CardFlipAnimator démarre son Timer Swing.
 * Le timer se déclenche périodiquement (e.g., 60 fois par seconde). À chaque
 * tick, la méthode CardFlipAnimator.mettreAJourAnimation() est appelée.
 * Elle calcule le nouvel angle de rotation (angleActuel) en interpolant
 * entre l'angle de départ et l'angle cible, en fonction du temps écoulé et
 * d'une fonction d'easing (ici, basée sur Math.cos pour un mouvement doux).
 *
 * 4.  Notification de Mise à Jour : Après avoir mis à jour l'angle,
 * CardFlipAnimator.mettreAJourAnimation() appelle notifyAnimationUpdated().
 * Cela déclenche la méthode animationUpdated() sur tous les listeners enregistrés.
 *
 * 5.  Demande de Rafraîchissement : Le listener d'animation (une lambda) ajouté par
 * AnimationUtils.applyCardFlipAnimation() au CardFlipAnimator est notifié.
 * Ce listener appelle layer.repaint() sur le JLayer. Cela indique à Swing que
 * le JLayer a besoin d'être redessiné.
 *
 * 6.  Interception de la Peinture : Lorsque Swing traite la demande de repaint
 * pour le JLayer, il délègue la tâche de peinture à l'objet LayerUI associé,
 * en appelant CardFlipLayerUI.paint(Graphics g, JComponent c). 'g' est le
 * contexte graphique et 'c' est le JLayer lui-même (qui peut fournir l'accès
 * à la vue enveloppée via JLayer.getView()).
 *
 * 7.  Application de la Transformation : Dans CardFlipLayerUI.paint(), on obtient
 * l'angle actuel de l'animateur. On crée une AffineTransform qui représente
 * la rotation par cet angle et une mise à l'échelle (rétrécissement sur un axe
 * pour l'effet 3D), centrée sur le composant. Cette transformation est ensuite
 * appliquée au contexte graphique 2D (Graphics2D) via g2d.transform().
 *
 * 8.  Peinture du Composant Enveloppé : L'appel super.paint(g2d, c) est effectué.
 * Pour un JLayer, cet appel demande à la vue enveloppée (le bouton original)
 * de se peindre. MAIS, la vue se peint en utilisant le contexte graphique
 * modifié (g2d). En conséquence, tout ce que le bouton dessine (texte, icône,
 * fond, bordure) est automatiquement déformé par l'AffineTransform.
 *
 * 9.  Nettoyage : Le LayerUI restaure la transformation graphique originale pour
 * éviter d'affecter d'autres opérations de dessin après son exécution.
 *
 * 10. Fin de l'Animation : Lorsque l'animation atteint 100% de progression,
 * l'animateur arrête le timer, ajuste l'angle final et notifie une dernière
 * fois. Le dernier repaint affiche l'état final stabilisé.
 */

/*
 * =============================================================================
 * 3. Complexité
 * =============================================================================
 *
 * Complexité Temporelle :
 * - L'animation elle-même (CardFlipAnimator) est déclenchée par un timer à
 * fréquence fixe. Chaque tick prend un temps constant (calcul d'angle,
 * notification des listeners), soit O(1) par tick (en assumant un faible
 * nombre de listeners).
 * - La méthode paint() du LayerUI est appelée à chaque tick du timer (via repaint).
 * Elle effectue des calculs trigonométriques et matriciels (O(1)). Le coût
 * principal est l'appel à super.paint(), dont la complexité dépend du composant
 * enveloppé (taille, contenu). L'ajout de la transformation n'augmente pas
 * significativement cette complexité O(PaintOriginal).
 * - L'animation totale dure un temps fixe (DUREE_ANIMATION), indépendamment du
 * nombre ou de la taille des composants animés.
 *
 * Complexité Spatiale :
 * - Pour chaque composant auquel l'animation est appliquée, une instance de
 * CardFlipAnimator, CardFlipLayerUI et JLayer est créée.
 * - CardFlipAnimator et CardFlipLayerUI ont une complexité spatiale O(1)
 * (stockent quelques variables et références). L'animateur stocke aussi
 * une liste de listeners O(N_L), où N_L est le nombre de listeners (typiquement 1).
 * - JLayer stocke une référence à la vue et au LayerUI, O(1).
 * - La complexité spatiale totale ajoutée par composant animé est O(1).
 * - La complexité spatiale globale est O(N_A), où N_A est le nombre de composants
 * animés dans l'application.
 */

/*
 * =============================================================================
 * 4. Utilisation (Comment appliquer l'animation)
 * =============================================================================
 *
 * Pour appliquer l'animation de retournement 3D à un JButton existant (ou un autre JComponent,
 * en adaptant le type générique de JLayer/LayerUI si nécessaire) :
 *
 * 1.  Créez votre JButton normalement, comme vous le feriez sans animation.
 * Exemple :
 * JButton monBouton = new JButton("Cliquez ici");
 * monBouton.setPreferredSize(new Dimension(150, 50));
 *
 * 2.  Utilisez la méthode utilitaire AnimationUtils.applyCardFlipAnimation()
 * pour envelopper votre bouton.
 * Exemple :
 * JLayer<JButton> monBoutonAnime = AnimationUtils.applyCardFlipAnimation(monBouton);
 *
 * 3.  Au lieu d'ajouter le bouton original (monBouton) à votre conteneur Swing
 * (JPanel, JFrame, etc.), ajoutez le JLayer résultant (monBoutonAnime).
 * Exemple :
 * monPanel.add(monBoutonAnime);
 *
 * 4.  Lorsque l'utilisateur cliquera sur le bouton affiché, l'animation de
 * retournement se produira. Le bouton original (monBouton) à l'intérieur du JLayer
 * fonctionne toujours normalement (vous pouvez toujours ajouter des ActionListeners
 * directement au bouton original, ils seront déclenchés).
 *
 * Important : La méthode applyCardFlipAnimation doit être appelée sur le bouton
 * *avant* qu'il ne soit ajouté à un conteneur.
 */