package com.example.fastbrain;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConexionBBDD {

    private FirebaseAuth mauth;
    private Context context;

    // Constructor que recibe el contexto de la actividad
    public ConexionBBDD(Context context) {
        this.context = context;
        mauth = FirebaseAuth.getInstance();
    }

    // Método para agregar un nuevo usuario (ya tienes este método en tu código)
    public void agregarUsuario(String email, String password, RegistroCallback callback) {
        if (isEmailValid(email)) {
            mauth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            callback.onRegistroExitoso();
                        } else {
                            Toast.makeText(context, "Error en registro", Toast.LENGTH_SHORT).show();
                            callback.onRegistroFallido();
                        }
                    });
        } else {
            Toast.makeText(context, "El correo electrónico no es válido", Toast.LENGTH_SHORT).show();
            callback.onRegistroFallido();
        }
    }

    // Verificar que el correo es válido (puedes ajustar esto a tus necesidades)
    private boolean isEmailValid(String email) {
        return email.contains("@gmail.com") || email.contains("@outlook.com");
    }

    // Método para iniciar sesión
    public void iniciarSesion(String email, String password, OnLoginResultCallBack callBack) {
        mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callBack.onSuccess();
                    } else {
                        callBack.onFailure();
                    }
                });
    }

    // Cerrar sesión
    public void cerrarSesion() {
        mauth.signOut();
        Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
    }

    // Interfaz para manejar el resultado del inicio de sesión
    public interface OnLoginResultCallBack {
        void onSuccess();
        void onFailure();
    }

    // Nueva interfaz para manejar el resultado del registro
    public interface RegistroCallback {
        void onRegistroExitoso();
        void onRegistroFallido();
    }

    // Método para cambiar la contraseña del usuario logueado
    public void cambiarContrasena(String nuevaContrasena) {
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
            // Si el usuario está autenticado, actualizamos la contraseña
            user.updatePassword(nuevaContrasena)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Si no hay usuario autenticado
            Toast.makeText(context, "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
