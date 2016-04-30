package ch.pitschna.mosaic;

import ch.pitschna.mosaic.match.MatchMaker;
import ch.pitschna.mosaic.originalfile.OriginalFileReader;
import ch.pitschna.mosaic.originalfile.SplitOriginalResult;
import ch.pitschna.mosaic.tiles.TilesGenerator;

public class MosaicGenerator {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("run like this:");
            System.out.println("java MosaicGenerator <name of origninal picture> <number of horizontal tiles> " +
                    "<folder with tiles pictures>");
            return;
        }
        String fileName = args[0];
        Integer numberOfHorizontalTiles = Integer.valueOf(args[1]);
        String tilesFolder = args[2];

        System.out.println("Picture: " + fileName);
        System.out.println("Number of horizontal tiles: " + numberOfHorizontalTiles);
        System.out.println("Folder with tiles pictures: " + tilesFolder);

        // read in original file
        SplitOriginalResult original = OriginalFileReader.read(fileName, numberOfHorizontalTiles);
        // generate tiles
        TilesGenerator.generate(tilesFolder, original.getSizeOfTile());
        // find tile most similar
        MatchMaker.match(original.getImage(), original.getSizeOfTile());
        // put image together
    }
}
