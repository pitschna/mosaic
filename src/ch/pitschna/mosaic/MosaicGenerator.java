package ch.pitschna.mosaic;

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
        // put image together
    }
}
