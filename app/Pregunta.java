public class Pregunta {
    private String pregunta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private int respuestaCorrecta; // Índice de la opción correcta (0, 1, 2 o 3)

    // Constructor
    public Pregunta(String pregunta, String opcion1, String opcion2, String opcion3, String opcion4, int respuestaCorrecta) {
        this.pregunta = pregunta;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    // Métodos getters para obtener los valores de las propiedades
    public String getPregunta() {
        return pregunta;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
}
