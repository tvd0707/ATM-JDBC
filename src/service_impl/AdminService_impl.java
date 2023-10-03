package service_impl;

import database.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;

public class AdminService_impl implements service.AdminService {
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	Scanner sc = new Scanner(System.in);
	DAO dao = new DAO();
	Connection connection = DbConn.getInstance().getConnection();

	@Override
	public boolean login() {
		try {
			String account_number, account_passcode;
			System.out.print("Enter account number: ");
			account_number = sc.nextLine();
			System.out.print("Enter account passcode: ");
			account_passcode = sc.nextLine();
			String sql = "SELECT * FROM account WHERE account_number = ? AND account_passcode = ? AND is_admin = true";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, account_number);
			preparedStatement.setString(2, account_passcode);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			preparedStatement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void viewTransaction() {
		dao.viewTransaction();
	}

	@Override
	public void viewAccount() {
		dao.viewUser();
	}

	@Override
	public void findAccount() {
		dao.findAccount();
	}

	@Override
	public void findTransaction() {
		dao.findTransaction();
	}

	@Override
	public void accountStatistics() {
		dao.totalAccount();
	}

	@Override
	public void transactionStatistics() {
		dao.totalTransaction();
	}

	@Override
	public void updateUserInfo() {
		try {
			int id;
			String account_holder, account_passcode, account_number;
			boolean is_admin;
			System.out.print("Enter account ID: ");
			id = Integer.parseInt(sc.nextLine());
			System.out.print("Enter account holder: ");
			account_holder = sc.nextLine();
			System.out.print("Enter account number: ");
			account_number = sc.nextLine();
			System.out.print("Enter account passcode: ");
			account_passcode = sc.nextLine();
			System.out.print("Enter account admin status: ");
			is_admin = Boolean.parseBoolean(sc.nextLine());
			String sql = "UPDATE account SET account_holder = ?, account_number = ?, account_passcode = ?, is_admin = ? WHERE account_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, account_holder);
			preparedStatement.setString(2, account_number);
			preparedStatement.setString(3, account_passcode);
			preparedStatement.setBoolean(4, is_admin);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
			System.out.println("Update successfully!");
			preparedStatement.close();

		} catch (SQLException e) {
			System.out.println("Update failed!");
			e.printStackTrace();
		}

	}

	@Override
	public void logout() {
		System.out.println("Logout successfully!");
	}
}