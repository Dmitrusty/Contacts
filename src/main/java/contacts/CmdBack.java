package contacts;

class CmdBack implements Command {
    @Override
    public RetVal execute() {
        return RetVal.BACK;
    }

    @Override
    public void setParameters(Params parameters) {

    }
}