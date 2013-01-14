package de.fivestrikes.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


public class SpielerEditActivity extends Activity {
	int spieler_ID=0;
	Integer mannschaft_ID=0;
	EditText spieler_name=null;
	EditText spieler_nummer=null;
	String spieler_position= null;
	SQLHelper helper=null;
	String spielerId=null;
	String mannschaftId=null;
	String lastID=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.spieler_edit);
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
	    
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);
        customTitleText.setText(R.string.spielerEditTitel);
		
		helper=new SQLHelper(this);
		
		spieler_name=(EditText)findViewById(R.id.spielerName);
		spieler_nummer=(EditText)findViewById(R.id.spielerNummer);
	    
	    Button save=(Button)findViewById(R.id.savePlayer);
	    Button delete=(Button)findViewById(R.id.deletePlayer);
	    Button backButton = (Button) findViewById(R.id.back_button);
	    
	    Spinner spinner = (Spinner)findViewById(R.id.spinnerPositionen);

	    save.setOnClickListener(onSave);
	    delete.setOnClickListener(onDelete);
	    
	    spielerId=getIntent().getStringExtra(SpielerActivity.ID_SPIELER_EXTRA);
        mannschaftId=getIntent().getStringExtra(MannschaftEditActivity.ID_MANNSCHAFT_EXTRA);
	    
	    if (spielerId!=null) {
	    	load(spinner);
	    }
	    else {
	    	Cursor c=helper.getTeamCursor(mannschaftId);
	    	if (c.moveToFirst()!=false) {
		    	c.moveToFirst();
		    	lastID = String.valueOf(Integer.parseInt(helper.getSpielerAnzahl(c)) + 1);
	        }
	        else {
	        	lastID = "1";
	        }
	    	c.close();
	    	spieler_position = "";
	    	helper.insertSpieler("Spieler " + lastID, lastID, mannschaftId, spieler_position);
	    	spieler_name.setText("Spieler " + lastID);
	    	spieler_nummer.setText(lastID);
	    	
			Cursor newC=helper.getLastSpielerId();
			newC.moveToFirst();
			spielerId = helper.getSpielerId(newC);
			newC.close();
		}
	    
        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });
        
	    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	        	switch(position) {
        			case 0:
        				spieler_position = "";
        				break;
        			case 1:
        				spieler_position = getString(R.string.spielerPositionTorwart);
        				break;
        			case 2:
        				spieler_position = getString(R.string.spielerPositionLinksaussen);
        				break;
        			case 3:
        				spieler_position = getString(R.string.spielerPositionRueckraumLinks);
        				break;
        			case 4:
        				spieler_position = getString(R.string.spielerPositionRueckraumMitte);
        				break;
        			case 5:
        				spieler_position = getString(R.string.spielerPositionRueckraumRechts);
        				break;
        			case 6:
        				spieler_position = getString(R.string.spielerPositionRechtsaussen);
        				break;
        			case 7:
        				spieler_position = getString(R.string.spielerPositionKreislaeufer);
        				break;
	        	}
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> parentView) {
	            // your code here
	        }

	    });
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	  
	    helper.close();
	}

	public void load(Spinner spinner) {
		Cursor c=helper.getSpielerById(spielerId);
		c.moveToFirst();    
		spieler_name.setText(helper.getSpielerName(c));
		spieler_nummer.setText(helper.getSpielerNummer(c));
		String position = helper.getSpielerPosition(c);
		Resources res = getResources();
		System.out.println("Load" + position);
		
		spinner.setSelection(0);
		spieler_position = "";
		if (position.equals(res.getString(R.string.spielerPositionTorwart))) {
			spinner.setSelection(1);
			spieler_position = position;
		}
		if (position.equals(res.getString(R.string.spielerPositionLinksaussen))) {
			spinner.setSelection(2);
			spieler_position = position;
		}
		if (position.equals(res.getString(R.string.spielerPositionRueckraumLinks))) {
			spinner.setSelection(3);
			spieler_position = position;
		}
		if (position.equals(res.getString(R.string.spielerPositionRueckraumMitte))) {
			spinner.setSelection(4);
			spieler_position = position;
		}
		if (position.equals(res.getString(R.string.spielerPositionRueckraumRechts))) {
			spinner.setSelection(5);
			spieler_position = position;
		}
		if (position.equals(res.getString(R.string.spielerPositionRechtsaussen))) {
			spinner.setSelection(6);
			spieler_position = position;
		}
		if (position.equals(res.getString(R.string.spielerPositionKreislaeufer))) {
			spinner.setSelection(7);
			spieler_position = position;
		}
		
		c.close();
	}

	private View.OnClickListener onSave=new View.OnClickListener() {
		public void onClick(View v) {
			
		    helper.updateSpieler(spielerId,
		        		  		 spieler_name.getText().toString(),
		        		  		 spieler_nummer.getText().toString(),
		        		  		 spieler_position);
		    System.out.println(spieler_position);
			finish();
		}
	};
	
	private View.OnClickListener onDelete=new View.OnClickListener() {
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(SpielerEditActivity.this);
			builder
			.setTitle(R.string.spielerMsgboxTitel)
			.setMessage(R.string.spielerMsgboxText)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(R.string.tickerMSGBoxJa, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {			      	
			        
					if(helper.countTickerSpielerId(spielerId)>0){	// Existiert noch ein Spiel mit dieser Mannschaft?
	        	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SpielerEditActivity.this);
	        	    	alertDialogBuilder
	        	    		.setTitle(R.string.spielerDelMsgboxTitel)
	        	    		.setMessage(R.string.spielerDelMsgboxText)
	        	    		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	        					public void onClick(DialogInterface dialog,int id) {

	        					}
	        	    		});
	        	    	AlertDialog alertDialog = alertDialogBuilder.create();
	        	    	alertDialog.show();
					}
					else{
						helper.deleteSpieler(spielerId,
			        			  spieler_name.getText().toString(),
		  			  	  		  spieler_nummer.getText().toString());
					
				        finish();
					}
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