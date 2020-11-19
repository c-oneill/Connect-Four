package connect4;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class encapsulates the network functions of the Connect4 program.
 * 
 * @author Kristopher Rangel
 *
 */
public class Connect4Network {
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    private boolean startedWithoutError;
    private String errorMessage;
    
    /**
     * Constructor.
     * <p>This constructor starts a server connections or client connection as
     * appropriate on the given port.
     * 
     * @param isServer - whether a server connection should be established
     *                   <li>if True, a server connection will be established
     *                   <li>if False, a client connection will be established
     * @param server - the hostname
     * @param port - the port number
     * 
     * @author Kristopher Rangel
     * 
     */
    public Connect4Network(boolean isServer, String server, int port) {
        
        if(isServer) {
            startedWithoutError = startServer(port);
        }else {
            startedWithoutError = startClient(server, port);
        }
    }
    
    /**
     * <ul><b><i>startServer</i></b></ul>
     * <ul><ul><p><code>private boolean startServer () </code></p></ul>
     *
     * Creates a server connecting accepting clients.
     *
     * <p>If an exception occurred while trying to establish the connection,
     * false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @return true if no exception, false otherwise
     * 
     * @author Kristopher Rangel
     */
    private boolean startServer(int port) {
        boolean hasNoException = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            connection = serverSocket.accept();
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
 
        }catch(IOException e) {
            hasNoException = false;
            errorMessage = "IOException occurred while trying to establish server.";
            System.out.println("IOException occurred while trying to establish server.");
            e.printStackTrace();
        }
        return hasNoException;
    }

    /**
     * <ul><b><i>startClient</i></b></ul>
     * <ul><ul><p><code>private boolean startClient (String server, int port) </code></p></ul>
     *
     * Creates a client connection and attempts to connect to the specified server.
     *
     * <p>If an exception occurred while trying to establish the connection,
     * false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @param server - the host name, or null for the loopback address.
     * @param port - the port number
     * @return true if no exception, false otherwise
     * 
     * @author Kristopher Rangel
     * 
     */

    private boolean startClient(String server, int port) {
        boolean hasNoException = true;
        try {
            connection = new Socket(server, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
        }catch(IOException e) {
            hasNoException = false;
            errorMessage = "IOException occurred while trying to establish connection to server.";
            System.out.println("IOException occurred while trying to establish connection to server.");
            e.printStackTrace();
        }
        return hasNoException;
    }
    
    /**
     * <ul><b><i>closeConnection</i></b></ul>
     * <ul><ul><p><code>public boolean closeConnection () </code></p></ul>
     *
     * Closes the connection created by this class.
     * 
     * <p>If an exception occurred or an attempt was made to close a
     * null connection, false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @return true if no exception, false otherwise
     * 
     * @author Kristopher Rangel
     */
    public boolean closeConnection() {
        boolean hasNoException = true;
        try { 
        	
            if(connection != null)
                connection.close();
            else {
                hasNoException = false;
                errorMessage = "Attempted to close a null connection.";
                System.out.println("Attempted to close a null connection.");
            }
                
        }catch(IOException e) {
            errorMessage = "IOException occurred while trying to close connection.";
            System.out.println("IOException occurred while trying to close connection.");
            e.printStackTrace();
        }
        return hasNoException;
    }
    
    /**
     * <ul><b><i>writeMessage</i></b></ul>
     * <ul><ul><p><code>public boolean writeMessage (Connect4MoveMessage message) </code></p></ul>
     *
     * Writes a {@link Connect4MoveMessage} to the output buffer of this connection.
     *
     * <p>If an exception occurred while trying to establish the connection,
     * false is returned. In the event false is returned, 
     * the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @param message - the {@link Connect4MoveMessage} to transmit
     * @return true if no exception, false otherwise
     * 
     */
    public boolean writeMessage(Connect4MoveMessage message) {
        boolean hasNoException = true;
        errorMessage = "No error occurred";
        try {
            output.writeObject(message);
            System.out.println("writing message!");
        }catch(IOException e) {
            hasNoException = false;
            errorMessage = "IOException occured while writing message.";
            System.out.println("IOException occured while writing message.");
            e.printStackTrace();
        }
        return hasNoException;
    }
    
    /**
     * <ul><b><i>readMessage</i></b></ul>
     * <ul><ul><p><code>public Connect4MoveMessage readMessage () </code></p></ul>
     *
     * Reads a {@link Connect4MoveMessage} from the input buffer of this connection.
     * 
     * <p>If an exception occurred while trying to read the message, null will be returned.
     * In that event, the error message can be retrieved by invoking {@link #getErrorMessage()}.
     *
     * @return - the <code>Connect4MoveMessage</code> read, or null if an exception occurred or no input exists
     */
    public Connect4MoveMessage readMessage() {
        Connect4MoveMessage message = null;
        errorMessage = "No error occurred.";
        try {
            message = (Connect4MoveMessage) input.readObject();
            errorMessage = "No error message.";
            System.out.println("reading message!");
        } catch(SocketException | EOFException e) {
            errorMessage = "Connection Closed.";
            System.out.println(errorMessage);
            closeConnection();
            //e.printStackTrace(); 
        }catch(IOException e) {
            errorMessage = "IOException occured while trying to read message.";
            System.out.println(errorMessage);
            e.printStackTrace();
        }catch(ClassNotFoundException e) {
            errorMessage = "ClassNotFoundException occured while trying to read message.";
            System.out.println(errorMessage);
            e.printStackTrace();
        }
        return message;
    }
    
    public boolean getStartError() { return !startedWithoutError; }
    public String getErrorMessage() { return errorMessage; }
}
