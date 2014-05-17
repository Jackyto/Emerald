package com.emerald.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.emerald.R;
import com.emerald.RemoteActivity;

public class RemoteFragment extends Fragment {
	EditText		ipField;
	
	public static RemoteFragment newInstance() {
		RemoteFragment fragment = new RemoteFragment();
		return fragment;
	}

	public RemoteFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getApplicationContext();
		View rootView = null;

		rootView = inflater.inflate(R.layout.remote_frag, container, false);
		Button ok = (Button) rootView.findViewById(R.id.connection);
		ipField = (EditText) rootView.findViewById(R.id.ipEditText);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity().getApplicationContext(), RemoteActivity.class);
				Bundle b = new Bundle();
				b.putString("IP", ipField.getText().toString());
				intent.putExtras(b); 
				startActivity(intent);	
			}
		});
		return rootView;
	}
}
