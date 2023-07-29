package main.java.userInterface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;



public class UIController implements Initializable {
	
	private int screenshotCount = 0;
	private int timeout = 150;
//	private List<BufferedImage> screenshots = new ArrayList<>();

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private MenuButton menuButton;

    @FXML
    private Button button4;
    
    @FXML
    private ImageView captureImage;
    
    @FXML
    private ImageView saveImage;
    
    @FXML
    private ImageView delayImage;
    
    @FXML
    private ImageView resetImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	String cachePath = System.getProperty("java.io.tmpdir") + "screenshot_cache";
        File cacheDir = new File(cachePath);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] screenshotFiles = cacheDir.listFiles();
            if (screenshotFiles != null) {
                for (File file : screenshotFiles) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }

        // Rest the screenshot count to 0
        screenshotCount = 0;
        Image captureImg = new Image(getClass().getResourceAsStream("Capture.png"));
        Image saveImg = new Image(getClass().getResourceAsStream("Save.png"));
        Image delayImg = new Image(getClass().getResourceAsStream("Delay.png"));
        Image resetImg = new Image(getClass().getResourceAsStream("Reset.png"));
        
        // Set the loaded image to the ImageView
        captureImage.setImage(captureImg);
        saveImage.setImage(saveImg);
        delayImage.setImage(delayImg);
        resetImage.setImage(resetImg);
    }
    
    public List<BufferedImage> getScreenshots() {
    	List<BufferedImage> screenshots = new ArrayList<>();
        String cachePath = System.getProperty("java.io.tmpdir") + "screenshot_cache";
        File cacheDir = new File(cachePath);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] screenshotFiles = cacheDir.listFiles();
            if (screenshotFiles != null) {
                for (File file : screenshotFiles) {
                    if (file.isFile()) {
                        try {
                            BufferedImage screenshot = ImageIO.read(file);
                            screenshots.add(screenshot);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return screenshots;
    }

    @FXML
    private void onButton1Click() {
    	  // Get the JavaFX window reference
        Stage stage = (Stage) button1.getScene().getWindow();

        // Hide the window temporarily to take the screenshot
        stage.hide();

        // Take a screenshot of the entire screen (excluding the JavaFX window) after the UI is fully hidden
        Platform.runLater(() -> {
        	try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            try {
                // Create a Robot instance to capture the screen
                Robot robot = new Robot();

                // Get the bounds of the entire screen
                Rectangle screenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                // Capture the screen as a BufferedImage
                BufferedImage screenshot = robot.createScreenCapture(screenBounds);
//                screenshots.add(screenshot);

             // Create a directory for the cache (if it doesn't exist)
                String cachePath = System.getProperty("java.io.tmpdir") + "screenshot_cache";
                File cacheDir = new File(cachePath);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }

                // Generate a unique filename for the screenshot
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timestamp = sdf.format(new Date());
                String fileName = "screenshot_"+screenshotCount +"_"+ timestamp + ".png";
                screenshotCount++;

                // Save the screenshot to the cache directory
                File outputFile = new File(cacheDir, fileName);
                ImageIO.write(screenshot, "png", outputFile);

                System.out.println("Screenshot saved to: " + outputFile.getAbsolutePath());
            } catch (AWTException | IOException ex) {
                ex.printStackTrace();
            }

            // Show the window after taking the screenshot
            stage.show();
        });
    }

    @FXML
    private void onResetClick() {
    	 // Clear all previously taken screenshots from the cache directory
        String cachePath = System.getProperty("java.io.tmpdir") + "screenshot_cache";
        File cacheDir = new File(cachePath);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] screenshotFiles = cacheDir.listFiles();
            if (screenshotFiles != null) {
                for (File file : screenshotFiles) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }

        // Rest the screenshot count to 0
        screenshotCount = 0;

        System.out.println("All previously taken screenshots deleted.");
    }

    @FXML
    private void onSaveClick() throws InvalidFormatException {
    	 try {
    		 createWordDocument();
    		 String cachePath = System.getProperty("java.io.tmpdir") + "screenshot_cache";
    	        File cacheDir = new File(cachePath);
    	        if (cacheDir.exists() && cacheDir.isDirectory()) {
    	            File[] screenshotFiles = cacheDir.listFiles();
    	            if (screenshotFiles != null) {
    	                for (File file : screenshotFiles) {
    	                    if (file.isFile()) {
    	                        file.delete();
    	                    }
    	                }
    	            }
    	        }

    	        // Rest the screenshot count to 0
    	        screenshotCount = 0;
//             FileChooser fileChooser = new FileChooser();
//             fileChooser.setTitle("Save Screenshots as Word Document");
//             fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word files", "*.docx"));
//
//             File outputFile = fileChooser.showSaveDialog(null);
//             if (outputFile != null) {
//                 createWordDocument(outputFile);
////                 System.out.println("Screenshots saved to Word document: " + outputFile.getAbsolutePath());
//             }
         } catch (Exception ex) {
             ex.printStackTrace();
         }
    }
    
    private void createWordDocument() throws IOException, InvalidFormatException {
    	 // Create a new in-memory Word document
        XWPFDocument doc = new XWPFDocument();

        // Create a new paragraph to hold the screenshots
        XWPFParagraph paragraph = doc.createParagraph();

        // Get all the screenshots from the screenshots list
        List<BufferedImage> screenshots = getScreenshots();

        // Define the width and height of the images to fit the page
        int pageWidth = 595; // Adjust this value based on the page width in Word (A4 size is 595)
        int pageHeight = 842; // Adjust this value based on the page height in Word (A4 size is 842)
        int maxWidth = pageWidth - 100; // Leave some margin on the sides
        int maxHeight = pageHeight - 100; // Leave some margin on the top and bottom

        // Add each screenshot to the Word document
        for (BufferedImage screenshot : screenshots) {
            XWPFRun run = paragraph.createRun();
            run.addBreak();

            // Calculate the new dimensions of the image to fit the page while maintaining the aspect ratio
            int originalWidth = screenshot.getWidth();
            int originalHeight = screenshot.getHeight();

            int newWidth = originalWidth;
            int newHeight = originalHeight;

            if (originalWidth > maxWidth) {
                newWidth = maxWidth;
                newHeight = (newWidth * originalHeight) / originalWidth;
            }

            if (newHeight > maxHeight) {
                newHeight = maxHeight;
                newWidth = (newHeight * originalWidth) / originalHeight;
            }

            // Convert the image to an input stream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(screenshot, "png", os);
            String imageName = "Screenshot " + (screenshots.indexOf(screenshot) + 1);
            XWPFRun captionRun = paragraph.createRun();
            captionRun.setText(imageName);

            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

            // Add the image to the paragraph with the resized dimensions
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, "screenshot.png", Units.toEMU(newWidth), Units.toEMU(newHeight));
            os.close();
            is.close();
        }


        // Create a temporary file
        File tempFile = File.createTempFile("temp", ".docx");

        // Write the Word document to the temporary file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            doc.write(fos);
        }

        // Open the temporary file using the default application
        Desktop.getDesktop().open(tempFile);

        // Delete the temporary file after opening
        tempFile.deleteOnExit();
    }

    


    @FXML
    private void onMenuItemClick(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        HBox menuItemContent = (HBox) menuItem.getGraphic();

        // Find the Label component within the HBox
        for (Node node : menuItemContent.getChildren()) {
            if (node instanceof Label) {
                String menuItemText = ((Label) node).getText();
                String[] timeoutUpdate = menuItemText.split(" ");
                timeout = Integer.parseInt(timeoutUpdate[0])*1000;
                System.out.println("Menu Item Selected: " + menuItemText);
                break;
            }
        }
    }

}
