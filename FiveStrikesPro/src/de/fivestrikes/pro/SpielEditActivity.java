package de.fivestrikes.pro;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
//import de.fivestrikes.lite.R;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;


public class SpielEditActivity extends Activity {
	private static final int GET_CODE = 0;
	int spiel_ID=0;
	EditText spiel_halbzeitlaenge=null;
	SQLHelper helper=null;
	String spielId=null;
	String lastID=null;
	String haTeam=null;
	int heimID=0;
	int auswID=0;
	Date spielDatum=null;
	String strSpielDatum = null;
	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 999;
	final Context context = this;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.spiel_edit);
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);
        customTitleText.setText(R.string.spielEditTitel);
		helper=new SQLHelper(this);

/* Daten aus Activity laden */ 
        
	    spielId=getIntent().getStringExtra("GameID");

/* Button und Textfelder definieren */
        
		spiel_halbzeitlaenge=(EditText)findViewById(R.id.spielHalbzeitlaenge);
	    
	    Button save=(Button)findViewById(R.id.save);
	    Button delete=(Button)findViewById(R.id.delete);
	    Button backButton=(Button) findViewById(R.id.back_button);
	    Button tickerButton=(Button) findViewById(R.id.ticker);
	    Button btnDatum=(Button)findViewById(R.id.spielDatum);
	    Button btnHeim=(Button)findViewById(R.id.spielHeim);
	    Button btnAusw=(Button)findViewById(R.id.spielAusw);
	    
	    save.setOnClickListener(onSave);
	    delete.setOnClickListener(onDelete);

/* Spieldaten aus Datenbank laden oder Spiel neu einrichten*/
        
	    if (spielId!=null) {   // Wurde ein bestehendes Spiel ausgewählt...
	    	load();
	    }
	    else {				  // ... falls kein bestehendes Spiel ausgewhält wurde    	
	    	// Halbzeitlänge-Textfeld füllen
	    	spiel_halbzeitlaenge.setText("30");
	    	
	    	// Aktuelles Datum auf Button schreiben
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			
			spielDatum=new Date(year-1900, month, day);
	    }
        
