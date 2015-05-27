package servers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Server {

    private static int uniqueId;
    private final ArrayList<ClientThread> vec;
    private final SimpleDateFormat sdf;
    private final int port;
    private boolean isRunning;
    HashSet<String> hash_names;

    public static void main(String[] args) {
        int portNumber = 1700;
        Server server = new Server(portNumber);
        server.start();
    }

    public Server(int _port) {
        port = _port;
        sdf = new SimpleDateFormat("HH:mm:ss");
        vec = new ArrayList<>();
        hash_names = new HashSet<>();
    }

    public void start() {
        isRunning = true;
        try {
            ServerSocket ss = new ServerSocket(port);

            while (isRunning) {
                Socket socket = ss.accept();
                if (!isRunning) {
                    break;
                }
                ClientThread t = new ClientThread(socket);
                vec.add(t);
                t.start();
            }
            try {
                ss.close();
                for (int i = 0; i < vec.size(); ++i) {
                    ClientThread tc = vec.get(i);
                    try {
                        tc.ois.close();
                        tc.ous.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                    }
                }
            } catch (IOException e) {
                display("Eroare la inchiderea serverului: " + e);
            }
        } catch (IOException e) {
            String msg = sdf.format(new Date())
                    + " Exceptie pe noul ServerSocket " + e + "\n";
            display(msg);
        }
    }

    private void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;
        System.out.println(time);

    }

    private synchronized void broadcast(String message) {
        String time = sdf.format(new Date());
        String message_to_send = time + " " + message + "\n";
        System.out.print(message_to_send);

        for (int i = vec.size(); --i >= 0;) {
            ClientThread ct = vec.get(i);
            if (!ct.writeMsg(message_to_send)) {
                vec.remove(i);
                display("Clientul " + ct.username + " a fost sters.");
            }
        }
    }

    private String getClientThreadNick(int id) {
        for (int i = vec.size(); --i >= 0;) {
            ClientThread ct = vec.get(i);
            if (ct.id == id) {
                return ct.username;
            }
        }
        return "NU EXISTA";
    }

    private void writeMsgTo(int current_user, String user_to_find, String message) {
       
        String time = sdf.format(new Date());
        String message_to_send;
        message_to_send = time + " Mesaj de la " + getClientThreadNick(current_user) + ": " + message + "\n";
        boolean ok = false;
        for (int i = vec.size(); --i >= 0;) {
            ClientThread ct = vec.get(i);
            if (ct.username == null ? user_to_find == null : ct.username.equals(user_to_find)) {
                if (!ct.writeMsg(message_to_send)) {
                    vec.remove(i);
                    display("Clientul " + ct.username + " a fost sters.");
                }
                ok = true;
            }
        }
        if (ok == false) {
            String msg = "Userul cu nick-ul " + user_to_find + " nu exista !";
            writeMsgTo(current_user, msg);
        }
    }

    private void writeMsgTo(int id, String message) {
        String time = sdf.format(new Date());
        String message_to_send = time + " " + message + "\n";

        for (int i = vec.size(); --i >= 0;) {
            ClientThread ct = vec.get(i);
            if (ct.id == id) {
                if (!ct.writeMsg(message_to_send)) {
                    vec.remove(i);
                    display("Clientul " + ct.username + " a fost sters.");
                }
            }
        }
    }

    synchronized void remove(int id) {
        for (int i = 0; i < vec.size(); ++i) {
            ClientThread ct = vec.get(i);
            if (ct.id == id) {
                hash_names.remove(ct.username);
                vec.remove(i);
                return;
            }
        }
    }

    class ClientThread extends Thread {

        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream ous;
        int id;
        String username;
        ChatMessage cm;
        String date;

        ClientThread(Socket _socket) {

            id = ++uniqueId;
            socket = _socket;

            try {
                ous = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                username = (String) ois.readObject();
                display(username + " s-a conectat.");
            } catch (IOException e) {
                display("Exception aruncata la crearea ois/ous: " + e);
                return;
            } catch (ClassNotFoundException e) {
            }
            date = new Date().toString() + "\n";
        }

        @Override
        public void run() {

            boolean keepGoing = true;
            while (keepGoing) {
                try {
                    cm = (ChatMessage) ois.readObject();
                } catch (IOException e) {
                    display(username + " Exceptie aruncata la citirea ois: "
                            + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }
                keepGoing = this.handleMessage(cm);

            }
            remove(id);
            close();
        }

        private void close() {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
            }
            try {
                if (ois != null) {
                    ois.close();
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

        private boolean writeMsg(String msg) {
            if (socket.isClosed()) {
                close();
                return false;
            }
            try {
                ous.writeObject(msg);
            } catch (IOException e) {
                display("Eroare de trimitere pentru " + username);
                display(e.toString());
            }
            return true;
        }
        
        private boolean handleMessage(ChatMessage cm){
        	switch (cm.getCommand()) {
            case "ERROR":
                String _str = "Comanda incorecta. ";
                writeMsgTo(id, _str);
                break;
            case "BCAST":
                broadcast(username + ": " + cm.getMessage());
                break;
            case "QUIT":
                broadcast(username + " s-a delogat.");
                remove(id);
                return false;
            case "MSG":
                String str = cm.getMessage();
                String to_user = null,
                 to_user_message = null;
                StringTokenizer tok = new StringTokenizer(str);
                StringBuilder sb = new StringBuilder();
                to_user = (String) tok.nextElement();
                while (tok.hasMoreElements()) {
                    sb.append((String) tok.nextElement() + " ");
                }
                to_user_message = sb.toString();
                writeMsgTo(id, to_user, to_user_message);
                break;
            case "LIST":
                StringBuilder listBuilder = new StringBuilder();
                for (int i = 0; i < vec.size(); ++i) {
                    ClientThread ct = vec.get(i);
                    listBuilder.append(ct.username).append('\n');
                }
                writeMsg(listBuilder.toString());
                break;
        }
        	return true;
        }
    }
}
