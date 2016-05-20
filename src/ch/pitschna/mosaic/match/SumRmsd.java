package ch.pitschna.mosaic.match;

import ch.pitschna.mosaic.common.RgbColorResult;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static ch.pitschna.mosaic.common.ColorCalculator.getColorSinglePixel;
import static ch.pitschna.mosaic.common.ColorCalculator.getSquareDeviation;

class SumRmsd extends RecursiveTask<Double> {

    private final BufferedImage originalImage;
    private final BufferedImage tile;
    private final int originalFileStartX;
    private final int sizeOfTile;
    private final int originalFileStartY;
    private final List<Double> colorCorrector;
    private int low;
    private int high;


    private SumRmsd(BufferedImage originalImage, BufferedImage tile, int originalFileStartX, int sizeOfTile,
                    int originalFileStartY, List<Double> colorCorrector, int low, int high) {
        this.originalImage = originalImage;
        this.tile = tile;
        this.originalFileStartX = originalFileStartX;
        this.sizeOfTile = sizeOfTile;
        this.originalFileStartY = originalFileStartY;
        this.colorCorrector = colorCorrector;
        this.low = low;
        this.high = high;
    }

    static Double calculateRmsd(BufferedImage originalImage, BufferedImage tile, int originalFileStartX,
                                int originalFileStartY, int sizeOfTile, List<Double> colorCorrector) {
        return ForkJoinPool.commonPool().invoke(new SumRmsd(originalImage, tile, originalFileStartX, sizeOfTile,
                originalFileStartY, colorCorrector, 0, sizeOfTile - 1));
    }

    protected Double compute() {
        if (high - low < 1) {
            double rmsd = 0;
            for (int yTile = 0; yTile < sizeOfTile; yTile++) {
                RgbColorResult colorImage = getColorSinglePixel(originalImage, originalFileStartX + low,
                        originalFileStartY + yTile);
                RgbColorResult colorTile = getColorSinglePixel(tile, low, yTile, colorCorrector);
                rmsd += getSquareDeviation(colorImage, colorTile);
            }
            return rmsd;
        } else {
            int mid = low + (high - low) / 2;
            SumRmsd left = new SumRmsd(originalImage, tile, originalFileStartX, sizeOfTile,
                    originalFileStartY, colorCorrector, low, mid);
            SumRmsd right = new SumRmsd(originalImage, tile, originalFileStartX, sizeOfTile,
                    originalFileStartY, colorCorrector, mid + 1, high);
            left.fork();
            Double rightAns = right.compute();
            Double leftAns = left.join();
            return leftAns + rightAns;
        }
    }
}
