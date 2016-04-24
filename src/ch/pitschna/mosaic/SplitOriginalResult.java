package ch.pitschna.mosaic;

import java.awt.image.BufferedImage;

 class SplitOriginalResult {

    private final String fileName;
    private final BufferedImage image;
    private final Integer sizeOfTile;
    private final Integer numberOfHorizontalTiles;
    private final Integer numberOfVerticalTiles;

    SplitOriginalResult(String fileName,
                               BufferedImage image,
                               Integer sizeOfTile,
                               Integer numberOfHorizontalTiles,
                               Integer numberOfVerticalTiles) {
        this.fileName = fileName;
        this.image = image;
        this.sizeOfTile = sizeOfTile;
        this.numberOfHorizontalTiles = numberOfHorizontalTiles;
        this.numberOfVerticalTiles = numberOfVerticalTiles;
    }

    String getFileName() {
        return fileName;
    }

    Integer getSizeOfTile() {
        return sizeOfTile;
    }

    Integer getNumberOfHorizontalTiles() {
        return numberOfHorizontalTiles;
    }

    Integer getNumberOfVerticalTiles() {
        return numberOfVerticalTiles;
    }

}
