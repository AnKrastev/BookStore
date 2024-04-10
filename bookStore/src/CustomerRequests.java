import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

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

     double totalAmount=0;



    //////////////////////////////////////////SELECT REQUESTS////////////////////////////////////////////////////////

    //select book
    //works
    public void selectProduct(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            //set new book price if we have reduction
            //reduction must be here because it in while cicle will be return error
            double reduction=reduction(clientSocket);
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
                if(reduction!=0){
                    bookPrice=bookPrice-bookPrice*(reduction/100);
                }
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


//works
    public void selectProductFromShoppingCart(Socket clientSocket,int idBook,int quality){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            //the reduction must be out from while cycle
            //added this price multiplq quality to totalAmount
            double reduction=reduction(clientSocket);
            String sql="SELECT book.book_title,book.book_price FROM book WHERE book_ID=?";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,idBook);
            rs=ps.executeQuery();
            while(rs.next()){
                String titleBook=rs.getString("book_title");
                double price=rs.getDouble("book_price");
                if(reduction!=0){
                   price=price-price*(reduction/100);
                }
                totalAmount+=(price*quality);
                printToClient.print(titleBook+" "+price+" "+quality);
            }
            System.out.println(totalAmount);
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with shoppingCart");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }


    //SELECT STORE
    //works
    public int selectStore(Socket clientSocket,int bookID){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT store_store_ID,MAX(quantity) FROM inventory WHERE book_book_ID=? GROUP BY store_store_ID HAVING MAX(quantity) AND MAX(quantity)>0  LIMIT 1 ";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,bookID);
            rs=ps.executeQuery();
            printToClient.println("All stores: ");
            while(rs.next()){
                int idStore=rs.getInt("store_store_ID");
                return idStore;
            }
            System.out.println("Select of stores is successful");
        }catch(IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with select store");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
        return 0;
    }

//select reduction
    //works
    public double reduction(Socket clientSocket){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            //get today's date
            LocalDateTime today = LocalDateTime.now();
            // Define the desired format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // Format the LocalDateTime object into a String
            String todayDate = today.format(formatter);
            //check today's date with start date and end date from every reduction and if have match get percent of reduction
            String sql="SELECT percentReduction FROM reduction WHERE ? BETWEEN startDate AND endDate";
            ps=connection.prepareStatement(sql);
            ps.setString(1,todayDate);
            rs= ps.executeQuery();
            while(rs.next()){
                int percentReduction=rs.getInt("percentReduction");
                //return percent reduction
                return percentReduction;
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with reduction");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
        //return 0 if it doesn't have reduction
        return 0;
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
                double bookPrice=rs.getDouble("book_price");
                String bookPublisher=rs.getString("book_publisher");
                String bookTitle=rs.getString("book_title");
                double reduction=reduction(clientSocket);
                if(reduction!=0){
                    bookPrice=bookPrice-bookPrice*(reduction/100);
                }
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
                double bookPrice=rs.getDouble("book_price");
                String bookPublisher=rs.getString("book_publisher");
                String bookTitle=rs.getString("book_title");
                double reduction=reduction(clientSocket);
                if(reduction!=0){
                    bookPrice=bookPrice-bookPrice*(reduction/100);
                }
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


////////////////////////////////////////////////////////////////ORDER REQUEST///////////////////////////////////////////////
    //////////////////////////////////////////////EVERYTHINK FOR ORDER///////////////////////////////////////////
//works
//create order
public void createOrder(Socket clientSocket, int idCustomer, String orderDate, int idStore, double totalAmount){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="INSERT INTO `order`(customer_customer_ID,order_date,store_store_ID,total_amount) VALUES(?,?,?,?)";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,idCustomer);
            ps.setString(2,orderDate);
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

//??????????????????????????????????????????????????/
//create order detail
    public void createOrderDetail(Socket clientSocket,int quantity,int orderId,int book_id,double unitPrice){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            double reduction=reduction(clientSocket);
            String sql="INSERT INTO orderdetails(book_book_ID,order_order_ID,quantity,unit_price) VALUES(?,?,?,?)";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,book_id);
            ps.setInt(2,orderId);
            ps.setInt(3,quantity);
            if(reduction!=0){
                unitPrice=unitPrice-unitPrice*(reduction/100);
            }
            ps.setDouble(4,unitPrice);
            ps.execute();
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with orderDetails");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }



    //select for order
    //return idOrder
    public int selectOrderId(String orderDate,int customerID){
        try{
            connection=MySQLConnection.connection();
            String sql="SELECT order_ID FROM `order` WHERE order_date=? and customer_customer_ID=?";
            ps=connection.prepareStatement(sql);
            ps.setString(1,orderDate);
            ps.setInt(2,customerID);
            ps.execute();
            rs=ps.executeQuery();
            while(rs.next()){
                int idOrder=rs.getInt("order_ID");
                return idOrder;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
        return 0;
    }


    //select for order
    //return bookPrice
    public double selectPriceBook(int idBook){
        try{
            connection=MySQLConnection.connection();
            String sql="SELECT book_price FROM book WHERE book_ID=?";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,idBook);
            rs=ps.executeQuery();
            while(rs.next()){
                double bookPrice=rs.getDouble("book_price");
                return bookPrice;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
        return 0;
    }




    //////////////////////////////////////////////////////reducing the number in the store///////////////////////////////////
    //reduction the number of product in the store
    //synchronized this method because every thread will could edit this variable
    synchronized public void qualityBooksInStore(Socket clientSocket,int idBook,int idStore,int quantityBook){
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="UPDATE inventory SET quantity=quantity-? WHERE book_book_ID=? AND store_store_ID=?";
            ps=connection.prepareStatement(sql);
            ps.setInt(1,quantityBook);
            ps.setInt(2,idBook);
            ps.setInt(3,idStore);
            ps.execute();
            System.out.println("Successful reduction of product");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with reductionStore's inventory");
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
