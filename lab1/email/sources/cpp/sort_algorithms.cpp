/* 
 * Author: x11
 *
 * Created on March 16, 2009, 8:59 PM
 */

#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <list>
#include <time.h>
#include <boost/timer.hpp>
using namespace std;

void list_dump(string title, list<int> &source) {
    list<int>::iterator it;

    cout << title;
    for (it = source.begin(); it != source.end(); it++)
        cout << *it << ", ";
    cout << endl;
}

list<int> list_read_data(string filename) {
    list<int> data;
    ifstream inFile;
    inFile.open(filename.c_str());

    if (!inFile) {
        cerr << "Unable to open file " << filename << endl;
        exit(1); // call system to stop
    }

    int length;
    inFile >> length;
    for (int i = 0; i < length; i++) {
        int number;
        inFile >> number;
        data.push_back(number);
    }

    return data;
}

void list_write_data(list<int> &data, string filename) {
    ofstream file;
    file.open(filename.c_str());

    if (!file) {
        cerr << "Unable to open file " << filename << endl;
        exit(1); // call system to stop
    }

    file << data.size() << "\n\n";
    list<int>::iterator it;
    for (it = data.begin(); it != data.end(); it++) {
        file << *it << "\n";
    }

    file.close();
}

list<int> list_merge(list<int> &left, list<int> &right, long &number_of_operations) {
    list<int> result;
    while (left.size() > 0 && right.size() > 0) {
        if (left.front() <= right.front()) {
            result.push_back(left.front());
            left.pop_front();
        } else {
            result.push_back(right.front());
            right.pop_front();
        }

        number_of_operations++;
    }

    result.splice(result.end(), left);
    result.splice(result.end(), right);

    return result;
}

list<int> list_merge_sort(list<int> source, long &number_of_operations) {
    int length = source.size();
    if (length <= 1) {
        return source;
    }

    int middle = length / 2 - 1;
    list<int> left, right;

    list<int>::iterator it = source.begin();
    for (int i = 0; i <= middle; i++) {
        left.push_back(*it);
        it++;
    }
    for (int i = middle + 1; i < length; i++) {
        right.push_back(*it);
        it++;
    }

    left = list_merge_sort(left, number_of_operations);
    right = list_merge_sort(right, number_of_operations);
    list<int> result = list_merge(left, right, number_of_operations);
    return result;
}

void vector_dump(string title, vector<int> &source) {
    vector<int>::iterator it;

    cout << title;
    for (it = source.begin(); it < source.end(); it++)
        cout << *it << ", ";
    cout << endl;
}

vector<int> vector_read_data(string filename) {
    vector<int> data;
    ifstream inFile;
    inFile.open(filename.c_str());

    if (!inFile) {
        cerr << "Unable to open file " << filename << endl;
        exit(1); // call system to stop
    }

    int length;
    inFile >> length;
    data.reserve(length);
    for (int i = 0; i < length; i++) {
        int number;
        inFile >> number;
        data.push_back(number);
    }

    return data;
}

void vector_write_data(vector<int> &data, string filename) {
    ofstream file;
    file.open(filename.c_str());

    if (!file) {
        cerr << "Unable to open file " << filename << endl;
        exit(1); // call system to stop
    }

    file << data.size() << "\n\n";
    vector<int>::iterator it;
    for (it = data.begin(); it < data.end(); it++) {
        file << *it << "\n";
    }

    file.close();
}

vector<int> vector_merge(vector<int> &left, vector<int> &right, long &number_of_operations) {
    vector<int> result;
    int leftIndex = 0, rightIndex = 0;
    while (leftIndex < left.size() && rightIndex < right.size()) {
        if (left.at(leftIndex) <= right.at(rightIndex)) {
            result.push_back(left.at(leftIndex));
            leftIndex++;
        } else {
            result.push_back(right.at(rightIndex));
            rightIndex++;
        }
        number_of_operations++;
    }

    for (int i = leftIndex; i < left.size(); i++) {
        result.push_back(left.at(i));
    }
    for (int i = rightIndex; i < right.size(); i++) {
        result.push_back(right.at(i));
    }

    return result;
}

vector<int> vector_merge_sort(vector<int> &source, long &number_of_operations) {
    int length = source.size();
    if (length <= 1) {
        return source;
    }

    int middle = length / 2 - 1;
    vector<int> left, right;
    left.reserve(middle + 1);
    right.reserve(middle + 1);

    for (int i = 0; i <= middle; i++) {
        left.push_back(source.at(i));
    }
    for (int i = middle + 1; i < length; i++) {
        right.push_back(source.at(i));
    }

    left = vector_merge_sort(left, number_of_operations);
    right = vector_merge_sort(right, number_of_operations);
    vector<int> result = vector_merge(left, right, number_of_operations);
    return result;
}

