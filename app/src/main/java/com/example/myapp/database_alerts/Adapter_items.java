package com.example.myapp.database_alerts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

import java.util.List;

public class Adapter_items extends RecyclerView.Adapter<Adapter_items.MyViewHolder> {

    List<Contacts> contacts;

    public Adapter_items(List<Contacts> contacts) {
        this.contacts = contacts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView name,phone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Contacts contact = contacts.get(i);
        myViewHolder.name.setText(contact.getFirstname()+ "\n" + "ID : " + contact.getPhoneNumber());
        myViewHolder.phone.setText( contact.getLastname());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


}
