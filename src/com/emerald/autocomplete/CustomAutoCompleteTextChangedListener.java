package com.emerald.autocomplete;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{
	public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
	Context context;
	AutoCompleteTextView view;
	AutoCompleteSongArrayAdapter adapter;

	public CustomAutoCompleteTextChangedListener(Context context, AutoCompleteTextView autoComplete, AutoCompleteSongArrayAdapter adapter){
		this.context = context;
		this.view = autoComplete;
		this.adapter = adapter;
	}

	@Override
	public void afterTextChanged(Editable s) {
		adapter.getFilter().filter(s);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence userInput, int start, int before, int count) {

	}
}
