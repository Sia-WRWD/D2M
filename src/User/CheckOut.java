/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Staff.AdminMainMenu;
import Staff.Sorter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author chinojen7
 */
public class CheckOut extends javax.swing.JFrame {

    String saveDir;
    String SelectedItemID; //Imported Selected Item ID.
    String DisplayTotalPrice;
    int newTransactionID;
    String TransactionID;
    int SelectedItemQuantity; //Imported Selected Item Quantity.
    int CurrentItemQuantity;
    Double FinalTotal, FinalChange;
    final DecimalFormat money = new DecimalFormat("0.00");
    private final Color euf = new Color(255, 0, 0);
    private final Color neuf = new Color(0, 0, 0);

    /**
     * Creates new form UserMainMenu
     *
     * @param RetrievedItemID
     * @param RetrievedItemQuantity
     * @param FinalTotalAmount
     */
    public CheckOut(String RetrievedItemID, int RetrievedItemQuantity, Double FinalTotalAmount) throws IOException {
        initComponents();
        SelectedItemID = RetrievedItemID;
        SelectedItemQuantity = RetrievedItemQuantity;
        FinalTotal = FinalTotalAmount;
        RetrieveData(RetrievedItemID);
        DisplayTotalPrice();
        DisplayDate();
        DisplayTime();
        btnConfirm.setEnabled(false);
    }

    private void HighlightEmptyField() {
        if ("".equals(tfPay.getText())) {
            lblPay.setForeground(euf);
        }
    }

    private void DeHighlightEmptyField() {
        lblPay.setForeground(neuf);
    }

    //Fetch Item Data
    private void RetrieveData(String RetrievedItemID) throws IOException {
        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Directory to ItemList.txt.
        File file = new File(saveDir);

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieve Items Data.
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];