void swap(int &a, int &b) {
    int temp = a;
    a = b;
    b = temp;
}

void vector_sift_down(vector<int> &source, int start, int end, long &number_of_operations) {
    // end represents the limit of how far down the heap to sift.

    int root = start;
    // While the root has at least one child
    while (root * 2 + 1 <= end) {
        // root*2+1 points to the left child
        int child = root * 2 + 1;
        // If the child has a sibling and the child's value is less than its sibling's...
        number_of_operations++;
        if (child + 1 <= end && source.at(child) < source.at(child + 1)) {
            // ... then point to the right child instead
            child++;
        }
        // out of max-heap order
        number_of_operations++;
        if (source.at(root) < source.at(child)) {
            swap(source.at(root), source.at(child));
            // repeat to continue sifting down the child now
            root = child;
        } else {
            return;
        }
    }
}

void vector_heapify(vector<int> &source, long &number_of_operations) {
    // start is assigned the index in a of the last parent node
    int start = (source.size() - 2) / 2;

    while (start >= 0) {
        // sift down the node at index start to the proper place such that all nodes below
        // the start index are in heap order)
        vector_sift_down(source, start, source.size() - 1, number_of_operations);
        start--;
        // after sifting down the root all nodes/elements are in heap order
    }
}

vector<int> vector_heap_sort(vector<int> &source, long &number_of_operations) {
    vector<int> result = vector<int>(source);
    vector_heapify(result, number_of_operations);

    int end = result.size() - 1;
    while (end > 0) {
        // swap the root(maximum value) of the heap with the last element of the heap
        swap(result.at(0), result.at(end));
        // decrease the size of the heap by one so that the previous max value will
        // stay in its proper placement
        end--;
        // put the heap back in max-heap order
        vector_sift_down(result, 0, end, number_of_operations);
    }
    return result;
}

void list_swap(list<int> &l1, list<int>::iterator it1,
        list<int> &l2, list<int>::iterator it2) {
    l1.insert(it1, *it2);
    l2.insert(it2, *it1);
    l1.erase(it1);
    l2.erase(it2);
}

void list_sift_down(list<int> &source, int start, int end, long &number_of_operations) {
    // end represents the limit of how far down the heap to sift.

    int root = start;
    // While the root has at least one child
    while (root * 2 + 1 <= end) {
        // root*2+1 points to the left child
        int child = root * 2 + 1;

        // If the child has a sibling and the child's value is less than its sibling's...
        list<int>::iterator it = source.begin();
        advance(it, child);
        if (child + 1 <= end) {
            list<int>::iterator it1 = it;
            it1++;

            number_of_operations++;
            if (*it < *it1) {
                // ... then point to the right child instead
                child++;
                it = it1;
            }
        }

        // out of max-heap order
        list<int>::iterator root_it = source.begin();
        advance(root_it, root);
        
        number_of_operations++;
        if (*root_it < *it) {
            list_swap(source, root_it, source, it);
            // repeat to continue sifting down the child now
            root = child;
        } else {
            return;
        }
    }
}

void list_heapify(list<int> &source, long &number_of_operations) {
    // start is assigned the index in a of the last parent node
    int start = (source.size() - 2) / 2;

    while (start >= 0) {
        // sift down the node at index start to the proper place such that all nodes below
        // the start index are in heap order)
        list_sift_down(source, start, source.size() - 1, number_of_operations);
        start--;
        // after sifting down the root all nodes/elements are in heap order
    }
}

list<int> list_heap_sort(list<int> &source, long &number_of_operations) {
    list<int> result = list<int>(source);
    list_heapify(result, number_of_operations);

    int end = result.size() - 1;
    while (end > 0) {
        // swap the root(maximum value) of the heap with the last element of the heap
        list<int>::iterator end_it = result.begin();
        advance(end_it, end);
        list_swap(result, result.begin(), result, end_it);
        // decrease the size of the heap by one so that the previous max value will
        // stay in its proper placement
        end--;
        // put the heap back in max-heap order
        list_sift_down(result, 0, end, number_of_operations);
    }
    return result;
}

