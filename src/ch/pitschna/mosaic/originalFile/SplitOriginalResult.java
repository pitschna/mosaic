package ch.pitschna.mosaic.originalfile;

import java.awt.image.BufferedImage;

public class SplitOriginalResult {

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

    public String getFileName() {
        return fileName;
    }

    public Integer getSizeOfTile() {
        return sizeOfTile;
    }

    public Integer getNumberOfHorizontalTiles() {
        return numberOfHorizontalTiles;
    }

    public Integer getNumberOfVerticalTiles() {
        return numberOfVerticalTiles;
    }

    public BufferedImage getImage() {
        return image;
    }
}
