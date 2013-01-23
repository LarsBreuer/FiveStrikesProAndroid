package de.fivestrikes.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.Window;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

public class FiveStrikesProActivity extends Activity {
    /** Called when the activity is first created. */
	public final static String ID_SPIEL_EXTRA="de.fivestrikes.pro.spiel_ID";
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
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        
        /**
		 * On Click event for Single Gridview Item
		 * */
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				switch(position) {
        			case 0:
        				startActivity(new Intent(getApplicationContext(), MannschaftActivity.class));
        				break;
        			case 1:
        				startActivity(new Intent(getApplicationContext(), SpielActivity.class));
        				break;
        			case 2:
        				startActivity(new Intent(getApplicationContext(), StatActivity.class));
        				break;
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
        			case 4:
        				helper=new SQLHelper(FiveStrikesProActivity.this);
        				helper.createSchnellesSpiel();
        				Intent newIntent = new Intent(getApplicationContext(), TickerActivity.class);
        				Cursor c=helper.getLastSpielId();
        				c.moveToFirst();
        				spielId = helper.getSpielId(c);
        				c.close();
        				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
        				startActivity(newIntent);
        				break;
        			case 5:
        				AlertDialog.Builder builderWebside = new AlertDialog.Builder(FiveStrikesProActivity.this);
        				builderWebside
        				.setTitle(R.string.importMsgboxTitel)
        				.setMessage(R.string.importMsgboxText)
        				.setIcon(android.R.drawable.ic_dialog_alert)
        				.setPositiveButton(R.string.tickerMSGBoxJa, new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int which) {			      	

        						// File dbfile = new File("/sdcard/database_name.db" ); 
        						// SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        						if(new File("/sdcard/database_name.db").exists()){
        							String dbString = "/sdcard/database_name.db"; 
            						SQLiteDatabase db = SQLiteDatabase.openDatabase(dbString, null, SQLiteDatabase.OPEN_READONLY);
            						helper=new SQLHelper(FiveStrikesProActivity.this);
        							if(db.isOpen()==true){
        								helper.deleteAllTeam();
        								helper.deleteAllSpiel();
        								helper.deleteAllTicker();
        								helper.deleteAllSpieler();
        								Cursor cTeam= db.rawQuery("SELECT * FROM team", null);
        								cTeam.moveToFirst();
        								helper.insertTeamImport(cTeam.getString(1), cTeam.getString(2), Integer.parseInt(cTeam.getString(3)));
        								Cursor lastTeamC=helper.getLastTeamId();
        								lastTeamC.moveToFirst();
        								Integer lastId = Integer.parseInt(helper.getTeamId(lastTeamC))-1;
        								lastTeamC.close();
        								while(cTeam.moveToNext())
        								{
        									helper.insertTeamImport(cTeam.getString(1), cTeam.getString(2), Integer.parseInt(cTeam.getString(3)));
        								}
        								cTeam.close();
        							
        								Cursor cSpieler= db.rawQuery("SELECT * FROM spieler", null);
        								while(cSpieler.moveToNext())
        								{
        									helper.insertSpielerImport(Integer.parseInt(cSpieler.getString(1))+lastId, cSpieler.getString(2), 
        											cSpieler.getString(3), "", Integer.parseInt(cSpieler.getString(5)));
        								}
        								cSpieler.close();
        								
        								/* Cursor cSpiel= db.rawQuery("SELECT * FROM spiel", null);
        								while(cSpiel.moveToNext())
        		                    	{
        									if(cSpiel.getString(7)!=null && cSpiel.getString(8)!=null){
        										helper.insertSpielImport(Integer.parseInt(cSpiel.getString(1)), Integer.parseInt(cSpiel.getString(2)), 
        											Integer.parseInt(cSpiel.getString(3)), Integer.parseInt(cSpiel.getString(4)), 
        											Integer.parseInt(cSpiel.getString(5)), cSpiel.getString(6), Integer.parseInt(cSpiel.getString(7)), 
        											Integer.parseInt(cSpiel.getString(8)));
        									} else {
        										helper.insertSpielImportOhneTore(Integer.parseInt(cSpiel.getString(1)), Integer.parseInt(cSpiel.getString(2)), 
            											Integer.parseInt(cSpiel.getString(3)), Integer.parseInt(cSpiel.getString(4)), 
            											Integer.parseInt(cSpiel.getString(5)), cSpiel.getString(6));
        									}
        		                    	}
        								cSpiel.close();*/
        							
        								/* Cursor cTicker= db.rawQuery("SELECT * FROM ticker", null);
        								while(cTicker.moveToNext())
        		                    	{
        									if(cTicker.getString(6)!=null && cTicker.getString(7)!=null && cTicker.getString(8)!=null){
        										helper.insertTickerImport(Integer.parseInt(cTicker.getString(1)), cTicker.getString(2), 
        												Integer.parseInt(cTicker.getString(3)), 
        												cTicker.getString(4), Integer.parseInt(cTicker.getString(5)), Integer.parseInt(cTicker.getString(6)), 
        												Integer.parseInt(cTicker.getString(7)), cTicker.getString(8), Integer.parseInt(cTicker.getString(11)), 
        												Integer.parseInt(cTicker.getString(13)));
        									} else {
        										helper.insertTickerImportOhneTore(Integer.parseInt(cTicker.getString(1)), cTicker.getString(2), 
        												Integer.parseInt(cTicker.getString(3)), 
        												cTicker.getString(4), Integer.parseInt(cTicker.getString(5)), Integer.parseInt(cTicker.getString(11)), 
        												Integer.parseInt(cTicker.getString(13)));
        									}
        		                    	}
        								cTicker.close();*/
        								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FiveStrikesProActivity.this);
        								alertDialogBuilder
        		    	    				.setTitle(R.string.importFertigMsgboxTitel)
        		    	    				.setMessage(R.string.importFertigMsgboxText)
        		    	    				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        		    	    					public void onClick(DialogInterface dialog,int id) {
        		    	    					
        		    	    					}
        		    	    				});
        								AlertDialog alertDialog = alertDialogBuilder.create();
        								alertDialog.show();
        								}
        							} else {
        								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FiveStrikesProActivity.this);
        								alertDialogBuilder
        		    	    				.setTitle(R.string.importFehlMsgboxTitel)
        		    	    				.setMessage(R.string.importFehlMsgboxText)
        		    	    				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        		    	    					public void onClick(DialogInterface dialog,int id) {
        		    	    					
        		    	    					}
        		    	    				});
        								AlertDialog alertDialog = alertDialogBuilder.create();
        								alertDialog.show();
        							}
        						}
        					})
        					.setNegativeButton(R.string.tickerMSGBoxNein, new DialogInterface.OnClickListener() {
        						public void onClick(DialogInterface dialog, int which) {			      	
        						
        						}
        					})
        				.show();
        				break;
					}
			}
		});
    }
}