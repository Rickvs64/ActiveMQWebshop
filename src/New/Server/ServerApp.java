package New.Server;

import New.Client.ClientApp;
import New.Shared.Connector;
import New.Shared.IManualListener;
import New.Shared.MessageProductResult;
import New.Shared.MessageProductSearch;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

public class ServerApp implements IManualListener, Runnable {

    private static Connector connector;
    private List<String> availableProducts;


    private void initConnector() {
        connector = new Connector("ProductQueue", this);
    }

    @Override
    public void onProductSearchReceived(MessageProductSearch message) {
        System.out.println("Received request for product: " + message.getProductName());
        System.out.println();

        if (isProductAvailable(message.getProductName())) {
            System.out.println(getWebshopName() + " has this product available!");
            System.out.println("Sending response...");
            try {
                connector.sendMessage(new MessageProductResult(message.getProductName(), getWebshopName(), true, 1, 1.0d));
                System.out.println("Response sent.");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(getWebshopName() + " does NOT have this product available...");
            System.out.println("Sending response...");
            try {
                connector.sendMessage(new MessageProductResult(message.getProductName(), getWebshopName(), false, 0, 0.0d));
                System.out.println("Response sent.");
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onProductResultReceived(MessageProductResult message) {

    }

    @Override
    public void run() {
        initConnector();
        initProductList();

        showIntro();
        showWaiting();
        try {
            connector.receiveGenericMessage();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void showIntro() {
        System.out.println("SERVER app has been started.");
        System.out.println("This is " + getWebshopName());
        System.out.println("############################");
        System.out.println();
    }

    private void showWaiting() {
        System.out.println("Awaiting requests...");
    }

    private void initProductList() {
        availableProducts = new ArrayList<>();
        availableProducts.add("Apple");
        availableProducts.add("Banana");
        availableProducts.add("Orange");
        availableProducts.add("Kiwi");
        availableProducts.add("Pear");
        availableProducts.add("Strawberry");
        availableProducts.add("Berry");
    }

    private Boolean isProductAvailable(String productName) {
        return availableProducts.contains(productName);
    }

    private String getWebshopName() {
        return "Test webshop";
    }

    public static void main(String[] args) {
        Runnable runnable = new ServerApp();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
