import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DequeTest {
  private Deque<Integer> deque;

  @BeforeEach
  void Setup() {
    deque = new Deque<>();
  }

  @Test
  void WhenDequeIsNewlyCreatedItIsEmpty(){
    assertTrue(deque.isEmpty());
  }

  @Test
  void WhenItemIsAddedFirstToEmptyDequeSizeReturnsOne(){
    deque.addFirst(10);
    assertEquals(1,deque.size());
  }

  @Test
  void WhenItemIsAddedLastToEmptyDequeSizeReturnsOne(){
    deque.addLast(10);
    assertEquals(1,deque.size());
  }

  @Test
  void WhenItemIsRemovedFirstFromSingleItemDequeSizeReturnsZero(){
    deque.addFirst(10);
    int returned = deque.removeFirst();
    assertEquals(0,deque.size());
    assertEquals(10,returned);
  }

  @Test
  void WhenItemIsRemovedLastFromSingleItemDequeSizeReturnsZero(){
    deque.addFirst(10);
    int returned = deque.removeLast();
    assertEquals(0,deque.size());
    assertEquals(10,returned);
  }

  @Test
  void WhenTwoItemsAddedFirstAndRemovedLastToEmptyDequeItAgainBecomesEmpty(){
    deque.addFirst(10);
    deque.addFirst(20);
    int returned = deque.removeLast();
    assertEquals(10,returned);

    returned = deque.removeLast();
    assertEquals(20,returned);

    assertEquals(0,deque.size());
    assertTrue(deque.isEmpty());

  }

  @Test
  void WhenItemsAreAlternatelyAddedFirstAndRemovedLastToEmptyDequeItsSizeNeverExceedsOne(){
    deque.addFirst(10);
    assertTrue(deque.size() <= 1);
    int returned = deque.removeLast();
    assertTrue(deque.size() <= 1);
    assertEquals(10,returned);

    deque.addFirst(20);
    assertTrue(deque.size() <= 1);
    returned = deque.removeLast();
    assertTrue(deque.size() <= 1);
    assertEquals(20,returned);

    assertTrue(deque.isEmpty());

  }

  @Test
  void WhenThreeItemsAreAddedFirstAndThenRemovedLastToEmptyDequeItBecomesEmpty(){
    deque.addFirst(10);
    deque.addFirst(20);
    deque.addFirst(30);

    int returned = deque.removeLast();
    assertEquals(10,returned);

    returned = deque.removeLast();
    assertEquals(20,returned);

    returned = deque.removeLast();
    assertEquals(30,returned);

    assertTrue(deque.isEmpty());
  }

  @Test
  void WhenItemsAreAddedToDequeTheyCanBeIteratedFromFrontToEnd() {
    int[] items = new int[3];
    items[0] = 10;
    items[1] = 20;
    items[2] = 30;

    for (int item : items) {
      deque.addFirst(item);
    }

    Iterator<Integer> iterator = deque.iterator();

    int count = 2;
    while (iterator.hasNext()) {
      assertEquals(items[count], (int) iterator.next());
      count--;
    }

  }

  @Test
  void WhenMultipleItemsAreAddedLastAndThenOneRemovedLastTheRecentlyAddedItemIsReturned() {
    deque.addLast(1);
    deque.addLast(2);
    deque.addLast(3);
    deque.addLast(4);
    deque.addLast(5);
    deque.addLast(6);
    deque.addLast(7);

    int returned = deque.removeLast();
    assertEquals(7, returned);
  }

  @Test
  void AddRemoveFirstNodeTest() {
    deque.addFirst(1);
    deque.removeFirst()  ;
    deque.addFirst(3);
    deque.removeFirst()  ;
    deque.isEmpty()       ;
    deque.addFirst(6);
    deque.addFirst(7);
    deque.removeFirst()  ;
    int returned = deque.removeFirst();
    assertEquals(returned, 6);
  }

  @Test
  void randomTest() {

    deque.addLast(0);
    deque.addLast(1);
    deque.addLast(2);
    deque.addLast(3);
    deque.addLast(4);
    deque.removeFirst() ;
    deque.addLast(6);
    deque.isEmpty()     ;
    deque.addLast(8);
    int returned = deque.removeFirst();
    assertEquals(returned, 1);
  }
}