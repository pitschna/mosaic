package ch.pitschna.mosaic.stitch;

import ch.pitschna.mosaic.common.AverageColorCalculator;
import ch.pitschna.mosaic.common.BufferedImageUtil;
import ch.pitschna.mosaic.common.ColorResult;
import ch.pitschna.mosaic.common.JpgFilter;
import ch.pitschna.mosaic.originalfile.SplitOriginalResult;

import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MosaicStitcher {
    public static void stitch(SplitOriginalResult originalResult, Map<Integer, String> mosaicMap) {

        Integer sizeOfTile = originalResult.getSizeOfTile();
        Integer numberOfHorizontalTiles = originalResult.getNumberOfHorizontalTiles();
        Integer numberOfVerticalTiles = originalResult.getNumberOfVerticalTiles();
        String originalFileName = originalResult.getFileName();
        BufferedImage originalImage = originalResult.getImage();
        BufferedImage mosaicImage = new BufferedImage(sizeOfTile * numberOfHorizontalTiles, sizeOfTile * numberOfVerticalTiles, originalImage.getType());


        // loop over original image
        for (int xTileNumber = 0; xTileNumber < numberOfHorizontalTiles; xTileNumber++) {
            for (int yTileNumber = 0; yTileNumber < numberOfVerticalTiles; yTileNumber++) {
                // loop over individual tile
                String tileName = mosaicMap.get(yTileNumber * numberOfHorizontalTiles + xTileNumber);
                BufferedImage tile = BufferedImageUtil.bufferedImageReader(tileName);

                // correct the color
                ColorResult tileColor = new ColorResult(AverageColorCalculator.calculateOne(tile, sizeOfTile, 0, 0));
                ColorResult originalColor = new ColorResult(AverageColorCalculator.calculateOne(originalImage, sizeOfTile, xTileNumber * sizeOfTile, yTileNumber * sizeOfTile));

                List<Double> colorCorrector = AverageColorCalculator.calculateColorCorrector(originalColor, tileColor);

                for (int xTile = 0; xTile < sizeOfTile; xTile++) {
                    for (int yTile = 0; yTile < sizeOfTile; yTile++) {
                        int correctedRgb = AverageColorCalculator.calculateCorrectedRgb(colorCorrector, tile.getRGB(xTile, yTile));
                        mosaicImage.setRGB(xTileNumber * sizeOfTile + xTile, yTileNumber * sizeOfTile + yTile, correctedRgb);
                    }
                }

            }
        }
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        BufferedImageUtil.bufferdImageWriter(mosaicImage, originalFileName.replace(".", timeStamp + "."));
    }



}
