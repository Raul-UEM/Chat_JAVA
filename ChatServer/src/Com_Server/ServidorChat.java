package Com_Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorChat {

    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor listo para recibir conexiones en el puerto " + PUERTO);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado.");

                // Crear un hilo para manejar la comunicación con el cliente
                ManejadorCliente manejador = new ManejadorCliente(clienteSocket);
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ManejadorCliente implements Runnable {
        private Socket clienteSocket;
        private BufferedReader entrada;
        private PrintWriter salida;

        public ManejadorCliente(Socket socket) {
            this.clienteSocket = socket;
            try {
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                salida = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // Enviar opciones al cliente
                salida.println("Seleccione una opción:");
                salida.println("1. Saludo");
                salida.println("2. Pregunta");
                salida.println("3. Chiste malo");
                salida.println("4. Despedida");
                salida.println("5. Salir");
                salida.println(); // Separador entre opciones y respuestas

                while (true) {
                    String opcionCliente = entrada.readLine();
                    if (opcionCliente == null) {
                        // Si el cliente se desconecta, cerramos la conexión
                        System.out.println("Cliente desconectado.");
                        clienteSocket.close();
                        break;
                    }

                    // Procesar la opción del cliente y enviar la respuesta
                    String respuesta = obtenerRespuesta(opcionCliente);
                    salida.println(respuesta);
                    salida.println(); // Separador entre cada respuesta

                    if ("5".equals(opcionCliente)) {
                        // Si el cliente elige "Salir", cerramos la conexión desde el servidor
                        System.out.println("Cliente eligió salir. Cerrando conexión.");
                        clienteSocket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String obtenerRespuesta(String opcion) {
            switch (opcion) {
            case "1":
                return "Kaixo!!!";
            case "2":
                return "¿Tienes alguna duda que preguntarme?";
            case "3":
                return "Eliminar correos no deseados es muy fácil: spam comido.";
            case "4":
                return "Agur!!!";
            case "5":
                return "Gero arte!!!.";
            default:
                return "Upps,no reconozco esa opción.";
            }
        }
    }
}

