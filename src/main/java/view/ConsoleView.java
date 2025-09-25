package view;

public class ConsoleView {

    public void showHeader(String title){
        System.out.println("==== " + title + "====");
    }

    public void showMenu(String[] options){
        for(int i = 0 ; i < options.length ; i++){
            System.out.println((i+1) + " . " + options[i]);
        }
    }

    public void showMessage(String message){
        System.out.println(message);
    }
}
