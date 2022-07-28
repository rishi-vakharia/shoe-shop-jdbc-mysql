// https://stackoverflow.com/questions/2168969/how-to-connect-xampp-mysql-local-db-using-jdbc

import java.sql.*;
import java.util.Scanner;

public class ShoeShop {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   	static final String DB_URL = "jdbc:mysql://127.0.0.1/shoeshopdb?allowPublicKeyRetrieval=true&useSSL=false";
   	static final String USER = "rishi";
   	static final String PASS = "rishi";

	public static void mainMenuPrint(){
		System.out.println("1. Login as Owner");
		System.out.println("2. Login as Employee");
		System.out.println("3. Login as Customer");
		System.out.println("0. Exit");
		System.out.println();
	}
   	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Connection conn = null;
		Statement stmt1 = null;
		Statement stmt2 = null;


		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt1 = conn.createStatement();
			stmt2 = conn.createStatement();

			clearScreen();
			mainMenuPrint();
			System.out.print("Your Choice: ");
			Integer choice=tryParseInt(sc.nextLine());

			if(choice==null)
				System.out.println("\nIllegal Input!\n");

			else{
				while(choice!=0){
					switch(choice){
						case 1:
							ownerMenu(stmt1, stmt2, sc);
							break;
						case 2:
							employeeMenu(stmt1, sc);
							break;
						case 3:
							customerMenu(stmt1, sc);
							break;
						default:
							System.out.println("\nInvalid Input!\n");
							break;
					}
					mainMenuPrint();
					System.out.print("Your Choice: ");
					choice = tryParseInt(sc.nextLine());		
					if(choice==null){
						System.out.println("\nIllegal Input!\n");
						break;
					}
				}
				if(choice==null);
				else if(choice==0){
					clearScreen();
					System.out.println("Exited Successfully!\n");	
				}
			}
	
