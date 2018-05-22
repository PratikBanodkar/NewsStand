package pratik.com.newsstand;

import java.util.ArrayList;

/**
 * Created by Pratz on 30-01-2018.
 */

public class Category{
    private String categoryName;
    private ArrayList categorySourceIDs;
    private ArrayList categorySourceNames;
    private ArrayList categorySourceLogos;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList getCategorySourceNames() {
        return categorySourceNames;
    }

    public void setCategorySourceNames(ArrayList categorySourceNames) {
        this.categorySourceNames = categorySourceNames;
    }

    public ArrayList getCategorySourceLogos() {
        return categorySourceLogos;
    }

    public void setCategorySourceLogos(ArrayList categorySourceLogos) {
        this.categorySourceLogos = categorySourceLogos;
    }

    public ArrayList getCategorySourceIDs() {
        return categorySourceIDs;
    }

    public void setCategorySourceIDs(ArrayList categorySourceIDs) {
        this.categorySourceIDs = categorySourceIDs;
    }
}
