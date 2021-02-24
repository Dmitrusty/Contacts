package contacts;

public class CmdDelete implements Command {
    private PhoneBook phoneBook;
    private int number;

    public CmdDelete(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    @Override
    public RetVal execute() {
        RetVal retVal;

        LOOP:
        while (true){
            System.out.print("Please, confirm deletion (Y / N): ");
            switch (Main.scan.nextLine().trim().toUpperCase()) {
                case "Y":
                    phoneBook.removeContact(number);
                    System.out.println("The record is deleted.");
                    retVal = RetVal.MAIN;
                    break LOOP;
                case "N":
                    retVal = RetVal.BACK;
                    break LOOP;
                default:
                    System.out.println("Wrong input. Try again!");
                    retVal = RetVal.HOLD;
            }
        }
        return retVal;
    }

    @Override
    public void setParameters(Params parameters) {
        number = parameters.containsKey("number")? Integer.parseInt(parameters.getParameter("number")) : 0;
    }
}
