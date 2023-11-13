package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import main.AVLPlayerNode;
import main.Player;

public class AVLPlayerNodeTest {
    private AVLPlayerNode tree;
    private Player alice, bob, carol, dan;

    @Before
    public void setUp() {
        // Initialize players
        alice = new Player("Alice", 1, 1200.0);
        bob = new Player("Bob", 2, 1100.0);
        carol = new Player("Carol", 3, 1300.0);
        dan = new Player("Dan", 3, 1500.0);

        // Inserting players into the tree
        tree = new AVLPlayerNode(alice, alice.getELO());
        tree = tree.insert(bob, bob.getELO());
        tree = tree.insert(carol, carol.getELO());
    }

    @Test
    public void testAVLPlayerNodeTreeString() {
        // Now the tree is already set up with alice, bob, and carol
        assertEquals("Constructor should initialize data with provided player",
                "((Bob)Alice(Carol))", tree.treeString());
        tree = tree.insert(dan, dan.getELO());

    }

    @Test
    public void testTreeStructureAfterInsertions() {
        String expectedStructure = "((Bob)Alice(Carol))"; // Update as per expected structure
        assertEquals("Tree structure should match after insertions", expectedStructure, tree.treeString());

        // Inserting Dan and checking the tree structure again
        tree = tree.insert(dan, dan.getELO());
        expectedStructure = "((Bob)Alice(Carol(Dan)))"; // Update this as per the expected structure after
        // Dan's insertion
        assertEquals("Tree structure should match after inserting Dan",
                expectedStructure, tree.treeString());
    }

    @Test
    public void testBalanceFactors() {
        // Assuming a method 'getBalanceFactor' to get the balance factor of a node
        assertTrue("Balance factor should be between -1 and 1", Math.abs(tree.getBalanceFactor()) <= 1);

        // Insert Dan and check balance factors again
        tree = tree.insert(dan, dan.getELO());
        assertTrue("Balance factor should be between -1 and 1 after inserting Dan",
                Math.abs(tree.getBalanceFactor()) <= 1);
    }

    @Test
    public void testRightWeight() {
        // Assuming a method 'getRightWeight' to get the right weight of a node
        int expectedRightWeight = 1; // Determine the expected right weight after initial insertions
        assertEquals("Right weight should match expected value", expectedRightWeight, tree.getRightWeight());

        // Insert Dan and check right weight again
        tree = tree.insert(dan, dan.getELO());
        expectedRightWeight = 1; // Update expected right weight after Dan's insertion
        assertEquals("Right weight should match expected value after inserting Dan", expectedRightWeight,
                tree.getRightWeight());
    }

    @Test
    public void testTreeHeight() {
        // Assuming a method 'getHeight' to get the height of the tree
        int expectedHeight = 1; // Determine the expected height after initial insertions
        assertEquals("Height should match expected value", expectedHeight, tree.getHeight());

        // Insert Dan and check height again
        tree = tree.insert(dan, dan.getELO());
        expectedHeight = 1; // Update expected height after Dan's insertion
        assertEquals("Height should match expected value after inserting Dan", expectedHeight, tree.getHeight());
    }

    @Test
    public void testRightHeavyOnlyRightMembersRotation() {
        tree = new AVLPlayerNode(bob, bob.getELO());
        tree = tree.insert(carol, carol.getELO());
        tree = tree.insert(dan, dan.getELO());

        String expectedStructure = "((Bob)Carol(Dan))";
        assertEquals("Tree structure should match expected value after left rotation", expectedStructure,
                tree.treeString());
    }

    @Test
    public void testLeftHeavyOnlyLeftMembersRotation() {
        // Create a new tree
        AVLPlayerNode tree = new AVLPlayerNode(dan, dan.getELO()); // Insert highest value first
        tree = tree.insert(carol, carol.getELO()); // Insert next lower value
        tree = tree.insert(bob, bob.getELO()); // Insert lowest value, should cause rebalancing

        // Expected structure after right rotation or left-right rotation
        String expectedStructure = "((Bob)Carol(Dan))"; // Adjust this based on expected outcome

        assertEquals("Tree structure should be balanced after left-heavy insertions",
                expectedStructure, tree.treeString());

        // Verify balance factors
        assertTrue("Balance factor of root should be between -1 and 1",
                Math.abs(tree.getBalanceFactor()) <= 1);
    }

    // TODO: Add more tests for rotations
    @Test
    public void testLeftRightRotation() {
        tree = new AVLPlayerNode(carol, carol.getELO());
        tree = tree.insert(dan, dan.getELO());
        tree = tree.insert(bob, bob.getELO());

        String expectedStructure = "((Bob)Carol(Dan))";
        assertEquals("Tree structure should match expected value after left-right rotation", expectedStructure,
                tree.treeString());
    }

    @Test
    public void testRightLeftRotation() {
        tree = new AVLPlayerNode(carol, carol.getELO());
        tree = tree.insert(bob, bob.getELO());
        tree = tree.insert(dan, dan.getELO());

        String expectedStructure = "((Bob)Carol(Dan))";
        assertEquals("Tree structure should match expected value after left-right rotation", expectedStructure,
                tree.treeString());
    }

}