            if (ItemID.equals(RetrievedItemID)) { //Retrieve Item's Details based on SelectedItemID.
                String GetItemPic = System.getProperty("user.dir") + "\\src\\Item_Pic\\" + ItemPic; //Directory for Item Picture.
                BufferedImage BuffImageIco = ImageIO.read(new File(GetItemPic));
                lblRetrievedItemPic.setIcon(new ImageIcon(BuffImageIco));
                lblRetrievedItemName.setText(ItemName);
                lblRetrievedItemPrice.setText("RM" + ItemPrice);
                lblRetrievedItemQuantity.setText(String.valueOf(SelectedItemQuantity));
            } else {

            }
        }
        br.close();
    }

    //Display Total Price
    private void DisplayTotalPrice() { //Display Final Total.
        DisplayTotalPrice = money.format(FinalTotal);
        jlTotal.setText(DisplayTotalPrice);
    }

    //Display Local Date & Time
    private void DisplayDate() { 
        LocalDate Date = LocalDate.now();
        DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //Set Date Pattern.
        String FormattedDate = Date.format(DateFormat);
        LocalDate CorrectDate = LocalDate.parse(FormattedDate, DateFormat);
        lblDate.setText(String.valueOf(CorrectDate));
    }

    private void DisplayTime() {
        LocalTime Time = LocalTime.now();
        DateTimeFormatter TimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss"); //Set Time Pattern.
        String FormattedDate = Time.format(TimeFormat);
        LocalTime CorrectTime = LocalTime.parse(FormattedDate, TimeFormat);
        lblTime.setText(String.valueOf(CorrectTime));
    }

    //Calculate Change
    private void CalculateChange() { 
        String Payregex = "\\d{1,3}\\.\\d{1,2}"; //Check Change Input.
        boolean CustomerPaymatch = tfPay.getText().matches(Payregex);

        if (tfPay.getText().equals("")) { //Check Empty Fields.
            JOptionPane.showMessageDialog(null, "Empty Fields Detected, Please Fill Them Up!", "Empty Fields", JOptionPane.WARNING_MESSAGE);
            HighlightEmptyField();
        } else if (!CustomerPaymatch) { //Check Item Pay Regex.
            JOptionPane.showMessageDialog(null, "The Input Should be in X.XX, XX.XX, XXX.XX Format,Please Try Again!", "Wrong Input Format", JOptionPane.WARNING_MESSAGE);
            tfPay.setText("");
        } else if (FinalTotal > Double.parseDouble(tfPay.getText())) { //Check if Final Total value is higher than Final Pay, Yes = Error Msg, Invalid Payment value.
            JOptionPane.showMessageDialog(null, "Total Value is Higher than The Pay Value, Please Provide a Higher Value!", "Insufficient Pay Value", JOptionPane.WARNING_MESSAGE);
        } else if (Double.parseDouble(tfPay.getText()) >= FinalTotal) { //Check if Final Pay value is higher than Final Total, Yes = Execute Calculate Change.
            double FinalPay = Double.parseDouble(tfPay.getText()); //Assign Change Input to Variable FinalPay in Double Format.
            FinalChange = FinalPay - FinalTotal;
            money.format(FinalChange);
            jlChange.setText("RM" + money.format(FinalChange));
            btnConfirm.setEnabled(true);
            DeHighlightEmptyField();
            JOptionPane.showMessageDialog(null, "The Change Will Be: RM" + money.format(FinalChange), "Transaction Change", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Save Transaction - Add Transaction Incrementor
    private void TransactionIncrementor() throws IOException {
        String[] matchedID = null;
        boolean RecordExist = false;
        try {
            saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\PastTransaction.txt"; //Directory to PastTransaction.txt
            File PastTransactiontxt = new File(saveDir);
            if (!PastTransactiontxt.exists()) { //Check if File exist.
                PastTransactiontxt.createNewFile();
            }
            Scanner inputFile;
            try {
                inputFile = new Scanner(PastTransactiontxt);
                while (inputFile.hasNext()) { //Read File.
                    String entry = inputFile.nextLine();
                    matchedID = entry.split("::");
                    RecordExist = true;
                }
                inputFile.close();
                if (!RecordExist) { //Check if Transaction Record Exist.
                    JOptionPane.showMessageDialog(null, "No Transaction record was found! Restarting...", "Transaction DB is empty!", JOptionPane.ERROR_MESSAGE);
                    newTransactionID = 1;
                } else { //Create new Transaction ID.
                    newTransactionID = Integer.parseInt(matchedID[0]) + 1;
                }
            } catch (FileNotFoundException ex) {

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void SaveTransaction() throws IOException {

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\PastTransaction.txt"; //Directory to Past PastTransaction.txt.
        File file = new File(saveDir);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        DecimalFormat dc = new DecimalFormat("0000000"); //Decimal Format for Transaction ID.

        try {
            TransactionIncrementor();
            TransactionID = dc.format(newTransactionID); //Transaction ID to follow Decimal Format.

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String newTransactionDate = lblDate.getText(); //Assign Variables for Inputs..
            String newTransactionTime = lblTime.getText();
            String newTransactionTotal = "RM" + jlTotal.getText();
            String newTransactionPaid = "RM" + tfPay.getText();
            String newTransactionChange = jlChange.getText();
            String newTransactionItemID = SelectedItemID;
            String newTransactionItemName = lblRetrievedItemName.getText();
            String newTransactionItemQuantity = lblRetrievedItemQuantity.getText();
            String newTransactionItemPrice = lblRetrievedItemPrice.getText();
            String newTransactionItemPic = "ItemPic" + SelectedItemID + ".png";
            String TransactionData = TransactionID + "::" + newTransactionDate + "::" + newTransactionTime + "::"
                    + newTransactionTotal + "::" + newTransactionPaid + "::" + newTransactionChange
                    + "::" + newTransactionItemID + "::" + newTransactionItemName + "::"
                    + newTransactionItemQuantity + "::" + newTransactionItemPrice + "::"
                    + newTransactionItemPic + "\n";
            bw.write(TransactionData); //Writes the Values From The Variables to the PastTransaction.txt File.
            bw.close();
            sortItemData(saveDir, saveDir); //Rearrange the Past Transaction Data Lines in PastTransaction.txt Alphabetically.
            DeHighlightEmptyField();
        } catch (Exception ex) {
            Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sortItemData(String filepath, String tofileName) {

        try { //To sort the PastTransaction.txt & ItemList.txt alphabetically.

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

    //Minus Stock
    private void deleteItemData(String filepath, String removeterm, int positionOfTerm, String delimiter) throws IOException {

        int pos = positionOfTerm - 1; // Integer Value Assigned To Choose Which Column in a Line To Be Selected for Deletion.
        String tempoFile = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\tempo.txt"; //Creates A Temporary File Storing the Updated PastTransaction.txt File.
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

            while ((readData = br.readLine()) != null) { //Data in PastTransaction.txt Will Be Inserted into data Array.
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

            oldFile.delete(); //Delete the old PastTransaction.txt File.
            File dump = new File(filepath);
            newFile.renameTo(dump); //Renames The tempo.txt File created just now to PastTransaction.txt.

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An Error has Occured while Deleting File!", "File Deletion Failure", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void MinusItemStock() throws IOException {

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
                if (SelectedItemID.equals(ItemID)) { //Checks if SelectedItemID exists in the itemlist.txt.
                    br.close();
                    deleteItemData(saveDir, ItemID, 1, ":"); //Removes Line of Empty Data.
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    CurrentItemQuantity = Integer.parseInt(ItemQuantity); //Convert String to Integer.
                    String newItemQuantity = Integer.toString(CurrentItemQuantity - SelectedItemQuantity); //Minusing the Item Stocks and Converting to String.
                    String newItemData = ItemID + ":" + ItemName + ":" + ItemPrice + ":" + newItemQuantity + ":" + ItemPic + "\n";
                    bw.write(newItemData); //Writes the Values From The Variables to the itemlist.txt File.
                    bw.close();
                    sortItemData(saveDir, saveDir); //Rearrange the Item Data Lines in itemlist.txt Alphabetically.
                    JOptionPane.showMessageDialog(null, "Payment for " + lblRetrievedItemName.getText() + " is Successful." + "\n" + "Thank You and Please Come Again!" + "\n" + "Dispensing Your Item...", "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, "Dispensing Item: " + lblRetrievedItemName.getText() + " is Successful!" + "\n" + "Redirecting Back to Main Menu...", "Item Dispense Successful", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Header = new javax.swing.JPanel();
        HeaderIcon = new javax.swing.JPanel();
        btnClose = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnMin = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnMusic = new javax.swing.JPanel();
        btnBack = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        VM_Name = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Content = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jpItemList = new javax.swing.JPanel();
        lblRetrievedItemPic = new javax.swing.JLabel();
        lblRetrievedItemName = new javax.swing.JLabel();
        lblRetrievedItemPrice = new javax.swing.JLabel();
        lblRetrievedItemQuantity = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblPay = new javax.swing.JLabel();
        jlTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jlChange = new javax.swing.JLabel();
        tfPay = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        Header.setBackground(new java.awt.Color(0, 0, 0));
        Header.setPreferredSize(new java.awt.Dimension(800, 50));
        Header.setLayout(new java.awt.BorderLayout());

        HeaderIcon.setBackground(new java.awt.Color(0, 0, 0));
        HeaderIcon.setPreferredSize(new java.awt.Dimension(150, 50));
        HeaderIcon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/close_32px.png"))); // NOI18N

        javax.swing.GroupLayout btnCloseLayout = new javax.swing.GroupLayout(btnClose);
        btnClose.setLayout(btnCloseLayout);
        btnCloseLayout.setHorizontalGroup(
            btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        btnCloseLayout.setVerticalGroup(
            btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        HeaderIcon.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 50, 50));

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

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/minus_32px.png"))); // NOI18N

        javax.swing.GroupLayout btnMinLayout = new javax.swing.GroupLayout(btnMin);
        btnMin.setLayout(btnMinLayout);
        btnMinLayout.setHorizontalGroup(
            btnMinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        btnMinLayout.setVerticalGroup(
            btnMinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        HeaderIcon.add(btnMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 50, 50));

        btnMusic.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout btnMusicLayout = new javax.swing.GroupLayout(btnMusic);
        btnMusic.setLayout(btnMusicLayout);
        btnMusicLayout.setHorizontalGroup(
            btnMusicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        btnMusicLayout.setVerticalGroup(
            btnMusicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        HeaderIcon.add(btnMusic, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 50));

        Header.add(HeaderIcon, java.awt.BorderLayout.LINE_END);

        btnBack.setBackground(new java.awt.Color(0, 0, 0));
        btnBack.setPreferredSize(new java.awt.Dimension(50, 50));
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBackMouseExited(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/back_32px.png"))); // NOI18N

        javax.swing.GroupLayout btnBackLayout = new javax.swing.GroupLayout(btnBack);
        btnBack.setLayout(btnBackLayout);
        btnBackLayout.setHorizontalGroup(
            btnBackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        btnBackLayout.setVerticalGroup(
            btnBackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        Header.add(btnBack, java.awt.BorderLayout.LINE_START);

        VM_Name.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("D2M");

        javax.swing.GroupLayout VM_NameLayout = new javax.swing.GroupLayout(VM_Name);
        VM_Name.setLayout(VM_NameLayout);
        VM_NameLayout.setHorizontalGroup(
            VM_NameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VM_NameLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 508, Short.MAX_VALUE))
        );
        VM_NameLayout.setVerticalGroup(
            VM_NameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        Header.add(VM_Name, java.awt.BorderLayout.CENTER);

        getContentPane().add(Header, java.awt.BorderLayout.PAGE_START);

        Content.setBackground(new java.awt.Color(204, 204, 204));
        Content.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Times New Roman", 3, 48)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CheckOut");

        lblDate.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));

        lblTime.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        Content.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        jpItemList.setBackground(new java.awt.Color(0, 0, 0));

        lblRetrievedItemPic.setBackground(new java.awt.Color(255, 255, 255));
        lblRetrievedItemPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblRetrievedItemName.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblRetrievedItemName.setForeground(new java.awt.Color(255, 255, 255));
        lblRetrievedItemName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRetrievedItemName.setText("Itemxxxxxx");

        lblRetrievedItemPrice.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblRetrievedItemPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblRetrievedItemPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRetrievedItemPrice.setText("RMxx.xx");

        lblRetrievedItemQuantity.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblRetrievedItemQuantity.setForeground(new java.awt.Color(255, 255, 255));
        lblRetrievedItemQuantity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRetrievedItemQuantity.setText("x2");

        javax.swing.GroupLayout jpItemListLayout = new javax.swing.GroupLayout(jpItemList);
        jpItemList.setLayout(jpItemListLayout);
        jpItemListLayout.setHorizontalGroup(
            jpItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
            .addGroup(jpItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpItemListLayout.createSequentialGroup()
                    .addGap(98, 98, 98)
                    .addGroup(jpItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblRetrievedItemName, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpItemListLayout.createSequentialGroup()
                            .addGap(48, 48, 48)
                            .addComponent(lblRetrievedItemPic, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(lblRetrievedItemPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpItemListLayout.createSequentialGroup()
                            .addGap(59, 59, 59)
                            .addComponent(lblRetrievedItemQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(51, 51, 51)))
                    .addGap(99, 99, 99)))
        );
        jpItemListLayout.setVerticalGroup(
            jpItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jpItemListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpItemListLayout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(lblRetrievedItemPic, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblRetrievedItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(lblRetrievedItemPrice)
                    .addGap(24, 24, 24)
                    .addComponent(lblRetrievedItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(45, Short.MAX_VALUE)))
        );

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Total:");

        lblPay.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblPay.setText("Pay:");

        jlTotal.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jlTotal.setForeground(new java.awt.Color(255, 255, 255));
        jlTotal.setText("RMxxx.xx");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("Change:");

        jlChange.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jlChange.setForeground(new java.awt.Color(255, 255, 255));
        jlChange.setText("RMxx.xx");

        tfPay.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        tfPay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPayFocusLost(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/fun_resize.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jpItemList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlTotal))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPay)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlChange)
                            .addComponent(tfPay, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jlTotal))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPay)
                            .addComponent(tfPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jlChange))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jpItemList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Content.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 78, 800, 300));

        btnConfirm.setBackground(new java.awt.Color(0, 0, 0));
        btnConfirm.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirm.setText("CONFIRM & PAY");
        btnConfirm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConfirm.setPreferredSize(new java.awt.Dimension(100, 23));
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });
        Content.add(btnConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 385, 280, 60));

        getContentPane().add(Content, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(800, 500));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void changecolour(JPanel hover, Color rand) {
        hover.setBackground(rand);
    }

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        System.exit(0);
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
        setState(CheckOut.ICONIFIED);
    }//GEN-LAST:event_btnMinMouseClicked

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
        try {
            UserMainMenu umm;
            umm = new UserMainMenu();
            umm.setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBackMouseClicked

    private void btnBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseEntered
        changecolour(btnBack, new Color(220, 220, 220));
    }//GEN-LAST:event_btnBackMouseEntered

    private void btnBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseExited
        changecolour(btnBack, new Color(0, 0, 0));
    }//GEN-LAST:event_btnBackMouseExited

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        try {
            SaveTransaction();
            MinusItemStock();
        } catch (IOException ex) {
            Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            new UserMainMenu().setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void tfPayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPayFocusLost
        CalculateChange();
    }//GEN-LAST:event_tfPayFocusLost

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
            java.util.logging.Logger.getLogger(CheckOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CheckOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CheckOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CheckOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel HeaderIcon;
    private javax.swing.JPanel VM_Name;
    private javax.swing.JPanel btnBack;
    private javax.swing.JPanel btnClose;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JPanel btnMin;
    private javax.swing.JPanel btnMusic;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jlChange;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpItemList;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblPay;
    private javax.swing.JLabel lblRetrievedItemName;
    private javax.swing.JLabel lblRetrievedItemPic;
    private javax.swing.JLabel lblRetrievedItemPrice;
    private javax.swing.JLabel lblRetrievedItemQuantity;
    private javax.swing.JLabel lblTime;
    private javax.swing.JTextField tfPay;
    // End of variables declaration//GEN-END:variables
}
