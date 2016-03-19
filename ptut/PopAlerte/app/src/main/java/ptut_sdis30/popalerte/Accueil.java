package ptut_sdis30.popalerte;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class Accueil extends AppCompatActivity {
    //
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "Acceuil";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //private ProgressBar mRegistrationProgressBar;
    //private TextView mInformationTextView;
    //


    private boolean alerte = true;
    dbHandler dbhandler;
    ListView historiqueListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Global.notificationOK = true;

        //
        //mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               // mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                   // mInformationTextView.setText(getString('gg'));
                } else {
                    //mInformationTextView.setText(getString('error'));
                }
            }
        };
        //mInformationTextView = (TextView) findViewById(R.id.informationTextView);


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        //


        dbhandler = new dbHandler(this, null);

        historiqueListView = (ListView) findViewById(R.id.historique_list_view);
        printItem();

        /*
        *
        * gère le tabhost
        * */
        final TabHost tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec tabspec = tabhost.newTabSpec("Accueil");
        tabspec.setContent(R.id.AccueilTab);
        tabspec.setIndicator("Accueil");
        tabhost.addTab(tabspec);

        tabspec = tabhost.newTabSpec("Historique");
        tabspec.setContent(R.id.HistoriqueTab);
        tabspec.setIndicator("Historique");
        tabhost.addTab(tabspec);

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabID) {
                tabhost.clearAnimation();
            }
        });

        /*
        *
        * fin gestion tabhost
        * */


        /*
        *
        * crée et lance le thread qui simule verifie si l'alerte est simulé ou non
        *
        * */
        final Runnable verifs = new Runnable() {

            @Override
            public void run() { //la fonction qui va ettre appelé quand le thread sera lancé
                while (true){ //le thread doit toujours etre actif
                    if(!alerte){ //que doit faire la page d'accueil si il y a une alerte ou pas
                        findViewById(R.id.AccueilTab).post(new Runnable() { //requis
                            @Override
                            public void run() { //si il n'y a pas d'alerte
                                findViewById(R.id.AccueilAlert).setVisibility(View.GONE); //cacher la page alerte
                                findViewById(R.id.AccueilNoAlert).setVisibility(View.VISIBLE); //afficher la page noAlerte
                            }
                        });
                    }
                    else{
                        findViewById(R.id.AccueilTab).post(new Runnable() { //requis
                            @Override
                            public void run() { //si il y a une alerte
                                findViewById(R.id.AccueilAlert).setVisibility(View.VISIBLE); //afficher la page alerte
                                findViewById(R.id.AccueilNoAlert).setVisibility(View.GONE); //cacher la page noAlerte
                            }
                        });
                    }
                    try {
                        Thread.sleep(100); // attends pendant 1 seconde avant de refaire un tour de boucle
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread_verifs = new Thread(verifs); //crée le thread de vérifications
        thread_verifs.start(); //lance le thread de verifications

        /*
        *
        * fin création et lancement thread de verif
        *
        * */

        ImageView image_type;
        image_type = (ImageView) findViewById(R.id.type_image);
        image_type.setImageResource(R.drawable.type_incendie);

    }

    public void Consignes(View view){
        dbhandler.deleteAlerte();
        printItem();
        Intent i = new Intent(this, Consignes.class);
        startActivity(i);
    }

    public void Indications(View view){
        printItem();
        Intent i = new Intent(this, Indications.class);
        startActivity(i);
    }

    //
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    //


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            alerte = !alerte;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void printItem(){
        HistoriqueItem[] test = dbhandler.databaseToItem();
        ListAdapter listAdapter = new HistoriqueAdapter(this, test);
        historiqueListView.setAdapter(listAdapter);
    }

    public void addButtonClicked(View view){
        Alerte alerte = new Alerte("titre", "desc");
        dbhandler.addAlerte(alerte);
        printItem();
    }

    public void deleteButtonClicked(View view){
        dbhandler.deleteAlerte();
        printItem();
    }
}
