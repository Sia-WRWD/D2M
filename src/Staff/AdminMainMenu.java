/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Staff;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author chinojen7
 */
public class AdminMainMenu extends javax.swing.JFrame {

    /**
     * Creates new form AdminMainMenu
     */
    String saveDir;
    private final Color euf = new Color(255, 0, 0);
    private final Color neuf = new Color(0, 0, 0);
    boolean x = true;

    public AdminMainMenu() throws IOException {
        initComponents();
        this.setLocationRelativeTo(null);
        getItemData();
        JP_Registration.setVisible(false);
        JP_AddItem.setVisible(false);
        JP_DeleteItem.setVisible(false);
        JP_EditPwd.setVisible(false);
        JP_DeleteAccount.setVisible(false);
        JP_UpdateItem.setVisible(false);
        JP_PastTransaction.setVisible(false);
        lblTemp.setVisible(false);
        lblUpTemp.setVisible(false);
        GetUsername();
    }

    //Check EmptyFields, throwing exception for Registration, Add Item, Delete Item, Edit Password, Delete Account.
    private void RegistrationEmptyField() throws Exception {
        if ("".equals(tfAddUsername.getText())) {
            throw new Exception("Empty New Admin Username!");
        }

        if ("".equals(String.valueOf(pfAddPassword.getPassword()))) {
            throw new Exception("Empty New Admin Password Value!");
        }

        if ("".equals(tfAddName.getText())) {
            throw new Exception("Empty New Admin Real Name!");
        }
    }

    private void AddItemEmptyField() throws Exception {
        if ("".equals(tfAddItemName.getText())) {
            throw new Exception("Empty New Item Name!");
        }

        if ("".equals(tfAddItemPrice.getText())) {
            throw new Exception("Empty New Item Price!");
        }

        if ("".equals(lblTemp.getText())) {
            throw new Exception("Empty New Item Picture!");
        }
    }

    private void DelItemEmptyField() throws Exception {
        if ("".equals(tfDelItemID.getText())) {
            throw new Exception("Empty Delete Item ID!");
        }

        if ("".equals(tfDelItemName.getText())) {
            throw new Exception("Empty Delete Item Name!");
        }
    }

    private void EditPwdEmptyField() throws Exception {
        if ("".equals(tfEditUsername.getText())) {
            throw new Exception("Empty Edit Password's Username!");
        }

        if ("".equals(String.valueOf(pfEditNewPassword.getPassword()))) {
            throw new Exception("Empty Edit Password's Password!");
        }
    }

    private void DelAccountEmptyField() throws Exception {
        if ("".equals(tfDelUsername.getText())) {
            throw new Exception("Empty Delete Account's Username!");
        }

        if ("".equals(String.valueOf(pfDelPassword.getPassword()))) {
            throw new Exception("Empty Delete Account's Password!");
        }
    }

    private void UpdateItemEmptyField() throws Exception {
        if ("".equals(tfUpItemID.getText())) {
            throw new Exception("Empty Update Item's Item ID!");
        }

        if ("".equals(tfUpItemName.getText())) {
            throw new Exception("Empty Update Item's Old Item Name!");
        }

        if ("".equals(tfUpItemPrice.getText())) {
            throw new Exception("Empty Update Item's Item Price!");
        }

        if ("".equals(tfUpItemQuantity.getText())) {
            throw new Exception("Empty Update Item's Item Quantity!");
        }
    }

    private void PastTransactionEmptyField() throws Exception {
        if ("".equals(tfPastTranID.getText())) {
            throw new Exception("Empty Past Transaction ID!");
        }
    }

