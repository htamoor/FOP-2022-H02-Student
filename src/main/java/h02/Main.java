package h02;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.World;

import java.util.concurrent.ThreadLocalRandom;

public class Main {
  public static final byte NUMBER_OF_STEPS_BETWEEN_REDUCTIONS = 10;

  public static int getRandomWorldSize() {
    return 4 + ThreadLocalRandom.current().nextInt(6);
  }

  // ---------------- DO NOT CHANGE ANYTHING ABOVE THIS LINE ---------------

  public static void main(String[] args) {
    int cols = getRandomWorldSize();
    int rows = getRandomWorldSize();
    World.setSize(cols, rows);
    World.setDelay(10);
    World.setVisible(true);
    System.out.println("Size of world: " + cols + "x" + rows);
    // Hier programmieren

    // Task 1
    Robot[] allRobots = initializeRobots(cols, rows);
    int[] paces = initializePaces(allRobots);


    // Task 3
    int loopCounter = 0;
    while (allRobots.length > 0) {
      loopCounter++;
      moveForward(allRobots, paces);
      if (allRobots.length > 2) {
        int[] distinctInts = generateThreeDistinctInts(allRobots);
        swapPaces(paces, distinctInts[0],distinctInts[1], distinctInts[2]);
      }
      if (loopCounter % NUMBER_OF_STEPS_BETWEEN_REDUCTIONS == 0) {
        int deleteIndex = ThreadLocalRandom.current().nextInt(allRobots.length);
        allRobots = reduceRobots(allRobots, deleteIndex);
        paces = reducePaces(paces, deleteIndex);
      }
    }
  }

  /**
   * Initializes allRobots array for given World size.
   *
   * @param cols number of columns in World
   * @param rows number of rows in World
   * @return correctly initialized allRobots array (i.e. correct size, correct positions, correct directions)
   */
  public static Robot[] initializeRobots(int cols, int rows) {
    int minimum = Math.min(cols, rows);
    Robot[] allRobots = new Robot[minimum % 2 == 0 ? minimum : minimum - 1];
    for (int i = 0; i < allRobots.length; i++) {
      if (i < allRobots.length / 2) {
        allRobots[i] = new Robot(i, i, Direction.RIGHT, 1000);
      } else {
        allRobots[i] = new Robot(i, i, Direction.LEFT, 1000);
      }
    }

    return allRobots;

  }

  /**
   * Initializes paces array for given allRobots array.
   * <p>
   * Assumes allRobots array was initialized correctly.
   * </p>
   *
   * @param allRobots previously initialized allRobots array
   * @return correctly initialized paces array (i.e. correct size, filled w/ random integers from [1...5])
   */
  public static int[] initializePaces(Robot[] allRobots) {
    int[] paces = new int[allRobots.length];
    for (int i = 0; i < paces.length; i++) {
      paces[i] = ThreadLocalRandom.current().nextInt(1, 6);
    }

    return paces;
  }

  /**
   * Moves all robots from allRobots array corresponding to the number of steps to take for each robot (from paces).
   *
   * @param allRobots allRobots array from _main, contains all robots which should be moved by moveForward
   * @param paces     corresponding paces array to allRobots
   */
  public static void moveForward(Robot[] allRobots, int[] paces) {
    for (int i = 0; i < allRobots.length; i++) {
      for (int j = 0; j < paces[i]; j++) {
        if (!ifCanMove(allRobots[i])) {
          allRobots[i].turnLeft();
        } else {
          allRobots[i].move();
        }
      }
    }
  }

  private static boolean ifCanMove(Robot robot) {
    switch (robot.getDirection()) {
      case LEFT:
        return robot.getX() != 0;
      case DOWN:
        return robot.getY() != 0;
      case UP:
        return robot.getY() != World.getHeight() - 1;
      case RIGHT:
        return robot.getX() != World.getWidth() - 1;
      default:
        throw new IllegalArgumentException("Ups...Somthing went Wrong!");
    }
  }

