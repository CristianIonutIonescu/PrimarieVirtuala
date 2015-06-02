package servers;

import java.io.*;

public class ChatMessage implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message, command;

    public ChatMessage(String _command, String _message) {
        command = _command;
        message = _message;
    }

    public String getCommand() {
        return command;
    }

    public String getMessage() {
        return message;
    }
}
