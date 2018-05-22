package pratik.com.newsstand;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by Pratz on 29-01-2018.
 */

public class LibraryOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;

    public LibraryOnTabSelectedListener(ViewPager pager){
        this.viewPager = pager;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
