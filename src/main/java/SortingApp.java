import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;



/**
 * SortingApp is desktop application that sort elements in real time.
 *
 * @author Kirill Osipenko
 */
public class SortingApp extends JFrame {

    private final JFrame mainFrame = new JFrame(); //main frame
    private JPanel buttonPanel;
    private JButton enterSizeButton; // button for enter size on main screen
    private JButton resetButton; //reset button on a sort screen
    private JButton sortButton;  // sort button on sort screen
    private JScrollPane buttonScrollPane;
    private JTextField sizeTextField; // field for enter number
    private JLabel enterSizeLabel; // label for main screen
    private boolean order = true;
    private int count; //number that input in main screen
    private ArrayList<Integer> listToSort; //list of int for sort
    private ArrayList<JButton> buttonList; //list of buttons for show on sort screen




    //custom layout manager for scroll pane

    /**
     * This class gives coordinates to buttons on sort screen according to the task.
     * Buttons are located in columns 10 elements each.
     */
    static class ColumnLayout implements LayoutManager {
        private final int vgap;
        int x;
        int y;
        private int minWidth = 0, minHeight = 0;
        private int preferredWidth = 0, preferredHeight = 0;

        public ColumnLayout() {
            this(5);
        }

        public ColumnLayout(int v) {
            vgap = v;
        }

        // Required by LayoutManager. /
        public void addLayoutComponent(String name, Component comp) {
        }

        // Required by LayoutManager. /
        public void removeLayoutComponent(Component comp) {
        }

        private void setSizes(Container parent) {
            int nComps = parent.getComponentCount();

            //Reset preferred/minimum width and height.
            preferredWidth = 750;
            preferredHeight = 0;
            minWidth = 750;
            minHeight = 0;

            for (int i = 0; i < nComps; i++) {
                Component c = parent.getComponent(i);
                if (c.isVisible()) {

                    if (i > 60 && i%10==0) {
                        preferredWidth += 85;
                        minWidth += 85;
                    }
                }
            }
        }

        // Required by LayoutManager. /

        public Dimension preferredLayoutSize(Container parent) {
            Dimension dim = new Dimension(0, 0);

            setSizes(parent);

            //Always add the container's insets!
            Insets insets = parent.getInsets();
            dim.width = preferredWidth
                    + insets.left + insets.right;
            dim.height = preferredHeight
                    + insets.top + insets.bottom;

            return dim;
        }


        public Dimension minimumLayoutSize(Container parent) {
            Dimension dim = new Dimension(0, 0);

            //Always add the container's insets!
            Insets insets = parent.getInsets();
            dim.width = minWidth
                    + insets.left + insets.right;
            dim.height = minHeight
                    + insets.top + insets.bottom;

            return dim;
        }

        public void layoutContainer(Container parent) {
            x = 10;
            y = 0;
            int nComps = parent.getComponentCount();
            for (int i = 0; i < nComps; i++) {
                Component c = parent.getComponent(i);
                if (i % 10 == 0&&i>1) {
                    x += 85;
                    y = 0;
                }
                c.setBounds(x, y += 40, 80, 30);
            }
        }


        public String toString() {
            String str = "";
            return getClass().getName() + "[vgap=" + vgap + str + "]";
        }
    }

    //update buttons in sort func

    /**
     * This method for update buttons text and ActionListener in buttonList that take part in sort.
     *
     * @param intList ArrayList of integer. Every element in intList is text on every button in buttonList.
     * @return ArrayList of JButton that shows on sort screen.
     */
    public ArrayList<JButton> updateButtons(ArrayList<Integer> intList) {
        for (int i = 0; i < intList.size(); i++) {
            buttonList.get(i).setText(String.valueOf(intList.get(i)));
            String temp = buttonList.get(i).getText();
            for( ActionListener al : buttonList.get(i).getActionListeners() ) {
                buttonList.get(i).removeActionListener( al );
            }
            buttonList.get(i).addActionListener(e -> {
                if (Integer.parseInt(temp) > 30) {
                    showMessageDialog(null, "Number must be less or equal 30", "Error", ERROR_MESSAGE);
                } else {
                    showSortScreen(initButtonsArray(createArray(Integer.parseInt(temp))));
                }
            });
        }
        return buttonList;
    }



