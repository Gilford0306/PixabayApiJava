package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.json.*;

public class Main {
    public static void main(String[] args) {
        String apiKey = "39207646-8ccbcc7ff50179f772c7bccff";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your search query:");
        String query = scanner.nextLine();

        System.out.println("Enter number of images: (minimum 3)");
        int numberOfImages = scanner.nextInt();
        if (numberOfImages<3)
            return;
        scanner.close();

        //int numberOfImages = 10;

        try {
            String urlString = "https://pixabay.com/api/?key=" + apiKey + "&q=" + URLEncoder.encode(query, "UTF-8") + "&per_page=" + numberOfImages;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray hits = jsonResponse.getJSONArray("hits");
            File documentsFolder = new File(System.getProperty("user.home") + "/Documents/PixabayImages");
            if (!documentsFolder.exists()) {
                documentsFolder.mkdirs();
            }

            for (int i = 0; i < hits.length(); i++) {
                JSONObject image = hits.getJSONObject(i);
                String imageUrl = image.getString("webformatURL");
                saveImage(imageUrl, documentsFolder.getAbsolutePath() + "/ " + query+" "+ i + ".jpg");
            }

            System.out.println("Images downloaded successfully.");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream(); OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFile))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
