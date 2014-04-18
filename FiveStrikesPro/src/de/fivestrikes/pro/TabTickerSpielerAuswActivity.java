package de.fivestrikes.pro;

import android.app.ListActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class TabTickerSpielerAuswActivity extends ListActivity {
	
	Cursor model=null;
    Cursor c=null;
	SpielerAdapter adapter=null;
	SQLHelper helper=null;
	String mannschaftId=null;
	String teamHeimId=null;
	String teamAuswId=null;
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
    Integer intSpielBallbesitz=null;
    String strTeamHeimKurzBySpielID=null;
    String strTeamAuswKurzBySpielID=null;
    String strSpielTorwartHeim=null;
    String strSpielTorwartAuswaerts=null;
    String strSpielSpielerWurfecke=null;
    int zeitZurueck=0;
    int halbzeitlaenge=0;
    Boolean finish=false;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
        setContentView(R.layout.tab_spieler_heim);

/* Daten aus Activity laden */ 
     
        mannschaftId=getIntent().getStringExtra("TeamHomeID");
        aktionInt=getIntent().getStringExtra("StrAktionInt");
        aktionString=getIntent().getStringExtra("StrAktion");
        aktionTeamHeim="0"; 
        spielId=getIntent().getStringExtra("GameID");
        zeit=getIntent().getStringExtra("Time");
        realzeit=getIntent().getStringExtra("RealTime");
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);
        model=helper.getAllSpieler(mannschaftId);
        startManagingCursor(model);
        adapter=new SpielerAdapter(model);
        setListAdapter(adapter);

/* Daten aus Datenbank laden */
        
        intSpielBallbesitz= Integer.parseInt(helper.getSpielBallbesitz(spielId));
        strSpielTorwartHeim = helper.getSpielTorwartHeim(spielId);
    	strSpielTorwartAuswaerts = helper.getSpielTorwartAuswaerts(spielId);
    	strSpielSpielerWurfecke = helper.getSpielSpielerWurfecke(spielId);
	    halbzeitlaenge=Integer.parseInt(helper.getSpielHalbzeitlaenge(spielId))*60*2000;
	    strTeamHeimKurzBySpielID = helper.getTeamHeimKurzBySpielID(spielId);
        strTeamAuswKurzBySpielID = helper.getTeamAuswKurzBySpielID(spielId);
        if(Integer.parseInt(aktionTeamHeim)==1){
			if(strSpielTorwartAuswaerts!=null){
				torwartId=strSpielTorwartAuswaerts;    
				torwartString=helper.getSpielerName(torwartId);
			}
		}
		if(Integer.parseInt(aktionTeamHeim)==0){
			if(strSpielTorwartHeim!=null){
				torwartId=strSpielTorwartHeim;  
				torwartString=helper.getSpielerName(torwartId);
			}
		}
    }
	
	@Override
	public void onDestroy() {
	  super.onDestroy();
	    
	}

