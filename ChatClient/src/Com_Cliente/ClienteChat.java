package Com_Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteChat {

    private static final String SERVIDOR_IP = "localhost";
    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR_IP, PUERTO);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader lectorConsola = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                // Leer opciones del servidor
                String mensajeServidor;
                while ((mensajeServidor = entrada.readLine()) != null && !mensajeServidor.isEmpty()) {
                    System.out.println(mensajeServidor);
                }

                // Leer la opción del usuario desde la consola
                System.out.print("Seleccione una opción: ");
                String opcionUsuario = lectorConsola.readLine().trim();

                // Enviar la opción al servidor
                salida.println(opcionUsuario);

                // Recibir y mostrar la respuesta del servidor
                String respuestaServidor = entrada.readLine();
                if (respuestaServidor != null && !respuestaServidor.isEmpty()) {
                    System.out.println("Respuesta del servidor: " + respuestaServidor);
                }

                if ("5".equals(opcionUsuario)) {
                    // Si el usuario elige "Salir", cerramos la conexión desde el cliente
                    System.out.println("Cerrando conexión desde el cliente.");
                    break; // Salimos del bucle principal
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
