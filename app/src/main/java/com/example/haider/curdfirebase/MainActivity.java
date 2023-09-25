package com.example.haider.curdfirebase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haider.curdfirebase.Adepters.card_view_adapter;
import com.example.haider.curdfirebase.dataHolder.DataHolder;
import com.example.haider.curdfirebase.dataHolder.IdHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DataHolder> dataHolders;
    DatabaseReference databaseReference;
    card_view_adapter adapter;
    FloatingActionButton addButton;
    ArrayList<IdHolder> ids;


    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.mainRec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataHolders=new ArrayList<>();
        ids=new ArrayList<>();
        //-----------Add to Firebase--------------//
        addButton=findViewById(R.id.btnAddData);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.data_adding_layout);
                dialog.show();
                Button save=dialog.findViewById(R.id.btnAddSave);
                //-----------Adding New Data-----------------//
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         TextView n=dialog.findViewById(R.id.addName);
                         TextView e=dialog.findViewById(R.id.addEmail);
                         TextView c=dialog.findViewById(R.id.addCourse);
                         TextView p=dialog.findViewById(R.id.addProfile);
                         dataHolders.clear();

//                         String key=databaseReference.push().getKey();
//                         Toast.makeText(MainActivity.this, key, Toast.LENGTH_SHORT).show();
                         DataHolder dat=(new DataHolder(n.getText().toString(),
                                 e.getText().toString(),
                                 c.getText().toString(),
                                 p.getText().toString()
                         ));
                        ProgressDialog dialog1=new ProgressDialog(MainActivity.this);
                        dialog1.setTitle("Uploading");
                        dialog1.setMessage("please be patient...");
                        dialog1.show();
                        //push() generates a key
                         databaseReference.push().setValue(dat).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(MainActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                 }
                                 else {
                                     Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                 }
                                 dialog1.dismiss();
                                 dialog.dismiss();
                             }
                         });
                    }
                });
            }
        });
        //----------------------------------//
        //---------Connecting With FireBase(1)-------------//

        databaseReference= FirebaseDatabase.getInstance().getReference("Student");

        //----------Retrieve from Firebase(2)--------------//
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String name=snapshot.child("s1").child("name").getValue(String.class)+"";
//                Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                for(DataSnapshot snap : snapshot.getChildren()){
                    String id=snap.getKey();
                    String name= snap.child("name").getValue(String.class);
                    String email= snap.child("email").getValue(String.class);
                    String course=snap.child("course").getValue(String.class);
                    String profile=snap.child("profile").getValue(String.class);
                    dataHolders.add(new DataHolder(name,email,course,profile));
                    //-------adding the id in parallel---------//
                    ids.add(new IdHolder(id));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter=new card_view_adapter(MainActivity.this,dataHolders,ids);
        dataHolders.clear();
        recyclerView.setAdapter(adapter);
    }
}