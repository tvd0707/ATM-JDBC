package Object;

public class UserOBJ {

    //Thông tin tài khoản
    private int account_id;
    private String account_holder;
    private Double account_balance;
    private String account_number;
    private String account_passcode;
    private boolean is_admin = false;
    //Setters
    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public void setAccount_holder(String account_holder) {
        this.account_holder = account_holder;
    }

    public void setAccount_balance(Double account_balance) {
        this.account_balance = account_balance;
    }

    public void setAccount_passcode(String account_passcode) {
        this.account_passcode = account_passcode;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
    //Getters
    public String getAccount_number() {
        return account_number;
    }
    public String getAccount_holder() {
        return account_holder;
    }
    public Double getAccount_balance() {
        return account_balance;
    }
    public String getAccount_passcode() {
        return account_passcode;
    }
    public int getAccount_id() {
        return account_id;
    }
    public boolean isIs_admin() {
        return is_admin;
    }

}