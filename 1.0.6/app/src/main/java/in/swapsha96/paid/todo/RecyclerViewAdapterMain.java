package in.swapsha96.paid.todo;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerViewAdapterMain extends RecyclerView.Adapter<RecyclerViewAdapterMain.ViewHolder> {

    String[] strings;
    DBManager dbManager;

    public RecyclerViewAdapterMain(String[] arrayList) {
        this.strings = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(strings[position]);
        dbManager = new DBManager(holder.itemView.getContext(),null,null,1);
        switch (dbManager.getPriority(strings[position])){
            case 0 :
                holder.priority.setText(" L ");
                holder.priority.setTextColor(Color.parseColor("#2962ff"));
                break;
            case 1 :
                holder.priority.setText(" M ");
                holder.priority.setTextColor(Color.parseColor("#00c853"));
                break;
            case 2 :
                holder.priority.setText(" H ");
                holder.priority.setTextColor(Color.parseColor("#d50000"));
                break;
        }
        holder.priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] option = {"LOW","MEDIUM","HIGH"};
                final AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
                alert.setTitle("Set Priority:");
                alert.setSingleChoiceItems(option,-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if(option[which]=="LOW") {
                                    dbManager.setPriority(strings[position],0);
                                }
                                if(option[which]=="MEDIUM") {
                                    dbManager.setPriority(strings[position],1);
                                }
                                if(option[which]=="HIGH") {
                                    dbManager.setPriority(strings[position],2);
                                }
                                strings = dbManager.showLists();
                                RecyclerViewAdapterMain.this.notifyDataSetChanged();
                            }
                        });
                alert.show();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),SecondActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("list",strings[position]);
                intent.putExtras(bundle);
                holder.itemView.getContext().startActivity(intent);
                Activity activity = (Activity) holder.itemView.getContext();
                activity.overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dbManager = new DBManager(holder.itemView.getContext(),null,null,1);
                                dbManager.deleteList(strings[position]);
                                strings = dbManager.showLists();
                                RecyclerViewAdapterMain.this.notifyDataSetChanged();
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
        TextView priority;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView2);
            textView = (TextView) itemView.findViewById(R.id.textView2);
            priority = (TextView) itemView.findViewById(R.id.priority);
        }
    }
}
