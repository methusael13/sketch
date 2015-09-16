package core.modules;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayDeque;

public class FloodFill {

    protected BufferedImage image = null;
    protected int[] tolerance = new int[] {20,20,20};

    protected int width;
    protected int height;
    protected int[] pixels;

    protected int[] startColorA = new int[] {0,0,0};
    protected int fillColor;
    protected float alpha;
    protected boolean[] pixelsCompleted;
    
    protected int newR;
    protected int newG;
    protected int newB;
    
    private int oldR;
    private int oldG;
    private int oldB;

    protected ArrayDeque<BufferRange> ranges;

    public FloodFill(BufferedImage image, Color c) {
        initializeImage(image);
        alpha = c.getAlpha() / 255f;
        
        fillColor = 0xff000000 | c.getRGB();
        newR = (fillColor >> 16) & 0xff;
        newG = (fillColor >> 8) & 0xff;
        newB = fillColor & 0xff;
    }

    public final void initializeImage(BufferedImage img) {
        image = img;
        width = image.getWidth();
        height = image.getHeight();
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }

    public void startUp() {
        ranges = new ArrayDeque<>();
        pixelsCompleted = new boolean[pixels.length];
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage img) {
        initializeImage(img);
    }

    public void setTolerance(int[] tolerance) {
        this.tolerance = tolerance;
    }

    public void floodFill(int x, int y) {
        startUp();

        int initPixel = pixels[(width * y) + x];
        // Get the red value
        startColorA[0] = (initPixel >> 16) & 0xff;
        // Get the green value
        startColorA[1] = (initPixel >> 8) & 0xff;
        // Get the blue value
        startColorA[2] = initPixel & 0xff;

        fillRange(x, y);

        BufferRange range;
        while (ranges.size() > 0) {

            range = ranges.remove();

            int upPxLoc = width * (range.Y - 1) + range.leftX;
            int downPxLoc = width * (range.Y + 1) + range.leftX;

            for (int i = range.leftX; i <= range.rightX; i++) {
                if (range.Y > 0 && !pixelsCompleted[upPxLoc] && checkTolerance(upPxLoc)) {
                    fillRange(i, range.Y - 1);
                }

                if (range.Y < (height - 1) && !pixelsCompleted[downPxLoc] && checkTolerance(downPxLoc)) {
                    fillRange(i, range.Y + 1);
                }

                downPxLoc++;
                upPxLoc++;
            }
        }
    }

    public void fillRange(int x, int y) {
        int fillL = x;
        int pxId = width * y + x;
        
        while (true) {
            oldR = (pixels[pxId] >> 16) & 0xff;
            oldG = (pixels[pxId] >> 8) & 0xff;
            oldB = pixels[pxId] & 0xff;
            
            oldR = (int)(alpha * newR + (1 - alpha) * oldR);
            oldG = (int)(alpha * newG + (1 - alpha) * oldG);
            oldB = (int)(alpha * newB + (1 - alpha) * oldB);
            
            pixels[pxId] = 0xff000000 | (oldR << 16) | (oldG << 8) | oldB;
            pixelsCompleted[pxId] = true;

            fillL--;
            pxId--;
            if (fillL < 0 || pixelsCompleted[pxId] || !checkTolerance(pxId))
                break;
        }
        fillL++;

        int fillR = x;
        pxId = (width * y) + x;
        while (true) {
            oldR = (pixels[pxId] >> 16) & 0xff;
            oldG = (pixels[pxId] >> 8) & 0xff;
            oldB = pixels[pxId] & 0xff;
            
            oldR = (int)(alpha * newR + (1 - alpha) * oldR);
            oldG = (int)(alpha * newG + (1 - alpha) * oldG);
            oldB = (int)(alpha * newB + (1 - alpha) * oldB);
            
            pixels[pxId] = 0xff000000 | (oldR << 16) | (oldG << 8) | oldB;
            pixelsCompleted[pxId] = true;

            fillR++;
            pxId++;
            if (fillR >= width || pixelsCompleted[pxId] || !checkTolerance(pxId))
                break;
        }
        fillR--;

        BufferRange br = new BufferRange(fillL, fillR, y);
        ranges.offer(br);
    }

    public boolean checkTolerance(int pixel) {
        int red = (pixels[pixel] >> 16) & 0xff;
        int green = (pixels[pixel] >> 8) & 0xff;
        int blue = pixels[pixel] & 0xff;

        return ((startColorA[0] - red) <= tolerance[0] && (red - startColorA[0]) <= tolerance[0] &&
                (startColorA[1] - green) <= tolerance[1] && (green - startColorA[1]) <= tolerance[1] &&
                (startColorA[2] - blue) <= tolerance[2] && (blue - startColorA[2]) <= tolerance[2]);
    }

    protected class BufferRange {
        public int leftX;
        public int rightX;
        public int Y;

        public BufferRange(int leftX, int rightX, int Y) {
            this.leftX = leftX;
            this.rightX = rightX;
            this.Y = Y;
        }
    }
}
