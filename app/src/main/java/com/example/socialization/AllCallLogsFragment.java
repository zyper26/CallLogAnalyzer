package com.example.socialization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialization.utils.CallLogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllCallLogsFragment extends Fragment {

    private static final String TAG = "call_log_fragment";
    private RecyclerView recyclerView;
    private CallLogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_log_fragment,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        initComponents();
        return view;
    }

    private void initComponents() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CallLogAdapter(getContext());
        recyclerView.setAdapter(adapter);
        loadData();
    }

    public void loadData(){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(getContext());
        adapter.addAllCallLog(callLogUtils.readCallLogs());
        adapter.notifyDataSetChanged();
    }
}
