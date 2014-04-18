package de.fivestrikes.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import java.util.Date;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.text.DateFormat;



public class TabMenuActivity extends Activity {

	private static final int GET_CODE = 0;
	SQLHelper helper=null;
	String spielId=null;
	String aktion=null;
	String teamId=null;
	String teamHeimId=null;
	String teamAuswId=null;
	String strTeamHeimKurzBySpielID=null;
	String strTeamAuswKurzBySpielID=null;
	String aktionTeamHeim=null;
	String strAktion=null;
	String strAktionInt=null;
	String strBallbesitz=null;
	String spielerEingabe=null;
	String realzeit=null;
	Integer zeit=null;
	Integer intSpielBallbesitz=null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
        setContentView(R.layout.tab_menu);
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);

/* Daten aus Activity laden */ 
        
        spielId=getIntent().getStringExtra("GameID");  

/* Daten aus Datenbank laden */
        
		teamHeimId = helper.getSpielHeim(spielId);
		teamAuswId = helper.getSpielAusw(spielId);
		strTeamHeimKurzBySpielID=helper.getTeamHeimKurzBySpielID(spielId);
		strTeamAuswKurzBySpielID=helper.getTeamAuswKurzBySpielID(spielId);

/* GridView laden */
        
		gridview();

    }
    
	@Override
	public void onDestroy() {
	  super.onDestroy();
	  
	}

/*
 * 
 * Auswahl einer Aktion 
 *
 */
	
    public void startAktion() {
    	
    	if (helper.getSpielBallbesitz(spielId)!=null){
			strBallbesitz = String.valueOf(helper.getSpielBallbesitz(spielId));
		} else {
			strBallbesitz = "1";
		}
	
		if(helper.getSpielSpielerEingabe(spielId)!=null){
			spielerEingabe=helper.getSpielSpielerEingabe(spielId);
		} else {
			spielerEingabe="1";
		}
		if(strAktionInt.equals("2") || strAktionInt.equals("14") || strAktionInt.equals("20") || strAktionInt.equals("3") ||
				strAktionInt.equals("15") || strAktionInt.equals("21") || strAktionInt.equals("24") || strAktionInt.equals("4")){
			aktionTeamHeim=strBallbesitz;
		}
		if(strAktionInt.equals("6") || strAktionInt.equals("5") || strAktionInt.equals("11") || 
				strAktionInt.equals("7") || strAktionInt.equals("9")){
			if (strBallbesitz.equals("1")){
				aktionTeamHeim = "0";
			} else {
				aktionTeamHeim = "1";
			}
		}
		if(spielerEingabe.equals("0")){
			if (!strAktionInt.equals("12") && !strAktionInt.equals("7")){
				aktionDirekt();
			} 
		}else {
			if (strAktionInt.equals("12")){
				Intent i = new Intent(getApplicationContext(),TickerSpielerStartaufstellungActivity.class);
				i.putExtra("StrAktionInt", strAktionInt);
				i.putExtra("StrAktion", strAktion);
				i.putExtra("AktionTeamHome", aktionTeamHeim);
				i.putExtra("GameID", spielId);
				i.putExtra("Time", String.valueOf(TickerActivity.elapsedTime));
				i.putExtra("RealTime", String.valueOf(realzeit));
				i.putExtra("TeamHomeID", teamHeimId);
				i.putExtra("TeamAwayID", teamAuswId);
				startActivityForResult(i, GET_CODE);
			} else {
				Intent i = new Intent(getApplicationContext(), TickerSpielerActivity.class);
				i.putExtra("StrAktionInt", strAktionInt);
				i.putExtra("StrAktion", strAktion);
				i.putExtra("AktionTeamHome", aktionTeamHeim);
				i.putExtra("GameID", spielId);
				i.putExtra("Time", String.valueOf(TickerActivity.elapsedTime));
				i.putExtra("RealTime", String.valueOf(realzeit));
				i.putExtra("TeamHomeID", teamHeimId);
				i.putExtra("TeamAwayID", teamAuswId);
				startActivityForResult(i, GET_CODE);
			}
    	}
		
    }

