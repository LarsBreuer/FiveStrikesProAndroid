package de.fivestrikes.pro;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ImageView;

public class TickerInfoActivity extends Activity {
	SQLHelper helper=null;
	String spielId=null;
	EditText notiz=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
/* Grundlayout setzen */
        
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ticker_info);
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_back);
        final TextView customTitleText = (TextView) findViewById(R.id.titleBackText);
        customTitleText.setText(R.string.info);
        
/* Button, CheckBox und Textfeld definieren */
        
	    Button backButton = (Button) findViewById(R.id.back_button);
	    CheckBox chkSpieler = (CheckBox) findViewById(R.id.checkbox_spieler_eingeben);
	    CheckBox chkWurfecke = (CheckBox) findViewById(R.id.checkbox_wurfecke_eingeben);
	    notiz=(EditText)findViewById(R.id.notiz);

/* Daten aus Activity laden */ 
        
        spielId=getIntent().getStringExtra("GameID");
        
/* Datenbank laden */
        
        helper=new SQLHelper(this);

/* Daten aus Datenbank laden */
        
		if(helper.getSpielSpielerEingabe(spielId)!=null){
			if(helper.getSpielSpielerEingabe(spielId).equals("1")){
				chkSpieler.setChecked(true);
			} else {
				chkSpieler.setChecked(false);
			}
		} else {
			chkSpieler.setChecked(true);
		}
		if(helper.getSpielSpielerWurfecke(spielId)!=null){
			if(helper.getSpielSpielerWurfecke(spielId).equals("1")){
				chkWurfecke.setChecked(true);
			} else {
				chkWurfecke.setChecked(false);
			}
		} else {
			chkWurfecke.setChecked(true);
		}
		notiz.setText(helper.getSpielNotiz(spielId));
        
/* Button einrichten */
		
		ImageView imgTor=(ImageView) findViewById(R.id.aktion_torwurf);
		imgTor.setImageResource(R.drawable.aktion_torwurf);
        helper.scaleImageViewLinear(imgTor, 50);
        ImageView img7mTor = (ImageView) findViewById(R.id.aktion_7m_tor);
        img7mTor.setImageResource(R.drawable.aktion_7m_tor);
        helper.scaleImageViewLinear(img7mTor, 50);
        ImageView imgTGTor=(ImageView) findViewById(R.id.aktion_tg_tor);
        imgTGTor.setImageResource(R.drawable.aktion_tg_tor);
        helper.scaleImageViewLinear(imgTGTor, 50);
        ImageView imgGelbe=(ImageView) findViewById(R.id.aktion_gelbekarte);
        imgGelbe.setImageResource(R.drawable.aktion_gelbekarte);
        helper.scaleImageViewLinear(imgGelbe, 50);
        ImageView imgFehl=(ImageView) findViewById(R.id.aktion_fehlwurf);
        imgFehl.setImageResource(R.drawable.aktion_fehlwurf);
        helper.scaleImageViewLinear(imgFehl, 50);
        ImageView img7mFehl=(ImageView) findViewById(R.id.aktion_7m_fehlwurf);
        img7mFehl.setImageResource(R.drawable.aktion_7m_fehlwurf);
        helper.scaleImageViewLinear(img7mFehl, 50);
        ImageView imgTGFehl=(ImageView) findViewById(R.id.aktion_tg_fehlwurf);
        imgTGFehl.setImageResource(R.drawable.aktion_tg_fehlwurf);
        helper.scaleImageViewLinear(imgTGFehl, 50);
        ImageView imgZwei=(ImageView) findViewById(R.id.aktion_zweimin);
        imgZwei.setImageResource(R.drawable.aktion_zweimin);
        helper.scaleImageViewLinear(imgZwei, 50);
        ImageView imgTechFehl = (ImageView) findViewById(R.id.aktion_techfehl);
        imgTechFehl.setImageResource(R.drawable.aktion_techfehl);
        helper.scaleImageViewLinear(imgTechFehl, 50);
        ImageView imgAufstellheim=(ImageView) findViewById(R.id.aktion_aufstellung_heim);
        imgAufstellheim.setImageResource(R.drawable.aktion_aufstellung_heim);
        helper.scaleImageViewLinear(imgAufstellheim, 50);
        ImageView imgAuszeitHeim=(ImageView) findViewById(R.id.aktion_auszeit_heim);
        imgAuszeitHeim.setImageResource(R.drawable.aktion_auszeit_heim);
        helper.scaleImageViewLinear(imgAuszeitHeim, 50);
        ImageView imgZweiPlusZwei=(ImageView) findViewById(R.id.aktion_zweipluszwei);
        imgZweiPlusZwei.setImageResource(R.drawable.aktion_zweipluszwei);
        helper.scaleImageViewLinear(imgZweiPlusZwei, 50);
        ImageView imgWechsel=(ImageView) findViewById(R.id.aktion_wechsel);
        imgWechsel.setImageResource(R.drawable.aktion_wechsel);
        helper.scaleImageViewLinear(imgWechsel, 50);
        ImageView imgAufstellAusw=(ImageView) findViewById(R.id.aktion_aufstellung_auswaerts);
        imgAufstellAusw.setImageResource(R.drawable.aktion_aufstellung_auswaerts);
        helper.scaleImageViewLinear(imgAufstellAusw, 50);
        ImageView imgAuszeitAuswaerts=(ImageView) findViewById(R.id.aktion_auszeit_auswaerts);
        imgAuszeitAuswaerts.setImageResource(R.drawable.aktion_auszeit_auswaerts);
        helper.scaleImageViewLinear(imgAuszeitAuswaerts, 50);
        ImageView imgRote = (ImageView) findViewById(R.id.aktion_rotekarte);
        imgRote.setImageResource(R.drawable.aktion_rotekarte);
        helper.scaleImageViewLinear(imgRote, 50);
       
/* Button beschriften */
        
        /* Button zurück */
        backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					helper.updateSpielNotiz(spielId, notiz.getText().toString());
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
 * Checkbos für Statistikeingabe definieren 
 *
 */
	
	public void onCheckboxClicked(View view) {
		boolean checked = ((CheckBox) view).isChecked();
	    CheckBox chkSpieler = (CheckBox) findViewById(R.id.checkbox_spieler_eingeben);
	    CheckBox chkWurfecke = (CheckBox) findViewById(R.id.checkbox_wurfecke_eingeben);
		switch(view.getId()) {
		case R.id.checkbox_spieler_eingeben:
			if (checked) {
				helper.updateSpielSpielerEingeben(spielId, 1);
			} else {
				helper.updateSpielSpielerEingeben(spielId, 0);
				helper.updateSpielSpielerWurfecke(spielId, 0);
				chkWurfecke.setChecked(false);
			}
				
			break;
		case R.id.checkbox_wurfecke_eingeben:
			if (checked) {
				helper.updateSpielSpielerWurfecke(spielId, 1);
				helper.updateSpielSpielerEingeben(spielId, 1);
				chkSpieler.setChecked(true);
			} else {
				helper.updateSpielSpielerWurfecke(spielId, 0);
				helper.updateSpielSpielerEingeben(spielId, 1);
				chkSpieler.setChecked(true);
			}
				
			break;
			
		}
	}
}
