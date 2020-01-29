package com.kfp.privatesale.view.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Event;
import com.kfp.privatesale.view.adapter.EventAdapter;


public class EventListFragment extends Fragment {

    private OnListFragmentInteractionListerner mListener;
    private EventAdapter adapter;

    public EventListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.event_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        //Set adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("events").orderBy("name");
            adapter = new EventAdapter(query, mListener);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startQuery();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopQuery();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListerner) {
            mListener = (OnListFragmentInteractionListerner) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface  OnListFragmentInteractionListerner{
        void onListFragmentInteraction(Event event);
    }
}
