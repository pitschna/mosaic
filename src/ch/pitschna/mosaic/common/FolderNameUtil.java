package ch.pitschna.mosaic.common;


public final class FolderNameUtil {

    private static final String TILES = "\\tiles\\";

    static public String getFolderName(String folderName){
        return folderName + TILES;
    }
}
