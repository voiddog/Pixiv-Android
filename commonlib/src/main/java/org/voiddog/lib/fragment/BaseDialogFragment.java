package org.voiddog.lib.fragment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * 基础dialog fragment
 * Created by Dog on 2015/8/6.
 */
public class BaseDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        return null;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(isAdded()){
            return;
        }
        super.show(manager, tag);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if(isAdded()){
            return -1;
        }
        return super.show(transaction, tag);
    }
}
