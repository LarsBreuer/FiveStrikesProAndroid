package de.fivestrikes.pro;

import android.app.TabActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class TickerSpielerActivity extends TabActivity {

	String mannschaftId=null;
	String teamHeimId=null;
	String teamAuswId=null;
	String teamHeimString=null;
	String teamAuswString=null;
    String spielId=null;
    String aktionString=null;
    String aktionInt=null;
    String torwartAktionString=null;
    String torwartAktionInt=null;
    String aktionTeamHeim=null;
    String torwartAktionTeamHeim=null;
    String spielerId=null;
    String torwartId=null;
    String spielerString=null;
    String torwartString=null;
    String spielerPosition=null;
    String zeit=null;
    String realzeit=null;
    String tickerId=null;
    String torwartTickerId=null;
    Boolean finish=false;
	View tabview;
	private TabHost mTabHost;
	SQLHelper helper=null;
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.spieler_tab);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
        getWindow().setWindowAnimations(0);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);

/* Daten aus Activity laden */ 
        
        aktionInt=getIntent().getStringExtra("StrAktionInt");
        aktionString=getIntent().getStringExtra("StrAktion");
        aktionTeamHeim=getIntent().getStringExtra("AktionTeamHome"); 
        spielId=getIntent().getStringExtra("GameID");
        zeit=getIntent().getStringExtra("Time");
        realzeit=getIntent().getStringExtra("RealTime");
        teamHeimId=getIntent().getStringExtra("TeamHomeID");
        teamAuswId=getIntent().getStringExtra("TeamAwayID");
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);

/* Daten aus Datenbank laden */
        
		teamHeimString = helper.getTeamHeimKurzBySpielID(spielId);
		teamAuswString = helper.getTeamAuswKurzBySpielID(spielId);
		if(aktionTeamHeim==null){
			aktionTeamHeim = String.valueOf(helper.getSpielBallbesitz(spielId));
		}
        
        if(aktionInt.equals("7")){
        	 customTitleText.setText(R.string.tickerAktionEinwechselung);
        	 aktionString = getString(R.string.tickerAktionEinwechselung);
        } else {
        	customTitleText.setText(aktionString);
        }
        
/* Tabs einrichten */
        
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
	    setupTab(new TextView(this), teamHeimString);
	    setupTab(new TextView(this), teamAuswString);
	    if (aktionTeamHeim.equals("1")){
	    	mTabHost.setCurrentTab(0);
	    } else {
	    	mTabHost.setCurrentTab(1);
	    }
	    
/* Button beschriften */
        
        Button backButton = (Button) findViewById(R.id.back_button);
        
        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });

    }
	
	@Override
	public void onDestroy() {
	  super.onDestroy();

	}

/*
 * 
 * Tabs einrichten 
 *
 */
	
	private void setupTab(final View view, final String tag) {
	    View tabview = createTabView(mTabHost.getContext(), tag);
	    Intent i;
	    TabSpec setContent;
	    
	    if (tag.compareTo(teamHeimString) == 0) {
	    	i = new Intent().setClass(this, TabTickerSpielerHeimActivity.class);
			i.putExtra("StrAktionInt", aktionInt);
			i.putExtra("StrAktion", aktionString);
			i.putExtra("AktionTeamHome", aktionTeamHeim);
			i.putExtra("GameID", spielId);
			i.putExtra("Time", String.valueOf(TickerActivity.elapsedTime));
			i.putExtra("RealTime", realzeit);
			i.putExtra("TeamHomeID", teamHeimId);
			setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(i);
			mTabHost.addTab(setContent);
	    }

	    if (tag.compareTo(teamAuswString) == 0) {
	    	i = new Intent().setClass(this, TabTickerSpielerAuswActivity.class);
			i.putExtra("StrAktionInt", aktionInt);
			i.putExtra("StrAktion", aktionString);
			i.putExtra("AktionTeamHome", aktionTeamHeim);
			i.putExtra("GameID", spielId);
			i.putExtra("Time", String.valueOf(TickerActivity.elapsedTime));
			i.putExtra("RealTime", realzeit);
			i.putExtra("TeamHomeID", teamAuswId);
			setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(i);
			mTabHost.addTab(setContent);
	    }
		
	}

	private static View createTabView(final Context context, final String text) {
	    View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
	    TextView tv = (TextView) view.findViewById(R.id.tabsText);
	    tv.setText(text);
	    return view;
	}

}
