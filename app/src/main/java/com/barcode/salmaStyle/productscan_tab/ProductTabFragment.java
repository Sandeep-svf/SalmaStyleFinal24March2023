package com.barcode.salmaStyle.productscan_tab;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.barcode.salmaStyle.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductTabFragment extends Fragment {

    private TabLayout tab_Layout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_scan_tab, container, false);
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager = view.findViewById(R.id.viewPager1);
        tab_Layout = view.findViewById(R.id.tab_Layout);
        tab_Layout.setupWithViewPager(viewPager);
       // setupViewPager(viewPager);

        TabLayout.Tab Approved = tab_Layout.newTab();
        Approved.setText(getString(R.string.approved));
        Approved.setTag("1");
        tab_Layout.addTab(Approved);

        TabLayout.Tab Pending = tab_Layout.newTab();
        Pending.setText("Pending");
        Pending.setTag("2");
        tab_Layout.addTab(Pending);

        TabLayout.Tab Rejected = tab_Layout.newTab();
        Rejected.setText(getString(R.string.rejected));
        Rejected.setTag("2");
        tab_Layout.addTab(Rejected);

        TabLayout.Tab OnDiet = tab_Layout.newTab();
        OnDiet.setText(getString(R.string.ondiet));
        OnDiet.setTag("3");
        tab_Layout.addTab(OnDiet);

        TabLayout.Tab occassion = tab_Layout.newTab();
        occassion.setText(getString(R.string.occassionaly));
        occassion.setTag("4");
        tab_Layout.addTab(occassion);


        tab_Layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                click(view, tab.getTag().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                click(view, tab.getTag().toString());
            }
        });
        Approved.select();

        return view;

    }

    private void click(View view, String tag) {
        ((LinearLayout) view.findViewById(R.id.load_fragment)).removeAllViews();

        if (tag.equals("1")){
            ProductApprovedFragment productApprovedFragment = new ProductApprovedFragment();
            getChildFragmentManager().beginTransaction().add(R.id.load_fragment, productApprovedFragment).commit();
        } else if (tag.equals("2")){
            ProductPendingFragment productPendingFragment = new ProductPendingFragment();
            getChildFragmentManager().beginTransaction().add(R.id.load_fragment, productPendingFragment).commit();
        } else if (tag.equals("3")){
            ProductRejectedFragment productRejectedFragment = new ProductRejectedFragment();
            getChildFragmentManager().beginTransaction().add(R.id.load_fragment, productRejectedFragment).commit();
        } else if (tag.equals("4")){
            OnDietFragment onDietFragment = new OnDietFragment();
            getChildFragmentManager().beginTransaction().add(R.id.load_fragment, onDietFragment).commit();
        } else if (tag.equals("5")){
            OccasionalyFragment occasionalyFragment = new OccasionalyFragment();
            getChildFragmentManager().beginTransaction().add(R.id.load_fragment, occasionalyFragment).commit();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProductApprovedFragment(), "Approved");
        adapter.addFragment(new ProductPendingFragment(), "Pending");
        adapter.addFragment(new ProductRejectedFragment(), "Rejected");
        adapter.addFragment(new OnDietFragment(), "OnDiet");
        adapter.addFragment(new OccasionalyFragment(), "occassion");
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
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
