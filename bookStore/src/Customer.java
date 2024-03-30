import java.util.ArrayList;

public class Customer extends User{


    private ArrayList<Book> shoppingCart;

    public Customer(String email, String password, String name, String phone) {
        super(email, password, name, phone);
        shoppingCart=new ArrayList<Book>();
    }

    public ArrayList<Book> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<Book> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Override
    public TypeUser getType() {
        return TypeUser.Customer;
    }
}
