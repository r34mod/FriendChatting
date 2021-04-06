package com.example.friendchatting.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.friendchatting.fragmentos.chatsFragment;
import com.example.friendchatting.fragmentos.popularFragment;
import com.example.friendchatting.fragmentos.solicitudFragment;
import com.example.friendchatting.fragmentos.usuarioFrag;

public class AdapterPages extends FragmentStateAdapter {

    public AdapterPages(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new usuarioFrag();
            case 1:
                return new chatsFragment();
            case 2:
                return new solicitudFragment();
            case 3:
                return new popularFragment();
            default:
                return new chatsFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
