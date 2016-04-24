package ch.pitschna.mosaic;


import javafx.scene.paint.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;

final class AverageColorCalculator {

    static AverageColorResult calculateAll(BufferedImage image) {
        int height = image.getHeight();
        int halfHeight = height / 2;

        AverageColorResult averageColorResult = new AverageColorResult();


        return averageColorResult;
    }

    static int calculateOne(BufferedImage image, int range, int startX, int startY) {
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range; y++) {
                int rgb = image.getRGB(startX + x, startY + y);

                sumR += (rgb >> 16) & 0xFF; // red
                sumG += (rgb >> 8) & 0xFF; // green
                sumB += (rgb & 0xFF); // blue
            }
        }
        return getRgb(sumR / (range * range), sumG / (range * range), sumB / (range * range));
    }

    private static Color getColor(int rgb) {
        return Color.rgb((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb & 0xFF));
    }

    private static int getRgb(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }
}
