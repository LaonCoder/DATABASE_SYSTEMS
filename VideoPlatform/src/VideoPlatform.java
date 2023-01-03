import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;


public class VideoPlatform {
	
	private static Scanner scanner;
	private static Connection con;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	private static boolean isDBConnected = false;
	private static boolean isDBDisconnected = false;

	
	public static void main(String[] args) {
		
		try {
			// ��ĳ�� ����
			scanner = new Scanner(System.in);
			
			// DB ����
			connectDB();
			isDBConnected = true;
			
			// ���α׷� ����
			runProgram();
			
			// DB ���� ����
			disconnectDB();
			isDBDisconnected = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// DB�� ����Ǿ����� �������� ���� ���¿��� ���α׷��� ����� ���
			if (isDBConnected && !isDBDisconnected) {
				disconnectDB();
			}
			scanner.close();
		}
	}
	
	
	private static void connectDB() {
		
		String dbName;
		String url;
		String userName;
		String userPassword;
		boolean isConnected = false;
		
		do {
			try {
				// DB �̸�, ����� ID, ��� ��ȣ �Է�
				System.out.println("[DB ����]");
				System.out.println("------------------------------------------------------");
				
				do {
					System.out.print("(����� DB�� �̸��� �Է��ϼ���) >>> ");
					dbName = scanner.nextLine().trim();
				} while (dbName.length() == 0);
				
				url = "jdbc:mysql://localhost:3306/" + dbName;
				
				do {
					System.out.print("(DB ����� �̸��� �Է��ϼ���) >>> ");
					userName = scanner.nextLine().trim();
				} while (userName.length() == 0);
				
				do {
					System.out.print("(DB ����� ��й�ȣ�� �Է��ϼ���) >>> ");
					userPassword = scanner.nextLine().trim();
				} while (userPassword.length() == 0);
				
		        try {
		            Class.forName("com.mysql.jdbc.Driver");
		        } catch (ClassNotFoundException e) {
		            System.out.println("\n(!) JDBC ����̹��� �ε��ϴµ� �����߽��ϴ�.\n");
		            e.printStackTrace();
		            System.exit(1);
		        }
				
				// DBMS Connection ����
				con = DriverManager.getConnection(url, userName, userPassword);
				
				System.out.println("\n[DB ���� �Ϸ�]");
				isConnected = true;
			
			} catch (SQLException e) {
				System.out.println("\n(!) DB���� ������ �����߽��ϴ�. �ٽ� �õ����ּ���.\n\n");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (!isConnected);
	}
	

	private static void disconnectDB() {
		try {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (con != null) con.close();
			System.out.println("[DB ���� ����]");
			
		} catch (SQLException e) {
			System.out.println("\n(!) ���������� DB���� ������ �����ϴµ� �����߽��ϴ�.\n");
		}
	}
	
	
	private static void runProgram() {
		
		String menu;
		
		System.out.println("[���α׷� ����]");
		
		while (true) {
			System.out.println("\n\n========================< Video Platform >========================");
			System.out.println(" (1) �� �� ��");
			System.out.println(" (2) ȸ�� ����");
			System.out.println(" (3) ���α׷� ����");
			System.out.println("==================================================================");
			
			do {
				System.out.print("(�޴��� �����ϼ���) >>> ");
				menu = scanner.nextLine().trim();
			} while (menu.length() == 0);
			
			switch (menu) {
				case "1":
					login();
					break;
				case "2":
					signUp();
					break;
				case "3":
					System.out.println("\n[���α׷� ����]");
					return;
				default:
					System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
			}
		}
	}
	
	
	private static void signUp() {
		
		String email;
		String password;
		String name;
		int age;
		String sex;
		
		String sql;
		String choice;
		String manager_code;
		boolean isManager;

		try {
			System.out.println("\n\n--------------------------< Sign Up >--------------------------");
			
			// (1) �Ŵ���, ����� ����
			while (true) {
				System.out.print("(�Ŵ��� ������ '1'��, ����� ������ '2'�� �Է��ϼ���) >>> ");
				choice = scanner.nextLine().trim();
				
				if (!(choice.equals("1") || choice.equals("2")) || choice.length() == 0) continue;
				isManager = choice.equals("1");
				break;
			}
			
			// (2) �Ŵ��� �ĺ� �ڵ� Ȯ�� (�Ŵ����� ��쿡��)
			if (isManager) {
				
				do {
					System.out.print("(�Ŵ��� �ĺ� �ڵ带 �Է��ϼ���) >>> ");
					manager_code = scanner.nextLine().trim();
				} while (manager_code.length() == 0);
				
				
				// �Ŵ��� �ĺ� �ڵ�('ITE2038')�� ��ġ���� �ʴ� ��� ���� ȭ������ ���ư���.
				if (!manager_code.equals("ITE2038")) {
					System.out.println("\n(!) �ĺ� �ڵ尡 ��ġ���� �ʽ��ϴ�. ȸ�� ������ �ٽ� �������ּ���.");
					return;
				}
			}
			
			// (3) �̸��� �Է�
			while (true) {
				do {
					System.out.print("(����� �̸����� �Է��ϼ���) >>> ");
					email = scanner.nextLine().trim();
				} while (email.length() == 0);
				
				// �̸��� ����� �߸��� ���
				if (!email.matches("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$")) {
					System.out.println("\n(!) �̸��� ����� �߸��Ǿ����ϴ�. �ٽ� �Է����ּ���.\n");
					continue;
				} else if (email.length() > 100) {
					System.out.println("\n(!) �̸����� 100�ڸ� �ʰ��� �� �����ϴ�.\n");
					continue;
				}
				
				// �Ŵ����� ��� manager����, ������� ��� users���� �ߺ� �̸����� �˻��Ѵ�.
				sql = String.format("SELECT EXISTS (SELECT * FROM %s WHERE email = ? limit 1);", (isManager) ? "manager" : "users");
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, email);
				rs = pstmt.executeQuery();
				rs.next();
				
				if (rs.getInt(1) == 1) {
					System.out.println("\n(!) �̹� ���Ե� �̸����Դϴ�. �ٸ� �̸��Ϸ� �õ����ּ���.\n");
					continue;
				} else 
					break;
			}
			
			// (4) ��й�ȣ �Է�
			while (true) {
				do {
					System.out.print("(����� ��й�ȣ�� �Է��ϼ���) >>> ");
					password = scanner.nextLine().trim();
				} while (password.length() == 0);
								
				// ��й�ȣ ����� �߸��� ���
				if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")) {
					System.out.println("\n(!) ��й�ȣ�� ������ Ư������, ���ڸ� ������ 8�� �̻� 20�� �̸��̾�� �մϴ�.\n");
					continue;
				} else if (password.length() > 100) {
					System.out.println("\n(!) ��й�ȣ�� 100�ڸ� �ʰ��� �� �����ϴ�.\n");
					continue;
				}
				break;
			}
			
			// (5) �̸� �Է�
			while (true) {
				do {
					System.out.print("(�̸��� �Է��ϼ���) >>> ");
					name = scanner.nextLine().trim();
				} while (name.length() == 0);
				
				if (name.length() > 200) {
					System.out.println("\n(!) �̸��� 200�ڸ� �ʰ��� �� �����ϴ�.");
					continue;
				}
				break;
			}
			
			if (isManager) {
				sql = "INSERT INTO manager(email, manager_password, manager_name) VALUES(?, ?, ?);";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, email);
				pstmt.setString(2, password);
				pstmt.setString(3, name);
				pstmt.executeUpdate();
				
				System.out.println("---------------------------------------------------------------");
				System.out.println("\n[ȸ������ �Ϸ�]");
				return;
			}
			
			// (6) ���� �Է� (������� ��쿡��)
			while (true) {
				while (true) {
					try {
						System.out.print("(���̸� �Է��ϼ���) >>> ");
						age = scanner.nextInt();
						scanner.nextLine();
					} catch (InputMismatchException e) {
						scanner.nextLine();
						System.out.println("\n(!) ���̴� ���� ���� �Է��� �� �ֽ��ϴ�.\n");
						continue;
					}
					break;
				}
				
				if (age < 0 || age > 150) {
					System.out.println("\n(!) ���̸� ��Ȯ�ϰ� �Է����ּ���.\n");
					continue;
				}
				break;
			}
			
			// (7) ���� �Է� (������� ��쿡��)
			while (true) {
				do {
					System.out.print("(������ ��� '1'��, ������ ��� '2'�� �Է��ϼ���) >>> ");
					choice = scanner.nextLine().trim();
				} while (choice.length() == 0);

				switch (choice) {
					case "1": 
						sex = "����"; 
						break;
					case "2": 
						sex = "����";
						break;
					default:
						continue;
				}
				break;
			}
			
			sql = "INSERT INTO users(email, user_password, user_name, age, sex) VALUES(?, ?, ?, ?, ?);";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.setString(3, name);
			pstmt.setInt(4, age);
			pstmt.setString(5, sex);
			pstmt.executeUpdate();
			System.out.println("---------------------------------------------------------------");
			System.out.println("\n[ȸ������ �Ϸ�]");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void login() {
		
		String email;
		String password;
		String name;
		int age;
		String sex;
		
		String sql;
		String choice;
		String manager_code;
		String continueLogin;
		boolean isManager;
		
		try {
			System.out.println("\n\n--------------------------< Sign In >--------------------------");
			
			// (1) �Ŵ���, ����� ����
			while (true) {
				do {
					System.out.print("(�Ŵ��� �α����� '1'��, ����� �α����� '2'�� �Է��ϼ���) >>> ");
					choice = scanner.nextLine().trim();
				} while (choice.length() == 0);
				
				if (!(choice.equals("1") || choice.equals("2")) || choice.length() == 0) continue;
				isManager = choice.equals("1");
				break;
			}
			
			// (2) �Ŵ��� �ĺ� �ڵ� Ȯ�� (�Ŵ����� ��쿡��)
			if (isManager) {
				
				do {
					System.out.print("(�Ŵ��� �ĺ� �ڵ带 �Է��ϼ���) >>> ");
					manager_code = scanner.nextLine().trim();
				} while (manager_code.length() == 0);
				
				
				// �Ŵ��� �ĺ� �ڵ�('ITE2038')�� ��ġ���� �ʴ� ��� ���� ȭ������ ���ư���.
				if (!manager_code.equals("ITE2038")) {
					System.out.println("\n(!) �ĺ� �ڵ尡 ��ġ���� �ʽ��ϴ�. �α����� �ٽ� �������ּ���.");
					return;
				}
			}
			
			while (true) {
				// (3) �̸��� �Է�
				do {
					System.out.print("(�̸����� �Է��ϼ���) >>> ");
					email = scanner.nextLine().trim();
				} while (email.length() == 0);
				
				if (email.length() > 100) {
					System.out.println("\n(!) �̸����� 100�ڸ� �ʰ��� �� �����ϴ�.\n");
					continue;
				}
				
				// (4) ��й�ȣ �Է�
				while (true) {
					do {
						System.out.print("(��й�ȣ�� �Է��ϼ���) >>> ");
						password = scanner.nextLine().trim();
					} while (password.length() == 0);
					
					if (password.length() > 100) {
						System.out.println("\n(!) ��й�ȣ�� 100�ڸ� �ʰ��� �� �����ϴ�.\n");
						continue;
					}
					break;
				}
				
				// (5) �Ŵ��� or ����� ���� ���� Ȯ��
				sql = String.format("SELECT * FROM %s WHERE email = ? AND %s = ?;", 
						(isManager) ? "manager" : "users",
						(isManager) ? "manager_password" : "user_password");
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, email);
				pstmt.setString(2, password);
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					if (isManager) {
						email = rs.getString(1);
						password = rs.getString(2);
						name = rs.getString(3);
						
						System.out.println("---------------------------------------------------------------");
						System.out.println("\n[�α��� �Ϸ�]\n");
						
						Manager manager = new Manager(email, password, name);
						runManagerSubProgram(manager);  // �Ŵ��� ���� ���α׷� ����
					} else {
						email = rs.getString(1);
						password = rs.getString(2);
						name = rs.getString(3);
						age = rs.getInt(4);
						sex = rs.getString(5);
						
						System.out.println("---------------------------------------------------------------");
						System.out.println("\n[�α��� �Ϸ�]\n");

						User user = new User(email, password, name, age, sex);
						runUserSubProgram(user);  // ����� ���� ���α׷� ����
					}
					return;
				} else {
					System.out.println("\n(!) ��ϵ��� ���� �̸����̰ų�, �̸��� �Ǵ� ��й�ȣ�� �߸� �Է��ϼ̽��ϴ�.");
					
					while (true) {
						do {
							System.out.print("    �ٽ� �α��� �Ͻðڽ��ϱ�? (Y/N) >>> ");
							continueLogin = scanner.nextLine().trim();
						} while (continueLogin.length() == 0);
						
						continueLogin = continueLogin.toUpperCase();
						
						if (continueLogin.equals("Y")) {
							System.out.println();
							break;
						} else if (continueLogin.equals("N")) {
							return;
						} else {
							continue;
						}
					}	
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void runManagerSubProgram(Manager manager) {
		
		String menu;
		
		while (true) {
			System.out.println("\n========================< Video Platform >========================");
			System.out.println(String.format("%64s", "������ : " + manager.getManagerName() + " (manager)"));
			System.out.println(" (1) �˻� �� ��û");
			System.out.println(" (2) ����� ��û ��� ����");
			System.out.println(" (3) ����� �Ű� ������ ����");
			System.out.println(" (4) �α׾ƿ�");
			System.out.println("==================================================================");
			
			do {
				System.out.print("(�޴��� �����ϼ���) >>> ");
				menu = scanner.nextLine().trim();
			} while (menu.length() == 0);
			
			switch (menu) {
				case "1":
					searchAndWatch(manager);
					break;
				case "2":
					manageUserViewingHistories(manager);
					break;
				case "3":
					manageUserReportedVideos(manager);
					break;
				case "4":
					System.out.println("\n[�α׾ƿ�]");
					return;
				default:
					System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.\n");
			}
		}
	}
	
	
	private static void searchAndWatch(Manager manager) {
		
		String video_id;
		String title;

		String keyword;
		String sql;
		String command;
		String menu;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int page;
		int rowCount;
		boolean isLastPage;

		try {
			while (true) {
				System.out.println("\n\n-------------------------< �˻� �� ��û >-------------------------");
				System.out.println(" (1) ��  ��");
				System.out.println(" (2) ��  û");
				System.out.println(" (3) ���ư���");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine().trim();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[��  ��]\n");
						do {
							System.out.print("(������ ����, ID, �׸� �� ����� �̸��� �˻��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
						
						page = 1;
						rowCount = 0;
						isLastPage = false;
		
						do {
							sql = "SELECT DISTINCT title, user_name, email, views, uploaded_time, video.video_id "
									+ "FROM video, users, video_categories "
									+ "WHERE users.email = video.uploader_email AND video.video_id = video_categories.video_id AND (user_name = ? OR title = ? OR video.video_id = ? OR category = ?) "
									+ "ORDER BY views "
									+ "DESC LIMIT ?, 10;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, keyword);
							pstmt.setString(2, keyword);
							pstmt.setString(3, keyword);
							pstmt.setString(4, keyword);
							pstmt.setInt(5, 10 * (page - 1));
							rs = pstmt.executeQuery();

					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) ����� �������� �����ϴ�.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | %s (%s) | ��ȸ�� : %dȸ | ���ε� �ð� : %s | ID : %s", 
										rs.getString("title"), 
										rs.getString("user_name"),
										rs.getString("email"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   ��� ����Ͻðڽ��ϱ�? (Y/N) >>> ");
									command = scanner.nextLine().trim();
								} while (command.length() == 0);
								
								command = command.toUpperCase();
								
								switch (command) {
									case "Y":
										page++;
										break;
									case "N":
										isLastPage = true;
										break;
									default:
										continue;
								}
								break;
							}
						} while (!isLastPage);
						
						// �ʱ�ȭ
						isLastPage = false;
						page = 0;
						
						break;
					case "2":
						System.out.println("\n[��  û]\n");
						
						do {
							System.out.print("(��û�� �������� ���� �Ǵ� ID�� �Է��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
						
						sql = "SELECT * FROM video WHERE video_id = ? OR title = ?;"; 
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, keyword);
						pstmt.setString(2,  keyword);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        	title = rs.getString("title");
				        } else {
				        	System.out.println();
				        	
				        	// (2) ������ ID �Է�
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ���ε� �ð� : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(��û�� �������� ID�� �Է��ϼ���) >>> ");
								video_id = scanner.nextLine();
							} while (video_id.length() == 0);
							
							sql = "SELECT * FROM video WHERE video_id = ?;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, video_id);
							rs = pstmt.executeQuery();
							
					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
					        	break;
					        }
					        
					        rs.next();
					        title = rs.getString("title");
				        }
						
				        // �Ŵ����� ��û ����� ���� �ʴ´�.
						System.out.println(String.format("\n[\'%s\' ��û �Ϸ�]", title));
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void manageUserViewingHistories(Manager manager) {
		
		String email;
		
		String sql;
		String command;
		String menu;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int page;
		int rowCount;
		boolean isLastPage;
		
		try {
			while (true) {
				System.out.println("\n\n----------------------< ����� ��û ��� ���� >----------------------");
				System.out.println(" (1) ��û ��� Ȯ��");;
				System.out.println(" (2) ���ư���");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine().trim();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[��û ��� Ȯ��]\n");
						
						do {
							System.out.print("(��û ����� Ȯ���� ������� �̸����� �Է��ϼ���) >>> ");
							email = scanner.nextLine();
						} while (email.length() == 0);
						
						sql = "SELECT EXISTS (SELECT * FROM users WHERE email = ? LIMIT 1);";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, email);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 0) {
				        	System.out.println("\n(!) �������� �ʴ� ������Դϴ�.");
				        	break;
						}
						
						page = 1;
						rowCount = 0;
						isLastPage = false;
		
						do {
							sql = "SELECT * "
									+ "FROM users, watch, video "
									+ "WHERE users.email = ? AND users.email = watch.email AND watch.video_id = video.video_id "
									+ "ORDER BY last_viewed_time "
									+ "DESC LIMIT ?, 10;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, email);
							pstmt.setInt(2, 10 * (page - 1));
							rs = pstmt.executeQuery();
	
					        rs.last();    
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) ����� ��û ����� �����ϴ�.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ������ ��û �ð� : %s",
										rs.getString("title"), 
										rs.getString("video_id"), 
										sdf.format(rs.getTimestamp("last_viewed_time"))));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   ��� ����Ͻðڽ��ϱ�? (Y/N) >>> ");
									command = scanner.nextLine().trim();
								} while (command.length() == 0);
								
								command = command.toUpperCase();
								
								switch (command) {
									case "Y":
										page++;
										break;
									case "N":
										isLastPage = true;
										break;
									default:
										continue;
								}
								break;
							}
						} while (!isLastPage);
						
