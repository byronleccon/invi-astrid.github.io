import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ola {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new InvitacionHandler());

        // Imagen portada
        server.createContext("/fondo.jpg", exchange -> {
            byte[] imagen = Files.readAllBytes(Path.of("fondo.jpg"));
            exchange.getResponseHeaders().add("Content-Type", "image/jpeg");
            exchange.sendResponseHeaders(200, imagen.length);
            exchange.getResponseBody().write(imagen);
            exchange.close();
        });

        // Imagen fondo rosa
        server.createContext("/rosa.jpg", exchange -> {
            byte[] imagen = Files.readAllBytes(Path.of("rosa.jpg"));
            exchange.getResponseHeaders().add("Content-Type", "image/jpeg");
            exchange.sendResponseHeaders(200, imagen.length);
            exchange.getResponseBody().write(imagen);
            exchange.close();
        });

        server.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
    }

    static class InvitacionHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String html = """
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Invitación de Cumpleaños</title>

<style>
html {
    scroll-behavior: smooth;
}

body {
    margin: 0;
    font-family: Arial, sans-serif;
}

/* PORTADA */
.portada {
    height: 100vh;
    background-image: url('/fondo.jpg');
    background-size: cover;
    background-position: center;
    display: flex;
    justify-content: center;
    align-items: center;
}

.portada button {
    background: rgba(233, 30, 99, 0.9);
    color: white;
    border: none;
    padding: 15px 30px;
    font-size: 18px;
    border-radius: 30px;
    cursor: pointer;
    margin-bottom: 50px;
}

/* SECCIÓN INFERIOR */
.info {
    min-height: 100vh;
    background-image: url('/rosa.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    display: flex;
    justify-content: center;
    align-items: center;
}

/* TARJETA */
.invitacion {
    background: rgba(255, 255, 255, 0.93);
    padding: 30px;
    border-radius: 20px;
    text-align: center;
    width: 350px;
    box-shadow: 0 10px 25px rgba(0,0,0,0.25);
    animation: aparecer 1s ease;
}

@keyframes aparecer {
    from {
        opacity: 0;
        transform: scale(0.8);
    }
    to {
        opacity: 1;
        transform: scale(1);
    }
}

h1 {
    color: #e91e63;
}

/* BOTÓN CONFIRMAR */
button.confirmar {
    background: #e91e63;
    color: white;
    border: none;
    padding: 12px 25px;
    border-radius: 25px;
    cursor: pointer;
    font-size: 16px;
}

button.confirmar:hover {
    background: #d81b60;
}

/* BOTÓN UBICACIÓN */
.ubicacion {
    display: inline-block;
    border: 3px solid #e91e63;
    padding: 10px 20px;
    border-radius: 30px;
    color: #e91e63;
    font-weight: bold;
    text-decoration: none;
    margin-top: 10px;
}

.ubicacion:hover {
    background-color: #e91e63;
    color: white;
}

#mensaje {
    margin-top: 15px;
    color: green;
    font-weight: bold;
}
</style>

</head>
<body>

<!-- PORTADA -->
<section class="portada">
    <button onclick="location.href='#info'">Ver invitación ⬇️</button>
</section>

<!-- INFORMACIÓN -->
<section id="info" class="info">
    <div class="invitacion">
        <h1>🎉 ¡Estás invitado! 🎉</h1>

        <p>Ven a celebrar el cumpleaños de</p>
        <h2>Astrid</h2>

        <p><strong>20 años</strong></p>
        <p>📅 28 de enero</p>

        <p>
            📍<br>
            <a class="ubicacion"
               href="https://maps.app.goo.gl/WUM6f1ZZCZSGb5TeA"
               target="_blank">
               Ver ubicación
            </a>
        </p>

        <button class="confirmar" onclick="confirmar()">Confirmar asistencia</button>
        <div id="mensaje"></div>
    </div>
</section>

<script>
function confirmar() {
    document.getElementById("mensaje").innerText =
        "¡Gracias por confirmar! 🎉 ¡Te esperamos!";
}
</script>

</body>
</html>
            """;

            exchange.sendResponseHeaders(200, html.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        }
    }
}
