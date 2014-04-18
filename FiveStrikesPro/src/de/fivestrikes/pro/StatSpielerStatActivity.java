package de.fivestrikes.pro;

//import de.fivestrikes.lite.R;
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
import android.util.Log;


public class StatSpielerStatActivity extends ListActivity {
    /** Called when the activity is first created. */
    
	public final static String ID_SPIEL_EXTRA="de.fivestrikes.pro.spiel_ID";
	public final static String ID_SPIELER_EXTRA="de.fivestrikes.pro.spieler_ID";
	
	Cursor model=null;
	SpielerStatAdapter adapter=null;
	SQLHelper helper=null;
	String mannschaftId=null;
    String spielId=null;
    String spielerId=null;
    String spielerText=null;
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.spieler_stat);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);
        customTitleText.setText(R.string.spielerTitel);

/* Daten aus Activity laden */ 
        
		spielId=getIntent().getStringExtra("GameID");
		mannschaftId=getIntent().getStringExtra("TeamID");
		spielerId=getIntent().getStringExtra("PlayerID");
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);
        
/* Spielerstatistik laden */
        
		helper.createStatSpielerStat(spielId, spielerId, this);
        
        
/* Statistik einrichten */
        
        model=helper.getAllStatSpielerStat();
        startManagingCursor(model);
        adapter=new SpielerStatAdapter(model);
        setListAdapter(adapter); 
        
/* Textfelder und Button beschriften */
        
        final TextView spielerNummer = (TextView) findViewById(R.id.spielerStatNummer);
        final TextView spielerName = (TextView) findViewById(R.id.spielerStatName);
        spielerNummer.setText(helper.getSpielerNummer(spielerId));
        spielerName.setText(helper.getSpielerName(spielerId));       
        
        Button backButton = (Button) findViewById(R.id.back_button);
		Button wurfecke_Button = (Button) findViewById(R.id.spieler_wurfecke);
        
        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });
        
        /* Button Wurfecke */
        wurfecke_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent i=new Intent(StatSpielerStatActivity.this, StatSpielerWurfeckeActivity.class);
            	i.putExtra(ID_SPIELER_EXTRA, spielerId);
            	i.putExtra(ID_SPIEL_EXTRA, spielId);
            	startActivity(i);
            }
        });

    }
	
	@Override
	public void onDestroy() {
	  super.onDestroy();
	    
	}

/*
 * 
 * Liste Spielerstatistik definieren 
 *
 */
	
	class SpielerStatAdapter extends CursorAdapter {
		SpielerStatAdapter(Cursor c) {
			super(StatSpielerStatActivity.this, c);
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
			View row=inflater.inflate(R.layout.row_stat_spieler, parent, false);
			SpielerHolder holder=new SpielerHolder(row);

			row.setTag(holder);

			return(row);
		}
	}
	
	static class SpielerHolder {
	    private TextView bezeichnung=null;
	    private TextView wert=null;
	    
	    SpielerHolder(View row) {
	    	bezeichnung=(TextView)row.findViewById(R.id.spielerBezeichnung);
	    	wert=(TextView)row.findViewById(R.id.spielerWert);
	    }
	    
	    /** Hinweis: StatTore missbraucht für Spielerliste */
	    void populateFrom(Cursor c, SQLHelper helper) {
	    	bezeichnung.setText(helper.getStatSpielerBezeichnung(c));
	    	wert.setText(helper.getStatSpielerWert(c));
	  
	    }
	}
}
