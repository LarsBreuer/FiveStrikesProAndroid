package de.fivestrikes.pro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TabStatisticActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        TextView textview = new TextView(this);
        textview.setText("This is statistic tab");
        setContentView(textview);
    }
}
