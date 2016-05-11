package ch.pitschna.mosaic.common;

import java.io.File;
import java.io.FilenameFilter;

public enum JpgFilter implements FilenameFilter {
    INSTANCE;

    private static final String JPG = ".jpg";

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(JPG);
    }
}