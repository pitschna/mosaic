package ch.pitschna.mosaic.match;

import ch.pitschna.mosaic.common.*;
import ch.pitschna.mosaic.originalfile.SplitOriginalResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.pitschna.mosaic.common.BufferedImageUtil.bufferedImageReader;
import static ch.pitschna.mosaic.common.ColorCalculator.*;

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
                int originalFileStartX = xTileNumber * sizeOfTile;
                int originalFileStartY = yTileNumber * sizeOfTile;

                ColorResult originalColor = new ColorResult(calculateOne(originalImage, sizeOfTile, originalFileStartX, originalFileStartY));

                for (File file : dir.listFiles(JpgFilter.INSTANCE)) {
                    BufferedImage tile = bufferedImageReader(file.getAbsolutePath());

                    ColorResult tileColor = new ColorResult(getColorFromFileName(file.getName()));
                    List<Double> colorCorrector = calculateColorCorrector(originalColor, tileColor);

                    double rmsd = 0;
                    for (int xTile = 0; xTile < sizeOfTile; xTile++) {
                        for (int yTile = 0; yTile < sizeOfTile; yTile++) {
                            ColorResult colorImage = getColorSinglePixel(originalImage, originalFileStartX + xTile,
                                    originalFileStartY + yTile);
                            ColorResult colorTile = getColorSinglePixel(tile, xTile, yTile, colorCorrector);
                            rmsd += getSquareDeviation(colorImage, colorTile);
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
