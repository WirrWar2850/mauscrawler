package system.visualization;

import system.utilities.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * simple 2-axis graph visualization
 *
 * Created by Fabi on 23.02.2015.
 */
public class MausGraphImage2D {
    private BufferedImage image;
    private float maxXValue = 1.0f;
    private float minXValue = -1.0f;
    private float maxYValue = 1.0f;
    private float minYValue = -1.0f;
    private int probeRadius = 1;
    private int border = 20;
    private float xScale = 0.1f;
    private float yScale = 0.1f;

    private boolean drawXScale = true;
    private boolean drawYScale = true;

    private int clearColor = 0xffffff;
    private int defaultProbesPerFunction = 500;

    private List<Pair<Pair<Float, Float>, Integer>> probes = new ArrayList<>();
    private Map<String, Pair<Function2D, Pair<Integer, Integer>>> functions = new HashMap<>();

    public MausGraphImage2D(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        clear(clearColor);
    }

    public void setMaxXValue(float value) {
        this.maxXValue = value;
        clear(clearColor);
    }

    public void setMinXValue(float value) {
        this.minXValue = value;
        clear(clearColor);
    }

    public void setMaxYValue(float value) {
        this.maxYValue = value;
        clear(clearColor);
    }

    public void setMinYValue(float value) {
        this.minYValue = value;
        clear(clearColor);
    }

    public void setXScale(float value) {
        if(value < Float.MIN_VALUE) {
            this.setDrawXScale(false);
        }
        this.xScale = value;
        clear(clearColor);
    }

    public void setYScale(float value) {
        if(value < Float.MIN_VALUE) {
            this.setDrawYScale(false);
        }
        this.yScale = value;
        clear(clearColor);
    }

    public void setDrawXScale(boolean value) {
        this.drawXScale = value;
    }

    public void setDrawYScale(boolean value) {
        this.drawYScale = value;
    }

    public void save(String filename) {
        try {
            File f = new File(filename);
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            ImageIO.write(image, "BMP", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear(int color) {
        for(int i=0;i<image.getWidth();i++) {
            for(int j=0;j<image.getHeight();j++) {
                image.setRGB(i, j, color);
            }
        }
        refreshCoordinates();
        paintScale();
        redrawProbes();
        paintLabels();
    }

    public void discardProbes() {
        this.probes.clear();
    }

    public void setProbeRadius(int pixels) {
        this.probeRadius = pixels;
    }

    public void addProbe(float x, float y) {
        this.addProbe(x, y, 0x000000);
    }

    public void addProbe(float x, float y, int color) {
        this.probes.add(new Pair<>(new Pair<>(x,y), color));
        drawProbe(x, y, color);
    }

    private void drawProbe(float x, float y, int color) {
        if(x < minXValue || x > maxXValue || y < minYValue || y > maxYValue) {
            System.err.println("Probe value out of allowed scope. Probe will not be drawn!");
            return;
        }

        Pair<Integer, Integer> coord = valueToPixel(x, y);

        for(int i= -probeRadius;i<probeRadius*2;i++) {
            for(int j= -probeRadius;j<probeRadius*2;j++) {
                image.setRGB(coord._1+i, coord._2+j, color);
            }
        }
    }

    public void addFunction(String name, Function2D func) {
        addFunction(name, func, defaultProbesPerFunction);
    }

    public void addFunction(String name, Function2D func, int numProbes) {
        addFunction(name, func, numProbes, 0x000000);
    }

    public void addFunction(String name, Function2D func, int numProbes, int color) {
        this.functions.put(name, new Pair<>(func, new Pair<>(numProbes, color)));

        drawFunction(func, numProbes, color);
    }

    private void drawFunction(Function2D func, int numProbes, int color) {
        float step = (Math.abs(minXValue) + Math.abs(maxXValue)) / (float)numProbes;

        for(float i=minXValue;i<maxXValue;i+=step) {
            drawProbe(i, func.calculate(i), color);
        }
        paintLabels();
    }

    private Pair<Integer, Integer> valueToPixel(float x, float y) {
        x += Math.abs(minXValue);
        y += Math.abs(minYValue);

        float width = (float)image.getWidth()-(float)border*2;
        float height = (float)image.getHeight()-(float)border*2;

        float xSpan = Math.abs(maxXValue) + Math.abs(minXValue);
        float ySpan = Math.abs(maxYValue) + Math.abs(minYValue);

        float xFactor = (width / xSpan);
        float yFactor = (height / ySpan);

        return new Pair<>((int)Math.ceil(xFactor * x) + border, (int)Math.ceil(height - yFactor * y) + border);
    }

    private void refreshCoordinates() {
        Pair<Integer, Integer> center = valueToPixel(0.0f, 0.0f);

        for(int i=border;i<image.getWidth()-border;i++) {
            image.setRGB(i, center._2, 0x000000);
        }
        for(int i=border;i<image.getHeight()-border;i++) {
            image.setRGB(center._1, i, 0x000000);
        }
    }

    private void paintScale() {
        Font scale = new Font("Arial", Font.PLAIN, 11);
        Graphics2D g = image.createGraphics();
        g.setFont(scale);
        g.setColor(Color.BLACK);

        if(drawXScale) {
            for (float i = minXValue; i < maxXValue; i += xScale) {
                Pair<Integer, Integer> coord = valueToPixel(i, 0.0f);
                for (int j = 0; j < 3; j++) {
                    image.setRGB(coord._1, coord._2 + j, 0x000000);
                }
                g.drawString(String.format("%.2f", i), coord._1, coord._2 + 15);
            }
        }
        if(drawYScale) {
            for (float i = minYValue; i < maxYValue; i += yScale) {
                Pair<Integer, Integer> coord = valueToPixel(0.0f, i);
                for (int j = 0; j < 3; j++) {
                    image.setRGB(coord._1 + j, coord._2, 0x000000);
                }
                g.drawString(String.format("%.2f", i), coord._1 + 10, coord._2);
            }
        }

        g.dispose();
    }

    private void paintLabels() {
        Font scale = new Font("Arial", Font.PLAIN, 12);
        Graphics2D g = image.createGraphics();
        g.setFont(scale);
        g.setColor(Color.BLACK);

        int i=0;
        for(String name : this.functions.keySet()) {
            if(!name.isEmpty()) {
                printTopLabel(g, name, i++, this.functions.get(name)._2._2);
            }
        }

        g.dispose();
    }

    private void printTopLabel(Graphics2D graphics, String label, int row, int color) {
        graphics.setColor(new Color(color));
        graphics.drawString(label, image.getWidth()/2.0f-graphics.getFontMetrics().stringWidth(label)-10, 3+(row+1)*graphics.getFontMetrics().getHeight());
    }

    /**
     * redraws all saved probes if not out of scope
     */
    private void redrawProbes() {
        for(Pair<Pair<Float, Float>, Integer> p : this.probes) {
            drawProbe(p._1._1, p._1._2, p._2);
        }
        for(Pair<Function2D, Pair<Integer, Integer>> f : this.functions.values()) {
            drawFunction(f._1, f._2._1, f._2._2);
        }
    }

    public int getNumberOfProbes() {
        return this.probes.size();
    }
}
