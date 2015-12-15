package com.test.guill.popalerte;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity {
    private boolean alerte = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //cette fonction définie le comportement de l'application a son démarrage
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //la page par défaut est la page d'accueil
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*************************************
         *
         * comportement du TabHost
         *
         */

        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost); //recupère le tabHost

        tabHost.setup(); //requis

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("accueil"); //met la premiere tab dans le TabHost
        tabSpec.setContent(R.id.Accueil);
        tabSpec.setIndicator("Accueil");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec2 =  tabHost.newTabSpec("historique"); //met la deuxième tab dans le TabHost
        tabSpec2.setContent(R.id.Historique);
        tabSpec2.setIndicator("Historique");
        tabHost.addTab(tabSpec2);

        //efface ce qu'il y avait d'affiché sur la page avant de changer de tab
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabID) {
                tabHost.clearAnimation();
            }
        });

        /************************
         *
         * thread qui effectuera des verifications régulières par rapport au arréviées de données et autres
         *
         */

        final Runnable verifs = new Runnable() { //runnable du thread de la boussole (fonction qui sera utilisé pour le thread)

            @Override
            public void run() { //la fonction qui va ettre appeler quand le thread sera lancé
                while (true){ //le thread doit toujours etre actif
                    try {
                        Thread.sleep(1000); // attends pendant 1 seconde avant de refaire un tour de boucle
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!alerte){ //que doit faire la page d'accueil si il y a une alerte ou pas
                        findViewById(R.id.accueil_content_layout).post(new Runnable() { //requis
                            @Override
                            public void run() { //si il n'y a pas d'alerte
                                findViewById(R.id.accueil_content_layout_alerte).setVisibility(View.GONE); //cacher la page alerte
                                findViewById(R.id.accueil_content_layout_noAlerte).setVisibility(View.VISIBLE); //afficher la page noAlerte
                            }
                        });
                    }
                    else{
                        findViewById(R.id.accueil_content_layout).post(new Runnable() { //requis
                            @Override
                            public void run() { //si il y a une alerte
                                findViewById(R.id.accueil_content_layout_alerte).setVisibility(View.VISIBLE); //afficher la page alerte
                                findViewById(R.id.accueil_content_layout_noAlerte).setVisibility(View.GONE); //cacher la page noAlerte
                            }
                        });
                    }
                }
            }
        };

        Thread thread_verifs = new Thread(verifs); //crée le thread de vérifications
        thread_verifs.start(); //lance le thread de verifications

        /**************************************
         *
         * comportement de la boussole
         *
         */

        final ImageView image_boussole;
        image_boussole = (ImageView) findViewById(R.id.boussole);//crée et récupère l'image de la boussole

        image_boussole.setRotationX(image_boussole.getDrawable().getBounds().width() / 2); //définie le point de rotation de l'image
        image_boussole.setRotationY(image_boussole.getDrawable().getBounds().height() / 2);//de la boussole au centre de l'image

        final Runnable myRunnable = new Runnable() { //runnable du thread de la boussole (fonction qui sera utilisé pour le thread)

            float rot = 0; // réel de rotation contenant les degrés pour faire tourner la boussole

            @Override
            public void run() { //la fonction qui va ettre appeler quand le thread sera lancé
                /**************
                 *
                 * boucle while si la page d'indications est affiché
                 *
                 */


                while (findViewById(R.id.Indications).getVisibility() == View.VISIBLE) {
                    try {
                        Thread.sleep(10); // attends pendant 10 millisecondes avant de refaire un tour de boucle
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    image_boussole.post(new Runnable() { //fonction de l'image pour la faire tourner
                        @Override
                        public void run() {
                            if(findViewById(R.id.Indications).getVisibility() == View.VISIBLE) {
                                rot = rot + 1; //augmente les degrés de rotation de 1 pour fair tourner la boussole
                                image_boussole.setRotation(rot); //fait tourner l'image de "rot" degrés depuis la premiere position
                            }
                        }
                    });
                }
            }
        };

        /***********************************
         *
         * comportement des boutons des pages
         *
         */

        //declaration des bouttons
        Button btn_indications, btn_consignes, btn_retour_indications, btn_retour_consignes;

        //boutton de redirection vers les indications depuis l'accueil
        btn_indications = (Button) findViewById(R.id.indications_button);
        btn_indications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.GONE); //cache l'accueil
                findViewById(R.id.Indications).setVisibility(View.VISIBLE);//affiche le layout des indications
                Thread thread_boussole = new Thread(myRunnable); //on ne peux pas relancer un thread arrété, il est donc recréé a chaque fois
                thread_boussole.start(); //démarre le thread de la boussole qui tourne
            }
        });

        //boutton de redirection vers les consignes depuis l'accueil
        btn_consignes = (Button) findViewById(R.id.consignes_button);
        btn_consignes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.GONE); //cache l'accueil
                findViewById(R.id.Consignes).setVisibility(View.VISIBLE);//affiche le layout des consignes

                Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                //delay, virbation_time, delay, ...
                long[] pattern = {0, 1000, 500, 1000, 500};
                vibe.vibrate(pattern, -1);
            }
        });

        //boutton de retour vers l'accueil depuis les consignes
        btn_retour_consignes = (Button) findViewById(R.id.retour_consignes_button);
        btn_retour_consignes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.VISIBLE); //affiche l'accueil
                findViewById(R.id.Consignes).setVisibility(View.GONE); //cache le layout des consignes
            }
        });

        //boutton de retour vers l'accueil depuis les indications
        btn_retour_indications = (Button) findViewById(R.id.retour_indications_button);
        btn_retour_indications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabHost.setVisibility(View.VISIBLE); //affiche l'accueil
                findViewById(R.id.Indications).setVisibility(View.GONE); //cache le layout des indications
            }

        });

        /******************************
         *
         * l'image dans l'accueil du type d'alerte
         *
         */

        ImageView image_type;
        image_type = (ImageView) findViewById(R.id.type_image);
        image_type.setImageResource(R.drawable.type_incendie);
    }

    @Override
    protected void onResume() { //fonction appelé quand on reviens sur l'application apres avoir tapé sur le boutton "home"
        super.onResume();
    }

    @Override
    protected void onPause() { //fonction appelé aprés avoir tapé sur le boutton "home"
        super.onPause();
        findViewById(R.id.tabHost).setVisibility(View.VISIBLE); //affiche l'accueil
        findViewById(R.id.Indications).setVisibility(View.GONE); //cache le layout des indications
        // afin d'éviter que le thread de la boussole continue en arriere plan
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
            alerte = !alerte;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
