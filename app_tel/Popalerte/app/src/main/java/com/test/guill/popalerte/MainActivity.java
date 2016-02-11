package com.test.guill.popalerte;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.os.Vibrator;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ALERTES = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    private static final String DATABASE_NAME = "alertes.db";
    private static final int DATABASE_VERSION = 1;

    // Commande sql pour la création de la base de données
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ALERTES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i("MainActivity", "MySQLiteHelper");
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTES);
        onCreate(db);
    }
}

class Alerte {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Sera utilisée par ArrayAdapter dans la ListView
    @Override
    public String toString() {
        return name;
    }
}

class CommentsDataSource {

    // Champs de la base de données
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME};

    public CommentsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Alerte createAlerte(String name) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        long insertId = database.insert(MySQLiteHelper.TABLE_ALERTES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ALERTES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Alerte newAlerte = cursorToAlerte(cursor);
        cursor.close();
        return newAlerte;
    }

    public void deleteAlerte(Alerte alerte) {
        long id = alerte.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ALERTES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Alerte> getAllAlertes() {
        List<Alerte> alertes = new ArrayList<Alerte>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ALERTES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Alerte alerte= cursorToAlerte(cursor);
            alertes.add(alerte);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return alertes;
    }

    private Alerte cursorToAlerte(Cursor cursor) {
        Alerte alerte= new Alerte();
        alerte.setId(cursor.getLong(0));
        alerte.setName(cursor.getString(1));
        return alerte;
    }
}


class TestDatabaseActivity extends ListActivity {
    public CommentsDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "TestDatabaseActivity");
        super.onCreate(savedInstanceState);

        datasource = new CommentsDataSource(this);
        datasource.open();

        List<Alerte> values = datasource.getAllAlertes();

        // utilisez SimpleCursorAdapter pour afficher les
        // éléments dans une ListView
        ArrayAdapter<Alerte> adapter = new ArrayAdapter<Alerte>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Sera appelée par l'attribut onClick
    // des boutons déclarés dans main.xml
    public void onClick(View view) {
        Log.i("MainActivity", "onClick");
        ArrayAdapter<Alerte> adapter = (ArrayAdapter<Alerte>) getListAdapter();
        Alerte alerte= null;
        switch (view.getId()) {
            case R.id.add:
                String[] alertes = new String[] { "feu", "innondations", "accident" };
                int nextInt = new Random().nextInt(3);
                // enregistrer le nouveau commentaire dans la base de données
                alerte = datasource.createAlerte(alertes[nextInt]);
                //adapter.add(alerte);
                break;
            case R.id.delete:
                Log.i("MainActivity", String.valueOf(getListAdapter().getCount()));
                /*if (getListAdapter().getCount() > 0) {
                    alerte = (Alerte) getListAdapter().getItem(0);
                    datasource.deleteAlerte(alerte);
                    adapter.remove(alerte);
                }*/
                break;
        }
        //adapter.notifyDataSetChanged();
    }
}





public class MainActivity extends AppCompatActivity {
    private boolean alerte = true;
    TestDatabaseActivity database;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //cette fonction définie le comportement de l'application a son démarrage
        super.onCreate(savedInstanceState);
        database = new TestDatabaseActivity();
        setContentView(R.layout.activity_main); //la page par défaut est la page d'accueil
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new TestDatabaseActivity();

        /*************************************
         * comportement du TabHost
         ****************************/

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
         * thread qui effectuera des verifications régulières par rapport au arréviées de données et autres
         ***********************/

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

        /***********************************
         * comportement des boutons des pages
         **************/

        //declaration des bouttons
        Button btn_indications, btn_consignes, btn_retour_indications, btn_retour_consignes;

        /**************************************
         * comportement de la boussole
         **************************/

        final ImageView image_boussole;
        image_boussole = (ImageView) findViewById(R.id.boussole);//crée et récupère l'image de la boussole

        image_boussole.setRotationX(image_boussole.getDrawable().getBounds().width() / 2); //définie le point de rotation de l'image
        image_boussole.setRotationY(image_boussole.getDrawable().getBounds().height() / 2);//de la boussole au centre de l'image

        //boutton de redirection vers les indications depuis l'accueil
        btn_consignes = (Button) findViewById(R.id.indications_button);
        btn_consignes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Runnable r = new Runnable() { //runnable du thread de la boussole (fonction qui sera utilisé pour le thread)
                    float rot = 0; // réel de rotation contenant les degrés pour faire tourner la boussole
                    boolean end = false;
                    @Override
                    public void run() { //la fonction qui va ettre appeler quand le thread sera lancé
                        /**************
                         * boucle while si la page d'indications est affiché
                         ***********/
                        try {
                            Thread.sleep(500); // attends pendant 10 millisecondes avant de refaire un tour de boucle
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while (!end) {
                            if(findViewById(R.id.Indications).getVisibility() == View.VISIBLE) {
                                try {
                                    Thread.sleep(10); // attends pendant 10 millisecondes avant de refaire un tour de boucle
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                image_boussole.post(new Runnable() { //fonction de l'image pour la faire tourner
                                    @Override
                                    public void run() {
                                        rot = rot + 1; //augmente les degrés de rotation de 1 pour fair tourner la boussole
                                        image_boussole.setRotation(rot); //fait tourner l'image de "rot" degrés depuis la premiere position
                                    }
                                });
                            }
                            else{
                                end = true;
                            }
                        }
                    }
                };
                Thread boussole_thread = new Thread(r);
                boussole_thread.start();

                tabHost.setVisibility(View.GONE); //cache l'accueil
                findViewById(R.id.Indications).setVisibility(View.VISIBLE);//affiche le layout des indications
            }
        });

        /**********************************/

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
         * l'image dans l'accueil du type d'alerte
         ************************/

        ImageView image_type;
        image_type = (ImageView) findViewById(R.id.type_image);
        image_type.setImageResource(R.drawable.type_incendie);
    }

    public void onClick(View view){
        database.onClick(view);
    }

    @Override
    protected void onResume() { //fonction appelé quand on reviens sur l'application apres avoir tapé sur le boutton "home"
        database = new TestDatabaseActivity();
        super.onResume();
    }

    @Override
    protected void onPause() { //fonction appelé aprés avoir tapé sur le boutton "home"
        super.onPause();
        database.datasource.close();
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

