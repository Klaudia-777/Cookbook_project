public class Pair {
    private String input;
    private boolean expextedResult;

    public Pair(String input, boolean expextedResult) {
        this.input = input;
        this.expextedResult = expextedResult;
    }

    public String getInput() {
        return input;
    }

    public boolean getExpextedResult() {
        return expextedResult;
    }
}