/* Button und Textfelder beschriften */
        	    
	    btnDatum.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append(".").append(month + 1).append(".")
				.append(year).append(" "));
	    
        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });
        
        /* Button Datum */
        btnDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	showDialog(DATE_DIALOG_ID);
            }
        });
        
        /* Button Ticker aufrufen */
        tickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        	    if (heimID==0 || auswID==0 || spielDatum==null) {
        	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        	    	alertDialogBuilder
        	    		.setTitle(R.string.spielMsgboxTitel)
        	    		.setMessage(R.string.spielMsgboxText)
        	    		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog,int id) {

        					}
        	    		});
        	    	AlertDialog alertDialog = alertDialogBuilder.create();
        	    	alertDialog.show();
        	    }
        	    else {
            		if (spielId!=null) {   // Wurde ein bestehendes Spiel ausgewählt...
            			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            			String strSpielDatum = dateFormat.format(spielDatum);
            			helper.updateSpiel(spielId,
            							   strSpielDatum,
            							   Integer.parseInt(spiel_halbzeitlaenge.getText().toString()),
            							   heimID,
            							   auswID);
        				helper.updateTickerSpieler(spielId);	// Spielernamen in Tickermeldungen schreiben
            			Intent i = new Intent(getApplicationContext(), TickerActivity.class);
        				i.putExtra("GameID", spielId);
        				startActivity(i);
            		}
            		else {				  // ... falls kein bestehendes Spiel ausgewählt wurde
            			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            			String strSpielDatum = dateFormat.format(spielDatum);
            			helper.insertSpiel(strSpielDatum,
            							   Integer.parseInt(spiel_halbzeitlaenge.getText().toString()),
        	    					   	   heimID,
        	    					   	   auswID);

        				Intent i = new Intent(getApplicationContext(), TickerActivity.class);
        				/** Hinweis: Nächste Zeile kann wahrscheinlich raus, da ein neues Spiel eingerichtet wurde */ 
            			helper.updateTickerSpieler(helper.getLastSpielId());	// Spielernamen in Tickermeldungen schreiben
        				i.putExtra("GameID", spielId);
        				startActivity(i);
            		}
        	    }
            }
        });
        
        /* Button Heimmannschaft aufrufen */
        btnHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	// Abfragen, ob schon eine Aktion im Spiel eingegeben wurde
            	// Falls ja, kann die Mannschaft nicht mehr geändert werden
            	if(spielId!=null){
            		if(helper.countTickerAktionGesamt(spielId)>0){
            			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            			alertDialogBuilder
        	    			.setTitle(R.string.spielTeamauswahlMsgboxTitel)
        	    			.setMessage(R.string.spielTeamauswahlMsgboxText)
        	    			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        	    				public void onClick(DialogInterface dialog,int id) {

        	    				}
        	    			});
            			AlertDialog alertDialog = alertDialogBuilder.create();
            			alertDialog.show();
            		} else {
            			haTeam="Heim";
            			Intent i = new Intent(getApplicationContext(), MannschaftAuswahlActivity.class);
            			i.putExtra("HomeAway", haTeam);
            			i.putExtra("OpponentID", String.valueOf(auswID));
            			startActivityForResult(i, GET_CODE);
            		}
            	} else{
        			haTeam="Heim";
        			Intent i = new Intent(getApplicationContext(), MannschaftAuswahlActivity.class);
        			i.putExtra("HomeAway", haTeam);
        			i.putExtra("OpponentID", String.valueOf(auswID));
        			startActivityForResult(i, GET_CODE);
            	}
            }
        });
        
        /* Button Auswärtsmannschaft aufrufen */
        btnAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	// Abfragen, ob schon eine Aktion im Spiel eingegeben wurde
            	// Falls ja, kann die Mannschaft nicht mehr geändert werden
            	if(spielId!=null){
            		if(helper.countTickerAktionGesamt(spielId)>0){
            			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            			alertDialogBuilder
        	    			.setTitle(R.string.spielTeamauswahlMsgboxTitel)
        	    			.setMessage(R.string.spielGegnerauswahlMsgboxText)
        	    			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        	    				public void onClick(DialogInterface dialog,int id) {
        	    					
        	    				}
        	    			});
            			AlertDialog alertDialog = alertDialogBuilder.create();
            			alertDialog.show();
            		} else {
            			haTeam="Auswaerts";
            			Intent i = new Intent(getApplicationContext(), MannschaftAuswahlActivity.class);
            			i.putExtra("HomeAway", haTeam);
            			i.putExtra("OpponentID", String.valueOf(heimID));
            			startActivityForResult(i, GET_CODE);
            		}
            	} else {
        			haTeam="Auswaerts";
        			Intent i = new Intent(getApplicationContext(), MannschaftAuswahlActivity.class);
        			i.putExtra("HomeAway", haTeam);
        			i.putExtra("OpponentID", String.valueOf(heimID));
        			startActivityForResult(i, GET_CODE);
            	}
            }
        });
	}

