package de.fivestrikes.pro;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;


public class TickerEditWurfeckeActivity extends Activity {

	String wurfecke=null;
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/* Grundlayout setzen */
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.ticker_edit_wurfecke);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
        getWindow().setWindowAnimations(0);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);
        customTitleText.setText(R.string.tickerAktionWurfecke);

/* Daten aus Activity laden */ 
        
        wurfecke=getIntent().getStringExtra("Wurfecke");
        
/* Button beschriften */
        
        Button backButton = (Button) findViewById(R.id.back_button);
        final Button tor_ol_Button = (Button) findViewById(R.id.tor_ol);
        final Button tor_om_Button = (Button) findViewById(R.id.tor_om);
        final Button tor_or_Button = (Button) findViewById(R.id.tor_or);
        final Button tor_ml_Button = (Button) findViewById(R.id.tor_ml);
        final Button tor_mm_Button = (Button) findViewById(R.id.tor_mm);
        final Button tor_mr_Button = (Button) findViewById(R.id.tor_mr);
        final Button tor_ul_Button = (Button) findViewById(R.id.tor_ul);
        final Button tor_um_Button = (Button) findViewById(R.id.tor_um);
        final Button tor_ur_Button = (Button) findViewById(R.id.tor_ur);
        
        if (wurfecke.equals("OL")){
        	tor_ol_Button.setText("X");
        }
        if (wurfecke.equals("OM")){
        	tor_om_Button.setText("X");
        }
        if (wurfecke.equals("OR")){
        	tor_or_Button.setText("X");
        }
        if (wurfecke.equals("ML")){
        	tor_ml_Button.setText("X");
        }
        if (wurfecke.equals("MM")){
        	tor_mm_Button.setText("X");
        }
        if (wurfecke.equals("MR")){
        	tor_mr_Button.setText("X");
        }
        if (wurfecke.equals("UL")){
        	tor_ul_Button.setText("X");
        }
        if (wurfecke.equals("UM")){
        	tor_um_Button.setText("X");
        }
        if (wurfecke.equals("UR")){
        	tor_ur_Button.setText("X");
        }

        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });

        /* Button Wurfecke*/
        tor_ol_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="OL";
            	tor_button_leeren();
            	tor_ol_Button.setText("X");
            	uebertragen();
            }
        });
        tor_om_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="OM";
            	tor_button_leeren();
            	tor_om_Button.setText("X");
            	uebertragen();
            }
        });
        tor_or_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="OR";
            	tor_button_leeren();
            	tor_or_Button.setText("X");
            	uebertragen();
            }
        });
        tor_ml_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="ML";
            	tor_button_leeren();
            	tor_ml_Button.setText("X");
            	uebertragen();
            }
        });
        tor_mm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="MM";
            	tor_button_leeren();
            	tor_mm_Button.setText("X");
            	uebertragen();
            }
        });
        tor_mr_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="MR";
            	tor_button_leeren();
            	tor_mr_Button.setText("X");
            	uebertragen();
            }
        });
        tor_ul_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="UL";
            	tor_button_leeren();
            	tor_ul_Button.setText("X");
            	uebertragen();
            }
        });
        tor_um_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="UM";
            	tor_button_leeren();
            	tor_um_Button.setText("X");
            	uebertragen();
            }
        });
        tor_ur_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	wurfecke="UR";
            	tor_button_leeren();
            	tor_ur_Button.setText("X");
            	uebertragen();
            }
        });
    }
	
	@Override
	public void onDestroy() {
	  super.onDestroy();
	    
	}

/*
 * 
 * Button leeren 
 *
 */
	
	public void tor_button_leeren() {

        final Button tor_ol_Button = (Button) findViewById(R.id.tor_ol);
        final Button tor_om_Button = (Button) findViewById(R.id.tor_om);
        final Button tor_or_Button = (Button) findViewById(R.id.tor_or);
        final Button tor_ml_Button = (Button) findViewById(R.id.tor_ml);
        final Button tor_mm_Button = (Button) findViewById(R.id.tor_mm);
        final Button tor_mr_Button = (Button) findViewById(R.id.tor_mr);
        final Button tor_ul_Button = (Button) findViewById(R.id.tor_ul);
        final Button tor_um_Button = (Button) findViewById(R.id.tor_um);
        final Button tor_ur_Button = (Button) findViewById(R.id.tor_ur);
        tor_ol_Button.setText("");
        tor_om_Button.setText("");
        tor_or_Button.setText("");
        tor_ml_Button.setText("");
        tor_mm_Button.setText("");
        tor_mr_Button.setText("");
        tor_ul_Button.setText("");
        tor_um_Button.setText("");
        tor_ur_Button.setText("");

	}

/*
 * 
 * Zurück zu Ticker Edit und Ergebnis übertragen
 *
 */
	
	public void uebertragen() {

		Intent i=new Intent();
		i.putExtra("Activity", "Wurfecke");
		i.putExtra("Wurfecke", wurfecke);
		setResult(RESULT_OK, i);
		finish();
		
	}
}
