package ch.pitschna.mosaic.common;


import java.util.Arrays;
import java.util.List;

public class ColorResult {
    private final List<Integer> colors;

    public ColorResult(int rgb){
        colors = Arrays.asList((rgb >> 16) & 0xFF,(rgb >> 8) & 0xFF, (rgb & 0xFF) );
    }

    public List<Integer> getColors() {
        return colors;
    }

}
