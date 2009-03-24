package com.lottodroid;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Activity for the main screen. 
 */
public class Lottodroid extends ListActivity {

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);

    /* Sample data */
    ArrayList<Draw> drawList = new ArrayList<Draw>();
    drawList.add(new Bonoloto("Domingo, 01/03/2009"));
    drawList.add(new Lototurf("Domingo, 23/02/2008"));
    drawList.add(new Bonoloto("Domingo, 31/02/2008"));

    setListAdapter(new DrawAdapter(this, drawList));
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    
    Intent i = new Intent(this, DetailsActivity.class);
    startActivity(i);
  }
  
}
