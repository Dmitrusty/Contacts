package contacts;

class CmdRecordMenu implements Command {
    private GeneralMenu recordMenu;
    private PhoneBook phoneBook;
    private int numberCount;
    private int number;

    public CmdRecordMenu(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;

        recordMenu = new GeneralMenu("record");
        recordMenu.add("edit", new CmdEdit(phoneBook))
                .add("delete", new CmdDelete(phoneBook))
                .add("contacts/menu", new CmdMain());
    }


    @Override
    public RetVal execute() {
        RetVal retVal = RetVal.HOLD;
        Params parameters = new Params();

        if (numberCount < 1){
            System.out.println("Bad records list!");
            return retVal.BACK;
        }else if(1 > number || number > numberCount){
            System.out.println("Bad record index!");
            return retVal.BACK;
        }

        do {
            System.out.println(phoneBook.getContact(number).toString());
            do {
                System.out.print(recordMenu);
                parameters.putParameter("number", String.valueOf(number));
                retVal = recordMenu.executeCommand(Main.scan.nextLine().trim(), parameters);
            } while (retVal == RetVal.HOLD);

        } while (retVal == RetVal.AGAIN);

        return retVal == RetVal.BACK ? RetVal.HOLD : retVal;
    }

    @Override
    public void setParameters(Params parameters) {
        numberCount = parameters.containsKey("numberCount")? Integer.parseInt(parameters.getParameter("numberCount")) : 0;
        number = parameters.containsKey("number")? Integer.parseInt(parameters.getParameter("number")) : 0;
    }
}