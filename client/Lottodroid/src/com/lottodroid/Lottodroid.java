package com.lottodroid;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lottodroid.communication.LotteryFetcher;
import com.lottodroid.communication.LotteryFetcherFactory;
import com.lottodroid.communication.LotteryInfoUnavailableException;
import com.lottodroid.model.Lottery;
import com.lottodroid.model.LotteryId;
import com.lottodroid.sorting.LotterySorter;
import com.lottodroid.sorting.LotterySorterFactory;
import com.lottodroid.util.UserTask;
import com.lottodroid.view.AboutDialog;
import com.lottodroid.view.ErrorDialog;
import com.lottodroid.view.LotteryViewController;
import com.lottodroid.view.ViewControllerFactory;

/**
 * Activity for the main screen.
 */
public class Lottodroid extends ListActivity {

  public static final String TAG = "Lottodroid";
  
  private static final int ORDER_LOTTERY_MENU_ID = Menu.FIRST;
  private static final int ABOUT_MENU_ID = Menu.FIRST + 1;
  
  private final LotterySorter sorter = LotterySorterFactory.getLotterySorter(this);
  
  private MainViewAdapter adapter;
  private ListView listView;
  
  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);
    this.listView = getListView();

    fetchDataForMainView();
    
    Log.i(TAG, "onCreate");
  }

  /** 
   * Fetches and set the data that the main view will display 
   */
  private void fetchDataForMainView() {
    try {
      new FetchAllLotteryResultsTask().execute();
    } catch (IllegalStateException e) {
      Log.e(TAG, "Fatal error: " + e.getMessage());

      new ErrorDialog(Lottodroid.this, getString(R.string.error_dialog_content)).show();
    }
  }

  /**
   * This method will be called when an item in the list is selected, creating an alert dialog with
   * the options of the lottery type selected.
   */
  @Override
  protected void onListItemClick(ListView l, View v, final int position, long id) {
    super.onListItemClick(l, v, position, id);
    
    startDetailsActivity(position);
    
    // There is only one option
    //
    //    new AlertDialog.Builder(Lottodroid.this).setTitle("Opciones").setItems(
    //        new String[] { "Ver historial" }, new DialogInterface.OnClickListener() {
    //          @Override
    //          public void onClick(DialogInterface dialog, int which) {
    //            // TODO: there is no way that not depend on button positions?
    //            if (which == 0) {
    //              startDetailsActivity(position);
    //            }
    //          }
    //        }).show();
  }

  /** Creates the menu items */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, ORDER_LOTTERY_MENU_ID, 0, "Ordenar sorteos")
      .setIcon(android.R.drawable.ic_menu_sort_alphabetically);
    menu.add(0, ABOUT_MENU_ID, 0, "Acerca de")
      .setIcon(android.R.drawable.ic_menu_info_details);
    return true;
  }

  /** Handles item selections */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case ORDER_LOTTERY_MENU_ID:
      // Launch the subactivity to let the user sort the entries
      Intent i = new Intent(this, SortingActivity.class);
      // TODO(pablo): fix the ugly casting!
      i.putExtra(IntentExtraDataNames.SORTER_IN, (Serializable) sorter.getOrder());
      startActivityForResult(i, 0); // We assume there is only one subactivity
      return true;

    case ABOUT_MENU_ID:
      new AboutDialog(this).show();
      return true;
      
    }
    return false;
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    Log.i(TAG, "Our subactivity has finished.");

    if (resultCode == Activity.RESULT_CANCELED) { // The user pressed the "Back" button
      Log.i(TAG, "The subactivity did not produce any changes");
    } else { // resultCode is probably RESULT_OK
        Log.i(TAG, "The subactivity has changed the order");
        final List<LotteryId> lotteryIds = (List<LotteryId>) intent.getExtras()
          .getSerializable(IntentExtraDataNames.SORTER_OUT);
        sorter.setOrder(lotteryIds);
        adapter.refresh();
        listView.invalidateViews();
        Toast.makeText(this, "Orden guardado con �xito", Toast.LENGTH_SHORT).show();
    }
  }
  
  /** Start the new activity details for the lottery type selected */
  private void startDetailsActivity(int position) {
    Intent i = new Intent(this, DetailsActivity.class);

    Lottery lottery = (Lottery) listView.getItemAtPosition(position);
    
    @SuppressWarnings("unchecked")
    // TODO(pablo): Can I remove this warning?
    LotteryViewController viewController = ViewControllerFactory.createViewController(lottery.getId());
    i.putExtra(IntentExtraDataNames.LOTTERY_VIEW_CONTROLLER, viewController);

    startActivity(i);
  }

  /**
   * Task that fetches the data that the main view will display: the last results for every lottery
   * type.
   */
  private class FetchAllLotteryResultsTask extends UserTask<Void, Void, MainViewAdapter> {

    @Override
    public MainViewAdapter doInBackground(Void... params) {
      MainViewAdapter mainViewAdapter = null;

      try {
        LotteryFetcher dataFetcher = LotteryFetcherFactory.newLotteryFetcher(Lottodroid.this);
        List<Lottery> lotteries = dataFetcher.retrieveLastAllLotteries();
        mainViewAdapter = new MainViewAdapter(Lottodroid.this, lotteries);
        mainViewAdapter.refresh();

      } catch (LotteryInfoUnavailableException e) {
        Log.e(TAG, "Lottery info unavailable", e);
      }

      return mainViewAdapter;
    }

    @Override
    public void end(MainViewAdapter mainViewAdapter) {
      // Adapter set to null if there is an error or an exception thrown
      if (mainViewAdapter == null) {
        new ErrorDialog(Lottodroid.this, getString(R.string.error_dialog_content)).show();
      } else {
        setListAdapter(mainViewAdapter);
        adapter = mainViewAdapter;
      }
    }

  }
}
