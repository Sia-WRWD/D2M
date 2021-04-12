/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Staff.AdminLogin;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public class UserMainMenu extends javax.swing.JFrame {

    Integer SelectedItemQuantity = 1;
    String PassItemID;
    Double InitialTotal = 0.0, FinalTotal = 0.0; //Making InitialTotal and FinalTotal Double with 0.0 format.
    final DecimalFormat money = new DecimalFormat("0.00"); //Decimal Format for Total Price and Price-related Matter.
    String CurrentItemQuantity;
    
    /**
     * Creates new form UserMainMenu
     */
    public UserMainMenu() throws IOException {
        initComponents();
        getItemData();
        UpdateItemQuantity();
    }

    //Fetch Item Data
    private void getItemData() throws IOException {

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Directory to Itemlist.txt.
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

            if ("*".equals(ItemName)) { //Check for empty slots.
                ItemName = "Empty Slot";
                ItemPrice = "0.00";
                ItemQuantity = "UnAvailable";
                ItemPic = "comingsoon.png";
            } else if (ItemQuantity.equals("0")) { //Check for empty item quantity.
                ItemQuantity = "Out of Stock";
            }
            setMainMenuItemData(itemcount, ItemID, ItemName, ItemPrice, ItemQuantity, ItemPic); //SetItem to Designated Labels.

            ++itemcount;
        }
        br.close();
    }

    private void setMainMenuItemData(int itemcount, String ItemID, String ItemName, String ItemPrice, String ItemQuantity, String ItemPic) throws IOException {
        String GetItemPic = System.getProperty("user.dir") + "\\src\\Item_Pic\\" + ItemPic; //Directory to Item Pictures.
        BufferedImage BuffImageIco = ImageIO.read(new File(GetItemPic));

        switch (itemcount) { //Assign All Items to its designated Labels.
            case 1:
                ItemID1.setText(ItemID);
                ItemName1.setText(ItemName);
                ItemPrice1.setText("RM" + ItemPrice);
                ItemQuantity1.setText(ItemQuantity);
                ItemPic1.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 2:
                ItemID2.setText(ItemID);
                ItemName2.setText(ItemName);
                ItemPrice2.setText("RM" + ItemPrice);
                ItemQuantity2.setText(ItemQuantity);
                ItemPic2.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 3:
                ItemID3.setText(ItemID);
                ItemName3.setText(ItemName);
                ItemPrice3.setText("RM" + ItemPrice);
                ItemQuantity3.setText(ItemQuantity);
                ItemPic3.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 4:
                ItemID4.setText(ItemID);
                ItemName4.setText(ItemName);
                ItemPrice4.setText("RM" + ItemPrice);
                ItemQuantity4.setText(ItemQuantity);
                ItemPic4.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 5:
                ItemID5.setText(ItemID);
                ItemName5.setText(ItemName);
                ItemPrice5.setText("RM" + ItemPrice);
                ItemQuantity5.setText(ItemQuantity);
                ItemPic5.setIcon(new ImageIcon(BuffImageIco));
                break;
            case 6:
                ItemID6.setText(ItemID);
                ItemName6.setText(ItemName);
                ItemPrice6.setText("RM" + ItemPrice);
                ItemQuantity6.setText(ItemQuantity);
                ItemPic6.setIcon(new ImageIcon(BuffImageIco));
                break;
        }
    }

    //Fetch Selected Item Data
    private void retrieveItemData(String SelectedItemID) throws IOException {
        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Directory to ItemList.txt.
        File file = new File(saveDir);

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieve all Item Data.
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];

            if (ItemName.equals("*")) { //Check if There's empty slot.

                ItemName = "-";
                ItemPrice = "-";
                ItemQuantity = "-";
                ItemPic = "comingsoon.png";

            }

            if (SelectedItemID.equals(ItemID)) { //Check if SelectedItemID exist in database.
                String GetItemPic = System.getProperty("user.dir") + "\\src\\Item_Pic\\" + ItemPic;
                BufferedImage BuffImageIco = ImageIO.read(new File(GetItemPic));
                PassItemID = SelectedItemID; //Assigning Item Information for Item Selected and Shopping Cart labels.
                CurrentItemQuantity = ItemQuantity;
                jlItemPicSelected.setIcon(new ImageIcon(BuffImageIco));
                jlItemNameSelected.setText(ItemName);
                jlItemPriceSelected.setText("RM " + ItemPrice);
                InitialTotal = Double.parseDouble(ItemPrice);
                jlTotal.setText("RM " + ItemPrice);
            } else {

            }
        }
        br.close();
    }

    //Check for Empty Slots
    private void EmptySlotWarning(String SelectedItemID) throws IOException {

        String saveDir = System.getProperty("user.dir") + "\\src\\DB_txtfiles\\itemlist.txt"; //Directory for ItemList.txt
        File file = new File(saveDir);

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String record;

        while ((record = br.readLine()) != null) { //Retrieve Data from Itemlist.txt.
            String[] fields = record.split(":");
            String ItemID = fields[0];
            String ItemName = fields[1];
            String ItemPrice = fields[2];
            String ItemQuantity = fields[3];
            String ItemPic = fields[4];

            if (SelectedItemID.equals(ItemID) && ItemName.equals("*")) { //Check if ItemID exist and Data are * which represents empty slot.
                br.close();
                JOptionPane.showMessageDialog(null, "Item is UnAvailable, Please Try Something Else!", "Item UnAvailable", JOptionPane.WARNING_MESSAGE);
                ItemName = "-";
                ItemPrice = "-";
                ItemQuantity = "-";
                ItemPic = "comingsoon.png";
            }

            if (SelectedItemID.equals(ItemID) && ItemQuantity.equals("0")) { //Check if ItemID exist and Item Quantity is 0.
                br.close();
                JOptionPane.showMessageDialog(null, "Item is Out of Stock, Please Try Something Else!", "Item Out of Stock", JOptionPane.WARNING_MESSAGE);
            }
        }
        br.close();
    }

    //Update Item Quantity
    private void UpdateItemQuantity() { //Update Item Quantity and Final Total after Addition and Subtraction.
        jlItemQuantitySelected.setText(SelectedItemQuantity.toString());
        CalculateTotal();
    }
    
    //Add Item Quantity
    private void AddItemQuantity() throws IOException { //Add Item Quantity.

        if (SelectedItemQuantity >= Integer.parseInt(CurrentItemQuantity)) { //Prevent Purchasable Item Quantity from Available Item Quantity.
            JOptionPane.showMessageDialog(null, "Item Quantity Exceeded Available Stock. ", "Item Quantity UnAvailable", JOptionPane.WARNING_MESSAGE);

        } else {
            SelectedItemQuantity++;
            UpdateItemQuantity();
        }
    }

    //Minus Item Quantity
    private void MinusItemQuantity() { //Minus Item Quantity
        if (SelectedItemQuantity <= 1) { //Prevent Item Quantity from reaching 0.

            JOptionPane.showMessageDialog(null, "Item Quantity to Purchase Cannot be Zero!", "Item Quantity Null", JOptionPane.WARNING_MESSAGE);

        } else {
            SelectedItemQuantity--;
            UpdateItemQuantity();
        }
    }

    //Calculate Total
    private void CalculateTotal() { //Calculate the Final Total.
        FinalTotal = InitialTotal * SelectedItemQuantity;
        jlTotal.setText("RM" + money.format(FinalTotal));
    }

    //Generate Random Item Choice
    private void GenerateItemID() throws IOException {
        List<Integer> item = new ArrayList<>(); //Declare ArrayList for itemID.
        item.add(1); //ItemIDs in Int
        item.add(2);
        item.add(3);
        item.add(4);
        item.add(5);
        item.add(6);

        lblItemGenerated.setText("I Should Buy ItemID = '0" + String.valueOf(getRandomItemID(item)) + "'"); //Set Item ID generated to label.
    }

    private int getRandomItemID(List<Integer> item) { //Generate Random Item ID from the 6 declared.
        Random rand = new Random();
        return item.get(rand.nextInt(item.size()));
    }

    //Check for Empty Shopping Cart
    private void CheckShoppingCart() throws IOException {
        if (jlTotal.getText().equals("RMxxx.xx") || jlTotal.getText().equals("RM0.00")) {
            JOptionPane.showMessageDialog(null, "Please Add an Item Before Proceeding to CheckOut!", "Empty Shopping Cart", JOptionPane.WARNING_MESSAGE);
        } else {
            new CheckOut(PassItemID, SelectedItemQuantity, FinalTotal).setVisible(true);
            this.dispose();
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
        jPanel1 = new javax.swing.JPanel();
        VM_Name = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Content = new javax.swing.JPanel();
        PaymentContent = new javax.swing.JPanel();
        btnCheckOut = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jpCart = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jlItemNameSelected = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jlTotal = new javax.swing.JLabel();
        jlItemPriceSelected = new javax.swing.JLabel();
        jlItemQuantitySelected = new javax.swing.JLabel();
        jlItemPicSelected = new javax.swing.JLabel();
        btnAddQuantity = new javax.swing.JButton();
        btnMinusQuantity = new javax.swing.JButton();
        ItemContent = new javax.swing.JPanel();
        btnAdminLogin = new javax.swing.JButton();
        Item1 = new javax.swing.JPanel();
        ItemPic1 = new javax.swing.JLabel();
        ItemName1 = new javax.swing.JLabel();
        ItemPrice1 = new javax.swing.JLabel();
        ItemQuantity1 = new javax.swing.JLabel();
        ItemID1 = new javax.swing.JLabel();
        Item2 = new javax.swing.JPanel();
        ItemPic2 = new javax.swing.JLabel();
        ItemName2 = new javax.swing.JLabel();
        ItemPrice2 = new javax.swing.JLabel();
        ItemQuantity2 = new javax.swing.JLabel();
        ItemID2 = new javax.swing.JLabel();
        Item3 = new javax.swing.JPanel();
        ItemPic3 = new javax.swing.JLabel();
        ItemName3 = new javax.swing.JLabel();
        ItemPrice3 = new javax.swing.JLabel();
        ItemQuantity3 = new javax.swing.JLabel();
        ItemID3 = new javax.swing.JLabel();
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
        lblItemGenerated = new javax.swing.JLabel();
        btnGenerate = new javax.swing.JButton();

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

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        Header.add(jPanel1, java.awt.BorderLayout.LINE_START);

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

        Content.setBackground(new java.awt.Color(153, 153, 153));
        Content.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PaymentContent.setBackground(new java.awt.Color(51, 51, 51));
        PaymentContent.setPreferredSize(new java.awt.Dimension(200, 450));
        PaymentContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCheckOut.setBackground(new java.awt.Color(0, 0, 0));
        btnCheckOut.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnCheckOut.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckOut.setText("CHECKOUT");
        btnCheckOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckOutActionPerformed(evt);
            }
        });
        PaymentContent.add(btnCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 150, 40));

        btnClear.setBackground(new java.awt.Color(0, 0, 0));
        btnClear.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        PaymentContent.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 150, 40));

        jpCart.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel4.setText("Shopping Cart");

        jlItemNameSelected.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jlItemNameSelected.setForeground(new java.awt.Color(255, 255, 255));
        jlItemNameSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlItemNameSelected.setText("Itemxxxxxx");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 255, 204));
        jLabel10.setText("Total");

        jlTotal.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jlTotal.setForeground(new java.awt.Color(255, 153, 255));
        jlTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTotal.setText("RMxxx.xx");

        jlItemPriceSelected.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jlItemPriceSelected.setForeground(new java.awt.Color(255, 255, 255));
        jlItemPriceSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlItemPriceSelected.setText("RMxx.xx");

        jlItemQuantitySelected.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jlItemQuantitySelected.setForeground(new java.awt.Color(255, 255, 255));
        jlItemQuantitySelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlItemQuantitySelected.setText("x2");

        jlItemPicSelected.setBackground(new java.awt.Color(255, 255, 255));
        jlItemPicSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnAddQuantity.setBackground(new java.awt.Color(0, 0, 0));
        btnAddQuantity.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAddQuantity.setForeground(new java.awt.Color(51, 204, 0));
        btnAddQuantity.setText("+");
        btnAddQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddQuantityActionPerformed(evt);
            }
        });

        btnMinusQuantity.setBackground(new java.awt.Color(0, 0, 0));
        btnMinusQuantity.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnMinusQuantity.setForeground(new java.awt.Color(255, 0, 0));
        btnMinusQuantity.setText("-");
        btnMinusQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinusQuantityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCartLayout = new javax.swing.GroupLayout(jpCart);
        jpCart.setLayout(jpCartLayout);
        jpCartLayout.setHorizontalGroup(
            jpCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCartLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jlItemPicSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCartLayout.createSequentialGroup()
                .addGroup(jpCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpCartLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpCartLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlItemPriceSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlItemNameSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpCartLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnAddQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                    .addComponent(jlItemQuantitySelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMinusQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 2, Short.MAX_VALUE)))))
                .addContainerGap())
            .addComponent(jlTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpCartLayout.setVerticalGroup(
            jpCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCartLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlItemPicSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlItemNameSelected)
                .addGap(11, 11, 11)
                .addComponent(jlItemPriceSelected)
                .addGap(24, 24, 24)
                .addGroup(jpCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMinusQuantity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAddQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jlItemQuantitySelected, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(4, 4, 4)
                .addComponent(jlTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        PaymentContent.add(jpCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, 330));

        Content.add(PaymentContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 0, 210, -1));

        ItemContent.setBackground(new java.awt.Color(204, 204, 204));

        btnAdminLogin.setBackground(new java.awt.Color(0, 0, 0));
        btnAdminLogin.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnAdminLogin.setForeground(new java.awt.Color(255, 255, 0));
        btnAdminLogin.setText("A");
        btnAdminLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminLoginActionPerformed(evt);
            }
        });

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
                .addGap(14, 14, 14))
        );
        Item1Layout.setVerticalGroup(
            Item1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Item1Layout.createSequentialGroup()
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

        Item3.setBackground(new java.awt.Color(0, 0, 0));
        Item3.setForeground(new java.awt.Color(255, 204, 0));
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

        Item4.setBackground(new java.awt.Color(0, 0, 0));
        Item4.setForeground(new java.awt.Color(255, 204, 0));
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
                        .addGap(0, 0, Short.MAX_VALUE))
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
        Item5.setForeground(new java.awt.Color(255, 204, 0));
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
                .addContainerGap()
                .addGroup(Item6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ItemID6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemPrice6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemName6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemQuantity6, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(Item6Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(ItemPic6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        lblItemGenerated.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblItemGenerated.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItemGenerated.setText("I Should Buy ItemID = '??'");

        btnGenerate.setBackground(new java.awt.Color(0, 0, 0));
        btnGenerate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnGenerate.setForeground(new java.awt.Color(0, 204, 102));
        btnGenerate.setText("WHAT SHOULD I BUY?");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ItemContentLayout = new javax.swing.GroupLayout(ItemContent);
        ItemContent.setLayout(ItemContentLayout);
        ItemContentLayout.setHorizontalGroup(
            ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemContentLayout.createSequentialGroup()
                .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ItemContentLayout.createSequentialGroup()
                        .addComponent(btnAdminLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblItemGenerated, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGenerate))
                    .addGroup(ItemContentLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Item1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Item4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Item2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Item5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Item3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Item6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(23, 23, 23))
        );
        ItemContentLayout.setVerticalGroup(
            ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Item2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Item5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ItemContentLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btnAdminLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ItemContentLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(ItemContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblItemGenerated, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );

        Content.add(ItemContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 450));

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
        setState(UserMainMenu.ICONIFIED);
    }//GEN-LAST:event_btnMinMouseClicked

    private void btnAdminLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminLoginActionPerformed
        AdminLogin al = new AdminLogin();
        al.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAdminLoginActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        try {
            new UserMainMenu().setVisible(true);
            this.dispose();
            /*jlItemPicSelected.setIcon(null);
            jlItemNameSelected.setText("Itemxxxxxx");
            jlItemPriceSelected.setText("RMxx.xx");
            jlItemQuantitySelected.setText("-");
            jlTotal.setText("RMxxx.xx");*/
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCheckOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckOutActionPerformed
        try {
            CheckShoppingCart();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCheckOutActionPerformed

    private void Item1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item1MouseClicked
        try {
            EmptySlotWarning("01");
            retrieveItemData("01");
            UpdateItemQuantity();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item1MouseClicked

    private void Item2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item2MouseClicked
        try {
            EmptySlotWarning("02");
            retrieveItemData("02");
            UpdateItemQuantity();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item2MouseClicked

    private void Item3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item3MouseClicked
        try {
            EmptySlotWarning("03");
            retrieveItemData("03");
            UpdateItemQuantity();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item3MouseClicked

    private void Item4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item4MouseClicked
        try {
            EmptySlotWarning("04");
            retrieveItemData("04");
            UpdateItemQuantity();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item4MouseClicked

    private void Item5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item5MouseClicked
        try {
            EmptySlotWarning("05");
            retrieveItemData("05");
            UpdateItemQuantity();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item5MouseClicked

    private void Item6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Item6MouseClicked
        try {
            EmptySlotWarning("06");
            retrieveItemData("06");
            UpdateItemQuantity();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Item6MouseClicked

    private void btnAddQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddQuantityActionPerformed
        try {
            AddItemQuantity();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddQuantityActionPerformed

    private void btnMinusQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinusQuantityActionPerformed
        MinusItemQuantity();
    }//GEN-LAST:event_btnMinusQuantityActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        try {
            GenerateItemID();
        } catch (IOException ex) {
            Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGenerateActionPerformed

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
            java.util.logging.Logger.getLogger(UserMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new UserMainMenu().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(UserMainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel HeaderIcon;
    private javax.swing.JPanel Item1;
    private javax.swing.JPanel Item2;
    private javax.swing.JPanel Item3;
    private javax.swing.JPanel Item4;
    private javax.swing.JPanel Item5;
    private javax.swing.JPanel Item6;
    private javax.swing.JPanel ItemContent;
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
    private javax.swing.JPanel PaymentContent;
    private javax.swing.JPanel VM_Name;
    private javax.swing.JButton btnAddQuantity;
    private javax.swing.JButton btnAdminLogin;
    private javax.swing.JButton btnCheckOut;
    private javax.swing.JButton btnClear;
    private javax.swing.JPanel btnClose;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JPanel btnMin;
    private javax.swing.JButton btnMinusQuantity;
    private javax.swing.JPanel btnMusic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jlItemNameSelected;
    private javax.swing.JLabel jlItemPicSelected;
    private javax.swing.JLabel jlItemPriceSelected;
    private javax.swing.JLabel jlItemQuantitySelected;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpCart;
    private javax.swing.JLabel lblItemGenerated;
    // End of variables declaration//GEN-END:variables
}
