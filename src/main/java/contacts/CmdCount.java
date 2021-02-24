package contacts;

class CmdCount implements Command {
    private PhoneBook phoneBook;

    public CmdCount(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    @Override
    public RetVal execute() {
        System.out.println("The Phone Book has " + phoneBook.getCount() + " records.");
        return RetVal.HOLD;
    }

    @Override
    public void setParameters(Params parameters) {

    }
}