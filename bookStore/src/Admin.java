public class Admin extends User{


    public Admin(String email, String password, String name, String phone) {
        super(email, password, name, phone);
    }

    @Override
    public TypeUser getType() {
        return TypeUser.Admin;
    }
}
