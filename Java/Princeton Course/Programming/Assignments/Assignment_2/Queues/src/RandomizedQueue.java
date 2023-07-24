import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item>{

  private Item[] queue;
  private int count;
  private int front;
  private int rear;


  public RandomizedQueue() {
    initialize();
  }

  private void initialize() {
    queue = (Item[]) new Object[2];
    count = 0;
    front = -1;
    rear = -1;
  }

  @Override
  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  public boolean isEmpty() {
    return count == 0;
  }

  public void enqueue(Item i) {
    if(i == null) {
      throw new java.lang.IllegalArgumentException();
    }

    if(isEmpty()) {
      queue[0] = i;
      front = 0;
      rear = 0;
    }
    else {
        if (rear+1 == queue.length) {
          queue = resize(queue.length * 2);
        }
        queue[++rear] = i;
    }
    count++;
    }

  private Item[] resize(int newSize) {
    Item[] newQueue = (Item[]) new Object[newSize];
    int newCount = 0;
    for(int i=0; i <= rear; i++ ){
      if (queue[i] != null) {
        newQueue[newCount++] = queue[i];
      }
    }
    front = 0;
    rear = newCount - 1;
    return newQueue;
  }

  public int size() {
    return count;
  }

  public Item dequeue() {
    if(isEmpty()) {
      throw new java.util.NoSuchElementException();
    }

    int random = StdRandom.uniform(front, rear + 1);
    Item item = queue[random];
    while(item == null) {
      random = StdRandom.uniform(front, rear + 1);
      item = queue[random];
    }

    queue[random] = null;
    count--;

    if (queue.length > 8 && count <= queue.length/4) {
      queue = resize(queue.length/2);
    }
    return item;
  }

  public Item sample() {
    if(isEmpty()) {
      throw new java.util.NoSuchElementException();
    }
    int random = StdRandom.uniform(front, rear+1);
    Item item = queue[random];
    while (item==null){
      random = StdRandom.uniform(front, rear+1);
      item = queue[random];
    }
    return item;
  }

  private class ListIterator implements Iterator<Item> {

    int iterator = 0;
    int[] randoms = new int[rear+1];
    int iterateCount = 0;

    ListIterator() {
      if(count==0) return;
      for (int i =0; i<=rear; i++) {
        randoms[i] = i;
      }
      StdRandom.shuffle(randoms);
    }

    @Override
    public boolean hasNext() {
      return count > 0 && iterator <= rear && iterateCount < count;
    }

    @Override
    public Item next() {
      if(!hasNext()) {
        throw new java.util.NoSuchElementException();
      }
      int random = randoms[iterator++];
      Item item = queue[random];
      while (item==null && iterator <= rear){
        random = randoms[iterator++];
        item = queue[random];
      }
      iterateCount++;
      return item;
    }
  }
}