			sc.close();
			stmt1.close();
			stmt2.close();
			conn.close();
	  	}	  
		catch(SQLException se){    	
			se.printStackTrace();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}
		finally{			
			try{
				if(stmt1!=null)
				stmt1.close();
			}     
			catch(SQLException se){
				se.printStackTrace();
			}
			try{
				if(stmt2!=null)
				stmt2.close();
			}     
			catch(SQLException se){
				se.printStackTrace();
			}
			try{
				if(conn!=null)
				conn.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}			
		}				
   	}			

	public static void ownerMenuPrint(){
		System.out.println("1. See All Employees");
		System.out.println("2. See All Customers");
		System.out.println("3. See All Bills");
		System.out.println("4. See All Shoes");
		System.out.println("5. Add Employee");
		System.out.println("6. Remove Employee");
		System.out.println("7. Change Salary");
		System.out.println("0. Exit");
		System.out.println();
	}
	
	public static void ownerMenu(Statement stmt, Statement stmt1, Scanner sc){
		
		clearScreen();
		ownerMenuPrint();
		System.out.print("Your Choice: ");
		Integer choice = tryParseInt(sc.nextLine());

		if(choice==null)
			System.out.println("\nIllegal Input!\n");

		else{
			while(choice!=0){
				switch(choice){
					case 1:
						seeAllEmployees(stmt);
						break;
					case 2:
						seeAllCustomers(stmt);
						break;
					case 3:
						seeAllBills(stmt, stmt1);
						break;
					case 4:
						seeAllShoes(stmt);
						break;
					case 5:
						addEmployee(stmt, sc);
						break;
					case 6:
						removeEmployee(stmt, sc);
						break;
					case 7:
						changeSalary(stmt, sc);
						break;
					default:
						System.out.println("\nInvalid Input!\n");
						break;
				}
				ownerMenuPrint();
				System.out.print("Your Choice: ");
				
				choice = tryParseInt(sc.nextLine());		
				if(choice==null){
					System.out.println("\nIllegal Input!\n");
					break;
				}
			}	
			if(choice==null);
			else if(choice==0){
				clearScreen();
				System.out.println("Exited Successfully!\n");	
			}	
		}			
	}

	public static void seeAllEmployees(Statement stmt){
		try{
			clearScreen();
			System.out.printf("%-12s%-20s%-10s\n", "Emp ID", "Name", "Salary");
			
			String sql= "select * from Employee;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.printf("%-12d%-20s%-10d\n", rs.getInt("emp_id"), rs.getString("emp_name"), rs.getInt("emp_salary"));
			}
			System.out.println();
			rs.close();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void seeAllCustomers(Statement stmt){
		try{
			clearScreen();
			System.out.printf("%-12s%-20s\n", "Cust ID", "Name");
			
			String sql= "select * from Customer;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.printf("%-12d%-20s\n", rs.getInt("cust_id"), rs.getString("cust_name"));
			}
			System.out.println();
			rs.close();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void seeAllBills(Statement stmt, Statement stmt_temp){
		try{
			String sql, sql_temp;
			ResultSet rs, rs_temp;
			int bill_id, cust_id, shoe_id;

			clearScreen();
			System.out.printf("%-12s%-12s%-12s%-20s%-40s%-12s\n", "Bill ID", "Cust ID", "Shoe ID", "Cust Name", "Shoe Name", "Shoe Price");
			
			sql= "select * from Bill;";
			rs = stmt.executeQuery(sql);

			while(rs.next()){
				bill_id = rs.getInt("bill_id");
				cust_id = rs.getInt("cust_id");
				shoe_id = rs.getInt("shoe_id");

				sql_temp = String.format("select cust_name from Customer where cust_id = %d", cust_id);
				rs_temp = stmt_temp.executeQuery(sql_temp);
				rs_temp.first();
				String cust_name = rs_temp.getString("cust_name");
				rs_temp.close();
				
				sql_temp = String.format("select shoe_name, shoe_price from Shoes where shoe_id = %d", shoe_id);
				rs_temp = stmt_temp.executeQuery(sql_temp);
				rs_temp.first();
				String shoe_name = rs_temp.getString("shoe_name");
				int shoe_price = rs_temp.getInt("shoe_price");
				rs_temp.close();

				System.out.printf("%-12d%-12d%-12d%-20s%-40s%-12d\n", bill_id, cust_id, shoe_id, cust_name, shoe_name, shoe_price);
			}
			System.out.println();
			rs.close();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void seeAllShoes(Statement stmt){
		try{
			clearScreen();
			System.out.printf("%-12s%-40s%-10s%-7s%-10s\n", "Shoe ID", "Name", "Price", "Size", "Status");
			
			String sql= "select * from Shoes;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.printf("%-12d%-40s%-10d%-7d%-10s\n", rs.getInt("shoe_id"), rs.getString("shoe_name"), rs.getInt("shoe_price"), rs.getInt("shoe_size"), rs.getString("shoe_status"));
			}
			System.out.println();
			rs.close();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void addEmployee(Statement stmt, Scanner sc){
		try{
			String sql;
			ResultSet rs;

			seeAllEmployees(stmt);

			System.out.print("Name: ");
			String name = sc.nextLine();

			System.out.print("Salary: ");
			int salary = Integer.parseInt(sc.nextLine());

			sql = String.format("insert into Employee(emp_name, emp_salary) values('%s', %d);", name, salary);
			stmt.executeUpdate(sql);

			System.out.printf("\nEmployee Added Successfully:\n%-12s%-20s%-10s\n", "Emp ID", "Name", "Salary");
			
			sql= String.format("select * from Employee where emp_name='%s' and emp_salary=%d;", name, salary);
			rs = stmt.executeQuery(sql);
			rs.last();
			System.out.printf("%-12d%-20s%-10d\n\n", rs.getInt("emp_id"), rs.getString("emp_name"), rs.getInt("emp_salary"));
			rs.close();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static int checkInvalidEmpId(Statement stmt, int emp_id){
		String sql;
		ResultSet rs;
		int dne=1;

		try{
			sql= String.format("select not exists (select * from Employee where emp_id='%d') as dne;", emp_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			dne = rs.getInt("dne");
			rs.close();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}

		if(dne==1)
			return 1;

		return 0;
	}

	public static void removeEmployee(Statement stmt, Scanner sc){
		try{
			String sql, emp_name;
			ResultSet rs;
			int emp_id, emp_salary;

			seeAllEmployees(stmt);

			System.out.print("Emp ID: ");
			emp_id = Integer.parseInt(sc.nextLine());

			while(checkInvalidEmpId(stmt, emp_id)==1){
				System.out.println("\nPlease enter a valid Emp ID...\n");
				System.out.print("Emp ID: ");
				emp_id = Integer.parseInt(sc.nextLine());
			}

			sql= String.format("select * from Employee where emp_id = %d;", emp_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			emp_name = rs.getString("emp_name");
			emp_salary = rs.getInt("emp_salary");
			rs.close();

			sql= String.format("delete from Employee where emp_id = %d;", emp_id);
			stmt.executeUpdate(sql);
			
			System.out.printf("\nEmployee Removed Successfully:\n%-12s%-20s%-10s\n", "Emp ID", "Name", "Salary");
			System.out.printf("%-12d%-20s%-10d\n\n", emp_id, emp_name, emp_salary);
		}

		catch(Exception e){        	
			e.printStackTrace();
		}
	}
	
	public static void changeSalary(Statement stmt, Scanner sc){
		try{
			String sql, emp_name;
			ResultSet rs;
			int emp_id, emp_salary, new_salary;

			seeAllEmployees(stmt);

			System.out.print("Emp ID: ");
			emp_id = Integer.parseInt(sc.nextLine());

			while(checkInvalidEmpId(stmt, emp_id)==1){
				System.out.println("\nPlease enter a valid Emp ID...\n");
				System.out.print("Emp ID: ");
				emp_id = Integer.parseInt(sc.nextLine());
			}

			sql= String.format("select * from Employee where emp_id = %d;", emp_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			emp_name = rs.getString("emp_name");
			emp_salary = rs.getInt("emp_salary");
			rs.close();
			
			System.out.print("New Salary: ");
			new_salary = Integer.parseInt(sc.nextLine());

			sql = String.format("update Employee set emp_salary = %d where emp_id = %d;", new_salary, emp_id);
			stmt.executeUpdate(sql);
			
			System.out.printf("\nSalary Changed Successfully:\n%-12s%-20s%-10s%-15s\n", "Emp ID", "Name", "Salary", "New Salary");
			System.out.printf("%-12d%-20s%-10d%-15d\n\n", emp_id, emp_name, emp_salary, new_salary);
		}

		catch(Exception e){        	
			e.printStackTrace();
		}
	}
	
	public static void employeeMenuPrint(){
		System.out.println("1. See All Shoes");
		System.out.println("2. Add Shoes");
		System.out.println("3. Remove Shoes");
		System.out.println("4. Change Price");
		System.out.println("0. Exit");
		System.out.println();
	}

	public static void employeeMenu(Statement stmt, Scanner sc){
		
		clearScreen();
		employeeMenuPrint();
		System.out.print("Your Choice: ");
		Integer choice = tryParseInt(sc.nextLine());

		if(choice==null)
			System.out.println("\nIllegal Input!\n");
		
		else{
			while(choice!=0){
				switch(choice){
					case 1:
						seeAllUnsoldShoes(stmt);
						break;
					case 2:
						addShoes(stmt, sc);
						break;
					case 3:
						removeShoes(stmt, sc);
						break;
					case 4:
						changePrice(stmt, sc);
						break;
					default:
						System.out.println("\nInvalid Input!\n");
						break;
				}
				employeeMenuPrint();
				System.out.print("Your Choice: ");
				choice = tryParseInt(sc.nextLine());		
				if(choice==null){
					System.out.println("\nIllegal Input!\n");
					break;
				}
			}
			if(choice==null);
			else if(choice==0){
				clearScreen();
				System.out.println("Exited Successfully!\n");	
			}	
		}
	}

	public static void addShoes(Statement stmt, Scanner sc){

		try{
			String sql;
			ResultSet rs;

			seeAllUnsoldShoes(stmt);

			System.out.print("Name: ");
			String name = sc.nextLine();

			System.out.print("Price: ");
			int price = Integer.parseInt(sc.nextLine());

			System.out.print("Size: ");
			int size = Integer.parseInt(sc.nextLine());

			sql = String.format("insert into Shoes(shoe_name, shoe_price, shoe_size, shoe_status) values('%s', %d, %d, '%s');", name, price, size, "UNSOLD");
			stmt.executeUpdate(sql);

			System.out.printf("\nShoes Added Successfully:\n%-12s%-40s%-10s%-7s\n", "Shoe ID", "Name", "Price", "Size");
			
			sql= String.format("select * from Shoes where shoe_name='%s' and shoe_price=%d and shoe_size=%d and shoe_status='UNSOLD';", name, price, size);
			rs = stmt.executeQuery(sql);
			rs.last();
			System.out.printf("%-12d%-40s%-10d%-7d\n\n", rs.getInt("shoe_id"), rs.getString("shoe_name"), rs.getInt("shoe_price"), rs.getInt("shoe_size"));
			rs.close();
		}

		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static int checkInvalidShoeId(Statement stmt, int shoe_id){
		String sql;
		ResultSet rs;
		int dne=1, sold=1;

		try{
			sql= String.format("select not exists (select * from Shoes where shoe_id='%d') as dne;", shoe_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			dne = rs.getInt("dne");
			rs.close();

			sql= String.format("select exists (select * from Shoes where shoe_id='%d' and shoe_status='SOLD') as sold;", shoe_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			sold = rs.getInt("sold");
			rs.close();
		}
		catch(Exception e){        	
			e.printStackTrace();
		}

		if(dne==1 || sold==1)
			return 1;

		return 0;
	}

	public static void removeShoes(Statement stmt, Scanner sc){
		
		try{
			String sql, shoe_name;
			ResultSet rs;
			int shoe_id, shoe_price, shoe_size;

			seeAllUnsoldShoes(stmt);

			System.out.print("Shoe ID: ");
			shoe_id = Integer.parseInt(sc.nextLine());

			while(checkInvalidShoeId(stmt, shoe_id)==1){
				System.out.println("\nPlease enter a valid Shoe ID...\n");
				System.out.print("Shoe ID: ");
				shoe_id = Integer.parseInt(sc.nextLine());
			}

			sql= String.format("select * from Shoes where shoe_id = %d;", shoe_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			shoe_name = rs.getString("shoe_name");
			shoe_price = rs.getInt("shoe_price");
			shoe_size = rs.getInt("shoe_size");
			rs.close();

			sql= String.format("delete from Shoes where shoe_id = %d;", shoe_id);
			stmt.executeUpdate(sql);
			
			System.out.printf("\nShoes Removed Successfully:\n%-12s%-40s%-10s%-7s\n", "Shoe ID", "Name", "Price", "Size");
			System.out.printf("%-12d%-40s%-10d%-7d\n\n", shoe_id, shoe_name, shoe_price, shoe_size);
		}

		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void changePrice(Statement stmt, Scanner sc){
		
		try{
			String sql, shoe_name;
			ResultSet rs;
			int shoe_id, shoe_price, shoe_size, new_price;

			seeAllUnsoldShoes(stmt);

			System.out.print("Shoe ID: ");
			shoe_id = Integer.parseInt(sc.nextLine());

			while(checkInvalidShoeId(stmt, shoe_id)==1){
				System.out.println("\nPlease enter a valid Shoe ID...\n");
				System.out.print("Shoe ID: ");
				shoe_id = Integer.parseInt(sc.nextLine());
			}

			sql= String.format("select * from Shoes where shoe_id = %d;", shoe_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			shoe_name = rs.getString("shoe_name");
			shoe_price = rs.getInt("shoe_price");
			shoe_size = rs.getInt("shoe_size");
			rs.close();
			
			System.out.print("New Price: ");
			new_price = Integer.parseInt(sc.nextLine());

			sql = String.format("update Shoes set shoe_price = %d where shoe_id = %d;", new_price, shoe_id);
			stmt.executeUpdate(sql);
			
			System.out.printf("\nPrice Changed Successfully:\n%-12s%-40s%-10s%-7s%-15s\n", "Shoe ID", "Name", "Price", "Size", "New Price");
			System.out.printf("%-12d%-40s%-10d%-7d%-15d\n\n", shoe_id, shoe_name, shoe_price, shoe_size, new_price);
		}
		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void customerMenuPrint(){
		System.out.println("1. See All Shoes");
		System.out.println("2. Buy Shoes");
		System.out.println("0. Exit");
		System.out.println();
	}
	
	public static void customerMenu(Statement stmt, Scanner sc){

		clearScreen();
		customerMenuPrint();
		System.out.print("Your Choice: ");
		Integer choice = tryParseInt(sc.nextLine());

		if(choice==null)
			System.out.println("\nIllegal Input!\n");

		else{
			while(choice!=0){
				switch(choice){
					case 1:
						seeAllUnsoldShoes(stmt);
						break;
					case 2:
						buyShoes(stmt, sc);
						break;
					default:
						System.out.println("\nInvalid Input!\n");
						break;
				}
				customerMenuPrint();
				System.out.print("Your Choice: ");
				choice = tryParseInt(sc.nextLine());		
				if(choice==null){
					System.out.println("\nIllegal Input!\n");
					break;
				}
			}
			if(choice==null);
			else if(choice==0){
				clearScreen();
				System.out.println("Exited Successfully!\n");
			}
		}
	}

	public static void seeAllUnsoldShoes(Statement stmt){
		
		try{
			clearScreen();
			System.out.printf("%-12s%-40s%-10s%-7s\n", "Shoe ID", "Name", "Price", "Size");
			
			String sql= "select * from Shoes where shoe_status='UNSOLD';";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.printf("%-12d%-40s%-10d%-7d\n", rs.getInt("shoe_id"), rs.getString("shoe_name"), rs.getInt("shoe_price"), rs.getInt("shoe_size"));
			}
			System.out.println();
			rs.close();
		}

		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void seeBill(Statement stmt, int cust_id, int shoe_id){
		try{

			String sql, cust_name, shoe_name;
			ResultSet rs;
			int bill_id, shoe_price;

			System.out.printf("\nYour Bill:\n%-12s%-12s%-12s%-20s%-40s%-12s\n", "Bill ID", "Cust ID", "Shoe ID", "Cust Name", "Shoe Name", "Shoe Price");
			
			sql= String.format("select * from Bill where cust_id = %d and shoe_id = %d;", cust_id, shoe_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			bill_id = rs.getInt("bill_id");
			rs.close();
	
			sql = String.format("select cust_name from Customer where cust_id = %d", cust_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			cust_name = rs.getString("cust_name");
			rs.close();
				
			sql = String.format("select shoe_name, shoe_price from Shoes where shoe_id = %d", shoe_id);
			rs = stmt.executeQuery(sql);
			rs.first();
			shoe_name = rs.getString("shoe_name");
			shoe_price = rs.getInt("shoe_price");
			rs.close();

			System.out.printf("%-12d%-12d%-12d%-20s%-40s%-12d\n\n", bill_id, cust_id, shoe_id, cust_name, shoe_name, shoe_price);
					
		}

		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void buyShoes(Statement stmt, Scanner sc){
		try{

			String cust_name;
			String sql;
			ResultSet rs;
			int flag, shoe_id, cust_id;

			seeAllUnsoldShoes(stmt);

			System.out.print("Shoe ID: ");
			shoe_id = Integer.parseInt(sc.nextLine());

			while(checkInvalidShoeId(stmt, shoe_id)==1){
				System.out.println("\nSorry, the given Shoe ID is not available. Try again\n");
				System.out.print("Shoe ID: ");
				shoe_id = Integer.parseInt(sc.nextLine());
			}

			System.out.print("Customer Name: ");	
			cust_name = sc.nextLine();

			sql= String.format("select exists (select * from Customer where cust_name='%s') as flag;", cust_name);
			rs = stmt.executeQuery(sql);
			rs.first();
			flag = rs.getInt("flag");
			rs.close();

			while(flag==1){
				System.out.println("\nSorry, the given Customer Name already exists! Enter another Customer Name\n");
				System.out.print("Customer Name: ");	
				cust_name = sc.nextLine();

				sql= String.format("select exists (select * from Customer where cust_name='%s') as flag;", cust_name);
				rs = stmt.executeQuery(sql);
				rs.first();
				flag = rs.getInt("flag");
				rs.close();
			}
			
			sql = String.format("insert into Customer(cust_name) values('%s');", cust_name);
			stmt.executeUpdate(sql);

			sql= String.format("select cust_id from Customer where cust_name = '%s';", cust_name);
			rs = stmt.executeQuery(sql);
			rs.first();
			cust_id = rs.getInt("cust_id");
			rs.close();

			sql = String.format("insert into Bill(cust_id, shoe_id) values(%d, %d);", cust_id, shoe_id);
			stmt.executeUpdate(sql);

			sql= String.format("update Shoes set shoe_status = 'SOLD' where shoe_id = %d;", shoe_id);
			stmt.executeUpdate(sql);
			
			seeBill(stmt, cust_id, shoe_id);
		}

		catch(Exception e){        	
			e.printStackTrace();
		}
	}

	public static void clearScreen() {  
		System.out.print("\033[H\033[2J");  
		System.out.flush();  
	}  

	public static Integer tryParseInt(String text) {
		try {
			return Integer.parseInt(text);
		} 
		catch (NumberFormatException e) {
			return null;
		}
	}
}					