/*
 * 
 * Ergebnis der Sub-Activity "Mannschaftsauswahl abfragen, welcher Mannschaft ausgewählt wurde 
 *
 */
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
				
		if (requestCode == GET_CODE){
			if (resultCode == RESULT_OK) {
				haTeam=data.getStringExtra("HomeAway");
				if (haTeam.equals("Heim")) {
					
					heimID=Integer.parseInt(data.getStringExtra("TeamID"));
					Button btnHeim=(Button)findViewById(R.id.spielHeim);
					btnHeim.setText(helper.getTeamName(data.getStringExtra("TeamID")));
					
				}
				else{
					
					auswID=Integer.parseInt(data.getStringExtra("TeamID"));
					Button btnAusw=(Button)findViewById(R.id.spielAusw);
					btnAusw.setText(helper.getTeamName(data.getStringExtra("TeamID")));
					
				}	
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

	}

/*
 * 
 * Spieldaten aus Datenbank laden
 *
 */
	
	private void load() {
	    
		spiel_halbzeitlaenge.setText(helper.getSpielHalbzeitlaenge(spielId));
		heimID = Integer.parseInt(helper.getSpielHeim(spielId));
		auswID = Integer.parseInt(helper.getSpielAusw(spielId));	
		strSpielDatum = helper.getSpielDatum(spielId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			spielDatum = sdf.parse(strSpielDatum);
			day = spielDatum.getDate();    // getDate => Day of month; getDay => day of week
			month = spielDatum.getMonth();
			year = spielDatum.getYear()+1900;
		}
		catch (ParseException e) {}
		Button btnHeim=(Button)findViewById(R.id.spielHeim);
		Button btnAusw=(Button)findViewById(R.id.spielAusw);
		Button btnDatum=(Button)findViewById(R.id.spielDatum);
		btnDatum.setText(new StringBuilder()
		// Month is 0 based, just add 1
			.append(day).append(".").append(month + 1).append(".")
			.append(year).append(" "));
		btnHeim.setText(helper.getTeamHeimNameBySpielID(spielId));
		btnAusw.setText(helper.getTeamAuswNameBySpielID(spielId));
		
	}

/*
 * 
 * Date Picker definieren
 *
 */
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
 
	private DatePickerDialog.OnDateSetListener datePickerListener 
                = new DatePickerDialog.OnDateSetListener() {
 
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			// set current date to buttontext
			
			Button btnDatum=(Button)findViewById(R.id.spielDatum);
			btnDatum.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append(".").append(month + 1).append(".")
				.append(year).append(" "));
			
			spielDatum=new Date(year-1900, month, day);
		}
	};

/*
 * 
 * Spiel speichern
 *
 */
	
	private View.OnClickListener onSave=new View.OnClickListener() {
		public void onClick(View v) {
    	    if (heimID==0 || auswID==0) {
    	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    	    	alertDialogBuilder
    	    		.setTitle(R.string.spielMsgboxTitel)
    	    		.setMessage(R.string.spielMsgboxText)
    	    		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {

    					}
    	    		});
    	    	AlertDialog alertDialog = alertDialogBuilder.create();
    	    	alertDialog.show();
    	    }
    	    else {
    	    	if (spielId!=null) {   // Wurde ein bestehendes Spiel ausgewählt...
    	    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	    		strSpielDatum = dateFormat.format(spielDatum);
    	    		helper.updateSpiel(spielId,
    	    						   strSpielDatum,
    	    						   Integer.parseInt(spiel_halbzeitlaenge.getText().toString()),
    	    						   heimID,
    	    						   auswID);
    	    		finish();
    	    	}
    	    	else {				  // ... falls kein bestehendes Spiel ausgewhält wurde
    	    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	    		strSpielDatum = dateFormat.format(spielDatum);
    	    		helper.insertSpiel(strSpielDatum,
    	    						   Integer.parseInt(spiel_halbzeitlaenge.getText().toString()),
    	    						   heimID,
    	    						   auswID);
    	    		finish();
    	    	}
    	    }

		}
	};

/*
 * 
 * Spiel löschen
 *
 */
	
	private View.OnClickListener onDelete=new View.OnClickListener() {
		public void onClick(View v) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(SpielEditActivity.this);
			builder
			.setTitle(R.string.spielDelMsgboxTitel)
			.setMessage(R.string.spielDelMsgboxText)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(R.string.tickerMSGBoxJa, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {			      	
					if (spielId!=null) {
						helper.deleteSpiel(spielId);
					}
					finish();
				}
			})
			.setNegativeButton(R.string.tickerMSGBoxNein, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {			      	
					
				}
			})
			.show();
		}
	};
}