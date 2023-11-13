package main;

/**
 * Your code goes in this file
 * fill in the empty methods to allow for the required
 * operations. You can add any fields or methods you want
 * to help in your implementations.
 */

public class AVLPlayerNode {
    private Player data;
    private double value;
    private AVLPlayerNode parent;
    private AVLPlayerNode leftChild;
    private AVLPlayerNode rightChild;
    private int rightWeight;
    private int balanceFactor;

    public static void main(String[] args) {
        // Creating player objects
        Player bob = new Player("Bob", 2, 1100.0);
        Player carol = new Player("Carol", 1, 1300.0);
        Player dan = new Player("Dan", 3, 1500.0);

        // Creating the AVL tree
        AVLPlayerNode tree = new AVLPlayerNode(bob, bob.getELO());
        tree = tree.insert(dan, dan.getELO());
        tree = tree.insert(carol, carol.getELO());
        // tree = tree.insert(dan, dan.getELO());

        // Print the tree structure before rotations
        System.out.println("Tree structure before insertion:");
        System.out.println(tree.treeString());
        // tree = tree.insert(dan, dan.getELO());

        // Creating the ID tree
        AVLPlayerNode idTree = new AVLPlayerNode(bob, bob.getID());
        idTree = idTree.insert(dan, dan.getID());
        idTree = idTree.insert(carol, carol.getID());

        System.out.println(idTree.treeString());
        System.out.println(idTree.getPlayer(1).getName());

        // tree.rotateLeft();
        // tree = tree.getRoot();
        // System.out.println(tree.treeString());
        // tree.rotateRight();
        // tree = tree.getRoot();
        // System.out.println(tree.treeString());

    }

    public AVLPlayerNode(Player data, double value) {
        this.data = data;
        this.value = value;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.rightWeight = 0;
        this.balanceFactor = 0;
    }

    // This should return the new root of the tree
    // make sure to update the balance factor and right weight
    // and use rotations to maintain AVL condition

    /*
     *
     * Code used from class notes.
     */
    public AVLPlayerNode insert(Player newGuy, double value) {
        AVLPlayerNode z = new AVLPlayerNode(newGuy, value);
        AVLPlayerNode v = null;
        AVLPlayerNode root = this;

        while (root != null) {
            v = root;
            if (z.value < root.value) {
                root.rightWeight++;
                root = root.leftChild;
            } else {
                root = root.rightChild;
            }

        }
        z.parent = v;

        if (v == null) {
            return z;
        } else if (z.value < v.value) {
            v.leftChild = z;
        } else {
            v.rightChild = z;
        }
        rebalanceTree(z);
        return this.getRoot();
    }

    private void rebalanceTree(AVLPlayerNode node) {
        while (node != null) {
            node.updateBalanceFactor();

            // Check balance factor and perform rotations
            if (node.balanceFactor < -1) { // Right heavy
                if (height(node.rightChild.leftChild) > height(node.rightChild.rightChild)) {
                    node.rightChild.rotateRight(); // Right-Left case
                }
                node.rotateLeft(); // Right-Right case
            } else if (node.balanceFactor > 1) { // Left heavy
                if (height(node.leftChild.rightChild) > height(node.leftChild.leftChild)) {
                    node.leftChild.rotateLeft(); // Left-Right case
                }
                node.rotateRight(); // Left-Left case
            }

            node = node.parent; // Move up to the parent
        }
    }

