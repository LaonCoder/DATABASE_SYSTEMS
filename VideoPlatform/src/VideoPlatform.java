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
			// 스캐너 설정
			scanner = new Scanner(System.in);
			
			// DB 설정
			connectDB();
			isDBConnected = true;
			
			// 프로그램 실행
			runProgram();
			
			// DB 연결 해제
			disconnectDB();
			isDBDisconnected = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// DB가 연결되었지만 해제되지 않은 상태에서 프로그램이 종료된 경우
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
				// DB 이름, 사용자 ID, 비밀 번호 입력
				System.out.println("[DB 설정]");
				System.out.println("------------------------------------------------------");
				
				do {
					System.out.print("(사용할 DB의 이름을 입력하세요) >>> ");
					dbName = scanner.nextLine().trim();
				} while (dbName.length() == 0);
				
				url = "jdbc:mysql://localhost:3306/" + dbName;
				
				do {
					System.out.print("(DB 사용자 이름을 입력하세요) >>> ");
					userName = scanner.nextLine().trim();
				} while (userName.length() == 0);
				
				do {
					System.out.print("(DB 사용자 비밀번호를 입력하세요) >>> ");
					userPassword = scanner.nextLine().trim();
				} while (userPassword.length() == 0);
				
		        try {
		            Class.forName("com.mysql.jdbc.Driver");
		        } catch (ClassNotFoundException e) {
		            System.out.println("\n(!) JDBC 드라이버를 로딩하는데 실패했습니다.\n");
		            e.printStackTrace();
		            System.exit(1);
		        }
				
				// DBMS Connection 생성
				con = DriverManager.getConnection(url, userName, userPassword);
				
				System.out.println("\n[DB 연결 완료]");
				isConnected = true;
			
			} catch (SQLException e) {
				System.out.println("\n(!) DB와의 연결을 실패했습니다. 다시 시도해주세요.\n\n");
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
			System.out.println("[DB 연결 종료]");
			
		} catch (SQLException e) {
			System.out.println("\n(!) 정상적으로 DB와의 연결을 해제하는데 실패했습니다.\n");
		}
	}
	
	
	private static void runProgram() {
		
		String menu;
		
		System.out.println("[프로그램 실행]");
		
		while (true) {
			System.out.println("\n\n========================< Video Platform >========================");
			System.out.println(" (1) 로 그 인");
			System.out.println(" (2) 회원 가입");
			System.out.println(" (3) 프로그램 종료");
			System.out.println("==================================================================");
			
			do {
				System.out.print("(메뉴를 선택하세요) >>> ");
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
					System.out.println("\n[프로그램 종료]");
					return;
				default:
					System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
			
			// (1) 매니저, 사용자 구분
			while (true) {
				System.out.print("(매니저 가입은 '1'을, 사용자 가입은 '2'를 입력하세요) >>> ");
				choice = scanner.nextLine().trim();
				
				if (!(choice.equals("1") || choice.equals("2")) || choice.length() == 0) continue;
				isManager = choice.equals("1");
				break;
			}
			
			// (2) 매니저 식별 코드 확인 (매니저인 경우에만)
			if (isManager) {
				
				do {
					System.out.print("(매니저 식별 코드를 입력하세요) >>> ");
					manager_code = scanner.nextLine().trim();
				} while (manager_code.length() == 0);
				
				
				// 매니저 식별 코드('ITE2038')가 일치하지 않는 경우 메인 화면으로 돌아간다.
				if (!manager_code.equals("ITE2038")) {
					System.out.println("\n(!) 식별 코드가 일치하지 않습니다. 회원 가입을 다시 진행해주세요.");
					return;
				}
			}
			
			// (3) 이메일 입력
			while (true) {
				do {
					System.out.print("(사용할 이메일을 입력하세요) >>> ");
					email = scanner.nextLine().trim();
				} while (email.length() == 0);
				
				// 이메일 양식이 잘못된 경우
				if (!email.matches("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$")) {
					System.out.println("\n(!) 이메일 양식이 잘못되었습니다. 다시 입력해주세요.\n");
					continue;
				} else if (email.length() > 100) {
					System.out.println("\n(!) 이메일은 100자를 초과할 수 없습니다.\n");
					continue;
				}
				
				// 매니저인 경우 manager에서, 사용자인 경우 users에서 중복 이메일을 검사한다.
				sql = String.format("SELECT EXISTS (SELECT * FROM %s WHERE email = ? limit 1);", (isManager) ? "manager" : "users");
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, email);
				rs = pstmt.executeQuery();
				rs.next();
				
				if (rs.getInt(1) == 1) {
					System.out.println("\n(!) 이미 가입된 이메일입니다. 다른 이메일로 시도해주세요.\n");
					continue;
				} else 
					break;
			}
			
			// (4) 비밀번호 입력
			while (true) {
				do {
					System.out.print("(사용할 비밀번호를 입력하세요) >>> ");
					password = scanner.nextLine().trim();
				} while (password.length() == 0);
								
				// 비밀번호 양식이 잘못된 경우
				if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")) {
					System.out.println("\n(!) 비밀번호는 영문과 특수문자, 숫자를 포함한 8자 이상 20자 미만이어야 합니다.\n");
					continue;
				} else if (password.length() > 100) {
					System.out.println("\n(!) 비밀번호는 100자를 초과할 수 없습니다.\n");
					continue;
				}
				break;
			}
			
			// (5) 이름 입력
			while (true) {
				do {
					System.out.print("(이름을 입력하세요) >>> ");
					name = scanner.nextLine().trim();
				} while (name.length() == 0);
				
				if (name.length() > 200) {
					System.out.println("\n(!) 이름은 200자를 초과할 수 없습니다.");
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
				System.out.println("\n[회원가입 완료]");
				return;
			}
			
			// (6) 나이 입력 (사용자인 경우에만)
			while (true) {
				while (true) {
					try {
						System.out.print("(나이를 입력하세요) >>> ");
						age = scanner.nextInt();
						scanner.nextLine();
					} catch (InputMismatchException e) {
						scanner.nextLine();
						System.out.println("\n(!) 나이는 정수 값만 입력할 수 있습니다.\n");
						continue;
					}
					break;
				}
				
				if (age < 0 || age > 150) {
					System.out.println("\n(!) 나이를 정확하게 입력해주세요.\n");
					continue;
				}
				break;
			}
			
			// (7) 성별 입력 (사용자인 경우에만)
			while (true) {
				do {
					System.out.print("(남성인 경우 '1'을, 여성인 경우 '2'를 입력하세요) >>> ");
					choice = scanner.nextLine().trim();
				} while (choice.length() == 0);

				switch (choice) {
					case "1": 
						sex = "남성"; 
						break;
					case "2": 
						sex = "여성";
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
			System.out.println("\n[회원가입 완료]");
			
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
			
			// (1) 매니저, 사용자 구분
			while (true) {
				do {
					System.out.print("(매니저 로그인은 '1'을, 사용자 로그인은 '2'를 입력하세요) >>> ");
					choice = scanner.nextLine().trim();
				} while (choice.length() == 0);
				
				if (!(choice.equals("1") || choice.equals("2")) || choice.length() == 0) continue;
				isManager = choice.equals("1");
				break;
			}
			
			// (2) 매니저 식별 코드 확인 (매니저인 경우에만)
			if (isManager) {
				
				do {
					System.out.print("(매니저 식별 코드를 입력하세요) >>> ");
					manager_code = scanner.nextLine().trim();
				} while (manager_code.length() == 0);
				
				
				// 매니저 식별 코드('ITE2038')가 일치하지 않는 경우 메인 화면으로 돌아간다.
				if (!manager_code.equals("ITE2038")) {
					System.out.println("\n(!) 식별 코드가 일치하지 않습니다. 로그인을 다시 진행해주세요.");
					return;
				}
			}
			
			while (true) {
				// (3) 이메일 입력
				do {
					System.out.print("(이메일을 입력하세요) >>> ");
					email = scanner.nextLine().trim();
				} while (email.length() == 0);
				
				if (email.length() > 100) {
					System.out.println("\n(!) 이메일은 100자를 초과할 수 없습니다.\n");
					continue;
				}
				
				// (4) 비밀번호 입력
				while (true) {
					do {
						System.out.print("(비밀번호를 입력하세요) >>> ");
						password = scanner.nextLine().trim();
					} while (password.length() == 0);
					
					if (password.length() > 100) {
						System.out.println("\n(!) 비밀번호는 100자를 초과할 수 없습니다.\n");
						continue;
					}
					break;
				}
				
				// (5) 매니저 or 사용자 가입 정보 확인
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
						System.out.println("\n[로그인 완료]\n");
						
						Manager manager = new Manager(email, password, name);
						runManagerSubProgram(manager);  // 매니저 전용 프로그램 실행
					} else {
						email = rs.getString(1);
						password = rs.getString(2);
						name = rs.getString(3);
						age = rs.getInt(4);
						sex = rs.getString(5);
						
						System.out.println("---------------------------------------------------------------");
						System.out.println("\n[로그인 완료]\n");

						User user = new User(email, password, name, age, sex);
						runUserSubProgram(user);  // 사용자 전용 프로그램 실행
					}
					return;
				} else {
					System.out.println("\n(!) 등록되지 않은 이메일이거나, 이메일 또는 비밀번호를 잘못 입력하셨습니다.");
					
					while (true) {
						do {
							System.out.print("    다시 로그인 하시겠습니까? (Y/N) >>> ");
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
			System.out.println(String.format("%64s", "접속자 : " + manager.getManagerName() + " (manager)"));
			System.out.println(" (1) 검색 및 시청");
			System.out.println(" (2) 사용자 시청 기록 관리");
			System.out.println(" (3) 사용자 신고 동영상 관리");
			System.out.println(" (4) 로그아웃");
			System.out.println("==================================================================");
			
			do {
				System.out.print("(메뉴를 선택하세요) >>> ");
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
					System.out.println("\n[로그아웃]");
					return;
				default:
					System.out.println("\n(!) 메뉴를 다시 입력해주세요.\n");
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
				System.out.println("\n\n-------------------------< 검색 및 시청 >-------------------------");
				System.out.println(" (1) 검  색");
				System.out.println(" (2) 시  청");
				System.out.println(" (3) 돌아가기");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine().trim();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[검  색]\n");
						do {
							System.out.print("(동영상 제목, ID, 테마 및 사용자 이름을 검색하세요) >>> ");
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
					        	System.out.println("\n(!) 출력할 동영상이 없습니다.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | %s (%s) | 조회수 : %d회 | 업로드 시간 : %s | ID : %s", 
										rs.getString("title"), 
										rs.getString("user_name"),
										rs.getString("email"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   계속 출력하시겠습니까? (Y/N) >>> ");
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
						
						// 초기화
						isLastPage = false;
						page = 0;
						
						break;
					case "2":
						System.out.println("\n[시  청]\n");
						
						do {
							System.out.print("(시청할 동영상의 제목 또는 ID를 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        	title = rs.getString("title");
				        } else {
				        	System.out.println();
				        	
				        	// (2) 동영상 ID 입력
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 업로드 시간 : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(시청할 동영상의 ID를 입력하세요) >>> ");
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
					        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
					        	break;
					        }
					        
					        rs.next();
					        title = rs.getString("title");
				        }
						
				        // 매니저는 시청 기록이 남지 않는다.
						System.out.println(String.format("\n[\'%s\' 시청 완료]", title));
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
				System.out.println("\n\n----------------------< 사용자 시청 기록 관리 >----------------------");
				System.out.println(" (1) 시청 기록 확인");;
				System.out.println(" (2) 돌아가기");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine().trim();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[시청 기록 확인]\n");
						
						do {
							System.out.print("(시청 기록을 확인할 사용자의 이메일을 입력하세요) >>> ");
							email = scanner.nextLine();
						} while (email.length() == 0);
						
						sql = "SELECT EXISTS (SELECT * FROM users WHERE email = ? LIMIT 1);";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, email);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 0) {
				        	System.out.println("\n(!) 존재하지 않는 사용자입니다.");
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
					        	System.out.println("\n(!) 출력할 시청 기록이 없습니다.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 마지막 시청 시간 : %s",
										rs.getString("title"), 
										rs.getString("video_id"), 
										sdf.format(rs.getTimestamp("last_viewed_time"))));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   계속 출력하시겠습니까? (Y/N) >>> ");
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
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
				System.out.println("\n\n---------------------< 업로드된 동영상 관리 >---------------------");
				System.out.println(" (1) 신고 목록 확인");
				System.out.println(" (2) 업로드된 동영상 삭제");
				System.out.println(" (3) 돌아가기");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[신고 목록 확인]");
						
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
					        	System.out.println("\n(!) 출력할 신고 내역이 없습니다.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - ID : %s | 신고자 : %s | 신고 사유 : %s | 신고 시간 : %s", 
										rs.getString("video_id"), 
										rs.getString("email"), 
										rs.getString("reason"),
										sdf.format(rs.getTimestamp("reported_time"))));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   계속 출력하시겠습니까? (Y/N) >>> ");
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
						System.out.println("\n[업로드된 동영상 삭제]\n");
						
						do {
							System.out.print("(삭제할 동영상의 ID를 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
				        	break;
				        } else {
							sql = "DELETE FROM video WHERE video_id = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, video_id);
							pstmt.executeUpdate();
							
				        	System.out.println("\n[동영상 삭제 완료]");
				        }
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
			System.out.println(String.format("%64s", "접속자 : " + user.getUserName() + " (user)"));
			System.out.println(" (1) 검색 및 시청");
			System.out.println(" (2) 내 플레이리스트");
			System.out.println(" (3) 내 구독 목록");
			System.out.println(" (4) 내 시청 목록");
			System.out.println(" (5) 내 동영상 관리");
			System.out.println(" (6) 로그아웃");
			System.out.println("==================================================================");
			
			do {
				System.out.print("(메뉴를 입력하세요) >>> ");
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
					System.out.println("\n[로그아웃]");
					return;
				default:
					System.out.println("\n(!) 메뉴를 다시 입력해주세요.\n");
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
		
		// 초기화 필요
		int page;
		boolean isCategoryEmpty = true;
		boolean isLastCategory = false;
		boolean isLastPage = false;
		HashSet<String> categories = new HashSet<String>();
		
		try {
			while (true) {
				System.out.println("\n\n------------------------< 내 동영상 관리 >------------------------");
				System.out.println(" (1) 동영상 업로드");
				System.out.println(" (2) 업로드 목록 확인");
				System.out.println(" (3) 업로드한 동영상 삭제");
				System.out.println(" (4) 업로드한 동영상 통계");
				System.out.println(" (5) 돌아가기");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[동영상 업로드]\n");
						
						// (1) title 입력
						while (true) {
							do {
								System.out.print("(동영상의 제목을 입력하세요) >>> ");
								title = scanner.nextLine().trim();
							} while (title.length() == 0);
							
							
							if (title.length() > 100) {
								System.out.println("\n(!) 제목은 100자를 초과할 수 없습니다.\n");
								continue;
							}
							break;
						}
						
						// (2) video_id / views / uploader_email / uploaded_time 설정
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
						
						// (3) 카테고리 입력
						System.out.println("\n| 카테고리 종류 확인 : \'#CATEGORY\' / 카테고리 입력 종료 : \'#END\' |");
						do {
							do {
								System.out.print("(카테고리를 입력하세요) >>> ");
								category = scanner.nextLine().trim().toUpperCase();
							} while (category.length() == 0);
							
							switch (category) {
								case "#CATEGORY":
									System.out.println("\n[카테고리 종류]");
									System.out.println("  - #AUTOS/VEHICLES       : 자동차");
									System.out.println("  - #BEAUTY/FASHION       : 뷰티/패션");
									System.out.println("  - #COMEDY               : 코미디");
									System.out.println("  - #EDUCATION            : 교육");
									System.out.println("  - #ENTERTAINMENT        : 엔터테인먼트");
									System.out.println("  - #FAMILY               : 가족");
									System.out.println("  - #FILM/ANIMATION       : 영화/애니메이션");
									System.out.println("  - #FOOD                 : 음식");
									System.out.println("  - #GAME                 : 게임");
									System.out.println("  - #HOWTO/STYLE          : 노하우/스타일");
									System.out.println("  - #MUSIC                : 음악");
									System.out.println("  - #NEWS/POLITICS        : 뉴스/정치");
									System.out.println("  - #NON-PROFITS/ACTIVISM : 비영리/사회운동");
									System.out.println("  - #PEOPLE/BLOGS         : 인물/블로그");
									System.out.println("  - #PETS/ANIMALS         : 애완동물/동물");
									System.out.println("  - #SCIENCE/TECHNOLOGY   : 과학/기술");
									System.out.println("  - #SPORTS               : 스포츠");
									System.out.println("  - #TRAVEL/EVENTS        : 여행/이벤트\n");
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
									System.out.println("\n(!) 잘못된 카테고리입니다. 다시 입력해주세요.\n");
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
						
						// 초기화
						isCategoryEmpty = true;
						isLastCategory = false;
						categories.clear();
						
						System.out.println("\n[동영상 업로드 완료]");
						break;
					case "2":
						System.out.println("\n[업로드 목록 확인]");
						
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
					        	System.out.println("\n(!) 출력할 동영상이 없습니다.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | 조회수 : %d | 업로드 시간 : %s | ID : %s", 
										rs.getString("title"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   계속 출력하시겠습니까? (Y/N) >>> ");
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
						System.out.println("\n[업로드한 동영상 삭제]\n");
						
						// (1) title 입력
						while (true) {
							do {
								System.out.print("(동영상의 제목을 입력하세요) >>> ");
								title = scanner.nextLine().trim();
							} while (title.length() == 0);
							
							if (title.length() > 100) {
								System.out.println("\n(!) 제목은 100자를 초과할 수 없습니다.\n");
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
				        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
				        } else if (rowCount == 1) {
				        	sql = "DELETE FROM video WHERE title = ? AND uploader_email = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, title);
							pstmt.setString(2, user.getEmail());
							pstmt.executeUpdate();
							
							System.out.println("\n[동영상 삭제 완료]");
				        } else {
				        	System.out.println();
				        	
				        	// (2) 동영상 ID 입력
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 업로드 시간 : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							System.out.print("\n(삭제할 동영상의 ID를 입력하세요) >>> ");
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
					        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
					        } else {
								sql = "DELETE FROM video WHERE video_id = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, video_id);
								pstmt.executeUpdate();
								
					        	System.out.println("\n[동영상 삭제 완료]");
					        }
				        }
				        break;
					case "4":
						System.out.println("\n[업로드한 동영상 통계]\n");
						
						// (1) 동영상 개수 | 최대 조회수 영상 | 평균 조회수 retrieve 
						sql = "SELECT COUNT(*), SUM(views), AVG(views) FROM video WHERE uploader_email = ?;";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
							
						if (!rs.next()) {
							System.out.println("(!) 아직 업로드한 동영상이 없습니다.");
							break;
						}
				        
				        System.out.println(" 1. 동영상 개수  : " + rs.getInt("COUNT(*)") + "회");
				        System.out.println(" 2. 조회수 합계  : " + rs.getInt("SUM(views)") + "회");
				        System.out.println(" 3. 평균 조회수  : " + Math.round(rs.getDouble("AVG(views)") * 10) / 10.0 + "회");
				        
				        
				        // (2) 주요 시청층 (평균 나이 / 성별)
				        sql = "SELECT AVG(users.age) FROM users, watch, video WHERE users.email = watch.email AND watch.video_id = video.video_id AND video.Uploader_email = ?;";
				        pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getString("AVG(users.age)") == null){
							System.out.println(" 4. 주요 시청층  : - ");
						}
						else {
							System.out.print(" 4. 주요 시청층  : " + Math.round(rs.getDouble("AVG(users.age)") * 10) / 10.0 + "세 / ");
							
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
						
						// (3) 주 카테고리
						sql = "SELECT category, COUNT(*) AS cnt "
								+ "FROM video, video_categories "
								+ "WHERE video.video_id = video_categories.video_id AND video.uploader_email = ? "
								+ "GROUP BY category "
								+ "ORDER BY cnt DESC;";
				        pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						
						if (!rs.next() || rs.getString("category") == null){
							System.out.println(" 5. 주 카테고리  : - ");
						} else {
							if (rs.getString("category").equals("#NONE")) {
								if (!rs.next()) {
									System.out.println(" 5. 주 카테고리  : - ");
								} else {
									System.out.println(" 5. 주 카테고리  : " + rs.getString("category"));
								}
							} else {
								System.out.println(" 5. 주 카테고리  : " + rs.getString("category"));
							}					
						}
						break;
					case "5":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
				System.out.println("\n\n-------------------------< 검색 및 시청 >-------------------------");
				System.out.println(" [최근 업로드된 동영상 #5]");
				
				sql = "SELECT * FROM users, video WHERE email = uploader_email ORDER BY uploaded_time DESC LIMIT 5;";
		        pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while(rs.next()) {
					System.out.println(String.format("   - %s | %s | %d회 | %s", rs.getString("title"), rs.getString("user_name"), rs.getInt("views"), sdf.format(rs.getTimestamp("uploaded_time"))));
				}
				
				System.out.println("\n [인기 동영상 #5]");
				
				sql = "SELECT * FROM users, video WHERE email = uploader_email ORDER BY views DESC LIMIT 5;";
		        pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while(rs.next()) {
					System.out.println(String.format("   - %s | %s | %d회 | %s", rs.getString("title"), rs.getString("user_name"), rs.getInt("views"), sdf.format(rs.getTimestamp("uploaded_time"))));
				}
				
				System.out.println("\n [메 뉴]");
				System.out.println("  (1) 검  색");
				System.out.println("  (2) 시  청");
				System.out.println("  (3) 내 플레이리스트에 추가");
				System.out.println("  (4) 내 구독 리스트에 추가");
				System.out.println("  (5) 부적절한 동영상 신고");
				System.out.println("  (6) 돌아가기");
				System.out.println("--------------------------------------------------------------");

				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":
						System.out.println("\n[검  색]\n");
						
						do {
							System.out.print("(동영상 제목, ID, 테마 및 사용자 이름을 검색하세요) >>> ");
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
					        	System.out.println("\n(!) 출력할 동영상이 없습니다.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | %s (%s) | 조회수 : %d회 | 업로드 시간 : %s | ID : %s", 
										rs.getString("title"), 
										rs.getString("user_name"),
										rs.getString("email"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n   계속 출력하시겠습니까? (Y/N) >>> ");
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
						System.out.println("\n[시  청]\n");
						
						do {
							System.out.print("(시청할 동영상의 제목 또는 ID를 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        	title = rs.getString("title");
				        } else {
				        	System.out.println();
				        	
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 업로드 시간 : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							System.out.print("\n(시청할 동영상의 ID를 입력하세요) >>> ");
							video_id = scanner.nextLine();
							
							sql = "SELECT * FROM video WHERE video_id = ?;";
							pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
							pstmt.setString(1, video_id);
							rs = pstmt.executeQuery();
							
					        rs.last();      
					        rowCount = rs.getRow();
					        rs.beforeFirst();
					        
					        if (rowCount == 0) {
					        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
					        	break;
					        }
					        
					        rs.next();
					        title = rs.getString("title");
				        }
				        
						last_viewed_time = new Timestamp(System.currentTimeMillis());
						
						// 조회수 1 증가
						sql = "UPDATE video SET views = views + 1 WHERE video_id = ?;";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, video_id);
						pstmt.executeUpdate();
						
						// 시청 목록에 추가
						sql = "INSERT INTO watch(email, video_id, last_viewed_time) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE last_viewed_time = ?;";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, video_id);
						pstmt.setTimestamp(3, last_viewed_time);
						pstmt.setTimestamp(4, last_viewed_time);
						pstmt.executeUpdate();
						
						System.out.println(String.format("\n[\'%s\' 시청 완료]", title));
						break;
					case "3":
						System.out.println("\n[내 플레이리스트에 추가]\n");
						
						do {
							System.out.print("(동영상을 추가할 플레이리스트 이름을 입력하세요) >>> ");
							playlist_name = scanner.nextLine().trim();
						} while (playlist_name.length() == 0);
						
						do {
							System.out.print("(추가할 동영상 이름 또는 ID를 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        	title = rs.getString("title");
				        } else {
				        	System.out.println();
				        	
				        	// (2) 동영상 ID 입력
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 업로드 시간 : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(추가할 동영상의 ID를 입력하세요) >>> ");
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
					        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
					        	break;
					        }
					        
					        rs.next();
					        title = rs.getString("title");
				        }
				        
						added_time = new Timestamp(System.currentTimeMillis());
						
						// 시청 목록에 존재하는지 확인 
						sql = "SELECT EXISTS (SELECT * FROM add_to_playlist WHERE email = ? AND video_id = ? AND playlist_name = ? limit 1);";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, video_id);
						pstmt.setString(3, playlist_name);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 1) {
							System.out.println("\n(!) 이미 해당 플레이리스트에 추가되어 있는 동영상입니다.");
						} else {
							// 시청 목록에 추가
							sql = "INSERT INTO add_to_playlist(email, video_id, playlist_name, added_time) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE added_time = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, user.getEmail());
							pstmt.setString(2, video_id);
							pstmt.setString(3, playlist_name);
							pstmt.setTimestamp(4, added_time);
							pstmt.setTimestamp(5, added_time);
							pstmt.executeUpdate();
							
							System.out.println(String.format("\n[\'%s\'을(를) \'%s\'에 추가 완료]", title, playlist_name));
						}
						break;
					case "4":
						System.out.println("\n[내 구독 리스트에 추가]\n");
	
						do {
							System.out.print("(구독할 사용자의 이름 또는 이메일을 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않는 사용자입니다.");
				        	break;
				        } else if (rowCount == 1) {
							// 구독 목록에 존재하는지 확인 
				        	rs.next();
				        	publisher_email = rs.getString("email");
							sql = "SELECT EXISTS (SELECT * FROM subscribe WHERE publisher_email = ? AND subscriber_email = ? LIMIT 1);";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 1) {
								System.out.println("\n(!) 이미 구독되어 있는 사용자입니다.");
								break;
							} else {
					        	sql = "INSERT INTO subscribe(subscriber_email, publisher_email) VALUES(?, ?);";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, user.getEmail());
								pstmt.setString(2, publisher_email);
								pstmt.executeUpdate();
								
								// publisher의 구독자수 1 증가
								sql = "UPDATE users SET num_of_subscribers = num_of_subscribers + 1 WHERE email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.executeUpdate();
								
								System.out.println("\n[구독 완료]");
							}
						} else {
				        	System.out.println();
				        	
							while (rs.next()) {
								System.out.println(String.format(" - %s (%s) | 구독자 수 : %d명", 
										rs.getString("user_name"), 
										rs.getString("email"), 
										rs.getInt("num_of_subscribers")));
							}
							
							do {
								System.out.print("\n(구독할 사용자의 이메일을 입력하세요) >>> ");
								publisher_email = scanner.nextLine();		
							} while (publisher_email.length() == 0);
							
							sql = "SELECT EXISTS (SELECT * FROM subscribe WHERE publisher_email = ? AND subscriber_email = ? LIMIT 1)";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 1) {
					        	System.out.println("\n(!) 이미 구독되어 있는 사용자입니다.");
					        	break;
							} else {
					        	sql = "INSERT INTO subscribe(subscriber_email, publisher_email) VALUES(?, ?);";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, user.getEmail());
								pstmt.setString(2, publisher_email);
								pstmt.executeUpdate();
								
								// publisher의 구독자수 1 증가
								sql = "UPDATE users SET num_of_subscribers = num_of_subscribers + 1 WHERE email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.executeUpdate();
								
								System.out.println("\n[구독 완료]");
					        }
				        }
						break;
					case "5":
						System.out.println("\n[부적절한 동영상 신고]\n");
						
						do {
							System.out.print("(신고할 동영상의 제목 또는 ID를 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	video_id = rs.getString("video_id");
				        } else {
				        	System.out.println();
				        	
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 업로드 시간 : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(신고할 동영상의 ID를 입력하세요) >>> ");
								video_id = scanner.nextLine().trim();
							} while (video_id.length() == 0);

							sql = "SELECT EXISTS (SELECT * FROM video WHERE video_id = ?)";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, video_id);
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 0) {
								System.out.println("\n(!) 존재하지 않는 동영상입니다.");
					        	break;
							}
				        }
				        
						while (true) {
							do {
								System.out.print("(신고 사유를 입력하세요 (최대 200자)) >>> ");
								reason = scanner.nextLine().trim();
							} while (reason.length() == 0);
							
							if (reason.length() > 200) {
								System.out.println("\n(!) 신고 사유는 최대 200자까지 작성할 수 있습니다.\n");
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
						
						System.out.println("\n[신고 완료]");
						break;
					case "6":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
				System.out.println("\n\n------------------------< 내 플레이리스트 >------------------------");
				System.out.println(" (1) 내 플레이리스트 확인");
				System.out.println(" (2) 플레이리스트에서 동영상 삭제");
				System.out.println(" (3) 돌아가기");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[내 플레이리스트 확인]\n");
						
						sql = "SELECT playlist_name, COUNT(*) FROM add_to_playlist WHERE email = ? GROUP BY playlist_name;";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						
						rowCount = 0;

				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
						
						if (rowCount == 0) {
							System.out.println("(!) 생성된 플레이리스트가 없습니다.");
							break;
						}
						
						while(rs.next()) {
							System.out.println(String.format(" - %s (%d개의 영상)", rs.getString("playlist_name"), rs.getInt("COUNT(*)")));
						}
						
						do {
							System.out.print("\n(플레이리스트의 이름을 입력하세요) >>> ");
							playlist = scanner.nextLine().trim();
						} while (playlist.length() == 0);
						
						sql = "SELECT EXISTS (SELECT * FROM add_to_playlist WHERE email = ? AND playlist_name = ? limit 1)";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, playlist);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 0) {
							System.out.println("\n(!) 존재하지 않는 플레이리스트입니다.");
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
					        	System.out.println("(!) 출력할 동영상이 없습니다.\n");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | %s (%s) | 조회수 : %d회 | 업로드 시간 : %s | ID : %s", 
										rs.getString("title"), 
										rs.getString("user_name"),
										rs.getString("email"),
										rs.getInt("views"), 
										sdf.format(rs.getTimestamp("uploaded_time")),
										rs.getString("video_id")));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n계속 출력하시겠습니까? (Y/N) >>> ");
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
						// 플레이리스트에서 삭제
						System.out.println("\n[플레이리스트에서 동영상 삭제]\n");
						
						do {
							System.out.print("(플레이리스트의 이름을 입력하세요) >>> ");
							playlist = scanner.nextLine().trim();
						} while (playlist.length() == 0);	
						
						sql = "SELECT EXISTS (SELECT * FROM add_to_playlist WHERE email = ? AND playlist_name = ? limit 1)";
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getEmail());
						pstmt.setString(2, playlist);
						rs = pstmt.executeQuery();
						rs.next();
						
						if (rs.getInt(1) == 0) {
							System.out.println("\n(!) 존재하지 않는 플레이리스트입니다.");
							break;
						} 
						
						do {
							System.out.print("(삭제할 동영상의 제목 또는 ID를 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않는 동영상입니다.");
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
							
							System.out.println("\n[플레이리스트에서 동영상 삭제 완료]");
				        } else {
				        	System.out.println();
				        	
				        	// (2) 동영상 ID 입력
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 업로드 시간 : %s", 
										rs.getString("title"), 
										rs.getString("video_id"),
										sdf.format(rs.getTimestamp("uploaded_time"))));
							}
							
							do {
								System.out.print("\n(삭제할 동영상의 ID를 입력하세요) >>> ");
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
								System.out.println("\n(!) 존재하지 않는 동영상입니다.");
								break;
							} else {
								sql = "DELETE FROM add_to_playlist WHERE email = ? AND playlist_name = ? AND video_id = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, user.getEmail());
								pstmt.setString(2, playlist);
								pstmt.setString(3, video_id);
								pstmt.executeUpdate();
								
								System.out.println("\n[플레이리스트에서 동영상 삭제 완료]");
					        }
				        }
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
				System.out.println("\n\n------------------------< 내 구독 리스트 >------------------------");
				System.out.println(" (1) 구독 리스트 확인");
				System.out.println(" (2) 구독 취소");
				System.out.println(" (3) 돌아가기");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[구독 리스트 확인]\n");
						
						sql = "SELECT * FROM users, subscribe WHERE subscriber_email = ? AND publisher_email = users.email;";
						pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmt.setString(1, user.getEmail());
						rs = pstmt.executeQuery();
						
				        rs.last();      
				        rowCount = rs.getRow();
				        rs.beforeFirst();
				        
				        if (rowCount == 0) {
				        	System.out.println("(!) 구독 리스트가 비어 있습니다.");
				        	break;
				        } else {
							while(rs.next()) {
								System.out.println(String.format(" - %s (%s) | 구독자 수 : %d명", 
										rs.getString("user_name"), 
										rs.getString("email"), 
										rs.getInt("num_of_subscribers")));
							}
			        	}
						break;
					case "2":
						// 구독 리스트에서 삭제
						System.out.println("\n[구독 취소]\n");
						
						do {
							System.out.print("(구독 취소할 사용자의 이름 또는 이메일을 입력하세요) >>> ");
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
				        	System.out.println("\n(!) 존재하지 않거나, 아직 구독하지 않은 사용자입니다.");
				        	break;
				        } else if (rowCount == 1) {
				        	rs.next();
				        	publisher_email = rs.getString("publisher_email");
				        	sql = "DELETE FROM subscribe WHERE publisher_email = ? AND subscriber_email = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							pstmt.executeUpdate();
							
							// publisher의 구독자수 1 감소
							sql = "UPDATE users SET num_of_subscribers = num_of_subscribers - 1 WHERE email = ?;";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.executeUpdate();
							
							System.out.println("\n[구독 취소 완료]");
				        } else {
				        	System.out.println();
				        	
				        	// 구독 취소할 사용자 이메일 입력
							while (rs.next()) {
								System.out.println(String.format(" - %s (%s) | 구독자 수 : %d명", 
										rs.getString("user_name"), 
										rs.getString("email"), 
										rs.getInt("num_of_subscribers")));
							}
							
							do {
								System.out.print("\n(구독 취소할 사용자의 이메일을 입력하세요) >>> ");
								publisher_email = scanner.nextLine();
							} while (publisher_email.length() == 0);
							
							sql = "SELECT EXISTS (SELECT * FROM subscribe WHERE publisher_email = ? AND subscriber_email = ? LIMIT 1)";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, publisher_email);
							pstmt.setString(2, user.getEmail());
							rs = pstmt.executeQuery();
							rs.next();
							
							if (rs.getInt(1) == 0) {
					        	System.out.println("\n(!) 존재하지 않거나, 아직 구독하지 않은 사용자입니다.");
					        	break;
							} else {
					        	sql = "DELETE FROM subscribe WHERE publisher_email = ? AND subscriber_email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.setString(2, user.getEmail());
								pstmt.executeUpdate();
								
								// publisher의 구독자수 1 감소
								sql = "UPDATE users SET num_of_subscribers = num_of_subscribers - 1 WHERE email = ?;";
								pstmt = con.prepareStatement(sql);
								pstmt.setString(1, publisher_email);
								pstmt.executeUpdate();
								
								System.out.println("\n[구독 취소 완료]");
					        }
				        }
						break;
					case "3":
						System.out.println();
						return;
					default:
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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
				System.out.println("\n\n------------------------< 내 시청 기록 >------------------------");
				System.out.println(" (1) 시청 기록 확인");
				System.out.println(" (2) 돌아가기");
				System.out.println("--------------------------------------------------------------");
				
				do {
					System.out.print("(메뉴를 선택하세요) >>> ");
					menu = scanner.nextLine();
				} while (menu.length() == 0);
				
				switch (menu) {
					case "1":						
						System.out.println("\n[시청 기록 확인]");

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
					        	System.out.println("\n(!) 출력할 시청 기록이 없습니다.");
					        	break;
					        } else if (rowCount < 10) {
					        	isLastPage = true;
					        }

					        System.out.println();
							while (rs.next()) {
								System.out.println(String.format(" - %s | ID : %s | 마지막 시청 시간 : %s", rs.getString("title"), rs.getString("video_id"), sdf.format(rs.getTimestamp("last_viewed_time"))));
							}
							
							while (!isLastPage) {
								do {
									System.out.print("\n계속 출력하시겠습니까? (Y/N) >>> ");
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
						System.out.println("\n(!) 메뉴를 다시 입력해주세요.");
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