package com.example.fastbrain;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ConexionBBDD {

    private FirebaseAuth mauth;
    private Context context;

    // Constructor que recibe el contexto de la actividad
    public ConexionBBDD(Context context) {
        this.context = context;
        mauth = FirebaseAuth.getInstance();
    }

    public void agregarUsuario(String email, String password) {
        //validamos que el correo sea correcto
        if (isEmailValid(email)) {
            mauth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error en registro", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "El correo electrónico no es válido" , Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValid(String email) {
        // Aquí puedes usar un patrón más flexible, o limitarlo a ciertos dominios
        return email.contains("@gmail.com") || email.contains("@outlook.com") ;
    }

    //Método para iniciar sesión

    public void iniciarSesion (String email, String password, OnLoginResultCallBack callBack){
        mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        //Inicio de sesión exitoso
                        callBack.onSuccess();
                    } else {
                        //Error al iniciar sesión
                        callBack.onFailure();
                    }
                });
    }

    //Interfaz para manejar resultados del inicio de sesión

    public interface OnLoginResultCallBack {
        void onSuccess();
        void onFailure();
    }


}