						isLastPage = false;
						page = 0;
						
						break;
					case "2":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void manageUserReportedVideos(Manager manager) {
		
		String video_id;
		
		String menu;
		String command;
		String sql;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int page;
		int rowCount;
		boolean isLastPage;
		
		try {
			while (true) {
				System.out.println("\n\n---------------------< ���ε�� ������ ���� >---------------------");
				System.out.println(" (1) �Ű� ��� Ȯ��");
				System.out.println(" (2) ���ε�� ������ ����");
				System.out.println(" (3) ���ư���");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[�Ű� ��� Ȯ��]");
						
						page = 1;
						rowCount = 0;
						isLastPage = false;
		
						do {
							sql = "SELECT * FROM report ORDER BY reported_time DESC LIMIT ?, 10;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setInt(1, 10 * (page - 1));
							rs = pstmt.executeQuery();

					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) ����� �Ű� ������ �����ϴ�.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - ID : %s | �Ű��� : %s | �Ű� ���� : %s | �Ű� �ð� : %s", 
										rs.getString("video_id"), 
										rs.getString("email"), 
										rs.getString("reason"),
										sdf.format(rs.getTimestamp("reported_time"))));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   ��� ����Ͻðڽ��ϱ�? (Y/N) >>> ");
									command = scanner.nextLine().trim();
								} while (command.length() == 0);
								
