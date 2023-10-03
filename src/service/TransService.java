package service;
import Object.UserOBJ;
public interface TransService {
    public abstract void deposit(Double amount, UserOBJ userOBJ);
    public abstract void withdraw(Double amount, UserOBJ userOBJ);
    public abstract void transfer(Double amount, UserOBJ userOBJ, int to_account_number);
}