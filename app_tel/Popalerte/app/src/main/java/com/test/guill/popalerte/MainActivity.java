package com.test.guill.popalerte;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Accueil");
        tabSpec.setContent(R.id.Accueil);
        tabSpec.setIndicator("Accueil");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec2 =  tabHost.newTabSpec("historique");
        tabSpec2.setContent(R.id.Historique);
        tabSpec2.setIndicator("Historique");
        tabHost.addTab(tabSpec2);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabID) {
                tabHost.clearAnimation();
            }
        });

        Button btn_indications, btn_consignes, btn_retour_indications, btn_retour_consignes;

        btn_indications = (Button) findViewById(R.id.indications_button);
        btn_indications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.GONE);
                findViewById(R.id.Indications).setVisibility(View.VISIBLE);
            }
        });

        btn_consignes = (Button) findViewById(R.id.consignes_button);
        btn_consignes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.GONE);
                findViewById(R.id.Consignes).setVisibility(View.VISIBLE);
            }
        });

        btn_retour_consignes = (Button) findViewById(R.id.retour_consignes_button);
        btn_retour_consignes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.VISIBLE);
                findViewById(R.id.Consignes).setVisibility(View.GONE);
            }
        });

        btn_retour_indications = (Button) findViewById(R.id.retour_indications_button);
        btn_retour_indications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.VISIBLE);
                findViewById(R.id.Indications).setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
