package Model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;


public class UserDatabase {
    protected ArrayList<User> records;
    protected String filename;

    public UserDatabase(){
        records = new ArrayList<>();
        filename = "src/Model/userdatabase.json";
        readFromFile(filename);
    }

    public ArrayList<User> readFromFile(String filePath) {
        ArrayList<User> users = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray array = new JSONArray(content);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String email = obj.getString("email");
                String password = obj.getString("password");
                String usertype = obj.getString("usertype");

                if (usertype.equalsIgnoreCase("Student")) {
                    users.add(new Student(email, password));
                } else if (usertype.equalsIgnoreCase("Instructor")) {
                    users.add(new Instructor(email, password));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


    public void appendUser(String filePath, User u) {
        JSONArray array;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            array = new JSONArray(jsonText.toString());
        } catch (Exception e) {
            array = new JSONArray();
        }

        JSONObject obj = new JSONObject(new LinkedHashMap<>());
        obj.put("email", u.email);
        obj.put("password", u.password);

        if (u instanceof Student) {
            obj.put("usertype", "Student");
        } else if (u instanceof Instructor) {
            obj.put("usertype", "Instructor");
        }
        array.put(obj);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(array.toString(4));
            System.out.println("Appended user to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean contains(String email){
        for (User user : records) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public String getFilename() {
        return filename;
    }
}

