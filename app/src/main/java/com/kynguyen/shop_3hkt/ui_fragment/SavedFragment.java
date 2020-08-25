package com.kynguyen.shop_3hkt.ui_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kynguyen.shop_3hkt.Model.Saves;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowSaveViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class SavedFragment extends Fragment {
    private View savedView;
    private DatabaseReference refSaved, ref;
    private RecyclerView recyclerViewListSave;
    private RelativeLayout save_show_no_Order;
    private RecyclerView.LayoutManager layoutManagerListSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedView = inflater.inflate(R.layout.fragment_saves, container, false);
        Paper.init(savedView.getContext());
        mapping();
        return savedView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Prevalent.currentOnLineUsers != null) {
            Query query = FirebaseDatabase.getInstance().getReference().child("saves").orderByChild(Prevalent.currentOnLineUsers.getUid()).equalTo(true);
            FirebaseRecyclerOptions<Saves> options = new FirebaseRecyclerOptions.Builder<Saves>()
                    .setQuery(query, Saves.class).build();

            FirebaseRecyclerAdapter<Saves, ShowSaveViewHolder> adapter = new FirebaseRecyclerAdapter<Saves, ShowSaveViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ShowSaveViewHolder holder, int position, @NonNull final Saves model) {
                    holder.name.setText(model.getName());
                    holder.address.setText(model.getAddress());
                    Picasso.get().load(model.getImage()).fit().into(holder.image);
                    save_show_no_Order.setVisibility(View.INVISIBLE);
                }

                @NonNull
                @Override
                public ShowSaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_save_holder_view, parent, false);
                    ShowSaveViewHolder holder = new ShowSaveViewHolder(view);
                    return holder;
                }
            };
            recyclerViewListSave.setAdapter(adapter);
            adapter.startListening();

//      refSaved = FirebaseDatabase.getInstance().getReference().child("saves");
//      FirebaseRecyclerOptions<Saves> options = new FirebaseRecyclerOptions.Builder<Saves>()
//          .setQuery(refSaved, Saves.class).build();
//
//      FirebaseRecyclerAdapter<Saves, ShowSaveViewHolder> adapter = new FirebaseRecyclerAdapter<Saves, ShowSaveViewHolder>(options) {
//        @Override
//        protected void onBindViewHolder(@NonNull final ShowSaveViewHolder holder, int position, @NonNull final Saves model) {
//
//          refSaved.child(model.pid).child(Prevalent.currentOnLineUsers.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//              if (dataSnapshot.exists()) {
//                if (dataSnapshot.getValue().equals(true)){
//                holder.name.setText(model.getName());
//                holder.address.setText(model.getAddress());
//                Picasso.get().load(model.getImage()).fit().into(holder.image);
//                save_show_no_Order.setVisibility(View.INVISIBLE);
//                } else {
//                  holder.relativeLayout.setVisibility(View.GONE);
//                  holder.relativeLayout.getLayoutParams().height = 0;
//                  holder.relativeLayout.getLayoutParams().width = 0;
//                }
//              }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//          });
//
//
//        }
//
//        @NonNull
//        @Override
//        public ShowSaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_save_holder_view,parent,false);
//          ShowSaveViewHolder holder = new ShowSaveViewHolder(view);
//          return holder;
//        }
//      };
//      recyclerViewListSave.setAdapter(adapter);
//      adapter.startListening();
        }
    }

    private void mapping() {
        layoutManagerListSave = new LinearLayoutManager(savedView.getContext());
        recyclerViewListSave = savedView.findViewById(R.id.list_saves_product_holder_View);
        recyclerViewListSave.setHasFixedSize(true);
        recyclerViewListSave.setLayoutManager(layoutManagerListSave);
        save_show_no_Order = savedView.findViewById(R.id.saved_show_no_Order);
    }
}
