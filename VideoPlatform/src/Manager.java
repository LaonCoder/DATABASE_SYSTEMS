public class Manager {
	
	private String email;
	private String managerPassword;
	private String managerName;
	
	
	public Manager(String email, String pw, String name) {
		this.email = email;
		this.managerPassword = pw;
		this.managerName = name;
	}
	
	
	public String getEmail() {
		return this.email;
	}
	
	
	public String getManagerPassword() {
		return this.managerPassword;
	}
	
	
	public String getManagerName() {
		return this.managerName;
	}
}
