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


public class TickerSpielerAuswechselungActivity extends ListActivity {
    /** Called when the activity is first created. */
    
	Cursor model=null;
	SpielerAdapter adapter=null;
	SQLHelper helper=null;
	String mannschaftId=null;
    String spielId=null;
    String aktionTeamHeim=null;
    String spielerId=null;
    String spielerString=null;
    String zeit=null;
    String realzeit=null;
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.spieler);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back_ok);
        getWindow().setWindowAnimations(0);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackOkText);
        customTitleText.setText(R.string.tickerAktionAuswechselung);

/* Daten aus Activity laden */ 
        
        mannschaftId=getIntent().getStringExtra("TeamHomeID");
        aktionTeamHeim=getIntent().getStringExtra("AktionTeamHome");
        spielId=getIntent().getStringExtra("GameID");
        zeit=getIntent().getStringExtra("Time");
        realzeit=getIntent().getStringExtra("RealTime");
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);
        model=helper.getAllSpieler(mannschaftId);
        startManagingCursor(model);
        adapter=new SpielerAdapter(model);
        setListAdapter(adapter);
        
/* Button beschriften */
        
        Button backButton = (Button) findViewById(R.id.back_button);
        Button okButton = (Button) findViewById(R.id.ok_button);
        
        /* Button zur�ck */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });
        
        /* Button ok */
        okButton.setOnClickListener(new View.OnClickListener() {
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
 * Auswahl des Spielers => dann zur�ck zu �bersicht Spiel
 *
 */
	
	@Override
	public void onListItemClick(ListView list, View view,
            int position, long id) {
		
		/* Spielernamen anhand Spieler ID erhalten und neuen Ticker einf�gen */
		
		spielerId=String.valueOf(id);
		spielerString=helper.getSpielerName(spielerId);
		helper.insertTicker(8, getString(R.string.tickerAktionAuswechselung), Integer.parseInt(aktionTeamHeim), spielerString, 
				Integer.parseInt(spielerId), Integer.parseInt(spielId), Integer.parseInt(zeit) + 1, realzeit);
		finish();
	}

/*
 * 
 * Spielerliste definieren 
 *
 */
	
	class SpielerAdapter extends CursorAdapter {
		SpielerAdapter(Cursor c) {
			super(TickerSpielerAuswechselungActivity.this, c);
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
