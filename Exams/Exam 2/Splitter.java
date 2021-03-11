
import java.util.*;

public class Splitter {
    public List<Thing> things;          // the Things being split
    public String property;             // one of the properties of the Things, used here to split them into those with and those without
    public Splitter with, without;      // Splitters for the subsets of Things with and without the property

    public Splitter(List<Thing> things) {
        this.things = things;
        chooseProperty();
        split();
    }

    public void chooseProperty() {
        // TODO: your code here
        // Cast things to list of strings
//        List<List<String>> listOfThings = new ArrayList<>();
        for (Thing thing : this.things) {
            // pick one of it's properties
            for (String property : thing.properties) {
                int count = 1;                          // current count for the specific property
                // Check if the other things have it
                for (Thing otherThing : this.things) {
                    // Make sure it's not current thing!
                    if (otherThing != thing && otherThing.properties.contains(property)) {
                        count += 1; // if it has the property, increment
                    }
                }

                // Finally, if we divide count by total and get something between 0.4 and 0.6, let's keep it and exit
                int total = things.size();
                double ratio = (double) count/total;
                if (ratio < 0.6 && ratio > 0.4) {
                    this.property = property;       // save the property
                    break;                          // break the loop
                }
            }
        }
    }

    public void split() {
        // TODO: your code here
        // Initialize new lists to hold items with and without property respectively
        List<Thing> withProperty = new ArrayList<>();
        List<Thing> withoutProperty = new ArrayList<>();

        // Loop over every Thing in this.things
        for (Thing thing : this.things) {
            // If thing has property, add to withProperty
            if (thing.properties.contains(property)) {
                withProperty.add(thing);
            }
            // else Thing doesn't have property --> add to withoutProperty
            else {
                withoutProperty.add(thing);
            }
        }

        // IF both lists (withProperty and withoutProperty) are non-empty,
        // create new Splitters and insert into the instance variables
        if (withProperty.size() != 0 && withoutProperty.size() != 0){
            this.with = new Splitter(withProperty);
            this.without = new Splitter(withoutProperty);
        }
    }
}