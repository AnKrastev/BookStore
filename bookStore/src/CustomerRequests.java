import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CustomerRequests {

    //connect to database
    Connection connection=null;
    //object for set sql script to database
    PreparedStatement ps=null;
    //getting result from database
    ResultSet rs=null;
    //print information to the client
    PrintStream printToClient=null;
    //input information from client
    Scanner inputFromClient=null;

//CREATE VARIABLE TOTALAMOUNT
    private double totalAmount=0;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    //////////////////////////////////////////SELECT REQUESTS////////////////////////////////////////////////////////

    //select book
    //works
    public void selectProduct(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM book";
            ps=connection.prepareStatement(sql);
            printToClient.println("All products are: ");
            //get results and show them to the client
            rs=ps.executeQuery();
            while(rs.next()){
                int bookID= rs.getInt("book_ID");
                String bookAuthor=rs.getString("book_author");
                double bookPrice=rs.getDouble("book_price");
                String bookPublisher=rs.getString("book_publisher");
                String bookTitle=rs.getString("book_title");
                printToClient.println(bookID+" "+bookAuthor+" "+bookPrice+" "+bookPublisher+" "+bookTitle);
            }
            System.out.println("Select products is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with visualisation of products");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }

    }



    public void selectProductFromShoppingCart(Socket clientSocket,int idBook,int quality){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT book.book_title,book.book_price FROM book WHERE book_ID=?";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,idBook);
            rs=ps.executeQuery();
            while(rs.next()){
                String titleBook=rs.getString("book_title");
                double price=rs.getDouble("book_price");
                //added this price multiplq quality to totalAmount
                totalAmount=price*quality;
                printToClient.print(titleBook+" "+price);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with shoppingCart");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }


    /////////////////////////////////////////////FILTERS TO DATABASE//////////////////////////////////////////////////////


    //filters books which are between two prices
    //works
    public void bookFilterBetweenTwoPrices(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM book WHERE book_price BETWEEN ? AND ?";
            ps=connection.prepareStatement(sql);
            printToClient.println("Filters product between two prices....");
            printToClient.println("Enter min price of product: ");
            double minPrice=inputFromClient.nextDouble();
            printToClient.println("Enter max price of product: ");
            double maxPrice=inputFromClient.nextDouble();
            ps.setDouble(1,minPrice);
            ps.setDouble(2,maxPrice);
            ps.execute();
            rs=ps.executeQuery();
            while(rs.next()){
                int bookId=rs.getInt("book_ID");
                String bookAuthor=rs.getString("book_author");
                String bookPrice=rs.getString("book_price");
                String bookPublisher=rs.getString("book_publisher");
                String bookTitle=rs.getString("book_title");
                printToClient.println(bookId+" "+bookAuthor+" "+bookPrice+" "+bookPublisher+" "+bookTitle);
            }
            System.out.println("Filtering of product is successful");
            printToClient.println("Filtering of product is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with filter books");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }



    //sorted books by price
    //works
    public void sortBooks(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM book ORDER BY book_price";
            ps=connection.prepareStatement(sql);
            printToClient.println("Sort books....");
            rs= ps.executeQuery();
            while(rs.next()){
                int bookId=rs.getInt("book_ID");
                String bookAuthor=rs.getString("book_author");
                String bookPrice=rs.getString("book_price");
                String bookPublisher=rs.getString("book_publisher");
                String bookTitle=rs.getString("book_title");
                printToClient.println(bookId+" "+bookAuthor+" "+bookPrice+" "+bookPublisher+" "+bookTitle);
            }
            System.out.println("Select book is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with sorting products");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }

/////////////////////////////////////////REGISTER FORM/////////////////////////////////
//register form
//works
public boolean registerForm(Socket clientSocket){
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
        return true;
    }catch (IOException e){
        System.out.println(e.getMessage());
        printToClient.println("Error with create customer");
    }catch (Exception e){
        System.out.println(e.getMessage());
        printToClient.println("Error");
    }
    return false;
}

//create order
public void createOrder(Socket clientSocket, int idCustomer, Date orderDate, int idStore, double totalAmount){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="INSERT INTO order(customer_customer_ID,order_date,store_store_ID,total_amount) VALUES(?,?,?,?)";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,idCustomer);
            ps.setDate(2,orderDate);
            ps.setInt(3,idStore);
            ps.setDouble(4,totalAmount);
            ps.execute();
            printToClient.println("Order is successful");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with order");
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
