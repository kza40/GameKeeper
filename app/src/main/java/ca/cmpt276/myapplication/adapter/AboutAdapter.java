package ca.cmpt276.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ca.cmpt276.myapplication.DevelopersFragment;
import ca.cmpt276.myapplication.HelpFragment;
import ca.cmpt276.myapplication.ResourcesFragment;

public class AboutAdapter extends FragmentStateAdapter {

    private static final int FRAGMENT_COUNT = 3;

    public AboutAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DevelopersFragment();
            case 1:
                return new ResourcesFragment();
            case 2:
                return new HelpFragment();
            default:
                return new DevelopersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_COUNT;
    }
}
