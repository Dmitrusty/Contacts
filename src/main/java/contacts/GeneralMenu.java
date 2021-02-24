package contacts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// todo в каком файле их лучше разместить?
enum RetVal {
    AGAIN,       // после выполнения команды - перезаход в текущее меню
    HOLD,        // после выполнения команды - остаемся в текущем меню
    BACK,        // после выполнения команды - переход в вышестоящее меню
    MAIN,        // после выполнения команды - переход в главное меню
    EXIT;
}

interface Command {
    RetVal execute();
    void setParameters(Params parameters);
}

class Params {
    private HashMap<String, String> params = new HashMap<>();

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public void clearParams(){
        this.params.clear();
    }

    public boolean containsKey(String key){
        return params.containsKey(key);
    }

    public String getParameter(String key) {
        // todo что вернуть если нет такого параметра???
        return params.getOrDefault(key, "");
    }

    public void putParameter(String key, String value) {
        this.params.put(key, value);
    }

    public static Params emptyParams(){
        return new Params();
    }
}


class GeneralMenu {
    private String menuTitle;
    private LinkedHashMap<String, Command> menuItems;

    public GeneralMenu(String menuId) {
        this.menuTitle = "[" + menuId.toLowerCase() + "] Enter action";
        this.menuItems = new LinkedHashMap<>();
    }

    public RetVal executeCommand(String name, Params parameters) {
        RetVal retVal = RetVal.HOLD;
        Params params = new Params();

        name = name.toLowerCase();
        if (this.menuItems.containsKey(name)) {
            this.menuItems.get(name).setParameters(parameters);
            retVal = this.menuItems.get(name).execute();
        }
        else if (this.menuItems.containsKey("\\d+")) {
            this.menuItems.get("\\d+").setParameters(parameters);
            retVal = this.menuItems.get("\\d+").execute();
        }
        else System.out.println("Wrong input. Try again!");

        return retVal;
    }

    public GeneralMenu add(String name, Command command) {
        this.menuItems.putIfAbsent(name, command);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("\n" + this.menuTitle + " (");
        for (Map.Entry<String, Command> item : this.menuItems.entrySet()) {
            String menuItem = item.getKey().equals("\\d+") ? "[number]" : item.getKey();
            out.append(menuItem).append(", ");
        }
        out.deleteCharAt(out.length() - 1).setCharAt(out.lastIndexOf(","), ')');
        return out.append(": ").toString();
    }
}



/*
public class Menu {

    enum Aim {
        MAIN,
        LIST,
        SEARCH,
        SEARCH_AGAIN,
    }

    public static Aim add(Scanner scan, Contact contact, PhoneBook phoneBook, PersonsFactory personsFactory, FirmsFactory firmsFactory) {
        Aim retAim = Aim.MAIN;
        TYPE_MENU:
        while (true) {
            System.out.print("Enter the type (person, organization): ");
            switch (scan.nextLine().trim().toLowerCase()) {
                case "person":
                    contact = personsFactory.makeContact();
                    break TYPE_MENU;
                case "organization":
                    contact = firmsFactory.makeContact();
                    break TYPE_MENU;
                default:
                    System.out.println(Main.wrongInputMessage);
            }
        }

        phoneBook.addContact(contact);
        System.out.println("The record added.");
        System.out.println(contact.toString());

        return retAim;
    }


    public static Aim list(Scanner scan, PhoneBook phoneBook) {
        Aim retAim = Aim.MAIN;
        int maxList = 0;

        if (phoneBook.getCount() == 0) {
            System.out.println("The Phone Book has 0 records.");
            return retAim;
        }

        maxList = phoneBook.printList();

        LIST_MENU:
        while (true) {
            System.out.print("\n[list] Enter action ([number], back): ");

            String input = scan.nextLine().trim().toLowerCase();

            if (input.equals("back")) {
                break LIST_MENU;
            }

            int index = 0;
            try {
                index = Integer.parseInt(input);

                if (1 <= index && index <= maxList) {
                    Contact contact = phoneBook.getContact(index);
                    System.out.println(contact.toString());

                    // Calling record menu
                    retAim = record(scan, phoneBook, contact, index);
                    if (!retAim.equals(Aim.LIST)) {
                        break LIST_MENU;
                    }
                    break;

                } else {
                    System.out.println(Main.wrongInputMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println(Main.wrongInputMessage);
            }
        }
        return retAim;
    }


    public static Aim record(Scanner scan, PhoneBook phoneBook, Contact contact, int index) {
        Aim retAim = Aim.LIST;
        String fieldName;
        String fieldValue;


        RECORD_MENU:
        while (true) {
            System.out.print("\n[record] Enter action (edit, delete, menu): ");

            switch (scan.nextLine().trim().toLowerCase()) {
                case "edit":
                    fieldName = contact.enterFieldName(scan);
                    fieldValue = contact.enterFieldValue(fieldName, scan);
                    contact.setContactField(fieldName, fieldValue);
                    phoneBook.setContact(contact, index);
                    System.out.println("Saved");
                    System.out.println(contact.toString());
                    break;
                case "delete":
                    phoneBook.removeContact(index);
                    break;
                case "menu":
                    return Aim.MAIN;
                default:
                    System.out.println(Main.wrongInputMessage);
            }
        }
        // return retAim;
    }

    public static Aim search(Scanner scan, PhoneBook phoneBook) {
        Aim retAim = Aim.MAIN;
        int maxSearch = 0;

        if (phoneBook.getCount() == 0) {
            System.out.println("The Phone Book has 0 records.");
            return retAim;
        }

        System.out.print("Enter search query: ");
        String searchQuery = scan.nextLine().trim();
        maxSearch = phoneBook.printSearch(searchQuery);

        SEARCH_MENU:
        while (true) {
            System.out.print("\n[search] Enter action ([number], back, again): ");

            String input = scan.nextLine().trim().toLowerCase();

            if (input.equals("back")) {
                break SEARCH_MENU;
            }
            if (input.equals("again")) {
                retAim = Aim.SEARCH_AGAIN;
                break SEARCH_MENU;
            }

            int index = 0;
            try {
                index = Integer.parseInt(input);

                if (1 <= index && index <= maxSearch) {
                    Contact contact = phoneBook.getContact(phoneBook.getFoundIndexes().get(index - 1) + 1);
                    System.out.println(contact.toString());

                    // Calling record menu
                    retAim = record(scan, phoneBook, contact, index);
                    if (!retAim.equals(Aim.SEARCH)) {
                        break SEARCH_MENU;
                    }
                    break;

                } else {
                    System.out.println(Main.wrongInputMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println(Main.wrongInputMessage);
            }


        }
        return retAim;
    }
}*/
