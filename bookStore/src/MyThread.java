import java.io.IOException;
import java.net.Socket;

public class MyThread implements Runnable{

    private Socket clientSocket;
    private StoreSystem storeSystem;

    public MyThread(Socket clientSocket, StoreSystem storeSystem) {
        this.clientSocket = clientSocket;
        this.storeSystem = storeSystem;
    }


    @Override
    public void run() {
        storeSystem.logIn(clientSocket);
    }
}
