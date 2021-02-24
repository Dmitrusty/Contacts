package contacts;

public class CmdSearchMenu implements Command {
    private GeneralMenu searchMenu;
    private PhoneBook phoneBook;

    public CmdSearchMenu(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;

        searchMenu = new GeneralMenu("search");
        searchMenu.add("\\d+", new CmdRecordMenu(phoneBook))
                .add("back", new CmdBack());
                // .add("again", new CmdAgain());
    }

    @Override
    public RetVal execute() {
        RetVal retVal = RetVal.HOLD;
        Params parameters = new Params();
        int maxSearch = 0;

        do{
            if (phoneBook.getCount() == 0) {
                System.out.println("The Phone Book has 0 records.");
                return retVal;
            }

            System.out.print("Enter search query: ");
            String searchQuery = Main.scan.nextLine().trim().toLowerCase();
            maxSearch = phoneBook.printSearch(searchQuery);

            if(maxSearch > 0){
                do{
                    System.out.print(searchMenu);
                    String input = Main.scan.nextLine().trim();
                    parameters.putParameter("numberCount", String.valueOf(phoneBook.getCount()));

                    try {
                        int inNumber = Integer.parseInt(input);
                        int index = phoneBook.getFoundIndexes().get(inNumber - 1);
                        input = String.valueOf(index + 1);
                    } catch (NumberFormatException ignored) {}

                    parameters.putParameter("number", input);
                    retVal = searchMenu.executeCommand(input, parameters);

                }while (retVal == RetVal.HOLD);
            }
            else {
                System.out.println("The search has 0 results.");
                retVal = RetVal.BACK;
            }
        }while (retVal == RetVal.AGAIN);

        return retVal == RetVal.BACK ? RetVal.HOLD : retVal;
    }

    @Override
    public void setParameters(Params parameters) {

    }
}
