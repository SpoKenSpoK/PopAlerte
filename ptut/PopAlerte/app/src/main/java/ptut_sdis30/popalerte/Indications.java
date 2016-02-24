package ptut_sdis30.popalerte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class Indications extends AppCompatActivity {
    Thread thread_boussole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView image_boussole;
        image_boussole = (ImageView) findViewById(R.id.boussole);//crée et récupère l'image de la boussole

        image_boussole.setRotationX(image_boussole.getDrawable().getBounds().width() / 2); //définie le point de rotation de l'image
        image_boussole.setRotationY(image_boussole.getDrawable().getBounds().height() / 2);//de la boussole au centre de l'image

        Runnable r = new Runnable() {
            float rot = 0;
            @Override
            public void run() {
                /**************
                 * boucle while si la page d'indications est affiché
                 ***********/
                try {
                    Thread.sleep(500); // attends pendant 10 millisecondes avant de refaire un tour de boucle
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true) {
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
            }
        };
        thread_boussole = new Thread(r);
        thread_boussole.start();

    }

    public void Retour(View view){
        Intent i = new Intent(this, Accueil.class);
        startActivity(i);
        thread_boussole.interrupt();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}