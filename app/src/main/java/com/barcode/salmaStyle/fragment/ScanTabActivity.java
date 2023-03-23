package com.barcode.salmaStyle.fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.productscan_tab.OccasionalyFragment;
import com.barcode.salmaStyle.productscan_tab.OnDietFragment;
import com.barcode.salmaStyle.productscan_tab.ProductApprovedFragment;
import com.barcode.salmaStyle.productscan_tab.ProductPendingFragment;
import com.barcode.salmaStyle.productscan_tab.ProductRejectedFragment;
import com.barcode.salmaStyle.productscan_tab.ProductTabFragment;
import com.barcode.salmaStyle.utol.Originator;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ScanTabActivity extends Originator {

    private TabLayout tab_Layout;
    private ViewPager viewPager;
    ConstraintLayout back;
    TextView text_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager = findViewById(R.id.viewPager1);
        back = findViewById(R.id.cons_tool_img);
        text_toolbar = findViewById(R.id.toolbar_text);
        text_toolbar.setText(getString(R.string.detail_list));
        setupViewPager(viewPager);

        tab_Layout = findViewById(R.id.tabLayout1);
        tab_Layout.setupWithViewPager(viewPager);
        backListener();
    }

    private void backListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ProductTabFragment.ViewPagerAdapter adapter = new ProductTabFragment.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProductApprovedFragment(), getString(R.string.approved));
        adapter.addFragment(new ProductPendingFragment(), getString(R.string.pending));
        adapter.addFragment(new ProductRejectedFragment(), getString(R.string.rejected));
        adapter.addFragment(new OnDietFragment(), getString(R.string.ondiet));
        adapter.addFragment(new OccasionalyFragment(), getString(R.string.ocasion));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}