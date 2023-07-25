import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	// write your code here
        OlympicAthlete olympicAthlete1 = new OlympicAthlete("Sharad",25,"Football");
        OlympicAthlete olympicAthlete2 = new OlympicAthlete("Sharad",25,"Football");
//        OlympicAthlete olympicAthlete2 = new OlympicAthlete("Sharad",4,"Soccer");
        HashMap<OlympicAthlete, Integer> countByAthelets = new HashMap<>();
        countByAthelets.put(olympicAthlete1,1);
//        countByAthelets.put(olympicAthlete2,2);

        countByAthelets.get(olympicAthlete2);
    }
}
