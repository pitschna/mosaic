package ch.pitschna.mosaic.tiles;

import ch.pitschna.mosaic.common.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;

public final class TilesGenerator {

    private static final String JPG = ".jpg";

    public static void generate(String folderName, Integer size) {

        String tilesFolder = FolderNameUtil.getFolderName(folderName);
        new File(tilesFolder).mkdir();

        File dir = new File(folderName);
        int fileName = 0;

        for (File file : dir.listFiles(JpgFilter.INSTANCE)) {
            BufferedImage image = BufferedImageUtil.bufferedImageReader(file.getAbsolutePath());
            System.out.println("File name: " + file.getName());

            int divisor = Math.min(image.getHeight(), image.getWidth()) / size;

            BufferedImage tile = new BufferedImage(size, size, image.getType());
            for (int xTile = 0; xTile < size; xTile++) {
                for (int yTile = 0; yTile < size; yTile++) {
                    int pixelAverage = AverageColorCalculator.calculateOne(image, divisor, divisor * xTile, divisor * yTile);
                    tile.setRGB(xTile, yTile, pixelAverage);
                }
            }

            AverageColorResult averageColor = AverageColorCalculator.calculateAll(tile);
            BufferedImageUtil.bufferdImageWriter(tile, tilesFolder + averageColor.getName() + fileName++ + JPG);
        }

    }

}
