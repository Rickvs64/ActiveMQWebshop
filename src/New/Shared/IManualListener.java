package New.Shared;

/**
 * MANUAL listener
 * Used by executable/runnable classes to tell the Connector
 * they wish to be notified upon receiving new messages.
 */
public interface IManualListener {
    void onProductSearchReceived(MessageProductSearch message);
    void onProductResultReceived(MessageProductResult message);
}
