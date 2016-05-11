package ch.pitschna.mosaic.common;


import java.awt.image.BufferedImage;
import java.util.List;

public final class AverageColorCalculator {

    public static AverageColorResult calculateAll(BufferedImage image) {
        int span = image.getHeight();
        int halfSpan = span / 2;

        ColorResult color1 = new ColorResult(calculateOne(image, halfSpan, 0, 0));
        ColorResult color2 = new ColorResult(calculateOne(image, halfSpan, halfSpan, 0));
        ColorResult color3 = new ColorResult(calculateOne(image, halfSpan, 0, halfSpan));
        ColorResult color4 = new ColorResult(calculateOne(image, halfSpan, halfSpan, halfSpan));

        return new AverageColorResult(color1, color2, color3, color4);
    }

    public static int calculateOne(BufferedImage image, int range, int startX, int startY) {
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

    public static ColorResult getColorSinglePixel(BufferedImage image, int startX, int startY){
        return new ColorResult(calculateOne(image, 1, startX, startY));
    }

    private static int getRgb(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }

    public static double getRootSquareDeviation(ColorResult colorImage, ColorResult colorTile) {
        int squareDeviation =0;
        List<Integer> colorsImage = colorImage.getColors();
        List<Integer> colorsTile = colorTile.getColors();
        for (int i = 0; i < colorsImage.size(); i++){
            squareDeviation += (colorsImage.get(i)- colorsTile.get(i))*(colorsImage.get(i)- colorsTile.get(i));
        }
        return Math.sqrt(squareDeviation);
    }
}