    /**
     * This method initiate buttonList, add text on buttons, ActionListeners and add buttons to button panel.
     *
     * @param intList ArrayList of integer. Every element in intList is text on every button in buttonList.
     * @return ArrayList of JButton that shows on sort screen.
     */
    public List<JButton> initButtonsArray(ArrayList<Integer> intList) {
        resetFrame();
        order = true;
        if (intList != null) {
            buttonList = new ArrayList<>();
            for (int i = 0; i < intList.size(); i++) {
                buttonList.add(new JButton());
            }
            for (int i = 0; i < intList.size(); i++) {
                buttonList.get(i).setText(String.valueOf(intList.get(i)));
                String temp = buttonList.get(i).getText();
                buttonList.get(i).addActionListener(e -> {
                    if(Integer.parseInt(temp)>30){
                        showMessageDialog(null, "Number must be less or equal 30", "Error", ERROR_MESSAGE);
                    }else {
                        showSortScreen(initButtonsArray(createArray(Integer.parseInt(temp))));
                    }
                });
                buttonPanel.add(buttonList.get(i));
            }
            return buttonList;
        }
        return null;
    }



    /**
     * This method clears button panel, when you click on reset button.
     */
    public void resetFrame() { //func that delete buttons from frame, if we need to generate new buttons
        if (buttonList != null) {
            for (JButton jButton : buttonList) {
                buttonPanel.remove(jButton);
            }
        }
    }

    /**
     * This method creates integer ArrayList whose elements will be the text on buttons in buttonList that takes a part in sorting.
     *
     * @param size Size from main screen text field.
     * @return ArrayList of integer values.
     */
    //create integer array, elements of this array will be text of buttons
    public ArrayList<Integer> createArray(int size){
        if(size!=0){
            boolean isLower = false;
            listToSort = new ArrayList<>();
            Random rand = new Random();
            for(int i = 0; i < size; i++){
                this.listToSort.add(rand.nextInt(1000)+1);
            }
            for (Integer integer : listToSort) {
                if (integer <= 30) {
                    isLower = true;
                    break;
                }
            }
            if(!isLower){
                listToSort.set(rand.nextInt(listToSort.size()), rand.nextInt(30)+1);
            }
            return listToSort;
        }
        return null;
    }


