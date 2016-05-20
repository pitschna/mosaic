package ch.pitschna.mosaic.match;

import ch.pitschna.mosaic.common.JpgFilter;
import ch.pitschna.mosaic.common.RgbColorResult;
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
        Map<Integer, Double> rmsdMap = new HashMap<>();
        Map<Integer, RgbColorResult> originalColorMap = new HashMap<>();


        Integer sizeOfTile = originalResult.getSizeOfTile();
        Integer numberOfHorizontalTiles = originalResult.getNumberOfHorizontalTiles();
        Integer numberOfVerticalTiles = originalResult.getNumberOfVerticalTiles();
        BufferedImage originalImage = originalResult.getImage();

        File dir = new File(tilesFolder);
        for (File file : dir.listFiles(JpgFilter.INSTANCE)) {
            BufferedImage tile = bufferedImageReader(file.getAbsolutePath());
            // loop over individual tile of original image
            for (int xTileNumber = 0; xTileNumber < numberOfHorizontalTiles; xTileNumber++) {
                for (int yTileNumber = 0; yTileNumber < numberOfVerticalTiles; yTileNumber++) {

                    // loop over tile images
                    int originalFileStartX = xTileNumber * sizeOfTile;
                    int originalFileStartY = yTileNumber * sizeOfTile;
                    int tileNumber = yTileNumber * numberOfHorizontalTiles + xTileNumber;
                    RgbColorResult originalColor = getOriginalColor(originalColorMap, sizeOfTile,
                            originalImage, originalFileStartX, originalFileStartY, tileNumber);

                    List<Double> colorCorrector = calculateColorCorrectorForTile(originalColor, file);

                    double rmsd = SumRmsd.calculateRmsd(originalImage, tile, originalFileStartX, originalFileStartY, sizeOfTile, colorCorrector);
                    if (rmsdMap.get(tileNumber) == null || rmsdMap.get(tileNumber) > rmsd) {
                        rmsdMap.put(tileNumber, rmsd);
                        mosaicMap.put(tileNumber, file.getAbsolutePath());
                    }
                }
            }
        }


        return mosaicMap;
    }

    private static RgbColorResult getOriginalColor(Map<Integer, RgbColorResult> originalColorMap, Integer sizeOfTile, BufferedImage originalImage, int originalFileStartX, int originalFileStartY, int tileNumber) {
        if (originalColorMap.get(tileNumber) != null) {
            return originalColorMap.get(tileNumber);
        }
        originalColorMap.put(tileNumber, new RgbColorResult(calculateOne(originalImage, sizeOfTile, originalFileStartX, originalFileStartY)));
        return originalColorMap.get(tileNumber);
    }

    private static List<Double> calculateColorCorrectorForTile(RgbColorResult originalColor, File file) {
        RgbColorResult tileColor = new RgbColorResult(getColorFromFileName(file.getName()));
        return calculateColorCorrector(originalColor, tileColor);
    }


}
