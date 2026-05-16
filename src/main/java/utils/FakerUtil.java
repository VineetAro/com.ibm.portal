package utils;

import com.github.javafaker.Faker;

public class FakerUtil {

	private static Faker faker = new Faker();

	public static String generateEmail() {
		return faker.internet().emailAddress();
	}

	public static String generatedPassword() {
		return faker.internet().password(6, 10);
	}

	public static String generateFirstName() {
		return faker.name().firstName();
	}

	public static String generateLastName() {
		return faker.name().lastName();
	}

}
