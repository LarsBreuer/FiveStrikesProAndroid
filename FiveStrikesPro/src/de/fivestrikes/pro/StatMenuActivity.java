package de.fivestrikes.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import android.os.Environment;
import android.database.sqlite.SQLiteDatabase;
import au.com.bytecode.opencsv.CSVWriter;
import android.util.Log;


public class StatMenuActivity extends Activity {

	SQLHelper helper=null;
	String spielId=null;
	String strIdTeamHeim=null;
	String strIdTeamAusw=null;
	String strHeim=null;
	String strAusw=null;
	String strHeimKurz=null;
	String strAuswKurz=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.stat_menu);
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);
        customTitleText.setText(R.string.statMenuTitel);
        
/* Datenbank laden */
        
		helper=new SQLHelper(this);
        
/* Button beschriften */
       
	    Button backButton = (Button) findViewById(R.id.back_button);
	    Button btnSpielstatistik = (Button) findViewById(R.id.statSpielstatistik);
	    Button btnNotiz = (Button) findViewById(R.id.statNotiz);
	    Button btnToreHeim = (Button) findViewById(R.id.statTorschuetzenHeim);
	    Button btnToreAusw = (Button) findViewById(R.id.statTorschuetzenAusw);
	    Button btnSpielerHeim = (Button) findViewById(R.id.statSpielerHeim);
	    Button btnSpielerAusw = (Button) findViewById(R.id.statSpielerAusw);
	    Button btnExport = (Button) findViewById(R.id.statExportCSV);

/* Daten aus Activity laden */ 
        
	    spielId=getIntent().getStringExtra("GameID");

/* Daten aus Datenbank laden */
        
	    btnSpielstatistik.setText(helper.getTeamHeimKurzBySpielID(spielId)+"-"+helper.getTeamAuswKurzBySpielID(spielId));
	    strHeim=helper.getTeamHeimNameBySpielID(spielId);
	    strAusw=helper.getTeamAuswNameBySpielID(spielId);
	    strHeimKurz=helper.getTeamHeimKurzBySpielID(spielId);
	    strAuswKurz=helper.getTeamAuswKurzBySpielID(spielId);
	    btnToreHeim.setText(strHeim);
	    btnToreAusw.setText(strAusw);
	    btnSpielerHeim.setText(strHeim);
	    btnSpielerAusw.setText(strAusw);
	    strIdTeamHeim=helper.getSpielHeim(spielId);
	    strIdTeamAusw=helper.getSpielAusw(spielId);
        
