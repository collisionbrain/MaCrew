package mx.cannavita.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mx.cannavita.model.Objects;


public class DbHelper extends SQLiteOpenHelper {
    public static final String nombreDB = "database.db";

    public DbHelper(Context context)
    {
        super(context, nombreDB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TablesManager tablesManager=new TablesManager(db);
        tablesManager.createTables();
    }
    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public long insertObjects(String IdObject,String Cadena,String Posicion ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vObjects = new ContentValues();
        vObjects.put("IdObject", IdObject);
        vObjects.put("Cadena", Cadena);
        vObjects.put("Posicion", Posicion);
        long res=db.insert("Objetos", null, vObjects);
        db.close();
        return res;
    }
    public List<Objects> listaObjetos(){
        List<Objects> listaObjects = new ArrayList<>();
         SQLiteDatabase db = this.getWritableDatabase();
        String QRY="SELECT * FROM Objetos";
        Cursor crObj= db.rawQuery(QRY, null);

        if (crObj != null) {
            if (crObj.moveToFirst()) {
                do{

                    Objects objectSingle=new Objects();
                    objectSingle.IdObject=crObj.getString(0);
                    objectSingle.Cadena=crObj.getString(1);
                    objectSingle.Posicion=crObj.getString(2);
                    listaObjects.add(objectSingle);

                }while(crObj.moveToNext());
            }
        }
        crObj.close();
        return listaObjects;
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = this.getReadableDatabase();
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

}
