package contacts;

public class CmdMain implements Command {
    @Override
    public RetVal execute() {
        return RetVal.MAIN;
    }

    @Override
    public void setParameters(Params parameters) {

    }
}
