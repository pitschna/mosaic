package ch.pitschna.mosaic.tiles;

import ch.pitschna.mosaic.common.*;

import java.awt.image.BufferedImage;
import java.io.File;

import static ch.pitschna.mosaic.common.MosaicConstants.*;

public final class TilesGenerator {

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
                if (divisor < 1) {
                    continue;
                }

                for (int startX = 0; startX + size * (divisor + 1) < image.getWidth(); startX += size * divisor / 3) {
                    for (int startY = 0; startY + size * (divisor + 1) < image.getHeight(); startY += size * divisor / 3) {

                        BufferedImage tile = new BufferedImage(size, size, image.getType());
                        for (int xTile = 0; xTile < size; xTile++) {
                            for (int yTile = 0; yTile < size; yTile++) {
                                int averageRgbOnePixel = ColorCalculator.calculateOne(image, divisor,
                                        startX + divisor * xTile, startY + divisor * yTile);
                                tile.setRGB(xTile, yTile, averageRgbOnePixel);
                            }
                        }
                        int averageRgbTile = ColorCalculator.calculateOne(tile, size, 0, 0);
                        BufferedImageUtil.bufferdImageWriter(tile,
                                tilesFolder + fileName++ + FILE_NAME_SEPARATOR_START + averageRgbTile + FILE_NAME_SEPARATOR_STOP + JPG);
                    }
                }
            }
        }
    }

}
