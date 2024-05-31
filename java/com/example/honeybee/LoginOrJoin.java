package com.example.honeybee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginOrJoin {
    private FirebaseAuth firebaseAuth;

    FirebaseUser fireUser = firebaseAuth.getCurrentUser();
    String phone = fireUser.getPhoneNumber();

}
