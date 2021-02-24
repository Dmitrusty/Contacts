package contacts;

import java.util.Scanner;

public class Main {
    public static Scanner scan;
    public static final String wrongInputMessage = "Wrong input.\nTry again!";

    public static void main(String[] args) {
        scan = new Scanner(System.in);

        String pathName = "";
        if (args.length == 2 && args[0].equals("--data")) {
            pathName = args[1];
        }
        PhoneBook phoneBook = new PhoneBook(pathName, "phonebook.phb");

        GeneralMenu mainMenu = new GeneralMenu("contacts/menu");
        mainMenu.add("add", new CmdAdd(phoneBook))
                .add("list", new CmdListMenu(phoneBook))
                .add("search", new CmdSearchMenu(phoneBook))
                .add("count", new CmdCount(phoneBook))
                .add("exit", new CmdExit())
                .add("0", new CmdExit());

        RetVal retVal;
        do {
            System.out.print(mainMenu);
            retVal = mainMenu.executeCommand(scan.nextLine().trim(), Params.emptyParams());
        } while (retVal == RetVal.HOLD || retVal == RetVal.MAIN);

        scan.close();
    }
}