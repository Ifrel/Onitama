package Modele;

import Global.Config;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

// ces tests ne prennent pas encore en compte le joueur IA
class JeuTest {
    /// renvoie un nombre pseudo aléatoire entre 1 et 100
    private int rand(SecureRandom r) {
        return Math.min(Math.max(1, r.nextInt()), 100);
    }

    ///  renvoie un string aléatoire de taille 'size'
    private String randString(int size) {
        String src = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQSTUVWXYZ01234567890-_";
        SecureRandom r = new SecureRandom();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(src.charAt(r.nextInt(src.length())));
        }
        return res.toString();
    }

    ///  vérifie si le rectangle de coin supérieur gauche (i, j) et
    /// de coin inférieur droit (di, dj) est plein
    private boolean rectangleEstPlein(Jeu jeu, int i, int j, int di, int dj) {
        for (int _i = i; _i < jeu.lignes() && _i <= di; _i++) {
            for (int _j = j; _j < jeu.colonnes() && _j <= dj; _j++) {
                if( ! jeu.estPleine(_i, _j)) {
                    return false;
                }
            }
        }
        return true;
    }

    ///  vérifie si le rectangle de coin supérieur gauche (i, j) et
    /// de coin inférieur droit (di, dj) est vide
    private boolean rectangleEstVide(Jeu jeu, int i, int j, int di, int dj) {
        for (int _i = i; _i < jeu.lignes() && _i <= di; _i++) {
            for (int _j = j; _j < jeu.colonnes() && _j <= dj; _j++) {
                if( ! jeu.estVide(_i, _j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    void recommencer() {
        // vérifie que la méthode recommencer() re-génère bien une nouvelle instance du jeu
        // avec de nouvelles dimensions ou les mêmes
        Jeu jeu;
        jeu = new Jeu(5, 5);
        jeu.jouer(2, 2);
        // recommencer une nouvelle partie avec les mêmes dimensions
        jeu.recommencer();
        // les dimensions sont les bonnes
        assertEquals(5, jeu.lignes());
        assertEquals(5, jeu.colonnes());

        // la nouvelle grille doit être pleine
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 4));

        jeu = new Jeu(5, 5);
        jeu.jouer(2, 2);
        // recommencer une nouvelle partie avec d'autres dimensions
        // (action changement de taille de la grille)
        jeu.recommencer(3, 7)        // la nouvelle grille doit être pleine
;
        // les dimensions sont les bonnes
        assertEquals(3, jeu.lignes());
        assertEquals(7, jeu.colonnes());

        // la nouvelle grille doit être pleine
        assertTrue(rectangleEstPlein(jeu, 0, 0, 2, 6));

        SecureRandom r;
        int a, b, c, d;

        r = new SecureRandom();
        a = rand(r);
        b = rand(r);
        // gaufre de taille aléatoire
        jeu = new Jeu(a, b);

        c = rand(r) % jeu.lignes();
        d = rand(r) % jeu.colonnes();
        // coup aléatoire
        jeu.jouer(c, d);

        // recommencer une nouvelle partie avec les mêmes dimensions (aléatoires)
        jeu.recommencer();
        // les dimensions sont les bonnes
        assertEquals(a, jeu.lignes());
        assertEquals(b, jeu.colonnes());

        // la nouvelle grille doit être pleine
        assertTrue(rectangleEstPlein(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));

        r = new SecureRandom();
        // gaufre de taille aléatoire
        jeu = new Jeu(rand(r), rand(r));

        c = rand(r) % jeu.lignes();
        d = rand(r) % jeu.colonnes();
        // coup aléatoire
        jeu.jouer(c, d);

        a = rand(r);
        b = rand(r);
        // recommencer une nouvelle partie avec d'autres dimensions (aléatoires)
        // (action changement de taille de la grille)
        jeu.recommencer(a, b);
        // les dimensions sont les bonnes
        assertEquals(a, jeu.lignes());
        assertEquals(b, jeu.colonnes());

        // la nouvelle grille doit être pleine
        assertTrue(rectangleEstPlein(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));
    }

    @Test
    void jouer() {
        // vérifie qu'un coup sur la grille ait bien l'effet escompté
        Jeu jeu;
        jeu = new Jeu(5, 5);
        // P : poison; G : gauffre
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G | G | G |
        //  5   | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 4));

        jeu.jouer(3, 3);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G |   |   |
        //  5   | G | G | G |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 3, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));
        assertTrue(rectangleEstPlein(jeu, 0, 3, 2, 4));

        jeu.jouer(2, 1);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G |   |   |   |   |
        //  4   | G |   |   |   |   |
        //  5   | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 2, 2, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 0));
        assertTrue(rectangleEstPlein(jeu, 0, 1, 1, 4));

        jeu.jouer(0, 2);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G |   |   |   |
        //  2   | G | G |   |   |   |
        //  3   | G |   |   |   |   |
        //  4   | G |   |   |   |   |
        //  5   | G |   |   |   |   |

        // comme le rectangle précédent n'est pas inclus dans le nouveau, on le revérifie "au cas où"
        assertTrue(rectangleEstVide(jeu, 2, 1, 4, 1));
        assertTrue(rectangleEstVide(jeu, 0, 2, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 0));
        assertTrue(rectangleEstPlein(jeu, 0, 1, 1, 1));

        SecureRandom r = new SecureRandom();
        // matrice de taille aléatoire
        jeu = new Jeu(rand(r), rand(r));

        // liste des coups joués aléatoirement
        LinkedList<int[]> coups = new LinkedList<>();

        // nombre de coups aléatoires
        int n = rand(r);
        for (int i = 0; i < n; i++) {
            int a, b;
            a = rand(r) % jeu.lignes();
            b = rand(r) % jeu.colonnes();
            // si le coup a eu un effet, l'ajouter à la liste des coips
            if (jeu.jouer(a, b)) {
                int [] c = {a, b};
                coups.add(c);
            }
        }

        // pour chaque coup, vérifier si le rectangle créé est bien vide
        // (plusieurs vérification d'un même rectangle comme effet secondaire)
        for (int i = 0; i < coups.size(); i++) {
            int [] c = coups.get(i);
            assertTrue(rectangleEstVide(jeu, c[0], c[1], jeu.lignes() - 1, jeu.colonnes() - 1));
        }

        jeu = new Jeu(5, 5);
        // P : poison; G : gauffre
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G | G | G |
        //  5   | G | G | G | G | G |

        jeu.jouer(1, 1);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G |   |   |   |   |
        //  3   | G |   |   |   |   |
        //  4   | G |   |   |   |   |
        //  5   | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 1, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 0));
        assertTrue(rectangleEstPlein(jeu, 0, 1, 0, 4));

        jeu.jouer(0, 0);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   |   |   |   |   |   |
        //  2   |   |   |   |   |   |
        //  3   |   |   |   |   |   |
        //  4   |   |   |   |   |   |
        //  5   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 0, 0, 4, 4));
        assertTrue(jeu.estVide(Config.posPoison.x, Config.posPoison.y));
        assertFalse(jeu.estPleine(Config.posPoison.x, Config.posPoison.y));
    }

    @Test
    void annulerCoup() {
        // vérifie que la méthode annulerCoup() fonctionne bien
        Jeu jeu;
        jeu = new Jeu(5, 5);
        // P : poison; G : gauffre
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G | G | G |
        //  5   | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 4));

        jeu.jouer(2, 2);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G |   |   |   |
        //  4   | G | G |   |   |   |
        //  5   | G | G |   |   |   |

        assertTrue(rectangleEstVide(jeu, 2, 2, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 1, 4));

        jeu.annulerCoup();
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G | G | G |
        //  5   | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 4));

        jeu.jouer(2, 0);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   |   |   |   |   |   |
        //  4   |   |   |   |   |   |
        //  5   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 2, 0, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 1, 4));

        jeu.jouer(0, 2);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G |   |   |   |
        //  2   | G | G |   |   |   |
        //  3   |   |   |   |   |   |
        //  4   |   |   |   |   |   |
        //  5   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 2, 0, 4, 1));
        assertTrue(rectangleEstVide(jeu, 0, 2, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 1, 1));

        jeu.annulerCoup();
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   |   |   |   |   |   |
        //  4   |   |   |   |   |   |
        //  5   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 2, 0, 4, 4));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 1, 4));

        jeu.annulerCoup();
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G | G | G |
        //  5   | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 4));

        jeu = new Jeu(5, 7);
        // P : poison; G : gauffre
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 6, 6));

        jeu.jouer(3, 3);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 3, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));
        assertTrue(rectangleEstPlein(jeu, 0, 3, 2, 6));

        jeu.jouer(3, 1);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G |   |   |   |   |   |   |
        //  5   | G |   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 1, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 0));
        assertTrue(rectangleEstPlein(jeu, 0, 1, 2, 6));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 3, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));
        assertTrue(rectangleEstPlein(jeu, 0, 3, 2, 6));

        jeu.jouer(0, 3);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G |   |   |   |   |
        //  2   | G | G | G |   |   |   |   |
        //  3   | G | G | G |   |   |   |   |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 0, 3, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 3, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));
        assertTrue(rectangleEstPlein(jeu, 0, 3, 2, 6));

        jeu.jouer(1, 2);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G |   |   |   |   |   |
        //  3   | G | G |   |   |   |   |   |
        //  4   | G | G |   |   |   |   |   |
        //  5   | G | G |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 2, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 0, 6));

        jeu.jouer(1, 0);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   |   |   |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 0, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 0, 6));

        jeu.jouer(0, 1);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P |   |   |   |   |   |   |
        //  2   |   |   |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 0, 4, 0));
        assertTrue(rectangleEstVide(jeu, 0, 1, 4, 6));
        assertTrue(jeu.estPleine(0, 0));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   |   |   |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 0, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 0, 6));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G |   |   |   |   |   |
        //  3   | G | G |   |   |   |   |   |
        //  4   | G | G |   |   |   |   |   |
        //  5   | G | G |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 2, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 0, 6));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 3, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));
        assertTrue(rectangleEstPlein(jeu, 0, 3, 2, 6));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 6, 6));
    }

    @Test
    void refaireCoup() {
        // vérifie que la méthode refaireCoup() fonctionne bien
        Jeu jeu;
        jeu = new Jeu(5, 7);
        // P : poison; G : gauffre
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 6, 6));

        jeu.jouer(3, 3);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 3, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));
        assertTrue(rectangleEstPlein(jeu, 0, 3, 2, 6));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        assertTrue(rectangleEstPlein(jeu, 0, 0, 6, 6));

        jeu.refaireCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 3, 3, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 2));
        assertTrue(rectangleEstPlein(jeu, 0, 3, 2, 6));

        jeu.jouer(1, 2);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G |   |   |   |   |   |
        //  3   | G | G |   |   |   |   |   |
        //  4   | G | G |   |   |   |   |   |
        //  5   | G | G |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 2, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 0, 6));

        jeu.jouer(1, 0);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   |   |   |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 0, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 0, 6));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G |   |   |   |   |   |
        //  3   | G | G |   |   |   |   |   |
        //  4   | G | G |   |   |   |   |   |
        //  5   | G | G |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 2, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 0, 6));

        jeu.jouer(2, 0);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 2, 0, 4, 1));
        assertTrue(rectangleEstVide(jeu, 1, 2, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 1, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 0, 6));

        jeu.annulerCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G |   |   |   |   |   |
        //  3   | G | G |   |   |   |   |   |
        //  4   | G | G |   |   |   |   |   |
        //  5   | G | G |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 1, 2, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 4, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 0, 6));

        jeu.refaireCoup();
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        assertTrue(rectangleEstVide(jeu, 2, 0, 4, 1));
        assertTrue(rectangleEstVide(jeu, 1, 2, 4, 6));
        assertTrue(rectangleEstPlein(jeu, 0, 0, 1, 1));
        assertTrue(rectangleEstPlein(jeu, 0, 2, 0, 6));

    }

