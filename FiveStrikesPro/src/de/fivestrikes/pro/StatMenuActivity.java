package de.fivestrikes.pro;

//import de.fivestrikes.lite.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;


public class StatMenuActivity extends Activity {
	public final static String ID_SPIEL_EXTRA="de.fivestrikes.pro.spiel_ID";
	public final static String ID_TEAMTORE_EXTRA="de.fivestrikes.pro.teamtore_ID";
	SQLHelper helper=null;
	String spielId=null;
	String strIdTeamHeim=null;
	String strIdTeamAusw=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.stat_menu);
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
	    
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);
        customTitleText.setText(R.string.statMenuTitel);
		
		helper=new SQLHelper(this);
	    
	    Button backButton = (Button) findViewById(R.id.back_button);
	    Button btnSpielstatistik = (Button) findViewById(R.id.statSpielstatistik);
	    Button btnToreHeim = (Button) findViewById(R.id.statTorschuetzenHeim);
	    Button btnToreAusw = (Button) findViewById(R.id.statTorschuetzenAusw);
	    Button btnSpielerHeim = (Button) findViewById(R.id.statSpielerHeim);
	    Button btnSpielerAusw = (Button) findViewById(R.id.statSpielerAusw);
	    Button btnExport = (Button) findViewById(R.id.statExportCSV);
	    
	    spielId=getIntent().getStringExtra(StatActivity.ID_EXTRA);
	    
		Cursor c=helper.getSpielById(spielId);
		c.moveToFirst();
	    btnSpielstatistik.setText(helper.getTeamHeimKurzBySpielID(c)+"-"+helper.getTeamAuswKurzBySpielID(c));
	    btnToreHeim.setText(helper.getTeamHeimNameBySpielID(c));
	    btnToreAusw.setText(helper.getTeamAuswNameBySpielID(c));
	    btnSpielerHeim.setText(helper.getTeamHeimNameBySpielID(c));
	    btnSpielerAusw.setText(helper.getTeamAuswNameBySpielID(c));
	    strIdTeamHeim=helper.getSpielHeim(c);
	    strIdTeamAusw=helper.getSpielAusw(c);
		c.close();
	    
        /* Button zurück */
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
				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torschützen Heim */
        btnToreHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatToreActivity.class);
				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
				newIntent.putExtra(ID_TEAMTORE_EXTRA, strIdTeamHeim);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torschützen Auswärts */
        btnToreAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatToreActivity.class);
				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
				newIntent.putExtra(ID_TEAMTORE_EXTRA, strIdTeamAusw);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torschützen Heim */
        btnSpielerHeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatSpielerActivity.class);
				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
				newIntent.putExtra(ID_TEAMTORE_EXTRA, strIdTeamHeim);
				startActivity(newIntent);
            }
        });
        
        /* Button Statistik Torschützen Heim */
        btnSpielerAusw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Intent newIntent = new Intent(getApplicationContext(), StatSpielerActivity.class);
				newIntent.putExtra(ID_SPIEL_EXTRA, spielId);
				newIntent.putExtra(ID_TEAMTORE_EXTRA, strIdTeamAusw);
				startActivity(newIntent);
            }
        });
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	  
	    helper.close();
	}
	
}