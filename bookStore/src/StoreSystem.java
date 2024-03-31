import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreSystem {


  private ArrayList<User> userList;
  private Scanner inputFromClient;
  private PrintStream printToClient;

    public StoreSystem() {
        this.userList = new ArrayList<>();
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }


    public void logIn(Socket clientSocket){

        try {
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            RequestsToDatabase requestsToDatabase = new RequestsToDatabase();
            printToClient.println("Choose option\n1-Select all from customers\n2-Delete customers");
            int option = inputFromClient.nextInt();
            switch (option) {
                case 1:
                    requestsToDatabase.select(clientSocket);
                    break;
                case 2:
                    requestsToDatabase.deleteReduction(clientSocket);
                    break;
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }


}
