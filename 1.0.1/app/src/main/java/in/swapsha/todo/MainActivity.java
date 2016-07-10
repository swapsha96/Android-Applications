package in.swapsha.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import in.swapsha.todopaid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button button = (Button)findViewById(R.id.button2);
        final EditText editText = (EditText)findViewById(R.id.editText);
        final ListView listView = (ListView)findViewById(R.id.listView);
        final DBManager dbManager = new DBManager(MainActivity.this,null,null,1);
        String[] strings;
        strings = dbManager.showLists();
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,strings);
        listView.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0) {
                    if (dbManager.addList(editText.getText().toString().trim()) == true)
                        Toast.makeText(MainActivity.this, "List added!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Same name list is already added.", Toast.LENGTH_LONG).show();
                    editText.setText("");
                    String[] string = dbManager.showLists();
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, string);
                    listView.setAdapter(arrayAdapter);

                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else{
                    Toast.makeText(MainActivity.this, "Enter list name first!", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("list",parent.getItemAtPosition(position).toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dbManager.deleteList(parent.getItemAtPosition(position).toString());
                String[] string = dbManager.showLists();
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,string);
                listView.setAdapter(arrayAdapter);
                return true;
            }
        });
    }
}
