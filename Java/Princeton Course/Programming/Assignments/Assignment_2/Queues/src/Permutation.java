import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);

    RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

    while (!StdIn.isEmpty()) {
      String input = StdIn.readString();
      if(!input.equals("-")) {
        randomizedQueue.enqueue(input);
      }
      else {
        break;
      }
    }
    //String[] strings = input.split(" ");
    //Deque<String> deque = new Deque<>();

    /*for (String string : strings) {
      randomizedQueue.enqueue(string);
    }*/

    for(int i=0; i<k; i++) {
      StdOut.println(randomizedQueue.dequeue());
    }
  }
}
