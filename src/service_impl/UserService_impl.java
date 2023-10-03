package service_impl;

import database.*;
import Object.UserOBJ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class UserService_impl implements service.UserService {
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	Connection connection = DbConn.getInstance().getConnection();
	UserOBJ userOBJ = new UserOBJ();
	DAO dao = new DAO();
	Scanner sc = new Scanner(System.in);

	public boolean register() {
		return dao.insertUser();
	}

	@Override
	public UserOBJ setUser(UserOBJ user) {
		this.userOBJ = user;
		user.setAccount_id(user.getAccount_id());
		user.setAccount_number(user.getAccount_number());
		user.setAccount_holder(user.getAccount_holder());
		user.setAccount_balance(user.getAccount_balance());
		user.setAccount_passcode(user.getAccount_passcode());
		user.setIs_admin(user.isIs_admin());
		return user;
	}

	private void setUserFromResultSet(ResultSet resultSet) throws Exception {
		userOBJ.setAccount_id(resultSet.getInt("account_id"));
		userOBJ.setAccount_number(resultSet.getString("account_number"));
		userOBJ.setAccount_holder(resultSet.getString("account_holder"));
		userOBJ.setAccount_balance(resultSet.getDouble("account_balance"));
		userOBJ.setAccount_passcode(resultSet.getString("account_passcode"));
	}

	@Override
	public boolean login() {
		try {
			String account_number;
			System.out.print("Enter account number: ");
			account_number = sc.nextLine();
			System.out.print("Enter account passcode: ");
			String account_passcode = sc.nextLine();

			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT * FROM account WHERE account_number = ? AND account_passcode = ?");
			preparedStatement.setString(1, account_number);
			preparedStatement.setString(2, account_passcode);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				setUserFromResultSet(resultSet);
				preparedStatement.close();
				return true;
			} else {
				System.out.println("Invalid account number or passcode!");
				preparedStatement.close();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void viewInfo() {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT * FROM account WHERE account_number = ?");
			preparedStatement.setString(1, userOBJ.getAccount_number());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				System.out.println("Account number: " + resultSet.getString("account_number"));
				System.out.println("Account holder: " + resultSet.getString("account_holder"));
				System.out.println("Account balance: $" + resultSet.getDouble("account_balance"));
			}
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void viewTransactionHistory() {
		try {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transaction\n"
					+ "INNER JOIN account ON transaction.from_account_number = account.account_number\n"
					+ "WHERE  account.account_number = ?\n");
			preparedStatement.setString(1, userOBJ.getAccount_number());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				// In thông tin giao dịch
				System.out.print("Transaction ID: " + resultSet.getInt("trans_id") + ", From account number: "
						+ resultSet.getString("from_account_number") + ", To account number: "
						+ resultSet.getString("to_account_number") + ", Amount: $" + resultSet.getDouble("trans_amount")
						+ ", Type: " + resultSet.getString("trans_type") + ", Date: "
						+ sdf.format(resultSet.getTimestamp("trans_date")) + ", Status: "
						+ resultSet.getString("trans_status") + "\n");
			}

			PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM transaction\n"
					+ "INNER JOIN account ON transaction.to_account_number = account.account_number\n"
					+ "WHERE  account.account_number = ?\n");
			preparedStatement1.setString(1, userOBJ.getAccount_number());
			ResultSet resultSet1 = preparedStatement1.executeQuery();
			while (resultSet.next()) {
				// In thông tin giao dịch
				System.out.print("Transaction ID: " + resultSet1.getInt("trans_id") + ", From account number: "
						+ resultSet1.getString("from_account_number") + ", To account number: "
						+ resultSet1.getString("to_account_number") + ", Amount: $" + resultSet1.getDouble("trans_amount")
						+ ", Type: " + resultSet1.getString("trans_type") + ", Date: "
						+ sdf.format(resultSet1.getTimestamp("trans_date")) + "\n");
			}
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deposit() {
		System.out.print("Enter amount to deposit: ");
		double amount = Double.parseDouble(sc.nextLine());
		try {
			if (amount <= 0) {
				PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
				preparedStatement.setString(1, userOBJ.getAccount_number());
				preparedStatement.setString(2, userOBJ.getAccount_number());
				preparedStatement.setDouble(3, amount);
				preparedStatement.setString(4, "Deposit");
				preparedStatement.setString(5, "Fail");
				preparedStatement.executeUpdate();
				preparedStatement.close();
				System.out.println("Invalid amount!");
			} else {
				PreparedStatement preparedStatement = connection.prepareStatement(
						"UPDATE account SET account_balance = account_balance + ? WHERE account_number = ?");
				preparedStatement.setDouble(1, amount);
				preparedStatement.setString(2, userOBJ.getAccount_number());
				preparedStatement.executeUpdate();
				preparedStatement.close();
				// Insert data vào transaction
				preparedStatement = connection.prepareStatement(
						"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
				preparedStatement.setString(1, userOBJ.getAccount_number());
				preparedStatement.setString(2, userOBJ.getAccount_number());
				preparedStatement.setDouble(3, amount);
				preparedStatement.setString(4, "Deposit");
				preparedStatement.setString(5, "Success");
				preparedStatement.executeUpdate();
				preparedStatement.close();
				System.out.println("Deposit successfully!");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean withdraw() {
		System.out.print("Enter amount to withdraw: ");
		double amount = Double.parseDouble(sc.nextLine());
		try {
			if (amount <= 0) {
				// Invalid amount -> Fail
				PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
				preparedStatement.setString(1, userOBJ.getAccount_number());
				preparedStatement.setString(2, userOBJ.getAccount_number());
				preparedStatement.setDouble(3, amount);
				preparedStatement.setString(4, "Withdraw");
				preparedStatement.setString(5, "Fail");
				preparedStatement.executeUpdate();
				preparedStatement.close();
				System.out.println("Invalid amount!");
			} else if (amount > userOBJ.getAccount_balance()) {
				// Not enough money -> Fail
				PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
				preparedStatement.setString(1, userOBJ.getAccount_number());
				preparedStatement.setString(2, userOBJ.getAccount_number());
				preparedStatement.setDouble(3, amount);
				preparedStatement.setString(4, "Withdraw");
				preparedStatement.setString(5, "Fail");
				preparedStatement.executeUpdate();
				preparedStatement.close();
				System.out.println("Not enough money!");
			} else {
				String account_passcode;
				System.out.print("Enter account passcode: ");
				account_passcode = sc.next();
				if (account_passcode != userOBJ.getAccount_passcode()) {
					// Wrong passcode -> Fail
					PreparedStatement preparedStatement = connection.prepareStatement(
							"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
					preparedStatement.setString(1, userOBJ.getAccount_number());
					preparedStatement.setString(2, userOBJ.getAccount_number());
					preparedStatement.setDouble(3, amount);
					preparedStatement.setString(4, "Withdraw");
					preparedStatement.setString(5, "Fail");
					preparedStatement.executeUpdate();
					preparedStatement.close();
					System.out.println("Wrong passcode!");
				} else {
					PreparedStatement preparedStatement = connection.prepareStatement(
							"UPDATE account SET account_balance = account_balance - ? WHERE account_number = ? AND account_passcode = ?;");
					preparedStatement.setDouble(1, amount);
					preparedStatement.setString(2, userOBJ.getAccount_number());
					preparedStatement.setString(3, userOBJ.getAccount_passcode());
					PreparedStatement preparedStatement1 = connection.prepareStatement(
							"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
					preparedStatement1.setString(1, userOBJ.getAccount_number());
					preparedStatement1.setString(2, userOBJ.getAccount_number());
					preparedStatement1.setDouble(3, amount);
					preparedStatement1.setString(4, "Withdraw");
					preparedStatement1.setString(5, "Success");
					preparedStatement.executeUpdate();
					preparedStatement1.executeUpdate();
					preparedStatement.close();
					preparedStatement1.close();
					System.out.println("Withdraw successfully!");
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean transfer() {
		try {
			System.out.print("Enter account number to transfer: ");
			String account_number = sc.nextLine();
			if (account_number.equals(userOBJ.getAccount_number())) {
				System.out.println("Cannot transfer to your own account!");
			} else if (!dao.checkAccount(account_number)) {
				System.out.println("Account not found!");
			} else {
				System.out.print("Enter amount to transfer: ");
				double amount = Double.parseDouble(sc.nextLine());
				if (amount <= 0) {
					PreparedStatement preparedStatement = connection.prepareStatement(
							"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
					preparedStatement.setString(1, userOBJ.getAccount_number());
					preparedStatement.setString(2, account_number);
					preparedStatement.setDouble(3, amount);
					preparedStatement.setString(4, "Transfer");
					preparedStatement.setString(5, "Fail");
					preparedStatement.executeUpdate();
					preparedStatement.close();
					System.out.println("Invalid amount!");
				} else if (amount > userOBJ.getAccount_balance()) {
					PreparedStatement preparedStatement = connection.prepareStatement(
							"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
					preparedStatement.setString(1, userOBJ.getAccount_number());
					preparedStatement.setString(2, account_number);
					preparedStatement.setDouble(3, amount);
					preparedStatement.setString(4, "Transfer");
					preparedStatement.setString(5, "Fail");
					preparedStatement.executeUpdate();
					System.out.println("Not enough money!");
				} else {
					String account_passcode;
					System.out.print("Enter account passcode: ");
					account_passcode = sc.next();
					if (!account_passcode.equals(userOBJ.getAccount_passcode())) {
						PreparedStatement preparedStatement = connection.prepareStatement(
								"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
						preparedStatement.setString(1, userOBJ.getAccount_number());
						preparedStatement.setString(2, account_number);
						preparedStatement.setDouble(3, amount);
						preparedStatement.setString(4, "Transfer");
						preparedStatement.setString(5, "Fail");
						preparedStatement.executeUpdate();
						System.out.println("Wrong passcode!");
					} else {
						// Thực hiện chuyển tiền từ tài khoản của người gửi
						performTransferFromSender(amount, account_passcode);

						// Cập nhật số dư tài khoản người nhận
						updateRecipientBalance(amount, account_number);
						PreparedStatement preparedStatement = connection.prepareStatement(
								"INSERT INTO transaction(trans_date,from_account_number, to_account_number, trans_amount, trans_type, trans_status) VALUES (NOW(), ?, ?, ?, ?, ?)");
						preparedStatement.setString(1, userOBJ.getAccount_number());
						preparedStatement.setString(2, account_number);
						preparedStatement.setDouble(3, amount);
						preparedStatement.setString(4, "Transfer");
						preparedStatement.setString(5, "Success");
						preparedStatement.executeUpdate();
						System.out.println("Transfer successfully!");
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
		return false;
	}

	private void performTransferFromSender(double amount, String account_passcode) throws Exception {
		PreparedStatement preparedStatement = connection.prepareStatement(
				"UPDATE account SET account_balance = account_balance - ? WHERE account_number = ? AND account_passcode = ?");
		preparedStatement.setDouble(1, amount);
		preparedStatement.setString(2, userOBJ.getAccount_number());
		preparedStatement.setString(3, account_passcode);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

	private void updateRecipientBalance(double amount, String account_number) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("UPDATE account SET account_balance = account_balance + ? WHERE account_number = ?");
		preparedStatement.setDouble(1, amount);
		preparedStatement.setString(2, account_number);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

	public void logout() {
		userOBJ = new UserOBJ();
		System.out.println("Logout successfully!");
	}

}