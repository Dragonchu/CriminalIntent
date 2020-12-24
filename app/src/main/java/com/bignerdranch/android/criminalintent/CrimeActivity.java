package com.bignerdranch.android.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    //其他类不会用到 EXTRA_CRIME_ID ，可以将其改为私有
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        //创建了显式intent后，调用 putExtra(...) 方法，传入匹配 crimeId 的字符串键与键值。
        //这里，由于 UUID 是 Serializable 对象，因此我们调用的其实是 putExtra(String, Serializable) 方法。
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        //crime ID现已安全存储到 CrimeActivity 的intent中。然而，要获取和使用extra信息的是CrimeFragment 类。
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        //在 createFragment()方法里，从 CrimeActivity 的intent中获取extra数据，
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        //并传入 CrimeFragment.newInstance(UUID)方法
        return CrimeFragment.newInstance(crimeId);
        //activity和fragment不需要也无法同时相互保持独立。
        //CrimeActivity 必须了解CrimeFragment 的内部细节，比如知道它内部有个 newInstance(UUID) 方法。
        //activity应该知道这些细节，以便托管fragment
    }
}