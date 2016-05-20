package ch.pitschna.mosaic.common;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static ch.pitschna.mosaic.common.MosaicConstants.FILE_NAME_SEPARATOR_START;
import static ch.pitschna.mosaic.common.MosaicConstants.FILE_NAME_SEPARATOR_STOP;
import static ch.pitschna.mosaic.common.MosaicConstants.MAX_COLOR;

public final class ColorCalculator {

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
        int rangeSqare = range * range;
        return getRgb(sumR / rangeSqare, sumG / rangeSqare, sumB / rangeSqare);
    }

    public static RgbColorResult getColorSinglePixel(BufferedImage image, int startX, int startY) {
        return new RgbColorResult(calculateOne(image, 1, startX, startY));
    }

    public static RgbColorResult getColorSinglePixel(BufferedImage image, int startX, int startY, List<Double> colorCorrector) {
        return new RgbColorResult(calculateCorrectedRgb(colorCorrector, calculateOne(image, 1, startX, startY)));
    }

    private static int getRgb(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }

    public static double getSquareDeviation(RgbColorResult colorImage, RgbColorResult colorTile) {
        int squareDeviation = 0;
        List<Integer> colorsImage = colorImage.getColors();
        List<Integer> colorsTile = colorTile.getColors();
        for (int i = 0; i < colorsImage.size(); i++) {
            squareDeviation += Math.abs(colorsImage.get(i) - colorsTile.get(i));
        }
        return squareDeviation;
    }

    public static List<Double> calculateColorCorrector(RgbColorResult originalColor, RgbColorResult tileColor) {
        ArrayList<Double> colorCorrector = new ArrayList<>();

        List<Integer> originalColors = originalColor.getColors();
        List<Integer> tileColors = tileColor.getColors();

        for (int i = 0; i < originalColors.size(); i++) {
            int sign = originalColors.get(i) - tileColors.get(i);
            if (sign == 0) {
                colorCorrector.add(1D);
            } else if (sign < 0) {
                colorCorrector.add(((double) originalColors.get(i)) / tileColors.get(i));
            } else {
                colorCorrector.add(-((double) (MAX_COLOR - originalColors.get(i))) / (MAX_COLOR - tileColors.get(i)));
            }
        }
        return colorCorrector;
    }


    public static int calculateCorrectedRgb(List<Double> colorCorrector, int rgb) {
        RgbColorResult colorToCorrect = new RgbColorResult(rgb);
        List<Integer> colorsToCorrectRgb = colorToCorrect.getColors();

        List<Integer> correctedColors = new ArrayList<>();

        for (int i = 0; i < colorsToCorrectRgb.size(); i++) {
            Double corrector = colorCorrector.get(i);
            Integer color = colorsToCorrectRgb.get(i);

            if (corrector > 0) {
                correctedColors.add((int) (color * corrector));
            } else {
                correctedColors.add((int) (MAX_COLOR + ((MAX_COLOR - color) * corrector)));
            }
        }
        return getRgb(correctedColors.get(0), correctedColors.get(1), correctedColors.get(2));
    }

    public static int getColorFromFileName(String fileName) {
        String tileColorRgb = fileName.substring(fileName.indexOf(FILE_NAME_SEPARATOR_START) + 3, fileName.indexOf(FILE_NAME_SEPARATOR_STOP));
        return Integer.valueOf(tileColorRgb);
    }

}
