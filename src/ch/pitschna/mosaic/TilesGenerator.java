package ch.pitschna.mosaic;

import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;

final class TilesGenerator {

    private static final String JPG = ".jpg";
    private static final String TILES = "\\tiles\\";

    static void generate(String folderName, Integer size) {
        String tilesFolder = folderName + TILES;
        new File(tilesFolder).mkdir();

        File dir = new File(folderName);
        int fileName = 0;

        for (File file : dir.listFiles(JpgFilter.INSTANCE)) {
            BufferedImage image = BufferedImageUtil.bufferedImageReader(file.getAbsolutePath());

            int divisor = Math.min(image.getHeight(), image.getWidth()) / size;
            System.out.println("Image type: " + image.getType());
            BufferedImage tile = new BufferedImage(size, size, image.getType());
            for (int xTile = 0; xTile < size; xTile++) {
                for (int yTile = 0; yTile < size; yTile++) {
                    int pixelAverage = AverageColorCalculator.calculateOne(image, divisor, divisor * xTile, divisor *yTile);
                    tile.setRGB(xTile, yTile,pixelAverage);
                }
            }

            AverageColorResult averageColor = AverageColorCalculator.calculateAll(tile);

            System.out.println("File name: " + file.getName());
            BufferedImageUtil.bufferdImageWriter(tile, tilesFolder + fileName++ + JPG);
        }

    }


    private enum JpgFilter implements FilenameFilter {
        INSTANCE;

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(JPG);
        }
    }
}
