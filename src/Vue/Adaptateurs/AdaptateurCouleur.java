package Vue.Adaptateurs;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.io.IOException;

/**
 * Adaptateur personnalisé pour la sérialisation/désérialisation des objets Color avec GSON
 * Permet de convertir les couleurs en format JSON et vice-versa
 */
public class AdaptateurCouleur extends TypeAdapter<Color> {

    /**
     * Sérialise un objet Color en JSON
     *
     * @param writer Writer JSON
     * @param color  Objet Color à sérialiser
     * @throws IOException En cas d'erreur d'écriture
     */
    @Override
    public void write(JsonWriter writer, Color color) throws IOException {
        writer.beginObject();
        writer.name("rgb").value(color.getRGB());
        writer.endObject();
    }

    /**
     * Désérialise un objet JSON en Color
     *
     * @param reader Reader JSON
     * @return Objet Color créé
     * @throws IOException En cas d'erreur de lecture
     */
    @Override
    public Color read(JsonReader reader) throws IOException {
        reader.beginObject();
        int rgb = 0;
        while (reader.hasNext()) {
            if (reader.nextName().equals("rgb")) {
                rgb = reader.nextInt();
            }
        }
        reader.endObject();
        return new Color(rgb, true);
    }
}
