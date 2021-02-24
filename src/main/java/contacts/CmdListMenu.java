package contacts;

class CmdListMenu implements Command {
    private GeneralMenu listMenu;
    private PhoneBook phoneBook;

    public CmdListMenu(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;

        listMenu = new GeneralMenu("list");
        listMenu.add("\\d+", new CmdRecordMenu(phoneBook));
        listMenu.add("back", new CmdBack());
    }

    @Override
    public RetVal execute() {
        RetVal retVal = RetVal.HOLD;
        Params parameters = new Params();

        do {
            int maxList = phoneBook.printList();
            if (maxList > 0) {
                do {
                    System.out.print(listMenu);
                    String input = Main.scan.nextLine().trim();
                    parameters.putParameter("numberCount", String.valueOf(maxList));
                    parameters.putParameter("number", input);
                    retVal = listMenu.executeCommand(input, parameters);
                } while (retVal == RetVal.HOLD);

            } else {
                System.out.println("The Phone Book has " + phoneBook.getCount() + " records.");
                retVal = RetVal.BACK;
            }
        } while (retVal == RetVal.AGAIN);

        return retVal == RetVal.BACK ? RetVal.HOLD : retVal;
    }

    @Override
    public void setParameters(Params parameters) {

    }
}