package contacts;

class CmdAdd implements Command
{
    private PhoneBook phoneBook;

    public CmdAdd(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    @Override
    public RetVal execute() {
        Contact contact;

        TYPE_LOOP:
        while (true) {
            System.out.print("Enter the type (person, organization): ");
            switch (Main.scan.nextLine().trim().toLowerCase()) {
                case "person":
                    contact = PersonsFactory.makeContact();
                    break TYPE_LOOP;
                case "organization":
                    contact = FirmsFactory.makeContact();
                    break TYPE_LOOP;
                default:
                    System.out.println(Main.wrongInputMessage);
            }
        }
        phoneBook.addContact(contact);
        System.out.println("The record added.");
        System.out.println(contact.toString());

        return RetVal.HOLD;
    }

    @Override
    public void setParameters(Params parameters) {

    }
}