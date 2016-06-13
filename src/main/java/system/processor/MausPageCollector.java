package system.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects MausPage instances from different threads
 *
 * Created by Fabi on 06.03.2015.
 */
public class MausPageCollector {
    private List<MausPage> pages = new ArrayList<>();

    public synchronized void addPage(MausPage page) {
        this.pages.add(page);
    }

    public List<MausPage> getPages() {
        return this.pages;
    }
}