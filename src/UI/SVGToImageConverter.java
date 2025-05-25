package src.UI;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;

public class SVGToImageConverter {
    public static BufferedImage convertSVGToImage(String svgFilePath) throws Exception {
        InputStream inputStream = new FileInputStream(svgFilePath);
        TranscoderInput input = new TranscoderInput(inputStream);

        final BufferedImage[] image = new BufferedImage[1];

        ImageTranscoder transcoder = new ImageTranscoder() {
            @Override
            public BufferedImage createImage(int width, int height) {
                return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            }

            @Override
            public void writeImage(BufferedImage img, TranscoderOutput output) {
                image[0] = img;
            }
        };

        transcoder.transcode(input, null);
        inputStream.close();

        return image[0];
    }

}