//    @Test
//    void toggleIA() {
//    }

    @Test
    void checkEstDansJeu() {
        // vérifie que la méthode checkEstDansJeu() renvoie bien une exception quand la position (i, j)
        // est en dehors de la grille
        SecureRandom r = new SecureRandom();
        Jeu j1 = new Jeu(5, 3);
        // test de valeurs négatives et supérieures aux bornes, prédéterminées et aléatoires
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(1, 7);});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(-1, 7);});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(-1, 2);});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(3, -2);});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu( -1 * rand(r), 0);});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(1, -1 * rand(r));});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(6, 3);});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(rand(r) + 5, 2);});
        assertThrows(RuntimeException.class, () -> {j1.checkEstDansJeu(1, rand(r) + 7);});
        // valeurs correctes
        j1.checkEstDansJeu(2, 2);
        j1.checkEstDansJeu(1, 2);
        j1.checkEstDansJeu(4, 2);
        j1.checkEstDansJeu(4, 1);

        // vérifie le fait que toutes les cases présentent dans la matrice soient considérées comme telles
        for (int i = 0; i < j1.lignes(); i++) {
            for (int j = 0; j < j1.colonnes(); j++) {
                j1.checkEstDansJeu(i, j);
            }
        }

        // matrice de taille aléatoire
        Jeu j2 = new Jeu(rand(r), rand(r));

        // vérifications sur matrice de taille aléatoire
        assertThrows(RuntimeException.class, () -> {j2.checkEstDansJeu(j2.lignes(), j2.colonnes() - 1);});
        assertThrows(RuntimeException.class, () -> {j2.checkEstDansJeu(j2.lignes() - 1, j2.colonnes());});
        assertThrows(RuntimeException.class, () -> {j2.checkEstDansJeu(rand(r) + j2.lignes(), rand(r) + j2.colonnes());});
        assertThrows(RuntimeException.class, () -> {j2.checkEstDansJeu(rand(r) + j2.lignes(), j2.colonnes() - 1);});
        assertThrows(RuntimeException.class, () -> {j2.checkEstDansJeu(j2.lignes() - 1, rand(r) + j2.colonnes());});

        // vérifie le fait que toutes les cases présentent dans la matrice soient considérées comme telles
        for (int i = 0; i < j2.lignes(); i++) {
            for (int j = 0; j < j2.colonnes(); j++) {
                j2.checkEstDansJeu(i, j);
            }
        }
    }

    @Test
    void flipJoueur() {
        // vérifie que l'inversion des joueurs se passe correctement
        Jeu jeu;
        jeu = new Jeu(5, 5);

        // on échange 10 fois les joueurs
        for (int i = 0; i < 10; i++) {
            assertEquals(Config.joueurB, jeu.flipJoueur());
            assertEquals(Config.joueurA, jeu.flipJoueur());
        }

        jeu = new Jeu(5, 5);
        SecureRandom r = new SecureRandom();
        int nbFlips = rand(r);
        int joueurAttendu = (nbFlips % 2 == 1) ? Config.joueurA : Config.joueurB;
        // on échange nbFlips + 1 fois les joueurs
        for (int i = 0; i < nbFlips; i++) {
            jeu.flipJoueur();
        }
        assertEquals(joueurAttendu, jeu.flipJoueur());
    }

    @Test
    void estVide() {

        // vérifie que la méthode estVide renvoie la bonne valeur
        Jeu jeu;
        jeu = new Jeu(5, 7);
        // P : poison; G : gauffre
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        // au début du jeu toutes les cases sont pleines
        assertTrue(rectangleEstPlein(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));

        jeu.jouer(4, 4);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G |   |   |   |

        assertTrue(jeu.estVide(4, 4));
        jeu.jouer(Config.posPoison.x, Config.posPoison.y);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   |   |   |   |   |   |   |   |
        //  2   |   |   |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        // si on mange le poison toutes les cases sont vides
        assertTrue(rectangleEstVide(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));

        SecureRandom r = new SecureRandom();
        // même chose pour une matrice de taille aléatoire
        jeu = new Jeu(rand(r), rand(r));

        // au début du jeu toutes les cases sont pleines
        assertTrue(rectangleEstPlein(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));

        int a, b;
        a = rand(r) % jeu.lignes(); // ligne
        b = rand(r) % jeu.colonnes(); // colonne
        // coup aléatoire
        jeu.jouer(a, b);
        // case jouée forcément vide
        assertTrue(jeu.estVide(a, b));
        jeu.jouer(Config.posPoison.x, Config.posPoison.y);

        // si on mange le poison toutes les cases sont vides
        assertTrue(rectangleEstVide(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));
    }

    @Test
    void estPleine() {
        // vérifie que la méthode estPleine renvoie la bonne valeur
        Jeu jeu;
        jeu = new Jeu(5, 7);
        // P : poison; G : gauffre
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        // au début du jeu toutes les cases sont pleines
        assertTrue(rectangleEstPlein(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));

        jeu.jouer(4, 4);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G |   |   |   |

        assertFalse(jeu.estPleine(4, 4));
        jeu.jouer(Config.posPoison.x, Config.posPoison.y);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   |   |   |   |   |   |   |   |
        //  2   |   |   |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        // si on mange le poison toutes les cases sont vides
        assertTrue(rectangleEstVide(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));

        SecureRandom r = new SecureRandom();
        // même chose pour une matrice de taille aléatoire
        jeu = new Jeu(rand(r), rand(r));

        // au début du jeu toutes les cases sont pleines
        assertTrue(rectangleEstPlein(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));

        int a, b;
        a = rand(r) % jeu.lignes(); // ligne
        b = rand(r) % jeu.colonnes(); // colonne
        // coup aléatoire
        jeu.jouer(a, b);
        // case jouée forcément vide
        assertFalse(jeu.estPleine(a, b));
        jeu.jouer(Config.posPoison.x, Config.posPoison.y);

        // si on mange le poison toutes les cases sont vides
        assertTrue(rectangleEstVide(jeu, 0, 0, jeu.lignes() - 1, jeu.colonnes() - 1));
    }

    @Test
    void estPoison() {
        // vérifie que la case contenant le poison est bien intialisée
        Jeu jeu;
        jeu = new Jeu(5, 5);
        assertTrue(jeu.estPoison(Config.posPoison.x, Config.posPoison.y));

        // vérifie que des matrices de taille i, j contiennent le poison au bon endroit
        for (int i = 1; i < 100; i++) {
            for (int j = 1; j < 100; j++) {
                jeu = new Jeu(i, j);
                assertTrue(jeu.estPoison(Config.posPoison.x, Config.posPoison.y));
            }
        }

        SecureRandom r = new SecureRandom();
        // vérifie qu'une matrice de taille aléatoire contienne le poison au bon endroit
        jeu = new Jeu(rand(r), rand(r));
        assertTrue(jeu.estPoison(Config.posPoison.x, Config.posPoison.y));
    }

    @Test
    void joueurCourant() {
        // vérifie que les tours soient correctement répartis (chacun son tour)
        Jeu jeu = new Jeu(5, 5);
        assertEquals(Config.joueurA, jeu.joueurCourant());
        jeu.jouer(3, 4);
        assertEquals(Config.joueurB, jeu.joueurCourant());
        jeu.jouer(3, 3);
        assertEquals(Config.joueurA, jeu.joueurCourant());
        jeu.jouer(2, 2);
        assertEquals(Config.joueurB, jeu.joueurCourant());
        jeu.jouer(1, 1);
        assertEquals(Config.joueurA, jeu.joueurCourant());

        SecureRandom r = new SecureRandom();
        int a = rand(r);
        // matrice carrée de taille aléatoire >= 0 et <= 100
        jeu = new Jeu(a, a);
        int nbCoups = Math.min(rand(r), a - 1);
        int joueurAttendu = (nbCoups % 2 == 0) ? Config.joueurA : Config.joueurB;

        // joue en diagonale en partant de la direction du coin inférieur droit
        // la case de départ = (nbCoups, nbCoups)
        // jusqu'à (0, 0)
        for (int i = 0; i < nbCoups; i++) {
            for (int j = 0; j < nbCoups; j++) {
                jeu.jouer(nbCoups - i, nbCoups - j);
            }
        }

        assertEquals(joueurAttendu, jeu.joueurCourant());
    }

    @Test
    void getGagnant() {
        // joue plusieurs parties courtes et vérifie qui gagne
        Jeu jeu;
        jeu  = new Jeu(10, 10);
        // le joueur A mange directement le poison, ce qui fait gagner le joueur B
        jeu.jouer(Config.posPoison.x, Config.posPoison.y);
        assertEquals(Config.joueurB, jeu.getGagnant());

        jeu = new Jeu(10, 10);
        jeu.jouer(1, 1);
        // le joueur B mange le poison, ce qui fait gagner le joueur B
        jeu.jouer(Config.posPoison.x, Config.posPoison.y);
        assertEquals(Config.joueurA, jeu.getGagnant());
    }

    @Test
    void lignes() {
        // vérifie que la méthode lignes() renvoie bien le bon nombre de lignes
        Jeu jeu;
        jeu = new Jeu(72, 24);
        assertEquals(72, jeu.lignes());
        jeu = new Jeu(0, 24);
        assertEquals(0, jeu.lignes());
        jeu = new Jeu(2, 24);
        assertEquals(2, jeu.lignes());

        // pour des jeu de taille l,c avec 0 <= l < 100 et 0 <= c < 100
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                jeu = new Jeu(i, j);
                assertEquals(i, jeu.lignes());
            }
        }

        SecureRandom r = new SecureRandom();
        int a, b;
        a = rand(r); // lignes
        b = rand(r); // colonnes
        // jeu de taille aléatoire
        jeu = new Jeu(a, b);
        assertEquals(a, jeu.lignes());
    }

    @Test
    void colonnes() {
        // vérifie que la méthode colonnes() renvoie bien le bon nombre de colonnes
        Jeu jeu;
        jeu = new Jeu(72, 24);
        assertEquals(24, jeu.colonnes());
        jeu = new Jeu(72, 0);
        assertEquals(0, jeu.colonnes());
        jeu = new Jeu(72, 1);
        assertEquals(1, jeu.colonnes());

        // pour des jeu de taille l,c avec 0 <= l < 100 et 0 <= c < 100
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                jeu = new Jeu(i, j);
                assertEquals(j, jeu.colonnes());
            }
        }

        SecureRandom r = new SecureRandom();
        int a, b;
        a = rand(r); // lignes
        b = rand(r); // colonnes
        // jeu de taille aléatoire
        jeu = new Jeu(a, b);
        assertEquals(b, jeu.colonnes());
    }

    @Test
    void nomJoueurs() {
        // vérifie si les noms des joueurs sont gérés correctement
        Jeu jeu;
        jeu = new Jeu(5, 5);
        assertEquals("InfosJoueur A", jeu.getNameJoueurA());
        assertEquals("InfosJoueur B", jeu.getNameJoueurB());

        jeu.setNameJoueurA("Bonjour !!!!");
        assertEquals("Bonjour !!!!", jeu.getNameJoueurA());

        jeu.setNameJoueurB("ABCDEFGH");
        assertEquals("ABCDEFGH", jeu.getNameJoueurB());
    }

    @Test
    void win() {
        // vérifie si on est correctement en situation de victoire
        Jeu jeu;
        jeu = new Jeu(5, 5);
        // P : poison; G : gauffre
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        assertFalse(jeu.estPartieFinie());

        jeu.jouer(4, 4);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G |   |   |   |

        assertFalse(jeu.estPartieFinie());

        jeu.jouer(Config.posPoison.x, Config.posPoison.y);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   |   |   |   |   |   |   |   |
        //  2   |   |   |   |   |   |   |   |
        //  3   |   |   |   |   |   |   |   |
        //  4   |   |   |   |   |   |   |   |
        //  5   |   |   |   |   |   |   |   |

        assertTrue(jeu.estPartieFinie());
    }

    @Test
    void sauvegarderChargerJeu() {


        Jeu jeu;
        jeu = new Jeu(5, 5);
        // P : poison; G : gauffre
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G | G | G | G |

        jeu.jouer(4, 4);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G | G | G | G | G |
        //  4   | G | G | G | G | G | G | G |
        //  5   | G | G | G | G |   |   |   |

        jeu.jouer(2, 3);
        // ----------------------------------
        //        A   B   C   D   E   F   G
        //
        //  1   | P | G | G | G | G | G | G |
        //  2   | G | G | G | G | G | G | G |
        //  3   | G | G | G |   |   |   |   |
        //  4   | G | G | G |   |   |   |   |
        //  5   | G | G | G |   |   |   |   |


        File dir = new File(Config.saveDir.toString());
        File[] files = dir.listFiles();

        // Print name of the all files present in that path
        if (files != null) {
            for (File file : files) {
                System.err.println(file.getName());
            }
        }
    }

    @Test
    void stats() {
        Jeu jeu;
        jeu = new Jeu(5, 5);
        // P : poison; G : gauffre
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G | G | G |
        //  5   | G | G | G | G | G |

        assertEquals(25, jeu.getNbTotalCases());

        assertEquals(0, jeu.nbCasesMangeesJoueurA());
        assertEquals(0, jeu.nbCasesMangeesJoueurB());

        jeu.jouer(2, 2);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G |   |   |   |
        //  4   | G | G |   |   |   |
        //  5   | G | G |   |   |   |

        assertEquals(25, jeu.getNbTotalCases());

        assertEquals(9, jeu.nbCasesMangeesJoueurA());
        assertEquals(0, jeu.nbCasesMangeesJoueurB());

        jeu.annulerCoup();
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G | G | G | G |
        //  4   | G | G | G | G | G |
        //  5   | G | G | G | G | G |

        assertEquals(25, jeu.getNbTotalCases());

        assertEquals(0, jeu.nbCasesMangeesJoueurA());
        assertEquals(0, jeu.nbCasesMangeesJoueurB());

        jeu.refaireCoup();
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   | P | G | G | G | G |
        //  2   | G | G | G | G | G |
        //  3   | G | G |   |   |   |
        //  4   | G | G |   |   |   |
        //  5   | G | G |   |   |   |

        assertEquals(25, jeu.getNbTotalCases());

        assertEquals(9, jeu.nbCasesMangeesJoueurA());
        assertEquals(0, jeu.nbCasesMangeesJoueurB());

        jeu.jouer(0, 0);
        // --------------------------
        //        A   B   C   D   E
        //
        //  1   |   |   |   |   |   |
        //  2   |   |   |   |   |   |
        //  3   |   |   |   |   |   |
        //  4   |   |   |   |   |   |
        //  5   |   |   |   |   |   |

        assertEquals(25, jeu.getNbTotalCases());

        assertEquals(9, jeu.nbCasesMangeesJoueurA());
        assertEquals(16, jeu.nbCasesMangeesJoueurB());
    }
}