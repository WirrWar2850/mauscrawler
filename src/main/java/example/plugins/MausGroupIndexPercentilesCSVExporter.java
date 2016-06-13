package example.plugins;

import system.plugins.MausGroupContentInterface;
import system.processor.MausPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Fabi on 3/17/2015.
 */
public class MausGroupIndexPercentilesCSVExporter implements MausGroupContentInterface {
    private String filename = "maus/result/";
    private int numPercentiles;
    private Map<String,List<Double>> indexValues = new HashMap<>();
    private String[] indices = new String[]{"balaban-index","zagreb-index","wiener-index","randic-index","vertex-count"};

    public MausGroupIndexPercentilesCSVExporter(String seed, int numPercentiles) {
        if(seed != null && !seed.isEmpty()) {
            this.filename += seed + "/";
        }
        this.filename += "index-percentiles-" + numPercentiles + ".csv";
        this.numPercentiles = numPercentiles;

        for(String index : indices) {
            indexValues.put(index, new ArrayList<>());
        }
    }

    @Override
    public void handle(MausPage page) {
        for(String index : indices) {
            try {
                indexValues.get(index).add(NumberFormat.getInstance(Locale.getDefault()).parse(page.getAttribute(index)).doubleValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save() {
        File f = new File(filename);

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(f));

            fout.write("##;##\n");
            fout.write("@File " + filename + " CSV File LiveGraph Compatible\n");


            boolean empty = true;
            fout.write("PERCENT;");
            for(String index : indices) {
                if(!empty) {
                    fout.write(";");
                }
                empty = false;
                fout.write(index);
            }
            fout.write("\n");

            Map<String,List<Integer>> percentiles = getPercentileList();

            for(int i=0;i<numPercentiles;i++) {
                fout.write(""+(i*(100/numPercentiles)));
                for(String index : indices) {
                    fout.write(";");
                    fout.write(percentiles.get(index).get(i)+"");
                }
                fout.write("\n");
            }

            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String,List<Integer>> getPercentileList() {
        Map<String,List<Integer>> result = new HashMap<>();

        for(String index : indices) {
            List<Double> values = indexValues.get(index);
            if(values.isEmpty()) {
                continue;
            }
            double min = values.get(0);
            double max = values.get(0);

            for(Double v : values) {
                if(v < min) {
                    min = v;
                }
                if(v > max) {
                    max = v;
                }
            }
            double range = (max - min) / numPercentiles;
            double currentLowerBound = min;

            List<Integer> percentiles = new ArrayList<>();

            for(int i=0;i<numPercentiles;i++) {
                int count = 0;
                for(Double v : values) {
                    if(v > currentLowerBound && v <= currentLowerBound+range) {
                        count++;
                    }
                }
                percentiles.add(count);
                currentLowerBound += range;
            }

            result.put(index, percentiles);
        }

        return result;
    }

    @Override
    public void setRootFolder() {
    }

    @Override
    public String getRootFolder() {
        return null;
    }

    @Override
    public String getFilePath() {
        return null;
    }
}
