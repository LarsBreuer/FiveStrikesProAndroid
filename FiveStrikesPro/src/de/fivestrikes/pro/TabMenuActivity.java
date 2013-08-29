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
	
	public final static String ID_AKTIONINT_EXTRA="de.fivestrikes.pro.aktionInt_ID";
	public final static String ID_AKTION_EXTRA="de.fivestrikes.pro.aktion_ID";
	public final static String ID_AKTIONTEAMHEIM_EXTRA="de.fivestrikes.pro.aktionTeamHeim_ID";
	public final static String ID_SPIEL_EXTRA="de.fivestrikes.pro.spiel_ID";
	public final static String ID_ZEIT_EXTRA="de.fivestrikes.pro.zeit_ID";
	public final static String ID_TEAM_HEIM_EXTRA="de.fivestrikes.pro.teamHeim_ID";
	public final static String ID_TEAM_AUSW_EXTRA="de.fivestrikes.pro.teamAusw_ID";

	private static final int GET_CODE = 0;
	SQLHelper helper=null;
	String spielId=null;
	String aktion=null;
	String teamId=null;
	String teamHeimId=null;
	String teamAuswId=null;
	String aktionTeamHeim=null;
	String strAktion=null;
	String strAktionInt=null;
	String zeit=null;
	String strBallbesitz=null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.tab_menu);
        
        helper=new SQLHelper(this);
        
        teamId=getIntent().getStringExtra(TickerAktionActivity.ID_TEAM_EXTRA);
        spielId=getIntent().getStringExtra(TickerAktionActivity.ID_SPIEL_EXTRA);
        zeit=getIntent().getStringExtra(TickerAktionActivity.ID_ZEIT_EXTRA);
        
		Cursor c=helper.getSpielById(spielId);
		c.moveToFirst();    
		teamHeimId = helper.getSpielHeim(c);
		teamAuswId = helper.getSpielAusw(c);
		if (helper.getSpielBallbesitz (c)!=null){
			strBallbesitz = helper.getSpielBallbesitz (c);
			} else {
			strBallbesitz = "1";
			}
		c.close();
        
        GridView gridview = (GridView) findViewById(R.id.aktionen_gridview);
        gridview.setAdapter(new AktionenImageAdapter(this));
        
        /**
		 * On Click event for Single Gridview Item
		 * */
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				Resources res = getResources();
				
				switch(position) {
    			case 0:
strAktionInt="2";
strAktion=res.getString(R.string.tickerAktionTor);
aktionTeamHeim=strBallbesitz;
startAktion();
    				break;
    			case 1:
strAktionInt="14";
strAktion=res.getString(R.string. tickerAktion7mTor);
aktionTeamHeim=strBallbesitz;
startAktion();
    				break;
    			case 2:
strAktionInt="20";
strAktion=res.getString(R.string. tickerAktionTempogegenstossTor);
aktionTeamHeim=strBallbesitz;
startAktion();
    				break;
    			case 3:
strAktionInt="4";
strAktion=res.getString(R.string. tickerAktionTechnischerFehler);
aktionTeamHeim=strBallbesitz;
startAktion();
    				break;
    			case 4:
strAktionInt="3";
strAktion=res.getString(R.string. tickerAktionFehlwurf);
aktionTeamHeim=strBallbesitz;
startAktion();
    				break;
    			case 5:
strAktionInt="15";
strAktion=res.getString(R.string. tickerAktion7mFehlwurf);
aktionTeamHeim=strBallbesitz;
startAktion();
    				break;
    			case 6:
strAktionInt="21";
strAktion=res.getString(R.string. tickerAktionTempogegenstossFehlwurf);
aktionTeamHeim=strBallbesitz;
startAktion();
    				break;
    			case 7:
strAktionInt="5";
strAktion=res.getString(R.string. tickerAktionZweiMinuten);
if (strBallbesitz.equals("1")){
aktionTeamHeim = "0";
} else {
aktionTeamHeim = "1";
}

startAktion();
    				break;
    			case 8:
strAktionInt="7";
strAktion=res.getString(R.string. tickerAktionEinwechselung);
startAktion();
    				break;
    			case 9:
strAktionInt="12";
strAktion=res.getString(R.string. tickerAktionStartaufstellung);
startAktion();
    				break;
    			case 10:
strAktionInt="6";
strAktion=res.getString(R.string. tickerAktionGelbeKarte);
if (strBallbesitz.equals("1")){
aktionTeamHeim = "0";
} else {
aktionTeamHeim = "1";
}
startAktion();
    				break;
    			case 11:
strAktionInt="9";
strAktion=res.getString(R.string. tickerAktionRoteKarte);
if (strBallbesitz.equals("1")){
aktionTeamHeim = "0";
} else {
aktionTeamHeim = "1";
}
startAktion();
    				break;
    			case 12:
strAktionInt="11";
strAktion=res.getString(R.string. tickerAktionZweiMalZwei);
if (strBallbesitz.equals("1")){
aktionTeamHeim = "0";
} else {
aktionTeamHeim = "1";
}
startAktion();
    				break;
    			case 13:
strAktionInt="24";
strAktion=res.getString(R.string. tickerAktionAuszeit);
startAktion();
    				break;
    			case 14:
strAktionInt="24";
strAktion=res.getString(R.string. tickerAktionAuszeit);
startAktion();
    				break;


				}
			}
		});
		
    }
    
    public void startAktion() {
    	
		Intent newIntent = new Intent(getApplicationContext(), TickerSpielerActivity.class);
		newIntent.putExtra(ID_AKTIONINT_EXTRA, strAktionInt);
		newIntent.putExtra(ID_AKTION_EXTRA, strAktion);
		newIntent.putExtra(ID_AKTIONTEAMHEIM_EXTRA, aktionTeamHeim);
		newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
		newIntent.putExtra(ID_ZEIT_EXTRA, String.valueOf(TickerActivity.elapsedTime));
		newIntent.putExtra(ID_TEAM_HEIM_EXTRA, teamHeimId);
		newIntent.putExtra(ID_TEAM_AUSW_EXTRA, teamAuswId);

		startActivityForResult(newIntent, GET_CODE);
    	
		/** Hinweis: Alles einheitlich newIntent oder i */
		
    }
}