								command = command.toUpperCase();
								
								switch (command) {
									case "Y":
										page++;
										break;
									case "N":
										isLastPage = true;
										break;
									default:
										continue;
								}
								break;
							}
						} while (!isLastPage);
						
						isLastPage = false;
						page = 0;
						
						break;
					case "2":
						System.out.println("\n[���ε�� ������ ����]\n");
						
						do {
							System.out.print("(������ �������� ID�� �Է��ϼ���) >>> ");
							video_id = scanner.nextLine().trim();
						} while (video_id.length() == 0);
						
						sql = "SELECT * FROM video WHERE video_id = ?;"; 
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, video_id);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
				        	break;
				        } else {
							sql = "DELETE FROM video WHERE video_id = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, video_id);
							pstmt.executeUpdate();
							
				        	System.out.println("\n[������ ���� �Ϸ�]");
				        }
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void runUserSubProgram(User user) {
		
		String menu;

		while (true) {			
			System.out.println("\n========================< Video Platform >========================");
			System.out.println(String.format("%64s", "������ : " + user.getUserName() + " (user)"));
			System.out.println(" (1) �˻� �� ��û");
			System.out.println(" (2) �� �÷��̸���Ʈ");
			System.out.println(" (3) �� ���� ���");
			System.out.println(" (4) �� ��û ���");
			System.out.println(" (5) �� ������ ����");
			System.out.println(" (6) �α׾ƿ�");
			System.out.println("==================================================================");
			
			do {
				System.out.print("(�޴��� �Է��ϼ���) >>> ");
				menu = scanner.nextLine();
			} while (menu.length() == 0);
			
			switch (menu) {
				case "1":
					searchAndWatch(user);
					break;
				case "2":
					myPlayLists(user);
					break;
				case "3":
					mySubscriptions(user);
					break;
				case "4":
					myWatchList(user);
					break;
				case "5":
					myVideos(user);
					break;
				case "6":
					System.out.println("\n[�α׾ƿ�]");
					return;
				default:
					System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.\n");
			}
		}
	}
	
	
	private static void myVideos(User user) {
		
		String title;
		String video_id;
		String uploader_email;
		Timestamp uploaded_time;
		int views;
		
		String menu;
		String command;
		String sql;
		String category;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int rowCount;
		
		// �ʱ�ȭ �ʿ�
		int page;
		boolean isCategoryEmpty = true;
		boolean isLastCategory = false;
		boolean isLastPage = false;
		HashSet<String> categories = new HashSet<String>();
		
		try {
			while (true) {
				System.out.println("\n\n------------------------< �� ������ ���� >------------------------");
				System.out.println(" (1) ������ ���ε�");
				System.out.println(" (2) ���ε� ��� Ȯ��");
				System.out.println(" (3) ���ε��� ������ ����");
				System.out.println(" (4) ���ε��� ������ ���");
				System.out.println(" (5) ���ư���");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[������ ���ε�]\n");
						
						// (1) title �Է�
						while (true) {
							do {
								System.out.print("(�������� ������ �Է��ϼ���) >>> ");
								title = scanner.nextLine().trim();
							} while (title.length() == 0);
							
							
							if (title.length() > 100) {
								System.out.println("\n(!) ������ 100�ڸ� �ʰ��� �� �����ϴ�.\n");
								continue;
							}
							break;
						}
						
						// (2) video_id / views / uploader_email / uploaded_time ����
						video_id = getRandomId();
						views = 0;
						uploader_email = user.getEmail();
						uploaded_time = new Timestamp(System.currentTimeMillis());
						
						sql = "INSERT INTO video(video_id, title, views, uploader_email, uploaded_time) VALUES(?, ?, ?, ?, ?);";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, video_id);
						pstmt.setString(2, title);
						pstmt.setInt(3, views);
						pstmt.setString(4, uploader_email);
						pstmt.setTimestamp(5, uploaded_time);
						pstmt.executeUpdate();
						
						// (3) ī�װ� �Է�
						System.out.println("\n| ī�װ� ���� Ȯ�� : \'#CATEGORY\' / ī�װ� �Է� ���� : \'#END\' |");
						do {
							do {
								System.out.print("(ī�װ��� �Է��ϼ���) >>> ");
								category = scanner.nextLine().trim().toUpperCase();
							} while (category.length() == 0);
							
							switch (category) {
								case "#CATEGORY":
									System.out.println("\n[ī�װ� ����]");
									System.out.println("  - #AUTOS/VEHICLES       : �ڵ���");
									System.out.println("  - #BEAUTY/FASHION       : ��Ƽ/�м�");
									System.out.println("  - #COMEDY               : �ڹ̵�");
									System.out.println("  - #EDUCATION            : ����");
									System.out.println("  - #ENTERTAINMENT        : �������θ�Ʈ");
									System.out.println("  - #FAMILY               : ����");
									System.out.println("  - #FILM/ANIMATION       : ��ȭ/�ִϸ��̼�");
									System.out.println("  - #FOOD                 : ����");
									System.out.println("  - #GAME                 : ����");
									System.out.println("  - #HOWTO/STYLE          : ���Ͽ�/��Ÿ��");
									System.out.println("  - #MUSIC                : ����");
									System.out.println("  - #NEWS/POLITICS        : ����/��ġ");
									System.out.println("  - #NON-PROFITS/ACTIVISM : �񿵸�/��ȸ�");
									System.out.println("  - #PEOPLE/BLOGS         : �ι�/��α�");
									System.out.println("  - #PETS/ANIMALS         : �ֿϵ���/����");
									System.out.println("  - #SCIENCE/TECHNOLOGY   : ����/���");
									System.out.println("  - #SPORTS               : ������");
									System.out.println("  - #TRAVEL/EVENTS        : ����/�̺�Ʈ\n");
									break;
								case "#END":
									if (isCategoryEmpty) {
										categories.add("#NONE");
									}
									isLastCategory = true;
									break;
								case "#AUTOS/VEHICLES":
								case "#BEAUTY/FASHION":
								case "#COMEDY":
								case "#EDUCATION":
								case "#ENTERTAINMENT":
								case "#FAMILY":
								case "#FILM/ANIMATION":
								case "#FOOD":
								case "#GAME":
								case "#HOWTO/STYLE":
								case "#MUSIC":
								case "#NEWS/POLITICS":
								case "#NON-PROFITS/ACTIVISM":
								case "#PEOPLE/BLOGS":
								case "#PETS/ANIMALS":
								case "#SCIENCE/TECHNOLOGY":
								case "#SPORTS":
								case "#TRAVEL/EVENTS":
									categories.add(category);
									isCategoryEmpty = false;
									break;
								default:
									System.out.println("\n(!) �߸��� ī�װ��Դϴ�. �ٽ� �Է����ּ���.\n");
							}
						} while (!isLastCategory);
						
						Iterator<String> iter = categories.iterator();
						while (iter.hasNext()) {
							sql = "INSERT INTO video_categories(video_id, category) VALUES(?, ?);";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, video_id);
							pstmt.setString(2, iter.next());
							pstmt.executeUpdate();
						}
						
						// �ʱ�ȭ
						isCategoryEmpty = true;
						isLastCategory = false;
						categories.clear();
						
						System.out.println("\n[������ ���ε� �Ϸ�]");
						break;
					case "2":
						System.out.println("\n[���ε� ��� Ȯ��]");
						
						page = 1;
						rowCount = 0;
						isLastPage = false;
		
						do {
							sql = "SELECT * FROM users, video WHERE email = ? AND email = uploader_email ORDER BY uploaded_time DESC LIMIT ?, 10;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, user.getEmail());
							pstmt.setInt(2, 10 * (page - 1));
							rs = pstmt.executeQuery();

					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) ����� �������� �����ϴ�.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | ��ȸ�� : %d | ���ε� �ð� : %s | ID : %s", 
										rs.getString("title"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   ��� ����Ͻðڽ��ϱ�? (Y/N) >>> ");
									command = scanner.nextLine().trim();
								} while (command.length() == 0);
								
								command = command.toUpperCase();
								
								switch (command) {
									case "Y":
										page++;
										break;
									case "N":
										isLastPage = true;
										break;
									default:
										continue;
								}
								break;
							}
						} while (!isLastPage);
						
						isLastPage = false;
						page = 0;
						
						break;
					case "3":
						System.out.println("\n[���ε��� ������ ����]\n");
						
						// (1) title �Է�
						while (true) {
							do {
								System.out.print("(�������� ������ �Է��ϼ���) >>> ");
								title = scanner.nextLine().trim();
							} while (title.length() == 0);
							
							if (title.length() > 100) {
								System.out.println("\n(!) ������ 100�ڸ� �ʰ��� �� �����ϴ�.\n");
								continue;
							}
							break;
						}
						
						sql = "SELECT * FROM video WHERE title = ? AND uploader_email = ?;";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, title);
						pstmt.setString(2, user.getEmail());
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
				        } else if (rowCount == 1) {
				        	sql = "DELETE FROM video WHERE title = ? AND uploader_email = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, title);
							pstmt.setString(2, user.getEmail());
							pstmt.executeUpdate();
							
							System.out.println("\n[������ ���� �Ϸ�]");
				        } else {
				        	System.out.println();
				        	
				        	// (2) ������ ID �Է�
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ���ε� �ð� : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							System.out.print("\n(������ �������� ID�� �Է��ϼ���) >>> ");
							video_id = scanner.nextLine();
							
							sql = "SELECT * FROM video WHERE video_id = ? AND uploader_email = ?;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, video_id);
							pstmt.setString(2, user.getEmail());
							rs = pstmt.executeQuery();
							
					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
					        } else {
								sql = "DELETE FROM video WHERE video_id = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, video_id);
								pstmt.executeUpdate();
								
					        	System.out.println("\n[������ ���� �Ϸ�]");
					        }
				        }
				        break;
					case "4":
						System.out.println("\n[���ε��� ������ ���]\n");
						
						// (1) ������ ���� | �ִ� ��ȸ�� ���� | ��� ��ȸ�� retrieve 
						sql = "SELECT COUNT(*), SUM(views), AVG(views) FROM video WHERE uploader_email = ?;";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
							
						if (!rs.next()) {
							System.out.println("(!) ���� ���ε��� �������� �����ϴ�.");
							break;
						}
				        
				        System.out.println(" 1. ������ ����  : " + rs.getInt("COUNT(*)") + "ȸ");
				        System.out.println(" 2. ��ȸ�� �հ�  : " + rs.getInt("SUM(views)") + "ȸ");
				        System.out.println(" 3. ��� ��ȸ��  : " + Math.round(rs.getDouble("AVG(views)") * 10) / 10.0 + "ȸ");
				        
				        
				        // (2) �ֿ� ��û�� (��� ���� / ����)
				        sql = "SELECT AVG(users.age) FROM users, watch, video WHERE users.email = watch.email AND watch.video_id = video.video_id AND video.Uploader_email = ?;";
				        pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getString("AVG(users.age)") == null){
							System.out.println(" 4. �ֿ� ��û��  : - ");
						}
						else {
							System.out.print(" 4. �ֿ� ��û��  : " + Math.round(rs.getDouble("AVG(users.age)") * 10) / 10.0 + "�� / ");
							
							sql = "SELECT sex, COUNT(*) "
									+ "FROM users, watch, video "
									+ "WHERE users.email = watch.email AND watch.video_id = video.video_id AND video.Uploader_email = ? "
									+ "GROUP BY sex "
									+ "ORDER BY COUNT(*) DESC;";
					        pstmt = con.prepareStatement(sql);
							pstmt.setString(1, user.getEmail());
							rs = pstmt.executeQuery();
							rs.next();
							
							System.out.println(rs.getString("sex"));
						}
						
						// (3) �� ī�װ�
						sql = "SELECT category, COUNT(*) AS cnt "
								+ "FROM video, video_categories "
								+ "WHERE video.video_id = video_categories.video_id AND video.uploader_email = ? "
								+ "GROUP BY category "
								+ "ORDER BY cnt DESC;";
				        pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						
						if (!rs.next() || rs.getString("category") == null){
							System.out.println(" 5. �� ī�װ�  : - ");
						} else {
							if (rs.getString("category").equals("#NONE")) {
								if (!rs.next()) {
									System.out.println(" 5. �� ī�װ�  : - ");
								} else {
									System.out.println(" 5. �� ī�װ�  : " + rs.getString("category"));
								}
							} else {
								System.out.println(" 5. �� ī�װ�  : " + rs.getString("category"));
							}					
						}
						break;
					case "5":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void searchAndWatch(User user) {
		
		String video_id;
		String title;
		String publisher_email;
		String playlist_name;
		String reason;

		String menu;
		String keyword;
		String sql;
		String command;
		Timestamp last_viewed_time;
		Timestamp added_time;
		Timestamp reported_time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int page;
		
		int rowCount;
		boolean isLastPage;
		
		try {
			while (true) {
				System.out.println("\n\n-------------------------< �˻� �� ��û >-------------------------");
				System.out.println(" [�ֱ� ���ε�� ������ #5]");
				
				sql = "SELECT * FROM users, video WHERE email = uploader_email ORDER BY uploaded_time DESC LIMIT 5;";
		        pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while(rs.next()) {
					System.out.println(String.format("   - %s | %s | %dȸ | %s", rs.getString("title"), rs.getString("user_name"), rs.getInt("views"), sdf.format(rs.getTimestamp("uploaded_time"))));
				}
				
				System.out.println("\n [�α� ������ #5]");
				
				sql = "SELECT * FROM users, video WHERE email = uploader_email ORDER BY views DESC LIMIT 5;";
		        pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while(rs.next()) {
					System.out.println(String.format("   - %s | %s | %dȸ | %s", rs.getString("title"), rs.getString("user_name"), rs.getInt("views"), sdf.format(rs.getTimestamp("uploaded_time"))));
				}
				
				System.out.println("\n [�� ��]");
				System.out.println("  (1) ��  ��");
				System.out.println("  (2) ��  û");
				System.out.println("  (3) �� �÷��̸���Ʈ�� �߰�");
				System.out.println("  (4) �� ���� ����Ʈ�� �߰�");
				System.out.println("  (5) �������� ������ �Ű�");
				System.out.println("  (6) ���ư���");
				System.out.println("--------------------------------------------------------------");

				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[��  ��]\n");
						
						do {
							System.out.print("(������ ����, ID, �׸� �� ����� �̸��� �˻��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
							
						page = 1;
						rowCount = 0;
						isLastPage = false;
		
						do {
							sql = "SELECT DISTINCT title, user_name, email, views, uploaded_time, video.video_id "
									+ "FROM video, users, video_categories "
									+ "WHERE users.email = video.uploader_email AND video.video_id = video_categories.video_id AND (user_name = ? OR title = ? OR video.video_id = ? OR category = ?) "
									+ "ORDER BY views DESC "
									+ "LIMIT ?, 10;"; 
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, keyword);
							pstmt.setString(2, keyword);
							pstmt.setString(3, keyword);
							pstmt.setString(4, keyword);
							pstmt.setInt(5, 10 * (page - 1));
							rs = pstmt.executeQuery();

					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) ����� �������� �����ϴ�.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | %s (%s) | ��ȸ�� : %dȸ | ���ε� �ð� : %s | ID : %s", 
										rs.getString("title"), 
										rs.getString("user_name"),
										rs.getString("email"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   ��� ����Ͻðڽ��ϱ�? (Y/N) >>> ");
									command = scanner.nextLine().trim();
								} while (command.length() == 0);
								
								command = command.toUpperCase();
								
								switch (command) {
									case "Y":
										page++;
										break;
									case "N":
										isLastPage = true;
										break;
									default:
										continue;
								}
								break;
							}
						} while (!isLastPage);
						
						isLastPage = false;
						page = 0;
						
						break;
					case "2":
						System.out.println("\n[��  û]\n");
						
						do {
							System.out.print("(��û�� �������� ���� �Ǵ� ID�� �Է��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
						
						sql = "SELECT * FROM video WHERE video_id = ? OR title = ?;"; 
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, keyword);
						pstmt.setString(2,  keyword);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        	title = rs.getString("title");
				        } else {
				        	System.out.println();
				        	
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ���ε� �ð� : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							System.out.print("\n(��û�� �������� ID�� �Է��ϼ���) >>> ");
							video_id = scanner.nextLine();
							
							sql = "SELECT * FROM video WHERE video_id = ?;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, video_id);
							rs = pstmt.executeQuery();
							
					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
					        	break;
					        }
					        
					        rs.next();
					        title = rs.getString("title");
				        }
				        
						last_viewed_time = new Timestamp(System.currentTimeMillis());
						
						// ��ȸ�� 1 ����
						sql = "UPDATE video SET views = views + 1 WHERE video_id = ?;";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, video_id);
						pstmt.executeUpdate();
						
						// ��û ��Ͽ� �߰�
						sql = "INSERT INTO watch(email, video_id, last_viewed_time) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE last_viewed_time = ?;";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, video_id);
						pstmt.setTimestamp(3, last_viewed_time);
						pstmt.setTimestamp(4, last_viewed_time);
						pstmt.executeUpdate();
						
						System.out.println(String.format("\n[\'%s\' ��û �Ϸ�]", title));
						break;
					case "3":
						System.out.println("\n[�� �÷��̸���Ʈ�� �߰�]\n");
						
						do {
							System.out.print("(�������� �߰��� �÷��̸���Ʈ �̸��� �Է��ϼ���) >>> ");
							playlist_name = scanner.nextLine().trim();
						} while (playlist_name.length() == 0);
						
						do {
							System.out.print("(�߰��� ������ �̸� �Ǵ� ID�� �Է��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
						
						sql = "SELECT * FROM video WHERE video_id = ? OR title = ?;"; 
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, keyword);
						pstmt.setString(2,  keyword);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        	title = rs.getString("title");
				        } else {
				        	System.out.println();
				        	
				        	// (2) ������ ID �Է�
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ���ε� �ð� : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(�߰��� �������� ID�� �Է��ϼ���) >>> ");
								video_id = scanner.nextLine();
							} while (video_id.length() == 0);
							
							sql = "SELECT * FROM video WHERE video_id = ?;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, video_id);
							rs = pstmt.executeQuery();
							
					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
					        	break;
					        }
					        
					        rs.next();
					        title = rs.getString("title");
				        }
				        
						added_time = new Timestamp(System.currentTimeMillis());
						
						// ��û ��Ͽ� �����ϴ��� Ȯ�� 
						sql = "SELECT EXISTS (SELECT * FROM add_to_playlist WHERE email = ? AND video_id = ? AND playlist_name = ? limit 1);";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, video_id);
						pstmt.setString(3, playlist_name);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 1) {
							System.out.println("\n(!) �̹� �ش� �÷��̸���Ʈ�� �߰��Ǿ� �ִ� �������Դϴ�.");
						} else {
							// ��û ��Ͽ� �߰�
							sql = "INSERT INTO add_to_playlist(email, video_id, playlist_name, added_time) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE added_time = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, user.getEmail());
							pstmt.setString(2, video_id);
							pstmt.setString(3, playlist_name);
							pstmt.setTimestamp(4, added_time);
							pstmt.setTimestamp(5, added_time);
							pstmt.executeUpdate();
							
							System.out.println(String.format("\n[\'%s\'��(��) \'%s\'�� �߰� �Ϸ�]", title, playlist_name));
						}
						break;
					case "4":
						System.out.println("\n[�� ���� ����Ʈ�� �߰�]\n");
	
						do {
							System.out.print("(������ ������� �̸� �Ǵ� �̸����� �Է��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
						
						sql = "SELECT * FROM users WHERE user_name = ? OR email = ?;";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, keyword);
						pstmt.setString(2, keyword);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� ������Դϴ�.");
				        	break;
				        } else if (rowCount == 1) {
							// ���� ��Ͽ� �����ϴ��� Ȯ�� 
				        	rs.next();
				        	publisher_email = rs.getString("email");
							sql = "SELECT EXISTS (SELECT * FROM subscribe WHERE publisher_email = ? AND subscriber_email = ? LIMIT 1);";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 1) {
								System.out.println("\n(!) �̹� �����Ǿ� �ִ� ������Դϴ�.");
								break;
							} else {
					        	sql = "INSERT INTO subscribe(subscriber_email, publisher_email) VALUES(?, ?);";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, user.getEmail());
								pstmt.setString(2, publisher_email);
								pstmt.executeUpdate();
								
								// publisher�� �����ڼ� 1 ����
								sql = "UPDATE users SET num_of_subscribers = num_of_subscribers + 1 WHERE email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.executeUpdate();
								
								System.out.println("\n[���� �Ϸ�]");
							}
						} else {
				        	System.out.println();
				        	
							while (rs.next()) {
								System.out.println(String.format(" - %s (%s) | ������ �� : %d��", 
										rs.getString("user_name"), 
										rs.getString("email"), 
										rs.getInt("num_of_subscribers")));
							}
							
							do {
								System.out.print("\n(������ ������� �̸����� �Է��ϼ���) >>> ");
								publisher_email = scanner.nextLine();		
							} while (publisher_email.length() == 0);
							
							sql = "SELECT EXISTS (SELECT * FROM subscribe WHERE publisher_email = ? AND subscriber_email = ? LIMIT 1)";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 1) {
					        	System.out.println("\n(!) �̹� �����Ǿ� �ִ� ������Դϴ�.");
					        	break;
							} else {
					        	sql = "INSERT INTO subscribe(subscriber_email, publisher_email) VALUES(?, ?);";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, user.getEmail());
								pstmt.setString(2, publisher_email);
								pstmt.executeUpdate();
								
								// publisher�� �����ڼ� 1 ����
								sql = "UPDATE users SET num_of_subscribers = num_of_subscribers + 1 WHERE email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.executeUpdate();
								
								System.out.println("\n[���� �Ϸ�]");
					        }
				        }
						break;
					case "5":
						System.out.println("\n[�������� ������ �Ű�]\n");
						
						do {
							System.out.print("(�Ű��� �������� ���� �Ǵ� ID�� �Է��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
						
						sql = "SELECT * FROM video WHERE video_id = ? OR title = ?;"; 
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, keyword);
						pstmt.setString(2,  keyword);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        } else {
				        	System.out.println();
				        	
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ���ε� �ð� : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(�Ű��� �������� ID�� �Է��ϼ���) >>> ");
								video_id = scanner.nextLine().trim();
							} while (video_id.length() == 0);

							sql = "SELECT EXISTS (SELECT * FROM video WHERE video_id = ?)";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, video_id);
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 0) {
								System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
					        	break;
							}
				        }
				        
						while (true) {
							do {
								System.out.print("(�Ű� ������ �Է��ϼ��� (�ִ� 200��)) >>> ");
								reason = scanner.nextLine().trim();
							} while (reason.length() == 0);
							
							if (reason.length() > 200) {
								System.out.println("\n(!) �Ű� ������ �ִ� 200�ڱ��� �ۼ��� �� �ֽ��ϴ�.\n");
								continue;
							}
							break;
						}
						
						reported_time = new Timestamp(System.currentTimeMillis());
				        
						sql = "INSERT INTO report(email, video_id, reported_time, reason) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE reason = ? ;";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, video_id);
						pstmt.setTimestamp(3, reported_time);
						pstmt.setString(4, reason);
						pstmt.setString(5, reason);
						pstmt.executeUpdate();
						
						System.out.println("\n[�Ű� �Ϸ�]");
						break;
					case "6":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private static void myPlayLists(User user) {
		
		String playlist;
		String video_id;

		String command;
		String menu;
		String keyword;
		String sql;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int rowCount;
		
		int page;
		boolean isLastPage = false;
		
		try {
			while (true) {
				System.out.println("\n\n------------------------< �� �÷��̸���Ʈ >------------------------");
				System.out.println(" (1) �� �÷��̸���Ʈ Ȯ��");
				System.out.println(" (2) �÷��̸���Ʈ���� ������ ����");
				System.out.println(" (3) ���ư���");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[�� �÷��̸���Ʈ Ȯ��]\n");
						
						sql = "SELECT playlist_name, COUNT(*) FROM add_to_playlist WHERE email = ? GROUP BY playlist_name;";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						
						rowCount = 0;

				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
						
						if (rowCount == 0) {
							System.out.println("(!) ������ �÷��̸���Ʈ�� �����ϴ�.");
							break;
						}
						
						while(rs.next()) {
							System.out.println(String.format(" - %s (%d���� ����)", rs.getString("playlist_name"), rs.getInt("COUNT(*)")));
						}
						
						do {
							System.out.print("\n(�÷��̸���Ʈ�� �̸��� �Է��ϼ���) >>> ");
							playlist = scanner.nextLine().trim();
						} while (playlist.length() == 0);
						
						sql = "SELECT EXISTS (SELECT * FROM add_to_playlist WHERE email = ? AND playlist_name = ? limit 1)";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, playlist);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 0) {
							System.out.println("\n(!) �������� �ʴ� �÷��̸���Ʈ�Դϴ�.");
							break;
						}
						
						page = 1;
						rowCount = 0;
						isLastPage = false;
		
						do {
							sql = "SELECT * "
									+ "FROM users, add_to_playlist, video "
									+ "WHERE users.email = add_to_playlist.email AND add_to_playlist.video_id = video.video_id AND users.email = ? AND playlist_name = ? "
									+ "ORDER BY added_time DESC "
									+ "LIMIT ?, 10;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1,  user.getEmail());
							pstmt.setString(2,  playlist);
							pstmt.setInt(3, 10 * (page - 1));
							rs = pstmt.executeQuery();
	
					        rs.last();    
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("(!) ����� �������� �����ϴ�.\n");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | %s (%s) | ��ȸ�� : %dȸ | ���ε� �ð� : %s | ID : %s", 
										rs.getString("title"), 
										rs.getString("user_name"),
										rs.getString("email"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n��� ����Ͻðڽ��ϱ�? (Y/N) >>> ");
									command = scanner.nextLine().trim();
								} while (command.length() == 0);
								
								command.toUpperCase();
								
								switch (command) {
									case "Y":
										page++;
										break;
									case "N":
										isLastPage = true;
										break;
									default:
										continue;
								}
								break;
							}
						} while (!isLastPage);
						
						isLastPage = false;
						page = 0;
						
						break;
					case "2":
						// �÷��̸���Ʈ���� ����
						System.out.println("\n[�÷��̸���Ʈ���� ������ ����]\n");
						
						do {
							System.out.print("(�÷��̸���Ʈ�� �̸��� �Է��ϼ���) >>> ");
							playlist = scanner.nextLine().trim();
						} while (playlist.length() == 0);	
						
						sql = "SELECT EXISTS (SELECT * FROM add_to_playlist WHERE email = ? AND playlist_name = ? limit 1)";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, playlist);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 0) {
							System.out.println("\n(!) �������� �ʴ� �÷��̸���Ʈ�Դϴ�.");
							break;
						} 
						
						do {
							System.out.print("(������ �������� ���� �Ǵ� ID�� �Է��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);				
						
						sql = "SELECT * "
								+ "FROM video, add_to_playlist "
								+ "WHERE video.video_id = add_to_playlist.video_id AND email = ? AND playlist_name = ? AND (add_to_playlist.video_id = ? OR title = ?);";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, playlist);
						pstmt.setString(3, keyword);
						pstmt.setString(4, keyword);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        	sql = "DELETE FROM add_to_playlist WHERE email = ? AND playlist_name = ? AND video_id = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, user.getEmail());
							pstmt.setString(2, playlist);
							pstmt.setString(3, video_id);
							pstmt.executeUpdate();
							
							System.out.println("\n[�÷��̸���Ʈ���� ������ ���� �Ϸ�]");
				        } else {
				        	System.out.println();
				        	
				        	// (2) ������ ID �Է�
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ���ε� �ð� : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(������ �������� ID�� �Է��ϼ���) >>> ");
								video_id = scanner.nextLine();
							} while (video_id.length() == 0);
							
							sql = "SELECT EXISTS (SELECT * FROM add_to_playlist WHERE email = ? AND playlist_name = ? AND video_id = ? limit 1)";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, user.getEmail());
							pstmt.setString(2, playlist);
							pstmt.setString(3, video_id);
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 0) {
								System.out.println("\n(!) �������� �ʴ� �������Դϴ�.");
								break;
							} else {
								sql = "DELETE FROM add_to_playlist WHERE email = ? AND playlist_name = ? AND video_id = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, user.getEmail());
								pstmt.setString(2, playlist);
								pstmt.setString(3, video_id);
								pstmt.executeUpdate();
								
								System.out.println("\n[�÷��̸���Ʈ���� ������ ���� �Ϸ�]");
					        }
				        }
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void mySubscriptions(User user) {
		
		String publisher_email;
		
		String keyword;
		String menu;
		String sql;

		int rowCount;

		try {
			while (true) {
				System.out.println("\n\n------------------------< �� ���� ����Ʈ >------------------------");
				System.out.println(" (1) ���� ����Ʈ Ȯ��");
				System.out.println(" (2) ���� ���");
				System.out.println(" (3) ���ư���");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[���� ����Ʈ Ȯ��]\n");
						
						sql = "SELECT * FROM users, subscribe WHERE subscriber_email = ? AND publisher_email = users.email;";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("(!) ���� ����Ʈ�� ��� �ֽ��ϴ�.");
				        	break;
				        } else {
							while(rs.next()) {
								System.out.println(String.format(" - %s (%s) | ������ �� : %d��", 
										rs.getString("user_name"), 
										rs.getString("email"), 
										rs.getInt("num_of_subscribers")));
							}
			        	}
						break;
					case "2":
						// ���� ����Ʈ���� ����
						System.out.println("\n[���� ���]\n");
						
						do {
							System.out.print("(���� ����� ������� �̸� �Ǵ� �̸����� �Է��ϼ���) >>> ");
							keyword = scanner.nextLine().trim();
						} while (keyword.length() == 0);
						
						sql = "SELECT * "
								+ "FROM users, subscribe "
								+ "WHERE publisher_email = users.email AND subscriber_email = ? AND (publisher_email = ? OR user_name = ?);";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, keyword);
						pstmt.setString(3, keyword);
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("\n(!) �������� �ʰų�, ���� �������� ���� ������Դϴ�.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	publisher_email = rs.getString("publisher_email");
				        	sql = "DELETE FROM subscribe WHERE publisher_email = ? AND subscriber_email = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							pstmt.executeUpdate();
							
							// publisher�� �����ڼ� 1 ����
							sql = "UPDATE users SET num_of_subscribers = num_of_subscribers - 1 WHERE email = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.executeUpdate();
							
							System.out.println("\n[���� ��� �Ϸ�]");
				        } else {
				        	System.out.println();
				        	
				        	// ���� ����� ����� �̸��� �Է�
							while (rs.next()) {
								System.out.println(String.format(" - %s (%s) | ������ �� : %d��", 
										rs.getString("user_name"), 
										rs.getString("email"), 
										rs.getInt("num_of_subscribers")));
							}
							
							do {
								System.out.print("\n(���� ����� ������� �̸����� �Է��ϼ���) >>> ");
								publisher_email = scanner.nextLine();
							} while (publisher_email.length() == 0);
							
							sql = "SELECT EXISTS (SELECT * FROM subscribe WHERE publisher_email = ? AND subscriber_email = ? LIMIT 1)";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 0) {
					        	System.out.println("\n(!) �������� �ʰų�, ���� �������� ���� ������Դϴ�.");
					        	break;
							} else {
					        	sql = "DELETE FROM subscribe WHERE publisher_email = ? AND subscriber_email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.setString(2, user.getEmail());
								pstmt.executeUpdate();
								
								// publisher�� �����ڼ� 1 ����
								sql = "UPDATE users SET num_of_subscribers = num_of_subscribers - 1 WHERE email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.executeUpdate();
								
								System.out.println("\n[���� ��� �Ϸ�]");
					        }
				        }
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void myWatchList(User user) {
		
		String menu;
		String command;
		String sql;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int rowCount;

		int page;
		boolean isLastPage = false;
		
		try {
			while (true) {
				System.out.println("\n\n------------------------< �� ��û ��� >------------------------");
				System.out.println(" (1) ��û ��� Ȯ��");
				System.out.println(" (2) ���ư���");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(�޴��� �����ϼ���) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[��û ��� Ȯ��]");

						page = 1;
						rowCount = 0;
						isLastPage = false;
		
						do {
							sql = "SELECT * FROM users, watch, video WHERE users.email = ? AND users.email = watch.email AND watch.video_id = video.video_id ORDER BY last_viewed_time DESC LIMIT ?, 10;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, user.getEmail());
							pstmt.setInt(2, 10 * (page - 1));
							rs = pstmt.executeQuery();
	
					        rs.last();    
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) ����� ��û ����� �����ϴ�.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | ������ ��û �ð� : %s", rs.getString("title"), rs.getString("video_id"), sdf.format(rs.getTimestamp("last_viewed_time"))));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n��� ����Ͻðڽ��ϱ�? (Y/N) >>> ");
									command = scanner.nextLine().trim();
								} while (command.length() == 0);
								
								command = command.toUpperCase();
								
								switch (command) {
									case "Y":
										page++;
										break;
									case "N":
										isLastPage = true;
										break;
									default:
										continue;
								}
								break;
							}
						} while (!isLastPage);
						
						isLastPage = false;
						page = 0;
				        
						break;
					case "2":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) �޴��� �ٽ� �Է����ּ���.");
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	private static String getRandomId() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 32;
		Random random = new Random();

		String generatedString = random.ints(leftLimit,rightLimit + 1)
		  .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		  .limit(targetStringLength)
		  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  .toString();

		return generatedString;
	}
}