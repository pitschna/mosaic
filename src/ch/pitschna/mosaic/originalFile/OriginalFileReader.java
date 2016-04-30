package ch.pitschna.mosaic.originalfile;

import ch.pitschna.mosaic.common.BufferedImageUtil;

import java.awt.image.BufferedImage;

public final class OriginalFileReader {
    public static SplitOriginalResult read(String fileName, Integer numberOfHorizontalTiles) {
        BufferedImage image = BufferedImageUtil.bufferedImageReader(fileName);

        Integer sizeOfTile = image.getWidth()/numberOfHorizontalTiles;
        Integer numberOfVerticalTiles = image.getHeight()/sizeOfTile;

        System.out.println("Size of tile: " + sizeOfTile);
        System.out.println("Number of vertical tiles: " + numberOfVerticalTiles);

        return new SplitOriginalResult(fileName, image, sizeOfTile, numberOfHorizontalTiles, numberOfVerticalTiles);
    }
}
