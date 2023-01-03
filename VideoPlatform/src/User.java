public class User {
	
	private String email;
	private String userPassword;
	private String userName;
	private int age;
	private String sex;
	
	
	public User(String email, String pw, String name, int age, String sex) {
		this.email = email;
		this.userPassword = pw;
		this.userName = name;
		this.age = age;
		this.sex = sex;
	}
	
	
	public String getEmail() {
		return this.email;
	}
	
	
	public String getUserPassword() {
		return this.userPassword;
	}
	
	
	public String getUserName() {
		return this.userName;
	}
	

	public int getUserAge() {
		return this.age;
	}
	
	
	public String getUserSex() {
		return this.sex;
	}	
}
