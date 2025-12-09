import java.util.*;

public class Main {

    // 1. Bubble Sort
    // So sánh từng cặp phần tử kề nhau và đổi chỗ nếu sai thứ tự.
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // 2. Selection Sort
    // Tìm phần tử nhỏ nhất trong đoạn chưa sắp xếp và đưa lên đầu.
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) minIndex = j;
            }
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    // 3. Insertion Sort
    // Chèn phần tử vào đúng vị trí trong phần đã sắp xếp.
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }


    /* --------------------- B. INTERMEDIATE SORTS --------------------- */

    // 4. Merge Sort
    // Chia mảng thành các mảng con rồi trộn lại theo thứ tự tăng dần.
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
        }

        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // 5. Quick Sort
    // Chọn pivot, chia mảng thành 2 phần nhỏ hơn/lớn hơn pivot, rồi đệ quy.
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }


    // 6. Shell Sort
    // Giảm dần khoảng cách (gap) rồi dùng insertion sort theo mỗi gap.
    public static void shellSort(int[] arr) {
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                int temp = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }


    /* ------------------------ C. ADVANCED SORTS ------------------------ */

    // 7. Heap Sort
    // Xây dựng max-heap rồi liên tục lấy phần tử lớn nhất ra ngoài.
    public static void heapSort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);

        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    public static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(arr, n, largest);
        }
    }

    // 8. Counting Sort
    // Đếm số lượng xuất hiện của từng giá trị rồi sắp xếp lại.
    public static void countingSort(int[] arr, int maxValue) {
        int[] count = new int[maxValue + 1];
        int[] output = new int[arr.length];

        for (int num : arr) count[num]++;

        for (int i = 1; i <= maxValue; i++)
            count[i] += count[i - 1];

        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i]] - 1] = arr[i];
            count[arr[i]]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    // 9. Radix Sort
    // Sắp xếp theo từng chữ số (1s, 10s, 100s...) dùng counting sort.
    public static void radixSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        for (int exp = 1; max / exp > 0; exp *= 10)
            countingSortByDigit(arr, exp);
    }

    public static void countingSortByDigit(int[] arr, int exp) {
        int[] output = new int[arr.length];
        int[] count = new int[10];

        for (int num : arr)
            count[(num / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = arr.length - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    // 10. Bucket Sort
    // Chia các phần tử (0-1) vào bucket, sort từng bucket, rồi ghép lại.
    public static void bucketSort(float[] arr) {
        int n = arr.length;
        List<Float>[] buckets = new List[n];

        for (int i = 0; i < n; i++)
            buckets[i] = new ArrayList<>();

        for (float num : arr)
            buckets[(int) (num * n)].add(num);

        for (List<Float> bucket : buckets)
            Collections.sort(bucket);

        int idx = 0;
        for (List<Float> bucket : buckets)
            for (float num : bucket)
                arr[idx++] = num;
    }

    /* ============================================================
     *                    II. SEARCHING ALGORITHMS
     * ============================================================ */

    // 1. Linear Search
    // Duyệt từng phần tử cho đến khi tìm thấy target.
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == target) return i;
        return -1;
    }

    // 2. Binary Search
    // Chỉ hoạt động trên mảng đã sắp xếp. Chia đôi mảng liên tục.
    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // 3. Interpolation Search
    // Tìm vị trí gần đúng dựa trên tỷ lệ giá trị.
    public static int interpolationSearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;

        while (low <= high && target >= arr[low] && target <= arr[high]) {
            int pos = low + (target - arr[low]) * (high - low) / (arr[high] - arr[low]);
            if (arr[pos] == target) return pos;
            if (arr[pos] < target) low = pos + 1;
            else high = pos - 1;
        }
        return -1;
    }

    // 4. Exponential Search
    // Tăng index theo mũ 2 rồi dùng binary search.
    public static int exponentialSearch(int[] arr, int target) {
        if (arr[0] == target) return 0;

        int i = 1;
        while (i < arr.length && arr[i] <= target)
            i *= 2;

        return binarySearch(Arrays.copyOfRange(arr, i / 2, Math.min(i, arr.length)), target);
    }

    // 5. Jump Search
    // Nhảy theo bước cố định (sqrt(n)) rồi tìm từng phần tử.
    public static int jumpSearch(int[] arr, int target) {
        int n = arr.length;
        int step = (int) Math.sqrt(n);
        int prev = 0;

        while (arr[Math.min(step, n) - 1] < target) {
            prev = step;
            step += (int) Math.sqrt(n);
            if (prev >= n) return -1;
        }

        for (int i = prev; i < Math.min(step, n); i++)
            if (arr[i] == target) return i;

        return -1;
    }


    /* ============================================================
     *                    III. MAIN DEMO EXECUTION
     * ============================================================ */

    public static void main(String[] args) {

        System.out.println("=== DEMO SORTING ALGORITHMS ===");

        int[] arr = {9, 3, 1, 5, 13, 12};
        int[] arr2 = arr.clone();
        int[] arr3 = arr.clone();

        bubbleSort(arr2);
        System.out.println("Bubble Sort: " + Arrays.toString(arr2));

        selectionSort(arr3);
        System.out.println("Selection Sort: " + Arrays.toString(arr3));

        int[] mergeArr = arr.clone();
        mergeSort(mergeArr, 0, mergeArr.length - 1);
        System.out.println("Merge Sort: " + Arrays.toString(mergeArr));

        int[] quickArr = arr.clone();
        quickSort(quickArr, 0, quickArr.length - 1);
        System.out.println("Quick Sort: " + Arrays.toString(quickArr));

        System.out.println("\n=== DEMO SEARCHING ALGORITHMS ===");

        int[] sortedArr = quickArr;
        int target = 5;

        System.out.println("Linear Search (5): " + linearSearch(sortedArr, target));
        System.out.println("Binary Search (5): " + binarySearch(sortedArr, target));
        System.out.println("Interpolation Search (5): " + interpolationSearch(sortedArr, target));
        System.out.println("Jump Search (5): " + jumpSearch(sortedArr, target));

        System.out.println("\n=== END DEMO ===");
    }
}
