package com.l3soft.routesmg.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l3soft.routesmg.R;
import com.l3soft.routesmg.data.RouteData;

public class TravelFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    public TravelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bus, container, false);

        swipeContainer = view.findViewById(R.id.swipe_refresh);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(
                R.color.purple,
                R.color.orange,
                R.color.green);
        setHasOptionsMenu(true);

        initView();
        return view;
    }

    public void initView(){
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RouteData routeData = new RouteData(getContext());
        routeData.getRoutes(recyclerView);
    }

    @Override
    public void onRefresh() {

    }
}
