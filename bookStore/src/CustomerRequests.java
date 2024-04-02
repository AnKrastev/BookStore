import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
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



    //////////////////////////////////////////SELECT REQUESTS////////////////////////////////////////////////////////

    //select book
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





    /////////////////////////////////////////////FILTERS TO DATABASE//////////////////////////////////////////////////////


    //filters books which are between two prices
    public void bookFilterBetweenTwoPrices(Socket clientSocket){
        printToClient.println("Filters product between two prices....");
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM book WHERE book_price BETWEEN ? AND ?";
            ps=connection.prepareStatement(sql);
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
    public void sortBooks(Socket clientSocket){
        printToClient.println("Sort books....");
        try{
            printToClient=new PrintStream(clientSocket.getOutputStream());
            inputFromClient=new Scanner(clientSocket.getInputStream());
            connection=MySQLConnection.connection();
            String sql="SELECT * FROM book ORDER BY book_price";
            ps=connection.prepareStatement(sql);
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


}
