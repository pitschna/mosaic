package ch.pitschna.mosaic.match;

import ch.pitschna.mosaic.common.*;
import ch.pitschna.mosaic.originalfile.SplitOriginalResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.pitschna.mosaic.common.BufferedImageUtil.bufferedImageReader;
import static ch.pitschna.mosaic.common.ColorCalculator.*;

public class MatchMaker {
    public static Map<Integer, String> match(SplitOriginalResult originalResult, String tilesFolder) {
        System.out.println("Start to match tiles: \n\t" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        Map<Integer, String> mosaicMap = new HashMap<>();

        Integer sizeOfTile = originalResult.getSizeOfTile();
        Integer numberOfHorizontalTiles = originalResult.getNumberOfHorizontalTiles();
        Integer numberOfVerticalTiles = originalResult.getNumberOfVerticalTiles();
        BufferedImage originalImage = originalResult.getImage();

        File dir = new File(tilesFolder);

        // loop over individual tile of original image
        for (int xTileNumber = 0; xTileNumber < numberOfHorizontalTiles; xTileNumber++) {
            for (int yTileNumber = 0; yTileNumber < numberOfVerticalTiles; yTileNumber++) {

                // loop over tile images
                double minimalRmsd = Double.MAX_VALUE;
                int originalFileStartX = xTileNumber * sizeOfTile;
                int originalFileStartY = yTileNumber * sizeOfTile;
                int tileNumber = yTileNumber * numberOfHorizontalTiles + xTileNumber;

                RgbColorResult originalColor = new RgbColorResult(calculateOne(originalImage, sizeOfTile, originalFileStartX, originalFileStartY));

                for (File file : dir.listFiles(JpgFilter.INSTANCE)) {
                    BufferedImage tile = bufferedImageReader(file.getAbsolutePath());

                    List<Double> colorCorrector = calculateColorCorrectorForTile(originalColor, file);

                    double rmsd = 0;
                    rmsd = calculateRmsdForTile(sizeOfTile, originalImage, originalFileStartX, originalFileStartY, tile, colorCorrector, rmsd);
                    if (minimalRmsd > rmsd) {
                        minimalRmsd = rmsd;
                        mosaicMap.put(tileNumber, file.getAbsolutePath());
                    }
                }
            }
        }


        return mosaicMap;
    }

    private static List<Double> calculateColorCorrectorForTile(RgbColorResult originalColor, File file) {
        RgbColorResult tileColor = new RgbColorResult(getColorFromFileName(file.getName()));
        return calculateColorCorrector(originalColor, tileColor);
    }

    private static double calculateRmsdForTile(Integer sizeOfTile, BufferedImage originalImage, int originalFileStartX, int originalFileStartY, BufferedImage tile, List<Double> colorCorrector, double rmsd) {
        for (int xTile = 0; xTile < sizeOfTile; xTile++) {
            for (int yTile = 0; yTile < sizeOfTile; yTile++) {
                RgbColorResult colorImage = getColorSinglePixel(originalImage, originalFileStartX + xTile,
                        originalFileStartY + yTile);
                RgbColorResult colorTile = getColorSinglePixel(tile, xTile, yTile, colorCorrector);
                rmsd += getSquareDeviation(colorImage, colorTile);
            }
        }
        return rmsd;
    }
}