/*
 * 
 * Direkte Eingabe einer Aktion 
 *
 */
	
    public void aktionDirekt() {
    	
    	intSpielBallbesitz = Integer.parseInt(helper.getSpielBallbesitz(spielId));
    	
    	if (helper.getSpielBallbesitz (spielId)!=null){
			strBallbesitz = String.valueOf(intSpielBallbesitz);
		} else {
			strBallbesitz = "1";
		}
		
    	if(strAktionInt.equals("2") || strAktionInt.equals("14") || strAktionInt.equals("20") || strAktionInt.equals("3") ||
				strAktionInt.equals("15") || strAktionInt.equals("21") || strAktionInt.equals("24")){
			aktionTeamHeim=strBallbesitz;
		}
		if(strAktionInt.equals("6") || strAktionInt.equals("5") || strAktionInt.equals("11") || 
				strAktionInt.equals("7") || strAktionInt.equals("9")){
			if (strBallbesitz.equals("1")){
				aktionTeamHeim = "0";
			} else {
				aktionTeamHeim = "1";
			}
		}
		
		zeit=(int) (long) TickerActivity.elapsedTime;
		int zeitZurueck=zeit;
		int halbzeitlaenge=Integer.parseInt(helper.getSpielHalbzeitlaenge(spielId))*60*2000;
		
    	helper.insertTicker(Integer.parseInt(strAktionInt), strAktion, Integer.parseInt(aktionTeamHeim), "", 
				0, Integer.parseInt(spielId), zeit, realzeit);
    	
    	Toast.makeText(getApplicationContext(), strAktion, Toast.LENGTH_SHORT).show();
    	
    	/* Spielstand in Tickereinträge schreiben falls Tor geworfen wurde */
		/* Wenn es Tickereinträge nach dem aktuellen Eintrag gibt, ändere die Torfolge bei den Einträgen */
    	int maxZeit = helper.maxTickerZeit(spielId);Cursor lastTickC=helper.getLastTickerId();
		lastTickC.moveToFirst();
		String tickerId = helper.getTickerId(lastTickC);
		lastTickC.close();
    	if(Integer.parseInt(strAktionInt)==2 || 
    			Integer.parseInt(strAktionInt)==14 || 
    			Integer.parseInt(strAktionInt)==20){
    		String[] args={spielId};
    		SQLiteDatabase db=helper.getWritableDatabase();
    		Cursor cTicker=db.rawQuery("SELECT * FROM ticker WHERE spielID=? ORDER BY zeitInteger ASC", args);
			cTicker.moveToFirst(); 
			String strErgebnis=null;
			if(zeit<=maxZeit){	
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
					if(Integer.parseInt(helper.getTickerZeitInt(cTicker))>=zeit){	// Wenn die Zeit der Schleifeneintrags größer ist als der aktuelle Eintrag, dann hat sich die Torfolge geändert und der Eintrag muss geändert werden
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
			/* Änderung des Ballbesitzes */
			helper.changeBallbesitz(aktionTeamHeim, intSpielBallbesitz, strTeamHeimKurzBySpielID, strTeamAuswKurzBySpielID, 
	    				spielId, String.valueOf(zeit), realzeit); 
    	}
    	
    	// Bei Fehlwurf nach Ballbesitzwechsel fragen
    	if(Integer.parseInt(strAktionInt)==3 || 
    			Integer.parseInt(strAktionInt)==15 || 
    			Integer.parseInt(strAktionInt)==21 || 
    			Integer.parseInt(strAktionInt)==4){
    		AlertDialog.Builder tfBuilder = new AlertDialog.Builder(this);
   			tfBuilder
   			.setTitle(R.string.tickerMSGBoxAktionTitel)
   			.setMessage(R.string.tickerMSGBoxAktionNachricht)
   			.setIcon(android.R.drawable.ic_dialog_alert)
   			.setPositiveButton(R.string.tickerMSGBoxJa, new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {
   					/* Änderung des Ballbesitzes */
   					helper.changeBallbesitz(aktionTeamHeim, intSpielBallbesitz, strTeamHeimKurzBySpielID, strTeamAuswKurzBySpielID, 
   		    				spielId, String.valueOf(zeit), realzeit); 
   	    			((TickerActivity)getParent()).onResume();
   				}
   			})
   			.setNegativeButton(R.string.tickerMSGBoxNein, new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {

   				}
   			})
   			.show();
    	}
		
		// Bei Zeitstrafen Spieler zurück eintragen
		if(Integer.parseInt(strAktionInt)==5 || 
    			Integer.parseInt(strAktionInt)==9 || 
    			Integer.parseInt(strAktionInt)==11){
    	    /** Hinweis: Nach Spiellänge 2 Minuten auf Spiellänge setzen */
    	    if(zeitZurueck>halbzeitlaenge){
    	    	zeitZurueck=halbzeitlaenge;
    	    }
    	    if(Integer.parseInt(strAktionInt)==5 || 
        			Integer.parseInt(strAktionInt)==9){
    	    	zeitZurueck=zeitZurueck+2*60000;
    	    }
    	    if(Integer.parseInt(strAktionInt)==11){
    	    	zeitZurueck=zeitZurueck+4*60000;
    	    }
    	    helper.insertTicker(10, getString(R.string.tickerAktionZurueck), Integer.parseInt(aktionTeamHeim), 
    	    		"", 0, Integer.parseInt(spielId), zeitZurueck, realzeit);
		}
		
		
    	helper.updateSpielErgebnis(Integer.parseInt(spielId));
    	((TickerActivity)getParent()).onResume();
    }

