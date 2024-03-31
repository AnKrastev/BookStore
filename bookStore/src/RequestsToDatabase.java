import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RequestsToDatabase {


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


    //select request for all customers
    public void select(Socket clientSocket) {
        System.out.println("Select all customers: ");
        try{
            //initialization the objects
            printToClient=new PrintStream(clientSocket.getOutputStream());
            Scanner inputFromClient=new Scanner(clientSocket.getInputStream());
            //create sql script for the request
            String sqlScript="SELECT * FROM customer";
            ps=connection.prepareStatement(sqlScript);
            resultSet=ps.executeQuery();
            //getting the results and bring them out to client's screen
            while(resultSet.next()){
                String customerName=resultSet.getString("customer_name");
                String customerPhone=resultSet.getString("customer_phone");
                String customerEmail=resultSet.getString("Customer_email");
                printToClient.println(customerName+" "+customerPhone+" "+customerEmail);
            }
        }catch(SQLException e){
            System.out.println("Ima problem");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }



    public void deleteReduction(Socket clientSocket) {
        try{
            //initialization the objects
            printToClient=new PrintStream(clientSocket.getOutputStream());
            Scanner inputFromClient=new Scanner(clientSocket.getInputStream());
            //create sql script for the request
            String sqlScript="DELETE FROM reduction WHERE id=?";
            ps=connection.prepareStatement(sqlScript);
            printToClient.println("Input the id of reduction which will be delete");
            int reductionID=inputFromClient.nextInt();
            System.out.println(reductionID);
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



}
