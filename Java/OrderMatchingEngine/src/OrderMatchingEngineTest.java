import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMatchingEngineTest {

  OrderMatchingEngine orderMatchingEngine;
  Thread orderMatchingEngineThread;
  @BeforeEach
  void setUp() {
    orderMatchingEngine = new OrderMatchingEngine();
    orderMatchingEngineThread = new Thread(orderMatchingEngine);
    orderMatchingEngineThread.start();
  }

  @AfterEach
  void tearDown() {
    orderMatchingEngine.clearOutstandingOrders();
    orderMatchingEngine.stop();
    try {
      orderMatchingEngineThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void buyOrdersAreFilledForOutstandingSellOrder() throws InterruptedException {
    AddOrderRequest sellOrderRequest = new AddOrderRequest(1, Side.SELL, 100, 10, OrderType.LIMIT);
    AddOrderRequest buyOrderRequest1 = new AddOrderRequest(2, Side.BUY, 50, 10, OrderType.LIMIT);
    AddOrderRequest buyOrderRequest2 = new AddOrderRequest(3, Side.BUY, 50, 11, OrderType.LIMIT);

    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest1, buyOrderRequest2, sellOrderRequest)));
    Thread.sleep(1000);
    assertEquals(6, orderMatchingEngine.messageBus.size());
    assertEquals(0, sellOrderRequest.quantity - sellOrderRequest.filledQuantity);
    assertEquals(0, buyOrderRequest1.quantity - buyOrderRequest1.filledQuantity);
    assertEquals(0, buyOrderRequest2.quantity - buyOrderRequest2.filledQuantity);
  }

  @Test
  void sellOrdersAreFilledForOutstandingBuyOrder() throws InterruptedException {
    AddOrderRequest buyOrderRequest = new AddOrderRequest(1, Side.BUY, 100,11, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest1 = new AddOrderRequest(2, Side.SELL, 50,10, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest2 = new AddOrderRequest(3, Side.SELL, 50,11, OrderType.LIMIT);
    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest, sellOrderRequest1, sellOrderRequest2)));
    Thread.sleep(1000);
    assertEquals(6, orderMatchingEngine.messageBus.size());
    assertEquals(0, sellOrderRequest1.quantity - sellOrderRequest1.filledQuantity);
    assertEquals(0, sellOrderRequest2.quantity - sellOrderRequest2.filledQuantity);
    assertEquals(0, buyOrderRequest.quantity - buyOrderRequest.filledQuantity);
  }


  @Test
  void buyOrderIsFilledWithSellOrdersWithSamePriceButDifferentAge() throws InterruptedException {
    Instant timestamp = Instant.now();
    Instant oneMinuteLater = timestamp.plusSeconds(60);
    AddOrderRequest buyOrderRequest = new AddOrderRequest(1, Side.BUY, 100,11, OrderType.LIMIT);
    // This is older order on timeline and should be matched before younger order is matched
    AddOrderRequest sellOrderRequest1 = new AddOrderRequest(3, Side.SELL, 50,10, timestamp, OrderType.LIMIT);
    // This is younger order on timeline and should be matched after older order is matched
    AddOrderRequest sellOrderRequest2 = new AddOrderRequest(2, Side.SELL, 100,10, oneMinuteLater, OrderType.LIMIT);

    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest, sellOrderRequest1, sellOrderRequest2)));
    Thread.sleep(1000);
    assertEquals(6, orderMatchingEngine.messageBus.size());
    assertEquals(50, sellOrderRequest2.quantity - sellOrderRequest2.filledQuantity);
    assertEquals(0, sellOrderRequest1.quantity - sellOrderRequest1.filledQuantity);
    assertEquals(0, buyOrderRequest.quantity - buyOrderRequest.filledQuantity);
  }


  @Test
  void sellOrderIsFilledWithBuyOrdersWithSamePriceButDifferentAge() throws InterruptedException {
    Instant timestamp = Instant.now();
    Instant oneMinuteEarlier = timestamp.minusSeconds(60);
    Instant twoMinutesEarlier = timestamp.minusSeconds(120);
    AddOrderRequest sellOrderRequest = new AddOrderRequest(1, Side.SELL, 100,10, OrderType.LIMIT);
    // This is the oldest order on timeline and should be matched before other orders
    AddOrderRequest buyOrderRequest1 = new AddOrderRequest(3, Side.BUY, 50,10, twoMinutesEarlier, OrderType.LIMIT);
    // This is next order on timeline and should be matched after oldest order is matched
    AddOrderRequest buyOrderRequest2 = new AddOrderRequest(2, Side.BUY, 20,10, oneMinuteEarlier, OrderType.LIMIT);
    // This is the youngest order on timeline and should be matched after all the other orders are matched
    AddOrderRequest buyOrderRequest3 = new AddOrderRequest(3, Side.BUY, 50,10, timestamp, OrderType.LIMIT);

    orderMatchingEngine.process(new ArrayList<>(List.of(sellOrderRequest, buyOrderRequest1, buyOrderRequest2, buyOrderRequest3)));
    Thread.sleep(1000);
    assertEquals(9, orderMatchingEngine.messageBus.size());
    assertEquals(0, sellOrderRequest.quantity - sellOrderRequest.filledQuantity);
    assertEquals(0, buyOrderRequest1.quantity - buyOrderRequest1.filledQuantity);
    assertEquals(0, buyOrderRequest2.quantity - buyOrderRequest2.filledQuantity);
    assertEquals(20, buyOrderRequest3.quantity - buyOrderRequest3.filledQuantity);
  }

  @Test
  void singleBuyAndSellOrderIsMatchedOutOfMultipleOutstandingOrders() throws InterruptedException {
    AddOrderRequest buyOrderRequest1 = new AddOrderRequest(1, Side.BUY, 50, 22, OrderType.LIMIT);
    AddOrderRequest buyOrderRequest2 = new AddOrderRequest(2, Side.BUY, 50, 20, OrderType.LIMIT);
    AddOrderRequest buyOrderRequest3 = new AddOrderRequest(3, Side.BUY, 50, 18, OrderType.LIMIT);
    AddOrderRequest buyOrderRequest4 = new AddOrderRequest(4, Side.BUY, 50, 10, OrderType.LIMIT);

    AddOrderRequest sellOrderRequest1 = new AddOrderRequest(1, Side.SELL, 50, 21, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest2 = new AddOrderRequest(2, Side.SELL, 50, 25, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest3 = new AddOrderRequest(3, Side.SELL, 50, 35, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest4 = new AddOrderRequest(4, Side.SELL, 50, 38, OrderType.LIMIT);

    orderMatchingEngine.process(
            new ArrayList<>(List.of(buyOrderRequest1, buyOrderRequest2, buyOrderRequest3, buyOrderRequest4,
                    sellOrderRequest1, sellOrderRequest2, sellOrderRequest3, sellOrderRequest4
            ))
    );
    Thread.sleep(1000);
    assertEquals(3, orderMatchingEngine.messageBus.size());
    assertEquals(0, sellOrderRequest1.quantity - sellOrderRequest1.filledQuantity);
    assertEquals(0, buyOrderRequest1.quantity - buyOrderRequest1.filledQuantity);
    assertNotEquals(0, buyOrderRequest2.quantity - buyOrderRequest2.filledQuantity);
    assertNotEquals(0, buyOrderRequest3.quantity - buyOrderRequest3.filledQuantity);
    assertNotEquals(0, buyOrderRequest4.quantity - buyOrderRequest4.filledQuantity);
    assertNotEquals(0, sellOrderRequest2.quantity - sellOrderRequest2.filledQuantity);
    assertNotEquals(0, sellOrderRequest3.quantity - sellOrderRequest3.filledQuantity);
    assertNotEquals(0, sellOrderRequest4.quantity - sellOrderRequest4.filledQuantity);

  }

  @Test
  void orderIsCancelledBeforeItGetsExecuted() throws InterruptedException {
    AddOrderRequest buyOrderRequest1 = new AddOrderRequest(1, Side.BUY, 50, 22, OrderType.LIMIT);
    AddOrderRequest buyOrderRequest2 = new AddOrderRequest(2, Side.BUY, 50, 21, OrderType.LIMIT);

    AddOrderRequest sellOrderRequest1 = new AddOrderRequest(1, Side.SELL, 50, 21, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest2 = new AddOrderRequest(2, Side.SELL, 50, 21, OrderType.LIMIT);
    CancelOrderRequest cancelOrderRequest2 = new CancelOrderRequest(2, Side.SELL, 50, 21, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest3 = new AddOrderRequest(3, Side.SELL, 50, 21, OrderType.LIMIT);

    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest1, sellOrderRequest1, sellOrderRequest2)));
    orderMatchingEngine.process(cancelOrderRequest2);
    orderMatchingEngine.process(buyOrderRequest2);
    orderMatchingEngine.process(sellOrderRequest3);
    Thread.sleep(3000);
    assertEquals(6, orderMatchingEngine.messageBus.size());
    assertEquals(0, sellOrderRequest1.quantity - sellOrderRequest1.filledQuantity);
    assertEquals(sellOrderRequest2, cancelOrderRequest2);
    assertEquals(OrderStatus.CANCELLED, sellOrderRequest2.orderStatus);
    // buyOrderRequest2 and sellOrderRequest3 are matched as sellOrderRequest2 is cancelled
    assertEquals(0, buyOrderRequest2.quantity - buyOrderRequest2.filledQuantity);
    assertEquals(0, sellOrderRequest3.quantity - sellOrderRequest3.filledQuantity);
  }

  @Test
  void buyMarketOrderIsExecutedAheadOfLimitOrderAndEvenWithoutAPriceMatch() throws InterruptedException {
    AddOrderRequest buyOrderRequest1 = new AddOrderRequest(1, Side.BUY, 50, 22, OrderType.LIMIT);
    AddOrderRequest buyOrderRequest2 = new AddOrderRequest(2, Side.BUY, 50, 22, OrderType.MARKET);

    AddOrderRequest sellOrderRequest1 = new AddOrderRequest(1, Side.SELL, 25, 21, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest2 = new AddOrderRequest(2, Side.SELL, 25, 25, OrderType.LIMIT);

    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest1, buyOrderRequest2)));
    orderMatchingEngine.process(sellOrderRequest1);
    orderMatchingEngine.process(sellOrderRequest2);
    Thread.sleep(1000);
    assertEquals(6, orderMatchingEngine.messageBus.size());
    assertEquals(0, sellOrderRequest1.quantity - sellOrderRequest1.filledQuantity);
    assertEquals(0, sellOrderRequest2.quantity - sellOrderRequest2.filledQuantity);
    assertEquals(50, buyOrderRequest1.quantity - buyOrderRequest1.filledQuantity);
    assertEquals(0, buyOrderRequest2.quantity - buyOrderRequest2.filledQuantity);
  }

  @Test
  void stopLossSellOrderExecutedAsMarketOrderWhenCurrentPriceCrossesStopLossPrice() throws InterruptedException {
    AddOrderRequest sellOrderRequest1 = new AddOrderRequest(2, Side.SELL, 50, 22, OrderType.STOP_LOSS);

    AddOrderRequest buyOrderRequest2 = new AddOrderRequest(3, Side.BUY, 25, 21, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest2 = new AddOrderRequest(4, Side.SELL, 25, 21, OrderType.LIMIT);

    AddOrderRequest buyOrderRequest3 = new AddOrderRequest(5, Side.BUY, 50, 25, OrderType.LIMIT);

    orderMatchingEngine.process(new ArrayList<>(List.of(sellOrderRequest1)));
    // matching of following two orders will trigger sellOrderRequest1 as MARKET order which will cross with buyOrderRequest3
    // execute trade to set current price to be 21 to trigger SELL SL order sellOrderRequest1
    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest2, sellOrderRequest2)));
    // wait before sending order that will trigger stop loss to become market order
    Thread.sleep(2000);
    // sellOrderRequest1 which is now Market order will be matched with following
    orderMatchingEngine.process(buyOrderRequest3);


    Thread.sleep(3000);
    assertEquals(6, orderMatchingEngine.messageBus.size());
    assertEquals(0, sellOrderRequest1.quantity - sellOrderRequest1.filledQuantity);
    assertEquals(0, buyOrderRequest2.quantity - buyOrderRequest2.filledQuantity);
    assertEquals(0, sellOrderRequest2.quantity - sellOrderRequest2.filledQuantity);
    assertEquals(0, buyOrderRequest3.quantity - buyOrderRequest3.filledQuantity);
  }

  @Test
  void stopLossBuyOrderExecutedAsMarketOrderWhenCurrentPriceCrossesStopLossPrice() throws InterruptedException {
    AddOrderRequest buyOrderRequest1 = new AddOrderRequest(1, Side.BUY, 50, 27, OrderType.STOP_LOSS);

    AddOrderRequest buyOrderRequest4 = new AddOrderRequest(6, Side.BUY, 25, 28, OrderType.LIMIT);
    AddOrderRequest sellOrderRequest4 = new AddOrderRequest(7, Side.SELL, 25, 28, OrderType.LIMIT);

    AddOrderRequest sellOrderRequest3 = new AddOrderRequest(8, Side.SELL, 50, 26, OrderType.LIMIT);

    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest1)));
    // matching of following two orders will trigger buyOrderRequest1 as MARKET order which will cross with sellOrderRequest3
    // execute trade to set current price to be 28 which will trigger buyOrderRequest1
    orderMatchingEngine.process(new ArrayList<>(List.of(buyOrderRequest4, sellOrderRequest4)));
    // wait before sending order that will trigger stop loss to become market order
    Thread.sleep(2000);
    // buyOrderRequest1 which is now Market order will be matched with following
    orderMatchingEngine.process(sellOrderRequest3);

    Thread.sleep(3000);
    assertEquals(6, orderMatchingEngine.messageBus.size());
    assertEquals(0, buyOrderRequest4.quantity - buyOrderRequest4.filledQuantity);
    assertEquals(0, sellOrderRequest4.quantity - sellOrderRequest4.filledQuantity);
    assertEquals(0, sellOrderRequest3.quantity - sellOrderRequest3.filledQuantity);
    assertEquals(0, buyOrderRequest1.quantity - buyOrderRequest1.filledQuantity);
  }
}