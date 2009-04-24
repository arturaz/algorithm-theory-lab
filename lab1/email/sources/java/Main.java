
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

  static void dump(String title, List<Integer> source) {
    System.out.print(title);
    for (Integer i : source) {
      System.out.print(i);
      System.out.print(", ");
    }
    System.out.println();
  }

  static LinkedList<Integer> list_read_data(String filename) throws IOException {
    LinkedList<Integer> data = new LinkedList<Integer>();
    BufferedReader inFile = new BufferedReader(new FileReader(filename));

    int length = new Integer(inFile.readLine());
    for (int i = 0; i < length; i++) {
      String line = inFile.readLine();
      if (!line.equals("")) {
        data.add(new Integer(line.trim()));
      } else {
        i--;
      }
    }

    return data;
  }

  static LinkedList<Integer> list_merge(LinkedList<Integer> left, LinkedList<Integer> right) {
    LinkedList<Integer> result = new LinkedList<Integer>();
    while (left.size() > 0 && right.size() > 0) {
      if (left.getFirst() <= right.getFirst()) {
        result.add(left.pop());
      } else {
        result.add(right.pop());
      }
      number_of_operations++;
    }

    result.addAll(left);
    result.addAll(right);

    return result;
  }

  static LinkedList<Integer> list_merge_sort(LinkedList<Integer> source) {
    LinkedList<Integer> work = new LinkedList<Integer>(source);
    int length = work.size();
    if (length <= 1) {
      return work;
    }

    int middle = length / 2 - 1;
    LinkedList<Integer> left = new LinkedList<Integer>();
    LinkedList<Integer> right = new LinkedList<Integer>();

    for (int i = 0; i <= middle; i++) {
      left.add(work.pop());
    }
    for (int i = middle + 1; i < length; i++) {
      right.add(work.pop());
    }

    left = list_merge_sort(left);
    right = list_merge_sort(right);
    return list_merge(left, right);
  }

  static ArrayList<Integer> vector_read_data(String filename) throws IOException {
    ArrayList<Integer> data = new ArrayList<Integer>();

    BufferedReader inFile = new BufferedReader(new FileReader(filename));

    int length = new Integer(inFile.readLine());
    data.ensureCapacity(length);
    for (int i = 0; i < length; i++) {
      String line = inFile.readLine();
      if (!line.equals("")) {
        data.add(new Integer(line.trim()));
      } else {
        i--;
      }
    }

    return data;
  }

  static ArrayList<Integer> vector_merge(ArrayList<Integer> left, ArrayList<Integer> right) {
    ArrayList<Integer> result = new ArrayList<Integer>(left.size() + right.size());
    int leftIndex = 0, rightIndex = 0;
    while (leftIndex < left.size() && rightIndex < right.size()) {
      if (left.get(leftIndex) <= right.get(rightIndex)) {
        result.add(left.get(leftIndex));
        leftIndex++;
      } else {
        result.add(right.get(rightIndex));
        rightIndex++;
      }
      number_of_operations++;
    }

    for (int i = leftIndex; i < left.size(); i++) {
      result.add(left.get(i));
    }
    for (int i = rightIndex; i < right.size(); i++) {
      result.add(right.get(i));
    }

    return result;
  }

  static ArrayList<Integer> vector_merge_sort(ArrayList<Integer> source) {
    int length = source.size();
    if (length <= 1) {
      return source;
    }

    int middle = length / 2 - 1;
    ArrayList<Integer> left = new ArrayList<Integer>(middle + 1);
    ArrayList<Integer> right = new ArrayList<Integer>(middle + 1);

    for (int i = 0; i <= middle; i++) {
      left.add(source.get(i));
    }
    for (int i = middle + 1; i < length; i++) {
      right.add(source.get(i));
    }

    left = vector_merge_sort(left);
    right = vector_merge_sort(right);
    ArrayList<Integer> result = vector_merge(left, right);
    return result;
  }

  static void swap(List<Integer> source, int indexA, int indexB) {
    int temp = source.get(indexA);
    source.set(indexA, source.get(indexB));
    source.set(indexB, temp);
  }

  static void vector_sift_down(ArrayList<Integer> source, int start, int end) {
    // end represents the limit of how far down the heap to sift.

    int root = start;
    // While the root has at least one child
    while (root * 2 + 1 <= end) {
      // root*2+1 points to the left child
      int child = root * 2 + 1;
      // If the child has a sibling and the child's value is less than its sibling's...
      number_of_operations++;
      if (child + 1 <= end && source.get(child) < source.get(child + 1)) {
        // ... then point to the right child instead
        child++;
      }
      // out of max-heap order
      number_of_operations++;
      if (source.get(root) < source.get(child)) {
        swap(source, root, child);
        // repeat to continue sifting down the child now
        root = child;
      } else {
        return;
      }
    }
  }

  static void vector_heapify(ArrayList<Integer> source) {
    // start is assigned the index in a of the last parent node
    int start = (source.size() - 2) / 2;

    while (start >= 0) {
      // sift down the node at index start to the proper place such that all nodes below
      // the start index are in heap order)
      vector_sift_down(source, start, source.size() - 1);
      start--;
    // after sifting down the root all nodes/elements are in heap order
    }
  }

  static ArrayList<Integer> vector_heap_sort(ArrayList<Integer> source) {
    ArrayList<Integer> result = new ArrayList<Integer>(source);
    vector_heapify(result);

    int end = result.size() - 1;
    while (end > 0) {
      // swap the root(maximum value) of the heap with the last element of the heap
      swap(result, 0, end);
      // decrease the size of the heap by one so that the previous max value will
      // stay in its proper placement
      end--;
      // put the heap back in max-heap order
      vector_sift_down(result, 0, end);
    }
    return result;
  }

  static void test_vector_heap_sort(String filename, boolean output) throws IOException {
    ArrayList<Integer> vector_data = vector_read_data(filename);
    ArrayList<Integer> result = vector_heap_sort(vector_data);
    if (output) {
      dump("[vector heap_sort] Input data: ", vector_data);
      dump("[vector heap_sort] Result: ", result);
    }
  }

  static void list_sift_down(LinkedList<Integer> source, int start, int end) {
    // end represents the limit of how far down the heap to sift.

    int root = start;
    // While the root has at least one child
    while (root * 2 + 1 <= end) {
      // root*2+1 points to the left child
      int child = root * 2 + 1;

      // If the child has a sibling and the child's value is less than its sibling's...
      number_of_operations++;
      if (child + 1 <= end && source.get(child) < source.get(child + 1)) {
        // ... then point to the right child instead
        child++;
      }

      // out of max-heap order
      number_of_operations++;
      if (source.get(root) < source.get(child)) {
        swap(source, root, child);
        // repeat to continue sifting down the child now
        root = child;
      } else {
        return;
      }
    }
  }

  static void list_heapify(LinkedList<Integer> source) {
    // start is assigned the index in a of the last parent node
    int start = (source.size() - 2) / 2;

    while (start >= 0) {
      // sift down the node at index start to the proper place such that all nodes below
      // the start index are in heap order)
      list_sift_down(source, start, source.size() - 1);
      start--;
    // after sifting down the root all nodes/elements are in heap order
    }
  }

  static LinkedList<Integer> list_heap_sort(LinkedList<Integer> source) {
    LinkedList<Integer> result = new LinkedList<Integer>(source);
    list_heapify(result);

    int end = result.size() - 1;
    while (end > 0) {
      // swap the root(maximum value) of the heap with the last element of the heap
      swap(result, 0, end);
      // decrease the size of the heap by one so that the previous max value will
      // stay in its proper placement
      end--;
      // put the heap back in max-heap order
      list_sift_down(result, 0, end);
    }
    return result;
  }

  static void test_list_heap_sort(String filename, boolean output) throws IOException {
    LinkedList<Integer> list_data = list_read_data(filename);
    LinkedList<Integer> result = list_heap_sort(list_data);
    if (output) {
      dump("[list heap_sort] Input data: ", list_data);
      dump("[list heap_sort] Result: ", result);
    }
  }

