package contacts;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Contact implements Serializable {
    private static final Long serialVersionUID = 6235119243437407474L;
    public String name;
    public String phone;
    final protected LocalDateTime created;
    protected LocalDateTime edited;
    transient private static final Pattern patternPhone = Pattern.compile("(\\+?\\(\\w+\\)([\\s-]\\w{2,})*)|(\\+?\\w+([\\s-]\\(\\w{2,}\\))?([\\s-]\\w{2,})*)");
    transient static protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
        created = LocalDateTime.now().withSecond(0).withNano(0);
        edited = created;
    }

    public String enterFieldValue(String fieldName) {
        return "";
    }

    public String enterFieldName() {

        StringBuilder sb = new StringBuilder();
        Field[] contactField = this.getClass().getFields();  // contact.getContactFields();

        int i = 0;
        sb.append(contactField[i++].getName());
        while (i < contactField.length) {
            sb.append(", ").append(contactField[i++].getName());
        }

        EDIT_LOOP:
        while (true) {
            System.out.print("Select a field (" + sb.toString() + "): ");

            String inputField = Main.scan.nextLine().trim().toLowerCase();
            for (Field f : contactField) {
                if (inputField.equals(f.getName())) {
                    return inputField;
                }
            }
            System.out.println(Main.wrongInputMessage);
        }
    }

    // 1. A method that returns all of the possible fields this class is able to change.
    public Field[] getContactFields() {
        return this.getClass().getFields();
    }

    // 2. A method that takes a string that represents a field that the class is able to change and its new value.
    public void setContactField(String fieldName, String fieldValue) {
        try {
            Field field = this.getClass().getField(fieldName);
            field.set(this, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 3. A method that takes a string representation of the field and returns the value of this field
    public String getContactField(String fieldName) {
        Object result = null;
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            Object obj = field.get(this.getClass());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result != null ? result.toString() : "";

    }

    public void setEdited(LocalDateTime edited) {
        this.edited = edited;
    }

    @Override
    public String toString() {
        return "Contact name: " + (name.equals("") ? "[no data]" : name) +
                "\nContact phone: " + (phone.equals("") ? "[no data]" : phone) +
                "\nContact created: " + created +
                "\nContact edited: " + edited;
    }

    public String forList() {
        return (name.equals("") ? "[no data]" : name);
    }

    public String forSearch() {
        return (name == null ? "" : name) + (phone == null ? "" : " " + phone);
    }

    public static String enterPhone() {
        System.out.print("Enter the number: ");
        String number = Main.scan.nextLine();

        if (isValid(number)) {
            return number;
        } else {
            System.out.println("Bad number!");
            return "";
        }
    }

    public static boolean isValid(String string) {
        Matcher matcher = patternPhone.matcher(string);
        return matcher.matches();
    }
}

class Person extends Contact implements Serializable {
    public String surname;
    public String birth;
    public String gender;

    public Person(String name, String surname, String birth, String gender, String phone) {
        super(name, phone);
        this.surname = surname;
        this.birth = birth;
        this.gender = gender;
    }

    public void setName(String name) {
        super.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        super.phone = phone;
    }

    public static String enterName() {
        System.out.print("Enter the name: ");
        return Main.scan.nextLine();
    }

    public static String enterSurname() {
        System.out.print("Enter the surname: ");
        return Main.scan.nextLine();
    }

    public static String enterBirth() {
        System.out.print("Enter the birth date: ");
        String string = Main.scan.nextLine();
        try {
            return LocalDate.parse(string).toString();
        } catch (Exception e) {
            System.out.println("Bad birth date!");
            return null;
        }
    }

    public static String enterGender() {
        System.out.print("Enter the gender (M, F): ");
        String string = Main.scan.nextLine().trim().toUpperCase();

        switch (string) {
            case "M":
            case "F":
                return string;
            default:
                System.out.println("Bad gender!");
        }
        return null;
    }

    public static Person makeContact() {

        String name = enterName();
        String surname = enterSurname();
        String birth = enterBirth();
        String gender = enterGender();
        String phone = enterPhone();

        return new Person(name, surname, birth, gender, phone);
    }

    // @Override
    public String enterFieldValue(String fieldName) {
        switch (fieldName) {
            case "name":
                return enterName();
            case "surname":
                return enterSurname();
            case "birth":
                return enterBirth();
            case "gender":
                return enterGender();
            case "phone":
                return enterPhone();
            default:
                System.out.println("Wrong field entering to Person");
                return "";
        }
    }

    @Override
    public String forList() {
        return (super.name.equals("") ? "[no data]" : super.name) +
                (surname.equals("") ? "" : " " + surname);

    }

    @Override
    public String forSearch() {
        return (super.name == null ? "" : super.name) +
                (surname == null ? "" : " " + surname) +
                (birth == null ? "" : " " + birth) +
                (gender == null ? "" : " " + gender) +
                (super.phone == null ? "" : " " + super.phone);
    }

    @Override
    public String toString() {
        return "Name: " + (super.name.equals("") ? "[no data]" : super.name) +
                "\nSurname: " + (surname.equals("") ? "[no data]" : surname) +
                "\nBirth date: " + (birth == null ? "[no data]" : birth) +
                "\nGender: " + (gender == null ? "[no data]" : gender) +

                "\nNumber: " + (phone.equals("") ? "[no data]" : super.phone) +
                "\nTime created: " + super.created.format(super.dateTimeFormatter) +
                "\nTime last edit: " + super.edited.format(super.dateTimeFormatter);
    }
}

class Firm extends Contact implements Serializable {
    public String address;

    public Firm(String name, String address, String phone) {
        super(name, phone);
        this.address = address;
    }

    public void setFirmName(String name) {
        super.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFirmPhone(String phone) {
        super.phone = phone;
    }

    public static String enterName() {
        System.out.print("Enter the organization name: ");
        return Main.scan.nextLine();
    }

    public static String enterAddress() {
        System.out.print("Enter the address: ");
        return Main.scan.nextLine();
    }


    public static Firm makeContact() {
        String name = enterName();
        String address = enterAddress();
        String phone = enterPhone();

        return new Firm(name, address, phone);
    }

    // @Override
    public String enterFieldValue(String fieldName) {
        switch (fieldName) {
            case "name":
                return enterName();
            case "address":
                return enterAddress();
            case "phone":
                return enterPhone();
            default:
                System.out.println("Wrong field entering to Organization");
                return "";
        }
    }

    @Override
    public String forList() {
        return (super.name.equals("") ? "[no data]" : super.name);
    }

    @Override
    public String forSearch() {
        return (super.name == null ? "" : super.name) +
                (address == null ? "" : address) +
                (super.phone == null ? "" : " " + super.phone);
    }

    @Override
    public String toString() {
        return "Organization name: " + (super.name.equals("") ? "[no data]" : super.name) +
                "\nAddress: " + (address.equals("") ? "[no data]" : address) +
                "\nNumber: " + (phone.equals("") ? "[no data]" : super.phone) +
                "\nTime created: " + super.created.format(super.dateTimeFormatter) +
                "\nTime last edit: " + super.edited.format(super.dateTimeFormatter);
    }
}
