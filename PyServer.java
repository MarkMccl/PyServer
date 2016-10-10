import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class PyServer {

    private static final String[] xdtCommands = {
            "xdotool mousemove_relative -- ",//0
            "xdotool click ", //1
            "xdotool type ", //2
            "xdotool key KP_Enter", //3
            "xdotool key Up", //4
            "xdotool key Down", //5
            "xdotool key Right", //6
            "xdotool key Left" //7
    };


    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(4444);
            System.out.println("Server on, port:4444");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String message = bufferedReader.readLine();
                    String trimValue = message.substring(0, 1);
                    message = message.substring(1);


                    int holder = Integer.parseInt(trimValue);

                    if (holder == 8) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(new URI(message));
                            } catch (URISyntaxException e) {
                                System.err.println(e.getMessage());
                            }
                        } else {
                            System.err.println("Desktop not supported.");
                        }

                    } else if (holder <= xdtCommands.length) {
                        message = xdtCommands[holder] + message;
                        inputStreamReader.close();
                        clientSocket.close();
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec(message);
                    } else {
                        System.err.println("Invalid holder value of " + holder);
                    }
                } catch (NumberFormatException | IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}