import java.sql.Connection;
import java.sql.PreparedStatement;

public class RequestsToDatabase {


    Connection connection=MySQLConnection.connection();
    PreparedStatement pst=null;

    public void select(){
        System.out.println("Select all customers");
        try{
            String sqlScript="SELECT * FROM customer";
            pst=connection.prepareStatement(sqlScript);
            pst.execute();
            System.out.println();
        }catch(Exception e){
            System.out.println("Ima problem");
        }
    }



}
