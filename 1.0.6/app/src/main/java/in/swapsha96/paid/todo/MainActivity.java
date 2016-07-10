package in.swapsha96.paid.todo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton button = (ImageButton)findViewById(R.id.button2);
        final EditText editText = (EditText)findViewById(R.id.editText);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        final DBManager dbManager = new DBManager(MainActivity.this,null,null,1);
        final String[] strings;
        strings = dbManager.showLists();
        RecyclerViewAdapterMain recyclerViewAdapter = new RecyclerViewAdapterMain(strings);
        recyclerView.setAdapter(recyclerViewAdapter);
        registerForContextMenu(recyclerView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Toast toast;
                if(editText.getText().toString().trim().length() > 0) {
                    if (dbManager.addList(editText.getText().toString().trim()) == true)
                        toast = Toast.makeText(MainActivity.this, "List added!", Toast.LENGTH_SHORT);
                    else
                        toast = Toast.makeText(MainActivity.this, "Same name list is already added.", Toast.LENGTH_SHORT);
                    editText.setText("");
                    String[] strings = dbManager.showLists();
                    RecyclerViewAdapterMain recyclerViewAdapter = new RecyclerViewAdapterMain(strings);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    registerForContextMenu(recyclerView);

                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else{
                    toast = Toast.makeText(MainActivity.this, "Enter list name first!", Toast.LENGTH_SHORT);
                    editText.setText("");
                }
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 800);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Toast toast = Toast.makeText(MainActivity.this,"Enter the name and touch to add the list.",Toast.LENGTH_SHORT);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
    }
}
