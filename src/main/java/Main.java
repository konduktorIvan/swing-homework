import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Main extends JFrame{
        JFrame frame = new JFrame(); //основной фрейм

        JButton enterSizeButton, resetButton, sortButton, tempButton; //Кнопки подтверждения ввода, ресета,
                                                                      // сортировки, темпкнопка для 2-го экрана
        JTextField sizeTextField; // поле куда вводить число

        JLabel testLabel;

        int x,y; //координаты кнопок

        int count; //введенное число


        ArrayList buttonList; //список кнопок, которые будут выведены на экране сортировки


        public void showSortScreen(ArrayList arrayList){
            if(arrayList!=null){
                System.out.println("It's func show sort screen");
                enterSizeButton.setVisible(false);
                sizeTextField.setVisible(false);
                resetButton.setVisible(true);
                sortButton.setVisible(true);
                showButtons(true);
            }
        }

        public void showButtons(boolean setVisible){
            System.out.println("its func show buttons");
            if(buttonList != null){
                for (int i = 0; i < buttonList.size(); i++) {
                    tempButton = (JButton) buttonList.get(i);
                    if(setVisible == true){
                        tempButton.setVisible(true);
                    }
                    else {
                        tempButton.setVisible(false);
                    }
                }
            }
        }

        public void resetFrame(){ //резет фрэйма для того, чтобы кнопки не оставались т.к. они добавлены в initButtonsArray
            showMainScreen();
            if(buttonList != null){
                for (int i = 0; i < buttonList.size(); i++) {
                    tempButton = (JButton) buttonList.get(i);
                    frame.remove(tempButton);
                }
            }
        }


        public ArrayList initButtonsArray(int size){
            resetFrame();
            System.out.println("its func init, size" + size);
            x = 200; //сбрасываем изначальное положение
            y = 100; //чтобы при резете не сьезжали кнопки
            if(size!=0){ // проверка, потому что инициализация происходит сначала программы и идея ругается,
                        // что сайз не задан, пока не введен
                Random r = new Random();
                buttonList = new ArrayList();
                buttonList.clear();
            for (int i = 0; i < size; i++) {
                buttonList.add(new JButton());
            }

            for (int i = 0; i < size; i++) {
                tempButton = (JButton) buttonList.get(i);
                if (i % 10 == 0) {
                    x += 100;
                    y = 100;
                }
                tempButton.setBounds(x, y += 40, 100, 30);
                tempButton.setText(String.valueOf(r.nextInt(30)));
                tempButton.addActionListener(e -> showSortScreen(initButtonsArray(checkErrors(tempButton.getText()))));
                tempButton.setVisible(false);
                frame.add(tempButton);
            }
                return buttonList;
            }
            return null;
        }

   /* public void doSort(ArrayList<JButton> arrayList, int low, int high)  {

        Integer middle = low + (high - low) / 2;
        Integer pivot =  Integer.parseInt(arrayList.get(middle).getText());
        int i = low;
        int j = high;

        while (i <= j) {
            while ((Integer) arrayList.get(i) < pivot) {
                ++i;
            }

            while ((Integer) arrayList.get(j) > pivot) {
                --j;
            }

            if (i <= j) {
                int temp = (Integer) arrayList.get(i);
                arrayList.set(i, (Integer) arrayList.get(j));
                arrayList.set(j, temp);
                ++i;
                --j;
            }

        }

        if (low < j) {
            doSort(arrayList, low, j);

        }

        if (high > i) {
            doSort(arrayList, i, high);
        }

    showSortScreen(arrayList);
    } // сортировка
*/
        public Integer checkErrors(String text){
            try {
                if (text.trim().length() == 0) {
                    showMessageDialog(null, "Empty field", "Error", ERROR_MESSAGE);
                    count = 0;
                } else if (Integer.parseInt(text) <= 0) {
                    //count = Integer.parseInt(textFromTextField);
                    showMessageDialog(null, "Number must be positive", "Error", ERROR_MESSAGE);
                    count = 0;
                }
                else if(Integer.parseInt(text) > 30){
                    showMessageDialog(null, "Number must be less 30", "Error", ERROR_MESSAGE);
                    count=0;
                }
                else {
                    count = Integer.parseInt(text);
                }

            }catch (NumberFormatException e) {
                showMessageDialog(null, "Invalid number", "Error", ERROR_MESSAGE);
            }
            return count;
        } //функция для проверки правильностиввода числа(отрицательное, нулевое, буквы)




        public void initElements(){ // функция изначальной инициализации элементов

            enterSizeButton = new JButton("Enter");
            sortButton = new JButton("Sort");
            resetButton = new JButton("Reset");
            //sortButton.addActionListener(e->doSort(listToSort,0,listToSort.size()-1));
            resetButton.addActionListener(e-> resetFrame());
            enterSizeButton.addActionListener(e -> showSortScreen(initButtonsArray(checkErrors(sizeTextField.getText()))));
            sizeTextField = new JTextField("Enter number");
            testLabel = new JLabel();
            sizeTextField.setBounds(300,250,190,25);
            enterSizeButton.setBounds(500,250,80,25);
            testLabel.setBounds(200,100,800,300);
            sortButton.setBounds(700,250,80,25);
            resetButton.setBounds(700,300,80,25);

            frame.add(testLabel);
            frame.add(sortButton);
            frame.add(resetButton);
            frame.add(sizeTextField);
            frame.add(enterSizeButton);
            //initButtonsArray();
        }

        public void showMainScreen(){  //показать экран ввода
            System.out.println("func show main screen");
            sizeTextField.setVisible(true);
            enterSizeButton.setVisible(true);
            showButtons(false);
            sortButton.setVisible(false);
            resetButton.setVisible(false);
            //for (int i = 0; i < 30; i++) {

           // }
        }

    public Main(){
        super("Homework");

        initElements(); //инициализируем элементы
        showMainScreen(); // набор элементов для экрана ввода
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);

    }


    public static void main(String[] args) {

        new Main();
    }
}
