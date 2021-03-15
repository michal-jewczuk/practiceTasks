package Task3;

import java.util.Comparator;

public class CatComparator implements Comparator<Cat> {
    @Override
    public int compare(Cat o1, Cat o2) {
        if (o1.equals(o2)) {
            int livesCompared = (-1) * Integer.compare(o1.getLives(), o2.getLives());
            return livesCompared;
        }
        else {
            int namesCompared = Integer.compare(o1.getName().length(), o2.getName().length());
            return namesCompared;
        }
    }
}
