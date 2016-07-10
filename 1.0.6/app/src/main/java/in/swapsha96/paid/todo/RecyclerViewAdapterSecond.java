package in.swapsha96.paid.todo;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class RecyclerViewAdapterSecond extends RecyclerView.Adapter<RecyclerViewAdapterSecond.ViewHolder> {

    String list;
    String[] strings;

    public RecyclerViewAdapterSecond(String list,String[] arrayList) {
        this.list = list;
        this.strings = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DBManager dbManager = new DBManager(holder.itemView.getContext(),null,null,1);
        holder.textView.setText(strings[position]);
        if(dbManager.checkItem(list,strings[position]) == 1)
        {
            holder.checkBox.setChecked(true);
        }
        else{
            holder.checkBox.setChecked(false);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager dbManager = new DBManager(holder.itemView.getContext(),null,null,1);
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                if(dbManager.toggleCheck(list,strings[position]) == 1){
                    ;
                }
                else {
                    ;
                }
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager dbManager = new DBManager(holder.itemView.getContext(),null,null,1);
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dbManager.deleteItem(list,strings[position]);
                                strings = dbManager.showItems(list);
                                RecyclerViewAdapterSecond.this.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Are you sure that you want to delete \""+strings[position]+"\"?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return strings.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView2);
            cardView = (CardView) itemView.findViewById(R.id.cardView2);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
