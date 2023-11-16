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


    public AVLPlayerNode(Player data, double value) {
        this.data = data;
        this.value = value;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.rightWeight = 0;
        this.balanceFactor = 0;
    }

    /**
     * Inserts a new node with the specified player and value into the AVL tree.
     * This method locates the appropriate position for the new node and inserts it,
     * maintaining the AVL tree property. After insertion, it rebalances the tree
     * if necessary to ensure that the tree remains balanced.
     *
     * @param newGuy The player object to be inserted into the tree.
     * @param value  The value associated with the player, used to determine the
     *               position of the new node in the tree.
     * @return The root of the AVL tree after insertion. If a new root is created
     *         as a result of rebalancing, it will be returned. Otherwise, the
     *         current root is returned.
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

            if (node.balanceFactor < -1) {
                if (height(node.rightChild.leftChild) > height(node.rightChild.rightChild)) {
                    // Right-Left case
                    node.rightChild.rotateRight();
                }
                // Right-Right case
                node.rotateLeft();
            } else if (node.balanceFactor > 1) {
                if (height(node.leftChild.rightChild) > height(node.leftChild.leftChild)) {
                    // Left-Right case
                    node.leftChild.rotateLeft();
                }
                // Left-Left case
                node.rotateRight();
            }

            node = node.parent;
        }
    }

    /**
     * Retrieves the root node of the AVL tree. Starting from the current node,
     * this method traverses up the tree by following the parent references until
     * it reaches the node that does not have a parent, which is the root of the
     * tree.
     * 
     * @return The root node of the AVL tree. If the current node is the root, it
     *         returns
     *         itself. If the tree is empty it returns null.
     */
    private AVLPlayerNode getRoot() {
        AVLPlayerNode root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    /**
     * Updates the balance factor of the current node in the AVL tree. The balance
     * factor
     * is calculated as the height of the left subtree minus the height of the right
     * subtree.
     */
    private void updateBalanceFactor() {
        this.balanceFactor = height(this.leftChild) - height(this.rightChild);
    }

    /**
     * Calculates the height of a given node in the AVL tree. The height of a node
     * is
     * defined as the number of edges on the longest path from the node to a leaf. A
     * leaf node will have a height of 0. If the node is null, this method returns
     * -1,
     * indicating that the height is undefined for a non-existent (null) node.
     * 
     * @param node The node for which the height is to be calculated.
     * @return The height of the given node. Returns -1 if the node is null.
     */
    private int height(AVLPlayerNode node) {
        if (node == null)
            return -1;
        return 1 + Math.max(height(node.leftChild), height(node.rightChild));
    }

    /**
     * Searches for a node with a specific value in the AVL tree.
     * Starting from this node, the method traverses the tree, comparing
     * the search value with each node's value to decide the direction of search.
     * The search continues until it finds a node with the matching value or reaches
     * a leaf.
     *
     * @param value The value to search for.
     * @return The node with the specified value, or null if no such node is found.
     */
    private AVLPlayerNode BSTSearch(double value) {
        AVLPlayerNode current = this;
        while (current != null) {
            if (current.value == value) {
                return current;
            } else if (value < current.value) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }
        return null;
    }

    /**
     * Deletes a node with the specified value from the AVL tree. This method first
     * searches for the node with the given value. If found, it determines the node
     * to be removed (which may be the found node itself or its successor) and then
     * proceeds with the deletion process.
     * 
     * @param value The value of the node to be deleted from the AVL tree.
     * @return The root of the AVL tree after the deletion. If the root was deleted,
     *         the new root is returned; otherwise, the current root is returned.
     */
    public AVLPlayerNode delete(double value) {
        AVLPlayerNode z = BSTSearch(value);
        AVLPlayerNode y;
        AVLPlayerNode x;
        if (z == null) {
            return this;
        }

        if (z.leftChild == null || z.rightChild == null) {
            y = z;
        } else {
            y = treeSuccessor(z);
        }

        if (y.leftChild != null) {
            x = y.leftChild;
        } else {
            x = y.rightChild;
        }

        if (x != null) {
            x.parent = y.parent;
        }

        if (y.parent == null) {

            if (y != z) {
                z.value = y.value;
                z.data = y.data;
                deleteNode(y);
                return this;
            } else {

                if (x != null) {
                    x.parent = null;
                }
                return x;
            }
        } else {

            deleteNode(y);
            return this.getRoot();
        }
    }

    /**
     * Removes a specified node from the AVL tree. This method handles the
     * re-linking
     * of the node's child (if any) to the node's parent, effectively bypassing the
     * node in the tree. It adjusts the parent's child reference and the child's
     * parent
     * reference as needed.
     * 
     * @param node The node to be deleted from the AVL tree.
     * @return The root of the AVL tree after the deletion.
     */

    private AVLPlayerNode deleteNode(AVLPlayerNode node) {
        AVLPlayerNode x = (node.leftChild != null) ? node.leftChild : node.rightChild;

        if (node.parent == null) {
            if (x != null) {
                x.parent = null;
            }
            return x;
        } else {
            if (node.parent.leftChild == node) {
                node.parent.leftChild = x;
            } else {
                node.parent.rightChild = x;
            }
            if (x != null) {
                x.parent = node.parent;
            }
            return this.getRoot();
        }
    }

    /**
     * Finds the in-order successor of a given node in the AVL tree. The in-order
     * successor
     * is the next node in the in-order traversal of the tree. It's the node with
     * the smallest
     * value greater than the given node's value.
     * 
     * @param node The node for which to find the in-order successor.
     * @return The in-order successor of the given node, or null if there is none
     *         (i.e., the
     *         given node is the maximum node in the tree).
     */
    private AVLPlayerNode treeSuccessor(AVLPlayerNode node) {

        if (node.rightChild != null) {
            return minValue(node.rightChild);
        }

        AVLPlayerNode parent = node.parent;
        while (parent != null && node == parent.rightChild) {
            node = parent;
            parent = parent.parent;
        }
        return parent;
    }

    /**
     * Finds the node with the minimum value in the subtree rooted at the given
     * node.
     * This is determined by traversing left down the tree as far as possible,
     * since the leftmost node in a binary search tree holds the smallest value.
     *
     * @param node The root node of the subtree where the search for the minimum
     *             value begins.
     * @return The node with the minimum value in the given subtree.
     */
    private AVLPlayerNode minValue(AVLPlayerNode node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    /**
     * Performs a right rotation on this node in the AVL tree. Right rotation is
     * used
     * to rebalance the tree when the left subtree is heavier than the right
     * subtree.
     * 
     * This operation involves moving the left child of this node to its position
     * and
     * making this node the right child of the new parent. The right subtree of the
     * new
     * parent (previously the left child of this node) becomes the left subtree of
     * this node.
     *
     * The balance factors of the affected nodes are updated accordingly to maintain
     * the
     * AVL tree properties.
     */
    private void rotateRight() {
        AVLPlayerNode y = this.leftChild;
        if (y != null) {
            AVLPlayerNode T2 = y.rightChild;

            y.rightChild = this;
            this.leftChild = T2;

            if (T2 != null) {
                T2.parent = this;
            }
            y.parent = this.parent;
            this.parent = y;

            y.rightWeight -= 1 + ((this.rightChild != null) ? this.rightChild.rightWeight : 0);

            if (y.parent != null) {
                if (y.parent.leftChild == this) {
                    y.parent.leftChild = y;
                } else {
                    y.parent.rightChild = y;
                }
            }

            this.updateBalanceFactor();
            if (this.leftChild != null) {
                this.leftChild.updateBalanceFactor();
            }
        }
    }

    /**
     * Performs a left rotation on this node in the AVL tree. left rotation is
     * used
     * to rebalance the tree when the left subtree is heavier than the left
     * subtree.
     * 
     * This operation involves moving the left child of this node to its position
     * and
     * making this node the left child of the new parent. The left subtree of the
     * new
     * parent (previously the right child of this node) becomes the right subtree of
     * this node.
     *
     * The balance factors of the affected nodes are updated accordingly to maintain
     * the
     * AVL tree properties.
     */
    private void rotateLeft() {
        AVLPlayerNode x = this.rightChild;
        if (x != null) {
            AVLPlayerNode T2 = x.leftChild;

            x.leftChild = this;
            this.rightChild = T2;

            if (T2 != null) {
                T2.parent = this;
            }
            x.parent = this.parent;
            this.parent = x;

            this.rightWeight = (T2 != null) ? T2.rightWeight + 1 : 0;
            x.rightWeight = this.rightWeight + 1;

            if (x.parent != null) {
                if (x.parent.leftChild == this) {
                    x.parent.leftChild = x;
                } else {
                    x.parent.rightChild = x;
                }
            }

            this.updateBalanceFactor();
            if (this.rightChild != null) {
                this.rightChild.updateBalanceFactor();
            }
        }
    }

    /**
     * Retrieves the player with the specified value from the AVL tree.
     * This method searches for the node with the given value and returns
     * the player associated with that node.
     *
     * @param value The value of the node to be retrieved.
     * @return The player associated with the node with the specified value.
     *         If no such node exists, null is returned.
     */
    public Player getPlayer(double value) {
        AVLPlayerNode foundNode = BSTSearch(value);
        if (foundNode != null) {
            return foundNode.data;
        }
        return null;
    }

    /**
     * Calculates the rank of a node in the AVL tree based on the given ELO score.
     * The rank is determined by the position of the node in an in-order traversal
     * of the tree, which reflects the sorted order of nodes. This method uses a
     * recursive helper function to traverse the tree and find the rank.
     * 
     * @param eloScore The ELO score for which to find the rank in the tree.
     * @return The rank of the node with the given ELO score, or 0 if no such node
     *         exists.
     */

    public int getRank(double eloScore) {
        return getRankRecursive(this, eloScore);
    }

    /**
     * Recursively calculates the rank of a node in the AVL tree based on the given
     * ELO score.
     * This method is a helper for the public getRank method and is used to traverse
     * the tree
     * and compute the rank. The rank reflects the position of the node in an
     * in-order traversal
     * of the tree, taking into account the ELO scores.
     *
     * 
     * @param node     The current node being inspected in the AVL tree.
     * @param eloScore The ELO score for which to find the rank.
     * @return The rank of the node with the given ELO score. If the node with the
     *         given ELO
     *         score does not exist, returns 0.
     */
    private int getRankRecursive(AVLPlayerNode node, double eloScore) {
        if (node == null) {
            return 0;
        }

        if (eloScore > node.value) {
            return getRankRecursive(node.rightChild, eloScore) + 1;
        } else {
            return getRankRecursive(node.leftChild, eloScore) + node.rightWeight + 1;
        }
    }

    /**
     * Retrieves the balance factor of this node in the AVL tree.
     * The balance factor is calculated as the height of the left subtree
     * minus the height of the right subtree. It is used to check the balance
     * of the tree at this node. In an AVL tree, the balance factor of any node
     * is always -1, 0, or 1.
     *
     * @return The balance factor of this node.
     */
    public int getBalanceFactor() {
        return this.balanceFactor;
    }

    /**
     * Retrieves the right weight of this node in the AVL tree. The right weight is
     * a measure that indicates the number of nodes in the right subtree of this
     * node.
     * It is used to quickly assess the size of the right subtree without needing to
     * traverse it.
     * 
     * @return The number of nodes in the right subtree of this node.
     */
    public int getRightWeight() {
        return this.rightWeight;
    }

    /**
     * Retrieves the height of this node in the AVL tree. The height is defined as
     * the number
     * of edges on the longest path from this node to a leaf node. A leaf node has a
     * height of 0.
     * This method utilizes a recursive helper function to calculate the height.
     * 
     * @return The height of this node in the AVL tree.
     */

    public int getHeight() {
        return height(this);
    }

    /**
     * Generates a string representation of the AVL tree. This method uses a helper
     * function
     * to perform an in-order traversal of the tree, building a string that
     * represents the
     * tree structure. In the resulting string, each node's value is enclosed in
     * parentheses,
     * with left and right children appearing recursively in the same format.
     * 
     * @return A string representation of the AVL tree.
     */
    public String treeString() {
        StringBuilder sb = new StringBuilder();
        buildTreeString(sb, this);
        return sb.toString();
    }

    /**
     * A helper method that recursively builds a string representation of the AVL
     * tree.
     * Starting from the specified node, it performs an in-order traversal (left
     * node,
     * current node, right node) and appends each node's value to the passed
     * StringBuilder
     * object. The values are enclosed in parentheses to visually represent the tree
     * structure.
     * 
     * @param sb   The StringBuilder object used to build the tree string.
     * @param node The current node being processed in the AVL tree.
     */
    private void buildTreeString(StringBuilder sb, AVLPlayerNode node) {
        if (node == null) {
            return;
        }
        sb.append("(");
        buildTreeString(sb, node.leftChild);
        sb.append(node.data.getName());
        buildTreeString(sb, node.rightChild);
        sb.append(")");
    }

    /**
     * Generates a formatted string that represents a scoreboard of the AVL tree.
     * This scoreboard is created by performing an in-order traversal of the tree
     * and formatting the data of each node into a line item in the scoreboard.
     * Each line item contains the player's name, ID, and ELO score, formatted in a
     * table-like structure.
     *
     * @return A string representing the scoreboard of players in the AVL tree.
     */
    public String scoreboard() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-3s %s\n", "NAME", "ID", "SCORE"));
        inOrderTraversal(sb, this);
        return sb.toString();
    }

    /**
     * A helper method that performs an in-order traversal of the AVL tree,
     * starting from the given node. During the traversal, it appends each node's
     * data to the provided StringBuilder object. The data includes the player's
     * name, ID, and ELO score, formatted in a line item format suitable for
     * display in a scoreboard.
     *
     * * @param sb The StringBuilder object used to build the scoreboard string.
     * 
     * @param node The current node being processed in the AVL tree.
     */
    private void inOrderTraversal(StringBuilder sb, AVLPlayerNode node) {
        if (node == null) {
            return;
        }
        inOrderTraversal(sb, node.rightChild);
        Player player = node.data;
        sb.append(String.format("%-10s %-3d %.2f\n", player.getName(), player.getID(), player.getELO()));
        inOrderTraversal(sb, node.leftChild);
    }

}
