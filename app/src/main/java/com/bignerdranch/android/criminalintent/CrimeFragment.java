package com.bignerdranch.android.criminalintent;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeFragment extends Fragment {
    //UIFragment 管理用户界面
    private static final String ARG_CRIME_ID = "crime_id";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    //每个fragment实例都可附带一个 Bundle 对象。该bundle包含键值对，我们可以像附加extra到 Activity 的intent中那样使用它们。一个键值对即一个argument。
    //要创建fragment argument，首先需创建 Bundle 对象。
    //然后，使用 Bundle 限定类型的 put 方法（类似于 Intent 的方法），将argument添加到bundle中
    //要附加argument bundle给fragment，需调用 Fragment.setArguments(Bundle) 方法。而且，还必须在fragment创建后、添加给activity前完成。
    //为满足以上要求，Android开发人员采取的习惯做法是：
    //添加名为 newInstance() 的静态方法给 Fragment 类
    //使用该方法，完成fragment实例及 Bundle 对象的创建
    //然后将argument放入bundle中，最后再附加给fragment
    //托管activity需要fragment实例时，转而调用 newInstance() 方法，而非直接调用其构造方法。
    //而且，为满足fragment创建argument的要求，activity可给 newInstance() 方法传入任何需要的参数。
    public static CrimeFragment newInstance(UUID crimeId) {//在 CrimeFragment 类中，编写可以接受 UUID 参数的 newInstance(UUID) 方法
        //创建argument bundle实例
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        //创建fragment实例
        CrimeFragment fragment = new CrimeFragment();
        //附加argument给fragment
        fragment.setArguments(args);
        //现在，需创建 CrimeFragment 时， CrimeActivity 应调用 CrimeFragment.newInstance(UUID)方法，
        //并传入从它的extra中获取的 UUID 参数值。
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //与activity相似，但是这里的conCreate方法是公共方法，因为需要被activity调用
        super.onCreate(savedInstanceState);
        //fragment需要获取它的argument时，会先调用 Fragment 类的 getArguments() 方法，
        //再调用Bundle 限定类型的 get 方法，如 getSerializable(...) 方法。
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
// This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
// This one too
            }
        });
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }
}
