
package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CatCafe implements Iterable<Cat> {
	public CatNode root;

	public CatCafe() {
	}


	public CatCafe(CatNode dNode) {
		this.root = dNode;
	}

	// Constructor that makes a shallow copy of a CatCafe
	// New CatNode objects, but same Cat objects
	public CatCafe(CatCafe cafe) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */

		// Calls on shallow copy
		this.shallowCopy(cafe.root);
	}

	// Recursively does a preorder traversal
	// Creates a new CatNode for each cat in café
	private void shallowCopy(CatNode current) {

		// Do a preorder traversal
		if (current == null) {
			return;
		}

		this.hire(current.catEmployee);

		shallowCopy(current.junior);

		shallowCopy(current.senior);
	}

	// add a cat to the café database
	public void hire(Cat c) {
		if (root == null) 
			root = new CatNode(c);
		else
			root = root.hire(c);
	}

	// removes a specific cat from the café database
	public void retire(Cat c) {
		if (root != null)
			root = root.retire(c);
	}

	// get the oldest hire in the café
	public Cat findMostSenior() {
		if (root == null)
			return null;

		return root.findMostSenior();
	}

	// get the newest hire in the café
	public Cat findMostJunior() {
		if (root == null)
			return null;

		return root.findMostJunior();
	}

	// returns a list of cats containing the top numOfCatsToHonor cats 
	// in the café with the thickest fur. Cats are sorted in descending
	// order based on their fur thickness. 
	public ArrayList<Cat> buildHallOfFame(int numOfCatsToHonor) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */

		// Create a new cats array
		ArrayList<Cat> cats = new ArrayList<Cat>();

		// Add all the cats in the tree to the array
		catsToList(root, cats);

		// Sort all the cats in the array based on fur thickness
		sort(cats);

		// Determine how big the hall of fame should be
		if (numOfCatsToHonor > cats.size()) {
			numOfCatsToHonor = cats.size();
		}

		// Create the final hall of fame
		ArrayList<Cat> hallOfFame = new ArrayList<Cat>();

		for (int i = 0; i < numOfCatsToHonor; i++) {
			hallOfFame.add(cats.get(i));
		}

		return hallOfFame;
	}

	// Used any time an array list of all the cats is needed
	// Does a postorder traversal
	// Doesn't really matter what traversal since in all cases the list is sorted after
	private void catsToList(CatNode current, ArrayList<Cat> cats) {
		if (current == null) {
			return;
		}

		catsToList(current.junior, cats);

		catsToList(current.senior, cats);

		cats.add(current.catEmployee);
	}

	// Sorts cats based on fur thickness
	// Uses a bubble sort method
	private void sort(ArrayList<Cat> cats) {

		for (int i = 0; i < cats.size() - 1; i++) {
			for (int j = 0; j < cats.size() - i - 1; j++) {
				if (cats.get(j).getFurThickness() < cats.get(j + 1).getFurThickness()) {
					Cat temp = cats.get(j);
					cats.set(j, cats.get(j + 1));
					cats.set(j + 1, temp);
				}
			}
		}
	}

	// Returns the expected grooming cost the café has to incur in the next numDays days
	public double budgetGroomingExpenses(int numDays) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */

		// Define a counter for the budget
		double budget = 0.0;

		// Create a stack and add the root to the stack
		ArrayList<CatNode> stack = new ArrayList<CatNode>();
		stack.add(root);
		CatNode current;

		while (stack.size() > 0) {

			// Pop the top cat off the stack
			current = stack.remove(stack.size() - 1);

			// Add the grooming cost to the budget
			if (current.catEmployee.getDaysToNextGrooming() <= numDays) {
				budget += current.catEmployee.getExpectedGroomingCost();
			}

			// Add any children to the stack
			if (current.junior != null) {
				stack.add(current.junior);
			}
			if (current.senior != null) {
				stack.add(current.senior);
			}
		}

		return budget;
	}

	// returns a list of list of Cats. 
	// The cats in the list at index 0 need be groomed in the next week. 
	// The cats in the list at index i need to be groomed in i weeks. 
	// Cats in each sublist are listed in from most senior to most junior. 
	public ArrayList<ArrayList<Cat>> getGroomingSchedule() {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */

		// Create a new list of all the cats in the tree
		ArrayList<Cat> cats = new ArrayList<Cat>();
		catsToList(root, cats);

		// Find how many sub-arrays to create
		int mostWeeks = 0;

		for (Cat cat: cats) {
			int weeksToGrooming = cat.getDaysToNextGrooming() / 7;

			if (weeksToGrooming > mostWeeks) {
				mostWeeks = weeksToGrooming;
			}
		}

		// Create a new list of lists to represent schedule
		ArrayList<ArrayList<Cat>> schedule = new ArrayList<ArrayList<Cat>>();

		if (cats.size() != 0) {
			for (int i = 0; i <= mostWeeks; i++) {
				ArrayList<Cat> arr = new ArrayList<Cat>();
				schedule.add(arr);
			}

			// Add cats to the schedule
			for (Cat cat : cats) {
				int weeksToGrooming = cat.getDaysToNextGrooming() / 7;

				schedule.get(weeksToGrooming).add(cat);
			}

			// Sort each sub-array based on seniority
			for (ArrayList<Cat> arr : schedule) {
				scheduleSort(arr);
			}
		}

		return schedule;
	}

	// Sorts cats in schedule based on seniority
	// Uses a bubble sort method
	private void scheduleSort(ArrayList<Cat> cats) {

		for (int i = 0; i < cats.size() - 1; i++) {
			for (int j = 0; j < cats.size() - i - 1; j++) {
				if (cats.get(j).getMonthHired() < cats.get(j + 1).getMonthHired()) {
					Cat temp = cats.get(j);
					cats.set(j, cats.get(j + 1));
					cats.set(j + 1, temp);
				}
			}
		}
	}


	public Iterator<Cat> iterator() {
		return new CatCafeIterator();
	}


	public class CatNode {
		public Cat catEmployee;
		public CatNode junior;
		public CatNode senior;
		public CatNode parent;

		public CatNode(Cat c) {
			this.catEmployee = c;
			this.junior = null;
			this.senior = null;
			this.parent = null;
		}

		// add the c to the tree rooted at this and returns the root of the resulting tree
		public CatNode hire (Cat c) {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */

			// Add the cat c to the tree based on BST properties
			CatNode added = this.add(c);

			// Define a CatNode to be the parent of the added cat
			CatNode parentOfAdded = added.parent;

			// Fix the BST so max heap properties hold
			// While the max heap properties are broken
			while (added != root && added.catEmployee.getFurThickness() > parentOfAdded.catEmployee.getFurThickness()) {

				// If added is the right child
				if (added.catEmployee.getMonthHired() < parentOfAdded.catEmployee.getMonthHired()) {

					// Do a left rotation
					parentOfAdded.leftRotation(added);

				// Added is the left child
				} else if (added.catEmployee.getMonthHired() > parentOfAdded.catEmployee.getMonthHired()) {

					// Do a right rotation
					parentOfAdded.rightRotation(added);

				}
				// Update the parent
				parentOfAdded = added.parent;
			}
			// Root should have been updated so this should be fine
			return root;
		}

		// Private methods for hire
		// Add c to the BST
		private CatNode add(Cat c) {

			// Define a CatNode current
			// Using this wasn't working properly when updating
			CatNode current = this;

			// Create a parent pointer node
			CatNode parent = null;

			// If there is no right child and BST properties are maintained if we add there
			// Add the node as the right child
			if(current.senior == null && current.catEmployee.getMonthHired() > c.getMonthHired()) {

				// Create a new node and update the parent
				current.senior = new CatNode (c);
				current.senior.parent = current;

				return current.senior;

			// If there is no left child and BST properties are maintained if we add there
			// Add the node as the left child
			} else if(current.junior == null && current.catEmployee.getMonthHired() < c.getMonthHired()) {

				// Create a new node and update the parent
				current.junior = new CatNode (c);
				current.junior.parent = current;

				return current.junior;
			}

			// If we can't add to this then iteratively traverse down the tree
			// While loop stops when we find the right place to add
			while (current != null) {

				// Update the parent
				parent = current;

				// Move down the tree
				if (c.getMonthHired() > current.catEmployee.getMonthHired()) {
					current = current.junior;

				} else {
					current = current.senior;
				}
			}

			// Parent is now a leaf where c can be added
			// Check whether to add c as junior or senior
			if (c.getMonthHired() > parent.catEmployee.getMonthHired()) {

				// Create a new node as the junior
				parent.junior = new CatNode (c);
				parent.junior.parent = parent;

				return parent.junior;

			} else {

				// Create a new node as the senior
				parent.senior = new CatNode (c);
				parent.senior.parent = parent;

				return parent.senior;
			}
		}

		// Private methods for the rotations to fix max heap properties
		// Perform a right rotation
		private void rightRotation(CatNode current) {

			// If the parent is the root
			if (this.parent == null) {

				// Update current so that it is now the root
				this.parent = current;
				current.parent = null;
				root = current;

			} else {

				// Create a temp to store the parent of the parent
				CatNode temp = this.parent;

				// Update current to be the parent of the parent
				this.parent = current;
				current.parent = temp;

				// Current is now junior or senior to temp
				// Update either junior or senior
				if (temp.catEmployee.getMonthHired() < current.catEmployee.getMonthHired()) {
					temp.junior = current;

				} else {
					temp.senior = current;
				}
			}

			// If the right node of current is null
			if (current.senior == null) { //senior node of current is null

				// Update current so the right node is now the former parent and make the junior pointer null
				current.senior = this;
				this.junior = null;

			} else {
				// Create a temporary node for storing the right child of current
				CatNode temp = current.senior;

				// Update pointers
				current.senior = this;
				this.junior = temp;
				temp.parent = this;
			}
		}

		// Perform a left rotation
		private void leftRotation(CatNode current) {

			// If the parent is the root
			if (this.parent == null) { //If the parent has no parent (parent is root)

				// Update current so it is now the root
				this.parent = current;
				current.parent = null;
				root = current;

			} else {

				// Create a temp to store the parent of the parent
				CatNode temp = this.parent;

				// Update current to be the parent of the parent
				this.parent = current;
				current.parent = temp;

				// Current is now junior or senior to temp
				// Update either junior or senior
				if (temp.catEmployee.getMonthHired() < current.catEmployee.getMonthHired()) {
					temp.junior = current;

				} else {
					temp.senior = current;
				}
			}

			// If the left node of current is null
			if (current.junior == null) {

				// Update current so the left node is now the former parent and make the senior pointer null
				current.junior = this;
				this.senior = null;

			} else {
				// Create a temporary node for storing the right child of current
				CatNode temp = current.junior;

				// Update the pointers
				current.junior = this;
				this.senior = temp;
				temp.parent = this;
			}
		}

		// remove c from the tree rooted at this and returns the root of the resulting tree
		public CatNode retire(Cat c) {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */

			// Define a CatNode for ease of use
			CatNode current = this;

			// Define a CatNode for the parent of current
			CatNode parentOfCurrent = null;

			// Find the node that has to be removed
			// Iterates through the tree checking if we are at the right node
			while (current != null && !current.catEmployee.equals(c)){

				// Update the parent of current
				parentOfCurrent = current;

				// Iterates down the tree
				if (c.getMonthHired() > current.catEmployee.getMonthHired()) {
					current = current.junior;

				} else {
					current = current.senior;
				}
			}

			// Edge case where the cat we are looking for is not in the tree
			if (current == null) {
				return this;
			}

			// For clarity split up cases where the node to be removed has 0 or 1 children or both children
			// Treat each case separately
			if (current.senior == null || current.junior == null) {

				// Define the CatNode that will replace the removed cat
				CatNode replacement;

				// If current has no junior then the replacement will its senior
				if (current.junior == null) {
					replacement = current.senior;

				// The current has a junior and the replacement will be this
				} else {
					replacement = current.junior;
				}

				// After traversal, if the node to be removed is still the root
				if (current == this) {

					// Store the parent of parent
					CatNode temp = current.parent;

					current = replacement;

					// Edge case handling
					if (replacement != null) {
						replacement.parent = temp;
					}

					// Should return the root of the resulting subtree
					return current;

				// The cat being removed is not the root
				} else {

					if (current == parentOfCurrent.junior) {
						parentOfCurrent.junior = replacement;

					} else {
						parentOfCurrent.senior = replacement;
					}

					// Should return the root of the resulting subtree
					// The root of the subtree the method was called on should not have changed
					return this;
				}

			// The node to be removed has both children
			} else {

				// Define the CatNode that will replace the cat being removed
				CatNode replacement = current.junior;

				// Define the parent of the replacement CatNode
				CatNode repParent = null;

				// Find the most senior cat in the left subtree
				// This is the cat that will replace the removed
				while (replacement.senior != null) {
					repParent = replacement;
					replacement = replacement.senior;
				}

				// Remove the cat that's replacing the cat to be removed from its current position
				// If the replacement cat has a child
				// Don't have to check senior since it should be the most senior in the subtree
				if (replacement.junior != null) {

					// Deal with if the replacement has a child itself
					if(repParent != null) {
						repParent.senior = replacement.junior;
						replacement.junior.parent = repParent;
					} else {
						current.junior = replacement.junior;
						replacement.junior.parent = current;
					}

				// The replacement cat is a leaf
				} else {

					// If the replacement is not the immediate left child of the cat being removed
					// i.e. we had to traverse farther down the left subtree
					if (repParent != null) {
						repParent.senior = null;

					// The replacement is the immediate junior
					} else {
						current.junior = null;
					}
					replacement.parent = null;
				}

				// Replace the cat being removed with its determined replacement
				current.catEmployee = replacement.catEmployee;

				// Perform down heap
				current = current.downHeap();

				// current should be the root of the resulting subtree
				return current;
			}
		}

		// Helper method for retire
		// Uses left and right rotation that were used for hire
		private CatNode downHeap() {

			// Keep track of the number of times the while loop runs
			int numOfDownHeaps = 0;

			// Define and keep track of the root of the resulting subtree
			CatNode newRoot = this;

			// While there are nodes that break the max heap properties
			while ((this.senior != null && (this.catEmployee.getFurThickness()
					< this.senior.catEmployee.getFurThickness())) || (this.junior != null
					&& (this.catEmployee.getFurThickness() < this.junior.catEmployee.getFurThickness()))) {

				// If this has both children
				if (this.junior != null && this.senior != null) {

					// Decide which rotation to do
					if (this.junior.catEmployee.getFurThickness() > this.senior.catEmployee.getFurThickness()) {

						// Do a right rotation
						this.rightRotation(this.junior);

					} else if (this.junior.catEmployee.getFurThickness() < this.senior.catEmployee.getFurThickness()){

						// Do a left rotation
						this.leftRotation(this.senior);
					}

				// If this has only the right node
				} else if (this.junior == null) {

					// Do a left rotation
					this.leftRotation(this.senior);

				// If this has only the left node
				} else {

					// Do a right rotation
					this.rightRotation(this.junior);
				}

				// Increase the number performed
				numOfDownHeaps++;

				// The root of the resulting subtree should only be able to be updated once
				// The more times we do a down heap should be lower down in the tree
				if (numOfDownHeaps == 1) {
					newRoot = this.parent;
				}
			}
			return newRoot;
		}

		// find the cat with the highest seniority in the tree rooted at this
		public Cat findMostSenior() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */

			CatNode current = this;

			while (current.senior != null) {
				current = current.senior;
			}

			return current.catEmployee;
		}

		// find the cat with the lowest seniority in the tree rooted at this
		public Cat findMostJunior() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */

			CatNode current = this;

			while (current.junior != null) {
				current = current.junior;
			}

			return current.catEmployee;
		}

		// Feel free to modify the toString() method if you'd like to see something else displayed.
		public String toString() {
			String result = this.catEmployee.toString() + "\n";
			if (this.junior != null) {
				result += "junior than " + this.catEmployee.toString() + " :\n";
				result += this.junior.toString();
			}
			if (this.senior != null) {
				result += "senior than " + this.catEmployee.toString() + " :\n";
				result += this.senior.toString();
			} /*
			if (this.parent != null) {
				result += "parent of " + this.catEmployee.toString() + " :\n";
				result += this.parent.catEmployee.toString() +"\n";
			}*/
			return result;
		}
	}


	private class CatCafeIterator implements Iterator<Cat> {
		// HERE YOU CAN ADD THE FIELDS YOU NEED

		ArrayList<CatNode> cats;

		private CatCafeIterator() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			cats = new ArrayList<CatNode>();

			CatNode r = root;

			while (r != null) {
				cats.add(r);
				r = r.junior;
			}
		}

		public Cat next(){
			/*
			 * TODO: ADD YOUR CODE HERE
			 */

			if (cats.size() == 0) {
				throw new NoSuchElementException();
			}

			CatNode current = cats.remove(cats.size() - 1);
			Cat cat = current.catEmployee;

			if (current.senior != null) {
				current = current.senior;

				while (current != null) {
					cats.add(current);
					current = current.junior;
				}
			}
			return cat;
		}

		public boolean hasNext() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			return cats.size() != 0;
		}
	}

	public static void main(String[] args) {
		Cat B = new Cat("Buttercup", 45, 53, 5, 85.0);
		Cat C = new Cat("Chessur", 8, 23, 2, 250.0);
		Cat J = new Cat("Jonesy", 0, 21, 12, 30.0);	
		Cat JJ = new Cat("JIJI", 156, 17, 1, 30.0);
		Cat JTO = new Cat("J. Thomas O'Malley", 21, 10, 9, 20.0);
		Cat MrB = new Cat("Mr. Bigglesworth", 71, 0, 31, 55.0);
		Cat MrsN = new Cat("Mrs. Norris", 100, 68, 15, 115.0);
		Cat T = new Cat("Toulouse", 180, 37, 14, 25.0);


		Cat BC = new Cat("Blofeld's cat", 6, 72, 18, 120.0);
		Cat L = new Cat("Lucifer", 10, 44, 20, 50.0);

	}
}