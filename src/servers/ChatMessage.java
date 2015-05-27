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

    String getCommand() {
        return command;
    }

    String getMessage() {
        return message;
    }
}
