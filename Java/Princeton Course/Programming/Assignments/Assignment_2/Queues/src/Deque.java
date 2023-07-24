import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

  private Node front;
  private Node rear;
  private int count;

  public Deque() {
    front = null;
    rear = null;
    count = 0;
  }

  public void addFirst(Item i) {
    if(i == null) {
      throw new java.lang.IllegalArgumentException();
    }

    try {
      Node node  = new Node();
      node.item = i;
      node.next = null;
      node.prev = null;

      if(isEmpty()) {
        addFirstNode(node);
        rear = node;
      }
      else {
        addFirstNode(node);
      }

      count += 1;
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }


  private void addFirstNode(Node node) {
    Node oldFront = front;
    front = node;
    node.next = oldFront;
    if (oldFront != null)
      oldFront.prev = node;
  }


  public int size() {
    return count;
  }

  public Item removeFirst() {
    if(isEmpty()) {
      throw new java.util.NoSuchElementException();
    }

    Item item = front.item;

    if (front == rear){
      front = null;
      rear = null;
    }
    else {
      removeFirstNode();
    }
    count -= 1;

    return item;
  }

  private void removeFirstNode() {
    Node oldFront = front;
    front = front.next;
    front.prev = null;
    oldFront.next = null;
    oldFront.prev = null;
  }

  public void addLast(Item i) {
    if(i == null) {
      throw new java.lang.IllegalArgumentException();
    }

    try {
      Node node = new Node();
      node.item = i;
      node.next = null;
      node.prev = null;

      if(isEmpty()) {
        addFirstNode(node);
        rear = node;
      }
      else {
        Node oldRear = rear;
        rear = node;
        oldRear.next = node;
        node.prev = oldRear;
      }
      count += 1;
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }



  public Item removeLast() {
    if(isEmpty()) {
      throw new java.util.NoSuchElementException();
    }

    Item item = rear.item;

    if (front == rear){
      front = null;
      rear = null;
    }
    else {
      Node last = rear;
      rear = last.prev;
      rear.next = null;

      /*Node traverse = front;
      while (traverse.next.next != null) {
        traverse = traverse.next;
      }
      rear = traverse;
      rear.next = null;*/
    }
    count -= 1;

    return item;
  }

  private class Node {
    Item item;
    Node next;
    Node prev;
  }

  public boolean isEmpty() {
    return front == null && rear == null;
  }

  @Override
  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  private class ListIterator implements Iterator<Item>{
    private Node current = front;
    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public Item next() {
      if(!hasNext()){
        throw new java.util.NoSuchElementException();
      }
      Item item = current.item;
      current = current.next;
      return item;
    }
  }
}

