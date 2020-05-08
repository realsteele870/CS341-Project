
public class User {
	int id;
	String firstName;
	String lastName;
	String userType;
	String userName;
	String deacDate;
	protected String password;
	public User(String fName, String lName, String uType) {
		this.firstName = fName;
		this.lastName  = lName;
		this.userType  = uType;
	}
	public User(String fName, String lName, String uType, String uName, String pWord) {
		this.firstName = fName;
		this.lastName  = lName;
		this.userType  = uType;
		this.userName  = uName;
		this.password  = pWord;
	}
	public User(String fName, String lName, String uType, String uName, String pWord, String dDate) {
		this.firstName = fName;
		this.lastName = lName;
		this.userType = uType;
		this.userName = uName;
		this.password = pWord;
		this.deacDate = dDate;
	}
	String getFirstName() {
		return this.firstName;
	}
	void setFirstName(String fName) {
		this.firstName = fName;
	}
	String getLastName() {
		return this.lastName;
	}
	void setLastName(String lName) {
		this.lastName = lName;
	}
	String getUserType() {
		return this.userType;
	}
	void setUserType(String uType) {
		this.userType = uType;
	}
	String getUserName() {
		return this.userName;
	}
	void setUserName(String uName) {
		this.userName = uName;
	}
	String getPassWord() {
		return this.password;
	}
	void setPassword(String pWord) {
		this.password = pWord;
	}
	
}