/// countingSort - sort an array of values.
///
/// For best results the range of values to be sorted
/// should not be significantly larger than the number of
/// elements in the array.
  static ArrayList<Integer> vector_counting_sort(ArrayList<Integer> source) {
    ArrayList<Integer> nums = new ArrayList<Integer>(source);
    int size = nums.size();
    // search for the minimum and maximum values in the input
    int i, min = nums.get(0), max = min;
    for (i = 1; i < size; ++i) {
      if (nums.get(i) < min) {
        min = nums.get(i);
      } else if (nums.get(i) > max) {
        max = nums.get(i);
      }
    }

    // create a counting array, counts, with a member for
    // each possible discrete value in the input.
    // request compiler to value-initialize all counts to 0.
    int distinct_element_count = max - min + 1;
    int[] counts = new int[distinct_element_count];

    // accumulate the counts -
    // each index in the counts array represents the value
    // of an element in the input nums array, so the result
    // of incrementing the sum at the index in the
    // counts array reflects the number of times the element
    // appears in the input array and therefore
    // must be copied to the sorted output.
    for (i = 0; i < size; ++i) {
      counts[nums.get(i) - min] += 1;
    }

    // store back into the input array the sorted value as
    // represented by the respective index in the counts array.
    // repeat for each additional occurrence of the value
    // found in the original array, as recorded by the
    // counts array.
    int j = 0;
    for (i = min; i <= max; i++) {
      number_of_operations++;
      for (int z = 0; z < counts[i - min]; z++) {
        nums.set(j++, i);
        number_of_operations++;
      }
    }

    return nums;
  }

