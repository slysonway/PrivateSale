package com.kfp.privatesale.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kfp.privatesale.R;
import com.kfp.privatesale.service.model.Event;
import com.kfp.privatesale.view.ui.fragment.EventListFragment;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements EventListener<QuerySnapshot> {

    private final List<Event> mValues;
    private final EventListFragment.OnListFragmentInteractionListerner mListener;
    private Query query;
    private ListenerRegistration registration;

    public EventAdapter(Query query, EventListFragment.OnListFragmentInteractionListerner listerner) {
        mValues = new ArrayList<>();
        mListener = listerner;
        this.query = query;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    public void startQuery() {
        if (query != null && registration == null) {
            registration = query.addSnapshotListener(this);
        }
    }

    public void stopQuery() {
        if (registration != null) {
            registration.remove();
            registration = null;
        }
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mEvent = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mDateView.setText(mValues.get(position).getDate().toDate().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onListFragmentInteraction(holder.mEvent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return; //TODO handle better exception
        }

        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    mValues.add(dc.getNewIndex(), dc.getDocument().toObject(Event.class));
                    notifyItemInserted(dc.getNewIndex());
                    break;
                case REMOVED:
                    mValues.remove(dc.getOldIndex());
                    notifyItemRemoved(dc.getOldIndex());
                    break;
                case MODIFIED:
                    if (dc.getOldIndex() == dc.getNewIndex()) {
                        mValues.set(dc.getNewIndex(), dc.getDocument().toObject(Event.class));
                        notifyItemChanged(dc.getNewIndex());
                    } else {
                        mValues.remove(dc.getOldIndex());
                        mValues.add(dc.getNewIndex(), dc.getDocument().toObject(Event.class));
                        notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                    }
                    break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDateView;
        public Event mEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.event_name);
            mDateView = (TextView) view.findViewById(R.id.event_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

}
