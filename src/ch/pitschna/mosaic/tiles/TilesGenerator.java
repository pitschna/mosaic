package ch.pitschna.mosaic.tiles;

import ch.pitschna.mosaic.common.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;

public final class TilesGenerator {

    private static final String JPG = ".jpg";

    public static void generate(String folderName, Integer size) {

        String tilesFolder = FolderNameUtil.getFolderName(folderName);

        if (!new File(tilesFolder).exists()) {
            if (!new File(tilesFolder).mkdir()) {
                System.err.println("Could not create directory " + tilesFolder + ".");
                System.exit(1);
            }
        }

        File dir = new File(folderName);
        int fileName = 0;

        for (File file : dir.listFiles(JpgFilter.INSTANCE)) {
            BufferedImage image = BufferedImageUtil.bufferedImageReader(file.getAbsolutePath());

            for (double div = 1; div < 2.5; div *= Math.sqrt(2D)) {
                int divisor = (int) Math.ceil(Math.min(image.getHeight(), image.getWidth()) / (size * div));

                for (int startX = 0; startX + size * (divisor + 1) < image.getWidth(); startX += size * divisor / 3) {
                    for (int startY = 0; startY + size * (divisor + 1) < image.getHeight(); startY += size * divisor / 3) {

                        BufferedImage tile = new BufferedImage(size, size, image.getType());
                        for (int xTile = 0; xTile < size; xTile++) {
                            for (int yTile = 0; yTile < size; yTile++) {
                                int averageRgb = AverageColorCalculator.calculateOne(image, divisor,
                                        startX + divisor * xTile, startY + divisor * yTile);
                                tile.setRGB(xTile, yTile, averageRgb);
                            }
                        }

                        BufferedImageUtil.bufferdImageWriter(tile, tilesFolder + fileName++ + JPG);
                    }
                }
            }
        }
    }

}
