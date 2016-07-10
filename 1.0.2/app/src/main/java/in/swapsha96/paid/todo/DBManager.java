package in.swapsha96.paid.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "todo.db", factory, 2);
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
        DatabaseUtils.sqlEscapeString(list);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int count = c.getCount();
        if (count>0)
            return false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("list", list);
        db.insert("todo_lists", null, contentValues);
        c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));

        db.execSQL("CREATE TABLE IF NOT EXISTS todo_lists_"+id+" (id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, checkbox TEXT);");
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
        DatabaseUtils.sqlEscapeString(list);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        db.execSQL("DELETE FROM todo_lists WHERE list = \""+list+"\";");
        db.execSQL("DROP TABLE IF EXISTS  todo_lists_"+id+";");
    }

    public String[] showItems(String list){
        DatabaseUtils.sqlEscapeString(list);
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        Cursor cursor =  db.rawQuery( "SELECT * FROM todo_lists_"+id+";", null);
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
        DatabaseUtils.sqlEscapeString(list);
        DatabaseUtils.sqlEscapeString(item);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        c =  db.rawQuery( "SELECT * FROM todo_lists_"+id+" WHERE item = \""+item+"\";", null);
        c.moveToFirst();
        int count = c.getCount();
        if (count>0)
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put("item", item);
        contentValues.put("checkbox", "0");
        db.insert("todo_lists_"+id, null, contentValues);
        return true;
    }

    public void deleteItem(String list, String item){
        DatabaseUtils.sqlEscapeString(list);
        DatabaseUtils.sqlEscapeString(item);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        db.execSQL("DELETE FROM todo_lists_"+id+" WHERE item = \""+item+"\";");
    }

    public int toggleCheck(String list, String item){
        int value;
        DatabaseUtils.sqlEscapeString(list);
        DatabaseUtils.sqlEscapeString(item);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        c =  db.rawQuery("SELECT * FROM todo_lists_"+id+" WHERE item = \""+item+"\";",null);
        c.moveToFirst();
        value = c.getInt(c.getColumnIndex("checkbox"));
        if (value == 0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("item", item);
            contentValues.put("checkbox", "1");
            db.update("todo_lists_"+id,contentValues,"item = \"" + item + "\"",null);
            c =  db.rawQuery("SELECT * FROM todo_lists_"+id+" WHERE item = \""+item+"\";",null);
            c.moveToFirst();
            value = c.getInt(c.getColumnIndex("checkbox"));
            c.close();
        }
        else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("item", item);
            contentValues.put("checkbox", "0");
            db.update("todo_lists_"+id,contentValues,"item = \"" + item + "\"",null);
            c =  db.rawQuery("SELECT * FROM todo_lists_"+id+" WHERE item = \""+item+"\";",null);
            c.moveToFirst();
            value = c.getInt(c.getColumnIndex("checkbox"));
            c.close();
        }
        c.close();
        return value;
    }

    public int checkItem(String list, String item){
        int value;
        DatabaseUtils.sqlEscapeString(list);
        DatabaseUtils.sqlEscapeString(item);
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT * FROM todo_lists WHERE list = \""+list+"\";", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        c =  db.rawQuery("SELECT * FROM todo_lists_"+id+" WHERE item = \""+item+"\";",null);
        c.moveToFirst();
        value = c.getInt(c.getColumnIndex("checkbox"));
        c.close();
        return value;
    }
}