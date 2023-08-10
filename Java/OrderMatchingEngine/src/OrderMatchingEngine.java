import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;

interface OrderMessage {
}

enum Side {
  BUY,
  SELL,
}

enum OrderStatus {
  ACTIVE,
  CANCELLED
}

enum OrderType {
  LIMIT,
  MARKET,
  STOP_LOSS
}
final class OrderFullyFilled implements OrderMessage {
  int orderId;
  Instant timestamp = Instant.now();

  public OrderFullyFilled(int orderId) {
    this.orderId = orderId;
  }
  @Override
  public String toString() {
    return "OrderFullyFilled: OrderId=" + orderId;
  }

}
final class OrderPartiallyFilled implements OrderMessage {
  int orderId;
  int filledQuantity;
  int remainingQuantity;
  Instant timestamp = Instant.now();

  public OrderPartiallyFilled(int orderId, int filledQuantity, int remainingQuantity) {
    this.orderId = orderId;
    this.filledQuantity = filledQuantity;
    this.remainingQuantity = remainingQuantity;
  }

  @Override
  public String toString() {
    return "OrderPartiallyFilled: OrderId=" + orderId + ", FilledQuantity=" + filledQuantity + ", RemainingQuantity=" + remainingQuantity;
  }

}
final class TradeEvent implements OrderMessage {
  int quantity;
  double price;
  Instant timestamp;

  public TradeEvent(int quantity, double price) {
    this.quantity = quantity;
    this.price = price;
  }
  @Override
  public String toString() {
    return "TradeEvent: Quantity=" + quantity + ", Price=" + price;
  }

}

class OrderRequest implements OrderMessage {
  int orderId;
  Side side;
  int quantity;
  double price;

  OrderType orderType;
  OrderStatus orderStatus = OrderStatus.ACTIVE;
  Instant timestamp = Instant.now();

  public boolean isMarketOrder() {
    return orderType == OrderType.MARKET;
  }

  public boolean isLimitOrder() {
    return orderType == OrderType.LIMIT;
  }

  public boolean isStopLossOrder() {
    return orderType == OrderType.STOP_LOSS;
  }
  // we define the equality of OrderRequest so that we can compare the incoming orders to those that are outstanding
  // to handle CancelOrderRequest requests
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderRequest that)) return false;
    return quantity == that.quantity &&
            Double.compare(that.price, price) == 0 &&
            that.orderId == orderId &&
            side == that.side &&
            orderType == that.orderType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, side, quantity, price, orderType);
  }
}
final class CancelOrderRequest extends OrderRequest {
  public CancelOrderRequest(int orderId, Side side, int quantity, double price, OrderType orderType) {
    this.orderId = orderId;
    this.side = side;
    this.quantity = quantity;
    this.price = price;
    this.orderType = orderType;
  }

  @Override
  public String toString() {
    return "CancelOrderRequest: OrderId=" + orderId + ", Side=" + side + ", Type="+ orderType +", Quantity=" + quantity + ", Price=" + price;
  }
}
final class AddOrderRequest extends OrderRequest {

  int filledQuantity;

  public AddOrderRequest(int orderId, Side side, int quantity, double price, OrderType orderType) {
    this.orderId = orderId;
    this.side = side;
    this.quantity = quantity;
    this.price = price;
    this.orderType = orderType;
  }

  public AddOrderRequest(int orderId, Side side, int quantity, double price, Instant timestamp, OrderType orderType) {
    this.orderId = orderId;
    this.side = side;
    this.quantity = quantity;
    this.price = price;
    this.timestamp = timestamp;
    this.orderType = orderType;
  }
  @Override
  public String toString() {
    return "AddOrderRequest: OrderId=" + orderId + ", Side=" + side + ", Type="+ orderType + ", Quantity=" + quantity + ", Price=" + price;
  }

}


class OrderRequestComparatorMin implements Comparator<AddOrderRequest> {
  @Override
  public int compare(AddOrderRequest order1, AddOrderRequest order2) {
    if (order1.isMarketOrder() && order2.isMarketOrder()) {
      return order1.timestamp.compareTo(order2.timestamp); // execute the oldest market order first
    } else if (order1.isMarketOrder()) {
      return -1; // Market Order has higher priority over Limit Order
    } else if (order2.isMarketOrder()) {
      return 1; // Other Order types have lower priority than Market Order
    }

    if (order1.price != order2.price) {
      return Double.compare(order1.price, order2.price);
    } else {
      return order1.timestamp.compareTo(order2.timestamp);
    }

  }
}

