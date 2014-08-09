package de.fivestrikes.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import java.util.Date;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

 /* Button einrichten */
	    
        ImageButton btnTor=(ImageButton) findViewById(R.id.menu_tor);
        btnTor.setImageResource(R.drawable.aktion_torwurf);
        helper.scaleImageLinear(btnTor, 110);
        ImageButton btn7mTor = (ImageButton) findViewById(R.id.menu_7mTor);
        btn7mTor.setImageResource(R.drawable.aktion_7m_tor);
        helper.scaleImageLinear(btn7mTor, 110);
        ImageButton btnTGTor=(ImageButton) findViewById(R.id.menu_TGTor);
        btnTGTor.setImageResource(R.drawable.aktion_tg_tor);
        helper.scaleImageLinear(btnTGTor, 110);
        ImageButton btnGelbe=(ImageButton) findViewById(R.id.menu_gelbe);
        btnGelbe.setImageResource(R.drawable.aktion_gelbekarte);
        helper.scaleImageLinear(btnGelbe, 110);
        ImageButton btnFehl=(ImageButton) findViewById(R.id.menu_fehl);
        btnFehl.setImageResource(R.drawable.aktion_fehlwurf);
        helper.scaleImageLinear(btnFehl, 110);
        ImageButton btn7mFehl=(ImageButton) findViewById(R.id.menu_7mFehl);
        btn7mFehl.setImageResource(R.drawable.aktion_7m_fehlwurf);
        helper.scaleImageLinear(btn7mFehl, 110);
        ImageButton btnTGFehl=(ImageButton) findViewById(R.id.menu_TGFehl);
        btnTGFehl.setImageResource(R.drawable.aktion_tg_fehlwurf);
        helper.scaleImageLinear(btnTGFehl, 110);
        ImageButton btnZwei=(ImageButton) findViewById(R.id.menu_zwei);
        btnZwei.setImageResource(R.drawable.aktion_zweimin);
        helper.scaleImageLinear(btnZwei, 110);
        ImageButton btnTechFehl = (ImageButton) findViewById(R.id.menu_techfehl);
        btnTechFehl.setImageResource(R.drawable.aktion_techfehl);
        helper.scaleImageLinear(btnTechFehl, 110);
        ImageButton btnAufstellheim=(ImageButton) findViewById(R.id.menu_aufstellheim);
        btnAufstellheim.setImageResource(R.drawable.aktion_aufstellung_heim);
        helper.scaleImageLinear(btnAufstellheim, 110);
        ImageButton btnAuszeitHeim=(ImageButton) findViewById(R.id.menu_AuszeitHeim);
        btnAuszeitHeim.setImageResource(R.drawable.aktion_auszeit_heim);
        helper.scaleImageLinear(btnAuszeitHeim, 110);
        ImageButton btnZweiPlusZwei=(ImageButton) findViewById(R.id.menu_zweipluszwei);
        btnZweiPlusZwei.setImageResource(R.drawable.aktion_zweipluszwei);
        helper.scaleImageLinear(btnZweiPlusZwei, 110);
        ImageButton btnWechsel=(ImageButton) findViewById(R.id.menu_wechsel);
        btnWechsel.setImageResource(R.drawable.aktion_wechsel);
        helper.scaleImageLinear(btnWechsel, 110);
        ImageButton btnAufstellAusw=(ImageButton) findViewById(R.id.menu_aufstellausw);
        btnAufstellAusw.setImageResource(R.drawable.aktion_aufstellung_auswaerts);
        helper.scaleImageLinear(btnAufstellAusw, 110);
        ImageButton btnAuszeitAuswaerts=(ImageButton) findViewById(R.id.menu_auszeitauswaerts);
        btnAuszeitAuswaerts.setImageResource(R.drawable.aktion_auszeit_auswaerts);
        helper.scaleImageLinear(btnAuszeitAuswaerts, 110);
        ImageButton btnRote = (ImageButton) findViewById(R.id.menu_rote);
        btnRote.setImageResource(R.drawable.aktion_rotekarte);
        helper.scaleImageLinear(btnRote, 110);
        
        /* Button zurück definieren*/

        btnTor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="2";
				strAktion=getResources().getString(R.string.tickerAktionTor);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btn7mTor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="14";
				strAktion=getResources().getString(R.string.tickerAktion7mTor);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnTGTor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="20";
				strAktion=getResources().getString(R.string.tickerAktionTempogegenstossTor);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnGelbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="6";
				strAktion=getResources().getString(R.string.tickerAktionGelbeKarte);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnFehl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="3";
				strAktion=getResources().getString(R.string.tickerAktionFehlwurf);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btn7mFehl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="15";
				strAktion=getResources().getString(R.string.tickerAktion7mFehlwurf);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnTGFehl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="21";
				strAktion=getResources().getString(R.string.tickerAktionTempogegenstossFehlwurf);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnZwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="5";
				strAktion=getResources().getString(R.string.tickerAktionZweiMinuten);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnTechFehl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="4";
				strAktion=getResources().getString(R.string.tickerAktionTechnischerFehler);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnAufstellheim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="12";
				aktionTeamHeim = "1";
				strAktion=getResources().getString(R.string.tickerAktionStartaufstellung);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnAuszeitHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="24";
				aktionTeamHeim = "1";
				strAktion=getResources().getString(R.string.tickerAktionAuszeit);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				aktionAuszeit();
            }
        });
        
        btnZweiPlusZwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="11";
				strAktion=getResources().getString(R.string.tickerAktionZweiMalZwei);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnWechsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="7";
				strAktion=getResources().getString(R.string.tickerAktionEinwechselung);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnAufstellAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="12";
				aktionTeamHeim = "0";
				strAktion=getResources().getString(R.string.tickerAktionStartaufstellung);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
        btnAuszeitAuswaerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="24";
				aktionTeamHeim = "0";
				strAktion=getResources().getString(R.string.tickerAktionAuszeit);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				aktionAuszeit();
            }
        });
        
        btnRote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				strAktionInt="9";
				strAktion=getResources().getString(R.string.tickerAktionRoteKarte);
				realzeit = DateFormat.getDateTimeInstance().format(new Date());
				startAktion();
            }
        });
        
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
				strAktionInt.equals("15") || strAktionInt.equals("21") || strAktionInt.equals("4")){
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
				strAktionInt.equals("15") || strAktionInt.equals("21")){
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
 * Bilder verkleinern
 *
 */
	 
    private void scaleImagen(ImageButton view, int boundBoxInDp)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

}


