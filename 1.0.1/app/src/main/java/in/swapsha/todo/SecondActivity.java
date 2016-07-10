package in.swapsha.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import in.swapsha.todopaid.R;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        final String list = bundle.getString("list");
        Button button = (Button)findViewById(R.id.button2);
        final EditText editText = (EditText)findViewById(R.id.editText2);
        final ListView listView = (ListView)findViewById(R.id.listView2);
        final DBManager dbManager = new DBManager(SecondActivity.this,null,null,1);
        String[] strings;
        strings = dbManager.showItems(list);
        ArrayAdapter arrayAdapter = new ArrayAdapter(SecondActivity.this,android.R.layout.simple_list_item_1,strings);
        listView.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0) {
                    if (dbManager.addItem(list,editText.getText().toString().trim()) == true)
                        ;
                    else
                        Toast.makeText(SecondActivity.this, "Item is already added.", Toast.LENGTH_LONG).show();
                    editText.setText("");
                    String[] string = dbManager.showItems(list);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(SecondActivity.this, android.R.layout.simple_list_item_1, string);
                    listView.setAdapter(arrayAdapter);
                }
                else{
                    Toast.makeText(SecondActivity.this, "Enter item name first!", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(SecondActivity.this, dbManager.checkItem(list,parent.getItemAtPosition(position).toString())+"", Toast.LENGTH_LONG).show();
////                if(dbManager.checkItem(list,parent.getItemAtPosition(position).toString()) == true)
////                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#2196f3"));
////                else
////                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#e3f2fd"));
//            }
//        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dbManager.deleteItem(list,parent.getItemAtPosition(position).toString());
                String[] string = dbManager.showItems(list);
                ArrayAdapter arrayAdapter = new ArrayAdapter(SecondActivity.this,android.R.layout.simple_list_item_1,string);
                listView.setAdapter(arrayAdapter);
                return true;
            }
        });
    }
}