    /**
     * This method starts second thread and animate sorting.
     */
    private void startThread() {
        sortButton.setEnabled(false);
        resetButton.setEnabled(false);
        for (JButton button: buttonList) {
            button.setEnabled(false);
        }
        new Thread(() -> {
            if(order){
                try {
                    sortingDesc(listToSort,0,listToSort.size()-1);
                    sortButton.setEnabled(true);
                    resetButton.setEnabled(true);
                    for (JButton button: buttonList) {
                        button.setEnabled(true);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                order = false;
            } else {
                try {
                    sortingIncr(listToSort,0,listToSort.size()-1);
                    sortButton.setEnabled(true);
                    resetButton.setEnabled(true);
                    for (JButton button: buttonList) {
                        button.setEnabled(true);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                order = true;
            }
        }).start();
    }



    //sort consist from 2 func partition and sort. there's 2 way to sort descending and increasing orders

    /**
     * This method sort elements with quicksort algorithm in increasing order.
     * @param list ArrayList of integers.
     * @param low head of array part that will sort.
     * @param high tail of array part that will sort.
     * @return sorted ArrayList in increasing order.
     * @throws InterruptedException Method executes in second thread.
     */
    public int partitionIncr(ArrayList<Integer> list, int low, int high) throws InterruptedException {
        int pivot = list.get(high);
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {
            if (list.get(j) < pivot)
            {
                i++;
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                Thread.sleep(500);
                showSortScreen(updateButtons(list));
            }
        }

        int temp = list.get(i+1);
        list.set(i+1, list.get(high));
        list.set(high, temp);
        Thread.sleep(500);
        showSortScreen(updateButtons(list));
        return (i + 1);
    }
    public void sortingIncr(ArrayList<Integer> list, int left, int right) throws InterruptedException {
        if(left < right)
        {
            int q = partitionIncr(list, left, right);
            sortingIncr(list, left, q-1);
            sortingIncr(list, q + 1, right);
        }
    }

    /**
     * This method sort elements with quicksort algorithm in descending order.
     * @param list ArrayList of integers.
     * @param low head of array part that will sort.
     * @param high tail of array part that will sort.
     * @return sorted ArrayList in descending order.
     * @throws InterruptedException Method executes in second thread.
     */
    public int partitionDesc(ArrayList<Integer> list, int low, int high) throws InterruptedException {
        int pivot = list.get(low);
        int i = low;
        for(int j = low + 1; j <= high; j++){
            if (list.get(j) > pivot){
                i = i + 1;
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                showSortScreen(updateButtons(list));
                Thread.sleep(500);
            }
        }
        int temp = list.get(i);
        list.set(i, list.get(low));
        list.set(low, temp);
        Thread.sleep(500);
        showSortScreen(updateButtons(list));
        return i;

    }
    public void sortingDesc(ArrayList<Integer> list, int left, int right) throws InterruptedException {
        if(left < right)
        {
            int q = partitionDesc(list, left, right);
            sortingDesc(list, left, q);
            sortingDesc(list, q + 1, right);
        }
    }




    /**
     * This method convert string from text field on main screen to integer and check it on empty field, input negative or zero number
     * and not integer value(letters and other not numeric symbols).
     *
     * @param text Text from text field on main screen.
     * @return Integer value that will be a count of buttons that takes part in sort.
     */
    public Integer checkKeyInputErrors(String text) {
        try {
            if (text.trim().length() == 0) {
                showMessageDialog(null, "Empty field", "Error", ERROR_MESSAGE);
                count = 0;
            } else if (Integer.parseInt(text) <= 0) {
                throw new NullPointerException();
            }else {
                count = Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            showMessageDialog(null, "Invalid number", "Error", ERROR_MESSAGE);

        }catch (NullPointerException e){
            showMessageDialog(null, "Number must be positive", "Error", ERROR_MESSAGE);

        }
        return count;
    }


    /**
     * This method allocates memory on all unique elements in program.
     */
    public void initElements() { // func to initiate all gui components in program
        enterSizeButton = new JButton("Enter");
        sortButton = new JButton("Sort");
        resetButton = new JButton("Reset");
        enterSizeLabel = new JLabel("How many numbers to display");
        buttonPanel = new JPanel();
        buttonScrollPane = new JScrollPane(buttonPanel);
        sizeTextField = new JTextField();
    }

    //configure all unique elements in program

    /**
     * This method configure all unique elements in program. Gives size, location, other settings.
     */
    public void configureElements(){
        buttonPanel.setLayout(new ColumnLayout());

        buttonScrollPane.setViewportView( buttonPanel );
        buttonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        buttonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        buttonScrollPane.setBounds(50, 30, 750, 500);

        sizeTextField.setBounds(300, 250, 190, 25);

        enterSizeButton.addActionListener(e -> showSortScreen(initButtonsArray(createArray(checkKeyInputErrors(sizeTextField.getText())))));
        enterSizeButton.setBounds(500, 250, 80, 25);

        enterSizeLabel.setBounds(310, 210, 300, 50);

        sortButton.addActionListener(event -> startThread());
        sortButton.setBounds(850, 250, 80, 25);

        resetButton.setBounds(850, 300, 80, 25);
        resetButton.addActionListener(e -> showMainScreen());

        mainFrame.add(buttonScrollPane);
        mainFrame.add(enterSizeLabel);
        mainFrame.add(sortButton);
        mainFrame.add(resetButton);
        mainFrame.add(sizeTextField);
        mainFrame.add(enterSizeButton);

        mainFrame.setSize(1000, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
    }


    //func to show sort screen

    /**
     * This method show elements that present on sort screen and hide elements from main screen.
     * @param arrayList ArrayList of buttons that will show in buttonPanel.
     */
    public void showSortScreen(List<JButton> arrayList) {
        if (arrayList != null) {
            buttonScrollPane.setVisible(false);
            enterSizeButton.setVisible(false);
            enterSizeLabel.setVisible(false);
            sizeTextField.setVisible(false);
            resetButton.setVisible(true);
            sortButton.setVisible(true);
            buttonScrollPane.setVisible(true);
        }
    }

    //show main screen of program

    /**
     * This method show elements that present on main screen and hide elements from sort screen.
     */
    public void showMainScreen() {  //show main screen
        resetFrame();
        sizeTextField.setVisible(true);
        enterSizeLabel.setVisible(true);
        sizeTextField.setText("");
        enterSizeButton.setVisible(true);
        sortButton.setVisible(false);
        resetButton.setVisible(false);
        buttonScrollPane.setVisible(false);
    }



    /**
     * Constructor makes all preparation to correct work of application.
     */
    public SortingApp() {
        super("Homework");
        initElements(); //initiate elements
        configureElements();
        showMainScreen(); //show main screen elements pack
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortingApp::new);
    }
}