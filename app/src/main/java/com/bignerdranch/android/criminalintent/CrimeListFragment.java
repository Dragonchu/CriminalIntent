package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    @Override
    //第一步
    //配置完CrimeListFragment的视图，接下来的任务就是视图和fragment的关联
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //该 方 法 实 例 化 fragment 视 图 的 布 局 ， 然 后 将 实 例 化 的 View 返 回 给 托 管 activity 。
        //LayoutInflater 及 ViewGroup 是实例化布局的必要参数。 Bundle 用来存储恢复数据，可供该方法从保存状态下重建视图。
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        //视图是直接通过调用 LayoutInflater.inflate(...)方法并传入布局的资源ID生成的。
        //第二个参数是视图的父视图，我们通常需要父视图来正确配置组件。
        //第三个参数告诉布局生成器是否将生成的视图添加给父视图。这里，传入了 false 参数，因为我们将以代码的方式添加视图。
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        //没有 LayoutManager 的支持，不仅 RecyclerView 无法工作，还会导致应用崩溃。
        //所以， RecyclerView 视图创建完成后，就立即转交给了 LayoutManager 对象。
        //RecyclerView 类不会亲自摆放屏幕上的列表项。实际上，摆放的任务被委托给了LayoutManager 。
        //除了在屏幕上摆放列表项， LayoutManager 还负责定义屏幕滚动行为。因此，
        //没有 LayoutManager ， RecyclerView 也就没法正常工作。
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    //第二步
    //在 CrimeListFragment 类中定义 ViewHolder 内部类，它会实例化并使用list_item_crime布局
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;
        //在 CrimeHolder 的构造方法里，我们首先实例化list_item_crime布局，然后传给 super(...)方法，也就是 ViewHolder 的构造方法。
        //基类 ViewHolder 因而实际引用这个视图。可以在 ViewHolder 的 itemView 变量里找到它。
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            //视图是直接通过调用 LayoutInflater.inflate(...)方法并传入布局的资源ID生成的。
            //第二个参数是视图的父视图，我们通常需要父视图来正确配置组件。
            //第三个参数告诉布局生成器是否将生成的视图添加给父视图。这里，传入了 false 参数，因为我们将以代码的方式添加视图。
            super(inflater.inflate(R.layout.list_item_crime, parent, false));//实例化list_item_crime布局，然后传给 super(...)方法
            //既然列表项视图都关联着 ViewHolder ，就可以让 ViewHolder 为它监听用户触摸事件。
            itemView.setOnClickListener(this);
            //我们把视图绑定工作放入 CrimeHolder 类里。
            //绑定之前，首先实例化相关组件。由于这是一次性任务，因此直接放在构造方法里处理
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        //CrimeHolder 还需要一个 bind(Crime) 方法。每次有新的 Crime 要在 CrimeHolder 中显示时，都要调用它一次
        public void bind(Crime crime) {
            //只要取到一个 Crime ， CrimeHolder 就会刷新显示 TextView 标题视图和 TextView 日期视图。
            //所谓绑定，实际就是让Java代码（ Crime 里的模型数据，或点击监听器）和组件关联起来。
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        //我们将关联CriminalIntent应用的列表与明细部分。用户点击某个crime列表项时，会
        //创建一个托管 CrimeFragment 的 CrimeActivity ，以展现 Crime 实例的明细信息。
        public void onClick(View v) {
            //指定要启动的activity为 CrimeActivity ， CrimeListFragment 创建了一个显式intent。
            //至于Intent 构造方法需要的 Context 对象， CrimeListFragment 是通过使用 getActivity() 方法传入它的托管activity来满足的。
            //启动 CrimeActivity 时，传递附加到 Intent extra上的crime ID， CrimeFragment 就能知道该显示哪个 Crime 。
            //这需要在 CrimeActivity 中新增 newIntent 方法
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            //从fragment中启动activity类似于从activity中启动activity。
            //我们调用 Fragment.startActivity(Intent) 方法，由它在后台再调用对应的 Activity 方法。
            startActivity(intent);
        }
    }
    //第三步
    //定义完 ViewHolder ，接下来的任务是创建 Adapter
    //需要显示新创建的 ViewHolder 或让 Crime 对象和已创建的 ViewHolder 关联时，
    //RecyclerView 会去找 Adapter （调用它的方法）。
    //RecyclerView 不关心也不了解具体的 Crime 对象，这是Adapter 要做的事。
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        //要创建Adapter,首先要定义RecyclerView.Adapter子类,然后由它封装从CrimeLab获取的crime
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }
        //RecyclerView需要显示视图对象时，就会去找它的Adapter,下面是RecyclerView会发起的会话
        @Override
        //首先，调用Adapter的getItemCount方法，RecyclerView询问数组列表中包含多少个对象
        public int getItemCount() {
            return mCrimes.size();
        }
        @Override
        //接着，RecyclerView调用Adapter的onCreateViewHolder方法创建ViewHolder及其要显示的视图
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //RecyclerView 需要新的 ViewHolder 来显示列表项时，会调用 onCreateViewHolder 方法。
            //在这个方法内部，我们创建一个 LayoutInflater ，然后用它创建 CrimeHolder 。
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }
        @Override
        //最后，RecyclerView会传入ViewHolder及其位置，调用onBindViewHolder方法。
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            //Adapter会找到目标位置的数据并将其绑定到ViewHolder的视图上
            Crime crime = mCrimes.get(position);
            //修改 CrimeAdapter 类，使用 bind(Crime) 方法。
            //每次 RecyclerView 要求 CrimeHolder绑定对应的 Crime 时 ， 都会调用 bind(Crime) 方法
            holder.bind(crime);
        }
        //onCreateViewHolder和onBindViewHolder方法会被RecyclerView调用多次
    }
    //第四步
    //搞定了 Adapter ，最后要做的就是将它和 RecyclerView 关联起来。
    //实现一个设置 CrimeListFragment 用户界面的 updateUI 方法，该方法创建 CrimeAdapter ，然后设置给 RecyclerView
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
