import java.util.*;

public class Permutation {

  /*
    Check out this method. This is nothing but climbing stairs problem LC#70 (LeetCode#70). It can also be solved by DP (check LC submissions for that).
    Following can be said a brute force method which uses our fundamental recursive permutation finding method!!
    This solution can also be used for question asked in Apple interview to find all the combinations of numbers less than a given max value from the input set of numbers!!
  */
  private void getPermutationsLessThanSum(Set<Integer> set, int sum, int maxSum, List<Integer> path, List<List<Integer>> result) {
    if (sum > maxSum) return;
    if (sum == maxSum) {
      result.add(new ArrayList<>(path));
      return;
    }

    for (int num : new HashSet<>(set)) {
//      set.remove(num);
      path.add(num);
      sum = sum + num;
      getPermutationsLessThanSum(set, sum, maxSum, path, result);
      sum = sum - num;
      path.remove(path.size()-1);
//      set.add(num);
    }
  }

  private void getPermutations(Set<Integer> set, List<Integer> path, List<List<Integer>> result) {
    if (set.isEmpty()) {
      result.add(new ArrayList<>(path));
      return;
    }

    for (int num : new HashSet<>(set)) {
      set.remove(num);
      path.add(num);
      getPermutations(set, path, result);
      path.remove(path.size()-1);
      set.add(num);
    }
  }

  public List<List<Integer>> getPermutations(Set<Integer> set) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    getPermutations(set, path, result);
    return result;
  }

  public List<List<Integer>> getPermutationsLessThanSum(Set<Integer> set, int sum) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    getPermutationsLessThanSum(set, 0, sum,path, result);
    return result;
  }


  public static void main(String[] args) {
      System.out.println("hi sharad sara pranali");
      Permutation permutation = new Permutation();
//      List<List<Integer>> result = permutation.getPermutations(new HashSet<>(Arrays.asList(3,2,1)));
      List<List<Integer>> result = permutation.getPermutationsLessThanSum(new HashSet<>(Arrays.asList(1,2)),4);
      for (List<Integer> list : result) {
        System.out.print("[");
        for (int i = 0; i < list.size(); i++) {
          int l = list.get(i);
          String str = i < list.size()-1 ? l +"," : String.valueOf(l);
          System.out.print(str);
        }
        System.out.print("]");
        System.out.println();
      }
  }
}
