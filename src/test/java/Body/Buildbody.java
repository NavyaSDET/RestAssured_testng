package Body;

import org.json.JSONObject;

public class Buildbody {

	// Method to build the JSON body for user update
	public static String buildUpdateUserBody(Object[] userRow) {
		// Create the outer JSON object for the user details
		JSONObject userBody = new JSONObject();

		// Add user details to the outer JSON object
		if(!userRow[9].equals("FALSE")) {
			userBody.put("user_first_name", userRow[9]);
		}
		if(!userRow[10].equals("FALSE")) {
			userBody.put("user_last_name", userRow[10]);
		}
		if(!userRow[11].equals("FALSE")) {
			userBody.put("user_contact_number", userRow[11]);
		}
		if(!userRow[12].equals("FALSE")) {
			userBody.put("user_email_id", userRow[12]);
		}

		// Create the inner JSON object for the user address
		if(!userRow[3].equals("FALSE")) {
			JSONObject userAddressObject = new JSONObject();
			if(!userRow[5].equals("FALSE")) {
				userAddressObject.put("plotNumber", userRow[5]);
			}
			if(!userRow[4].equals("FALSE")) {
				userAddressObject.put("Street", userRow[4]);
			}
			if(!userRow[6].equals("FALSE")) {
				userAddressObject.put("state", userRow[6]);
			}
			if(!userRow[7].equals("FALSE")) {
				userAddressObject.put("Country", userRow[7]);
			}
			if(!userRow[8].equals("FALSE")) {
				userAddressObject.put("zipCode", userRow[8]);
			}

			// Add the address object to the main user body
			userBody.put("userAddress", userAddressObject);
		}

		// Return the JSON object as a string
		return userBody.toString();
	}

	// Method to build the JSON body for user creation with address
	public static String buildCreateUserBody(Object[] userRow) {
		// Create the outer JSON object for the user details
		JSONObject userBody = new JSONObject();

		// Add user details to the outer JSON object
		userBody.put("user_first_name", userRow[9]);
		userBody.put("user_last_name", userRow[10]);
		userBody.put("user_contact_number", userRow[11]);
		userBody.put("user_email_id", userRow[12]);

		// Create the inner JSON object for the user address
		JSONObject userAddress = new JSONObject();
		userAddress.put("plotNumber", userRow[5]);
		userAddress.put("Street", userRow[4]);
		userAddress.put("state", userRow[6]);
		userAddress.put("Country", userRow[7]);
		userAddress.put("zipCode", userRow[8]);

		// Add the address object to the main user body
		userBody.put("userAddress", userAddress);

		// Return the JSON object as a string
		return userBody.toString();
	}
}