package de.fivestrikes.pro;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;


public class SpielerActivity extends ListActivity {
    /** Called when the activity is first created. */
    
	int mannschaft_ID=0;
	Cursor model=null;
	SpielerAdapter adapter=null;
	SQLHelper helper=null;
	String mannschaftId=null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.spieler);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back_plus);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackPlusText);
        customTitleText.setText(R.string.spielerTitel);
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);
        mannschaftId=getIntent().getStringExtra("TeamID");
        model=helper.getAllSpieler(mannschaftId);
        startManagingCursor(model);
        adapter=new SpielerAdapter(model);
        setListAdapter(adapter);
        
/* Button beschriften */
        
        Button backButton = (Button) findViewById(R.id.back_button);
        Button plusButton = (Button) findViewById(R.id.plus_button);
        
        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	finish();
            }
        });
 
        /* Button Spieler hinzufügen */
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent i = new Intent(SpielerActivity.this, SpielerEditActivity.class);
            	i.putExtra("TeamID", mannschaftId);
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
 * Spielerauswahl 
 *
 */
	
	@Override
	public void onListItemClick(ListView list, View view,
            int position, long id) {
		Intent i=new Intent(SpielerActivity.this, SpielerEditActivity.class);
		
		i.putExtra("PlayerID", String.valueOf(id));
		i.putExtra("TeamID", mannschaftId);
		startActivity(i);
	}

/*
 * 
 * Spielerliste definieren 
 *
 */
	
	class SpielerAdapter extends CursorAdapter {
		SpielerAdapter(Cursor c) {
			super(SpielerActivity.this, c);
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
