package de.fivestrikes.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.Window;
import android.net.Uri;


public class FiveStrikesProActivity extends Activity {
    /** Called when the activity is first created. */
	Cursor model=null;
	SQLHelper helper=null;
	String spielId=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_main);
        final TextView customTitleText = (TextView) findViewById(R.id.titleMainText);
        customTitleText.setText(R.string.uebersichtTitel);

/*
 * 
 * Menüpunkte einrichten 
 *
 */
	
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				switch(position) {

/* "Mannschaft" starten */ 
        
        			case 0:
        				startActivity(new Intent(getApplicationContext(), MannschaftActivity.class));
        				break;

/* "Spiel" starten */ 
        
        			case 1:
        				startActivity(new Intent(getApplicationContext(), SpielActivity.class));
        				break;

/* "Statistik" starten */ 
        
        			case 2:
        				startActivity(new Intent(getApplicationContext(), StatActivity.class));
        				break;

/* "Handbuch" starten */ 
        
        			case 3:
        				AlertDialog.Builder builder = new AlertDialog.Builder(FiveStrikesProActivity.this);
        				builder
        				.setTitle(R.string.infoMsgboxTitel)
        				.setMessage(R.string.infoMsgboxText)
        				.setIcon(android.R.drawable.ic_dialog_alert)
        				.setPositiveButton(R.string.tickerMSGBoxJa, new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int which) {			      	
        				        
        						Uri uri = Uri.parse( "http://www.fivestrikes.de/FiveStrikes_Handbuch.pdf" );
        						startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
        						
        					}
        				})
        				.setNegativeButton(R.string.tickerMSGBoxNein, new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int which) {			      	
        						
        					}
        				})
        				.show();
        				break;

/* "Schnelles Spiel" starten */ 
        
        			case 4:
        				helper=new SQLHelper(FiveStrikesProActivity.this);
        				helper.createSchnellesSpiel(FiveStrikesProActivity.this);
        				Intent newIntent = new Intent(getApplicationContext(), TickerActivity.class);
        				newIntent.putExtra("GameID", helper.getLastSpielId());
        				startActivity(newIntent);
        				break;

/* "Import / Export" starten */ 
        
        			case 5:
        				startActivity(new Intent(getApplicationContext(), ImportExportActivity.class));
        				break;
					}
			}
		});
    }
    
	@Override
	public void onDestroy() {
	  super.onDestroy();

	}
}
