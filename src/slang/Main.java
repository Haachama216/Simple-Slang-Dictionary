package slang;
import java.util.Scanner;
import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        SlangDictionary dictionary = new SlangDictionary();
        SlangDictionary.ReadFile(dictionary);
        int choice;
        Scanner sc = new Scanner(System.in);
        do {
            dictionary.menu();
            choice = sc.nextInt();
            sc.nextLine();
            System.out.println("_______________________________\n");
            dictionary.Choice(choice);
            System.out.print("Press enter to continue ");
            sc.nextLine();
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            catch(IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } while (choice != 0);
        sc.close();
    }
}