/*
 * 
 * Abfrage, welcher Spieler ausgew�hlt wurde 
 *
 */
	
	@Override
	public void onListItemClick(ListView list, View view,
            int position, long id) {
		
/* Spielernamen anhand Spieler ID erhalten und neuen Ticker einf�gen */
		
		int maxZeit = helper.maxTickerZeit(spielId); 	// Zeit der letzten Spielaktion des Spiel ermitteln
		spielerId=String.valueOf(id);    
		spielerString=helper.getSpielerName(spielerId);
		spielerPosition=helper.getSpielerPosition(spielerId);
		helper.insertTicker(Integer.parseInt(aktionInt), aktionString, Integer.parseInt(aktionTeamHeim), spielerString, 
				Integer.parseInt(spielerId), Integer.parseInt(spielId), Integer.parseInt(zeit), realzeit);
		c=helper.getLastTickerId();
		c.moveToFirst();
		tickerId = helper.getTickerId(c);
		c.close();
		helper.updateSpielErgebnis(Integer.parseInt(spielId));
	    finish=false;
	    
/* Aktion Tor einf�gen */
	   
    	if(Integer.parseInt(aktionInt)==2 || 
    			Integer.parseInt(aktionInt)==14 || 
    			Integer.parseInt(aktionInt)==20){
    		
    		/* �nderung des Ballbesitzes */
    		helper.changeBallbesitz(aktionTeamHeim, intSpielBallbesitz, strTeamHeimKurzBySpielID, strTeamAuswKurzBySpielID, 
    				spielId, zeit, realzeit);   		

   			/* �berpr�fen, ob bei gegnerischer Mannschaft ein Torwart angegeben wurde. */
   			/* Falls ja, f�r den Torwart ein Gegentor eintragen. */
    		if(torwartId!=null){
				if(aktionInt.equals("2")){
					torwartAktionInt="17";
					torwartAktionString=getString(R.string.tickerAktionTorwartGegentor);
				}
				if(aktionInt.equals("14")){
					torwartAktionInt="19";
					torwartAktionString=getString(R.string.tickerAktionTorwart7mGegentor);
				}
				if(aktionInt.equals("20")){
					torwartAktionInt="23";
					torwartAktionString=getString(R.string.tickerAktionTorwartTGGegentor);
				}
				if(aktionTeamHeim.equals("0")){
					torwartAktionTeamHeim="1";
				} else {
					torwartAktionTeamHeim="0";
				}
				helper.insertTicker(Integer.parseInt(torwartAktionInt), torwartAktionString, Integer.parseInt(torwartAktionTeamHeim), torwartString, 
						Integer.parseInt(torwartId), Integer.parseInt(spielId), Integer.parseInt(zeit), realzeit);
				Cursor lastTickTorwartC=helper.getLastTickerId();
				lastTickTorwartC.moveToFirst();
				torwartTickerId = helper.getTickerId(lastTickTorwartC);
				lastTickTorwartC.close();
    		} 

    		/* Spielstand in Tickereintr�ge schreiben falls Tor geworfen wurde */
   			/* Wenn es Tickereintr�ge nach dem aktuellen Eintrag gibt, �ndere die Torfolge bei den Eintr�gen */
			String[] args={spielId};
			SQLiteDatabase db=helper.getWritableDatabase();
			Cursor cTicker=db.rawQuery("SELECT * FROM ticker WHERE spielID=? ORDER BY zeitInteger ASC", args);
   			cTicker.moveToFirst(); 
			String strErgebnis=null;
   			if(Integer.parseInt(zeit)<=maxZeit){	
   				int toreHeim=0;
   				int toreAusw=0;
   				for (cTicker.moveToFirst(); !cTicker.isAfterLast(); cTicker.moveToNext()) {
   					if(Integer.parseInt(helper.getTickerAktionInt(cTicker))==2 || 
   							Integer.parseInt(helper.getTickerAktionInt(cTicker))==14 || 
   							Integer.parseInt(helper.getTickerAktionInt(cTicker))==20){
   						if(Integer.parseInt(helper.getTickerAktionTeamHeim(cTicker))==1){
   							toreHeim=toreHeim+1;
   						}
   						if(Integer.parseInt(helper.getTickerAktionTeamHeim(cTicker))==0){
   							toreAusw=toreAusw+1;
   						}
   					}
   					if(Integer.parseInt(helper.getTickerZeitInt(cTicker))>=Integer.parseInt(zeit)){	// Wenn die Zeit der Schleifeneintrags gr��er ist als der aktuelle Eintrag, dann hat sich die Torfolge ge�ndert und der Eintrag muss ge�ndert werden
   						strErgebnis=String.valueOf(toreHeim)+":"+String.valueOf(toreAusw);
   						helper.updateTickerErgebnis(helper.getTickerId(cTicker), toreHeim, toreAusw, strErgebnis);
   					}
   				}
   			} else {
   				strErgebnis=String.valueOf(helper.countTickerTore(spielId, "1", "9999999"))+
   							":"+
   							String.valueOf(helper.countTickerTore(spielId, "0", "9999999"));
   				helper.updateTickerErgebnis(tickerId, 
   											helper.countTickerTore(spielId, "1", "9999999"), 
   											helper.countTickerTore(spielId, "0", "9999999"), 
   											strErgebnis);
   			}
   			cTicker.close();
   			
   			if(strSpielSpielerWurfecke!=null){
   				if(strSpielSpielerWurfecke.equals("1")){
   					/* Activity starten um Wurfecke und Wurfposition einzugeben */
   					Intent newIntent = new Intent(getApplicationContext(), TickerSpielerWurfeckeActivity.class);
   					newIntent.putExtra("TickerID", tickerId);
   					if(torwartTickerId!=null){
   						newIntent.putExtra("TickerTorwartID", torwartTickerId);		
   					}
   					startActivity(newIntent);
    			
   					/** Hinweis: Bislang geht die Activity direkt zur�ck auf die Ticker Activity (�ber TickerAktionActivity).
   					 *  Dies geschieht auch bei dem Back-Button. Einrichten, dass man bei der Auswahl zur�ck zum Ticker kommt und 
   					 *  bei dem Back-Button nur zur�ck zur TickerSpielerActivity.  
   					 */
   					finish=true;
   					Intent i=new Intent();
   					setResult(RESULT_OK, i);
   					finish();
   				}
   			}	
    	}
    	
/* Aktion Fehlwurf einf�gen */
    	
    	if(Integer.parseInt(aktionInt)== 3 || 
    			Integer.parseInt(aktionInt)==15 || 
    			Integer.parseInt(aktionInt)==21){
    		if(strSpielSpielerWurfecke!=null){
   				if(strSpielSpielerWurfecke.equals("1")){
   					/* Activity starten um Wurfecke und Wurfposition einzugeben */
   					Intent newIntent = new Intent(getApplicationContext(), TickerSpielerFehlwurfActivity.class);
   					newIntent.putExtra("TickerID", tickerId);
   					newIntent.putExtra("StrAktionInt", aktionInt);
   					newIntent.putExtra("GameID", spielId);
   					newIntent.putExtra("AktionTeamHome", aktionTeamHeim);
   					newIntent.putExtra("Time", zeit);
   					newIntent.putExtra("RealTime", realzeit);
   					startActivity(newIntent);
    			
   					/** Hinweis: Bislang geht die Activity direkt zur�ck auf die Ticker Activity (�ber TickerAktionActivity).
   					 *  Dies geschieht auch bei dem Back-Button. Einrichten, dass man bei der Auswahl zur�ck zum Ticker kommt und 
   					 *  bei dem Back-Button nur zur�ck zur TickerSpielerActivity.  
   					 */
   					finish=true;
   					Intent i=new Intent();
   					setResult(RESULT_OK, i);
   					finish();
   				} else {
   					finish=true;
   		    		AlertDialog.Builder tfBuilder = new AlertDialog.Builder(this);
   		   			tfBuilder
   		   			.setTitle(R.string.tickerMSGBoxAktionTitel)
   		   			.setMessage(R.string.tickerMSGBoxAktionNachricht)
   		   			.setIcon(android.R.drawable.ic_dialog_alert)
   		   			.setPositiveButton(R.string.tickerMSGBoxJa, new DialogInterface.OnClickListener() {
   		   				public void onClick(DialogInterface dialog, int which) {			      	
   		   					/* �nderung des Ballbesitzes */
   		   					helper.changeBallbesitz(aktionTeamHeim, intSpielBallbesitz, strTeamHeimKurzBySpielID, strTeamAuswKurzBySpielID, 
   		   	    				spielId, zeit, realzeit); 
   		   					finish();
   		   				}
   		   			})
   		   			.setNegativeButton(R.string.tickerMSGBoxNein, new DialogInterface.OnClickListener() {
   		   				public void onClick(DialogInterface dialog, int which) {
   		   					finish();
   		   				}
   		   			})
   		   			.show();
   				}
    		}
    	}
    	
/* Technischer Fehler einf�gen */
    	
    	if(Integer.parseInt(aktionInt)== 4){
   			finish=true;
    		AlertDialog.Builder tfBuilder = new AlertDialog.Builder(this);
   			tfBuilder
   			.setTitle(R.string.tickerMSGBoxAktionTitel)
   			.setMessage(R.string.tickerMSGBoxAktionNachricht)
   			.setIcon(android.R.drawable.ic_dialog_alert)
   			.setPositiveButton(R.string.tickerMSGBoxJa, new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {			      	
   					/* �nderung des Ballbesitzes */
   		    		helper.changeBallbesitz(aktionTeamHeim, intSpielBallbesitz, strTeamHeimKurzBySpielID, strTeamAuswKurzBySpielID, 
   		    				spielId, zeit, realzeit); 
   					finish();
   				}
   			})
   			.setNegativeButton(R.string.tickerMSGBoxNein, new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {
   					finish();
   				}
   			})
   			.show();
    	}
    	
