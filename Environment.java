import java.util.HashMap;

// Hint!
// Use the Java API to implement your Environment.
// Browse:
//   https://docs.oracle.com/javase/tutorial/tutorialLearningPaths.html
// Read about Collections.
// Focus on the Map interface and HashMap implementation.
// Also:
//   https://www.tutorialspoint.com/java/java_map_interface.htm
//   http://www.javatpoint.com/java-map
// and elsewhere.

public class Environment {

    HashMap<String, Integer> map = new HashMap<String, Integer>();

    public int put(String key, int val) {
        map.put(key, val);
        return val;
    }
    
    public int get(String key) throws EvalException { 
        return map.get(key);
    }

}
