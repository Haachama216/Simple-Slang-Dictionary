package slang;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
class Slang {
    private String slangword;
    private String meaning;
    public Slang() {}
    public Slang(String slang, String meaning) {
        slangword = slang;
        this.meaning = meaning;
    }
    public void SetSlang(String slang) {
        slangword = slang;
    }
    public void SetMeaning(String meaning) {
        this.meaning = meaning;
    }
    public String GetSlang() {
        return slangword;
    }
    public String GetMeaning() {
        return meaning;
    }
}
public class SlangDictionary {
    private HashMap<String,String> SlangList;
    private HashMap <String,ArrayList<Slang>> KeywordList;
    private LinkedList<String> history;

    public SlangDictionary() {
        SlangList = new HashMap<String,String>();
        KeywordList = new HashMap<String,ArrayList<Slang>>();
        history = new LinkedList<String>();
    }
    public void AddSlang(Slang slang) {
        if (SlangList.containsKey(slang.GetSlang())) {
            System.out.println("This slang is already existed, do you want to overwrite it or not ?");
            System.out.println(slang.GetSlang());
            System.out.print("y/n: ");
            Scanner sc = new Scanner(System.in);
            if (sc.next().equals("n"))
                return;
        }
        SlangList.put(slang.GetSlang(),slang.GetMeaning());
        for (String keyword : slang.GetMeaning().split("\\|| ")) {
            AddKeyword(keyword.toLowerCase(),slang);
        }
    }
    public void AddKeyword(String keyword, Slang slang) {
        KeywordList.putIfAbsent(keyword.toLowerCase(), new ArrayList<Slang>());
        KeywordList.get(keyword.toLowerCase()).add(slang);
    }
    public void AddHistory(String slang) {
        if (history.size() == 10) {
            history.removeLast();
        }
        history.addFirst(slang);
    }
    public void SearchSlang(String slang) {
        String meaning = SlangList.get(slang);
        if (!SlangList.containsKey(slang)) {
            System.out.println("Cannot find this slang in dictionary");
            return;
        }
        System.out.println(slang + ": " + meaning.replace("|", " or"));
        AddHistory(slang);
    }
    public void SearchByKeyword(String keyword) {
        ArrayList<Slang> list = KeywordList.get(keyword.toLowerCase());
        if (!KeywordList.containsKey(keyword.toLowerCase())) {
            System.out.println("Cannot find any slang meaning containing this keyword");
            return;
        }
        for (Slang slang : list) {
            System.out.println(slang.GetSlang() + ": " + slang.GetMeaning().replace("|", " or"));
        }
    }
    public void PrintHistory() {
        Iterator it = history.iterator();
        while (it.hasNext()) {
            System.out.println((String)it.next());
        }
    }
    public void RemoveSlang(String slang) {
        if (!SlangList.containsKey(slang)) {
            System.out.println("Cannot find this slang in dictionary");
            return;
        }
        String meaning = SlangList.get(slang);
        for (String keyword : meaning.split("\\|| ")) {
            ArrayList<Slang> list = KeywordList.get(keyword.toLowerCase());
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i).GetSlang().equals(slang)) {
                    list.remove(i);
                    break;
                }
            }
        }
        SlangList.remove(slang);
    }
    public void EditSlang(String slang) {
        if (!SlangList.containsKey(slang)) {
            System.out.println("Cannot find this slang in dictionary");
            return;
        }
        String meaning = SlangList.get(slang);
        RemoveSlang(slang);
        String input = new String();
        System.out.println("Press enter if you dont want to change this field");
        Scanner sc = new Scanner(System.in);
        System.out.print("Slang: ");
        input = sc.nextLine();
        if (input.length() > 0)
            slang = input;
        System.out.print("Meaning: ");
        input = sc.nextLine();
        if (input.length() > 0)
            meaning = input;
        AddSlang(new Slang(slang,meaning));
        System.out.println("the slang was modified");
    }
    public void Reset() {
        SlangList.clear();
        KeywordList.clear();
        SlangDictionary.ReadFile(this);
    }
    public Slang SlangRandom() {
        ArrayList<String> ListofSlang = new ArrayList<String>(SlangList.keySet());
        Random random = new Random();
        int idx = random.nextInt(ListofSlang.size()); 
        String slang = ListofSlang.get(idx);
        String meaning = SlangList.get(slang);
        return new Slang(slang,meaning);
    }
    public void SlangGuessingGame() {
        String guessingSlang = new String();
        String answer = new String();
        ArrayList<String> choices = new ArrayList<String>(); 
        for (int i = 0; i < 4; ++i) {
            Slang randomSlang = SlangRandom();
            if (i == 0) {
                guessingSlang = randomSlang.GetSlang();
                answer = randomSlang.GetMeaning().replace("|", " or");
            }
            choices.add(randomSlang.GetMeaning().replace("|"," or"));
        }
        Collections.shuffle(choices);
        Scanner sc = new Scanner(System.in);
        System.out.format("What is the meaning of \"%s\"?\n",guessingSlang);
        String[] entries = {"A","B","C","D"};
        for (int i = 0; i < 4; ++i) {
            System.out.format("%s. %s\n",entries[i], choices.get(i).replace("|", " or"));
        }
        System.out.print("Your choice: ");
        if (choices.get(AnswerToIndex(sc.next())).equals(answer)) {
            System.out.println("Congrats that is the correct answer");
        }
        else {
            System.out.println("Oops you chose the wrong answer");
            System.out.format("the correct answer is \"%s\"\n",answer);
        }
    }
    public void MeaningGuessingGame() {
        String guessingMeaning = new String();
        String answer = new String();
        ArrayList<String> choices = new ArrayList<String>(); 
        for (int i = 0; i < 4; ++i) {
            Slang randomSlang = SlangRandom();
            if (i == 0) {
                answer = randomSlang.GetSlang();
                guessingMeaning = randomSlang.GetMeaning().replace("|", " or");
            }
            choices.add(randomSlang.GetSlang());
        }
        Collections.shuffle(choices);
        Scanner sc = new Scanner(System.in);
        System.out.format("What is the slang of \"%s\"?\n",guessingMeaning);
        String[] entries = {"A","B","C","D"};
        for (int i = 0; i < 4; ++i) {
            System.out.format("%s. %s\n",entries[i], choices.get(i).replace("|", " or"));
        }
        System.out.print("Your choice: ");
        if (choices.get(AnswerToIndex(sc.next())).equals(answer)) {
            System.out.println("Congrats that is the correct answer");
        }
        else {
            System.out.println("Oops you chose the wrong answer");
            System.out.format("the correct answer is \"%s\"\n",answer);
        }
    }
    private int AnswerToIndex(String input) {
        switch(input) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
        }
        return 3;
    }
    public static void ReadFile(SlangDictionary dictionary) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("Slang.txt"));
            String line = new String();
            while ((line = reader.readLine()) != null) {
                Slang slang = new Slang(line.split("`")[0],line.split("`")[1]);
                dictionary.AddSlang(slang);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void menu() {
        System.out.println("1. Search slang");
        System.out.println("2. Search slangs by keyword");
        System.out.println("3. Show search history");
        System.out.println("4. Add slang word");
        System.out.println("5. Edit slang word");
        System.out.println("6. Remove a slang word");
        System.out.println("7. Reset slang list");
        System.out.println("8. Random slang word");
        System.out.println("9. Slang guessing game");
        System.out.println("10. Meaning guessing game");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }
    public void Choice(int choice) {
        Scanner sc = new Scanner(System.in);
        switch(choice) {
            case 1:
                System.out.print("Type slang you want to search: ");
                SearchSlang(sc.nextLine());
                break;
            case 2:
                System.out.print("Type keyword you want to search: ");
                SearchByKeyword(sc.nextLine());
                break;
            case 3:
                System.out.println("Search history: ");
                PrintHistory();
                break;
            case 4: {
                String slang = new String();
                String meaning = new String();
                System.out.print("Type slang you want to add: ");
                slang = sc.nextLine();
                System.out.print("Type its meaning: ");
                meaning = sc.nextLine();
                AddSlang(new Slang(slang,meaning));
                break;
            }
            case 5:
                System.out.print("Type slang word you want to edit: ");
                EditSlang(sc.nextLine());
                break;
            case 6: {
                String slang = new String();
                System.out.print("Type slang word you want to remove: ");
                slang = sc.nextLine();
                System.out.print("You surely want to remove this slang? y/n ");
                String decision = sc.nextLine();
                if (decision.equals("n"))
                    break;
                else {
                    RemoveSlang(slang);
                    System.out.println("the slang was successfully removed");
                }
                break;
            }
            case 7: {
                System.out.print("You surely want to reset the dictionary ? y/n ");
                if (sc.nextLine().equals("y")) {
                    Reset();
                }
                break;
            }
            case 8: {
                Slang randomSlang = SlangRandom();
                System.out.print(randomSlang.GetSlang() + ": ");
                System.out.println(randomSlang.GetMeaning().replace("|", " or"));
                break;
            }
            case 9:
                SlangGuessingGame();
                break;
            case 10:
                MeaningGuessingGame();
                break;
        }
    }
}