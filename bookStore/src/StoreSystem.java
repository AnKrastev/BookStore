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
                            adminMenu(admin,clientSocket);
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



    ///////////////////////////////////////////////////////user's manu//////////////////////////////////////////

    //admin menu
    public void adminMenu(Admin admin,Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            printToClient.println("Welcome admin"+admin.getName());
            //choose from four type requests
            printToClient.println("Choose option:\n 1-Create Requests\n 2-Select Requests\n 3-Edit Requests\n 4-Delete Requests\n5-Exit");
            int chooseTypeRequest=inputFromClient.nextInt();
            AdminRequests adminRequests=new AdminRequests();
            //match the choice with all opportunities and chooses from all requests from match type
            //after every requests the user returns to admin menu
            switch (chooseTypeRequest){
                case 1:
                    printToClient.println("Choose option:\n1-Create reduction\n2-Create admin\n3-Create employee\n4-Create store\n5-Create book\n6-Create customer");
                    int choiceCreateRequest=inputFromClient.nextInt();
                    switch (choiceCreateRequest){
                        case 1:
                            adminRequests.createReduction(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 2:
                            adminRequests.createAdmin(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 3:
                            adminRequests.createEmployee(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 4:
                            adminRequests.createStore(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 5:
                            adminRequests.createBook(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 6:
                            adminRequests.createCustomer(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        default:
                            printToClient.println("Incorrect option");
                            printToClient.println("Try again");
                            adminMenu(admin,clientSocket);
                    }
                    break;
                case 2:
                    printToClient.println("Choose option:\n1-Select all products\n2-Select all reductions\n3-Select all stores\n4-Select all customers\n5-Select all employees\n6-viewQuantityOfBooksInStore");
                    int choiceSelectRequests=inputFromClient.nextInt();
                    switch (choiceSelectRequests){
                        case 1:
                            adminRequests.selectProduct(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 2:
                            adminRequests.selectReduction(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 3:
                            adminRequests.selectStore(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 4:
                            adminRequests.selectCustomers(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 5:
                            adminRequests.selectEmployee(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 6:
                            adminRequests.viewQuantityOfBooksInStore(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        default:
                            printToClient.println("Incorrect option");
                            printToClient.println("Try again");
                            adminMenu(admin,clientSocket);
                    }
                    break;
                case 3:
                    printToClient.println("Choose option:\n1-Edit products\n2-Edit reduction\n3-Edit customers\n4-Edit employees");
                    int choiceEditRequests=inputFromClient.nextInt();
                    switch (choiceEditRequests){
                        case 1:
                            adminRequests.editProducts(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 2:
                            adminRequests.editReduction(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 3:
                            adminRequests.editCustomer(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 4:
                            adminRequests.editEmployee(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        default:
                            printToClient.println("Incorrect option");
                            printToClient.println("Try again");
                            adminMenu(admin,clientSocket);
                    }
                    break;
                case 4:
                    printToClient.println("Choose option:\n1-Delete reduction\n2-Delete employee\n3-Delete store\n4-Delete book\n5-Delete customer");
                    int choiceDeleteRequest=inputFromClient.nextInt();
                    switch (choiceDeleteRequest){
                        case 1:
                            adminRequests.deleteReduction(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 2:
                            adminRequests.deleteEmoployee(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 3:
                            adminRequests.deleteStore(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 4:
                            adminRequests.deleteBook(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        case 5:
                            adminRequests.deleteCustomer(clientSocket);
                            adminMenu(admin,clientSocket);
                            break;
                        default:
                            printToClient.println("Incorrect option");
                            printToClient.println("Try again");
                            adminMenu(admin,clientSocket);
                    }
                    break;
                case 5:
                    //End the connection with server
                    printToClient.close();
                    inputFromClient.close();
                    clientSocket.close();
                    break;
                default:
                    printToClient.println("Incorrect option");
                    printToClient.println("Try again...");
                    adminMenu(admin,clientSocket);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with admin menu");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }




}
