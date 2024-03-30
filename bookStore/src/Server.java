import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {


        ServerSocket serverSocket=null;
        Socket clientSocket=null;
        StoreSystem storeSystem=new StoreSystem();

        try{
            serverSocket=new ServerSocket(1222);
            System.out.println("Server is listening.....");
            while(true){
                clientSocket=serverSocket.accept();
                MyThread myThread=new MyThread(clientSocket,storeSystem);
                Thread thread=new Thread(myThread);
                thread.start();
            }
        }catch (IOException e){
            e.getMessage();
        }




    }
}