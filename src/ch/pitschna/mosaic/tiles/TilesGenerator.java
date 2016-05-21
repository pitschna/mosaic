package ch.pitschna.mosaic.tiles;

import ch.pitschna.mosaic.common.FolderNameUtil;
import ch.pitschna.mosaic.common.JpgFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ch.pitschna.mosaic.common.BufferedImageUtil.bufferdImageWriter;
import static ch.pitschna.mosaic.common.BufferedImageUtil.bufferedImageReader;
import static ch.pitschna.mosaic.common.ColorCalculator.calculateOne;
import static ch.pitschna.mosaic.common.MosaicConstants.*;

public final class TilesGenerator {

    public static void generate(String folderName, Integer size) {
        System.out.println("Start to generate tiles: \n\t" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
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
            BufferedImage image = bufferedImageReader(file.getAbsolutePath());
            int height = image.getHeight();
            int width = image.getWidth();

            for (double div = 1; div < 2.5; div *= Math.sqrt(2D)) {
                int divisor = (int) Math.ceil(Math.min(height, width) / (size * div));
                if (divisor < 1) {
                    continue;
                }

                int maxAddedPixels = size * (divisor + 1);
                int shift = size * divisor / 5;

                int range = divisor > 10 ? 10 : divisor;
                for (int startX = 0; startX + maxAddedPixels < width; startX += shift) {
                    for (int startY = 0; startY + maxAddedPixels < height; startY += shift) {

                        BufferedImage tile = new BufferedImage(size, size, image.getType());
                        for (int xTile = 0; xTile < size; xTile++) {
                            for (int yTile = 0; yTile < size; yTile++) {
                                int averageRgbOnePixel = calculateOne(image, range, startX + divisor * xTile, startY + divisor * yTile);
                                tile.setRGB(xTile, yTile, averageRgbOnePixel);
                            }
                        }
                        int averageRgbTile = calculateOne(tile, size, 0, 0);
                        bufferdImageWriter(tile,
                                tilesFolder + fileName++ + FILE_NAME_SEPARATOR_START + averageRgbTile + FILE_NAME_SEPARATOR_STOP + JPG);
                    }
                }
            }
        }
    }

}
