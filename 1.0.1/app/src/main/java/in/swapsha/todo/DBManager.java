package in.swapsha.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "todo.db", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS todo_lists (id INTEGER PRIMARY KEY AUTOINCREMENT, list TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS todo_lists");
        onCreate(db);
    }

    public boolean addList(String list){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = '"+list+"';", null);
        c.moveToFirst();
        int count = c.getCount();
        if (count>0)
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put("list", list);
        db.insert("todo_lists", null, contentValues);
        db.execSQL("CREATE TABLE IF NOT EXISTS todo_lists_"+list.replaceAll(" ","_")+" (id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, checkbox TEXT);");
        return true;
    }

    public String[] showLists(){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "SELECT * FROM todo_lists;", null);
        cursor.moveToFirst();
        String[] strings = new String[cursor.getCount()];
        while(cursor.isAfterLast() == false){
            strings[i] = cursor.getString(cursor.getColumnIndex("list"));
            i++;
            cursor.moveToNext();
        }
        return strings;
    }

    public void deleteList(String list){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM todo_lists WHERE list = '"+list+"';");
        db.execSQL("DROP TABLE IF EXISTS  todo_lists_"+list.replaceAll(" ","_")+";");
    }

    public String[] showItems(String list){
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "SELECT * FROM todo_lists_"+list.replaceAll(" ","_")+";", null);
        cursor.moveToFirst();
        String[] strings = new String[cursor.getCount()];
        while(cursor.isAfterLast() == false){
            strings[i] = cursor.getString(cursor.getColumnIndex("item"));// + cursor.getString(cursor.getColumnIndex("checkbox"));
            i++;
            cursor.moveToNext();
        }
        return strings;
    }

    public boolean addItem(String list, String item){
        list = list.replaceAll(" ","_");
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists_"+list+" WHERE item = '"+item+"';", null);
        c.moveToFirst();
        int count = c.getCount();
        if (count>0)
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put("item", item);
        contentValues.put("checkbox", "no");
        db.insert("todo_lists_"+list, null, contentValues);
        return true;
    }

    public void deleteItem(String list, String item){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM todo_lists_"+list.replaceAll(" ","_")+" WHERE item = '"+item+"';");
    }

    public String checkItem(String list, String item){
        list = list.replaceAll(" ","_");
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor =  db.rawQuery( "SELECT * FROM todo_lists_"+list+" WHERE item = '"+item+"';", null);
        cursor.moveToFirst();
        if (cursor.getString(cursor.getColumnIndex("checkbox")).toString() == "yes") {
            db.execSQL("DELETE FROM todo_lists_" + list.replaceAll(" ", "_") + " WHERE item = '" + item + "';");
            ContentValues contentValues = new ContentValues();
            contentValues.put("item", item);
            contentValues.put("checkbox", "no");
            db.insert("todo_lists_" + list, null, contentValues);
        }
        else {
            db.execSQL("DELETE FROM todo_lists_" + list.replaceAll(" ", "_") + " WHERE item = '" + item + "';");
            ContentValues contentValues = new ContentValues();
            contentValues.put("item", item);
            contentValues.put("checkbox", "yes");
            db.insert("todo_lists_" + list, null, contentValues);
        }

        cursor =  db.rawQuery( "SELECT * FROM todo_lists_"+list+" WHERE item = '"+item+"';", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("checkbox")).toString();
    }
}
