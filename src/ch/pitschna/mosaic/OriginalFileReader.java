package ch.pitschna.mosaic;

import java.awt.image.BufferedImage;

final class OriginalFileReader {
    static SplitOriginalResult read(String fileName, Integer numberOfHorizontalTiles) {
        BufferedImage image = BufferedImageUtil.bufferedImageReader(fileName);

        Integer sizeOfTile = image.getWidth()/numberOfHorizontalTiles;
        Integer numberOfVerticalTiles = image.getHeight()/sizeOfTile;

        System.out.println("Size of tile: " + sizeOfTile);
        System.out.println("Number of vertical tiles: " + numberOfVerticalTiles);

        return new SplitOriginalResult(fileName, image, sizeOfTile, numberOfHorizontalTiles, numberOfVerticalTiles);
    }
}
