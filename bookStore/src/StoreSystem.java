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

    }


}
