package pratik.com.newsstand;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import pratik.com.newsstand.NewsFetching.NewsItemObject;

public class ExclusionStrategy_Bitmap_Drawable implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {

        return (f.getDeclaringClass() == NewsItemObject.class && f.getName().equals("articleImage"))||
                (f.getDeclaringClass() == NewsItemObject.class && f.getName().equals("articleSourceLogo"));
    }

}
