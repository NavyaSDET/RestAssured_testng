package Tests;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utils.CSVReader;
import Utils.ConfigReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class updateUserInfo {

	List<Integer> userIdList = new ArrayList<>(); 
	private static List<Object[]> userData;
	static String baseURL;
	static String username;
	static String password;

	@BeforeClass
	public static void loadData() throws Throwable {
		ConfigReader.loadConfig();
		baseURL = ConfigReader.getBaseUrl();
		username = ConfigReader.getUserName();
		password = ConfigReader.getPassword();

		// Specify the path to your CSV file
		userData = CSVReader.readCSV("Data/Put_scenarios.csv");
		System.out.println("Length of user data is " + userData.size());
	}

	@DataProvider(name = "createUserData")
	public static Object[][] getCreateDataFromCSV() throws IOException {
		// Assume readCSV() returns a List<Object[]>

		// List to hold rows where the first value is "POST"
		List<Object[]> postData = new ArrayList<>();

		// Loop through all data and filter for rows where the first cell is "POST"
		for (Object[] user : userData) {
			if (user[0] != null && user[0].equals("POST")) { // Check if first value is "POST"
				postData.add(user); // Add the row to postData list
			}
		}

		// Convert the list to 2D array for TestNG DataProvider
		return postData.toArray(new Object[postData.size()][]);
	}

	@DataProvider(name = "updateUserData")
	public static Object[][] getUpdateDataFromCSV() throws IOException {
		// Assume readCSV() returns a List<Object[]>

		// List to hold rows where the first value is "POST"
		List<Object[]> postData = new ArrayList<>();

		// Loop through all data and filter for rows where the first cell is "POST"
		for (Object[] user : userData) {
			if (user[0] != null && user[0].equals("PUT")) { // Check if first value is "POST"
				postData.add(user); // Add the row to postData list
			}
		}

		// Convert the list to 2D array for TestNG DataProvider
		return postData.toArray(new Object[postData.size()][]);
	}

	@Test(priority = 0, dataProvider = "createUserData")
	public void createUser(Object[] user) {

		String requestBody = Body.Buildbody.buildCreateUserBody(user);

		Response response = given()
				.baseUri(baseURL)
				.basePath("/createusers")
				.contentType("application/json")
				.auth().basic(username, password)  // Add Basic Authentication// Set content type to JSON
				.body(requestBody)               // Add the request body
				.when()
				.post(); 
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 201, "Expected status code to be 201");

		userIdList.add(response.jsonPath().get("user_id"));		

	}

	@Test(priority = 1)
	public void getUsers() {
		for (Integer userId : userIdList) {

			Response response = given()
					.baseUri(baseURL)
					.basePath("/user/"+userId)
					.auth().basic(username, password)  // Add Basic Authentication
					.when()
					.get(); 
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, 200);
		}
	}


	@Test(priority = 2, dataProvider = "updateUserData")
	public void updateUser(Object[] user) {
		Integer userId;

		String requestBody = Body.Buildbody.buildUpdateUserBody(user);
		System.out.println(requestBody);

		if(user[2].equals("null")) {
			userId = userIdList.get(0);
		}
		else if (user[2] == "false") {
			userId = null;
		}
		else {
			userId = Integer.parseInt((String) user[2]);
		}
		System.out.println("UserId is" + userId);

		Response response = given()
				.baseUri(baseURL)
				.basePath("/updateuser/"+ userId)
				.contentType("application/json")  // Set content type to JSON
				.auth().basic(username, password)  // Add Basic Authentication
				.body(requestBody)               // Add the request body
				.when()
				.put(); 
		Integer statusCode = response.getStatusCode();
		System.out.println("Response Body: " + response.getBody().asString());
		Assert.assertEquals(statusCode, Integer.parseInt((String) user[13]));

	}

	@Test(priority = 3)
	public void deleteUsers() {

		Response response = given()
				.baseUri(baseURL)
				.basePath("/deleteuser/"+userIdList.get(0))
				.auth().basic(username, password)  // Add Basic Authentication
				.when()
				.delete(); 
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Expected status code to be 201");
	}

	@Test(priority = 4)
	public void deleteUsersByName() {

		Response response = given()
				.baseUri(baseURL)
				.basePath("/user/"+userIdList.get(1))
				.auth().basic(username, password)  // Add Basic Authentication
				.when()
				.get(); 
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		JsonPath jsonPath = response.jsonPath();
		String firstname = jsonPath.getString("user_first_name");
		Response deleteResponse = given()
				.baseUri(baseURL)
				.basePath("/deleteuser/username/"+firstname)
				.auth().basic(username, password)  // Add Basic Authentication
				.when()
				.delete(); 
		int statusCode2 = deleteResponse.getStatusCode();
		Assert.assertEquals(statusCode2, 200, "Expected status code to be 201");

	}
}