import database.DAO;
import service_impl.*;
public class test {
    public static void main(String[] args) {
//        DAO dao = new DAO();
//        dao.viewUser();
//        dao.viewTransaction();
//        dao.findAccount();
//        dao.totalAccount();
//        dao.totalTransaction();
        UserService_impl userService_impl = new UserService_impl();
//        userService_impl.login();
        userService_impl.register();


    }
}