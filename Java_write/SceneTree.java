package Java_write;

import java.util.ArrayList;

public class SceneTree {

    public ArrayList<Node> tree = new ArrayList<Node>();
    
    public void register(Node node) {
        tree.add(node);
    }

    public void mainLoop() {
        long prevTime = System.nanoTime();
        while (true) {
            // get delta
            long now = System.nanoTime();
            double delta = (now - prevTime) / 1e9;
            prevTime = now;

            // go through all the nodes
            for (int node=0; node < this.tree.size(); node++) {
                this.tree.get(node).tick(delta, this);
            }
        }
    }
}
