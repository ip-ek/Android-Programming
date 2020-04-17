package com.ipk.mobilodev;

import com.ipk.mobilodev.R;

import java.util.ArrayList;

public class Person {
    //Kullanıcı adı, şifre, resim

    private String userName;
    private String password;
    private int imageId;

    public Person() {
    }

    public Person(String userName, String password, int imageId) {
        this.userName = userName;
        this.password = password;
        this.imageId = imageId;
    }

    public String getUserName() {
        return userName;
    }

    /*
    //username set edilemez
    public void setUserName(String userName) {
        this.userName = userName;
    }
*/
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static ArrayList<Person> getData(){
        ArrayList<Person> personList = new ArrayList<>();
        int personImages[] ={R.drawable.person_admin, R.drawable.person_bd, R.drawable.person_mag, R.drawable.person_mek};
        String userNames[] = {"Admin", "Banu Diri", "Amaç Güvensan", "Elif Karslıgil"};
        String passwords[] = {"0000", "bd01", "mag7", "mek3"};
        int i;
        Person tmp;
        for(i=0; i<personImages.length; i++){
            tmp =new Person(userNames[i],
                    passwords[i],
                    personImages[i]
                    );
            personList.add(tmp);
        }
        return personList;
    }



}
