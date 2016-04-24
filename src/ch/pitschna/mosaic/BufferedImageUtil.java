package ch.pitschna.mosaic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


final class BufferedImageUtil {

    static BufferedImage bufferedImageReader(String fileName){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return image;
    }

    static void bufferdImageWriter(BufferedImage image, String fileName){
        try {
            ImageIO.write(image, "jpg", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
