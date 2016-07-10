package in.swapsha96.paid.todo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

public class SecondActivity extends AppCompatActivity {

    private AdView mAdView;

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
        setTitle(list);
        ImageButton button = (ImageButton)findViewById(R.id.button2);
        final EditText editText = (EditText)findViewById(R.id.editText2);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));
        final DBManager dbManager = new DBManager(SecondActivity.this,null,null,1);
        final String[] strings;
        strings = dbManager.showItems(list);
        RecyclerViewAdapterSecond recyclerViewAdapter = new RecyclerViewAdapterSecond(list,strings);
//        {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//
//                View view;
//                if(convertView==null) {
//                    LayoutInflater Inflater = (LayoutInflater) SecondActivity.this.getSystemService(SecondActivity.this.LAYOUT_INFLATER_SERVICE);
//                    view = Inflater.inflate(android.R.layout.simple_list_item_1, null);
//                }
//                else
//                {
//                    view=convertView;
//                }
//                if(dbManager.checkItem(list,strings[position]) == 1)
//                {
//                    view.setBackgroundColor(Color.parseColor("#2196f3"));
//                }
//                else{
//                    view.setBackgroundColor(Color.parseColor("#e3f2fd"));
//                }
//                TextView textView = (TextView) view.findViewById(android.R.id.text1);
//                textView.setText(strings[position]);
//                return view;
//            }
//        };
        recyclerView.setAdapter(recyclerViewAdapter);
        registerForContextMenu(recyclerView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0) {
                    if (dbManager.addItem(list,editText.getText().toString().trim()) == true)
                        ;
                    else {
                        final Toast toast = Toast.makeText(SecondActivity.this, "Item is already added.", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 800);
                    }
                    editText.setText("");
                    String[] strings = dbManager.showItems(list);
                    RecyclerViewAdapterSecond recyclerViewAdapter = new RecyclerViewAdapterSecond(list,strings);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    registerForContextMenu(recyclerView);
                }
                else{
                    final Toast toast = Toast.makeText(SecondActivity.this, "Enter item name first!", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 800);
                    editText.setText("");
                }
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Toast toast = Toast.makeText(SecondActivity.this,"Enter the name and touch to add the item.",Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 800);
                return true;
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(dbManager.toggleCheck(list,parent.getItemAtPosition(position).toString()) == 1){
//                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#2196f3"));
//                }
//                else {
//                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#e3f2fd"));
//                }
//            }
//        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which){
//                            case DialogInterface.BUTTON_POSITIVE:
//                                dbManager.deleteItem(list,parent.getItemAtPosition(position).toString());
//                                String[] string = dbManager.showItems(list);
//                                ArrayAdapter arrayAdapter = new ArrayAdapter(SecondActivity.this,android.R.layout.simple_list_item_1,string);
//                                listView.setAdapter(arrayAdapter);
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                //No button clicked
//                                break;
//                        }
//                    }
//                };
//                AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
//                builder.setMessage("Are you sure that you want to delete \""+parent.getItemAtPosition(position).toString()+"\"?").setPositiveButton("Yes", dialogClickListener)
//                        .setNegativeButton("No", dialogClickListener).show();
//                return true;
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
    }
}
