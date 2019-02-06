package New.Shared;

public class MessageProductSearch extends CustomMessage {
    private String product;

    public MessageProductSearch(String product) {
        // This message type is a PRODUCT SEARCH
        type = MessageType.ProductSearch;

        this.product = product;
    }

    public String getProductName() {
        return product;
    }
}
