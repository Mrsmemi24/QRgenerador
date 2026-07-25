import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/generar-qr")
public class QRCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 1. Obtener dinámicamente la dirección del servidor donde corre el Servlet
        String scheme = request.getScheme();             // "http"
        String serverName = request.getServerName();     // "localhost"
        int serverPort = request.getServerPort();         // 8080
        String contextPath = request.getContextPath();   // "/GeneradorQR"

        // 2. Construir la URL completa a la que redirigirá el QR
        String urlDestino = scheme + "://" + serverName + ":" + serverPort + contextPath + "/destino.html";

        // 3. Dimensiones de la imagen QR en píxeles
        int ancho = 300;
        int alto = 300;

        try {
            // 4. Crear el escritor de QR y la matriz de puntos (bits)
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(urlDestino, BarcodeFormat.QR_CODE, ancho, alto);

            // 5. Configurar las cabeceras HTTP de la respuesta
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

            // 6. Escribir la imagen en el flujo de salida
            OutputStream out = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            out.flush();

        } catch (WriterException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el código QR");
        }
    }
}
