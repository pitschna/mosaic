package ch.pitschna.mosaic.stitch;

import ch.pitschna.mosaic.common.BufferedImageUtil;
import ch.pitschna.mosaic.common.RgbColorResult;
import ch.pitschna.mosaic.originalfile.SplitOriginalResult;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static ch.pitschna.mosaic.common.BufferedImageUtil.bufferdImageWriter;
import static ch.pitschna.mosaic.common.ColorCalculator.*;
import static ch.pitschna.mosaic.common.MosaicConstants.JPG;

public class MosaicStitcher {
    public static void stitch(SplitOriginalResult originalResult, Map<Integer, String> mosaicMap) {
        System.out.println("Start to stitch tiles: \n\t" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        Integer sizeOfTile = originalResult.getSizeOfTile();
        Integer numberOfHorizontalTiles = originalResult.getNumberOfHorizontalTiles();
        Integer numberOfVerticalTiles = originalResult.getNumberOfVerticalTiles();
        String originalFileName = originalResult.getFileName();
        BufferedImage originalImage = originalResult.getImage();
        BufferedImage mosaicImage = new BufferedImage(sizeOfTile * numberOfHorizontalTiles, sizeOfTile * numberOfVerticalTiles,
                originalImage.getType());

        stitchTilesTogether(mosaicMap, sizeOfTile, numberOfHorizontalTiles, numberOfVerticalTiles, originalImage, mosaicImage);
        saveMosaicImage(originalFileName, mosaicImage);
    }

    private static void stitchTilesTogether(Map<Integer, String> mosaicMap, Integer sizeOfTile, Integer numberOfHorizontalTiles, Integer numberOfVerticalTiles, BufferedImage originalImage, BufferedImage mosaicImage) {
        // loop over original image
        for (int xTileNumber = 0; xTileNumber < numberOfHorizontalTiles; xTileNumber++) {
            for (int yTileNumber = 0; yTileNumber < numberOfVerticalTiles; yTileNumber++) {
                // loop over individual tile
                String tileName = mosaicMap.get(yTileNumber * numberOfHorizontalTiles + xTileNumber);
                BufferedImage tile = BufferedImageUtil.bufferedImageReader(tileName);

                int originalFileStartX = xTileNumber * sizeOfTile;
                int originalFileStartY = yTileNumber * sizeOfTile;

                // correct the color
                List<Double> colorCorrector = calculateColorCorrectorForTile(sizeOfTile, originalImage, tileName,
                        originalFileStartX, originalFileStartY);
                setTile(sizeOfTile, mosaicImage, tile, originalFileStartX, originalFileStartY, colorCorrector);
            }
        }
    }

    private static void saveMosaicImage(String originalFileName, BufferedImage mosaicImage) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        bufferdImageWriter(mosaicImage, originalFileName.replace(JPG, timeStamp + JPG));
    }

    private static List<Double> calculateColorCorrectorForTile(Integer sizeOfTile, BufferedImage originalImage, String tileName, int originalFileStartX, int originalFileStartY) {
        RgbColorResult tileColor = new RgbColorResult(getColorFromFileName(tileName));
        RgbColorResult originalColor = new RgbColorResult(calculateOne(originalImage, sizeOfTile, originalFileStartX, originalFileStartY));

        return calculateColorCorrector(originalColor, tileColor);
    }

    private static void setTile(Integer sizeOfTile, BufferedImage mosaicImage, BufferedImage tile, int originalFileStartX, int originalFileStartY, List<Double> colorCorrector) {
        for (int xTile = 0; xTile < sizeOfTile; xTile++) {
            for (int yTile = 0; yTile < sizeOfTile; yTile++) {
                int correctedRgb = calculateCorrectedRgb(colorCorrector, tile.getRGB(xTile, yTile));
                mosaicImage.setRGB(originalFileStartX + xTile, originalFileStartY + yTile, correctedRgb);
            }
        }
    }


}