/* Zeitstrafen einf�gen */
    	
    	if(Integer.parseInt(aktionInt)== 5){
    	    zeitZurueck=(int)Integer.parseInt(zeit)+2*60000;
    	    /** Hinweis: Nach Spiell�nge 2 Minuten auf Spiell�nge setzen */
    	    if(zeitZurueck>halbzeitlaenge){
    	    	zeitZurueck=halbzeitlaenge;
    	    }
    	    helper.insertTicker(10, spielerString+" "+getString(R.string.tickerAktionZurueck), Integer.parseInt(aktionTeamHeim), 
    	    		spielerString, Integer.parseInt(spielerId), Integer.parseInt(spielId), zeitZurueck, realzeit);
    	    finish=true;
    	   	finish();
    	}
    	
/* Einwechselung einf�gen */
    	
    	if(Integer.parseInt(aktionInt)== 7){
    		/* Torwart eintragen, falls ein Torwart ausgew�hlt wurde */
    		if(spielerPosition.equals(getString(R.string.spielerPositionTorwart))){
    			if(Integer.parseInt(aktionTeamHeim)==1){
    				helper.updateSpielTorwartHeim(spielId, Integer.parseInt(spielerId));
    			}
    			if(Integer.parseInt(aktionTeamHeim)==0){
    				helper.updateSpielTorwartAuswaerts(spielId, Integer.parseInt(spielerId));
    			}
    		}

    		/* Activity Auswechselung aufrufen */
    		Intent newIntent = new Intent(getApplicationContext(), TickerSpielerAuswechselungActivity.class);
    		newIntent.putExtra("TeamID", mannschaftId);
    		newIntent.putExtra("GameID", spielId);
    		newIntent.putExtra("AktionTeamHome", aktionTeamHeim);
    		newIntent.putExtra("Time", zeit);
    		startActivity(newIntent);
    			
    		/** Hinweis: Bislang geht die Activity direkt zur�ck auf die Ticker Activity (�ber TickerAktionActivity).
    		 *  Dies geschieht auch bei dem Back-Button. Einrichten, dass man bei der Auswahl zur�ck zum Ticker kommt und 
    		 *  bei dem Back-Button nur zur�ck zur TickerSpielerActivity.  
    		 */
    		
    		finish=true;
    		Intent i=new Intent();
    		setResult(RESULT_OK, i);
    		finish();
    	}
    	
