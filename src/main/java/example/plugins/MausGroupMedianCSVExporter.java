package example.plugins;

import system.plugins.MausGroupContentInterface;
import system.processor.MausPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Fabi on 09.04.2015.
 */
public class MausGroupMedianCSVExporter implements MausGroupContentInterface {
    private String filename = "maus/result/";
    private List<Map<String, String>> values = new ArrayList<>();
    private String[] indices;

    public MausGroupMedianCSVExporter(String seed, String[] indices) {
        if(seed != null && !seed.isEmpty()) {
            this.filename += seed + "/";
        }
        this.filename += "indices-median.csv";
        this.indices = indices;
    }

    @Override
    public void handle(MausPage page) {
        Map<String, String> entry = new HashMap<>();
        for(String index : indices) {
            entry.put(index, page.getAttribute(index));
        }
        values.add(entry);
    }

    @Override
    public void save() {
        Map<String, Map<String, Double>> medianValues = new HashMap<>();

        for (String index : indices) {
            medianValues.put(index, new HashMap<>());
            medianValues.get(index).put("median", 0.0d);
            medianValues.get(index).put("count", values.size() + 0.0d);
            medianValues.get(index).put("min-value", Double.MAX_VALUE);
            medianValues.get(index).put("max-value", Double.MIN_VALUE);
            medianValues.get(index).put("value-span", 0.0d);
            medianValues.get(index).put("variance", 0.0d);
            medianValues.get(index).put("standard-deviation", Double.MAX_VALUE);
        }

        try {
            for (Map<String, String> value : values) {
                for (String index : indices) {
                    medianValues.get(index).put("median", medianValues.get(index).get("median") + Double.valueOf(NumberFormat.getInstance(Locale.getDefault()).parse(value.get(index)).toString()));
                    Double curMin = medianValues.get(index).get("min-value");
                    if (curMin > Double.valueOf(NumberFormat.getInstance(Locale.getDefault()).parse(value.get(index)).toString())) {
                        medianValues.get(index).put("min-value", Double.valueOf(NumberFormat.getInstance(Locale.getDefault()).parse(value.get(index)).toString()));
                    }
                    Double curMax = medianValues.get(index).get("max-value");
                    if (curMax < Double.valueOf(NumberFormat.getInstance(Locale.getDefault()).parse(value.get(index)).toString())) {
                        medianValues.get(index).put("max-value", Double.valueOf(NumberFormat.getInstance(Locale.getDefault()).parse(value.get(index)).toString()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String index : indices) {
            medianValues.get(index).put("median", medianValues.get(index).get("median") / values.size());
            medianValues.get(index).put("value-span", medianValues.get(index).get("max-value") - medianValues.get(index).get("min-value"));
        }

        try {
            for (Map<String, String> value : values) {
                for (String index : indices) {
                    medianValues.get(index).put("variance", medianValues.get(index).get("variance") + Math.pow(Double.valueOf(NumberFormat.getInstance(Locale.getDefault()).parse(value.get(index)).toString()) - medianValues.get(index).get("median"), 2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String index : indices) {
            medianValues.get(index).put("variance", medianValues.get(index).get("variance") * (1.0d/values.size()));
            medianValues.get(index).put("standard-deviation", Math.sqrt(medianValues.get(index).get("variance")));
        }

        File f = new File(filename);

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(f));

            fout.write("##;##\n");
            fout.write("@File " + filename + " CSV File LiveGraph Compatible\n");

            for(String index : indices) {
                fout.write(";");
                fout.write(index);
            }
            fout.write("\n");

            Map<String,String> rows = new HashMap<>();

            rows.put("median", "median");
            rows.put("count", "count");
            rows.put("min-value", "min-value");
            rows.put("max-value", "max-value");
            rows.put("value-span", "value-span");
            rows.put("variance", "variance");
            rows.put("standard-deviation", "standard-deviation");

            for(String index : indices) {
                rows.put("median", rows.get("median") + ";" + String.format(Locale.getDefault(), "%.5f", medianValues.get(index).get("median")));
                rows.put("count", rows.get("count") + ";" + String.format(Locale.getDefault(), "%.1f", medianValues.get(index).get("count")));
                rows.put("min-value", rows.get("min-value") + ";" + String.format(Locale.getDefault(), "%.5f", medianValues.get(index).get("min-value")));
                rows.put("max-value", rows.get("max-value") + ";" + String.format(Locale.getDefault(), "%.5f", medianValues.get(index).get("max-value")));
                rows.put("value-span", rows.get("value-span") + ";" + String.format(Locale.getDefault(), "%.5f", medianValues.get(index).get("value-span")));
                rows.put("variance", rows.get("variance") + ";" + String.format(Locale.getDefault(), "%.5f", medianValues.get(index).get("variance")));
                rows.put("standard-deviation", rows.get("standard-deviation") + ";" + String.format(Locale.getDefault(), "%.5f", medianValues.get(index).get("standard-deviation")));
            }

            for(String row : rows.values()) {
                fout.write(row + "\n");
            }

            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
