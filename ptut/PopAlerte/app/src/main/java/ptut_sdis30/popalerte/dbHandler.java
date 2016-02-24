package ptut_sdis30.popalerte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "popAlerte.db";
    private static final String TABLE_ALERTE = "alertes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ALERTE_NAME = "_alertename";
    private static final String COLUMN_ALERTE_DESC = "_description";

    public dbHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ALERTE + "( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ALERTE_NAME + " TEXT, " +
                COLUMN_ALERTE_DESC + " TEXT " +
                " );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTE + ";");
        onCreate(db);
    }

    public void addAlerte(Alerte alerte){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALERTE_NAME, alerte.get_alertename());
        values.put(COLUMN_ALERTE_DESC, alerte.get_description());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ALERTE, null, values);
        db.close();
    }

    public void deleteAlerte(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ALERTE + " WHERE 1;");
        db.close();
    }

    public HistoriqueItem[] databaseToItem(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ALERTE + " WHERE 1;";

        Cursor c = db.rawQuery(query, null);

        HistoriqueItem[] itemTab = new HistoriqueItem[c.getCount()];

        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_ALERTE_NAME))!=null){
                itemTab[c.getPosition()] = new HistoriqueItem(c.getString(c.getColumnIndex(COLUMN_ALERTE_NAME)) + c.getPosition(), c.getString(c.getColumnIndex(COLUMN_ALERTE_DESC)) + c.getPosition());
            }
            c.moveToNext();
        }
        db.close();
        return itemTab;
    }
}