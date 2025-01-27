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

    // Modificado para aceptar un callback
    public void agregarUsuario(String email, String password, RegistroCallback callback) {
        // Validamos que el correo sea correcto
        if (isEmailValid(email)) {
            mauth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Si el registro es exitoso, mostramos el Toast y llamamos al callback
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            callback.onRegistroExitoso(); // Notificamos que el registro fue exitoso
                        } else {
                            // Si el registro falla, mostramos el Toast y llamamos al callback
                            Toast.makeText(context, "Error en registro", Toast.LENGTH_SHORT).show();
                            callback.onRegistroFallido(); // Notificamos que el registro falló
                        }
                    });
        } else {
            // Si el correo no es válido, mostramos un Toast y llamamos al callback
            Toast.makeText(context, "El correo electrónico no es válido", Toast.LENGTH_SHORT).show();
            callback.onRegistroFallido(); // Notificamos que el registro falló
        }
    }

    // Método para verificar que el correo es válido (limitarlo a ciertos dominios)
    private boolean isEmailValid(String email) {
        return email.contains("@gmail.com") || email.contains("@outlook.com");
    }

    // Método para iniciar sesión
    public void iniciarSesion(String email, String password, OnLoginResultCallBack callBack) {
        mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        callBack.onSuccess();
                    } else {
                        // Error al iniciar sesión
                        callBack.onFailure();
                    }
                });
    }

    public void cerrarSesion (){
        mauth.signOut();  // Cierra la sesión del usuario en Firebase
        Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
    }

    // Interfaz para manejar resultados del inicio de sesión
    public interface OnLoginResultCallBack {
        void onSuccess();
        void onFailure();
    }

    // Nueva interfaz para manejar los resultados del registro
    public interface RegistroCallback {
        void onRegistroExitoso();
        void onRegistroFallido();
    }
}