/// countingSort - sort an array of values.
///
/// For best results the range of values to be sorted
/// should not be significantly larger than the number of
/// elements in the array.
vector<int> vector_counting_sort(vector<int> &source, long &number_of_operations) {
    vector<int> nums = vector<int>(source);
    int size = nums.size();
    // search for the minimum and maximum values in the input
    int i, min = nums.at(0), max = min;
    for (i = 1; i < size; ++i) {
        if (nums.at(i) < min)
            min = nums.at(i);
        else if (nums[i] > max)
            max = nums.at(i);
    }

    // create a counting array, counts, with a member for
    // each possible discrete value in the input.
    // request compiler to value-initialize all counts to 0.
    int distinct_element_count = max - min + 1;
    int *counts = new int[distinct_element_count]();

    // accumulate the counts -
    // each index in the counts array represents the value
    // of an element in the input nums array, so the result
    // of incrementing the sum at the index in the
    // counts array reflects the number of times the element
    // appears in the input array and therefore
    // must be copied to the sorted output.
    for (i = 0; i < size; ++i) {
        ++counts[ nums[i] - min ];
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
            nums.at(j++) = i;
            number_of_operations++;
        }
    }

    delete[] counts;

    return nums;
}

/// countingSort - sort an array of values.
///
/// For best results the range of values to be sorted
/// should not be significantly larger than the number of
/// elements in the array.
list<int> list_counting_sort(list<int> &nums, long &number_of_operations) {
    int size = nums.size();
    // search for the minimum and maximum values in the input
    int i, min = nums.front(), max = min;
    for (list<int>::iterator it = nums.begin(); it != nums.end(); it++) {
        if (*it < min)
            min = *it;
        else if (*it > max)
            max = *it;
    }

    // create a counting array, counts, with a member for
    // each possible discrete value in the input.
    // request compiler to value-initialize all counts to 0.
    int distinct_element_count = max - min + 1;
    int *counts = new int[distinct_element_count]();

    // accumulate the counts -
    // each index in the counts array represents the value
    // of an element in the input nums array, so the result
    // of incrementing the sum at the index in the
    // counts array reflects the number of times the element
    // appears in the input array and therefore
    // must be copied to the sorted output.
    for (list<int>::iterator it = nums.begin(); it != nums.end(); it++)
        ++counts[ *it - min ];

    // store back into the input array the sorted value as
    // represented by the respective index in the counts array.
    // repeat for each additional occurrence of the value
    // found in the original array, as recorded by the
    // counts array.
    list<int> result;
    for (i = min; i <= max; i++) {
        number_of_operations++;
        for (int z = 0; z < counts[i - min]; z++) {
            result.push_back(i);
            number_of_operations++;
        }
    }

    delete[] counts;

    return result;
}

void help() {
    cerr << "Usage: sort_algorithms storage algorithm input_file [output_file]\n\n"  <<
            "Storages:   vector list\n" <<
            "Algorithms: counting heap merge\n";
}

int main(int argc, char** argv) {
    if (argc < 4) {
        help();
        exit(1);
    }

    const int runs = 1;
    string storage_type = argv[1];
    string algorithm = argv[2];
    string filename = argv[3];
    string out_filename = "results.txt";
    if (argc == 5)
        out_filename = argv[4];

    double time;
    int data_size;
    long number_of_operations = 0;
    
    if (storage_type == "vector") {
            vector<int> data = vector_read_data(filename);
            data_size = data.size();
            vector<int> result;

            boost::timer timer;
            for (int i = 0; i < runs; i++) {
                if (algorithm == "counting") {
                    result = vector_counting_sort(data, number_of_operations);
                }
                else if (algorithm == "heap") {
                    result = vector_heap_sort(data, number_of_operations);
                }
                else if (algorithm == "merge") {
                    result = vector_merge_sort(data, number_of_operations);
                }
            }
            time = timer.elapsed();

            vector_write_data(result, out_filename);
    }
    else if (storage_type == "list") {
            list<int> data = list_read_data(filename);
            data_size = data.size();
            list<int> result;

            boost::timer timer;
            for (int i = 0; i < runs; i++) {
                if (algorithm == "counting") {
                    result = list_counting_sort(data, number_of_operations);
                }
                else if (algorithm == "heap") {
                    result = list_heap_sort(data, number_of_operations);
                }
                else if (algorithm == "merge") {
                    result = list_merge_sort(data, number_of_operations);
                }
            }
            time = timer.elapsed();

            list_write_data(result, out_filename);
    }

    printf("%s;%s;%d;%d;%.4f;%.4f;%d\n", storage_type.c_str(), algorithm.c_str(), runs,
            data_size, time / runs, time, number_of_operations);

    return (EXIT_SUCCESS);
}

