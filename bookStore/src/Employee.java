public class Employee extends User{

    private String position;

    public Employee(String email, String password, String name, String phone,String position) {
        super(email, password, name, phone);
        this.position=position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public TypeUser getType() {
        return TypeUser.Employee;
    }
}
