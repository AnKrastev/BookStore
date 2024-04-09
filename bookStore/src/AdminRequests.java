import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class AdminRequests extends AdminEmployeeRequests{


    //create object who saved connection to the database
    Connection connection=MySQLConnection.connection();

    //Object for set sql script to database
    PreparedStatement ps=null;

    //Object for saved all result is matching with sql script
    ResultSet resultSet=null;
    //Object for print message to client
    PrintStream printToClient=null;
    //Object for input message from client
    Scanner inputFromClient=null;


    /////////////////////////////////////////////////////////////SELECT REQUESTS///////////////////////////////////////
    //select request for all customers
    //works
    public void selectCustomers(Socket clientSocket) {
        try{
            //initialization the objects
            printToClient=new PrintStream(clientSocket.getOutputStream());
            Scanner inputFromClient=new Scanner(clientSocket.getInputStream());
            //create sql script for the request
            String sqlScript="SELECT * FROM customer";
            ps=connection.prepareStatement(sqlScript);
            resultSet=ps.executeQuery();
            printToClient.println("Select all customers: ");
            //getting the results and bring them out to client's screen
            while(resultSet.next()){
                int idCustomer=resultSet.getInt("customer_ID");
                String customerName=resultSet.getString("customer_name");
                String customerPhone=resultSet.getString("customer_phone");
                String customerEmail=resultSet.getString("Customer_email");
                printToClient.println(idCustomer+" "+customerName+" "+customerPhone+" "+customerEmail);
            }
        }catch(SQLException e){
            System.out.println("Ima problem");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }




    //SELECT EMPLOYEE
    //works
    public void selectEmployee(Socket clientSocket){
                try{
                    printToClient=new PrintStream(clientSocket.getOutputStream());
                    connection=MySQLConnection.connection();
                    String sql="SELECT * FROM employee";
                    ps=connection.prepareStatement(sql);
                    resultSet= ps.executeQuery();
                    printToClient.println("All employees are: ");
                    while(resultSet.next()){
                        int idEmployee=resultSet.getInt("employee_ID");
                        String emailEmployee=resultSet.getString("employee_email");
                        String nameEmployee=resultSet.getString("employee_name");
                        String phoneEmployee=resultSet.getString("employee_phone");
                        String positionEmployee=resultSet.getString("employee_position");
                        String passwordEmployee=resultSet.getString("password");
                        int idStore=resultSet.getInt("store_store_ID");
                        printToClient.println(idEmployee+" "+emailEmployee+" "+nameEmployee+" "+phoneEmployee+" "+positionEmployee+" "+passwordEmployee+" "+idStore);
                    }
                    System.out.println("Select employees ei successful");
                }catch (IOException e){
                    System.out.println(e.getMessage());
                    printToClient.println("Error with select employees");
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    printToClient.println("Error");
                }
    }



    ////////////////////////////////////////////////////////////////DELETE REQUESTS//////////////////////////////////////

    //delete reduction
    //works
    public void deleteReduction(Socket clientSocket) {
        try{
            //initialization the objects
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            //create sql script for the request
            String sqlScript="DELETE FROM reduction WHERE id=?";
            ps=connection.prepareStatement(sqlScript);
            printToClient.println("Input the id of reduction which will be delete");
            int reductionID=inputFromClient.nextInt();
            ps.setInt(1,reductionID);
            //start request and if it's true bring out a message for succеss to client's screen
           ps.execute();
            printToClient.println("reduction has been delete");
        }catch (SQLException e){
            System.out.println("There is problem with delete function");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    //delete employee
    //works
    public void deleteEmoployee(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="DELETE FROM employee WHERE employee_ID=?";
            ps=connection.prepareStatement(sql);
            printToClient.println("delete employee.....");
            printToClient.println("Enter id of employee which you want to be delete");
            int idEmployee=inputFromClient.nextInt();
            ps.setInt(1,idEmployee);
            ps.execute();
            System.out.println("Delete employee is successful");
            printToClient.println("Delete employee is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with delete employee");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }

//delete store
    //works
    public void deleteStore(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="DELETE FROM store WHERE store_ID=?";
            ps=connection.prepareStatement(sql);
            printToClient.println("Delete store.....");
            printToClient.println("Enter id of store which has been deleted");
            int idStore=inputFromClient.nextInt();
            ps.setInt(1,idStore);
            ps.execute();
            System.out.println("Deleting store is successful");
            printToClient.println("Deleting store is successful");
        }catch(IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with deleting store");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }



    //delete
    //works
    public void deleteBook(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="DELETE FROM book WHERE book_ID=?";
            ps=connection.prepareStatement(sql);
            printToClient.println("Delete book.....");
            printToClient.println("ENter book's id for deleting book: ");
            int idBook=inputFromClient.nextInt();
            ps.setInt(1,idBook);
            ps.execute();
            System.out.println("Deleting book is successful");
            printToClient.println("Deleting book is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with deleting book");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }


    //delete customer
    //works
    public void deleteCustomer(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="DELETE FROM customer WHERE customer_ID=?";
            ps=connection.prepareStatement(sql);
            printToClient.println("Delete customer....");
            printToClient.println("Enter customer id for deleting: ");
            int idCustomer=inputFromClient.nextInt();
            ps.setInt(1,idCustomer);
            ps.execute();
            System.out.println("Deleting customer is successful");
            printToClient.println("Deleting customer is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with deleting customer");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }



















////////////////////////////////////////////////////////////////////CREATE REQUESTS////////////////////////////////////////////////////////////
    //create admin
    //works
    public void createAdmin(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="INSERT INTO employee(employee_email,employee_name,employee_phone,employee_position,password,store_store_ID) VALUES(?,?,?,?,?,?)";
            ps=connection.prepareStatement(sql);
            printToClient.println("Create new Admin....");
            //enter staff to the variables
            printToClient.println("Enter email: ");
            String adminEmail=inputFromClient.nextLine();
            printToClient.println("Enter name: ");
            String adminName=inputFromClient.nextLine();
            printToClient.println("Phone: ");
            String adminPhone=inputFromClient.nextLine();
            //Enter variable Position with const Staff=admin(It can't change)
            String adminPosition="Админ";
            printToClient.println("Password: ");
            String adminPassword=inputFromClient.next();
            printToClient.println("Enter number of magazine: ");
            int adminIdStore=inputFromClient.nextInt();
            //check email
            if(checkEmail(adminEmail)) {
                ps.setString(1, adminEmail);
            }
            ps.setString(2,adminName);
            //check phone
            if(checkPhone(adminPhone)) {
                ps.setString(3, adminPhone);
            }
            ps.setString(4,adminPosition);
            ps.setString(5,adminPassword);
            ps.setInt(6,adminIdStore);
            //start request and if it's true bring out a message for succеss to client's screen
            ps.execute();
            printToClient.println("The admin is created");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with create admin");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("error");
        }

    }


//create employee
    //works
    public void createEmployee(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="INSERT INTO employee(employee_email,employee_name,employee_phone,employee_position,password,store_store_ID) VALUES(?,?,?,?,?,?)";
            ps=connection.prepareStatement(sql);
            printToClient.println("Create employee");
            printToClient.println("Enter employee's email: ");
            String emailEmployee=inputFromClient.nextLine();
            printToClient.println("Enter employee's name: ");
            String nameEmployee=inputFromClient.nextLine();
            printToClient.println("Enter phone of employee: ");
            String phoneEmployee=inputFromClient.nextLine();
            printToClient.println("Enter position of employee: ");
            String positionEmployee=inputFromClient.nextLine();
            printToClient.println("Enter password of employee: ");
            String passwordEmployee=inputFromClient.nextLine();
            printToClient.println("Enter id of magazine where employee works: ");
            int idStoreEmployee=inputFromClient.nextInt();
            //check email
            if(checkEmail(emailEmployee)) {
                ps.setString(1, emailEmployee);
            }
            ps.setString(2,nameEmployee);
            //check phone
            if(checkPhone(phoneEmployee)) {
                ps.setString(3, phoneEmployee);
            }
            //we set this condition because just admin can have position ADMIN
            //if any other worker's position is ADMIN this code will not work
            if(!positionEmployee.equals("Админ")) {
                ps.setString(4, positionEmployee);
            }
            ps.setString(5,passwordEmployee);
            ps.setInt(6,idStoreEmployee);
            ps.execute();
            System.out.println("Create employee is successful");
            printToClient.println("Create employee is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with create of employee");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }



    //create store
    //works
    public void createStore(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="INSERT INTO store(store_address,store_name,store_phone) VALUES(?,?,?)";
            ps=connection.prepareStatement(sql);
            printToClient.println("create new store: ");
            printToClient.println("Enter address of store: ");
            String addressStore=inputFromClient.nextLine();
            printToClient.println("Enter name of store: ");
            String nameStore=inputFromClient.nextLine();
            printToClient.println("Enter phone of store: ");
            String phoneStore= inputFromClient.nextLine();
            ps.setString(1,addressStore);
            ps.setString(2,nameStore);
            if(checkPhone(phoneStore)) {
                ps.setString(3, phoneStore);
            }
            ps.execute();
            System.out.println("Create store is successful");
            printToClient.println("Create store is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with create store");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }


    //create book
    //works
    public void createBook(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="INSERT INTO book(book_author,book_price,book_publisher,book_title,publisher_publisher_ID) VALUES(?,?,?,?,?)";
            ps=connection.prepareStatement(sql);
            printToClient.println("Create product: ");
            printToClient.println("Enter author of book: ");
            String bookAuthor=inputFromClient.nextLine();
            printToClient.println("Enter price of book: ");
            double priceBook=inputFromClient.nextDouble();
            printToClient.println("Enter publisher of book: ");
            String publisherBook=inputFromClient.next();
            printToClient.println("Enter title of book: ");
            String titleBook=inputFromClient.next();
            printToClient.println("Enter id of book's publisher");
            int idPublisherBook=inputFromClient.nextInt();
            ps.setString(1,bookAuthor);
            ps.setDouble(2,priceBook);
            ps.setString(3,publisherBook);
            ps.setString(4,titleBook);
            ps.setInt(5,idPublisherBook);
            ps.execute();
            System.out.println("Create book is successful");
            printToClient.println("Create book is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with create product");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }


    //create customer
    //works
    public void createCustomer(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="INSERT INTO customer(Customer_email,customer_name,customer_phone,password) VALUES(?,?,?,?)";
            ps=connection.prepareStatement(sql);
            printToClient.println("Create customer....");
            printToClient.println("Enter email of customer: ");
            String emailCustomer=inputFromClient.nextLine();
            printToClient.println("Enter name of customer: ");
            String nameCustomer=inputFromClient.nextLine();
            printToClient.println("Enter phone of customer: ");
            String phoneCustomer=inputFromClient.nextLine();
            printToClient.println("Enter password of customer: ");
            String passwordCustomer=inputFromClient.nextLine();
            //check email
            if(checkEmail(emailCustomer)) {
                ps.setString(1, emailCustomer);
            }
            ps.setString(2,nameCustomer);
            //check phone
            if(checkPhone(phoneCustomer)) {
                ps.setString(3, phoneCustomer);
            }
            ps.setString(4,passwordCustomer);
            ps.execute();
            System.out.println("Create customer is successful");
            printToClient.println("Create customer is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with create customer");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }





    ////////////////////////////////////////////////EDIT REQUESTS/////////////////////////////////////////////////////////////////////

//edit customer
    //works
    synchronized public void editCustomer(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="UPDATE customer SET Customer_email=?,customer_name=?,customer_phone=?,password=? WHERE customer_ID=?";
            ps=connection.prepareStatement(sql);
            printToClient.println("Edit customer.....");
            printToClient.println("Enter email of customer: ");
            String emailCustomer=inputFromClient.nextLine();
            printToClient.println("Enter name of customer: ");
            String nameCustomer=inputFromClient.nextLine();
            printToClient.println("Enter phone of customer: ");
            String phoneCustomer=inputFromClient.nextLine();
            printToClient.println("Enter password of customer:");
            String passwordCustomer=inputFromClient.nextLine();
            printToClient.println("Enter id of customer which will been change");
            int idCustomer=inputFromClient.nextInt();
            //check email
            if(checkEmail(emailCustomer)) {
                ps.setString(1, emailCustomer);
            }
            ps.setString(2,nameCustomer);
            //check phone
            if(checkPhone(phoneCustomer)) {
                ps.setString(3, phoneCustomer);
            }
            ps.setString(4,passwordCustomer);
            ps.setInt(5,idCustomer);
            ps.execute();
            System.out.println("Edit customer is successful");
            printToClient.println("Edit customer is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with edit customer");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }



    //edit employee
    //works
    synchronized public void editEmployee(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="UPDATE employee SET employee_email=?,employee_name=?,employee_phone=?,employee_position=?,password=?,store_store_ID=? WHERE employee_ID=?";
            ps=connection.prepareStatement(sql);
            printToClient.println("Edit employee..... ");
            printToClient.println("Enter employee's email: ");
            String emailEmployee=inputFromClient.nextLine();
            printToClient.println("Enter employee's name: ");
            String nameEmployee=inputFromClient.nextLine();
            printToClient.println("Enter employee's phone: ");
            String phoneEmployee=inputFromClient.nextLine();
            printToClient.println("Enter employee's position: ");
            String positionEmployee=inputFromClient.nextLine();
            printToClient.println("Enter employee's password: ");
            String passwordEmployee=inputFromClient.nextLine();
            printToClient.println("Enter employee's id store where he works");
            int idStoreEmployee=inputFromClient.nextInt();
            printToClient.println("Enter id of Employee which will bi change");
            int idEmployee=inputFromClient.nextInt();
            //check email
            if(checkEmail(emailEmployee)) {
                ps.setString(1, emailEmployee);
            }
            ps.setString(2,nameEmployee);
            //check phone
            if(checkPhone(phoneEmployee)) {
                ps.setString(3, phoneEmployee);
            }
            ps.setString(4,positionEmployee);
            ps.setString(5,passwordEmployee);
            ps.setInt(6,idStoreEmployee);
            ps.setInt(7,idEmployee);
            ps.execute();
            System.out.println("Editing employee is successful");
            printToClient.println("Editing employee is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with editing employee");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }



/////////////////////////////////////////////check methods///////////////////////////////////

    //check email
    public boolean checkEmail(String email){
        String regex="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(!email.matches(regex)){
            printToClient.println("Incorrect email!");
            return  false;
        }
        return true;
    }

    //check phone number
    public boolean checkPhone(String phone){
        String regex="[0-9]{10}";
        if(!phone.matches(regex)){
            printToClient.println("Incorrect phone number");
            return false;
        }
        return true;
    }



}
