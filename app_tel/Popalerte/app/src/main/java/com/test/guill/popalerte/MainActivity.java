package com.test.guill.popalerte;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*************************************
         *
         * comportement du TabHost
         *
         */

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

        /**************************************
         *
         * comportement de la boussole
         *
         */

        final ImageView image_boussole;
        image_boussole = (ImageView) findViewById(R.id.boussole);

        image_boussole.setRotationX(image_boussole.getDrawable().getBounds().width() / 2);
        image_boussole.setRotationY(image_boussole.getDrawable().getBounds().height() / 2);

        Runnable myRunnable = new Runnable() {

            Object mPauseLock = new Object();
            boolean mPaused = false;
            boolean mFinished = false;
            float rot = 0;

            @Override
            public void run() {
                while (!mFinished) {
                    try {
                        Thread.sleep(10); // Waits for 1 second (1000 milliseconds)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    image_boussole.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("run image_boussole", "test");
                            rot = rot + 1;
                            image_boussole.setRotation(rot); //rotating from the first position
                        }
                    });
                    synchronized (mPauseLock) {
                        while (mPaused) {
                            try {
                                mPauseLock.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            }
            public void onResume() {
                synchronized (mPauseLock) {
                    mPaused = true;
                }
            }
            public void onPause() {
                synchronized (mPauseLock) {
                    mPaused = false;
                    mPauseLock.notifyAll();
                }
            }
        };
        final Thread thread_boussole = new Thread(myRunnable);

        /***********************************
         *
         * comportement des boutons des pages
         *
         */

        Button btn_indications, btn_consignes, btn_retour_indications, btn_retour_consignes;

        btn_indications = (Button) findViewById(R.id.indications_button);
        btn_indications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.GONE);
                findViewById(R.id.Indications).setVisibility(View.VISIBLE);
                if(!thread_boussole.isAlive())
                    thread_boussole.start(); //démarre le thread de la boussole qui tourne
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
                thread_boussole.interrupt();
            }
        });

        /******************************
         *
         * l'image dans l'accueil du type d'alerte
         *
         */

        ImageView image_type;

        image_type = (ImageView) findViewById(R.id.type_image);
        image_type.setImageResource(R.drawable.type_biohazard);



    }

    @Override
    protected void onResume() {
        super.onResume();
        //relancer le thread de la boussole
    }

    @Override
    protected void onPause() {
        super.onPause();
        //tuer le thread de la boussole
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