  /**
   * Generates three distinct integers from index set of allRobots.
   *
   * @param allRobots allRobots array from main method
   * @return array containing three distinct integers as described above
   */
  public static int[] generateThreeDistinctInts(Robot[] allRobots) {
    int i1 = ThreadLocalRandom.current().nextInt(allRobots.length);
    int i2 = ThreadLocalRandom.current().nextInt(allRobots.length);
    int i3 = ThreadLocalRandom.current().nextInt(allRobots.length);

    while (i1 == i2 || i1 == i3 || i2 == i3) {
      i1 = ThreadLocalRandom.current().nextInt(allRobots.length);
      i2 = ThreadLocalRandom.current().nextInt(allRobots.length);
      i3 = ThreadLocalRandom.current().nextInt(allRobots.length);
    }



    return new int[] {i1, i2, i3};
  }

  /**
   * Returns an integer array of size 3, in which i1, i2 and i3 are sorted in ascending order.
   * <p>
   * Assumes that i1, i2 and i3 are distinct.
   * </p>
   *
   * @param i1 first number
   * @param i2 second number
   * @param i3 third number
   * @return sorted array of i1, i2, i3 in ascending order
   */
  public static int[] orderThreeInts(int i1, int i2, int i3) {
    if (i2 < i1) {
      int tmp = i2;
      i2 = i1;
      i1 = tmp;
    }

    if (i3 < i1) {
      int tmp = i3;
      i3 = i1;
      i1 = tmp;
    }

    if (i3 < i2) {
      int tmp = i3;
      i3 = i2;
      i2 = tmp;
    }

    return new int[] {i1, i2, i3};
  }

  /**
   * Swaps entries from paces array as described in exercise sheet.
   * <p>
   * Assumes i1, i2 and i3 are three distinct indices from the index set of paces array.
   * Note that i1, i2 and i3 are not ordered.
   * </p>
   *
   * @param paces current paces array
   * @param i1    first index
   * @param i2    second index
   * @param i3    third index
   * @return paces array with identical entries as before, but in order as described in exercise sheet
   */
  public static int[] swapPaces(int[] paces, int i1, int i2, int i3) {
    int[] orderedI = orderThreeInts(i1, i2, i3);
    i1 = orderedI[0];
    i2 = orderedI[1];
    i3 = orderedI[2];

    int[] orderedPaces = orderThreeInts(paces[i1], paces[i2], paces[i3]);
    paces[i1] = orderedPaces[0];
    paces[i2] = orderedPaces[1];
    paces[i3] = orderedPaces[2];
    return paces;
  }

  /**
   * Reduces returns new array of robots which contains the same elements as allRobots, without the
   * robot at deleteIndex.
   * <p>
   * The new array's length is smaller by one compared to allRobots
   * </p>
   *
   * @param allRobots   allRobots array from _main
   * @param deleteIndex index at which the robot should be removed
   * @return reduced array (as described above)
   */
  public static Robot[] reduceRobots(Robot[] allRobots, int deleteIndex) {
    Robot[] newRobots = new Robot[allRobots.length - 1];
    int counter = 0;
    for (int i = 0; i < allRobots.length; i++) {
      if (i == deleteIndex) {
        continue;
      }
      newRobots[counter] = allRobots[i];
      counter++;
    }

    return newRobots;
  }

  /**
   * Reduces returns new array of paces which contains the same elements as given paces array, without the
   * paces entry at deleteIndex.
   * <p>
   * The new array's length is smaller by one compared to given paces array
   * </p>
   *
   * @param paces       paces array from _main
   * @param deleteIndex index at which the paces entry should be removed
   * @return reduced array (as described above)
   */
  public static int[] reducePaces(int[] paces, int deleteIndex) {
    int[] newPaces = new int[paces.length - 1];
    int counter = 0;
    for (int i = 0; i < paces.length; i++) {
      if (i == deleteIndex) {
        continue;
      }
      newPaces[counter] = paces[i];
      counter++;
    }

    return newPaces;
  }
}
