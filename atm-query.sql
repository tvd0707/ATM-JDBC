CREATE DATABASE atm2studentms_hibernateatm2
    		CHARACTER SET "utf8mb4"
    		COLLATE "utf8mb4_general_ci";
    		
CREATE TABLE `account` (
	`account_id` INT(11) NOT NULL AUTO_INCREMENT,
	`account_holder` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`account_balance` DOUBLE NOT NULL DEFAULT '0',
	`account_number` VARCHAR(10) NOT NULL COLLATE 'utf8mb4_general_ci',
	`account_passcode` VARCHAR(6) NOT NULL COLLATE 'utf8mb4_general_ci',
	`is_admin` TINYINT(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (`account_id`) USING BTREE,
	UNIQUE INDEX `account_number` (`account_number`) USING BTREE,
	CONSTRAINT `acc_number_passcode_length` CHECK (octet_length(`account_number`) = 10 and octet_length(`account_passcode`) = 6)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5
;
CREATE TABLE `transaction` (
	`trans_id` INT(11) NOT NULL AUTO_INCREMENT,
	`trans_date` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
	`trans_amount` DOUBLE NOT NULL,
	`trans_type` VARCHAR(10) NOT NULL COLLATE 'utf8mb4_general_ci',
	`from_account_number` INT(11) NOT NULL,
	`to_account_number` INT(11) NOT NULL,
	`trans_status` VARCHAR(10) NOT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`trans_id`) USING BTREE,
	INDEX `from_account_id` (`from_account_number`) USING BTREE,
	INDEX `to_account_id` (`to_account_number`) USING BTREE,
	CONSTRAINT `FK_transaction_account` FOREIGN KEY (`from_account_number`) REFERENCES `account` (`account_id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK_transaction_account_2` FOREIGN KEY (`to_account_number`) REFERENCES `account` (`account_id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB