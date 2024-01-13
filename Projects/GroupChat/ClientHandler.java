package Projects.GroupChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String clientName;
    public ClientHandler(Socket socket)
    {
        this.socket = socket;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            clientName = inputStream.readUTF();
            clients.add(this);
            broadCastMessage(" has joined the room.");

        }
        catch (Exception e)
        {
            closeSocket(socket,outputStream,inputStream);
        }
    }

    private void broadCastMessage(String msg) {


            clients.forEach((i)->{
                if(i.clientName!=this.clientName)
                {
                    try {
                        i.outputStream.writeUTF("\nServer: "+clientName+": "+msg);
                        i.outputStream.flush();
                    }
                    catch (Exception e)
                    {
                        closeSocket(socket,outputStream,inputStream);
                    }

                }
            });




    }

    private void closeSocket(Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
        try {
           leftRoom();
            if(outputStream!=null)
            {
                outputStream.close();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
            if (socket!=null)
            {
                socket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        String msg = "";
        while (socket.isConnected())
        {
            try {
                msg = inputStream.readUTF();
                broadCastMessage(msg);
            }
            catch (Exception e)
            {
                closeSocket(socket,outputStream,inputStream);
                break;
            }
        }

    }
    private void leftRoom()
    {

        broadCastMessage("has left the room.");
        clients.remove(this);

    }

    public static void main(String[] args) {

    }
}
