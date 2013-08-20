package de.fivestrikes.pro;

import android.app.ListActivity;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.CursorAdapter;

public class TabListActivity extends ListActivity {
	
	public final static String ID_TICKER_EXTRA="de.fivestrikes.pro.ticker_ID";
	public final static String ID_SPIEL_EXTRA="de.fivestrikes.pro.spiel_ID";
	Cursor model=null;
	SQLHelper helper=null;
	TickerAdapter adapter=null;
	String spielId=null;
	private static final int GET_CODE = 0;
	final Context context = this;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_list);
 
        spielId=getIntent().getStringExtra(SpielEditActivity.ID_SPIEL_EXTRA);
        helper=new SQLHelper(this);
        model=helper.getAllTicker(spielId);
        startManagingCursor(model);
        adapter=new TickerAdapter(model);
        setListAdapter(adapter);
    }
    
	@Override
	public void onListItemClick(ListView list, View view,
            int position, long id) {
		
		Intent i=new Intent(TabListActivity.this, TickerEditActivity.class);
		i.putExtra(ID_TICKER_EXTRA, String.valueOf(id));
		i.putExtra(ID_SPIEL_EXTRA, spielId);
		startActivityForResult(i, GET_CODE);
		
	}
	
	class TickerAdapter extends CursorAdapter {
		TickerAdapter(Cursor c) {
			super(TabListActivity.this, c);
		}
    
		@Override
		public void bindView(View row, Context ctxt,
				Cursor c) {
			TickerHolder holder=(TickerHolder)row.getTag();
			      
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c,
				ViewGroup parent) {
			LayoutInflater inflater=getLayoutInflater();
			View row=inflater.inflate(R.layout.row_ticker, parent, false);
			TickerHolder holder=new TickerHolder(row);

			row.setTag(holder);

			return(row);
		}
	}
	
	static class TickerHolder {
	    private TextView aktion=null;
	    private TextView zeit=null;
	    private TextView spieler=null;
	    private TextView icon=null;
	    
	    TickerHolder(View row) {
	      aktion=(TextView)row.findViewById(R.id.rowTickerAktion);
	      zeit=(TextView)row.findViewById(R.id.rowTickerZeit);
	      spieler=(TextView)row.findViewById(R.id.rowTickerSpieler);
	      icon=(TextView)row.findViewById(R.id.rowTickerIcon);
	    }
	    
	    void populateFrom(Cursor c, SQLHelper helper) {
	      aktion.setText(helper.getTickerAktion(c));
	      zeit.setText(helper.getTickerZeit(c));
	      spieler.setText(helper.getTickerSpieler(c));			
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==2 || Integer.parseInt(helper.getTickerAktionInt(c))==14 || 
	    		  Integer.parseInt(helper.getTickerAktionInt(c))==20){
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_none);
	    	  icon.setText(helper.getTickerErgebnis(c));
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==3 || Integer.parseInt(helper.getTickerAktionInt(c))==4 || 
	    		  Integer.parseInt(helper.getTickerAktionInt(c))==15 || Integer.parseInt(helper.getTickerAktionInt(c))==21) {
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_spieler);
	    	  icon.setText("");
	      } 
			
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==6 || Integer.parseInt(helper.getTickerAktionInt(c))==9) {
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_karte);
	    	  icon.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==5 || Integer.parseInt(helper.getTickerAktionInt(c))==11) {
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_zwei);
	    	  icon.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==0 || Integer.parseInt(helper.getTickerAktionInt(c))==1) {
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_ballbesitz);
	    	  icon.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==8 || Integer.parseInt(helper.getTickerAktionInt(c))==10) {
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_auswechsel);
	    	  icon.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==7) {
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_einwechsel);
	    	  icon.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==16 || Integer.parseInt(helper.getTickerAktionInt(c))==17 || 
	    		  Integer.parseInt(helper.getTickerAktionInt(c))==18 || Integer.parseInt(helper.getTickerAktionInt(c))==19 || 
	    		  Integer.parseInt(helper.getTickerAktionInt(c))==22 || Integer.parseInt(helper.getTickerAktionInt(c))==23){
	    	  icon.setBackgroundResource(R.drawable.ticker_symbol_torwart);
	    	  icon.setText("");
	      }
	    }
	}
}

