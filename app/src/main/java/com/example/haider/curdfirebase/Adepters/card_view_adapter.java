package com.example.haider.curdfirebase.Adepters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.haider.curdfirebase.R;
import com.example.haider.curdfirebase.dataHolder.DataHolder;
import com.example.haider.curdfirebase.dataHolder.IdHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class card_view_adapter extends RecyclerView.Adapter<card_view_adapter.MyViewHoledr> {
    Context context;
    ArrayList<DataHolder> dataHolders;
    ArrayList<IdHolder> ids;
    public card_view_adapter(Context context, ArrayList<DataHolder> dataHolders, ArrayList<IdHolder> ids) {
        this.context = context;
        this.dataHolders = dataHolders;
        this.ids=ids;

    }

    @NonNull
    @Override
    public MyViewHoledr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_view_layout,parent,false);
        return new MyViewHoledr(v);
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHoledr holder, int position) {
        DataHolder data=dataHolders.get(position);
        holder.name.setText(data.getName());
        holder.course.setText(data.getCourse());
        holder.email.setText(data.getEmail());

        Glide.with(context).load(data.getProfile()).
                placeholder(R.drawable.person_three)
                .error(R.drawable.img)
                .into(holder.profile);

        //-----------Updating the Existing Data-------------------//
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.data_adding_layout);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                Toast.makeText(context, ids.get(position).getID(), Toast.LENGTH_SHORT).show();

                EditText n=dialog.findViewById(R.id.addName);
                EditText e=dialog.findViewById(R.id.addEmail);
                EditText c=dialog.findViewById(R.id.addCourse);
                EditText p=dialog.findViewById(R.id.addProfile);

                n.setText(data.getName());
                e.setText(data.getEmail());
                c.setText(data.getCourse());
                p.setText(data.getProfile());
                //---------UPDATING PROCESS STARTED------------//
                dialog.show();
                dialog.findViewById(R.id.btnAddSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",n.getText()+"");
                        map.put("email",e.getText()+"");
                        map.put("course",c.getText()+"");
                        map.put("profile",p.getText()+"");

                        FirebaseDatabase.getInstance().getReference().child("Student").child(ids.get(position).getID())
                                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onSuccess(Void unused) {
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                        Toast.makeText(context, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

            }
        });

        //-----------------------------------------------//

    }

    @Override
    public int getItemCount() {
        return dataHolders.size();
    }

    public class MyViewHoledr extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView course;
        ImageView profile;
        TextView btnEdit;
        public MyViewHoledr(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.txtName);
            email=itemView.findViewById(R.id.txtEmail);
            course=itemView.findViewById(R.id.txtCourse);
            profile=itemView.findViewById(R.id.imgProfile);
            btnEdit=itemView.findViewById(R.id.txtEdit);

        }
    }
}
