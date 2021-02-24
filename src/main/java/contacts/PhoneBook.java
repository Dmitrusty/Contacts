package contacts;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {
    private List<Contact> list = new ArrayList<>();
    private String pathName;
    private List<Integer> foundIndexes = new ArrayList<>(); // of a contact in phonebook.list

    public PhoneBook(String pathName, String defaultPathName) {

        File file = new File(pathName);

        if (file.isFile()) {
            this.pathName = pathName;
            loadPhoneBook();
        } else {
            file = new File(defaultPathName);

            if (file.isFile()) {

                this.pathName = defaultPathName;
                loadPhoneBook();
            } else {
                System.out.println("\nCan`t load phonebook from the specified file!");

                // Create new phonebook file
                file = new File(defaultPathName);
                try {
                    if (file.createNewFile()) {
                        System.out.println("The file:\n" + defaultPathName + "\nhas been created.");
                        this.pathName = defaultPathName;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void savePhoneBook() {
        File file = new File(pathName);

        if (file.isFile()) {
            try (FileOutputStream fos = new FileOutputStream(pathName, false);
                 BufferedOutputStream bos = new BufferedOutputStream(fos);
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {

                oos.writeObject(list);

            } catch (IOException e) {
                System.out.println("\nCan`t write phonebook to file!\n");
                e.printStackTrace();
            }
        } else {
            System.out.println("\nCan`t find the phonebook file!\n");
        }
    }

    public void loadPhoneBook() {
        File file = new File(pathName);

        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(pathName);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                list.clear();
                list.addAll((Collection<? extends Contact>) ois.readObject());

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("\nCan`t load phonebook from file!\n");
                e.printStackTrace();
            }
        } else {
            System.out.println("\nCan`t find the phonebook file!\n");
        }
    }

    public int printSearch(String searchQuery) {
        foundIndexes.clear();
        Pattern pattern = Pattern.compile(searchQuery);

        for (Contact c : list) {
            Matcher matcher = pattern.matcher(c.forSearch().toLowerCase());
            if (matcher.find()) {
                foundIndexes.add(list.indexOf(c));
            }
        }

        StringBuilder result = new StringBuilder("Found " + foundIndexes.size() + " results:");
        for (int i = 0; i < foundIndexes.size(); i++) {
            result.append("\n").append(i + 1).append(". ").append(getContact(foundIndexes.get(i) + 1).forList());
        }
        System.out.println(result.toString());

        return foundIndexes.size();
    }


    public Contact getContact(int number) {
        return list.get(number - 1);
    }

    public void setContact(Contact contact, int number) {
        contact.setEdited(LocalDateTime.now());
        this.list.set(number - 1, contact);
        savePhoneBook();
    }

    public void addContact(Contact contact) {
        list.add(contact);
        savePhoneBook();
    }

    public void removeContact(int number) {
        list.remove(number - 1);
        savePhoneBook();
    }


    public int printList() {
        StringBuilder result = new StringBuilder();

        int i = 0;
        for (Contact c : list) {
            result.append(list.indexOf(c) + 1).append(". ").append(c.forList()).append("\n");
            i++;
        }
        System.out.print(result.toString());
        return i;

    }

    public int getCount() {
        return list.size();
    }

    public List<Integer> getFoundIndexes() {
        return foundIndexes;
    }
}