/// countingSort - sort an array of values.
///
/// For best results the range of values to be sorted
/// should not be significantly larger than the number of
/// elements in the array.
  static LinkedList<Integer> list_counting_sort(LinkedList<Integer> source) {
    LinkedList<Integer> nums = new LinkedList<Integer>(source);
    int size = nums.size();
    // search for the minimum and maximum values in the input
    int i, min = nums.get(0), max = min;
    for (i = 1; i < size; ++i) {
      if (nums.get(i) < min) {
        min = nums.get(i);
      } else if (nums.get(i) > max) {
        max = nums.get(i);
      }
    }

    // create a counting array, counts, with a member for
    // each possible discrete value in the input.
    // request compiler to value-initialize all counts to 0.
    int distinct_element_count = max - min + 1;
    int[] counts = new int[distinct_element_count];

    // accumulate the counts -
    // each index in the counts array represents the value
    // of an element in the input nums array, so the result
    // of incrementing the sum at the index in the
    // counts array reflects the number of times the element
    // appears in the input array and therefore
    // must be copied to the sorted output.
    for (i = 0; i < size; ++i) {
      counts[nums.get(i) - min] += 1;
    }

    // store back into the input array the sorted value as
    // represented by the respective index in the counts array.
    // repeat for each additional occurrence of the value
    // found in the original array, as recorded by the
    // counts array.
    int j = 0;
    for (i = min; i <= max; i++) {
      number_of_operations++;
      for (int z = 0; z < counts[i - min]; z++) {
        number_of_operations++;
        nums.set(j++, i);
      }
    }

    return nums;
  }

  public static void help() {
    System.err.print("Usage: sort_algorithms storage algorithm input_file [output_file]\n\n" +
      "Storages:   vector list\n" +
      "Algorithms: counting heap merge\n");
  }

  public static void write_data(List<Integer> data, String filename) throws IOException {
    FileWriter out = new FileWriter(filename);

    out.write(String.format("%d\n\n", data.size()));

    for (Integer i: data) {
      out.write(String.format("%d\n", i));
    }

    out.close();
  }

  static long number_of_operations = 0;

  public static void main(String[] args) throws IOException {
    if (args.length < 3) {
        help();
        System.exit(1);
    }

    int runs = 1;
    String storage_type = args[0];
    String algorithm = args[1];
    String filename = args[2];
    String out_filename = "results.txt";
    if (args.length == 4)
        out_filename = args[3];

    double time = 0;
    int data_size = 0;

    if (storage_type.equals("vector")) {
            ArrayList<Integer> data = vector_read_data(filename);
            data_size = data.size();
            ArrayList<Integer> result = null;

            double start_time = System.currentTimeMillis();
            for (int i = 0; i < runs; i++) {
                if (algorithm.equals("counting")) {
                    result = vector_counting_sort(data);
                }
                else if (algorithm.equals("heap")) {
                    result = vector_heap_sort(data);
                }
                else if (algorithm.equals("merge")) {
                    result = vector_merge_sort(data);
                }
            }
            time = System.currentTimeMillis() - start_time;

            write_data(result, out_filename);
    }
    else if (storage_type.equals("list")) {
            LinkedList<Integer> data = list_read_data(filename);
            data_size = data.size();
            LinkedList<Integer> result = null;

            double start_time = System.currentTimeMillis();
            for (int i = 0; i < runs; i++) {
                if (algorithm.equals("counting")) {
                    result = list_counting_sort(data);
                }
                else if (algorithm.equals("heap")) {
                    result = list_heap_sort(data);
                }
                else if (algorithm.equals("merge")) {
                    result = list_merge_sort(data);
                }
            }
            time = System.currentTimeMillis() - start_time;

            write_data(result, out_filename);
    }

    time = time / 1000; // in seconds

    System.out.print(String.format(
            "%s;%s;%d;%d;%.4f;%.4f;%d\n",
            storage_type, algorithm, runs,
            data_size, time / runs, time, number_of_operations
            )
    );
  }
}
