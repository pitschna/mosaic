package ch.pitschna.mosaic.match;

import ch.pitschna.mosaic.common.AverageColorCalculator;
import ch.pitschna.mosaic.common.BufferedImageUtil;
import ch.pitschna.mosaic.common.ColorResult;
import ch.pitschna.mosaic.common.JpgFilter;
import ch.pitschna.mosaic.originalfile.SplitOriginalResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchMaker {
    public static Map<Integer, String> match(SplitOriginalResult originalResult, String tilesFolder) {
        Map<Integer, String> mosaicMap = new HashMap<>();

        Integer sizeOfTile = originalResult.getSizeOfTile();
        Integer numberOfHorizontalTiles = originalResult.getNumberOfHorizontalTiles();
        Integer numberOfVerticalTiles = originalResult.getNumberOfVerticalTiles();
        BufferedImage originalImage = originalResult.getImage();

        File dir = new File(tilesFolder);

        // loop over original image
        for (int xTileNumber = 0; xTileNumber < numberOfHorizontalTiles; xTileNumber++) {
            for (int yTileNumber = 0; yTileNumber < numberOfVerticalTiles; yTileNumber++) {
                // loop over individual tile of original image
                // loop over tile images
                double minimalRmsd = Double.MAX_VALUE;
                ColorResult originalColor = new ColorResult(AverageColorCalculator.calculateOne(originalImage, sizeOfTile, xTileNumber * sizeOfTile, yTileNumber * sizeOfTile));

                for (File file : dir.listFiles(JpgFilter.INSTANCE)) {
                    BufferedImage tile = BufferedImageUtil.bufferedImageReader(file.getAbsolutePath());

                    ColorResult tileColor = new ColorResult(AverageColorCalculator.calculateOne(tile, sizeOfTile, 0, 0));
                    List<Double> colorCorrector = AverageColorCalculator.calculateColorCorrector(originalColor, tileColor);

                    double rmsd = 0;
                    for (int xTile = 0; xTile < sizeOfTile; xTile++) {
                        for (int yTile = 0; yTile < sizeOfTile; yTile++) {
                            ColorResult colorImage = AverageColorCalculator.getColorSinglePixel(originalImage, xTileNumber * sizeOfTile + xTile, yTileNumber * sizeOfTile + yTile);
                            ColorResult colorTile = AverageColorCalculator.getColorSinglePixel(tile, xTile, yTile, colorCorrector);
                            rmsd += AverageColorCalculator.getRootSquareDeviation(colorImage, colorTile);
                        }
                    }
                    if (minimalRmsd > rmsd) {
                        minimalRmsd = rmsd;
                        mosaicMap.put(yTileNumber * numberOfHorizontalTiles + xTileNumber, file.getAbsolutePath());
                    }
                }
            }
        }


        return mosaicMap;
    }
}
