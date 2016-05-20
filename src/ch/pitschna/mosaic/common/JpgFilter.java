package ch.pitschna.mosaic.common;

import java.io.File;
import java.io.FilenameFilter;

import static ch.pitschna.mosaic.common.MosaicConstants.JPG;

public enum JpgFilter implements FilenameFilter {
    INSTANCE;

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(JPG);
    }
}