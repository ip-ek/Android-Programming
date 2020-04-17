package com.ipk.mobilodev;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipk.mobilodev.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {
    ArrayList<Person> personList;
    LayoutInflater inflater;

    public PersonAdapter(ArrayList<Person> personList, Context context) {
        this.personList = personList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_person, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Person selectedPerson=personList.get(position);

        holder.userName.setText(personList.get(position).getUserName());
        holder.password.setText(personList.get(position).getPassword());
        holder.image.setImageResource(personList.get(position).getImageId());

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, password;
        ImageView image;
        CheckBox button;

        public MyViewHolder(View itemView){
            super(itemView);
            userName=(TextView) itemView.findViewById(R.id.card_username);
            password=(TextView) itemView.findViewById(R.id.card_password);
            image =(ImageView) itemView.findViewById(R.id.card_image);
            button=itemView.findViewById(R.id.card_pw_visible);
            //başlangıçta yıldızlı olması için
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            //onclickListener kullanırsan ilk seferde kontrol yapamazsın!
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("deneme","içerde");
                    if(isChecked){
                        Log.d("deneme", "işaretli");
                        password.setCursorVisible(isChecked);
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else{
                        Log.d("deneme", "işaretli değil");
                        password.setCursorVisible(isChecked);
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            });
        }
    }

}
