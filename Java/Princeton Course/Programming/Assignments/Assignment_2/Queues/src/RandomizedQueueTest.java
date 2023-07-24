import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RandomizedQueueTest {

  RandomizedQueue<Integer> randomizedQueue;

  @BeforeEach
  void setUp() {
    randomizedQueue = new RandomizedQueue<>();
  }

  @Test
  void whenThereAreNoItemsAddedRandomizedQueueIsEmpty(){
    assertTrue(randomizedQueue.isEmpty());
  }

  @Test
  void whenSingleItemIsAddedToRandomizedQueueItsSizeIsOne() {
    randomizedQueue.enqueue(10);
    assertEquals(randomizedQueue.size(), 1);
  }

  @Test
  void whenSingleItemIsAddedAndRemovedRandomizedQueueBecomesEmpty() {
    randomizedQueue.enqueue(10);
    int item = randomizedQueue.dequeue();
    assertEquals(10, item);
    assertTrue(randomizedQueue.isEmpty());
  }

  @Test
  void whenMultipleElementsAreAddedAndThenRemovedOneAtaTimeSizeDecreasesAccordingly() {
    randomizedQueue.enqueue(10);
    randomizedQueue.enqueue(20);
    randomizedQueue.enqueue(30);
    randomizedQueue.enqueue(40);
    randomizedQueue.enqueue(50);

    randomizedQueue.dequeue();
    assertEquals(4, randomizedQueue.size());
    randomizedQueue.dequeue();
    assertEquals(3, randomizedQueue.size());
    randomizedQueue.dequeue();
    assertEquals(2, randomizedQueue.size());
    randomizedQueue.dequeue();
    assertEquals(1, randomizedQueue.size());
    randomizedQueue.dequeue();
    assertEquals(0, randomizedQueue.size());
    assertTrue(randomizedQueue.isEmpty());
  }

  @Test
  void whenSampleFunctionCalledDoesNotAffectTheSizeOfQueue() {
    randomizedQueue.enqueue(10);
    randomizedQueue.enqueue(20);
    randomizedQueue.enqueue(30);

    int sizeBefore = randomizedQueue.size();
    randomizedQueue.sample();
    int sizeAfter = randomizedQueue.size();

    assertEquals(sizeBefore,sizeAfter);

  }

  @Test
  void whenRandomizedQueueIsEmptyHasNextWithinIteratorReturnsFalse() {

    randomizedQueue.enqueue(0);
    randomizedQueue.enqueue(1);
    randomizedQueue.enqueue(2);
    randomizedQueue.dequeue();
    randomizedQueue.dequeue();
    randomizedQueue.dequeue();

    assertFalse(randomizedQueue.iterator().hasNext());
  }

  @Test
  void whenQueueIsEmptyTheIteratorNextThrowsNoSuchElementException() {
    randomizedQueue.enqueue(0);
    randomizedQueue.enqueue(1);
    randomizedQueue.dequeue();
    randomizedQueue.dequeue();

    assertThrows(NoSuchElementException.class, () -> randomizedQueue.iterator().next());
  }

  @Test
  void whenEnqueueIsCalledMultipleTimesIteratorIteratesThroughCorrectSize() {
    randomizedQueue.enqueue(0);
    randomizedQueue.enqueue(1);
    randomizedQueue.enqueue(2);
    randomizedQueue.enqueue(3);
    randomizedQueue.enqueue(4);
    randomizedQueue.enqueue(5);
    randomizedQueue.enqueue(6);

    int count = 0;
    Iterator<Integer> iterator = randomizedQueue.iterator();
    while(iterator.hasNext()) {
      iterator.next();
      count++;
    }
    assertEquals(7, count);
  }

  @Test
  void whenRandomItemsAreAddedAndRemovedRandomizedQueueDoesNotCrash() {
    randomizedQueue.enqueue(2);
    randomizedQueue.enqueue(3);
    randomizedQueue.dequeue();
    randomizedQueue.enqueue(1);
    randomizedQueue.enqueue(4);
    randomizedQueue.dequeue();
    randomizedQueue.dequeue();
    randomizedQueue.enqueue(5);

    assertEquals(2, randomizedQueue.size());
  }

  @Test
  void randomTest() {
    randomizedQueue.enqueue(197);
    randomizedQueue.enqueue(163);
    randomizedQueue.enqueue(475);
    randomizedQueue.dequeue();
    randomizedQueue.dequeue();
  }

  @Test
  void whenRandomOperationsOnQueueAreOverIteratorReturnsRemainingItems() {
    int count = 0;

    randomizedQueue.enqueue(2);
    randomizedQueue.enqueue(3);
    randomizedQueue.dequeue();
    randomizedQueue.enqueue(1);
    randomizedQueue.enqueue(4);
    randomizedQueue.enqueue(11);
    randomizedQueue.enqueue(41);
    randomizedQueue.dequeue();
    randomizedQueue.dequeue();
    randomizedQueue.enqueue(5);

    Iterator<Integer> iterator = randomizedQueue.iterator();
    while (iterator.hasNext()) {
      Integer next = iterator.next();
      System.out.println("Element: " + next);
      if(next > 0) {
        count++;
      }
    }

    assertEquals(4, count);
  }
}