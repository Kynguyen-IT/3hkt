package com.kynguyen.shop_3hkt.ui_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.kynguyen.shop_3hkt.SignInActivity;
import com.kynguyen.shop_3hkt.R;

public class AccountFragment extends Fragment {
  private Button loginBtn;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View viewAcount = inflater.inflate(R.layout.fragment_account,container, false);

    loginBtn = (Button) viewAcount.findViewById(R.id.login_btn);

    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        goToAttract(v);
      }
    });
    return viewAcount;
  }


  public void goToAttract(View v)
  {
    Intent intent = new Intent(getActivity().getApplication(), SignInActivity.class);
    startActivity(intent);
  }



}
