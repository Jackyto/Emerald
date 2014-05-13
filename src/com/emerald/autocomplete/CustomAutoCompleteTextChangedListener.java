package com.emerald.autocomplete;

import com.emerald.MusicManager;
import com.emerald.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{
	public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
    CustomAutoCompleteView view;
    AutoCompleteSongArrayAdapter adapter;
    String tmp;
     
    public CustomAutoCompleteTextChangedListener(Context context, CustomAutoCompleteView autoComplete, AutoCompleteSongArrayAdapter adapter){
        this.context = context;
        this.view = autoComplete;
        this.adapter = adapter;
        this.tmp = new String();
    }
     
    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    	if (s.toString().contentEquals(tmp))
            MusicManager.generateSearchList(s.toString()); 
    }
 
    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
 
        try{
            adapter = new AutoCompleteSongArrayAdapter(context, R.layout.song_row, MusicManager.getSearchList());
             
            view.setAdapter(adapter);
            tmp = userInput.toString();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
}
