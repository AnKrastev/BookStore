import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class StoreSystem {


  private ArrayList<User> userList;
  private Scanner inputFromClient;
  private Scanner input=new Scanner(System.in);
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
                            employeeMenu(employee,clientSocket);
                            printToClient.println("Welcome employee   " + employee.getName());
                            flagCorrectUser = false;
                            break;
                    }
                }
            }
            while(rsCustomer.next()){
                if (email.equals(rsCustomer.getString("customer.Customer_email")) && password.equals(rsCustomer.getString("customer.password"))) {
                    Customer customer = new Customer(rsCustomer.getString("customer.Customer_email"), rsCustomer.getString("customer.password"),rsCustomer.getString("customer.customer_name"),rsCustomer.getString("customer.customer_phone"));
                    customerMenu(customer,clientSocket);
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





    /////////////////////////////////////////////////////////MAIN METHOD///////////////////////////////////////////////////

    //the main method of program
    public void startProgram(Socket clientSocket) {
        try {
            printToClient = new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            printToClient.println("Choose option:\n 1-LogIn\n2-Register");
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
                    //the client should enter exit and after that connection with the server will be break
                    printToClient.println("Enter 'exit'");
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




    //employee menu
    public void employeeMenu(Employee employee,Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            printToClient.println("Welcome employee"+employee.getName());
            printToClient.println("choose option:\n1-Create requests\n2-Select requests\n3-Edit requests\n4-Exit");
            AdminEmployeeRequests adminEmployeeRequests=new AdminEmployeeRequests();
            int choiceTypeRequests=inputFromClient.nextInt();
            switch (choiceTypeRequests){
                case 1:
                    adminEmployeeRequests.createReduction(clientSocket);
                    employeeMenu(employee,clientSocket);
                    break;
                case 2:
                    printToClient.println("Choose select requests:\n1-Select product\n2-Select reduction\n3-Select store\n4-viewQuantityOfBooksInStore");
                    int choiceSelectRequests=inputFromClient.nextInt();
                    switch (choiceSelectRequests){
                        case 1:
                            adminEmployeeRequests.selectProduct(clientSocket);
                            employeeMenu(employee,clientSocket);
                            break;
                        case 2:
                            adminEmployeeRequests.selectReduction(clientSocket);
                            employeeMenu(employee,clientSocket);
                            break;
                        case 3:
                            adminEmployeeRequests.selectStore(clientSocket);
                            employeeMenu(employee,clientSocket);
                            break;
                        case 4:
                            adminEmployeeRequests.viewQuantityOfBooksInStore(clientSocket);
                            employeeMenu(employee,clientSocket);
                            break;
                        default:
                            printToClient.println("Incorrect choice");
                            printToClient.println("Try again");
                            employeeMenu(employee,clientSocket);
                    }
                    break;
                case 3:
                    printToClient.println("Choose edit requests:\n1-editProducts\n2-edit reduction");
                    int choiceEditRequest=inputFromClient.nextInt();
                    switch (choiceEditRequest){
                        case 1:
                            adminEmployeeRequests.editProducts(clientSocket);
                            employeeMenu(employee,clientSocket);
                            break;
                        case 2:
                            adminEmployeeRequests.editReduction(clientSocket);
                            employeeMenu(employee,clientSocket);
                            break;
                        default:
                            printToClient.println("Incorrect choice");
                            printToClient.println("Try again");
                            employeeMenu(employee,clientSocket);
                    }
                    break;
                case 4:
                        printToClient.println("Enter 'exit'");
                        break;
                default:
                    printToClient.println("Incorrect choice");
                    printToClient.println("Try again");
                    employeeMenu(employee,clientSocket);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with employeeMenu");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }








    //customer menu
    public void customerMenu(Customer customer,Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            printToClient.println("Choose:\n1-View products in store\n2-Shopping\n3-Exit");
            int choiceOption=inputFromClient.nextInt();
            CustomerRequests customerRequests=new CustomerRequests();
            switch (choiceOption){
                case 1:
                    printToClient.println("Choose:\n1-Select all products from magazine without filters\n2-Select all products between two price\n3-select the products by price in ascending order");
                    int choiceSelect=inputFromClient.nextInt();
                    switch (choiceSelect){
                        case 1:
                            customerRequests.selectProduct(clientSocket);
                            customerMenu(customer,clientSocket);
                            break;
                        case 2:
                            customerRequests.bookFilterBetweenTwoPrices(clientSocket);
                            customerMenu(customer,clientSocket);
                            break;
                        case 3:
                            customerRequests.sortBooks(clientSocket);
                            customerMenu(customer,clientSocket);
                            break;
                        default:
                            printToClient.println("Incorrect choice");
                            printToClient.println("Try again");
                            customerMenu(customer,clientSocket);
                    }
                    break;
                case 2:
                    printToClient.println("Shopping start");
                    break;
                case 3:
                    printToClient.println("Enter 'exit'");
                default:
                    printToClient.println("Incorrect choice");
                    printToClient.println("Try again");
                    customerMenu(customer,clientSocket);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }





//questions:
    //1-Как да направя така че при натискане на изход да прекъсна връзка със сървъра
    //2-Как да направя така 1е клиента да пазарува и да добавя неюа в кошницата
    //3-как да оправя проблема със датите в заявките
    //da ne se vizda parolata
    //hash map
//tuk sum

//






}
