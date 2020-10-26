package mx.cannavita.db;


import android.database.sqlite.SQLiteDatabase;

public class TablesManager {

    private SQLiteDatabase db;
    TablesManager(SQLiteDatabase db){
        this.db=db;
    }

    public void createTables() {

        db.execSQL("CREATE TABLE IF NOT EXISTS  Objetos(" +
                "IdObject TEXT, " +
                "Cadena TEXT, " +
                "Posicion TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS  ObjectList(" +
                "Fecha TEXT, " +
                "Detalle  TEXT, " +
                "Lista TEXT)");


    }


}
