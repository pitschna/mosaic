package ch.pitschna.mosaic.common;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public static ColorResult getColorSinglePixel(BufferedImage image, int startX, int startY, List<Double> colorCorrector){
        return new ColorResult(calculateCorrectedRgb(colorCorrector,calculateOne(image, 1, startX, startY)));
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

    public static List<Double> calculateColorCorrector(ColorResult originalColor, ColorResult tileColor) {
        ArrayList<Double> colorCorrector = new ArrayList<>();
        List<Integer> originalColors = originalColor.getColors();
        List<Integer> tileColors = tileColor.getColors();
        for (int i = 0; i< originalColors.size(); i++) {
            int sign = originalColors.get(i) - tileColors.get(i);
            if(sign == 0){
                colorCorrector.add(0D);
            } else if(sign < 0){
                colorCorrector.add(((double) originalColors.get(i))/ tileColors.get(i));
            } else if(sign > 0){
                colorCorrector.add(-((double) (255-originalColors.get(i)))/ (255-tileColors.get(i)));
            }
        }
        return colorCorrector;
    }


    public static int calculateCorrectedRgb(List<Double> colorCorrector, int rgb) {
        ColorResult colorToCorrect = new ColorResult(rgb);
        List<Integer> colorsToCorrectRgb = colorToCorrect.getColors();
        List<Integer> correctedColors = new ArrayList<>();
        for(int i = 0; i< colorsToCorrectRgb.size(); i++){
            Double corrector = colorCorrector.get(i);
            Integer color = colorsToCorrectRgb.get(i);
            if (corrector == 0){
                correctedColors.add(color);
            } else if (corrector > 0){
                correctedColors.add((int) (color * corrector));
            } else if (corrector < 0){
                correctedColors.add ((int) (255 + ((255 - color)* corrector)));
            }
        }
        return getRgb(correctedColors.get(0), correctedColors.get(1), correctedColors.get(2));
    }
}