/*
 * 
 * Eingabe der Auszeit
 *
 */
	 
    public void aktionAuszeit() {
		
    	helper.insertTicker(Integer.parseInt(strAktionInt), strAktion, Integer.parseInt(aktionTeamHeim), "", 
				0, Integer.parseInt(spielId), (int) (long) TickerActivity.elapsedTime, realzeit);
  	 	
    	((TickerActivity)getParent()).uhrStartStopp();
    }

/*
 * 
 * Menüpunkte definieren 
 *
 */
	
    public void gridview() {
        GridView gridview = (GridView) findViewById(R.id.aktionen_gridview);
        gridview.setAdapter(new AktionenImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				Resources res = getResources();
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				
				switch(position) {
    			case 0:
    				strAktionInt="2";
    				strAktion=res.getString(R.string.tickerAktionTor);
    				startAktion();
    				break;
    			case 1:
    				strAktionInt="14";
    				strAktion=res.getString(R.string. tickerAktion7mTor);
    				startAktion();
    				break;
    			case 2:
    				strAktionInt="20";
    				strAktion=res.getString(R.string. tickerAktionTempogegenstossTor);
    				startAktion();
    				break;
    			case 3:
    				strAktionInt="6";
    				strAktion=res.getString(R.string. tickerAktionGelbeKarte);
    				startAktion();
    				break;
    			case 4:
    				strAktionInt="3";
    				strAktion=res.getString(R.string. tickerAktionFehlwurf);
    				startAktion();
    				break;
    			case 5:
    				strAktionInt="15";
    				strAktion=res.getString(R.string. tickerAktion7mFehlwurf);
    				startAktion();
    				break;
    			case 6:
    				strAktionInt="21";
    				strAktion=res.getString(R.string. tickerAktionTempogegenstossFehlwurf);
    				startAktion();
    				break;
    			case 7:
    				strAktionInt="5";
    				strAktion=res.getString(R.string. tickerAktionZweiMinuten);
    				startAktion();
    				break;
    			case 8:
    				strAktionInt="4";
    				strAktion=res.getString(R.string.tickerAktionTechnischerFehler);
    				startAktion();
    				break;
    			case 9:
    				strAktionInt="12";
    				aktionTeamHeim = "1";
    				strAktion=res.getString(R.string. tickerAktionStartaufstellung);
    				startAktion();
    				break;
    			case 10:
    				strAktionInt="24";
    				aktionTeamHeim = "1";
    				strAktion=res.getString(R.string. tickerAktionAuszeit);
    				aktionAuszeit();
    				break;
    			case 11:
    				strAktionInt="11";
    				strAktion=res.getString(R.string. tickerAktionZweiMalZwei);
    				startAktion();
    				break;
    			case 12:
    				strAktionInt="7";
    				strAktion=res.getString(R.string.tickerAktionEinwechselung);
    				startAktion();
    				break;
    			case 13:
    				strAktionInt="12";
    				aktionTeamHeim = "0";
    				strAktion=res.getString(R.string.tickerAktionStartaufstellung);
    				startAktion();
    				break;
    			case 14:
    				strAktionInt="24";
    				aktionTeamHeim = "0";
    				strAktion=res.getString(R.string. tickerAktionAuszeit);
    				aktionAuszeit();
    				break;
    			case 15:
    				strAktionInt="9";
    				strAktion=res.getString(R.string. tickerAktionRoteKarte);
    				startAktion();
    				break;
				}
			}
		});
    }
}
