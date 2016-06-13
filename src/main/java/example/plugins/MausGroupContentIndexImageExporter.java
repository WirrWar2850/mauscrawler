package example.plugins;

import system.plugins.MausGroupContentInterface;
import system.visualization.MausGraphImage2D;
import system.processor.MausPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Fabi on 23.02.2015.
 */
public class MausGroupContentIndexImageExporter implements MausGroupContentInterface {
    Map<String, List<String>> values = new HashMap<>();
    String[] indices = new String[]{"balaban-index", "randic-index", "wiener-index", "zagreb-index"};
    String filename = "maus/result/";

    public MausGroupContentIndexImageExporter(String seed) {
        if(seed != null && !seed.isEmpty()) {
            filename += seed + "/";
        }

        for(String s : indices) {
            values.put(s, new ArrayList<>());
        }
    }

    @Override
    public void handle(MausPage page) {
        for(String s : indices) {
            values.get(s).add(page.getAttribute(s));
        }
    }

    @Override
    public void save() {
        if(values.size() == 0) {
            return;
        }
        for(String s : indices) {
            MausGraphImage2D image = new MausGraphImage2D(1024, 1024);
            String filename = this.filename + s + ".bmp";

            image.setMinXValue(-0.1f);
            image.setMinYValue(-0.1f);

            List<Float> values = new ArrayList<>();

            for(String val : this.values.get(s)) {
                try {
                    values.add(Float.valueOf(NumberFormat.getInstance(Locale.getDefault()).parse(val).toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            image.setMaxXValue(values.size());
            image.setDrawXScale(false);
            image.setDrawYScale(false);
            image.setProbeRadius(1);

            float i = 0.0f;

            Collections.sort(values);
            image.setYScale(Math.round(values.get(values.size()-1)/10.0f));
            image.setMaxYValue(values.get(values.size() - 1));

            for(Float val : values) {
                image.addProbe(i, val, 0xff0000);
                i += 1.0f;
            }

            image.save(filename);
        }
    }

    @Override
    public void setRootFolder() {/* not implemented */}

    /**
     * do not use this method!
     * @return null
     */
    @Override
    public String getRootFolder() {return null;}

    @Override
    public String getFilePath() {
        return filename;
    }
}
