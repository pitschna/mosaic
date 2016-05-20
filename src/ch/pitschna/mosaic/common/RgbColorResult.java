package ch.pitschna.mosaic.common;


import java.util.Arrays;
import java.util.List;

public class RgbColorResult {
    private final List<Integer> colors;

    public RgbColorResult(int rgb){
        colors = Arrays.asList((rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF, (rgb & 0xFF) );
    }

    List<Integer> getColors() {
        return colors;
    }

}
