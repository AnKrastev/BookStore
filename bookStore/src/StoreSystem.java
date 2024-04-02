import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreSystem {


  private ArrayList<User> userList;
  private Scanner inputFromClient;
  private PrintStream printToClient;
  private Connection connection=null;
  //results for employee and admin from database
  private PreparedStatement psEmployeeAdmin=null;
  private ResultSet rsEmployeeAdmin=null;
  //results from customers from database
  private PreparedStatement psCustomer=null;
  private ResultSet rsCustomer=null;


    public StoreSystem() {
        this.userList = new ArrayList<>();
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

/*
    public void logInn(Socket clientSocket){

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



        //Do admin and employee's requests work?
        AdminEmployeeRequests adminEmployeeRequests=new AdminEmployeeRequests();
        //adminEmployeeRequests.viewQuantityOfBooksInStore(clientSocket);
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
       // customerRequests.sortBooks(clientSocket);
        customerRequests.registerForm(clientSocket);

    }
*/

//log in method
    public boolean logIn(Socket clientSocket){
        //flag for correct password
        boolean flagCorrectUser=true;
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            //
            //
            //enter email and password
            printToClient.println("Enter email: ");
            String email=inputFromClient.next();
            printToClient.println("Enter password: ");
            String password=inputFromClient.next();
            //
            //
            //
            //sql script for request to database
            String sqlEmployeeAdmin="SELECT * FROM employee";
            String sqlCustomer="SELECT * FROM customer";
            //set sql script to database
            psEmployeeAdmin=connection.prepareStatement(sqlEmployeeAdmin);
            psCustomer=connection.prepareStatement(sqlCustomer);
            //get result from database if this script is valid
            rsEmployeeAdmin= psEmployeeAdmin.executeQuery();
            rsCustomer=psCustomer.executeQuery();
            //match the email and password with email and password of users in database
            while(rsEmployeeAdmin.next()) {
                if (email.equals(rsEmployeeAdmin.getString("employee.employee_email")) && password.equals(rsEmployeeAdmin.getString("employee.password"))) {
                    switch (rsEmployeeAdmin.getString("employee_position")) {
                        case "Админ":
                            Admin admin = new Admin(rsEmployeeAdmin.getString("employee.employee_email"), rsEmployeeAdmin.getString("employee.password"), rsEmployeeAdmin.getString("employee.employee_name"), rsEmployeeAdmin.getString("employee.employee_phone"));
                            //admin menu
                            //след това в менютата ще кажем на първия ред следното: Welcome + admin.getName(); за да има смисъл от обектите същото и за останалите потребители
                            printToClient.println("Welcome admin   " + admin.getName());
                            flagCorrectUser = false;
                            break;
                        case "Касиер":
                            Employee employee = new Employee(rsEmployeeAdmin.getString("employee.employee_email"), rsEmployeeAdmin.getString("employee.password"), rsEmployeeAdmin.getString("employee.employee_name"), rsEmployeeAdmin.getString("employee.employee_phone"), rsEmployeeAdmin.getString("employee.employee_position"));
                            //employee menu
                            printToClient.println("Welcome employee   " + employee.getName());
                            flagCorrectUser = false;
                            break;
                    }
                }
            }
            while(rsCustomer.next()){
                if (email.equals(rsCustomer.getString("customer.Customer_email")) && password.equals(rsCustomer.getString("customer.password"))) {
                    Customer customer = new Customer(rsCustomer.getString("customer.Customer_email"), rsCustomer.getString("customer.password"),rsCustomer.getString("customer.customer_name"),rsCustomer.getString("customer.customer_phone"));
                    //customer menu
                    printToClient.println("Welcome customer   " + customer.getName());
                    flagCorrectUser=false;
                    break;
                }
            }
            //check the flag
            if(flagCorrectUser) {
                printToClient.println("Incorrect password");
                return  false;
            }else{
                return  true;
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with enter data");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
        return false;
    }



    //the main method of program
    public void startProgram(Socket clientSocket) {
        try {
            printToClient = new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            printToClient.println("Choose option: 1-LogIn\n2-Register");
            int choice=inputFromClient.nextInt();
            switch (choice){
                case 1:
                    //log in  и register формите съм ги направил boolean, за да знам дали са успели да се регистрират или да влязат успешно в сайта
                   //ако е успешно влязъл потребителя си продължава с работата а ако не се извиква отновов start метода за да се опита да влезе отново
                    if(!logIn(clientSocket)){
                        startProgram(clientSocket);
                    }
                    break;
                case 2:
                    //ако регистрацията е успешна продължаваме с login метода ако ли не ни връща в start метода
                    CustomerRequests customerRequests=new CustomerRequests();
                    if(customerRequests.registerForm(clientSocket)){
                        logIn(clientSocket);
                    }else{
                        startProgram(clientSocket);
                    }

            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with store");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error with store");
        }
    }



}
