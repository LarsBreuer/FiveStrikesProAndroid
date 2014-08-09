package de.fivestrikes.pro;

import android.app.ListActivity;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.CursorAdapter;
import android.widget.Toast;
import android.util.Log;

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
        
/* Grundlayout setzen */
        
        setContentView(R.layout.tab_list);

/* Daten aus Activity laden */ 
        
        spielId=getIntent().getStringExtra("GameID");
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);
        model=helper.getAllTicker(spielId);
        startManagingCursor(model);
        adapter=new TickerAdapter(model);
        setListAdapter(adapter);
    }

/*
 * 
 * Inhalt neu laden, wenn Activity ernuet aufgerufen wird 
 *
 */
	
    @Override
	public void onResume() {
    	super.onResume();  // Always call the superclass method first

    	refreshContent();
    }
    
    public void refreshContent() {

    	helper=new SQLHelper(this);
        model=helper.getAllTicker(spielId);
        startManagingCursor(model);
        adapter=new TickerAdapter(model);
        setListAdapter(adapter);  
    }

/*
 * 
 * Tickerauswahl 
 *
 */
	   
	@Override
	public void onListItemClick(ListView list, View view,
            int position, long id) {
		
		Intent i=new Intent(TabListActivity.this, TickerEditActivity.class);
		i.putExtra("TickerID", String.valueOf(id));
		i.putExtra("GameID", spielId);
		startActivityForResult(i, GET_CODE);
		
	}
	
	@Override
	public void onDestroy() {
	  super.onDestroy();	  

	}

/*
 * 
 * Liste Ticker definieren 
 *
 */
	
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
	    private TextView iconText=null;
	    private ImageView icon=null;
	    private View teamColor=null;
	    
	    TickerHolder(View row) {
	      aktion=(TextView)row.findViewById(R.id.rowTickerAktion);
	      zeit=(TextView)row.findViewById(R.id.rowTickerZeit);
	      spieler=(TextView)row.findViewById(R.id.rowTickerSpieler);
	      iconText=(TextView)row.findViewById(R.id.rowTickerText);
	      icon=(ImageView)row.findViewById(R.id.rowTickerIcon);
	      teamColor=(View)row.findViewById(R.id.rowTickerTeamColor);
	    }
	    
	    void populateFrom(Cursor c, SQLHelper helper) {
	      aktion.setText(helper.getTickerAktion(c));
	      zeit.setText(helper.getTickerZeit(c) + " Min.");
	      if(!helper.getTickerSpielerId(c).equals("0") ){
	    	  spieler.setText(helper.getSpielerNummerById(helper.getTickerSpielerId(c)) + " - " + helper.getTickerSpieler(c));
	      } else {
			  spieler.setText("");
		  }
	      if (Integer.parseInt(helper.getTickerAktionTeamHeim(c))==1){
	    	  teamColor.setBackgroundColor(0xFF404895);   	  
	      }
	      if (Integer.parseInt(helper.getTickerAktionTeamHeim(c))==0){
	    	  teamColor.setBackgroundColor(0xFFCB061D);   	  
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==2 || Integer.parseInt(helper.getTickerAktionInt(c))==14 || 
	    		  Integer.parseInt(helper.getTickerAktionInt(c))==20){
	    	  icon.setImageResource(R.drawable.ticker_symbol_none);
	    	  iconText.setText(helper.getTickerErgebnis(c));   	  
	      }   
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==3) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_fehlwurf);
	    	  iconText.setText("");
	      } 
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==4) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_techfehl);
	    	  iconText.setText("");
	      } 
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==15) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_7m_fehlwurf);
	    	  iconText.setText("");
	      } 
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==21) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_tg_fehlwurf);
	    	  iconText.setText("");
	      } 
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==6) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_gelbekarte);
	    	  iconText.setText("");
	      }	      
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==9) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_rotekarte);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==5 || Integer.parseInt(helper.getTickerAktionInt(c))==11) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_zwei_raus);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==0 || Integer.parseInt(helper.getTickerAktionInt(c))==1) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_ballbesitz);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==8) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_auswechsel);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==10) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_zwei_rein);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==7) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_einwechsel);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==16 || Integer.parseInt(helper.getTickerAktionInt(c))==22) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_torwart_gehalten);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==17 || Integer.parseInt(helper.getTickerAktionInt(c))==23) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_torwart_tor);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==18) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_torwart_7m_gehalten);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==19) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_torwart_7m_tor);
	    	  iconText.setText("");
	      }
	      if (Integer.parseInt(helper.getTickerAktionInt(c))==24) {
	    	  icon.setImageResource(R.drawable.ticker_symbol_auszeit);
	    	  iconText.setText("");
	      }
	      helper.scaleImageRelative(icon, 45);   	
	    }
	    
	    private void scaleImagen(ImageView view, int boundBoxInDp)
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
	        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
	        params.width = width;
	        params.height = height;
	        view.setLayoutParams(params);
	    }
	}
	

	
}

