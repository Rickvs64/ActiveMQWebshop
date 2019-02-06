package New.Shared;

public class MessageProductResult extends CustomMessage {
    private String product;
    private String webshop;
    private Boolean isAvailable = false;
    private Integer amountAvailable = 0;
    private Double price = 0.0d;

    public MessageProductResult(String product, String webshop, Boolean isAvailable, Integer amountAvailable, Double price) {
        // This message type is a PRODUCT RESULT
        type = MessageType.ProductResult;

        this.product = product;
        this.webshop = webshop;
        this.isAvailable = isAvailable;
        this.amountAvailable = amountAvailable;
        this.price = price;
    }

    public String getProductName() {
        return product;
    }

    public String getWebshop() {
        return webshop;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public Double getPrice() {
        return price;
    }
}
