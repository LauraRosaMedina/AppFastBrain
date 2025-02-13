# FastBrain

### 📖 Descripción

FastBrain es un juego de mesa digitalizado de tipo quiz/trivial que combina el aprendizaje y la competencia en tiempo real. Diseñado para ser jugado por hasta cuatro participantes, la aplicación permite responder preguntas de diversas categorías para acumular puntos. El primer jugador en alcanzar 300 puntos es declarado ganador.

### 🖋️ Características Principales

- Autenticación de usuarios: Registro e inicio de sesión mediante correo electrónico o Google, con almacenamiento en Firebase.

- Juego multijugador en tiempo real: Interacción con amigos a través de partidas online con conexión a un servidor.

- Sistema de turnos automatizado: Implementado con un sistema cliente/servidor basado en hilos y multicliente.

- Variedad de categorías: Gamer, Mundo Sobrenatural, Sabores del Mundo, Risas y Memes.

- Personalización de partidas: Elección de dificultad, idioma y número de jugadores.

- Interfaz intuitiva: Diseño responsivo y optimizado para dispositivos móviles.

- Compatibilidad multiplataforma: Disponible para Android e iOS.

- Seguridad y optimización: Protección de datos con cifrado y gestión eficiente de recursos.

### 📺 Tecnologías Utilizadas

- Lenguaje de Programación: Java.

- Framework: Android SDK.

- Base de Datos: Firebase para almacenamiento de usuarios y sesiones. Firebase proporciona autenticación en tiempo real, almacenamiento en Firestore y sincronización de datos entre dispositivos.

- Autenticación: Firebase Authentication permite el registro e inicio de sesión mediante correo y Google.

- Servidor Multijugador: Implementado con Sockets, Threads y Semaphore. Esto permite la gestión de múltiples clientes simultáneamente en un entorno de juego dinámico.

- Manejo de Concurrencia: Uso de Thread para crear hilos independientes y Semaphore para gestionar el acceso concurrente a los recursos del servidor.

- Gestión de Navegación: Intents explícitos e implícitos para transiciones fluidas entre pantallas.

- Modelado y Diseño: Uso de StarUML para diagramas de actividad y flujo de pantallas.

- Documentación y Diseño de Usuario: Creación del manual de usuario con Behance, proporcionando una guía visual detallada para los jugadores.

### ⏳ Flujo de Navegación

- Inicio de Sesión y Registro: Los usuarios pueden registrarse o iniciar sesión con su correo electrónico o Google. La información de autenticación se almacena en Firebase.

- Pantalla de Creación o Unión a Sala: Los usuarios pueden crear una sala y convertirse en máster de la partida, generando un código único para compartir con otros jugadores. También pueden unirse a una sala existente introduciendo el código proporcionado por el máster.

- Pantalla de Juego: Los jugadores esperan su turno y, cuando les toca, pueden hacer clic en un cuadro de animación para revelar una pregunta. Cada pregunta pertenece a una categoría específica y tiene un límite de tiempo. Las respuestas correctas suman 50 puntos, mientras que las incorrectas restan 20 puntos. Si el temporizador llega a 0, el jugador pierde su turno. El primer jugador en alcanzar 300 puntos gana la partida.

###  🎥 Sistema Cliente/Servidor

FastBrain implementa un modelo de arquitectura cliente/servidor para gestionar las partidas en línea de manera eficiente:

- Servidor Multicliente: Se ha desarrollado un servidor en Java que maneja múltiples conexiones mediante Sockets y Threads.

- Manejo de Concurrencia: Se usa Semaphore para gestionar el acceso simultáneo de jugadores y evitar colisiones en los turnos.

- Sistema de Turnos: El servidor asigna turnos de manera automática y sincroniza los cambios con todos los jugadores conectados.

- Comunicación Cliente-Servidor: Cada cliente envía y recibe datos en tiempo real a través de sockets, permitiendo una experiencia de juego fluida.

### 📘 Instalación y Uso

- Clona el repositorio: git clone https://github.com/LauraRosaMedina/AppFastBrain.git

- Abre el proyecto en Android Studio.

- Compila y ejecuta en un emulador o dispositivo físico.

- Regístrate o inicia sesión para comenzar a jugar.

### ➰ Enlaces Relevantes

- [Diagrama](https://docs.google.com/document/d/1CF8uj0QVe5bWVNxtwCUyPsgxawuLUAv9EURdut01XmA/edit?tab=t.0)
- [Manual de Usuario](https://www.behance.net/gallery/219134773/FastBrain-Android)
- [Documentación App FastBrain](https://docs.google.com/document/d/1_owfrJb_FkfqrTOzANhRUvSM1FwPEwSyEW7WRkQi65k/edit?usp=drive_link)

### 👱‍♀️ Autores

- Laura Rosa Medina
- Davinia Rodríguez Portillo
- Mar Romero Portillo

### 🏠 Licencia
Este proyecto está bajo la licencia MIT.
