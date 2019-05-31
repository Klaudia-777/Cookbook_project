package javaLabApp;

public class Operation {
    private static CookbookDBService cookbookDBService = new CookbookDBService();

    public static void main(String[] args) {
        cookbookDBService.createConnection();
        cookbookDBService.dropRow();
        cookbookDBService.closeConnection();
    }
}
