package ptut_sdis30.popalerte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class Indications extends AppCompatActivity implements SensorEventListener {
    //Thread thread_boussole;

    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) findViewById(R.id.boussole);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);

            ra.setFillAfter(true);

            mPointer.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

        /*final ImageView image_boussole;
        image_boussole = (ImageView) findViewById(R.id.boussole);//crée et récupère l'image de la boussole

        image_boussole.setRotationX(image_boussole.getDrawable().getBounds().width() / 2); //définie le point de rotation de l'image
        image_boussole.setRotationY(image_boussole.getDrawable().getBounds().height() / 2);//de la boussole au centre de l'image

        Runnable r = new Runnable() {
            float rot = 0;
            @Override
            public void run() {
                /**************
                 * boucle while si la page d'indications est affiché
                 ***********//*
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

    }*/

    public void Retour(View view){
        Intent i = new Intent(this, Accueil.class);
        startActivity(i);
        //thread_boussole.interrupt();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}