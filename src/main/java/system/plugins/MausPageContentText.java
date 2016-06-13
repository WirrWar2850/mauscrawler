package system.plugins;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Fabi on 23.02.2015.
 */
public class MausPageContentText extends MausPageContentAbstract {
    private String text;

    public MausPageContentText(String filePath, String text) {
        this.setFilePath(filePath);
        this.text = text;
    }

    @Override
    public void save() {
        super.save();

        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(getRootFolder() + getFilePath()));

            fout.write(text);

            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setText(String text) {
        this.text = text;
    }
}
