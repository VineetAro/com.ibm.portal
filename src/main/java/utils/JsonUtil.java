package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static final String TESTDATA_PATH = "src/test/resources/testdata/";
	private static final ObjectMapper mapper = new ObjectMapper(); // Reusable instance

	public static JsonNode readJsonFile(String filename) {
		JsonNode node = null;
		// Try-with-resources ensures the stream closes automatically
		try (FileInputStream fis = new FileInputStream(TESTDATA_PATH + filename)) {
			node = mapper.readTree(fis);
		} catch (IOException e) {
			System.err.println("Error reading JSON file: " + TESTDATA_PATH + filename);
			e.printStackTrace();
		}
		return node;
	}

	public static List<Map<String, String>> readJsonArray(String filename, String arrayKey) {
		List<Map<String, String>> lst = new ArrayList<>();
		JsonNode node = readJsonFile(filename);

		// Check if the file was read successfully and the key exists
		if (node != null && node.has(arrayKey)) {
			JsonNode arrayNode = node.get(arrayKey);

			if (arrayNode.isArray()) {
				for (JsonNode element : arrayNode) {
					Map<String, String> map = mapper.convertValue(element, new TypeReference<Map<String, String>>() {
					});
					lst.add(map);
				}
			}
		}
		return lst;
	}
}
