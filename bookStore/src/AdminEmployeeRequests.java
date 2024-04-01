import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AdminEmployeeRequests {


    //create connection to database
    private Connection connection=null;
    //Object which set sql script to database
    private PreparedStatement ps=null;
    //getting result from database
    private ResultSet rs=null;
    //print to client messages
    private PrintStream printToClient=null;
    //input from client messages
    private Scanner inputFromClient=null;


////////////////////////////////////////////////////////////////////////SELECT REQUESTS/////////////////////////////////////////////

    //Select all products from table BOOK
    public void selectProduct(Socket clientSocket){
        printToClient.println("All products are: ");
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM book";
            ps=connection.prepareStatement(sql);
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



//SELECT ALL PRODUCTS
    public void selectReduction(Socket clientSocket){
        printToClient.println("All reductions: ");
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM reduction";
            ps=connection.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                int idReduction=rs.getInt("id");
                Date startDate=rs.getDate("startDate");
                Date endDate=rs.getDate("endDate");
                int percentReduction=rs.getInt("percentReduction");
                printToClient.println(idReduction+" "+startDate+" "+endDate+" "+percentReduction);
            }
            System.out.println("Select of reductions is successful");
        }catch(IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with select reduction");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }


    //SELECT STORE
    public void selectStore(Socket clientSocket){
        printToClient.println("All stores: ");
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM store";
            ps=connection.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                int idStore=rs.getInt("store_ID");
                String addressStore=rs.getString("store_address");
                String nameStore=rs.getString("store_name");
                String phoneStore=rs.getString("store_phone");
                printToClient.println(idStore+" "+addressStore+" "+nameStore+" "+phoneStore);
            }
            System.out.println("Select of stores is successful");
        }catch(IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with select store");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }








////////////////////////////////////////////////////////////////EDIT REQUESTS///////////////////////////////////////////////////
    //edit book
    public void editProducts(Socket clientSocket){
        printToClient.println("Edit price of Products");
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            String sql="UPDATE book SET book_price=? WHERE book_ID=?";
            ps=connection.prepareStatement(sql);
            //set staff to the variables
            printToClient.println("Enter bookId: ");
            int bookID=inputFromClient.nextInt();
            printToClient.println("Enter new price of product: ");
            double newPrice=inputFromClient.nextDouble();
            ps.setDouble(1,newPrice);
            ps.setInt(2,bookID);
            ps.execute();
            System.out.println("Successful edit");
            printToClient.println("Successful edit product");
        }catch(IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with edit of products");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }
    }


    //edit reduction
    public void editReduction(Socket clientSocket){
        printToClient.println("Edit reduction: ");
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            connection=MySQLConnection.connection();
            String sql="UPDATE reduction SET startDate=?, endDate=?, percentReduction=? WHERE id=?";
            ps=connection.prepareStatement(sql);
            //set staff to variables
            //set Date
            printToClient.println("Enter startDate of reduction: ");
            String firstDate=inputFromClient.next();
            //Instantiating the SimpleDateFormat class
            SimpleDateFormat simpleFirstDate=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            //Parsing the given String to Date object
            Date firstDateReduction=simpleFirstDate.parse(firstDate);
            //set second date
            printToClient.println("Enter second date of reduction: ");
            String secondDate=inputFromClient.next();
            SimpleDateFormat simpleSecondDate=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            Date secondDateReduction=simpleSecondDate.parse(secondDate);
            //others variable
            printToClient.println("Enter new percent of reduction: ");
            int percentReduction=inputFromClient.nextInt();
            printToClient.println("Enter id of reduction which will be edit");
            int idReduction=inputFromClient.nextInt();
            //set the variables to ps Object and then execute it
            ps.setDate(1, (java.sql.Date) firstDateReduction);
            ps.setDate(2, (java.sql.Date) secondDateReduction);
            ps.setInt(3,percentReduction);
            ps.setInt(4,idReduction);
            ps.execute();
            System.out.println("Successful edit reduction");
            printToClient.println("Successful edit reduction");
        }catch (IOException e){
            System.out.println(e.getMessage());
            printToClient.println("Error with edit reduction");
        }catch (Exception e){
            System.out.println(e.getMessage());
            printToClient.println("Error");
        }


    }




}
