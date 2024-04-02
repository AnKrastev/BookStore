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
/*
        try {
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            AdminRequests adminRequests = new AdminRequests();
            printToClient.println("Choose option\n1-Select all from customers\n2-Delete customers");
            int option = inputFromClient.nextInt();
            switch (option) {
                case 1:
                    adminRequests.selectCustomers(clientSocket);
                    break;
                case 2:
                    adminRequests.deleteReduction(clientSocket);
                    break;
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        */


        //Do admin and employee's requests work?
        AdminEmployeeRequests adminEmployeeRequests=new AdminEmployeeRequests();
        //adminEmployeeRequests.selectProduct(clientSocket);
        //adminEmployeeRequests.selectReduction(clientSocket);
        //adminEmployeeRequests.selectStore(clientSocket);
        //adminEmployeeRequests.editProducts(clientSocket);


        //Do admin's requests work?
        AdminRequests adminRequests=new AdminRequests();
       // adminRequests.selectCustomers(clientSocket);
       // adminRequests.selectEmployee(clientSocket);
        //adminRequests.createAdmin(clientSocket);
       // adminRequests.createEmployee(clientSocket);
        //adminRequests.createStore(clientSocket);
        //adminRequests.createBook(clientSocket);
        //adminRequests.createCustomer(clientSocket);
        //adminRequests.editCustomer(clientSocket);
        //adminRequests.editEmployee(clientSocket);
        //adminRequests.deleteReduction(clientSocket);
        //adminRequests.deleteEmoployee(clientSocket);
        //adminRequests.deleteStore(clientSocket);
        //adminRequests.deleteBook(clientSocket);
        //adminRequests.deleteCustomer(clientSocket);


        CustomerRequests customerRequests=new CustomerRequests();
        //customerRequests.selectProduct(clientSocket);
       // customerRequests.bookFilterBetweenTwoPrices(clientSocket);
        customerRequests.sortBooks(clientSocket);

    }


}