    private AVLPlayerNode getRoot() {
        AVLPlayerNode root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    private void updateBalanceFactor() {
        this.balanceFactor = height(this.leftChild) - height(this.rightChild);
    }

    private int height(AVLPlayerNode node) {
        if (node == null)
            return -1;
        return 1 + Math.max(height(node.leftChild), height(node.rightChild));
    }

    private AVLPlayerNode BSTSearch(double value) {
        AVLPlayerNode current = this; // Start at the root

        while (current != null) {
            if (current.value == value) {
                return current; // Node found
            } else if (value < current.value) {
                current = current.leftChild; // Go left
            } else {
                current = current.rightChild; // Go right
            }
        }

        return null; // Node not found
    }

    // This should return the new root of the tree
    // remember to update the right weight
    public AVLPlayerNode delete(double value) {
        // TODO: write standard vanilla BST delete method
        // Extra Credit: use rotations to maintain the AVL condition
        return null;
    }

    // remember to maintain rightWeight
    private void rotateRight() {
        AVLPlayerNode y = this.leftChild;
        if (y != null) {
            AVLPlayerNode T2 = y.rightChild;

            // Perform rotation
            y.rightChild = this;
            this.leftChild = T2;

            // Update parents
            if (T2 != null) {
                T2.parent = this;
            }
            y.parent = this.parent;
            this.parent = y;

            // Update rightWeight
            y.rightWeight -= 1 + ((this.rightChild != null) ? this.rightChild.rightWeight : 0);

            // Handle root case
            if (y.parent != null) {
                if (y.parent.leftChild == this) {
                    y.parent.leftChild = y;
                } else {
                    y.parent.rightChild = y;
                }
            }

            // Update balance factors
            this.updateBalanceFactor();
            if (this.leftChild != null) {
                this.leftChild.updateBalanceFactor();
            }
        }
    }

    // remember to maintain rightWeight
    private void rotateLeft() {
        AVLPlayerNode x = this.rightChild;
        if (x != null) {
            AVLPlayerNode T2 = x.leftChild;

            // Perform rotation
            x.leftChild = this;
            this.rightChild = T2;

            // Update parents
            if (T2 != null) {
                T2.parent = this;
            }
            x.parent = this.parent;
            this.parent = x;

            // Update rightWeight
            this.rightWeight = (T2 != null) ? T2.rightWeight + 1 : 0; // +1 for T2 itself
            x.rightWeight = this.rightWeight + 1; // +1 for this node

            // Handle root case
            if (x.parent != null) {
                if (x.parent.leftChild == this) {
                    x.parent.leftChild = x;
                } else {
                    x.parent.rightChild = x;
                }
            }

            // Update balance factors
            this.updateBalanceFactor();
            if (this.rightChild != null) {
                this.rightChild.updateBalanceFactor();
            }
        }
    }

    // this should return the Player object stored in the node with this.value ==
    // value
    public Player getPlayer(double value) {
        AVLPlayerNode foundNode = BSTSearch(value); // Use BSTSearch to find the node
        if (foundNode != null) {
            return foundNode.data; // Return the player data if the node is found
        }
        return null; // Return null if no player with the given value is found
    }

    // this should return the rank of the node with this.value == value
    public int getRank(double eloScore) {
        // TODO Revisit this method
        return getRankRecursive(this, eloScore); // +1 because the best player is ranked 1
    }

    private int getRankRecursive(AVLPlayerNode node, double eloScore) {
        // TODO Revisit this method
        if (node == null) {
            return 0;
        }

        if (eloScore > node.value) {
            // If the Elo score is greater than the root, the rank is equal to the rank in
            // the right subtree
            return getRankRecursive(node.rightChild, eloScore);
        } else {
            // If the Elo score is less than the root, the rank is the rank in the left
            // subtree plus the number of nodes in the right subtree, plus one for the root
            return getRankRecursive(node.leftChild, eloScore) + node.rightWeight + 1;
        }
    }

    public int getBalanceFactor() {
        return this.balanceFactor;
    }

    public int getRightWeight() {
        return this.rightWeight;
    }

    public int getHeight() {
        return height(this);
    }

    // this should return the tree of names with parentheses separating subtrees
    // eg "((bob)alice(bill))"
    public String treeString() {
        StringBuilder sb = new StringBuilder();

        // Helper method to recursively build the string
        buildTreeString(sb, this);

        return sb.toString();
    }

    private void buildTreeString(StringBuilder sb, AVLPlayerNode node) {
        if (node == null) {
            return;
        }

        sb.append("(");
        buildTreeString(sb, node.leftChild); // Add left subtree
        sb.append(node.data.getName()); // Add current node's data
        buildTreeString(sb, node.rightChild); // Add right subtree
        sb.append(")");
    }

    // this should return a formatted scoreboard in descending order of value
    // see example printout in the pdf for the command L
    public String scoreboard() {
        // TODO
        return "";
    }

}