/* Zeitstrafen einf�gen */
    
    	if(Integer.parseInt(aktionInt)== 9){
    	    zeitZurueck=(int)Integer.parseInt(zeit)+2*60000;
    	    /** Hinweis: Nach Spiell�nge 2 Minuten auf Spiell�nge setzen */
    	    helper.insertTicker(10, spielerString+" "+getString(R.string.tickerAktionZurueck), Integer.parseInt(aktionTeamHeim), 
    	    		spielerString, Integer.parseInt(spielerId), Integer.parseInt(spielId), zeitZurueck, realzeit);
    	    finish=true;
    	   	finish();
    	}
    	
/* Bei Zeitstrafen eintragen, wann Spieler zur�ck */
    	
    	if(Integer.parseInt(aktionInt)== 11){		// Bei Zeitstrafen eintragen, wann Spieler zur�ck
    	    zeitZurueck=(int)Integer.parseInt(zeit)+4*60000;
    	    /** Hinweis: Nach Spiell�nge 2 Minuten auf Spiell�nge setzen */
    	    helper.insertTicker(10, spielerString+" "+getString(R.string.tickerAktionZurueck), Integer.parseInt(aktionTeamHeim), 
    	    		spielerString, Integer.parseInt(spielerId), Integer.parseInt(spielId), zeitZurueck, realzeit);
    	    finish=true;
    	   	finish();
    	}
		/** Hinweis: Bislang geht die Activity direkt zur�ck auf die Ticker Activity (�ber TickerAktionActivity).
		 *  Dies geschieht auch bei dem Back-Button. Einrichten, dass man bei der Auswahl zur�ck zum Ticker kommt und 
		 *  bei dem Back-Button nur zur�ck zur TickerSpielerActivity.  
		 */
		if(finish==false){
			finish();
		}
	}

/*
 * 
 * Spielerliste definieren 
 *
 */
	
	class SpielerAdapter extends CursorAdapter {
		SpielerAdapter(Cursor c) {
			super(TabTickerSpielerAuswActivity.this, c);
		}
    
		@Override
		public void bindView(View row, Context ctxt,
				Cursor c) {
			SpielerHolder holder=(SpielerHolder)row.getTag();
			      
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c,
				ViewGroup parent) {
			LayoutInflater inflater=getLayoutInflater();
			View row=inflater.inflate(R.layout.row_player, parent, false);
			SpielerHolder holder=new SpielerHolder(row);

			row.setTag(holder);

			return(row);
		}
	}
	
	static class SpielerHolder {
	    private TextView name=null;
	    private TextView nummer=null;
	    
	    SpielerHolder(View row) {
	    	name=(TextView)row.findViewById(R.id.rowMannschaftName);
	    	nummer=(TextView)row.findViewById(R.id.rowMannschaftNummer);
	    }
	    
	    void populateFrom(Cursor c, SQLHelper helper) {
	    	String spielerId=helper.getSpielerId(c);
	    	name.setText(helper.getSpielerName(spielerId));
	    	nummer.setText(helper.getSpielerNummer(spielerId));
	  
	    }
	}
	
}