    //Registration
    private void addAdminInformation() throws Exception {
        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\Admin.txt"; //Retrieve Directory for Admin txt Database.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr); //Read Admin Data
        String record;
        String regex = "^[A-Za-z_][A-Za-z0-9_]{3,4}$"; //Admin Username can only be 3 to 5 characters.
        boolean match = tfAddUsername.getText().matches(regex);

        while ((record = br.readLine()) != null) { //Retrieve All Admin Data.
            String[] fields = record.split(":");
            String AdminID = fields[0];
            String AdminUsername = fields[1];
            String AdminPassword = fields[2];
            String AdminRealName = fields[3];

            String lastNum = fields[0];

            try {
                if (AdminUsername.equals(tfAddUsername.getText())) { //Check if Username ALready Existed
                    br.close();
                    JOptionPane.showMessageDialog(null, "Username Already Existed, Please Choose a New One and Try Again!", "Same Username Exist", JOptionPane.WARNING_MESSAGE);
                } else if ((lastNum.equals("10")) && (!"*".equals(AdminUsername))) { //Check if txt Database is full, if yes = data x inserted.
                    br.close();
                    JOptionPane.showMessageDialog(null, "The Number of Admins Allowed to Register is 10. Please Remove an Admin and Try Again!", "Max Admin Slot Reached", JOptionPane.WARNING_MESSAGE);
                } else if ("".equals(tfAddUsername.getText()) || "".equals(String.valueOf(pfAddPassword.getPassword())) || "".equals(tfAddName.getText())) { //Check Empty Fields;
                    br.close();
                    JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill it Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                    HighlightEmptyFields();
                    RegistrationEmptyField();
                } else if (!match) { //Check Regex for Admin Username.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Your Username Should be Between 3 to 5 Characters!", "Invalid Username", JOptionPane.WARNING_MESSAGE);
                } else if ("*".equals(tfAddUsername.getText()) || "*".equals(String.valueOf(pfAddPassword.getPassword()))) { //Check if User inputs * into textfields.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Username and Password Inputted are Invalid, Please Try Again!", "Invalid Username and Password", JOptionPane.WARNING_MESSAGE);
                } else if ("*".equals(AdminUsername) && "*".equals(AdminPassword) && "*".equals(AdminRealName)) { //Condition = Admin Data contains * which represents empty data slot. Condition met = Insert Data.
                    br.close();
                    deleteItemData(saveDir, AdminID, 1, ":"); //Remove Empty Data Lines
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String newAdminUsername = tfAddUsername.getText(); //Assign data inputted into textfields by user into Variables.
                    String newAdminPassword = String.valueOf(pfAddPassword.getText());
                    String newAdminRealName = tfAddName.getText();
                    String AdminAccData = AdminID + ":" + newAdminUsername + ":" + newAdminPassword + ":" + newAdminRealName + "\n";
                    bw.write(AdminAccData); //Writes values stored in the Variables into Admin txt Database.
                    bw.close();
                    sortItemData(saveDir, saveDir); //Rearrange the Data Lines in Text File Alphabetically.
                    DeHighlightEmptyFields();
                    JOptionPane.showMessageDialog(null, "Successfully Registered " + tfAddUsername.getText() + ".", "Register Admin Successful", JOptionPane.INFORMATION_MESSAGE);
                    tfAddUsername.setText(""); //Reset Form
                    pfAddPassword.setText("");
                    tfAddName.setText("");
                    break; //Stop While Loop
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Add Item
    private void InsertItem() throws IOException {

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Retrieve Directory for ItemList txt Database.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;
        String ItemNameregex = "^[A-Za-z_][A-Za-z0-9_]{4,10}$"; //Item Name Should be Within 4 to 10 characters.
        boolean ItemNamematch = tfAddItemName.getText().matches(ItemNameregex);
        String ItemPriceregex = "\\d{1,2}\\.\\d{1,2}"; //Item Price should be in 0.00, 00.00 format.
        boolean ItemPricematch = tfAddItemPrice.getText().matches(ItemPriceregex);

        while ((record = br.readLine()) != null) { //Retrieving Item Data from ItemList txt Database
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];

            String lastNum = fields[0];

            try {
                if (ItemName.equals(tfAddItemName.getText())) { //Check if User Input for Item Name is already existed in the Database.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Item With This Name Already Existed, Please Try Again!", "Item Already Existed", JOptionPane.WARNING_MESSAGE);
                } else if ((lastNum.equals("06")) && (!"*".equals(ItemName))) { //Check if Database still has Available Item Slot, No = X Insert Data.
                    br.close();
                    JOptionPane.showMessageDialog(null, "The Number of Items allowed is 6. Please Remove an Item and Try Again!", "Max Item Slot Reached", JOptionPane.WARNING_MESSAGE);
                } else if ("".equals(tfAddItemName.getText()) || "".equals(tfAddItemPrice.getText()) || "".equals(lblTemp.getText())) { //Check Empty Input Fields.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill it Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                    HighlightEmptyFields();
                    AddItemEmptyField();
                } else if (!ItemNamematch) { //Check Regex for Item Name.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Item Name Should be Between 5 and 10 Characters!", "Invalid Item Name", JOptionPane.WARNING_MESSAGE);
                } else if (!ItemPricematch) { //Check Regex for Item Price.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Item Price Should be in the '0.00' or '00.00' Format!", "Invalid Item Price", JOptionPane.WARNING_MESSAGE);
                } else if ("*".equals(ItemName)) { //Checks if Empty Slot Exist, Yes = Insert Item Data.
                    br.close();
                    deleteItemData(saveDir, ItemID, 1, ":"); //Removes Line of Empty Data in Database.
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String newItemName = tfAddItemName.getText(); //Assign Variables for User Inputs.
                    String newItemPrice = tfAddItemPrice.getText();
                    String newItemQuantity = "10";
                    String itemData = ItemID + ":" + newItemName + ":" + newItemPrice + ":" + newItemQuantity + ":" + "ItemPic" + ItemID + ".png" + "\n";
                    bw.write(itemData); //Writes the Values contained in the assigned Variables to the itemlist txt Database.
                    bw.close();
                    sortItemData(saveDir, saveDir); //Rearrange the Data Lines in Text File Alphabetically.
                    String oldFileLocation = lblTemp.getText(); //Assign the Location of the Uploaded Image to a label to send to the InsertKItemImage() Method.
                    insertItemImage(ItemID, oldFileLocation); //Calls InsertItemImage() Method with values of the ItemID and oldFileLocation.
                    DeHighlightEmptyFields();
                    JOptionPane.showMessageDialog(null, "Successfully Added " + tfAddItemName.getText() + ".", "Add Item Successful", JOptionPane.INFORMATION_MESSAGE);
                    tfAddItemName.setText(""); //Reset Form
                    tfAddItemPrice.setText("");
                    lblTemp.setText("");
                    lblItemPic.setIcon(null);
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void retrieveItemPicture() {

        String saveDir = System.getProperty("user.dir");
        String destination = saveDir + "\\src\\Item_Pic\\"; //Sets the Directory Folder Containing Item Picture Files.

        try {

            JFileChooser jfc = new JFileChooser();
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("4 Extensions Supported", "Jpg", "png", "jpeg", "gif"); //declaring type of Picture Extensions supported.
            jfc.setFileFilter(fnef);
            int selected = jfc.showOpenDialog(null);
            if (selected == JFileChooser.APPROVE_OPTION) { //Opens Window To Select Picture File To Upload As Item Picture.

                File file = jfc.getSelectedFile(); //Store Item Picture as File Object.
                String getselectedImage = file.getAbsolutePath(); //Assign a Variable to store the Directory of the Picture File Selected.

                BufferedImage BuffImageIco = ImageIO.read(new File(getselectedImage)); //Creates Image Icon Object From The Provided Directory.
                lblItemPic.setIcon(new ImageIcon(BuffImageIco)); //Sets Icon Image on Label As A Preview.
                lblTemp.setText(getselectedImage); //Stores the Directory of Selected Picture Which Will Be Sent To the InsertItemData() Method.

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error has Occured while Retrieving Image.", "Image Retrieval Failure", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void retrieveUpItemPicture() {

        String saveDir = System.getProperty("user.dir");
        String destination = saveDir + "\\src\\Item_Pic\\"; //Sets the Directory Folder Containing Thumbnail Image Files.

        try {

            JFileChooser jfc = new JFileChooser();
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("4 Extensions Supported", "Jpg", "png", "jpeg", "gif");
            jfc.setFileFilter(fnef);
            int selected = jfc.showOpenDialog(null);
            if (selected == JFileChooser.APPROVE_OPTION) { //Opens Window To Select Image File To Upload As Thumbnail Image.

                File file = jfc.getSelectedFile(); //Store Image as File Object.
                String getselectedImage = file.getAbsolutePath(); //Assigns Variable Containing Directory Where It Was Selected.

                BufferedImage BuffImageIco = ImageIO.read(new File(getselectedImage)); //Creates Image Icon Object From The Provided Directory.
                lblUpItemPicPrev.setIcon(new ImageIcon(BuffImageIco)); //Sets Icon Image on Label As A Preview.
                lblUpTemp.setText(getselectedImage); //Sets Directory of Selected Image Which Will Be Sent To the insertData() Method.

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error has Occured while Retrieving Image.", "Image Retrieval Failure", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void insertItemImage(String InsertedItemID, String picFileSelected) {

        File file = new File(picFileSelected); //Create A File Object With The Directory of the Selected Picture.

        String saveDir = System.getProperty("user.dir");
        String destination = saveDir + "\\src\\Item_Pic\\"; //Retrieving Directory of The Folder Containing Item Picture Files.
        String extensionName = FilenameUtils.getExtension(picFileSelected); // Retrieve File Extension of Selected Picture File.
        String newPictureFileName = destination + "ItemPic" + InsertedItemID + "." + extensionName; // Assigning New Directory and New Image Filename.

        File newFile = new File(newPictureFileName);  // Create File Objects of the Item Picture File That Will Be Transfered To The Source Folder.
        File oriFileName = new File(destination);

        try {
            if (newFile.exists()) { //Checks If Picture with same name already existed. If yes = replace existing file.
                Path imagesPath = Paths.get(newPictureFileName);
                Files.delete(imagesPath);
            }
            Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING); //Copies File To The Item_Pic Folder.
            boolean success = oriFileName.renameTo(newFile); //Item Picture File will be Renamed after copying.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteItemData(String filepath, String removeterm, int positionOfTerm, String delimiter) throws IOException {

        int pos = positionOfTerm - 1; // Integer Value Assigned To Choose Which Column in a Line To Be Selected for Deletion.
        String tempoFile = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\tempo.txt"; //Creates A Temporary File to Store the Updated itemlist.txt File.
        File oldFile = new File(filepath);
        File newFile = new File(tempoFile);
        String readData;
        String data[];

        try {

            FileWriter fw = new FileWriter(tempoFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            while ((readData = br.readLine()) != null) { //Data in itemlist txt Database Will Be Inserted into Data Array.
                data = readData.split(":");
                if (!(data[pos].equalsIgnoreCase(removeterm))) { //Identify Which Line Contains the removeterm Value.
                    pw.println(readData); //Writes to the tempo.txt File Without the Data of the Identified Line with the removeterm Value.
                }
            }

            pw.flush();
            pw.close();
            fr.close();
            br.close();
            bw.close();
            fw.close();

            oldFile.delete(); //Delete the old itemlist.txt File.
            File dump = new File(filepath);
            newFile.renameTo(dump); //Renames The tempo.txt File created to itemlist.txt.

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error has Occured while Deleting File!", "File Deletion Failure", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void deleteAdminData(String filepath, String removeterm, int positionOfTerm, String delimiter) throws IOException {
        int pos = positionOfTerm - 1; // Integer Value Assigned To Choose Which Column in a Line To Be Selected for Deletion.
        String tempoFile = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\tempo.txt"; //Creates A Temporary File to Store the Updated Admin.txt File.
        File oldFile = new File(filepath);
        File newFile = new File(tempoFile);
        String readData;
        String data[];

        try {

            FileWriter fw = new FileWriter(tempoFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            while ((readData = br.readLine()) != null) { //Data in Admin.txt Will Be Inserted into Data Array.
                data = readData.split(":");
                if (!(data[pos].equalsIgnoreCase(removeterm))) { //Identify Which Line Contains the removeterm Value.
                    pw.println(readData); //Writes to the tempo.txt File Without the Data of the Identified Line with the removeterm Value.
                }
            }

            pw.flush();
            pw.close();
            fr.close();
            br.close();
            bw.close();
            fw.close();

            oldFile.delete(); //Delete the old Admin.txt File.
            File dump = new File(filepath);
            newFile.renameTo(dump); //Renames The tempo.txt File created just now to Admin.txt.

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error has Occured while Deleting File!", "File Deletion Failure", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sortItemData(String filepath, String tofileName) {

        try { //To sort the txt Data File Alphabetically.

            Path path = Paths.get(filepath);
            Charset charset = Charset.forName("UTF-8");

            List<String> lines = Files.readAllLines(path, charset);
            Collections.sort(lines, new Sorter());

            Path toPath = Paths.get(tofileName);
            Files.write(toPath, lines, charset);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error has Occurred while Sorting!", "Item Sorting Failure", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Delete Item
    private void DeleteItem() throws IOException {

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Retrieving Directory of itemlist.txt File.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieving All Data itemlist.txt File.
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];

            try {
                if ("".equals(tfDelItemID.getText()) || "".equals(tfDelItemName.getText())) { //Check for Empty Fields.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill it Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                    HighlightEmptyFields();
                    DelItemEmptyField();
                } else if (!ItemName.equals(tfDelItemName.getText()) || (!ItemID.equals(tfDelItemID.getText()))) { //Check if Item Name and Item ID inputted exist in Database, No = X Delete Data.
                    if (ItemID.equals("06")) { //Conclude the search when it reaches last Item ID in itemlist.txt.
                        br.close();
                        JOptionPane.showMessageDialog(null, "Item Name or Item ID Provided is False, Please Try Again!", "Invalid Item Name or Item ID", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (ItemName.equals(tfDelItemName.getText()) && ItemID.equals(tfDelItemID.getText())) { //Checks if Item Name and Item ID inputted exist in Database, Yes = Delete Data.
                    br.close();
                    deleteItemData(saveDir, ItemID, 1, ":"); //Removes Line of Empty Data in Database.
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String DelItemName = "*"; //Assign Variables for User Inputs or What to Input into Database.
                    String DelItemPrice = "*";
                    String DelItemQuantity = "*";
                    String delItemData = ItemID + ":" + DelItemName + ":" + DelItemPrice + ":" + DelItemQuantity + ":" + "*" + "\n";
                    bw.write(delItemData); //Writes the Values From The Variables to the itemlist.txt File.
                    bw.close();
                    sortItemData(saveDir, saveDir); //Rearrange the Data Lines in Text File Alphabetically.
                    DeHighlightEmptyFields();
                    JOptionPane.showMessageDialog(null, "Successfully Deleted " + tfDelItemName.getText() + ".", "Delete Item Successful", JOptionPane.INFORMATION_MESSAGE);
                    tfDelItemID.setText(""); //Reset Form
                    tfDelItemName.setText("");
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Update Item
    private void UpdateItem() throws IOException {

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Retrieving Directory of itemlist.txt File.
        File file = new File(saveDir);
        FileReader fr2 = new FileReader(file);
        BufferedReader br2 = new BufferedReader(fr2);
        String record;

        while ((record = br2.readLine()) != null) { //Retrieving All Data itemlist.txt File.
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];
            String ItemNameregex = "^[A-Za-z_][A-Za-z0-9_]{4,9}$"; //Check if New Item Name inputted is within 4 to 10 characters.
            boolean ItemNamematch = tfUpItemName.getText().matches(ItemNameregex);
            String ItemPriceregex = "\\d{1,2}\\.\\d{1,2}"; //Check if New Item Price inputted is in the 0.00, 00.00 format.
            boolean ItemPricematch = tfUpItemPrice.getText().matches(ItemPriceregex);
            
            try {
                if (ItemName.equals(tfUpItemName.getText())) { //Check If Item Name Already Existed
                    br2.close();
                    JOptionPane.showMessageDialog(null, "Item Name Provided Already Exist, Please Try Something Else!", "Item Name Already Exist", JOptionPane.WARNING_MESSAGE);
                } else if("".equals(tfUpItemID.getText()) || "".equals(tfUpItemName.getText()) || "".equals(tfUpItemPrice.getText()) || "".equals(tfUpItemQuantity.getText())) { //Check for Empty Fields.
                    br2.close();
                    JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill it Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                    HighlightEmptyFields();
                    UpdateItemEmptyField();
                } else if (Integer.parseInt(tfUpItemQuantity.getText()) > 10) { //Check if Item Quantity Exceeded 10.
                    br2.close();
                    JOptionPane.showMessageDialog(null, "New Item Quantity Shouldn't Exceed 10", "Invalid Item Quantity", JOptionPane.WARNING_MESSAGE);
                } else if (!ItemNamematch) { //Check Item Name Regex.
                    br2.close();
                    JOptionPane.showMessageDialog(null, "New Item Name Should be Between 5 and 10 Characters!", "Invalid Item Name", JOptionPane.WARNING_MESSAGE);
                } else if (!ItemPricematch) { //Check Item Price Regex.
                    br2.close();
                    JOptionPane.showMessageDialog(null, "New Item Price Should be in the '0.00' or 00.00' format!", "Invalid Item Price", JOptionPane.WARNING_MESSAGE);
                } else if ("*".equals(tfUpItemName.getText()) || "*".equals(tfUpItemPrice.getText()) || "*".equals(tfUpItemQuantity.getText())) { //Check if Quantity, Item Price and Item Name inputs only contains * which is illegal.
                    br2.close();
                    JOptionPane.showMessageDialog(null, "Invalid New Item Name, Item Price or Item Quantity, Please Try Again!", "Invalid Inputs", JOptionPane.WARNING_MESSAGE);
                } else if (ItemID.equals(tfUpItemID.getText())) { //Checks if ItemID Matches Data in itemlist.txt, Yes = Update Data.
                    br2.close();
                    deleteItemData(saveDir, ItemID, 1, ":");
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String UpItemName = tfUpItemName.getText(); //Assign Variables Based on User Inputs.
                    String UpItemPrice = tfUpItemPrice.getText();
                    String UpItemQuantity = tfUpItemQuantity.getText();
                    String UpitemData = ItemID + ":" + UpItemName + ":" + UpItemPrice + ":" + UpItemQuantity + ":" + "ItemPic" + ItemID + ".png" + "\n";
                    bw.write(UpitemData); //Writes the Values From The Variables to the itemlist.txt File.
                    bw.close();
                    sortItemData(saveDir, saveDir); //Rearrange the data Lines in itemlist.txt Alphabetically.
                    String oldFileLocation = lblUpTemp.getText(); //Assigns Location of Selected Image to a Label to Send to insertImage() Method.
                    insertItemImage(ItemID, oldFileLocation); //Calls insertImage Method with values of product_id and oldFileLocation.
                    DeHighlightEmptyFields();
                    JOptionPane.showMessageDialog(null, "Successfully Updated " + tfUpItemName.getText() + ".", "Update Item Successful", JOptionPane.INFORMATION_MESSAGE);
                    tfUpItemID.setText(""); //Reset Form
                    tfUpItemName.setText("");
                    tfUpItemPrice.setText("");
                    tfUpItemQuantity.setText("");
                    lblUpTemp.setText("");
                    lblUpItemPicPrev.setIcon(null);
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void ShowUpdateDetails(String ItemSelectedID) throws IOException {
        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Retrieving Directory of itemlist.txt File.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieving All Data itemlist.txt File.
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];

            if (ItemSelectedID.equals(ItemID)) { //Display Item Details based on ItemID.
                br.close();
                tfUpItemID.setEnabled(false);
                tfUpItemID.setText(ItemID);
                tfUpItemName.setText(ItemName);
                tfUpItemPrice.setText(ItemPrice);
                tfUpItemQuantity.setText(ItemQuantity);
                String getSelectedImage = System.getProperty("user.dir") + "\\src\\Item_Pic\\" + ItemPic; //Directory for Image.
                BufferedImage BuffImageIco = ImageIO.read(new File(getSelectedImage)); //Creates Picture From The Image Icon.
                lblUpItemPicPrev.setIcon(new ImageIcon(BuffImageIco));
                break;
            }
        }
    }

    //Edit Password
    private void EditPassword() throws IOException {
        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\Admin.txt"; //Configure Directory to Admin.txt.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieving all Data from admin.txt.
            String[] fields = record.split(":");
            String AdminID = fields[0];
            String AdminUsername = fields[1];
            String AdminPassword = fields[2];
            String AdminName = fields[3];

            try {
                if ("".equals(tfEditUsername.getText()) || "".equals(String.valueOf(pfEditOldPassword.getPassword())) || "".equals(String.valueOf(pfEditNewPassword.getPassword()))) { //Check for Empty Fields.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill Them Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                    HighlightEmptyFields();
                    EditPwdEmptyField();
                } else if (!AdminUsername.equals(tfEditUsername.getText()) || !AdminPassword.equals(String.valueOf(pfEditOldPassword.getPassword()))) { //Check if Admin Username and Password exist in Database, No = X Edit Password.
                    if (AdminID.equals("10")) { //Conclude Search after reaches final AdminID.
                        br.close();
                        JOptionPane.showMessageDialog(null, "Admin Username or Old Admin Password Provided is False, Please Try Again!", "Invalid Admin Username or ID", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (String.valueOf(pfEditOldPassword.getPassword()).equals(String.valueOf(pfEditNewPassword.getPassword()))) { //Check if Old Password and New Password are the same.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Old Password and New Edit Password is the Same, Please Change!", "Same Old and New Password", JOptionPane.WARNING_MESSAGE);
                } else if (AdminUsername.equals(tfEditUsername.getText()) && AdminPassword.equals(String.valueOf(pfEditOldPassword.getPassword()))) { //Check If Admin Username and Password exist in Database,  Yes = Edit Password.
                    br.close();
                    deleteAdminData(saveDir, AdminID, 1, ":"); //Delete the Admin Data Line specified.
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String EditedAdminPassword = String.valueOf(pfEditNewPassword.getPassword()); //Assign Variables for the new password inputted.
                    String EditedAdminData = AdminID + ":" + AdminUsername + ":" + EditedAdminPassword + ":" + AdminName + "\n";
                    bw.write(EditedAdminData); //Writing the Variables containing the inputs into the Admin.txt.
                    bw.close();
                    sortItemData(saveDir, saveDir); //Reorganize the Admin Data Lines alphabetically.
                    DeHighlightEmptyFields();
                    JOptionPane.showMessageDialog(null, "Successfully Edited Password For Admin Account: " + AdminUsername + ".", "Password Edited Successful", JOptionPane.INFORMATION_MESSAGE);
                    tfEditUsername.setText(""); //Reset Form
                    pfEditOldPassword.setText("");
                    pfEditNewPassword.setText("");
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Delete Account
    private void DeleteAccount() throws IOException {

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\Admin.txt"; //Retrieving Directory of Admin.txt File.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieving All Data Admin.txt File.
            String[] fields = record.split(":");
            String AdminID = fields[0];
            String AdminUsername = fields[1];
            String AdminPassword = fields[2];
            String AdminRealName = fields[3];

            try {
                if ("".equals(tfDelID.getText()) || "".equals(tfDelUsername.getText()) || "".equals(String.valueOf(pfDelPassword.getPassword()))) { //Check for Empty Fields.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill it Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                    HighlightEmptyFields();
                    DelAccountEmptyField();
                } else if (!AdminID.equals(tfDelID.getText()) || !AdminUsername.equals(tfDelUsername.getText()) || !AdminPassword.equals(String.valueOf(pfDelPassword.getPassword()))) { //Check if Admin ID, Username and Password exist in Database, No = X Delete Account.
                    if (AdminID.equals("10")) { //Conclude Search after reaching Final Admin ID.
                        br.close();
                        JOptionPane.showMessageDialog(null, "Admin ID, Username or Password Provided is False, Please Try Again!", "Invalid Admin ID, Username or Password", JOptionPane.WARNING_MESSAGE);
                    }
                } else if ("*".equals(tfDelUsername.getText()) && "*".equals(String.valueOf(pfDelPassword.getPassword()))) { //Check if Inputs are * which is illegal, Exist = Error Msg.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Invalid Username and Password, Please Try Again!", "Invalid Username & password", JOptionPane.WARNING_MESSAGE);
                } else if (AdminID.equals(tfDelID.getText()) && AdminUsername.equals(tfDelUsername.getText()) && AdminPassword.equals(String.valueOf(pfDelPassword.getPassword()))) { //Check if Admin ID, Username and Password exist in Database, Yes = Delete Account.
                    br.close();
                    deleteAdminData(saveDir, AdminID, 1, ":"); //Delete Admin Data Line Specified.
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String DelAdminUsername = "*"; //Assign Variables to store Values to be Inserted into Admin.txt.
                    String DelAdminPassword = "*";
                    String DelAdminRealName = "*";
                    String delAccData = AdminID + ":" + DelAdminUsername + ":" + DelAdminPassword + ":" + DelAdminRealName + "\n";
                    bw.write(delAccData); //Writes the Variables containing inputs to the Admin.txt File.
                    bw.close();
                    sortItemData(saveDir, saveDir); //Rearrange the Data Lines in Admin.txt Alphabetically.
                    DeHighlightEmptyFields();
                    JOptionPane.showMessageDialog(null, "Successfully Deleted Account " + tfDelUsername.getText() + ".", "Delete Account Successful", JOptionPane.INFORMATION_MESSAGE);
                    tfDelUsername.setText(""); //Reset Form
                    pfDelPassword.setText("");
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Fetch Past Transaction
    private void getTransactionInfo() throws IOException {
        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\PastTransaction.txt"; //Set Directory for Transaction File.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        FileReader fr2 = new FileReader(file);
        BufferedReader br2 = new BufferedReader(fr2);
        String record;
        int Count = 0;
        int TotalLines = 0;

        while ((record = br2.readLine()) != null) {
            TotalLines++;
        }
        br2.close();

        while ((record = br.readLine()) != null) { //Retrieve Transaction Data.
            String[] fields = record.split("::");
            String PTransactionID = fields[0];
            String PTransactionDate = fields[1];
            String PTransactionTime = fields[2];
            String PTransactionTotal = fields[3];
            String PTransactionPaid = fields[4];
            String PTransactionChange = fields[5];
            String PTransactionItemID = fields[6];
            String PTransactionItemName = fields[7];
            String PTransactionItemQuantity = fields[8];
            String PTransactionItemPrice = fields[9];
            String PTransactionItemPic = fields[10];

            Count++;

            try {
                if ("".equals(tfPastTranID.getText())) { //Check for Empty Fields.
                    br.close();
                    JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill Them Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
                    HighlightEmptyFields();
                    PastTransactionEmptyField();
                } else if (!PTransactionID.equals(tfPastTranID.getText())) { //Check if Transaction ID Existed, No = Error Msg.
                    if (Count == TotalLines) {
                        br.close();
                        JOptionPane.showMessageDialog(null, "Transaction ID Provided is False, Please Try Again!", "Invalid Transaction ID", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (PTransactionID.equals(tfPastTranID.getText())) { //Check if Transaction ID Existed, Yes = Show Transaction Details.
                    br.close();
                    lblShowPTID.setText(PTransactionID);
                    lblShowPTDate.setText(PTransactionDate);
                    lblShowPTTime.setText(PTransactionTime);
                    lblShowPTTotal.setText(PTransactionTotal);
                    lblShowPTPaid.setText(PTransactionPaid);
                    lblShowPTChange.setText(PTransactionChange);
                    lblShowPTItemID.setText(PTransactionItemID);
                    lblShowPTItemName.setText(PTransactionItemName);
                    lblShowPTItemQuantity.setText(PTransactionItemQuantity);
                    lblShowPTItemPrice.setText(PTransactionItemPrice);
                    String getSelectedImage = System.getProperty("user.dir") + "\\src\\Item_Pic\\" + PTransactionItemPic; //Directory for Image.
                    BufferedImage BuffImageIco = ImageIO.read(new File(getSelectedImage)); //Creates Picture From The Image Icon.
                    lblShowPTItemPic.setIcon(new ImageIcon(BuffImageIco)); //Set Picture to Label.
                    DeHighlightEmptyFields();
                    JOptionPane.showMessageDialog(null, "Successfully Fetched Past Transaction: " + tfPastTranID.getText() + ".", "Successful Fetch", JOptionPane.INFORMATION_MESSAGE);
                    tfPastTranID.setText(""); //Reset Form
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//Fetch Item Data
    private void getItemData() throws IOException {
        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Directory for ItemList.txt.
        File file = new File(saveDir);

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;
        int itemcount = 1;

        while ((record = br.readLine()) != null) { //Retrieve Item Data.
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];

            if ("*".equals(ItemName)) { //Check for Empty Data.
                ItemName = "Empty Slot";
                ItemPrice = "UnAvailable";
                ItemQuantity = "UnAvailable";
                ItemPic = "comingsoon.png";
            } else if (ItemQuantity.equals("0")) { //Check for Empty Quantity.
                ItemQuantity = "Out of Stock";
            }
            setItemData(itemcount, ItemID, ItemName, ItemPrice, ItemQuantity, ItemPic); //Set Item Data

            ++itemcount;
        }
        br.close();
    }

    private void setItemData(int itemcount, String ItemID, String ItemName, String ItemPrice, String ItemQuantity, String ItemPic) throws IOException {
        String GetItemPic = System.getProperty("user.dir") + "\\src\\Item_Pic\\" + ItemPic; //Item Picture's Directory
        BufferedImage BuffImageIco = ImageIO.read(new File(GetItemPic)); //Get Items' Pictures

        switch (itemcount) { //Set Item Details to Dedicated Labels
            case 1:
                ItemID1.setText(ItemID);
                ItemName1.setText(ItemName);
                ItemPrice1.setText(ItemPrice);
                ItemQuantity1.setText(ItemQuantity);
                ItemPic1.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 2:
                ItemID2.setText(ItemID);
                ItemName2.setText(ItemName);
                ItemPrice2.setText(ItemPrice);
                ItemQuantity2.setText(ItemQuantity);
                ItemPic2.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 3:
                ItemID3.setText(ItemID);
                ItemName3.setText(ItemName);
                ItemPrice3.setText(ItemPrice);
                ItemQuantity3.setText(ItemQuantity);
                ItemPic3.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 4:
                ItemID4.setText(ItemID);
                ItemName4.setText(ItemName);
                ItemPrice4.setText(ItemPrice);
                ItemQuantity4.setText(ItemQuantity);
                ItemPic4.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 5:
                ItemID5.setText(ItemID);
                ItemName5.setText(ItemName);
                ItemPrice5.setText(ItemPrice);
                ItemQuantity5.setText(ItemQuantity);
                ItemPic5.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 6:
                ItemID6.setText(ItemID);
                ItemName6.setText(ItemName);
                ItemPrice6.setText(ItemPrice);
                ItemQuantity6.setText(ItemQuantity);
                ItemPic6.setIcon(new ImageIcon(BuffImageIco));
                break;
        }
    }

    //Logout
    private boolean endSession() { //Destroy Cache.txt while stores Admin ID and Username

        try {
            saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\Cache.txt";
            File cache = new File(saveDir);
            if (cache.exists()) {
                cache.delete();
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }

    //Get Username for Main Menu
    private void GetUsername() throws IOException { //Display Admin's Username and ID on Welcome Message at Main Menu

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\Cache.txt";
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieving All Data itemlist.txt File.
            String[] fields = record.split(":");
            String AdminID = fields[0];
            String AdminUsername = fields[1];

            lblWelcome.setText("WELCOME BACK " + AdminUsername.toUpperCase() + "_" + AdminID);

        }
        br.close();
        fr.close();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        Header = new javax.swing.JPanel();
        Header_Icon = new javax.swing.JPanel();
        btnMusic = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnMin = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnClose = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        VM_Name = new javax.swing.JPanel();
        btnMainMenu = new javax.swing.JPanel();
        showDashboard = new javax.swing.JLabel();
        Sidebar_Menu = new javax.swing.JPanel();
        Sidebar_Menu_Icon = new javax.swing.JPanel();
        lineHideMenu = new javax.swing.JPanel();
        HideMenu = new javax.swing.JPanel();
        btnHideMenu = new javax.swing.JLabel();
        lineSettings = new javax.swing.JPanel();
        Settings = new javax.swing.JPanel();
        btnSettings = new javax.swing.JLabel();
        lineAddItem = new javax.swing.JPanel();
        AddItem = new javax.swing.JPanel();
        btnAddItem = new javax.swing.JLabel();
        lineDeleteItem = new javax.swing.JPanel();
        DeleteItem = new javax.swing.JPanel();
        btnDeleteItem = new javax.swing.JLabel();
        linePastTransaction = new javax.swing.JPanel();
        PastTransaction = new javax.swing.JPanel();
        btnPastTransaction = new javax.swing.JLabel();
        lineDeleteAccount = new javax.swing.JPanel();
        DeleteAccount = new javax.swing.JPanel();
        btnDeleteAccount = new javax.swing.JLabel();
        lineLogout = new javax.swing.JPanel();
        Logout = new javax.swing.JPanel();
        btnLogout = new javax.swing.JLabel();
        Sidebar_Menu_Hide = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        Dashboard = new javax.swing.JPanel();
        JP_Registration = new javax.swing.JPanel();
        lblAddUsername = new javax.swing.JLabel();
        lblAddPassword = new javax.swing.JLabel();
        lblAddName = new javax.swing.JLabel();
        tfAddUsername = new javax.swing.JTextField();
        pfAddPassword = new javax.swing.JPasswordField();
        tfAddName = new javax.swing.JTextField();
        btnRegister = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblEditPassword = new javax.swing.JLabel();
        JP_AddItem = new javax.swing.JPanel();
        lblAddItemName = new javax.swing.JLabel();
        lblAddItemPrice = new javax.swing.JLabel();
        lblAddItemPic = new javax.swing.JLabel();
        btnAddItemPic = new javax.swing.JButton();
        tfAddItemPrice = new javax.swing.JTextField();
        tfAddItemName = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lblItemPic = new javax.swing.JLabel();
        lblTemp = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        JP_DeleteItem = new javax.swing.JPanel();
        lblDelItemID = new javax.swing.JLabel();
        lblDelItemName = new javax.swing.JLabel();
        tfDelItemID = new javax.swing.JTextField();
        tfDelItemName = new javax.swing.JTextField();
        btnDelItem = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        JP_EditPwd = new javax.swing.JPanel();
        lblEditUsername = new javax.swing.JLabel();
        lblEditNewPassword = new javax.swing.JLabel();
        tfEditUsername = new javax.swing.JTextField();
        btnEditPassword = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        pfEditNewPassword = new javax.swing.JPasswordField();
        lblEditOldPassword = new javax.swing.JLabel();
        pfEditOldPassword = new javax.swing.JPasswordField();
        JP_DeleteAccount = new javax.swing.JPanel();
        lblDelUsername = new javax.swing.JLabel();
        lblDelPassword = new javax.swing.JLabel();
        tfDelUsername = new javax.swing.JTextField();
        btnDelAcc = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        pfDelPassword = new javax.swing.JPasswordField();
        tfDelID = new javax.swing.JTextField();
        lblDelID = new javax.swing.JLabel();
        JP_MainMenu = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        Item3 = new javax.swing.JPanel();
        ItemPic3 = new javax.swing.JLabel();
        ItemName3 = new javax.swing.JLabel();
        ItemPrice3 = new javax.swing.JLabel();
        ItemQuantity3 = new javax.swing.JLabel();
        ItemID3 = new javax.swing.JLabel();
        Item2 = new javax.swing.JPanel();
        ItemPic2 = new javax.swing.JLabel();
        ItemName2 = new javax.swing.JLabel();
        ItemPrice2 = new javax.swing.JLabel();
        ItemQuantity2 = new javax.swing.JLabel();
        ItemID2 = new javax.swing.JLabel();
        Item1 = new javax.swing.JPanel();
        ItemPic1 = new javax.swing.JLabel();
        ItemName1 = new javax.swing.JLabel();
        ItemPrice1 = new javax.swing.JLabel();
        ItemQuantity1 = new javax.swing.JLabel();
        ItemID1 = new javax.swing.JLabel();
        Item4 = new javax.swing.JPanel();
        ItemPic4 = new javax.swing.JLabel();
        ItemName4 = new javax.swing.JLabel();
        ItemPrice4 = new javax.swing.JLabel();
        ItemQuantity4 = new javax.swing.JLabel();
        ItemID4 = new javax.swing.JLabel();
        Item5 = new javax.swing.JPanel();
        ItemPic5 = new javax.swing.JLabel();
        ItemName5 = new javax.swing.JLabel();
        ItemPrice5 = new javax.swing.JLabel();
        ItemQuantity5 = new javax.swing.JLabel();
        ItemID5 = new javax.swing.JLabel();
        Item6 = new javax.swing.JPanel();
        ItemPic6 = new javax.swing.JLabel();
        ItemName6 = new javax.swing.JLabel();
        ItemPrice6 = new javax.swing.JLabel();
        ItemQuantity6 = new javax.swing.JLabel();
        ItemID6 = new javax.swing.JLabel();
        JP_UpdateItem = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jlWelcome1 = new javax.swing.JLabel();
        lblUpItemName = new javax.swing.JLabel();
        lblUpItemPrice = new javax.swing.JLabel();
        lblUpItemQuantity = new javax.swing.JLabel();
        lblUpItemPic = new javax.swing.JLabel();
        btnUpItemPic = new javax.swing.JButton();
        tfUpItemQuantity = new javax.swing.JTextField();
        tfUpItemPrice = new javax.swing.JTextField();
        tfUpItemName = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        lblUpItemPicPrev = new javax.swing.JLabel();
        lblUpTemp = new javax.swing.JLabel();
        lblUpItemID = new javax.swing.JLabel();
        tfUpItemID = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        JP_PastTransaction = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblShowPTID = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblShowPTDate = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblShowPTTime = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblShowPTTotal = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblShowPTPaid = new javax.swing.JLabel();
        lblShowPTChange = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblTransactionID = new javax.swing.JLabel();
        tfPastTranID = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        lblShowPTItemPic = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblShowPTItemID = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblShowPTItemName = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lblShowPTItemQuantity = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        lblShowPTItemPrice = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTextField3.setText("jTextField3");

        jRadioButton1.setText("jRadioButton1");

        jLabel14.setText("jLabel14");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        Header.setBackground(new java.awt.Color(0, 0, 0));
        Header.setPreferredSize(new java.awt.Dimension(800, 50));
        Header.setLayout(new java.awt.BorderLayout());

        Header_Icon.setBackground(new java.awt.Color(0, 0, 0));
        Header_Icon.setPreferredSize(new java.awt.Dimension(150, 50));
        Header_Icon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnMusic.setBackground(new java.awt.Color(0, 0, 0));
        btnMusic.setPreferredSize(new java.awt.Dimension(50, 50));
        btnMusic.setLayout(new java.awt.BorderLayout());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnMusic.add(jLabel3, java.awt.BorderLayout.CENTER);

        Header_Icon.add(btnMusic, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btnMin.setBackground(new java.awt.Color(0, 0, 0));
        btnMin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMinMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinMouseExited(evt);
            }
        });
        btnMin.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/minus_32px.png"))); // NOI18N
        btnMin.add(jLabel2, java.awt.BorderLayout.CENTER);

        Header_Icon.add(btnMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 50, 50));

        btnClose.setBackground(new java.awt.Color(0, 0, 0));
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCloseMouseExited(evt);
            }
        });
        btnClose.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/close_32px.png"))); // NOI18N
        btnClose.add(jLabel1, java.awt.BorderLayout.CENTER);

        Header_Icon.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 50, 50));

        Header.add(Header_Icon, java.awt.BorderLayout.LINE_END);

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        Header.add(jPanel4, java.awt.BorderLayout.LINE_START);

        VM_Name.setBackground(new java.awt.Color(0, 0, 0));
        VM_Name.setPreferredSize(new java.awt.Dimension(50, 50));
        VM_Name.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnMainMenu.setBackground(new java.awt.Color(0, 0, 0));
        btnMainMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMainMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMainMenuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMainMenuMouseExited(evt);
            }
        });

        showDashboard.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        showDashboard.setForeground(new java.awt.Color(255, 255, 255));
        showDashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        showDashboard.setText("D2M");

        javax.swing.GroupLayout btnMainMenuLayout = new javax.swing.GroupLayout(btnMainMenu);
        btnMainMenu.setLayout(btnMainMenuLayout);
        btnMainMenuLayout.setHorizontalGroup(
            btnMainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnMainMenuLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(showDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnMainMenuLayout.setVerticalGroup(
            btnMainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnMainMenuLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(showDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        VM_Name.add(btnMainMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, 50));

        Header.add(VM_Name, java.awt.BorderLayout.CENTER);

        getContentPane().add(Header, java.awt.BorderLayout.PAGE_START);

        Sidebar_Menu.setBackground(new java.awt.Color(0, 0, 0));
        Sidebar_Menu.setPreferredSize(new java.awt.Dimension(270, 450));
        Sidebar_Menu.setLayout(new java.awt.BorderLayout());

        Sidebar_Menu_Icon.setBackground(new java.awt.Color(0, 0, 0));
        Sidebar_Menu_Icon.setPreferredSize(new java.awt.Dimension(50, 450));
        Sidebar_Menu_Icon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lineHideMenu.setBackground(new java.awt.Color(0, 0, 0));
        lineHideMenu.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineHideMenuLayout = new javax.swing.GroupLayout(lineHideMenu);
        lineHideMenu.setLayout(lineHideMenuLayout);
        lineHideMenuLayout.setHorizontalGroup(
            lineHideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineHideMenuLayout.setVerticalGroup(
            lineHideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Sidebar_Menu_Icon.add(lineHideMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 10));

        HideMenu.setBackground(new java.awt.Color(0, 0, 0));
        HideMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnHideMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnHideMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/back_32px.png"))); // NOI18N
        btnHideMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHideMenuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHideMenuMouseExited(evt);
            }
        });
        HideMenu.add(btnHideMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        Sidebar_Menu_Icon.add(HideMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 50, 50));

        lineSettings.setBackground(new java.awt.Color(0, 0, 0));
        lineSettings.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineSettingsLayout = new javax.swing.GroupLayout(lineSettings);
        lineSettings.setLayout(lineSettingsLayout);
        lineSettingsLayout.setHorizontalGroup(
            lineSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineSettingsLayout.setVerticalGroup(
            lineSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Sidebar_Menu_Icon.add(lineSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 50, 10));

        Settings.setBackground(new java.awt.Color(0, 0, 0));
        Settings.setPreferredSize(new java.awt.Dimension(50, 50));
        Settings.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSettings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/settings_32px.png"))); // NOI18N
        btnSettings.setPreferredSize(new java.awt.Dimension(50, 50));
        btnSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSettingsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSettingsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSettingsMouseExited(evt);
            }
        });
        Settings.add(btnSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        Sidebar_Menu_Icon.add(Settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 50, 50));

        lineAddItem.setBackground(new java.awt.Color(0, 0, 0));
        lineAddItem.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineAddItemLayout = new javax.swing.GroupLayout(lineAddItem);
        lineAddItem.setLayout(lineAddItemLayout);
        lineAddItemLayout.setHorizontalGroup(
            lineAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineAddItemLayout.setVerticalGroup(
            lineAddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Sidebar_Menu_Icon.add(lineAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, -1, 10));

        AddItem.setBackground(new java.awt.Color(0, 0, 0));
        AddItem.setPreferredSize(new java.awt.Dimension(50, 50));
        AddItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAddItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnAddItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/add_32px.png"))); // NOI18N
        btnAddItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddItemMouseExited(evt);
            }
        });
        AddItem.add(btnAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        Sidebar_Menu_Icon.add(AddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 50, 50));
        AddItem.getAccessibleContext().setAccessibleParent(Sidebar_Menu_Icon);

        lineDeleteItem.setBackground(new java.awt.Color(0, 0, 0));
        lineDeleteItem.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineDeleteItemLayout = new javax.swing.GroupLayout(lineDeleteItem);
        lineDeleteItem.setLayout(lineDeleteItemLayout);
        lineDeleteItemLayout.setHorizontalGroup(
            lineDeleteItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineDeleteItemLayout.setVerticalGroup(
            lineDeleteItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Sidebar_Menu_Icon.add(lineDeleteItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, -1, 10));

        DeleteItem.setBackground(new java.awt.Color(0, 0, 0));
        DeleteItem.setPreferredSize(new java.awt.Dimension(50, 50));
        DeleteItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDeleteItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDeleteItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete_32px.png"))); // NOI18N
        btnDeleteItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeleteItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeleteItemMouseExited(evt);
            }
        });
        DeleteItem.add(btnDeleteItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        Sidebar_Menu_Icon.add(DeleteItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 50, 50));

        linePastTransaction.setBackground(new java.awt.Color(0, 0, 0));
        linePastTransaction.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout linePastTransactionLayout = new javax.swing.GroupLayout(linePastTransaction);
        linePastTransaction.setLayout(linePastTransactionLayout);
        linePastTransactionLayout.setHorizontalGroup(
            linePastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        linePastTransactionLayout.setVerticalGroup(
            linePastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Sidebar_Menu_Icon.add(linePastTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, -1, 10));
        linePastTransaction.getAccessibleContext().setAccessibleParent(Sidebar_Menu_Icon);

        PastTransaction.setBackground(new java.awt.Color(0, 0, 0));
        PastTransaction.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPastTransaction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPastTransaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/past_32px.png"))); // NOI18N
        btnPastTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPastTransactionMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPastTransactionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPastTransactionMouseExited(evt);
            }
        });
        PastTransaction.add(btnPastTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        Sidebar_Menu_Icon.add(PastTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 50, 50));

        lineDeleteAccount.setBackground(new java.awt.Color(0, 0, 0));
        lineDeleteAccount.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineDeleteAccountLayout = new javax.swing.GroupLayout(lineDeleteAccount);
        lineDeleteAccount.setLayout(lineDeleteAccountLayout);
        lineDeleteAccountLayout.setHorizontalGroup(
            lineDeleteAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineDeleteAccountLayout.setVerticalGroup(
            lineDeleteAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Sidebar_Menu_Icon.add(lineDeleteAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, -1, 10));

        DeleteAccount.setBackground(new java.awt.Color(0, 0, 0));
        DeleteAccount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDeleteAccount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDeleteAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/fire_32px.png"))); // NOI18N
        btnDeleteAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteAccountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeleteAccountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeleteAccountMouseExited(evt);
            }
        });
        DeleteAccount.add(btnDeleteAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        Sidebar_Menu_Icon.add(DeleteAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 50, 50));

        lineLogout.setBackground(new java.awt.Color(0, 0, 0));
        lineLogout.setPreferredSize(new java.awt.Dimension(50, 5));

        javax.swing.GroupLayout lineLogoutLayout = new javax.swing.GroupLayout(lineLogout);
        lineLogout.setLayout(lineLogoutLayout);
        lineLogoutLayout.setHorizontalGroup(
            lineLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        lineLogoutLayout.setVerticalGroup(
            lineLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Sidebar_Menu_Icon.add(lineLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, -1, 10));

        Logout.setBackground(new java.awt.Color(0, 0, 0));
        Logout.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/logout_32px.png"))); // NOI18N
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoutMouseExited(evt);
            }
        });
        Logout.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        Sidebar_Menu_Icon.add(Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, -1, -1));

        Sidebar_Menu.add(Sidebar_Menu_Icon, java.awt.BorderLayout.LINE_START);
        Sidebar_Menu_Icon.getAccessibleContext().setAccessibleParent(Sidebar_Menu);

        Sidebar_Menu_Hide.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Register Admin");

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Add Item");

        jLabel7.setBackground(new java.awt.Color(204, 204, 204));
        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Delete Item");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Past Transaction");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Logout");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Delete Account");

        javax.swing.GroupLayout Sidebar_Menu_HideLayout = new javax.swing.GroupLayout(Sidebar_Menu_Hide);
        Sidebar_Menu_Hide.setLayout(Sidebar_Menu_HideLayout);
        Sidebar_Menu_HideLayout.setHorizontalGroup(
            Sidebar_Menu_HideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Sidebar_Menu_HideLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Sidebar_Menu_HideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        Sidebar_Menu_HideLayout.setVerticalGroup(
            Sidebar_Menu_HideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Sidebar_Menu_HideLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        Sidebar_Menu.add(Sidebar_Menu_Hide, java.awt.BorderLayout.CENTER);
        Sidebar_Menu_Hide.getAccessibleContext().setAccessibleParent(Sidebar_Menu);

        getContentPane().add(Sidebar_Menu, java.awt.BorderLayout.LINE_START);

        Dashboard.setBackground(new java.awt.Color(153, 153, 153));
        Dashboard.setPreferredSize(new java.awt.Dimension(270, 450));

        JP_Registration.setBackground(new java.awt.Color(255, 255, 204));

        lblAddUsername.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblAddUsername.setText("Username:");

        lblAddPassword.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblAddPassword.setText("Password:");

        lblAddName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblAddName.setText("Name:");

        tfAddUsername.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        pfAddPassword.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        tfAddName.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        btnRegister.setBackground(new java.awt.Color(51, 51, 51));
        btnRegister.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(255, 204, 153));
        btnRegister.setText("REGISTER");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Staff Registration");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 19, -1, -1));

        lblEditPassword.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lblEditPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEditPassword.setText("Want To Change Your Password?");
        lblEditPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditPasswordMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout JP_RegistrationLayout = new javax.swing.GroupLayout(JP_Registration);
        JP_Registration.setLayout(JP_RegistrationLayout);
        JP_RegistrationLayout.setHorizontalGroup(
            JP_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_RegistrationLayout.createSequentialGroup()
                .addGroup(JP_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_RegistrationLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(lblAddUsername)
                        .addGap(18, 18, 18)
                        .addComponent(tfAddUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_RegistrationLayout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(lblAddPassword)
                        .addGap(18, 18, 18)
                        .addComponent(pfAddPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_RegistrationLayout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(lblAddName)
                        .addGap(18, 18, 18)
                        .addComponent(tfAddName, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_RegistrationLayout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addGroup(JP_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(lblEditPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JP_RegistrationLayout.setVerticalGroup(
            JP_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_RegistrationLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(JP_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfAddUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddUsername))
                .addGap(21, 21, 21)
                .addGroup(JP_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pfAddPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddPassword))
                .addGap(20, 20, 20)
                .addGroup(JP_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfAddName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddName))
                .addGap(20, 20, 20)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEditPassword)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        JP_AddItem.setBackground(new java.awt.Color(204, 255, 204));

        lblAddItemName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblAddItemName.setText("Item Name:");

        lblAddItemPrice.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblAddItemPrice.setText("Item Price:");

        lblAddItemPic.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblAddItemPic.setText("Item Picture:");

        btnAddItemPic.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnAddItemPic.setText("Upload Picture");
        btnAddItemPic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemPicActionPerformed(evt);
            }
        });

        tfAddItemPrice.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        tfAddItemName.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        btnAdd.setBackground(new java.awt.Color(0, 0, 0));
        btnAdd.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(204, 255, 204));
        btnAdd.setText("ADD ITEM");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setPreferredSize(new java.awt.Dimension(398, 60));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Add Item");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblItemPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemPic.setMaximumSize(new java.awt.Dimension(250, 250));
        lblItemPic.setPreferredSize(new java.awt.Dimension(250, 250));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Uploaded Picture Format:");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 51));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Width = 83px");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 0, 51));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Height = 68px");

        javax.swing.GroupLayout JP_AddItemLayout = new javax.swing.GroupLayout(JP_AddItem);
        JP_AddItem.setLayout(JP_AddItemLayout);
        JP_AddItemLayout.setHorizontalGroup(
            JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
            .addGroup(JP_AddItemLayout.createSequentialGroup()
                .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(JP_AddItemLayout.createSequentialGroup()
                            .addGap(171, 171, 171)
                            .addComponent(lblAddItemName)
                            .addGap(18, 18, 18)
                            .addComponent(tfAddItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(JP_AddItemLayout.createSequentialGroup()
                            .addGap(177, 177, 177)
                            .addComponent(lblAddItemPrice)
                            .addGap(18, 18, 18)
                            .addComponent(tfAddItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(JP_AddItemLayout.createSequentialGroup()
                            .addGap(90, 90, 90)
                            .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblAddItemPic)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblItemPic, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnAddItemPic, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(lblTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(JP_AddItemLayout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JP_AddItemLayout.setVerticalGroup(
            JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_AddItemLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_AddItemLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblAddItemName))
                    .addComponent(tfAddItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_AddItemLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblAddItemPrice))
                    .addComponent(tfAddItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_AddItemLayout.createSequentialGroup()
                        .addComponent(btnAddItemPic, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addGroup(JP_AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JP_AddItemLayout.createSequentialGroup()
                                .addComponent(lblItemPic, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                .addGap(13, 13, 13))
                            .addGroup(JP_AddItemLayout.createSequentialGroup()
                                .addComponent(lblTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))
                    .addGroup(JP_AddItemLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lblAddItemPic)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel25)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        JP_DeleteItem.setBackground(new java.awt.Color(255, 204, 204));

        lblDelItemID.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblDelItemID.setText("Item ID:");

        lblDelItemName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblDelItemName.setText("Item Name:");

        tfDelItemID.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        tfDelItemName.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        btnDelItem.setBackground(new java.awt.Color(0, 0, 0));
        btnDelItem.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnDelItem.setForeground(new java.awt.Color(255, 204, 204));
        btnDelItem.setText("DELETE");
        btnDelItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelItemActionPerformed(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Delete Item");
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        javax.swing.GroupLayout JP_DeleteItemLayout = new javax.swing.GroupLayout(JP_DeleteItem);
        JP_DeleteItem.setLayout(JP_DeleteItemLayout);
        JP_DeleteItemLayout.setHorizontalGroup(
            JP_DeleteItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_DeleteItemLayout.createSequentialGroup()
                .addGroup(JP_DeleteItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_DeleteItemLayout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(lblDelItemID)
                        .addGap(18, 18, 18)
                        .addComponent(tfDelItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_DeleteItemLayout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(lblDelItemName)
                        .addGap(18, 18, 18)
                        .addComponent(tfDelItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_DeleteItemLayout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JP_DeleteItemLayout.setVerticalGroup(
            JP_DeleteItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_DeleteItemLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(JP_DeleteItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDelItemID)
                    .addComponent(tfDelItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JP_DeleteItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfDelItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDelItemName))
                .addGap(19, 19, 19)
                .addComponent(btnDelItem, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(206, Short.MAX_VALUE))
        );

        JP_EditPwd.setBackground(new java.awt.Color(204, 0, 204));

        lblEditUsername.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblEditUsername.setText("Username:");

        lblEditNewPassword.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblEditNewPassword.setText("New Password:");

        tfEditUsername.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        btnEditPassword.setBackground(new java.awt.Color(0, 0, 0));
        btnEditPassword.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnEditPassword.setForeground(new java.awt.Color(204, 0, 204));
        btnEditPassword.setText("EDIT");
        btnEditPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPasswordActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Edit Password");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(jLabel24)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel24))
        );

        lblEditOldPassword.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblEditOldPassword.setText("Old Password:");

        javax.swing.GroupLayout JP_EditPwdLayout = new javax.swing.GroupLayout(JP_EditPwd);
        JP_EditPwd.setLayout(JP_EditPwdLayout);
        JP_EditPwdLayout.setHorizontalGroup(
            JP_EditPwdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_EditPwdLayout.createSequentialGroup()
                .addGroup(JP_EditPwdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_EditPwdLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(lblEditUsername)
                        .addGap(18, 18, 18)
                        .addComponent(tfEditUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_EditPwdLayout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(btnEditPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_EditPwdLayout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addGroup(JP_EditPwdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JP_EditPwdLayout.createSequentialGroup()
                                .addComponent(lblEditOldPassword)
                                .addGap(18, 18, 18)
                                .addComponent(pfEditOldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JP_EditPwdLayout.createSequentialGroup()
                                .addComponent(lblEditNewPassword)
                                .addGap(18, 18, 18)
                                .addComponent(pfEditNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JP_EditPwdLayout.setVerticalGroup(
            JP_EditPwdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_EditPwdLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(JP_EditPwdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_EditPwdLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblEditUsername))
                    .addComponent(tfEditUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(JP_EditPwdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pfEditOldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEditOldPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JP_EditPwdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_EditPwdLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblEditNewPassword))
                    .addComponent(pfEditNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnEditPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        JP_DeleteAccount.setBackground(new java.awt.Color(51, 255, 0));

        lblDelUsername.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblDelUsername.setText("Username:");

        lblDelPassword.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblDelPassword.setText("Password:");

        tfDelUsername.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        btnDelAcc.setBackground(new java.awt.Color(0, 0, 0));
        btnDelAcc.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnDelAcc.setForeground(new java.awt.Color(0, 255, 0));
        btnDelAcc.setText("SUBMIT");
        btnDelAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelAccActionPerformed(evt);
            }
        });

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Delete Account");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(jLabel28)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel28))
        );

        tfDelID.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        lblDelID.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblDelID.setText("ID:");

        javax.swing.GroupLayout JP_DeleteAccountLayout = new javax.swing.GroupLayout(JP_DeleteAccount);
        JP_DeleteAccount.setLayout(JP_DeleteAccountLayout);
        JP_DeleteAccountLayout.setHorizontalGroup(
            JP_DeleteAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addComponent(lblDelID)
                .addGap(18, 18, 18)
                .addComponent(tfDelID, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addComponent(lblDelUsername)
                .addGap(18, 18, 18)
                .addComponent(tfDelUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(lblDelPassword)
                .addGap(18, 18, 18)
                .addComponent(pfDelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                .addGap(268, 268, 268)
                .addComponent(btnDelAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        JP_DeleteAccountLayout.setVerticalGroup(
            JP_DeleteAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(JP_DeleteAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblDelID))
                    .addComponent(tfDelID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(JP_DeleteAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblDelUsername))
                    .addComponent(tfDelUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(JP_DeleteAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_DeleteAccountLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblDelPassword))
                    .addComponent(pfDelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnDelAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        JP_MainMenu.setBackground(new java.awt.Color(255, 153, 0));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));

        lblWelcome.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("WELCOME BACK SIA");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblWelcome))
        );

        Item3.setBackground(new java.awt.Color(0, 0, 0));
        Item3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Item3MouseClicked(evt);
            }
        });

        ItemPic3.setBackground(new java.awt.Color(255, 255, 255));
        ItemPic3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ItemName3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemName3.setForeground(new java.awt.Color(255, 204, 0));
        ItemName3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemName3.setText("ItemName");

        ItemPrice3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemPrice3.setForeground(new java.awt.Color(255, 204, 0));
        ItemPrice3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemPrice3.setText("ItemPrice");

        ItemQuantity3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemQuantity3.setForeground(new java.awt.Color(255, 204, 0));
        ItemQuantity3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemQuantity3.setText("ItemQuantity");

        ItemID3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemID3.setForeground(new java.awt.Color(255, 204, 0));
        ItemID3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemID3.setText("ItemID");

        javax.swing.GroupLayout Item3Layout = new javax.swing.GroupLayout(Item3);
        Item3.setLayout(Item3Layout);
        Item3Layout.setHorizontalGroup(
            Item3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item3Layout.createSequentialGroup()
                .addGroup(Item3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Item3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(ItemPic3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(Item3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Item3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ItemID3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemPrice3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemName3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemQuantity3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        Item3Layout.setVerticalGroup(
            Item3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ItemPic3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ItemID3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ItemName3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemPrice3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemQuantity3)
                .addGap(21, 21, 21))
        );

        Item2.setBackground(new java.awt.Color(0, 0, 0));
        Item2.setForeground(new java.awt.Color(255, 204, 0));
        Item2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Item2MouseClicked(evt);
            }
        });

        ItemPic2.setBackground(new java.awt.Color(255, 255, 255));
        ItemPic2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ItemName2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemName2.setForeground(new java.awt.Color(255, 204, 0));
        ItemName2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemName2.setText("ItemName");

        ItemPrice2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemPrice2.setForeground(new java.awt.Color(255, 204, 0));
        ItemPrice2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemPrice2.setText("ItemPrice");

        ItemQuantity2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemQuantity2.setForeground(new java.awt.Color(255, 204, 0));
        ItemQuantity2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemQuantity2.setText("ItemQuantity");

        ItemID2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemID2.setForeground(new java.awt.Color(255, 204, 0));
        ItemID2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemID2.setText("ItemID");

        javax.swing.GroupLayout Item2Layout = new javax.swing.GroupLayout(Item2);
        Item2.setLayout(Item2Layout);
        Item2Layout.setHorizontalGroup(
            Item2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Item2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ItemID2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemPrice2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemName2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemQuantity2, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(Item2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(ItemPic2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Item2Layout.setVerticalGroup(
            Item2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ItemPic2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ItemID2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ItemName2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemPrice2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemQuantity2)
                .addGap(21, 21, 21))
        );

        Item1.setBackground(new java.awt.Color(0, 0, 0));
        Item1.setForeground(new java.awt.Color(255, 204, 0));
        Item1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Item1MouseClicked(evt);
            }
        });

        ItemPic1.setBackground(new java.awt.Color(255, 255, 255));
        ItemPic1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ItemName1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemName1.setForeground(new java.awt.Color(255, 204, 0));
        ItemName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemName1.setText("ItemName");

        ItemPrice1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemPrice1.setForeground(new java.awt.Color(255, 204, 0));
        ItemPrice1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemPrice1.setText("ItemPrice");

        ItemQuantity1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemQuantity1.setForeground(new java.awt.Color(255, 204, 0));
        ItemQuantity1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemQuantity1.setText("ItemQuantity");

        ItemID1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemID1.setForeground(new java.awt.Color(255, 204, 0));
        ItemID1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemID1.setText("ItemID");

        javax.swing.GroupLayout Item1Layout = new javax.swing.GroupLayout(Item1);
        Item1.setLayout(Item1Layout);
        Item1Layout.setHorizontalGroup(
            Item1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item1Layout.createSequentialGroup()
                .addGroup(Item1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Item1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(ItemPic1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(Item1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Item1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ItemID1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemPrice1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemQuantity1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        Item1Layout.setVerticalGroup(
            Item1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ItemPic1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ItemID1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ItemName1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemPrice1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemQuantity1)
                .addGap(21, 21, 21))
        );

        Item4.setBackground(new java.awt.Color(0, 0, 0));
        Item4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Item4MouseClicked(evt);
            }
        });

        ItemPic4.setBackground(new java.awt.Color(255, 255, 255));
        ItemPic4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ItemName4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemName4.setForeground(new java.awt.Color(255, 204, 0));
        ItemName4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemName4.setText("ItemName");

        ItemPrice4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemPrice4.setForeground(new java.awt.Color(255, 204, 0));
        ItemPrice4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemPrice4.setText("ItemPrice");

        ItemQuantity4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemQuantity4.setForeground(new java.awt.Color(255, 204, 0));
        ItemQuantity4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemQuantity4.setText("ItemQuantity");

        ItemID4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemID4.setForeground(new java.awt.Color(255, 204, 0));
        ItemID4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemID4.setText("ItemID");

        javax.swing.GroupLayout Item4Layout = new javax.swing.GroupLayout(Item4);
        Item4.setLayout(Item4Layout);
        Item4Layout.setHorizontalGroup(
            Item4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item4Layout.createSequentialGroup()
                .addGroup(Item4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Item4Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(ItemPic4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(Item4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Item4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ItemID4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemPrice4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemName4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemQuantity4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        Item4Layout.setVerticalGroup(
            Item4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ItemPic4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ItemID4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ItemName4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemPrice4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemQuantity4)
                .addGap(21, 21, 21))
        );

        Item5.setBackground(new java.awt.Color(0, 0, 0));
        Item5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Item5MouseClicked(evt);
            }
        });

        ItemPic5.setBackground(new java.awt.Color(255, 255, 255));
        ItemPic5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ItemName5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemName5.setForeground(new java.awt.Color(255, 204, 0));
        ItemName5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemName5.setText("ItemName");

        ItemPrice5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemPrice5.setForeground(new java.awt.Color(255, 204, 0));
        ItemPrice5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemPrice5.setText("ItemPrice");

        ItemQuantity5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemQuantity5.setForeground(new java.awt.Color(255, 204, 0));
        ItemQuantity5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemQuantity5.setText("ItemQuantity");

        ItemID5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemID5.setForeground(new java.awt.Color(255, 204, 0));
        ItemID5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemID5.setText("ItemID");

        javax.swing.GroupLayout Item5Layout = new javax.swing.GroupLayout(Item5);
        Item5.setLayout(Item5Layout);
        Item5Layout.setHorizontalGroup(
            Item5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item5Layout.createSequentialGroup()
                .addGroup(Item5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Item5Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(ItemPic5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(Item5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Item5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ItemID5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemPrice5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemName5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemQuantity5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        Item5Layout.setVerticalGroup(
            Item5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ItemPic5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ItemID5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ItemName5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemPrice5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemQuantity5)
                .addGap(21, 21, 21))
        );

        Item6.setBackground(new java.awt.Color(0, 0, 0));
        Item6.setForeground(new java.awt.Color(255, 204, 0));
        Item6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Item6MouseClicked(evt);
            }
        });

        ItemPic6.setBackground(new java.awt.Color(255, 255, 255));
        ItemPic6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        ItemName6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemName6.setForeground(new java.awt.Color(255, 204, 0));
        ItemName6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemName6.setText("ItemName");

        ItemPrice6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemPrice6.setForeground(new java.awt.Color(255, 204, 0));
        ItemPrice6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemPrice6.setText("ItemPrice");

        ItemQuantity6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemQuantity6.setForeground(new java.awt.Color(255, 204, 0));
        ItemQuantity6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemQuantity6.setText("ItemQuantity");

        ItemID6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemID6.setForeground(new java.awt.Color(255, 204, 0));
        ItemID6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemID6.setText("ItemID");

        javax.swing.GroupLayout Item6Layout = new javax.swing.GroupLayout(Item6);
        Item6.setLayout(Item6Layout);
        Item6Layout.setHorizontalGroup(
            Item6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item6Layout.createSequentialGroup()
                .addGroup(Item6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Item6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(ItemPic6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(Item6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Item6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ItemID6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemPrice6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemName6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemQuantity6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        Item6Layout.setVerticalGroup(
            Item6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Item6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ItemPic6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ItemID6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ItemName6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemPrice6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemQuantity6)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout JP_MainMenuLayout = new javax.swing.GroupLayout(JP_MainMenu);
        JP_MainMenu.setLayout(JP_MainMenuLayout);
        JP_MainMenuLayout.setHorizontalGroup(
            JP_MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_MainMenuLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(JP_MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_MainMenuLayout.createSequentialGroup()
                        .addComponent(Item1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(Item2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(Item3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_MainMenuLayout.createSequentialGroup()
                        .addComponent(Item4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(Item5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(Item6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JP_MainMenuLayout.setVerticalGroup(
            JP_MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_MainMenuLayout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(JP_MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Item1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(JP_MainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Item4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        JP_UpdateItem.setBackground(new java.awt.Color(0, 204, 204));

        jPanel13.setBackground(new java.awt.Color(51, 51, 51));

        jlWelcome1.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jlWelcome1.setForeground(new java.awt.Color(255, 255, 255));
        jlWelcome1.setText("UPDATE ITEM DETAILS");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jlWelcome1))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jlWelcome1))
        );

        lblUpItemName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblUpItemName.setText("Item Name:");

        lblUpItemPrice.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblUpItemPrice.setText("Item Price:");

        lblUpItemQuantity.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblUpItemQuantity.setText(" Quantity:");

        lblUpItemPic.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblUpItemPic.setText("Item Picture:");

        btnUpItemPic.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnUpItemPic.setText("Upload Picture");
        btnUpItemPic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpItemPicActionPerformed(evt);
            }
        });

        tfUpItemQuantity.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        tfUpItemPrice.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        tfUpItemName.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        btnUpdate.setBackground(new java.awt.Color(0, 0, 0));
        btnUpdate.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(0, 255, 204));
        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        lblUpItemPicPrev.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblUpItemID.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblUpItemID.setText("Item ID:");

        tfUpItemID.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Uploaded Picture Format:");

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 102));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Width = 83px");

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 102));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Height = 68px");

        javax.swing.GroupLayout JP_UpdateItemLayout = new javax.swing.GroupLayout(JP_UpdateItem);
        JP_UpdateItem.setLayout(JP_UpdateItemLayout);
        JP_UpdateItemLayout.setHorizontalGroup(
            JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(10, 10, 10)
                        .addComponent(lblUpItemPicPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(lblUpTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                                .addComponent(lblUpItemPrice)
                                .addGap(18, 18, 18)
                                .addComponent(tfUpItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                                .addComponent(lblUpItemQuantity)
                                .addGap(18, 18, 18)
                                .addComponent(tfUpItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                                .addComponent(lblUpItemName)
                                .addGap(18, 18, 18)
                                .addComponent(tfUpItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                                .addComponent(lblUpItemPic)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpItemPic, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                                .addComponent(lblUpItemID)
                                .addGap(18, 18, 18)
                                .addComponent(tfUpItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        JP_UpdateItemLayout.setVerticalGroup(
            JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblUpItemID))
                    .addComponent(tfUpItemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblUpItemName))
                    .addComponent(tfUpItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUpItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUpItemPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lblUpItemQuantity))
                    .addComponent(tfUpItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblUpItemPic))
                    .addComponent(btnUpItemPic))
                .addGap(11, 11, 11)
                .addGroup(JP_UpdateItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_UpdateItemLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel31)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel32))
                    .addComponent(lblUpItemPicPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUpTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        JP_PastTransaction.setBackground(new java.awt.Color(0, 102, 102));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setPreferredSize(new java.awt.Dimension(398, 60));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Past Transaction");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(jLabel26))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.setBackground(new java.awt.Color(0, 153, 153));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Transaction ID:");

        lblShowPTID.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTID.setText("xxxxxxx");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Transaction Date:");

        lblShowPTDate.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTDate.setText("xxxx/xx/xx");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Transaction Time:");

        lblShowPTTime.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTTime.setText("xx:xx:xx");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Transaction Total:");

        lblShowPTTotal.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTTotal.setText("RMxx.xx");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Transaction Paid:");

        lblShowPTPaid.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTPaid.setText("RMxx.xx");

        lblShowPTChange.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTChange.setText("RMxx.xx");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Transaction Change:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblShowPTID))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblShowPTDate))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblShowPTTime))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblShowPTTotal))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblShowPTPaid))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblShowPTChange)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblShowPTID))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblShowPTDate))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblShowPTTime))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblShowPTTotal))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(lblShowPTPaid))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(lblShowPTChange))
                .addContainerGap(158, Short.MAX_VALUE))
        );

        lblTransactionID.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblTransactionID.setForeground(new java.awt.Color(255, 255, 255));
        lblTransactionID.setText("Transaction ID:");

        tfPastTranID.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        btnSearch.setBackground(new java.awt.Color(0, 0, 0));
        btnSearch.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(0, 204, 153));
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jPanel14.setBackground(new java.awt.Color(0, 153, 102));

        lblShowPTItemPic.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTItemPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Item ID:");

        lblShowPTItemID.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTItemID.setText("xxx");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Item Name:");

        lblShowPTItemName.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTItemName.setText("XXXxxxxxxx");

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Item Quantity:");

        lblShowPTItemQuantity.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTItemQuantity.setText("xx");

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Item Price:");

        lblShowPTItemPrice.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblShowPTItemPrice.setText("RMxx.xx");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblShowPTItemPrice)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblShowPTItemID))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblShowPTItemName))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblShowPTItemQuantity)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(lblShowPTItemPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblShowPTItemPic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lblShowPTItemID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lblShowPTItemName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(lblShowPTItemQuantity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(lblShowPTItemPrice))
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout JP_PastTransactionLayout = new javax.swing.GroupLayout(JP_PastTransaction);
        JP_PastTransaction.setLayout(JP_PastTransactionLayout);
        JP_PastTransactionLayout.setHorizontalGroup(
            JP_PastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
            .addGroup(JP_PastTransactionLayout.createSequentialGroup()
                .addGroup(JP_PastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_PastTransactionLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(JP_PastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(JP_PastTransactionLayout.createSequentialGroup()
                                .addComponent(lblTransactionID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfPastTranID))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JP_PastTransactionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        JP_PastTransactionLayout.setVerticalGroup(
            JP_PastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JP_PastTransactionLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(JP_PastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JP_PastTransactionLayout.createSequentialGroup()
                        .addGroup(JP_PastTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JP_PastTransactionLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(lblTransactionID))
                            .addComponent(tfPastTranID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout DashboardLayout = new javax.swing.GroupLayout(Dashboard);
        Dashboard.setLayout(DashboardLayout);
        DashboardLayout.setHorizontalGroup(
            DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JP_Registration, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_AddItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_DeleteItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_EditPwd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_DeleteAccount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_MainMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_UpdateItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_PastTransaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DashboardLayout.setVerticalGroup(
            DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JP_Registration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_AddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_DeleteItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_EditPwd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_DeleteAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_MainMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_UpdateItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JP_PastTransaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JP_AddItem.getAccessibleContext().setAccessibleParent(Dashboard);
        JP_DeleteItem.getAccessibleContext().setAccessibleParent(Dashboard);
        JP_EditPwd.getAccessibleContext().setAccessibleParent(Dashboard);

        getContentPane().add(Dashboard, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(800, 500));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    //Icon and Panels Colours

    private void HighlightEmptyFields() { //Highlight Empty Fields' Labels.
        if ("".equals(tfAddUsername.getText())) {
            lblAddUsername.setForeground(euf);
        }

        if ("".equals(String.valueOf(pfAddPassword.getPassword()))) {
            lblAddPassword.setForeground(euf);
        }

        if ("".equals(tfAddName.getText())) {
            lblAddName.setForeground(euf);
        }

        if ("".equals(tfAddItemName.getText())) {
            lblAddItemName.setForeground(euf);
        }

        if ("".equals(tfAddItemPrice.getText())) {
            lblAddItemPrice.setForeground(euf);
        }

        if ("".equals(lblTemp.getText())) {
            lblAddItemPic.setForeground(euf);
        }

        if ("".equals(tfDelItemID.getText())) {
            lblDelItemID.setForeground(euf);
        }

        if ("".equals(tfDelItemName.getText())) {
            lblDelItemName.setForeground(euf);
        }

        if ("".equals(tfEditUsername.getText())) {
            lblEditUsername.setForeground(euf);
        }

        if ("".equals(String.valueOf(pfEditOldPassword.getPassword()))) {
            lblEditOldPassword.setForeground(euf);
        }

        if ("".equals(String.valueOf(pfEditNewPassword.getPassword()))) {
            lblEditNewPassword.setForeground(euf);
        }

        if ("".equals(tfDelID.getText())) {
            lblDelID.setForeground(euf);
        }

        if ("".equals(tfDelUsername.getText())) {
            lblDelUsername.setForeground(euf);
        }

        if ("".equals(String.valueOf(pfDelPassword.getPassword()))) {
            lblDelPassword.setForeground(euf);
        }

        if ("".equals(tfUpItemID.getText())) {
            lblUpItemID.setForeground(euf);
        }

        if ("".equals(tfUpItemName.getText())) {
            lblUpItemName.setForeground(euf);
        }

        if ("".equals(tfUpItemPrice.getText())) {
            lblUpItemPrice.setForeground(euf);
        }

        if ("".equals(tfUpItemQuantity.getText())) {
            lblUpItemQuantity.setForeground(euf);
        }
        
        if ("".equals(tfPastTranID.getText())) {
            lblTransactionID.setForeground(euf);
        }
    }

    private void DeHighlightEmptyFields() { //DeHighlight Empty Fields Labels.
        lblAddUsername.setForeground(neuf);
        lblAddPassword.setForeground(neuf);
        lblAddName.setForeground(neuf);
        lblAddItemName.setForeground(neuf);
        lblAddItemPrice.setForeground(neuf);
        lblAddItemPic.setForeground(neuf);
        lblDelItemID.setForeground(neuf);
        lblDelItemName.setForeground(neuf);
        lblEditUsername.setForeground(neuf);
        lblEditNewPassword.setForeground(neuf);
        lblDelID.setForeground(neuf);
        lblDelUsername.setForeground(neuf);
        lblDelPassword.setForeground(neuf);
        lblUpItemID.setForeground(neuf);
        lblUpItemName.setForeground(neuf);
        lblUpItemPrice.setForeground(neuf);
        lblUpItemQuantity.setForeground(neuf);
        lblItemPic.setForeground(neuf);
        lblTransactionID.setForeground(neuf);
    }

    public void changecolour(JPanel hover, Color rand) {
        hover.setBackground(rand);
    }

    public void clickmenu(JPanel h1, JPanel h2, JPanel h3, JPanel h4, JPanel h5, JPanel h6, JPanel h7, int numberbool) {
        if (numberbool == 1) {
            h1.setBackground(new Color(0, 0, 0));
            h2.setBackground(new Color(0, 0, 0));
        } else {
            h1.setBackground(new Color(0, 0, 0));
            h2.setBackground(new Color(0, 0, 0));
        }
    }

    public void changeIcon(JLabel button, String resourcheimg) { //Set Menu icon.
        ImageIcon aimg = new ImageIcon(getClass().getResource(resourcheimg));
        button.setIcon(aimg);
    }

    public void hideshow(JPanel menushowhide, boolean dashboard, JLabel button) { //Function for Set Close Icon to Menu Icon
        if (dashboard == true) {
            menushowhide.setPreferredSize(new Dimension(50, menushowhide.getHeight()));
            changeIcon(button, "/icon/menu_32px.png");
        } else {
            menushowhide.setPreferredSize(new Dimension(270, menushowhide.getHeight()));
            changeIcon(button, "/icon/back_32px.png");
        }
    }

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        if (JOptionPane.showConfirmDialog(null, "Are You Sure You Want to Log Out?", "Goodbye? :(", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Logout is Successful! Goodbye Now! :')", "Logout: Successful", JOptionPane.INFORMATION_MESSAGE);
            endSession(); //Destroy Cache File
            System.exit(0); //Exit 
        } else {
            //Remain in Admin Main Menu
        }
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseEntered
        changecolour(btnClose, new Color(220, 220, 220));
    }//GEN-LAST:event_btnCloseMouseEntered

    private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseExited
        changecolour(btnClose, new Color(0, 0, 0));
    }//GEN-LAST:event_btnCloseMouseExited

    private void btnMinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseEntered
        changecolour(btnMin, new Color(220, 220, 220));
    }//GEN-LAST:event_btnMinMouseEntered

    private void btnMinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseExited
        changecolour(btnMin, new Color(0, 0, 0));
    }//GEN-LAST:event_btnMinMouseExited

    private void btnMinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseClicked
        setState(AdminMainMenu.ICONIFIED); //Minimize
    }//GEN-LAST:event_btnMinMouseClicked

    private void btnHideMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideMenuMouseClicked
        clickmenu(HideMenu, Settings, AddItem, DeleteItem, PastTransaction, DeleteAccount, Logout, 1);
        //Hide and Show Menu Panel
        if (x == true) {
            hideshow(Sidebar_Menu, x, btnHideMenu);
            SwingUtilities.updateComponentTreeUI(this);
            x = false;
        } else {
            hideshow(Sidebar_Menu, x, btnHideMenu);
            SwingUtilities.updateComponentTreeUI(this);
            x = true;
        }
    }//GEN-LAST:event_btnHideMenuMouseClicked

    private void btnHideMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideMenuMouseEntered
        changecolour(lineHideMenu, new Color(247, 78, 105));
    }//GEN-LAST:event_btnHideMenuMouseEntered

    private void btnHideMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideMenuMouseExited
        changecolour(lineHideMenu, new Color(0, 0, 0));
    }//GEN-LAST:event_btnHideMenuMouseExited

    private void btnSettingsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettingsMouseExited
        changecolour(lineSettings, new Color(0, 0, 0));
    }//GEN-LAST:event_btnSettingsMouseExited

    private void btnSettingsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettingsMouseEntered
        changecolour(lineSettings, new Color(124, 252, 0));
    }//GEN-LAST:event_btnSettingsMouseEntered

    private void btnSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSettingsMouseClicked
        JP_Registration.setVisible(true);
        JP_AddItem.setVisible(false);
        JP_DeleteItem.setVisible(false);
        JP_EditPwd.setVisible(false);
        JP_DeleteAccount.setVisible(false);
        JP_MainMenu.setVisible(false);
        JP_UpdateItem.setVisible(false);
        JP_PastTransaction.setVisible(false);
    }//GEN-LAST:event_btnSettingsMouseClicked

    private void btnAddItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddItemMouseClicked
        JP_AddItem.setVisible(true);
        JP_DeleteItem.setVisible(false);
        JP_Registration.setVisible(false);
        JP_EditPwd.setVisible(false);
        JP_DeleteAccount.setVisible(false);
        JP_MainMenu.setVisible(false);
        JP_UpdateItem.setVisible(false);
        JP_PastTransaction.setVisible(false);
    }//GEN-LAST:event_btnAddItemMouseClicked

    private void btnAddItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddItemMouseEntered
        changecolour(lineAddItem, new Color(255, 215, 0));
    }//GEN-LAST:event_btnAddItemMouseEntered

    private void btnAddItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddItemMouseExited
        changecolour(lineAddItem, new Color(0, 0, 0));
    }//GEN-LAST:event_btnAddItemMouseExited

    private void btnDeleteItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteItemMouseEntered
        changecolour(lineDeleteItem, new Color(255, 0, 255));
    }//GEN-LAST:event_btnDeleteItemMouseEntered

    private void btnDeleteItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteItemMouseExited
        changecolour(lineDeleteItem, new Color(0, 0, 0));
    }//GEN-LAST:event_btnDeleteItemMouseExited

    private void btnDeleteItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteItemMouseClicked
        JP_DeleteItem.setVisible(true);
        JP_AddItem.setVisible(false);
        JP_Registration.setVisible(false);
        JP_EditPwd.setVisible(false);
        JP_DeleteAccount.setVisible(false);
        JP_MainMenu.setVisible(false);
        JP_UpdateItem.setVisible(false);
        JP_PastTransaction.setVisible(false);
    }//GEN-LAST:event_btnDeleteItemMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        if (JOptionPane.showConfirmDialog(null, "Are You Sure You Want to Log Out?", "Goodbye? :(", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Logout is Successful! Redirecting Back to Login Page", "Logout: Successful", JOptionPane.INFORMATION_MESSAGE);
            endSession();
            new AdminLogin().setVisible(true);
            this.dispose();
        } else {
            //Remain in Admin Main Menu
        }
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        changecolour(lineLogout, new Color(0, 255, 255));
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        changecolour(lineLogout, new Color(0, 0, 0));
    }//GEN-LAST:event_btnLogoutMouseExited

    private void btnPastTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastTransactionMouseClicked
        JP_PastTransaction.setVisible(true);
        JP_DeleteAccount.setVisible(false);
        JP_DeleteItem.setVisible(false);
        JP_AddItem.setVisible(false);
        JP_Registration.setVisible(false);
        JP_MainMenu.setVisible(false);
        JP_UpdateItem.setVisible(false);
        JP_EditPwd.setVisible(false);
    }//GEN-LAST:event_btnPastTransactionMouseClicked

    private void btnPastTransactionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastTransactionMouseEntered
        changecolour(linePastTransaction, new Color(51, 255, 153));
    }//GEN-LAST:event_btnPastTransactionMouseEntered

    private void btnPastTransactionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPastTransactionMouseExited
        changecolour(linePastTransaction, new Color(0, 0, 0));
    }//GEN-LAST:event_btnPastTransactionMouseExited

    private void btnDeleteAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteAccountMouseClicked
        JP_DeleteAccount.setVisible(true);
        JP_EditPwd.setVisible(false);
        JP_DeleteItem.setVisible(false);
        JP_AddItem.setVisible(false);
        JP_Registration.setVisible(false);
        JP_MainMenu.setVisible(false);
        JP_UpdateItem.setVisible(false);
        JP_PastTransaction.setVisible(false);
    }//GEN-LAST:event_btnDeleteAccountMouseClicked

    private void btnDeleteAccountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteAccountMouseEntered
        changecolour(lineDeleteAccount, new Color(255, 0, 127));
    }//GEN-LAST:event_btnDeleteAccountMouseEntered

    private void btnDeleteAccountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteAccountMouseExited
        changecolour(lineDeleteAccount, new Color(0, 0, 0));
    }//GEN-LAST:event_btnDeleteAccountMouseExited

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        try {
            addAdminInformation();
            DeHighlightEmptyFields();
            setVisible(false);
            try {
                new AdminMainMenu().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(AdminMainMenu.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnMainMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMenuMouseEntered
        changecolour(btnMainMenu, new Color(220, 220, 220));
    }//GEN-LAST:event_btnMainMenuMouseEntered

    private void btnMainMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMenuMouseExited
        changecolour(btnMainMenu, new Color(0, 0, 0));
    }//GEN-LAST:event_btnMainMenuMouseExited

    private void btnMainMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMenuMouseClicked
        try {
            this.setVisible(false);
            new AdminMainMenu().setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnMainMenuMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        try {
            InsertItem();
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnAddItemPicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemPicActionPerformed
        retrieveItemPicture();
    }//GEN-LAST:event_btnAddItemPicActionPerformed

    private void btnDelItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelItemActionPerformed
        try {
            DeleteItem();
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDelItemActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            UpdateItem();
            this.dispose();
            new AdminMainMenu().setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnUpItemPicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpItemPicActionPerformed
        retrieveUpItemPicture();
    }//GEN-LAST:event_btnUpItemPicActionPerformed

    private void btnEditPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPasswordActionPerformed
        try {
            EditPassword();
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEditPasswordActionPerformed

    private void btnDelAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelAccActionPerformed
        try {
            DeleteAccount();
            new AdminLogin().setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDelAccActionPerformed

    private void lblEditPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditPasswordMouseClicked
        JP_EditPwd.setVisible(true);
        JP_DeleteAccount.setVisible(false);
        JP_DeleteItem.setVisible(false);
        JP_AddItem.setVisible(false);
        JP_Registration.setVisible(false);
        JP_MainMenu.setVisible(false);
        JP_UpdateItem.setVisible(false);
        JP_PastTransaction.setVisible(false);
    }//GEN-LAST:event_lblEditPasswordMouseClicked

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try {
            getTransactionInfo();
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void Item1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item1MouseClicked
        try {
            if (ItemName1.getText().equals("Empty Slot")) {
                JOptionPane.showMessageDialog(null, "Empty Slot, Please try Something Else!", "Empty Slot", JOptionPane.WARNING_MESSAGE);
            } else {
                JP_MainMenu.setVisible(false);
                JP_UpdateItem.setVisible(true);
                ShowUpdateDetails("01");
            }
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item1MouseClicked

    private void Item2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item2MouseClicked
        try {
            if (ItemName2.getText().equals("Empty Slot")) {
                JOptionPane.showMessageDialog(null, "Empty Slot, Please try Something Else!", "Empty Slot", JOptionPane.WARNING_MESSAGE);
            } else {
                JP_MainMenu.setVisible(false);
                JP_UpdateItem.setVisible(true);
                ShowUpdateDetails("02");
            }
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item2MouseClicked

    private void Item3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item3MouseClicked
        try {
            if (ItemName1.getText().equals("Empty Slot")) {
                JOptionPane.showMessageDialog(null, "Empty Slot, Please try Something Else!", "Empty Slot", JOptionPane.WARNING_MESSAGE);
            } else {
                JP_MainMenu.setVisible(false);
                JP_UpdateItem.setVisible(true);
                ShowUpdateDetails("03");
            }
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item3MouseClicked

    private void Item4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item4MouseClicked
        try {
            if (ItemName1.getText().equals("Empty Slot")) {
                JOptionPane.showMessageDialog(null, "Empty Slot, Please try Something Else!", "Empty Slot", JOptionPane.WARNING_MESSAGE);
            } else {
                JP_MainMenu.setVisible(false);
                JP_UpdateItem.setVisible(true);
                ShowUpdateDetails("04");
            }
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item4MouseClicked

    private void Item5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item5MouseClicked
        try {
            if (ItemName1.getText().equals("Empty Slot")) {
                JOptionPane.showMessageDialog(null, "Empty Slot, Please try Something Else!", "Empty Slot", JOptionPane.WARNING_MESSAGE);
            } else {
                JP_MainMenu.setVisible(false);
                JP_UpdateItem.setVisible(true);
                ShowUpdateDetails("05");
            }
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item5MouseClicked

    private void Item6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item6MouseClicked
        try {            
            if (ItemName1.getText().equals("Empty Slot")) {
                JOptionPane.showMessageDialog(null, "Empty Slot, Please try Something Else!", "Empty Slot", JOptionPane.WARNING_MESSAGE);
            } else {
                JP_MainMenu.setVisible(false);
                JP_UpdateItem.setVisible(true);
                ShowUpdateDetails("06");
            }
        } catch (IOException ex) {
            Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item6MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminMainMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminMainMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminMainMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminMainMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AddItem;
    private javax.swing.JPanel Dashboard;
    private javax.swing.JPanel DeleteAccount;
    private javax.swing.JPanel DeleteItem;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel Header_Icon;
    private javax.swing.JPanel HideMenu;
    private javax.swing.JPanel Item1;
    private javax.swing.JPanel Item2;
    private javax.swing.JPanel Item3;
    private javax.swing.JPanel Item4;
    private javax.swing.JPanel Item5;
    private javax.swing.JPanel Item6;
    private javax.swing.JLabel ItemID1;
    private javax.swing.JLabel ItemID2;
    private javax.swing.JLabel ItemID3;
    private javax.swing.JLabel ItemID4;
    private javax.swing.JLabel ItemID5;
    private javax.swing.JLabel ItemID6;
    private javax.swing.JLabel ItemName1;
    private javax.swing.JLabel ItemName2;
    private javax.swing.JLabel ItemName3;
    private javax.swing.JLabel ItemName4;
    private javax.swing.JLabel ItemName5;
    private javax.swing.JLabel ItemName6;
    private javax.swing.JLabel ItemPic1;
    private javax.swing.JLabel ItemPic2;
    private javax.swing.JLabel ItemPic3;
    private javax.swing.JLabel ItemPic4;
    private javax.swing.JLabel ItemPic5;
    private javax.swing.JLabel ItemPic6;
    private javax.swing.JLabel ItemPrice1;
    private javax.swing.JLabel ItemPrice2;
    private javax.swing.JLabel ItemPrice3;
    private javax.swing.JLabel ItemPrice4;
    private javax.swing.JLabel ItemPrice5;
    private javax.swing.JLabel ItemPrice6;
    private javax.swing.JLabel ItemQuantity1;
    private javax.swing.JLabel ItemQuantity2;
    private javax.swing.JLabel ItemQuantity3;
    private javax.swing.JLabel ItemQuantity4;
    private javax.swing.JLabel ItemQuantity5;
    private javax.swing.JLabel ItemQuantity6;
    private javax.swing.JPanel JP_AddItem;
    private javax.swing.JPanel JP_DeleteAccount;
    private javax.swing.JPanel JP_DeleteItem;
    private javax.swing.JPanel JP_EditPwd;
    private javax.swing.JPanel JP_MainMenu;
    private javax.swing.JPanel JP_PastTransaction;
    private javax.swing.JPanel JP_Registration;
    private javax.swing.JPanel JP_UpdateItem;
    private javax.swing.JPanel Logout;
    private javax.swing.JPanel PastTransaction;
    private javax.swing.JPanel Settings;
    private javax.swing.JPanel Sidebar_Menu;
    private javax.swing.JPanel Sidebar_Menu_Hide;
    private javax.swing.JPanel Sidebar_Menu_Icon;
    private javax.swing.JPanel VM_Name;
    private javax.swing.JButton btnAdd;
    private javax.swing.JLabel btnAddItem;
    private javax.swing.JButton btnAddItemPic;
    private javax.swing.JPanel btnClose;
    private javax.swing.JButton btnDelAcc;
    private javax.swing.JButton btnDelItem;
    private javax.swing.JLabel btnDeleteAccount;
    private javax.swing.JLabel btnDeleteItem;
    private javax.swing.JButton btnEditPassword;
    private javax.swing.JLabel btnHideMenu;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JPanel btnMainMenu;
    private javax.swing.JPanel btnMin;
    private javax.swing.JPanel btnMusic;
    private javax.swing.JLabel btnPastTransaction;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel btnSettings;
    private javax.swing.JButton btnUpItemPic;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel jlWelcome1;
    private javax.swing.JLabel lblAddItemName;
    private javax.swing.JLabel lblAddItemPic;
    private javax.swing.JLabel lblAddItemPrice;
    private javax.swing.JLabel lblAddName;
    private javax.swing.JLabel lblAddPassword;
    private javax.swing.JLabel lblAddUsername;
    private javax.swing.JLabel lblDelID;
    private javax.swing.JLabel lblDelItemID;
    private javax.swing.JLabel lblDelItemName;
    private javax.swing.JLabel lblDelPassword;
    private javax.swing.JLabel lblDelUsername;
    private javax.swing.JLabel lblEditNewPassword;
    private javax.swing.JLabel lblEditOldPassword;
    private javax.swing.JLabel lblEditPassword;
    private javax.swing.JLabel lblEditUsername;
    private javax.swing.JLabel lblItemPic;
    private javax.swing.JLabel lblShowPTChange;
    private javax.swing.JLabel lblShowPTDate;
    private javax.swing.JLabel lblShowPTID;
    private javax.swing.JLabel lblShowPTItemID;
    private javax.swing.JLabel lblShowPTItemName;
    private javax.swing.JLabel lblShowPTItemPic;
    private javax.swing.JLabel lblShowPTItemPrice;
    private javax.swing.JLabel lblShowPTItemQuantity;
    private javax.swing.JLabel lblShowPTPaid;
    private javax.swing.JLabel lblShowPTTime;
    private javax.swing.JLabel lblShowPTTotal;
    private javax.swing.JLabel lblTemp;
    private javax.swing.JLabel lblTransactionID;
    private javax.swing.JLabel lblUpItemID;
    private javax.swing.JLabel lblUpItemName;
    private javax.swing.JLabel lblUpItemPic;
    private javax.swing.JLabel lblUpItemPicPrev;
    private javax.swing.JLabel lblUpItemPrice;
    private javax.swing.JLabel lblUpItemQuantity;
    private javax.swing.JLabel lblUpTemp;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel lineAddItem;
    private javax.swing.JPanel lineDeleteAccount;
    private javax.swing.JPanel lineDeleteItem;
    private javax.swing.JPanel lineHideMenu;
    private javax.swing.JPanel lineLogout;
    private javax.swing.JPanel linePastTransaction;
    private javax.swing.JPanel lineSettings;
    private javax.swing.JPasswordField pfAddPassword;
    private javax.swing.JPasswordField pfDelPassword;
    private javax.swing.JPasswordField pfEditNewPassword;
    private javax.swing.JPasswordField pfEditOldPassword;
    private javax.swing.JLabel showDashboard;
    private javax.swing.JTextField tfAddItemName;
    private javax.swing.JTextField tfAddItemPrice;
    private javax.swing.JTextField tfAddName;
    private javax.swing.JTextField tfAddUsername;
    private javax.swing.JTextField tfDelID;
    private javax.swing.JTextField tfDelItemID;
    private javax.swing.JTextField tfDelItemName;
    private javax.swing.JTextField tfDelUsername;
    private javax.swing.JTextField tfEditUsername;
    private javax.swing.JTextField tfPastTranID;
    private javax.swing.JTextField tfUpItemID;
    private javax.swing.JTextField tfUpItemName;
    private javax.swing.JTextField tfUpItemPrice;
    private javax.swing.JTextField tfUpItemQuantity;
    // End of variables declaration//GEN-END:variables
}
