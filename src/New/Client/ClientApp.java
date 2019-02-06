package New.Client;

import New.Shared.Connector;
import New.Shared.IManualListener;
import New.Shared.MessageProductResult;
import New.Shared.MessageProductSearch;

import javax.jms.JMSException;
import java.util.Scanner;

public class ClientApp implements IManualListener, Runnable {

    private static Connector connector;


    private void initConnector() {
        connector = new Connector("ProductQueue", this);
    }

    @Override
    public void onProductSearchReceived(MessageProductSearch message) {
        System.out.println("OEI!");
    }

    @Override
    public void onProductResultReceived(MessageProductResult message) {
        System.out.println("Received result from webshop " + message.getWebshop() + " for product: " + message.getProductName() + ".");

        if (message.getIsAvailable()) {
            System.out.println(message.getWebshop() + " has " + message.getAmountAvailable() + " " + message.getProductName() + "s available for $" + message.getPrice() + " each.");
        }
        else {
            System.out.println(message.getWebshop() + " does NOT have any " + message.getProductName() + "s available...");
        }
    }

    @Override
    public void run() {
        initConnector();

        String input = showIntro();
        try {
            connector.sendMessage(new MessageProductSearch(input));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String showIntro() {
        System.out.println("CLIENT app has been started.");
        System.out.println("############################");
        System.out.println();
        System.out.println("Please input what kind of product you're interested in: ");

        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        Runnable runnable = new ClientApp();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
