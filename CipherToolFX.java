import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Scanner;

public class CipherToolFX extends Application {

    private ComboBox<String> cipherSelector;
    private TextField filePathField, keyAField, keyBField, desKeyField;
    private RadioButton encryptButton, decryptButton;
    private TextArea inputArea, outputArea;
    private Label keyALabel, keyBLabel, desKeyLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cipher Tool (Atbash, Affine & DES)");

        cipherSelector = new ComboBox<>();
        cipherSelector.getItems().addAll("Atbash", "Affine", "DES");
        cipherSelector.setValue("Atbash");

        filePathField = new TextField();
        keyAField = new TextField();
        keyBField = new TextField();
        desKeyField = new TextField();
        desKeyField.setPromptText("Enter 8 characters");

        keyALabel = new Label("Key a :");
        keyBLabel = new Label("Key b :");
        desKeyLabel = new Label("Key :");

        encryptButton = new RadioButton("Encrypt");
        decryptButton = new RadioButton("Decrypt");
        ToggleGroup group = new ToggleGroup();
        encryptButton.setToggleGroup(group);
        decryptButton.setToggleGroup(group);

        Button processButton = new Button("Process");
        Button clearButton = new Button("Clear");

        inputArea = new TextArea();
        inputArea.setPromptText("Enter text here or provide a file path above.");
        inputArea.setWrapText(true);

        outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        GridPane inputPane = new GridPane();
        inputPane.setPadding(new Insets(10));
        inputPane.setHgap(10);
        inputPane.setVgap(10);

        inputPane.add(new Label("Cipher:"), 0, 0);
        inputPane.add(cipherSelector, 1, 0);

        inputPane.add(new Label("File Path:"), 0, 1);
        inputPane.add(filePathField, 1, 1);

        inputPane.add(keyALabel, 0, 2);
        inputPane.add(keyAField, 1, 2);

        inputPane.add(keyBLabel, 0, 3);
        inputPane.add(keyBField, 1, 3);

        inputPane.add(desKeyLabel, 0, 4);
        inputPane.add(desKeyField, 1, 4);

        inputPane.add(encryptButton, 0, 5);
        inputPane.add(decryptButton, 1, 5);

        inputPane.add(processButton, 0, 6);
        inputPane.add(clearButton, 1, 6);

        VBox root = new VBox(10,
                inputPane,
                new Label("Input Text (Optional):"), inputArea,
                new Label("Output:"), outputArea);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        cipherSelector.setOnAction(e -> {
            String selected = cipherSelector.getValue();
            boolean isAffineVisible = selected.equals("Affine");
            boolean isDES = selected.equals("DES");

            keyAField.setVisible(isAffineVisible);
            keyBField.setVisible(isAffineVisible);
            keyALabel.setVisible(isAffineVisible);
            keyBLabel.setVisible(isAffineVisible);

            desKeyField.setVisible(isDES);
            desKeyLabel.setVisible(isDES);
        });

        keyAField.setVisible(false);
        keyBField.setVisible(false);
        keyALabel.setVisible(false);
        keyBLabel.setVisible(false);
        desKeyField.setVisible(false);
        desKeyLabel.setVisible(false);

        processButton.setOnAction(e -> process());
        clearButton.setOnAction(e -> {
            inputArea.clear();
            outputArea.clear();
        });
    }

    private void process() {
        String cipher = cipherSelector.getValue();
        String filePath = filePathField.getText().trim();
        String content = "";

        if (!filePath.isEmpty()) {
            content = readFile(filePath).trim();
        }

        if (content.isEmpty()) {
            content = inputArea.getText().trim();
        }

        if (content.isEmpty()) {
            showAlert("Error", "No input text found (from file or input area).");
            return;
        }

        if (!encryptButton.isSelected() && !decryptButton.isSelected()) {
            showAlert("Error", "Please select Encrypt or Decrypt.");
            return;
        }

        boolean isEncrypt = encryptButton.isSelected();

        try {
            if (cipher.equals("Atbash")) {
                outputArea.setText(atbash(content));
            } else if (cipher.equals("Affine")) {
                int a = Integer.parseInt(keyAField.getText().trim());
                int b = Integer.parseInt(keyBField.getText().trim());
                if (gcd(a, 26) != 1) {
                    showAlert("Error", "'a' must be coprime with 26.");
                    return;
                }
                outputArea.setText(isEncrypt ? affineEncrypt(content, a, b) : affineDecrypt(content, a, b));
            } else if (cipher.equals("DES")) {
                String key = desKeyField.getText().trim();
                if (key.length() != 8) {
                    showAlert("Error", "DES key must be exactly 8 characters.");
                    return;
                }
                outputArea.setText(isEncrypt ? desEncrypt(content, key) : desDecrypt(content, key));
            }
        } catch (Exception e) {
            showAlert("Error", "Exception: " + e.getMessage());
        }
    }

    // Auther: Layan Alaboudi
    private String desEncrypt(String text, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "DES"); // key generation
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // create cipher object to select algrithim options
        cipher.init(Cipher.ENCRYPT_MODE, secretKey); // to select encryption mode, attach the key, and cipher object
        byte[] encrypted = cipher.doFinal(text.getBytes()); // convort string into bytes
        return Base64.getEncoder().encodeToString(encrypted);
    }// end desEncrypt

    private String desDecrypt(String base64Text, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "DES"); // key generation
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // create cipher object to select algrithim options
        cipher.init(Cipher.DECRYPT_MODE, secretKey); // to select decryption mode, attach the key, and cipher object
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(base64Text)); // decode and decrypt
        return new String(decrypted);
    }// end desDecrypt

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(title);
        alert.showAndWait();
    }// end showAlert

    // Auther: Roaa
    public static String atbash(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append((char) ('Z' - (c - 'A')));
            } else if (Character.isLowerCase(c)) {
                result.append((char) ('z' - (c - 'a')));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Auther: Dhay
    private String affineEncrypt(String text, int a, int b) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int x = c - base;
                int enc = (a * x + b) % 26;
                result.append((char) (enc + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String affineDecrypt(String text, int a, int b) {
        StringBuilder result = new StringBuilder();
        int a_inv = modInverse(a, 26);
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int y = c - base;
                int dec = (a_inv * (y - b + 26)) % 26;
                result.append((char) (dec + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                content.append(fileScanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            return "";
        }
        return content.toString();
    }
}