class OrderRequestComparatorMax implements Comparator<AddOrderRequest> {
  @Override
  public int compare(AddOrderRequest order1, AddOrderRequest order2) {
    if (order1.isMarketOrder() && order2.isMarketOrder()) {
      return order1.timestamp.compareTo(order2.timestamp); // execute the oldest market order first
    } else if (order1.isMarketOrder()) {
      return -1; // Market Order has higher priority over Limit Order
    } else if (order2.isMarketOrder()) {
      return 1; // Other Order types have lower priority than Market Order
    }

    if (order1.price != order2.price) {
      return Double.compare(order2.price, order1.price);
    } else {
      return order1.timestamp.compareTo(order2.timestamp);
    }

  }
}

class StopLossOrderRequestComparatorMin implements Comparator<AddOrderRequest> {
  @Override
  public int compare(AddOrderRequest order1, AddOrderRequest order2) {
    if (order1.price != order2.price) {
      return Double.compare(order1.price, order2.price);
    } else {
      return order1.timestamp.compareTo(order2.timestamp);
    }
  }
}

class StopLossOrderRequestComparatorMax implements Comparator<AddOrderRequest> {
  @Override
  public int compare(AddOrderRequest order1, AddOrderRequest order2) {
    if (order1.price != order2.price) {
      return Double.compare(order2.price, order1.price);
    } else {
      return order1.timestamp.compareTo(order2.timestamp);
    }
  }
}

