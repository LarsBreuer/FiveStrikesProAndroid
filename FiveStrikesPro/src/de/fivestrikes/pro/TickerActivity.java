package de.fivestrikes.pro;


import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.LinearLayout;
import android.view.View.OnLongClickListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;


public class TickerActivity extends TabActivity {    
    /** Called when the activity is first created. */
	public final static String ID_SPIEL_EXTRA="de.fivestrikes.pro.spiel_ID";
	public final static String ID_TEAM_EXTRA="de.fivestrikes.pro.team_ID";
	public final static String ID_AKTIONTEAMHEIM_EXTRA="de.fivestrikes.pro.aktionTeamHeim_ID";
	public final static String ID_ZEIT_EXTRA="de.fivestrikes.pro.zeit_ID";
	public static Long elapsedTime;
	private static final int GET_CODE = 0;
	Cursor model=null;
	SQLHelper helper=null;
	String spielId=null;
	String teamId=null;
	String aktionTeamHeim=null;
	private long zeitStart=0;
	private Handler mHandler = new Handler();
	private long startTime;
	private final int REFRESH_RATE = 100;
	private boolean stopped = false;
	final Context context = this;
	View tabview;
	private TabHost mTabHost;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.ticker);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back_info);
        getWindow().setWindowAnimations(0);
        
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackInfoText);
        customTitleText.setText(R.string.tickerTitel);
        
        spielId=getIntent().getStringExtra(SpielEditActivity.ID_SPIEL_EXTRA);
        helper=new SQLHelper(this);
        model=helper.getAllTicker(spielId);
        startManagingCursor(model);
        
        /* Tabs einrichten */
		Resources res = getResources();
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
	    setupTab(new TextView(this), res.getString(R.string.aktionen));
	    setupTab(new TextView(this), res.getString(R.string.ticker));
	    setupTab(new TextView(this), res.getString(R.string.statistik));
	    
	    /* Button einrichten */
        Button backButton=(Button) findViewById(R.id.back_button);
        Button infoButton = (Button) findViewById(R.id.info_button);
        Button btnTeamHeim=(Button) findViewById(R.id.btn_heim);
        Button btnTeamAusw=(Button) findViewById(R.id.btn_auswaerts);
        Button btnToreHeim=(Button) findViewById(R.id.btn_tore_heim);
        Button btnToreAusw=(Button) findViewById(R.id.btn_tore_auswaerts);
        Button btn_uhr=(Button) findViewById(R.id.btn_uhr);
        
    	/* Tore eingeben */
        btnToreHeim.setText(String.valueOf(helper.countTickerTore(spielId, "1", "9999999")));
        btnToreAusw.setText(String.valueOf(helper.countTickerTore(spielId, "0", "9999999")));
    	
        Cursor c=helper.getSpielCursor(spielId);
		c.moveToFirst();
		btnTeamHeim.setText(helper.getTeamHeimKurzBySpielID(c));
		btnTeamAusw.setText(helper.getTeamAuswKurzBySpielID(c));

		/* Zeit stellen */
		if (helper.getSpielZeitStart(c)!=null) zeitStart=Long.parseLong(helper.getSpielZeitStart(c));
		if (helper.getSpielZeitBisher(c)!=null) elapsedTime=Long.parseLong(helper.getSpielZeitBisher(c));
    	if(zeitStart==0){		// wenn Zeit beim letzten Verlassen gestoppt war
    		((TextView)findViewById(R.id.btn_uhr)).setText(helper.updateTimer(elapsedTime));
    		stopped = true;
    		btn_uhr.setBackgroundResource(R.drawable.buttonuhrrot);
    	}
    	else{
    		elapsedTime = elapsedTime + System.currentTimeMillis() - zeitStart;
    		startTime = System.currentTimeMillis() - elapsedTime; 
        	mHandler.removeCallbacks(startTimer);
            mHandler.postDelayed(startTimer, 0);
            stopped = false;
            ((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrgruen);
    	}
    	
    	/* Button Ballbesitz stellen */
    	switch(Integer.parseInt(helper.getSpielBallbesitz(c))){
			case 1:
				btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim_ball);
				btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts);
				break;
			case 0:
				btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts_ball);
				btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim);
				break;
			case 2:
				btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim);
				btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts);
				break;
    	}
    	/** Hinweis: Vielleicht in eigene Methode ausgliedern */
		c.close();
		
        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });
        
        /* Button Info */
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    	    	alertDialogBuilder
    	    		.setTitle(R.string.tickerInfoMsgboxTitel)
    	    		.setMessage(R.string.tickerInfoMsgboxText)
    	    		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {

    					}
    	    		});
    	    	AlertDialog alertDialog = alertDialogBuilder.create();
    	    	alertDialog.show();
            }
        });
        
        /* Button Aktion Spieler Heim */
        btnToreHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				Intent newIntent = new Intent(getApplicationContext(), TickerAktionActivity.class);
				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
				Cursor c=helper.getSpielById(spielId);
				c.moveToFirst();    
				teamId = helper.getSpielHeim(c);
				c.close();
				newIntent.putExtra(ID_TEAM_EXTRA, teamId);
				aktionTeamHeim="1";
				newIntent.putExtra(ID_AKTIONTEAMHEIM_EXTRA, aktionTeamHeim);
				newIntent.putExtra(ID_ZEIT_EXTRA, String.valueOf(elapsedTime));
				startActivityForResult(newIntent, GET_CODE);
				/** Hinweis: Alles einheitlich newIntent oder i */
            }
        });
        
        /* Button Aktion Spieler Auswärts */
        btnToreAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				Intent newIntent = new Intent(getApplicationContext(), TickerAktionActivity.class);
				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
				Cursor c=helper.getSpielById(spielId);
				c.moveToFirst();    
				teamId = helper.getSpielAusw(c);
				c.close();
				newIntent.putExtra(ID_TEAM_EXTRA, teamId);
				aktionTeamHeim="0";
				newIntent.putExtra(ID_AKTIONTEAMHEIM_EXTRA, aktionTeamHeim);
				newIntent.putExtra(ID_ZEIT_EXTRA, String.valueOf(elapsedTime));
				startActivityForResult(newIntent, GET_CODE);
            }
        });
 
        /* Button Team Heim */
        btnTeamHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button btnTeamHeim=(Button) findViewById(R.id.btn_heim);
                Button btnTeamAusw=(Button) findViewById(R.id.btn_auswaerts);
            	Cursor c=helper.getSpielCursor(spielId);
        		c.moveToFirst();
        		if (Integer.parseInt(helper.getSpielBallbesitz(c))!=1){
        			btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim_ball);
        			btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts);
        			String strBallbesitz="Ballbesitz " + helper.getTeamHeimKurzBySpielID(c);
        			helper.insertTicker(0, strBallbesitz, 1, "", 0, Integer.parseInt(spielId), (int) (long) elapsedTime);
        			// LISTVIEW => adapter.getCursor().requery();  // ListView aktualisieren
        			helper.updateSpielBallbesitz(spielId, 1);  // aktuellen Ballbesitz in Spiel eintragen
        		}
        		c.close();
            }
        });
        
        /* Button Team Auswärts */
        btnTeamAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button btnTeamHeim=(Button) findViewById(R.id.btn_heim);
                Button btnTeamAusw=(Button) findViewById(R.id.btn_auswaerts);
            	Cursor c=helper.getSpielCursor(spielId);
        		c.moveToFirst();
        		if (Integer.parseInt(helper.getSpielBallbesitz(c))!=0){
        			btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim);
        			btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts_ball);
    				String strBallbesitz="Ballbesitz " + helper.getTeamAuswKurzBySpielID(c);
    				helper.insertTicker(1, strBallbesitz, 0, "", 0, Integer.parseInt(spielId), (int) (long) elapsedTime);
    				// LISTVIEW => adapter.getCursor().requery();   // ListView aktualisieren
    				helper.updateSpielBallbesitz(spielId, 0);  // aktuellen Ballbesitz in Spiel eintragen
        		}
        		c.close();
				
            }
        });
        
        /* Uhr stellen */
        btn_uhr.setOnLongClickListener(new OnLongClickListener() { 
            @Override
            public boolean onLongClick(View v) {
				// Stopuhr stoppen 
        		startTime = System.currentTimeMillis();
            	mHandler.removeCallbacks(startTimer);
            	stopped = true;
            	((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrrot);
            	helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
            	
            	// get prompts.xml view
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.ticker_set_watch, null);
            	
            	// Toast.makeText(TickerActivity.this, "Yes button pressed", Toast.LENGTH_SHORT).show();
            	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);

				final EditText editMinutes = (EditText) promptsView.findViewById(R.id.setStopWatchMinutes);
				final EditText editSeconds = (EditText) promptsView.findViewById(R.id.setStopWatchSeconds);

				// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton(R.string.tickerMSGBoxEinstellen,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										int min=0;
										int sec=0;
										if(editMinutes.getText().length() != 0){
											min=Integer.parseInt(editMinutes.getText().toString());
										}
										else{
											min=0;
										}
										if(editSeconds.getText().length() != 0){
											sec=Integer.parseInt(editSeconds.getText().toString());
										}
										else{
											sec=0;
										}
										if (sec>59) {
											sec=59;
										}
										elapsedTime=(long) (min*60000)+(sec*1000);
										((TextView)findViewById(R.id.btn_uhr)).setText(helper.updateTimer(elapsedTime));
						            	/* Schreibe in die Datenbank, in welcher Halbzeit man sich befindet */
							            Cursor c=helper.getSpielCursor(spielId);
							    		c.moveToFirst();
						            	if(min<Integer.parseInt(helper.getSpielHalbzeitlaenge(c))){
						            		helper.updateSpielAktuelleHalbzeit(spielId, 0);
						            	}
						            	if(min<=Integer.parseInt(helper.getSpielHalbzeitlaenge(c))*2 && min>=30){
						            		helper.updateSpielAktuelleHalbzeit(spielId, 1);
						            	}
						            	if(min>Integer.parseInt(helper.getSpielHalbzeitlaenge(c))*2){
						            		helper.updateSpielAktuelleHalbzeit(spielId, 0);
						            	}
						            	c.close();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
                return true;
            }
        });

    }
	
	/**
	* This method is called when the sub-activity returns the result back to the
	* main activity.
	*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == GET_CODE){
			if (resultCode == RESULT_OK) {
				
				/* Spielergebnis auf die Button schreiben */
				Button btnToreHeim=(Button)findViewById(R.id.btn_tore_heim);
				Button btnToreAusw=(Button)findViewById(R.id.btn_tore_auswaerts);
				Cursor c=helper.getSpielById(spielId);
				c.moveToFirst();
				if(helper.getSpielToreHeim(c)!=null){
					btnToreHeim.setText(helper.getSpielToreHeim(c));
				}else{
					btnToreHeim.setText("0");
				}
				if(helper.getSpielToreAusw(c)!=null){
					btnToreAusw.setText(helper.getSpielToreAusw(c));
				}else{
					btnToreAusw.setText("0");
				}
		    	/* Button Ballbesitz stellen */
		        Button btnTeamHeim=(Button) findViewById(R.id.btn_heim);
		        Button btnTeamAusw=(Button) findViewById(R.id.btn_auswaerts);
		    	switch(Integer.parseInt(helper.getSpielBallbesitz(c))){
		    		case 1:
		    			btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim_ball);
		    			btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts);
		    			break;
		    		case 0:
		    			btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts_ball);
		    			btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim);
		    			break;
		    		case 2:
		    			btnTeamHeim.setBackgroundResource(R.drawable.mannschaft_heim);
		    			btnTeamAusw.setBackgroundResource(R.drawable.mannschaft_auswaerts);
		    			break;
		    	}
				/* Nachprüfen, ob während der Eingabe die Halbzeit oder das Spielende erreicht wurde */
		    	if(!stopped){	// Überprüfung nur notwenidg, wenn Zeit nicht gestoppt
					/* Wenn noch 1. Halbzeit und Zeit über der Halbzeitlänge */
					if(Integer.parseInt(helper.getSpielAktuelleHalbzeit(c))==0 &&
							((System.currentTimeMillis() - startTime)/60000)>Integer.parseInt(helper.getSpielHalbzeitlaenge(c))){
						/* Dann Uhrzeit stoppen */
		        		startTime = System.currentTimeMillis();
		            	mHandler.removeCallbacks(startTimer);
		            	stopped = true;
		            	((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrrot);
		            	/* Stoppuhr auf Halbzeit stellen */
						elapsedTime=(long) (Integer.parseInt(helper.getSpielHalbzeitlaenge(c))*60000);
						((TextView)findViewById(R.id.btn_uhr)).setText(helper.updateTimer(elapsedTime));
		            	helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
						/** Hinweis: Stoppen der Uhr in Funktion ausgliedern */
					}
					/* Wenn noch 2. Halbzeit und Zeit über Spielende */
					if(Integer.parseInt(helper.getSpielAktuelleHalbzeit(c))==1 &&
							((System.currentTimeMillis() - startTime)/60000)>(Integer.parseInt(helper.getSpielHalbzeitlaenge(c))*2)){
						/* Dann Uhrzeit stoppen */
		        		startTime = System.currentTimeMillis();
		            	mHandler.removeCallbacks(startTimer);
		            	stopped = true;
		            	((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrrot);
		            	/* Stoppuhr auf Halbzeit stellen */
						elapsedTime=(long) (Integer.parseInt(helper.getSpielHalbzeitlaenge(c))*2*60000);
						((TextView)findViewById(R.id.btn_uhr)).setText(helper.updateTimer(elapsedTime));
		            	helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
						/** Hinweis: Stoppen der Uhr in Funktion ausgliedern */
					}
				}
				c.close();
			}
		}
	}
	
	@Override
	public void onDestroy() {
	  super.onDestroy();
	  
	  /** Aktuelle Zeit auf der Stoppuhr in Datenbank übertragen */
	  if(stopped){
		  zeitStart=0;
	  }
	  else{
		  zeitStart=System.currentTimeMillis();
	  }
	  helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
	  
	  helper.close();
	}
	
	//
	// Stoppuhr Start
	//
	
    public void uhrClick (View view){
    	if(stopped){
    		startTime = System.currentTimeMillis() - elapsedTime; 
        	mHandler.removeCallbacks(startTimer);
            mHandler.postDelayed(startTimer, 0);
            stopped = false;
            helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
            ((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrgruen);
    	}
    	else{
    		startTime = System.currentTimeMillis();
        	mHandler.removeCallbacks(startTimer);
        	stopped = true;
        	helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
        	((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrrot);
    	}
    }
	
    private Runnable startTimer = new Runnable() {
    	public void run() {
    		elapsedTime = System.currentTimeMillis() - startTime;
    		((TextView)findViewById(R.id.btn_uhr)).setText(helper.updateTimer(elapsedTime));
    		mHandler.postDelayed(this,REFRESH_RATE);
            Cursor c=helper.getSpielCursor(spielId);
    		c.moveToFirst();
    		/* Uhr stoppen bei Halbzeitpause */
    		if(elapsedTime>Integer.parseInt(helper.getSpielHalbzeitlaenge(c))*60*1000 &&
    				Integer.parseInt(helper.getSpielAktuelleHalbzeit(c))==0){
        		/* Stoppe die Zeit */
    			startTime = System.currentTimeMillis();
            	mHandler.removeCallbacks(startTimer);
            	stopped = true;
            	helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
            	/* Schreibe in die Datenbank, dass die zweite Halbzeit begonnen hat */
            	helper.updateSpielAktuelleHalbzeit(spielId, 1);
            	((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrrot);
            	/** Hinweis: Stopp-Funktion eventuell in eigene Methode ausgliedern*/
    		}
    		/* Uhr stoppen bei Spielende */
    		if(elapsedTime>Integer.parseInt(helper.getSpielHalbzeitlaenge(c))*2*60*1000 &&
    				Integer.parseInt(helper.getSpielAktuelleHalbzeit(c))==1){
        		/* Stoppe die Zeit */
    			startTime = System.currentTimeMillis();
            	mHandler.removeCallbacks(startTimer);
            	stopped = true;
            	helper.updateSpielTicker(spielId, elapsedTime, zeitStart);
            	/* Schreibe in die Datenbank, dass die zweite Halbzeit begonnen hat */
            	helper.updateSpielAktuelleHalbzeit(spielId, 2);
            	((Button)findViewById(R.id.btn_uhr)).setBackgroundResource(R.drawable.buttonuhrrot);
            	/** Hinweis: Stopp-Funktion eventuell in eigene Methode ausgliedern*/
    		}
    		c.close();
    	}
	};

	
	//
	// Stoppuhr Ende
	//
	
	//
	// Tabs einrichten Start
	//
	
	private void setupTab(final View view, final String tag) {
	    View tabview = createTabView(mTabHost.getContext(), tag);
	    Resources res = getResources(); 
	    Intent tabIntent;
	    TabSpec setContent;
	    
	    if (tag.compareTo(res.getString(R.string.aktionen)) == 0) {
	    	tabIntent = new Intent().setClass(this, TabMenuActivity.class);
	    	tabIntent.putExtra(ID_SPIEL_EXTRA, spielId);
			Cursor cMenu=helper.getSpielById(spielId);
			cMenu.moveToFirst();    
			teamId = helper.getSpielHeim(cMenu);
			cMenu.close();
			tabIntent.putExtra(ID_TEAM_EXTRA, teamId);
			aktionTeamHeim="1";
			tabIntent.putExtra(ID_AKTIONTEAMHEIM_EXTRA, aktionTeamHeim);
			tabIntent.putExtra(ID_ZEIT_EXTRA, String.valueOf(elapsedTime));
			setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(tabIntent);
			mTabHost.addTab(setContent);
	    }

	    if (tag.compareTo(res.getString(R.string.ticker)) == 0) {
	    	tabIntent = new Intent().setClass(this, TabListActivity.class);
	    	tabIntent.putExtra(ID_SPIEL_EXTRA, spielId);
			setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(tabIntent);
			mTabHost.addTab(setContent);
	    }
	    
	    if (tag.compareTo(res.getString(R.string.statistik)) == 0) {
	    	tabIntent = new Intent().setClass(this, TabStatisticActivity.class);
			setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(tabIntent);
			mTabHost.addTab(setContent);
	    }
		
	}

	private static View createTabView(final Context context, final String text) {
	    View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
	    TextView tv = (TextView) view.findViewById(R.id.tabsText);
	    tv.setText(text);
	    return view;
	}

	//
	// Tabs einrichten Ende
	//
	
}
