package in.swapsha96.paid.todo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageButton button = (ImageButton)findViewById(R.id.button2);
        final EditText editText = (EditText)findViewById(R.id.editText2);
        final ListView listView = (ListView)findViewById(R.id.listView2);
        final DBManager dbManager = new DBManager(SecondActivity.this,null,null,1);
        final String[] strings;
        strings = dbManager.showItems(list);
        ArrayAdapter arrayAdapter = new ArrayAdapter(SecondActivity.this,android.R.layout.simple_list_item_1, strings){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view;
                if(convertView==null) {
                    LayoutInflater Inflater = (LayoutInflater) SecondActivity.this.getSystemService(SecondActivity.this.LAYOUT_INFLATER_SERVICE);
                    view = Inflater.inflate(android.R.layout.simple_list_item_1, null);
                }
                else
                {
                    view=convertView;
                }
                if(dbManager.checkItem(list,strings[position]) == 1)
                {
                    view.setBackgroundColor(Color.parseColor("#2196f3"));
                }
                else{
                    view.setBackgroundColor(Color.parseColor("#e3f2fd"));
                }
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(strings[position]);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0) {
                    if (dbManager.addItem(list,editText.getText().toString().trim()) == true)
                        ;
                    else
                        Toast.makeText(SecondActivity.this, "Item is already added.", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                    String[] string = dbManager.showItems(list);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(SecondActivity.this, android.R.layout.simple_list_item_1, string);
                    listView.setAdapter(arrayAdapter);
                }
                else{
                    Toast.makeText(SecondActivity.this, "Enter item name first!", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SecondActivity.this,"Enter the name and touch to add the item.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(dbManager.toggleCheck(list,parent.getItemAtPosition(position).toString()) == 1){
                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#2196f3"));
                    Toast.makeText(SecondActivity.this,"Checked!",Toast.LENGTH_SHORT).show();
                }
                else {
                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#e3f2fd"));
                    Toast.makeText(SecondActivity.this,"Unchecked!",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
