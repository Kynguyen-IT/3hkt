package com.kynguyen.shop_3hkt.ui_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.shop_3hkt.Model.Saves;
import com.kynguyen.shop_3hkt.Prevalent.Prevalent;
import com.kynguyen.shop_3hkt.R;
import com.kynguyen.shop_3hkt.ViewHolder.ShowSaveViewHolder;

import io.paperdb.Paper;

public class SavedFragment extends Fragment {
  private View savedView;
  private DatabaseReference refSaved;
  private RecyclerView recyclerViewListSave;
  private RelativeLayout save_show_no_Order;
  private RecyclerView.LayoutManager layoutManagerListSave;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    savedView = inflater.inflate(R.layout.fragment_saves,container, false);
    Paper.init(savedView.getContext());
    mapping();
//    save_show_no_Order.setVisibility(View.INVISIBLE);
    return savedView;
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Prevalent.currentOnLineUsers != null) {
      refSaved = FirebaseDatabase.getInstance().getReference().child("saves");

      FirebaseRecyclerOptions<Saves> options = new FirebaseRecyclerOptions.Builder<Saves>()
          .setQuery(refSaved, Saves.class).build();

      FirebaseRecyclerAdapter<Saves, ShowSaveViewHolder> adapter = new FirebaseRecyclerAdapter<Saves, ShowSaveViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull ShowSaveViewHolder holder, int position, @NonNull Saves model) {

          holder.name.setText(model.getName());
          holder.address.setText(model.getAddress());
          Toast.makeText(savedView.getContext(), "dsafdgfhdjf", Toast.LENGTH_SHORT).show();

        }

        @NonNull
        @Override
        public ShowSaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_save_holder_view,parent,false);
          ShowSaveViewHolder holder = new ShowSaveViewHolder(view);
          return holder;
        }
      };
      recyclerViewListSave.setAdapter(adapter);
      adapter.startListening();
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
