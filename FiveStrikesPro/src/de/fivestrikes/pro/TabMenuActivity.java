package de.fivestrikes.pro;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;


public class TabMenuActivity extends Activity {
	
	public final static String ID_SPIEL_EXTRA="de.fivestrikes.pro.spiel_ID";
	public final static String ID_TEAM_EXTRA="de.fivestrikes.pro.team_ID";
	public final static String ID_AKTIONTEAMHEIM_EXTRA="de.fivestrikes.pro.aktionTeamHeim_ID";
	public final static String ID_ZEIT_EXTRA="de.fivestrikes.pro.zeit_ID";
	public final static String ID_AKTION_EXTRA="de.fivestrikes.pro.aktion_ID";
	public final static String ID_AKTIONINT_EXTRA="de.fivestrikes.pro.aktionInt_ID";
	private static final int GET_CODE = 0;
	SQLHelper helper=null;
	String spielId=null;
	String aktion=null;
	String teamId=null;
	String aktionTeamHeim=null;
	String strAktion=null;
	String strAktionInt=null;
	String zeit=null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.tab_menu);
        
        helper=new SQLHelper(this);
        
        teamId=getIntent().getStringExtra(TickerAktionActivity.ID_TEAM_EXTRA);
        aktionTeamHeim=getIntent().getStringExtra(TickerAktionActivity.ID_AKTIONTEAMHEIM_EXTRA);
        spielId=getIntent().getStringExtra(TickerAktionActivity.ID_SPIEL_EXTRA);
        zeit=getIntent().getStringExtra(TickerAktionActivity.ID_ZEIT_EXTRA);
        
        GridView gridview = (GridView) findViewById(R.id.aktionen_gridview);
        gridview.setAdapter(new AktionenImageAdapter(this));
        
        /**
		 * On Click event for Single Gridview Item
		 * */
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				switch(position) {
        			case 0:
                    	aktion="2";
                    	startAktion(aktion);
        				break;
        			case 1:

        				break;
        			case 2:

        				break;
				}
			}
		});
		
        /**
        final Button aktion_tor_button = (Button) findViewById(R.id.aktion_tor_oben);
        
        aktion_tor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	aktion="2";
            	startAktion(aktion);
            }
        });
        */
    }
    
    public void startAktion(String aktion) {
    	
    	Resources res = getResources();
		Intent newIntent = new Intent(getApplicationContext(), TickerSpielerActivity.class);
		newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
		Cursor c=helper.getSpielById(spielId);
		c.moveToFirst();    
		teamId = helper.getSpielHeim(c);
		c.close();
		newIntent.putExtra(ID_TEAM_EXTRA, teamId);
		aktionTeamHeim="1";
		strAktion=res.getString(R.string.tickerAktionTor);
		strAktionInt="2";
		newIntent.putExtra(ID_AKTIONTEAMHEIM_EXTRA, aktionTeamHeim);
		newIntent.putExtra(ID_ZEIT_EXTRA, String.valueOf(TickerActivity.elapsedTime));
		newIntent.putExtra(ID_AKTION_EXTRA, strAktion);
		newIntent.putExtra(ID_AKTIONINT_EXTRA, strAktionInt);
		startActivityForResult(newIntent, GET_CODE);
    	
		/** Hinweis: Alles einheitlich newIntent oder i */
		
    }
}
