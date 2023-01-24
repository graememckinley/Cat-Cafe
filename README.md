# Cat-Cafe

This project was completed in fulfillment of COMP 250 - Intro to Computer Science at McGill University.

For grading purposes, and in the interest of time, `Cat.java` and a skeleton version of `CatCafe.java` were provided.
Methods I implemented are either private or are marked with the `TODO: ADD YOUR CODE HERE` comment and are explained below.
`Cat.java` and `CatCafe.java` were the only files submitted for evaluation, all other files were used for testing.

## About

The idea for this project was to create an employee database for a cat cafe in which all the employees are cats.
In order to learn and explore different types of data structures, the database was implemented as a binary search tree and max heap.
The project allows you to add and remove cats, find the most junior and senior employees, budget grooming expenses, get a grooming schedule,
and create a hall of fame honoring the cats with the thickest fur.

### The Cat Class

The employees for this implementation are cats represented by the class `Cat`.
This class contains the following fields:
* A name for the cat (`String`)
* The month in which the cat was hired (`int`)
* The thickness of its fur (`int`)
* The number of days until their next grooming appointement (`int`)
* The expected cost of their next grooming appointment (`double`)

This class implements `Comparable` and has a number of getter methods.

### The CatCafe Class

This class is used to represent the database of cat employees.
The tree used to represent a cat cafe stores elements of type `Cat`.
The tree is a binary search tree when looking at seniority and a max heap when looking at the thickness of the cat's fur.
Nodes in the tree are represented by the `CatNode` inner class.

### The CatNode Class

This class is used to represent the nodes in the employee database tree.
The class contains the following fields:
* `Cat catEmployee`: a reference to an object of type `Cat`. It contains information regarding a cat employed at the cafe.
* `CatNode junior`: the root of the left subtree which stores information about cats that are more junior and have shorter fur.
* `CatNode senior`: the root of the right subtree which stores information about cats that are more senior and have shorter fur.
* `CatNode parent`: the parent of the current node. The parent can be junior or senior according to the binary search tree but must have thicker fur according to the max heap.

## Method Breakdown

### `CatCafe.CatCafe(CatCafe cafe)`

This constructor makes a copy of the `CatCafe` input by calling on `shallowCopy(CatNode current)`.
The new `CatCafe` object is a tree storing the exact same object of type `Cat` but different `CatNodes`.

#### `CatCafe.shallowCopy(CatNode current)`

This helper method does a recursive preorder traversal of the tree rooted at `current` creating new `CatNode` objects for every cat in the tree.

### `CatNode.hire(Cat c)`

This method adds `c` to the tree rooted at the `CatNode` on which the method was called, returning the root to the new tree.
In order to preserve both the binary search tree and max heap properties this method must be performed in two steps,
first using the `CatNode.add()` helper method and then using `CatNode.rightRotation()` and `CatNode.leftRotation()` as necessary.

#### `CatNode.add(Cat c)`

This helper method adds `c` to the tree in the leaf position determined by the properties of a binary search tree.

#### `CatNode.rightRotation(CatNode current)` and `CatNode.leftRotation(CatNode current)`

These two helper methods help fix the tree so that the properties of the max heap are maintained.
For a normal max heap we would perform upheap, however, 
since we want to maintain the properties of the binary search tree as well, we use tree rotations which reverse the parent-child relationship whenever necessary.

### `CatNode.retire(Cat c)`

This method removes the cat from the tree that equals `c` from the tree rooted at the `CatNode` on which the method was called.
The method returns the root of the tree obtained after removing `c`.
Similarly to `hire()` this has to be performed in two steps to ensure all properties are maintained.
First, the cat equal to `c` is removed and replaced.
Following this, if this replacement breaks properties of the binary search tree we call on `CatNode.downHeap()`.

#### `CatNode.downHeap()`

This helper method calls on `CatNode.rightRotation()` and `CatNode.leftRotation()` to perform tree rotations, as explained earlier,
depending on the determination of which way we have to rotate.
This method of down heaping ensures that we both fix broken properties and do not introduce new problems.

### `CatNode.findMostSenior()`

This method returns the most senior cat in the tree rooted at the `CatNode` on which the method was called.

### `CatNode.findMostJunior()`

This method returns the most junior car in the tree rooted at the `CatNode` on which the method was called.

### `CatCafe.buildHallOfFame(int numOfCatsToHonor)`

This method returns a list containing `numOfCatsToHonor` cats from the cafe with the thickest fur, sorted in descending order of fur thickness.
Helper methods `catsToList()` and `sort()` are used to achieve this.

#### `CatCafe.catsToList(CatNode current, ArrayList<Cat> cats)`

This helper method does a postorder traversal of the `CatCafe` rooted at `current`, adding cats to `cats` as it goes.

#### `CatCafe.sort(ArrayList<Cat> cats)`

This helper method uses a bubble sort method to sort cats in descending order of fur thickness.

### `CatCafe.budgetGroomingExpenses(int numDays)`

This method returns a double indicating the expected amount of dollars the cafe will need to spend for grooming its cats in the next `numDays`.
A stack data structure is implemented using an `ArrayList<CatNode>` in order to achieve this.

### `CatCafe.getGroomingSchedule()`

This method returns an `ArrayList<ArrayList<Cat>>` where each inner array list contains cats that need to be groomed in the next i weeks.
Here i refers to the index of the inner array list, so cats that need grooming within the week (0 to 7 days) are in the array list at index 0.
Cats that need grooming in the next 8 to 14 days are in the array list at index 1 and so on.
In each sub array the cats appear in ascending order of seniority, calling on both `catsToList()` and `scheduleSort()`, similar to `sort()`, to achieve this.

### `CatCafeIterator`

The `CatCafeIterator` class was implemented.
This allows `CatCafe.iterator()` to return a `CatCafeIterator` object which can be used to iterate through all cats in the cafe.
This class was created last and implements methods `next()` and `hasNext()`.
