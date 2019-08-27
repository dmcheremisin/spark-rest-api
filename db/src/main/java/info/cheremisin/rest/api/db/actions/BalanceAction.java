package info.cheremisin.rest.api.db.actions;

public enum BalanceAction {
    ADD("+"), SUBSTRACT("-");

    private String action;

    BalanceAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