public class OrderMatchingEngine implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(OrderMatchingEngine.class.getName());

  private double currentPrice;

  private final PriorityQueue<AddOrderRequest> buyOrders = new PriorityQueue<>(new OrderRequestComparatorMax());
  private final PriorityQueue<AddOrderRequest> sellOrders = new PriorityQueue<>(new OrderRequestComparatorMin());

  private final PriorityQueue<AddOrderRequest> stopLossBuyOrders = new PriorityQueue<>(new StopLossOrderRequestComparatorMax());
  private final PriorityQueue<AddOrderRequest> stopLossSellOrders = new PriorityQueue<>(new StopLossOrderRequestComparatorMin());
  // We maintain the map of orders so that marking order CANCELLED is in constant time i.e. O(1) time.
  // This is efficient in practical scenarios when ACTIVE orders outweigh CANCELLED orders. When the CANCELLED order
  // bubbles up to the root of the heap, we just discard it. Also, there is no memory overhead or increase in memory
  // complexity as we are using existing order objects as the key and values of orders hashmap
  Map<OrderRequest, OrderRequest> orders = new HashMap<>();
  Queue<OrderMessage> messageBus = new ConcurrentLinkedQueue<>();
  private volatile boolean running = true;

  public OrderMatchingEngine() {
  }

  public OrderMatchingEngine(
      List<AddOrderRequest> buyOrderRequests, List<AddOrderRequest> sellOrderRequests
  ) {
    buyOrders.addAll(buyOrderRequests);
    for (OrderRequest order : buyOrderRequests) {
      orders.put(order, order);
    }

    sellOrders.addAll(sellOrderRequests);
    for (OrderRequest order : sellOrderRequests) {
      orders.put(order, order);
    }
  }

  public void clearOutstandingOrders() {
    buyOrders.clear();
    sellOrders.clear();
  }

  public void process(List<OrderMessage> orderRequests) {
    for (OrderMessage orderRequest : orderRequests) {
      process(orderRequest);
    }
  }
  public void process(OrderMessage orderRequest) {
    if (orderRequest instanceof AddOrderRequest order) {
      // Adding an order to the PQ/heap has runtime complexity of O(log(n)). That means if there are 1,000,000 orders
      // on the heap, adding a new order would take about 20 steps to bubble up new min or max to the root of the heap
      if (order.isStopLossOrder()) {
        if (order.side == Side.BUY) {
          stopLossBuyOrders.add(order);
        }
        else {
          stopLossSellOrders.add(order);
        }
      }
      else {
        if (order.side == Side.BUY) {
          buyOrders.add(order);
        }
        else {
          sellOrders.add(order);
        }
      }
      orders.put(order, order);
    }

    if (orderRequest instanceof CancelOrderRequest order) {
      OrderRequest existingOrder = orders.getOrDefault(order, null);
      // for simplicity, chosen here to log when Cancel order is not found. In practical application, this error message needs to be
      // returned back the client / order sending application
      if (existingOrder == null) {
        LOGGER.log(Level.WARNING, "CancelOrderRequest could not be found for OrderId=" + order.orderId);
      } else {
        existingOrder.orderStatus = OrderStatus.CANCELLED;
      }
    }
  }

  @Override
  public void run() {
    System.out.println("Starting Order Matching Engine");
    while (running) {
      processStopLossOrders();
      while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
        processStopLossOrders();
        AddOrderRequest buyOrder = buyOrders.peek();
        AddOrderRequest sellOrder = sellOrders.peek();
        if (!running) {
          break;
        }

        // discard the CANCELLED orders
        if (buyOrder.orderStatus == OrderStatus.CANCELLED) {
          orders.remove(buyOrder);
          buyOrders.poll();
          continue;
        }

        if (sellOrder.orderStatus == OrderStatus.CANCELLED) {
          orders.remove(sellOrder);
          sellOrders.poll();
          continue;
        }

        if (sellOrder.price <= buyOrder.price || buyOrder.isMarketOrder() || sellOrder.isMarketOrder()) {
          int buyQuantity = buyOrder.quantity - buyOrder.filledQuantity;
          int sellQuantity = sellOrder.quantity - sellOrder.filledQuantity;
          int currentFillQuantity = Math.min(buyQuantity, sellQuantity);

          double tradePrice;
          // we are executing trade at sell order's price when both are limit orders otherwise we execute at price of
          // the order which is limit order
          if (buyOrder.isLimitOrder() && sellOrder.isLimitOrder()) {
            tradePrice = sellOrder.price;
          }
          else {
            tradePrice = buyOrder.isLimitOrder() ? buyOrder.price : sellOrder.price;
          }

          currentPrice = tradePrice;
          messageBus.add(new TradeEvent(currentFillQuantity, tradePrice));

          if (buyOrder.quantity == buyOrder.filledQuantity + currentFillQuantity) {
            messageBus.add(new OrderFullyFilled(buyOrder.orderId));
          }
          if (buyOrder.quantity > buyOrder.filledQuantity + currentFillQuantity) {
            messageBus.add(
                    new OrderPartiallyFilled(
                            buyOrder.orderId,
                            currentFillQuantity,
                            buyOrder.quantity - buyOrder.filledQuantity - currentFillQuantity
                    )
            );
          }
          if (sellOrder.quantity == sellOrder.filledQuantity + currentFillQuantity) {
            messageBus.add(new OrderFullyFilled(sellOrder.orderId));
          }
          if (sellOrder.quantity > sellOrder.filledQuantity + currentFillQuantity) {
            messageBus.add(
                    new OrderPartiallyFilled(
                            sellOrder.orderId,
                            currentFillQuantity,
                            sellOrder.quantity - sellOrder.filledQuantity - currentFillQuantity
                    )
            );
          }

          buyOrder.filledQuantity += currentFillQuantity;
          sellOrder.filledQuantity += currentFillQuantity;

          // We remove the orders which are fully filled. Since our data structure is PriorityQueue (min or max heap),
          // The runtime complexity of this operation will be O(log(n)) which is pretty good e.g. if there are 1,000,000
          // trades on our heap, then polling would take about 20 steps to re-balance the heap
          if (buyOrder.quantity == buyOrder.filledQuantity) {
            orders.remove(buyOrder);
            buyOrders.poll();
          }
          if (sellOrder.quantity == sellOrder.filledQuantity) {
            orders.remove(sellOrder);
            sellOrders.poll();
          }
        }
      }
    }
    System.out.println("Stopping Order Matching Engine");
  }

  private void processStopLossOrders() {
    AddOrderRequest stopLossBuyOrder = stopLossBuyOrders.peek();
    AddOrderRequest stopLossSellOrder = stopLossSellOrders.peek();
    // In real application, I would design handling of StopLoss orders a bit differently e.g.
    // 1. Update the current price of the trade from OrderMatchingEngine on message (e.g. kafka) bus topic
    // 2. A separate StopLoss order processor application would subscribe to current price topic
    // 3. When currentPrice satisfies the if conditions below, call the OrderMatchingEngine's process
    if (stopLossBuyOrder != null && currentPrice > 0 && currentPrice >= stopLossBuyOrder.price) {
      stopLossBuyOrder.orderType = OrderType.MARKET;
      stopLossBuyOrders.poll();
      buyOrders.add(stopLossBuyOrder);
    }
    if (stopLossSellOrder != null && currentPrice > 0 && currentPrice <= stopLossSellOrder.price) {
      stopLossSellOrder.orderType = OrderType.MARKET;
      stopLossSellOrders.poll();
      sellOrders.add(stopLossSellOrder);
    }
  }

  public void stop() {
    running = false;
  }
}

  class OrderGeneratorEngine implements Runnable {
    private final Random random = new Random();
    private final OrderMatchingEngine orderMatchingEngine;
    private volatile boolean running = true;
    public OrderGeneratorEngine(OrderMatchingEngine orderMatchingEngine) {
      this.orderMatchingEngine = orderMatchingEngine;
    }

    @Override
    public void run() {
      System.out.println("Starting Message Generator");
      while (running) {
        int randomNum = random.nextInt(7); // any number between 0 and 5
        int orderId = random.nextInt(1000); // e.g. XXX
        Side side = random.nextBoolean() ? Side.BUY : Side.SELL;
        int quantity = random.nextInt(100) + 1; // Random quantity between 1 and 100
        double price = (double) random.nextInt(1000) + 1.0; // Random price between 1.0 and 1000.0
        OrderRequest orderRequest;
        if (randomNum == 0) {
          orderRequest = new CancelOrderRequest(orderId, side, quantity, price, OrderType.LIMIT);
        }
        else {
          if (randomNum == 1) {
            orderRequest = new CancelOrderRequest(orderId, side, quantity, price, OrderType.MARKET);
          } else {
            orderRequest = new AddOrderRequest(orderId, side, quantity, price, OrderType.LIMIT);
          }
        }
        System.out.println(orderRequest);
        orderMatchingEngine.process(orderRequest);
        while (!orderMatchingEngine.messageBus.isEmpty()) {
          System.out.println(orderMatchingEngine.messageBus.poll());
        }
        // wait for 50ms second before processing next order
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println("Stopping Message Generator");
    }

    public void stop() {
      running = false;
    }
  }

  class DriverApplication {
    public static void main(String[] args) {

      OrderMatchingEngine orderMatchingEngine = new OrderMatchingEngine();
      // To support multiple securities, we would need to create a separate order matching engine for each one of them.
      // Ideally the orders would be coming in from message bus from which, depending on the security, we invoke corresponding
      // order matching engine. In real applications when the bid/ask volume of securities can be in millions, a single thread
      // on a shared server for given security can be a bottleneck. we may need to dedicate a server instance itself to
      // the securities which have such a high volume. We can also design multiple load-balanced feeder servers e.g. to hold
      // the incoming messages over low latency / high throughput queue such as kafka before they are offered to the heap server
      // for actual matching of the bid/ask
      OrderGeneratorEngine orderGeneratorEngine = new OrderGeneratorEngine(orderMatchingEngine);

      Thread orderGeneratorEngineThread = new Thread(orderGeneratorEngine);
      orderGeneratorEngineThread.start();

      Thread orderMatchingEngineThread = new Thread(orderMatchingEngine);
      orderMatchingEngineThread.start();

      // sleep for 5 seconds to let the both the engines run
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // clear out unfilled buy and sell orders
      orderMatchingEngine.clearOutstandingOrders();

      // stop the message generator thread and order matching engine thread
      orderGeneratorEngine.stop();
      orderMatchingEngine.stop();

      // wait for the threads to finish
      try {
        orderGeneratorEngineThread.join();
        orderMatchingEngineThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
