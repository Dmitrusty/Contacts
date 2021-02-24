package contacts;

class CmdExit implements Command {
    @Override
    public RetVal execute() {
        System.out.println("\nBye!\n");
        return RetVal.EXIT;
    }

    @Override
    public void setParameters(Params parameters) {

    }
}

