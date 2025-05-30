package src.UI;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    public static ImageIcon convertSVGToImage(String svgFilePath,int height, int width) throws Exception {
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
        int currentWidth = image[0].getWidth();
        int currentHeight = image[0].getHeight();

        while (currentWidth != width && currentHeight != height) {
            currentWidth /= 2;
            currentHeight /= 2;
            if(currentHeight<height) {
                currentHeight = height;
            }
            if(currentWidth<width) {
                currentWidth = width;
            }
            BufferedImage tempImage = new BufferedImage(currentWidth, currentHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tempImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            g2d.drawImage(image[0], 0, 0, currentWidth, currentHeight, null);
            g2d.dispose();
            image[0] = tempImage;
        }
        return new ImageIcon(image[0]);
    }
}
