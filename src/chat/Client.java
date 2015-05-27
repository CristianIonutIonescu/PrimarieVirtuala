package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    public interface MessageListner {
        void onMessageReceived(String message);
    }

    private MessageListner callback;
    private ObjectInputStream ois;
    private ObjectOutputStream ous;
    private Socket socket;
    private final String server, username;
    private final int port;
    private final String type;

    public Client(String _server, int _port, String _username, MessageListner _callback, String _type) {
        server = _server;
        port = _port;
        username = _username;
        callback = _callback;
        type = _type;
    }

    public boolean start() {

        try {
            socket = new Socket(server, port);
        } catch (IOException ec) {
            display("Eroare la conectarea la server:" + ec);
            return false;
        }

        String msg = "Conectiune acceptata " + socket.getInetAddress() + ":"
                + socket.getPort();
        display(msg);

        try {
            ois = new ObjectInputStream(socket.getInputStream());
            ous = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            display("Exceptie la crearea ois/ous " + e);
            return false;
        }

        new ListenFromServer().start();

        try {
            ous.writeObject(username);
        } catch (IOException eIO) {
            display("Exceptie aruncata la login : " + eIO);
            disconnect();
            return false;
        }

        return true;
    }

    private void display(String msg) {
        if(callback != null) {
            callback.onMessageReceived(msg);
        }
    }

    public void sendMessage(ChatMessage msg) {
        try {
            ous.writeObject(msg);
        } catch (IOException e) {
            display("Exceptie la scrierea pe server: " + e);
        }
    }

    private void disconnect() {
        try {
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e) {
        }
        try {
            if (ous != null) {
                ous.close();
            }
        } catch (IOException e) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }
    
    public String getClientType()
    {
    	return type;
    }

    class ListenFromServer extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = (String) ois.readObject();
                    display(msg + "\n ");
                } catch (IOException e) {
                    display("Te-ai delogat! ");
                    break;
                } catch (ClassNotFoundException e2) {
                }
            }
        }
    }
}
