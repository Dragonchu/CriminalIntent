package com.bignerdranch.android.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class CrimeActivity extends SingleFragmentActivity {
    //Activity托管UIFragment
    //Activity在其视图层级里提供一处位置来放置Fragment视图
    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}