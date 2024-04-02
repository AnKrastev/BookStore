import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class MyThread implements Runnable{

    private Socket clientSocket;
    private StoreSystem storeSystem;

    public MyThread(Socket clientSocket, StoreSystem storeSystem) {
        this.clientSocket = clientSocket;
        this.storeSystem = storeSystem;
    }


    @Override
    public void run() {
       storeSystem.startProgram(clientSocket);
    }
}