/* Button beschriften */
       
        /* Button zur�ck */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });
        
        /* Button Statistik Spiel */
        btnSpielstatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatSpielActivity.class);
				newIntent.putExtra("GameID", spielId);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Spiel */
        btnNotiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatNotizActivity.class);
				newIntent.putExtra("GameID", spielId);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torsch�tzen Heim */
        btnToreHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatToreActivity.class);
				newIntent.putExtra("GameID", spielId);
				newIntent.putExtra("TeamID", strIdTeamHeim);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torsch�tzen Ausw�rts */
        btnToreAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatToreActivity.class);
				newIntent.putExtra("GameID", spielId);
				newIntent.putExtra("TeamID", strIdTeamAusw);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torsch�tzen Heim */
        btnSpielerHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatSpielerActivity.class);
				newIntent.putExtra("GameID", spielId);
				newIntent.putExtra("TeamID", strIdTeamHeim);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torsch�tzen Heim */
        btnSpielerAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatSpielerActivity.class);
				newIntent.putExtra("GameID", spielId);
				newIntent.putExtra("TeamID", strIdTeamAusw);
				startActivity(newIntent);
            }
        });
        
        /* Button CSV Export */
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	File exportDir = new File(Environment.getExternalStorageDirectory(), "");        
                if (!exportDir.exists()) 
                {
                    exportDir.mkdirs();
                }
                File file = new File(exportDir, "Spieldaten.csv");
                try 
                {
                    file.createNewFile();  
                    CSVWriter csvWrite = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file), "CP1250"), ';', CSVWriter.NO_QUOTE_CHARACTER, "\r\n"); 
                    SQLiteDatabase db = helper.getReadableDatabase();
                    String[] args={spielId};
                    
                    Cursor curSpiel = db.rawQuery("SELECT * FROM spiel WHERE _ID=?", args);
                    curSpiel.moveToFirst();
                    int halbzeitlaenge = (Integer.parseInt(curSpiel.getString(5))*60);
                    String[] arrStr = {curSpiel.getString(6),strHeim,strAusw,curSpiel.getString(7),curSpiel.getString(8),strHeimKurz, strAuswKurz,String.valueOf(halbzeitlaenge), curSpiel.getString(17)};
                    csvWrite.writeNext(arrStr);
                    
                    String strEnde= "Ende Spiel";
                    String[] arrStrEnde = {strEnde};
                    csvWrite.writeNext(arrStrEnde);
                    
                    Cursor curSpieler = db.rawQuery("SELECT * FROM spieler WHERE teamID=?", new String[] { curSpiel.getString(1) });
                    while(curSpieler.moveToNext())
                    {
                     	String[] heimArrStr = {curSpieler.getString(3),curSpieler.getString(2), curSpieler.getString(4)};
                         csvWrite.writeNext(heimArrStr);
                     }
                    arrStrEnde[0] = "Ende Heimmannschaft";
                    csvWrite.writeNext(arrStrEnde);
                    curSpieler = db.rawQuery("SELECT * FROM spieler WHERE teamID=?", new String[] { curSpiel.getString(2) });
                    while(curSpieler.moveToNext())
                    {
                     	String[] auswArrStr = {curSpieler.getString(3),curSpieler.getString(2), curSpieler.getString(4)};
                         csvWrite.writeNext(auswArrStr);
                     }
                    arrStrEnde[0] = "Ende Ausw�rtsmannschaft";
                    csvWrite.writeNext(arrStrEnde);
                    curSpieler.close();
                    curSpiel.close();
                             			
                    Cursor curTicker = db.rawQuery("SELECT * FROM ticker WHERE spielID=? ORDER BY zeitInteger ASC", args);
       				int toreHeim=0;
       				int toreAusw=0;
       				String strErgebnis=null;
                    while(curTicker.moveToNext())
                    {
       					if(Integer.parseInt(helper.getTickerAktionInt(curTicker))==2 || 
       							Integer.parseInt(helper.getTickerAktionInt(curTicker))==14 || 
       							Integer.parseInt(helper.getTickerAktionInt(curTicker))==20){
       						if(Integer.parseInt(helper.getTickerAktionTeamHeim(curTicker))==1){
       							toreHeim=toreHeim+1;
       						}
       						if(Integer.parseInt(helper.getTickerAktionTeamHeim(curTicker))==0){
       							toreAusw=toreAusw+1;
       						}
       					}
   						strErgebnis=String.valueOf(toreHeim)+":"+String.valueOf(toreAusw);
   						helper.updateTickerErgebnis(helper.getTickerId(curTicker), toreHeim, toreAusw, strErgebnis);
   						Integer zeitInt=((Integer.parseInt(curTicker.getString(11)))/1000);
   						String zeitStr=String.valueOf(zeitInt);
                    	String[] tickArrStr = {zeitStr,curTicker.getString(6), curTicker.getString(7), 
                    			curTicker.getString(3), curTicker.getString(2), curTicker.getString(4), 
                    			curTicker.getString(10), curTicker.getString(14), curTicker.getString(15)};
                        csvWrite.writeNext(tickArrStr);
                    }
                    arrStrEnde[0] = "Ende Ticker";
                    csvWrite.writeNext(arrStrEnde);
                    csvWrite.close();
                    curTicker.close();
                }
                catch(Exception sqlEx)
                {
                	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StatMenuActivity.this);
        			alertDialogBuilder
    	    			.setTitle(R.string.csvFehlerMsgboxTitel)
    	    			.setMessage(R.string.csvFehlerMsgboxText)
    	    			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
    	    				public void onClick(DialogInterface dialog,int id) {
    	    					
    	    				}
    	    			});
        			AlertDialog alertDialog = alertDialogBuilder.create();
        			alertDialog.show();
                	Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StatMenuActivity.this);
    			alertDialogBuilder
	    			.setTitle(R.string.csvFertigMsgboxTitel)
	    			.setMessage(R.string.csvFertigMsgboxText)
	    			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	    				public void onClick(DialogInterface dialog,int id) {
	    					
	    				}
	    			});
    			AlertDialog alertDialog = alertDialogBuilder.create();
    			alertDialog.show();
            }
        });
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	  
	}
	
}