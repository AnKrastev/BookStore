import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User{


    private HashMap<Integer,Integer> shoppingCart;

    public Customer(String email, String password, String name, String phone) {
        super(email, password, name, phone);
        shoppingCart=new HashMap<>();
    }

    public HashMap<Integer, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(HashMap<Integer,Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Override
    public TypeUser getType() {
        return TypeUser.Customer;
    }
}
