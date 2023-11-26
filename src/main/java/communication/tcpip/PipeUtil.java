package communication.tcpip;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;


public class PipeUtil {

    private Socket socket;
    private OutputStream outputStream;
    private Scanner scanner;

    public PipeUtil(Socket socket, OutputStream outputStream, Scanner scanner) {
        this.scanner = scanner;
        this.socket = socket;
        this.outputStream = outputStream;
    }

    public static PipeUtil connectBBAuth(String Host, String interfacePort, String bborganization, String bbuser) {
        try {
            Socket socket = new Socket(Host, Integer.parseInt(interfacePort));
            OutputStream outputStream = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            Scanner scanner = new Scanner(is, "ISO-8859-2");
            String welcomeMessage = scanner.nextLine();
            String password = DigestUtils.md5DigestAsHex((welcomeMessage + "jelszo").getBytes()).toUpperCase();

            outputStream.write(("BB|" + bborganization + "|" + bbuser + "|" + password + "|\r\n").getBytes(Charset.forName("ISO-8859-2")));
            outputStream.flush();

            scanner.nextLine(); // read welcome

            return new PipeUtil(socket, outputStream, scanner);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void PipeUtil() {
    }

    public String sendMessage(String message) throws IOException {
        try {
            outputStream.write((message + "\r\n").getBytes("ISO-8859-2"));
            outputStream.flush();
            return scanner.nextLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void finalizer() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
