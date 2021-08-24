import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
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

public class Main extends JFrame {

    private JFrame mainFrame = new JFrame(); //main frame
    private JPanel buttonPanel;
    private JButton enterSizeButton; // button for enter size on main screen
    private JButton resetButton; //reset button on a sort screen
    private JButton sortButton;  // sort button on sort screen
    private JScrollPane buttonScrollPane;
    private JTextField sizeTextField; // field for enter number
    private Random rand;
    private JLabel enterSizeLabel; // label for main screen
    private boolean isLower;
    private boolean order = true;
    private int count; //number that input in main screen
    private ArrayList<Integer> listToSort; //list of int for sort
    private List<JButton> buttonList; //list of buttons for show on sort screen





    static class ColumnLayout implements LayoutManager {
        private int vgap;
        int x;
        int y;
        private int minWidth = 0, minHeight = 0;
        private int preferredWidth = 0, preferredHeight = 0;
        private boolean sizeUnknown = true;

        public ColumnLayout() {
            this(5);
        }

        public ColumnLayout(int v) {
            vgap = v;
        }

        /* Required by LayoutManager. */
        public void addLayoutComponent(String name, Component comp) {
        }

        /* Required by LayoutManager. */
        public void removeLayoutComponent(Component comp) {
        }

        private void setSizes(Container parent) {
            int nComps = parent.getComponentCount();
            Dimension d = null;

            //Reset preferred/minimum width and height.
            preferredWidth = 0;
            preferredHeight = 0;
            minWidth = 0;
            minHeight = 0;

            for (int i = 0; i < nComps; i++) {
                Component c = parent.getComponent(i);
                if (c.isVisible()) {
                    d = c.getPreferredSize();

                    if (i > 0) {
                        preferredWidth += d.width/2;
                        preferredHeight += vgap;
                    } else {
                        preferredWidth = d.width;
                    }
                    preferredHeight += d.height;

                    minWidth = Math.max(c.getMinimumSize().width,
                            minWidth);
                    minHeight = preferredHeight;
                }
            }
        }

        /* Required by LayoutManager. */
        public Dimension preferredLayoutSize(Container parent) {
            Dimension dim = new Dimension(0, 0);
            int nComps = parent.getComponentCount();

            setSizes(parent);

            //Always add the container's insets!
            Insets insets = parent.getInsets();
            dim.width = preferredWidth
                    + insets.left + insets.right;
            dim.height = preferredHeight
                    + insets.top + insets.bottom;

            sizeUnknown = false;

            return dim;
        }


        public Dimension minimumLayoutSize(Container parent) {
            Dimension dim = new Dimension(0, 0);
            int nComps = parent.getComponentCount();

            //Always add the container's insets!
            Insets insets = parent.getInsets();
            dim.width = minWidth
                    + insets.left + insets.right;
            dim.height = minHeight
                    + insets.top + insets.bottom;

            sizeUnknown = false;

            return dim;
        }

        public void layoutContainer(Container parent) {
            x = 0;
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

    public void showSortScreen(List arrayList) {
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

    public void resetFrame() { //func that delete buttons from frame, if we need to generate new buttons
        if (buttonList != null) {
            for (JButton jButton : buttonList) {
                buttonPanel.remove(jButton);
            }
        }
    }

    public ArrayList<Integer> createArray(int size){
        if(size!=0){
            isLower = false;
            listToSort = new ArrayList();
            rand = new Random();
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

    private void startThread() {
        sortButton.setEnabled(false);
        resetButton.setEnabled(false);
        new Thread() {
            public void run() {
                if(order){
                    try {
                        sortingDesc(listToSort,0,listToSort.size()-1);
                        sortButton.setEnabled(true);
                        resetButton.setEnabled(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order = false;
                } else {
                    try {
                        sortingIncr(listToSort,0,listToSort.size()-1);
                        sortButton.setEnabled(true);
                        resetButton.setEnabled(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order = true;
                }
            }
        }.start();
    }


    public List<JButton> initButtonsArray(ArrayList intList) {
        resetFrame();
        if (intList.size() != 0) {
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
                Thread.sleep(1000);
                showSortScreen(initButtonsArray(list));
            }
        }

        int temp = list.get(i+1);
        list.set(i+1, list.get(high));
        list.set(high, temp);
        showSortScreen(initButtonsArray(list));
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

    public int partitionDesc(ArrayList<Integer> list, int low, int high) throws InterruptedException {
        int pivot = list.get(low);
        int i = low;
        for(int j = low + 1; j <= high; j++){
            if (list.get(j) > pivot){
                i = i + 1;
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                showSortScreen(initButtonsArray(list));
                Thread.sleep(1000);
            }
        }
        int temp = list.get(i);
        list.set(i, list.get(low));
        list.set(low, temp);
        Thread.sleep(1000);
        showSortScreen(initButtonsArray(list));
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

    public Integer checkKeyInputErrors(String text) {
        try {
            if (text.trim().length() == 0) {
                showMessageDialog(null, "Empty field", "Error", ERROR_MESSAGE);
                count = 0;
            } else if (Integer.parseInt(text) <= 0) {
                showMessageDialog(null, "Number must be positive", "Error", ERROR_MESSAGE);
                count = 0;
            }else {
                count = Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            showMessageDialog(null, "Invalid number", "Error", ERROR_MESSAGE);
        }
        return count;
    } //check input errors func


    public void initElements() { // func to initiate all gui components in program
        enterSizeButton = new JButton("Enter");
        sortButton = new JButton("Sort");
        resetButton = new JButton("Reset");
        enterSizeLabel = new JLabel("How many numbers to display");
        buttonPanel = new JPanel();
        buttonScrollPane = new JScrollPane(buttonPanel);
        sizeTextField = new JTextField();
    }


    public void configureElements(){
        buttonPanel.setLayout(new ColumnLayout());

        buttonScrollPane.setViewportView( buttonPanel );
        buttonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        buttonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
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

    public Main() {
        super("Homework");
        initElements(); //initiate elements
        configureElements();
        showMainScreen(); //show main screen elements pack
    }


    public static void main(String[] args) {
       SwingUtilities.invokeLater(()->new Main());

    }
}
