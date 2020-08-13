package internal.Canvas;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ColorShade {
    public static Collection<Color> colors = new HashSet<>(Arrays.asList(Color.LIGHT_GRAY,Color.magenta,Color.BLUE,
            Color.cyan,Color.GRAY,Color.green,Color.ORANGE,Color.pink,Color.red,Color.YELLOW));
    /**
     * Automatically produce a series of shades based on the initial color
     * @param init initial color
     * @param echelon number of shades
     * @return color shades
     */
    public static List<Color> generateShadesMap(Color init, int echelon){
        assert(echelon > 1);
        // echelon-1 times of brightening
        echelon -= 1;
        double fraction = 1./echelon;
        // List for colors
        List<Color> colors = new ArrayList<>(echelon);
        colors.add(init);
        Color c = init;
        for (int i = 0; i < echelon; i++) {
            Color c_ = brighten(c,fraction);
            colors.add(c_);
            c = c_;
        }
        return colors;
    }
    /**
     * Make a color brighten.
     *
     * @param color Color to make brighten.
     * @param fraction Darkness fraction.
     * @return Lighter color.
     */
    public static Color brighten(Color color, double fraction) {

        int red = (int) Math.round(Math.min(255, color.getRed() + 255 * fraction));
        int green = (int) Math.round(Math.min(255, color.getGreen() + 255 * fraction));
        int blue = (int) Math.round(Math.min(255, color.getBlue() + 255 * fraction));

        int alpha = color.getAlpha();

        return new Color(red, green, blue, alpha);

    }
}
