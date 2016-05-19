package ch.pitschna.mosaic.stitch;

import ch.pitschna.mosaic.common.ColorCalculator;
import ch.pitschna.mosaic.common.BufferedImageUtil;
import ch.pitschna.mosaic.common.ColorResult;
import ch.pitschna.mosaic.common.MosaicConstants;
import ch.pitschna.mosaic.originalfile.SplitOriginalResult;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static ch.pitschna.mosaic.common.BufferedImageUtil.bufferdImageWriter;
import static ch.pitschna.mosaic.common.ColorCalculator.*;

public class MosaicStitcher {
    public static void stitch(SplitOriginalResult originalResult, Map<Integer, String> mosaicMap) {

        Integer sizeOfTile = originalResult.getSizeOfTile();
        Integer numberOfHorizontalTiles = originalResult.getNumberOfHorizontalTiles();
        Integer numberOfVerticalTiles = originalResult.getNumberOfVerticalTiles();
        String originalFileName = originalResult.getFileName();
        BufferedImage originalImage = originalResult.getImage();
        BufferedImage mosaicImage = new BufferedImage(sizeOfTile * numberOfHorizontalTiles, sizeOfTile * numberOfVerticalTiles,
                originalImage.getType());


        // loop over original image
        for (int xTileNumber = 0; xTileNumber < numberOfHorizontalTiles; xTileNumber++) {
            for (int yTileNumber = 0; yTileNumber < numberOfVerticalTiles; yTileNumber++) {
                // loop over individual tile
                String tileName = mosaicMap.get(yTileNumber * numberOfHorizontalTiles + xTileNumber);
                BufferedImage tile = BufferedImageUtil.bufferedImageReader(tileName);

                int originalFileStartX = xTileNumber * sizeOfTile;
                int originalFileStartY = yTileNumber * sizeOfTile;

                // correct the color
                ColorResult tileColor = new ColorResult(getColorFromFileName(tileName));
                ColorResult originalColor = new ColorResult(calculateOne(originalImage, sizeOfTile, originalFileStartX, originalFileStartY));

                List<Double> colorCorrector = calculateColorCorrector(originalColor, tileColor);

                for (int xTile = 0; xTile < sizeOfTile; xTile++) {
                    for (int yTile = 0; yTile < sizeOfTile; yTile++) {
                        int correctedRgb = calculateCorrectedRgb(colorCorrector, tile.getRGB(xTile, yTile));
                        mosaicImage.setRGB(originalFileStartX + xTile, originalFileStartY + yTile, correctedRgb);
                    }
                }

            }
        }
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        bufferdImageWriter(mosaicImage, originalFileName.replace(".", timeStamp + "."));
    }



}
