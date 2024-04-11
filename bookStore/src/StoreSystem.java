import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.Cleaner;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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

    CustomerRequests customerRequests=new CustomerRequests();


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
                            System.out.println("Admin with ip: "+clientSocket.getInetAddress()+"is connected");
                            adminMenu(admin,clientSocket);
                            flagCorrectUser = false;
                            break;
                        case "Касиер":
                            Employee employee = new Employee(rsEmployeeAdmin.getString("employee.employee_email"), rsEmployeeAdmin.getString("employee.password"), rsEmployeeAdmin.getString("employee.employee_name"), rsEmployeeAdmin.getString("employee.employee_phone"), rsEmployeeAdmin.getString("employee.employee_position"));
                            System.out.println("Employee with ip: "+clientSocket.getInetAddress()+"is connected");
                            employeeMenu(employee,clientSocket);
                            flagCorrectUser = false;
                            break;
                    }
                }
            }
            while(rsCustomer.next()){
                if (email.equals(rsCustomer.getString("customer.Customer_email")) && password.equals(rsCustomer.getString("customer.password"))) {
                    Customer customer = new Customer(rsCustomer.getString("customer.Customer_email"), rsCustomer.getString("customer.password"),rsCustomer.getString("customer.customer_name"),rsCustomer.getString("customer.customer_phone"));
                    System.out.println("Customer with ip: "+clientSocket.getInetAddress()+"is connected");
                    customerMenu(customer,clientSocket);
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
            printToClient.println("Choose option:\n1-LogIn\n2-Register");
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
            printToClient.println("Welcome admin "+admin.getName());
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
                    printToClient.println("Choose option:\n1-Select all products\n2-Select all reductions\n3-Select all stores\n4-Select all customers\n5-Select all employees\n6-viewQuantityOfBooksInStore\n7-storeTurnover between two dates");
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
                        case 7:
                            adminRequests.storeTurnover(clientSocket);
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
            printToClient.println("Welcome employee "+employee.getName());
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
        printToClient.println(customer.getName());
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            printToClient.println("Welcome customer "+customer.getName()+"\n");
            printToClient.println("Choose:\n1-View products in store\n2-Shopping\n3-Exit");
            int choiceOption=inputFromClient.nextInt();
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
                    printToClient.println("Enter options\n1-Go shopping\n2-View your shoppingCart\n3-Order");
                    int option=inputFromClient.nextInt();
                    switch (option){
                        case 1:
                            addToShoppingCard(customer,clientSocket);
                            break;
                        case 2:
                            seeShoppingCard(customer,clientSocket);
                            break;
                        case 3:
                            //create order from customer
                            orderCustomer(customer,clientSocket);
                            //create order details from the same order
                            orderDetails(customer,clientSocket);
                            break;
                    }
                    break;
                case 3:
                    printToClient.println("Enter 'exit'");
                    break;
                default:
                    printToClient.println("Incorrect choice");
                    printToClient.println("Try again");
                    customerMenu(customer,clientSocket);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//////////////////////////////////////////////////////////////SHOPING CART AND ORDER////////////////////////////////////////////////////////

    //add product in user's shopping cart
    public void addToShoppingCard(Customer customer,Socket clientSocket){
        //create object CustomerRequests
        HashMap<Integer,Integer> shoppingCart=customer.getShoppingCart();
        customerRequests.selectProduct(clientSocket);
        //create the variables
        int idProduct;
        int quality;
        printToClient.println("The shopping will end when you enter zero");
        do{
            printToClient.println("Enter id of product which you want");
            idProduct = inputFromClient.nextInt();
            printToClient.println("Enter quality of product");
            quality = inputFromClient.nextInt();
            if(idProduct!=0 && quality!=0) {
                shoppingCart.put(idProduct, quality);
            }
        }while(idProduct!=0 && quality!=0);
        //when we end with added articuls to shoppingCart we will return to customerMenu
        customerMenu(customer,clientSocket);
    }


    //create method for vision all product in user's shoppingCart
public void seeShoppingCard(Customer customer,Socket clientSocket){
        //get the user shoppingCart
        HashMap<Integer,Integer> shopingCart=customer.getShoppingCart();
        //create object customerRequest for our requests

        if(shopingCart.isEmpty()){
            printToClient.println("You don't have anything articles in shoppingCart");
        }else {
            //we loop through it and output the title of the book and its price for the given request in shoppingCart(HashMap)
            for (Integer id : shopingCart.keySet()) {
                customerRequests.selectProductFromShoppingCart(clientSocket, id.intValue(),shopingCart.get(id).intValue());
                //we output null line like enter after every result
                printToClient.println("");
            }
        }
        //after we see the products or no we always return to customer menu
    customerMenu(customer,clientSocket);


}



//order and save data for order in database
    public void orderCustomer(Customer customer,Socket clientSocket) throws SQLException {
        //gets todays date
        LocalDateTime today = LocalDateTime.now();
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Format the LocalDateTime object into a String
        String todayDate = today.format(formatter);
        //create order in database
        //here we added number of magazine from whose we think shopping but order is from all magazines(here we added magazine from where the order is created)
        printToClient.println("Enter id Store from where this book is");
        int idStore=inputFromClient.nextInt();
        //we say this method if totalAmount is zero
        if(customerRequests.totalAmount==0){
            seeShoppingCard(customer,clientSocket);
        }
        customerRequests.createOrder(clientSocket,rsCustomer.getInt("customer_ID"),todayDate,idStore, customerRequests.totalAmount);
    }



//orderDetails
public void orderDetails(Customer customer,Socket clientSocket) throws SQLException {
    //gets todays date
    LocalDateTime today = LocalDateTime.now();
    // Define the desired format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Format the LocalDateTime object into a String
    String todayDate = today.format(formatter);
    //set today's date
        int orderId= customerRequests.selectOrderId(todayDate, rsCustomer.getInt("customer_ID"));
        //get book price
    HashMap<Integer,Integer> shopingCart=customer.getShoppingCart();
    if(shopingCart.isEmpty()){
        printToClient.println("You don't have anything articles in shoppingCart");
    }else {
        //we loop through it and output the title of the book and its price for the given request in shoppingCart(HashMap)
        for (Integer id : shopingCart.keySet()) {
            //get unit price for all books
            double bookPrice=customerRequests.selectPriceBook(id.intValue());
            //saved date in order details for all book from this order
            customerRequests.createOrderDetail(clientSocket,shopingCart.get(id),orderId,id.intValue(),bookPrice);
            //reduction store quantity for every book
            int idStore=customerRequests.selectStore(clientSocket,id.intValue());
            customerRequests.qualityBooksInStore(clientSocket,id.intValue(),idStore,shopingCart.get(id).intValue());
        }
        System.out.println("Quantity from store is reduction");
        System.out.println("Order details are included");
        //after oder the customer will return to customer Menu bu this will be hapen after all things util order  are successful
        customerMenu(customer,clientSocket);
    }
}












//completed
    ///1-Как да направя така че при натискане на изход да прекъсна връзка със сървъра- направено

    //2-da ne se vizda parolata-- това е невъзможно, защото за JVM , когато става въпрос за конзолно приложение , тъй като не се свързва директно с конзолата и съответно дава грешка че не съществува конзола
    //ако е за JOPTION pane ще стане

//3-Как да направя така 1е клиента да пазарува и да добавя неюа в кошницата

//4-da dobawq namalenieto kym produktite(НАМАЛЕНИЯТА КЪМ ПРОДУЦТИТЕ СА ДОБАВЕНИ В ОСНОВНИТЕ ЗАЯВКИ И ОТ ТАМ СЕ ЗАПАЗВАТ ВСИЧКИ ЦЕНИ ЗА ЦЯЛАТА ПРОГРАМА)
    //намаленията ще се показват когато изведем процентите на съответната дата и я извадим от крайаната цена на продукта в самата заявка(тоест сваляме цената на продукта докатп го селектираме по съответната дат


//5-как да оправя проблема със датите в заявките

    // 6- остана ми 1-да изгладя нещата в order и order detail
//7- да намалям книгите от магазина
//8-order details









}
