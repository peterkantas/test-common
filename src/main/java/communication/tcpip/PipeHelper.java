package communication.tcpip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class PipeHelper {
    private static OutputStream outputStream;
    private static InputStream inputStream;

    public static String sendPipeRequestWithoutAuth(String host,int port,String message) {
        PipeHelper interFaceSocket;
        interFaceSocket = PipeHelper.connect(host,port);
        return interFaceSocket.sendPipeReplyMessage(message);
    }

    public static String sendPipeRequestBBAuth(String Host, String HostPort, String bbOrg, String bbUser, String requestString) {
        try {
            PipeUtil pu = PipeUtil.connectBBAuth(Host, HostPort, bbOrg, bbUser);
            System.out.println("KÉRDÉS.: " + requestString);
            String response = pu.sendMessage(requestString);
            System.out.println("VÁLASZ.: " + response);
            pu.finalizer();
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PipeHelper(OutputStream outputStream, InputStream inputStream) {
        PipeHelper.outputStream = outputStream;
        PipeHelper.inputStream = inputStream;
    }

    public static PipeHelper connect(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            return new PipeHelper(outputStream, is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPipeReplyMessage(String message) {
        try {
            outputStream.write((message + "\r\n").getBytes());
            outputStream.flush();
            Scanner sc = new Scanner(inputStream, "ISO-8859-2");
            return sc.nextLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
