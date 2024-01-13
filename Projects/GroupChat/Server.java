package Projects.GroupChat;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private ServerSocket serverSocket;

    private Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server is running...");
            startServer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void startServer()
    {
            while (!serverSocket.isClosed())
            {
                new Thread(()->{
                    try {

                        Socket socket = serverSocket.accept();
                        System.out.println("A new user has connected\n");
                        ClientHandler clientHandler = new ClientHandler(socket);
                        Thread thread = new Thread(clientHandler);
                        thread.start();
                    }catch (Exception e)
                    {
                        closeServer();
                    }
                }).start();

        }
    }

    private void closeServer() {
        try {
            if(serverSocket!=null)
            {

                serverSocket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Server server = new Server(9876);

    }
}
