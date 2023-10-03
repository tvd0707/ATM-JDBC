package service;
import Object.*;
public interface AdminService {

    boolean login();
    void viewTransaction();
    void viewAccount();
    void findAccount();
    void findTransaction();
    void accountStatistics();
    void transactionStatistics();
    void updateUserInfo();
    void logout();
}