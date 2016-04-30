package ch.pitschna.mosaic.common;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AverageColorResult
{
    private final List<ColorResult> colorResults;

    public AverageColorResult(ColorResult color1, ColorResult color2, ColorResult color3, ColorResult color4){
        colorResults = Arrays.asList(color1, color2, color3, color4);
    }

    public String getName() {
        return colorResults
                .stream()
                .map(i -> i.getColors()
                        .stream()
                        .map(j -> String.format("%03d", j))
                        .collect(Collectors.joining("-", "", "")))
                .collect(Collectors.joining("_", "", ""));
    }


}
