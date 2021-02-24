package contacts;

public class CmdEdit implements Command {
    private PhoneBook phoneBook;
    private int number;

    public CmdEdit(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    @Override
    public RetVal execute() {
        Contact contact = phoneBook.getContact(number);

        String fieldName = contact.enterFieldName();;
        String fieldValue = contact.enterFieldValue(fieldName);
        contact.setContactField(fieldName, fieldValue);
        phoneBook.setContact(contact, number);

        System.out.println("Saved");
        System.out.println(contact.toString());

        return RetVal.BACK;
    }


    @Override
    public void setParameters(Params parameters) {
        number = parameters.containsKey("number")? Integer.parseInt(parameters.getParameter("number")) : 0;
    }
}
