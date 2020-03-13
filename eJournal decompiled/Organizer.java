// 
// Decompiled by Procyon v0.5.36
// 

public class Organizer
{
    public static void main(final String[] args) {
        final int[] test = { 1, 3, 4, 5, 5, 6, 7, 8, 9, 9, 11, 12 };
        final String[] strArray = { "Mathematics", "Computer Science", "Geography", "Biology", "Physics", "Chemistry", "History", "English", "Psychology", "Phys. Ed.", "Anthropology", "World Religions", "Drama" };
        insertionSort(strArray);
        System.out.println(strArray[sequentialSearch(strArray, "Drama")]);
        System.out.println(strArray[binarySearch(strArray, "Drama")]);
    }
    
    public static void bubbleSort(final int[] intA) {
        boolean isDone = false;
        while (!isDone) {
            isDone = true;
            for (int j = 0; j < intA.length - 1; ++j) {
                if (intA[j] > intA[j + 1]) {
                    final int temp = intA[j];
                    intA[j] = intA[j + 1];
                    intA[j + 1] = temp;
                    isDone = false;
                }
            }
        }
    }
    
    public static void bubbleSort(final Comparable[] compL) {
        boolean isDone = false;
        while (!isDone) {
            isDone = true;
            for (int j = 0; j < compL.length - 1; ++j) {
                if (compL[j].compareTo(compL[j + 1]) > 0) {
                    final Comparable temp = compL[j];
                    compL[j] = compL[j + 1];
                    compL[j + 1] = temp;
                    isDone = false;
                }
            }
        }
    }
    
    public static void selectionSort(final int[] intA) {
        for (int i = 0; i < intA.length; ++i) {
            for (int j = i + 1; j < intA.length; ++j) {
                if (intA[j] > intA[i]) {
                    final int temp = intA[j];
                    intA[j] = intA[i];
                    intA[i] = temp;
                }
            }
        }
    }
    
    public static void selectionSort(final Comparable[] compL) {
        for (int i = 0; i < compL.length; ++i) {
            for (int j = i + 1; j < compL.length; ++j) {
                if (compL[j].compareTo(compL[i]) > 0) {
                    final Comparable temp = compL[j];
                    compL[j] = compL[i];
                    compL[i] = temp;
                }
            }
        }
    }
    
    public static void insertionSort(final int[] numbers) {
        for (int i = 1; i < numbers.length; ++i) {
            int j;
            int currentElement;
            for (j = i, currentElement = numbers[j]; j > 0 && numbers[j - 1] > currentElement; --j) {
                numbers[j] = numbers[j - 1];
            }
            numbers[j] = currentElement;
        }
    }
    
    public static void insertionSort(final Comparable[] CompL) {
        for (int i = 1; i < CompL.length; ++i) {
            int j;
            Comparable currentElement;
            for (j = i, currentElement = CompL[j]; j > 0 && CompL[j - 1].compareTo(currentElement) > 0; --j) {
                CompL[j] = CompL[j - 1];
            }
            CompL[j] = currentElement;
        }
    }
    
    public static int sequentialSearch(final int[] intA, final int k) {
        for (int i = 0; i < intA.length; ++i) {
            if (k == intA[i]) {
                return i;
            }
        }
        return -1;
    }
    
    public static int sequentialSearch(final char[] intA, final char k) {
        for (int i = 0; i < intA.length; ++i) {
            if (k == intA[i]) {
                return i;
            }
        }
        return -1;
    }
    
    public static int sequentialSearch(final Comparable[] compA, final Comparable k) {
        for (int i = 0; i < compA.length; ++i) {
            if (k.equals(compA[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public static int sequentialSearch(final String[] compA, final String k) {
        for (int i = 0; i < compA.length; ++i) {
            if (k.equalsIgnoreCase(compA[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public static int binarySearch(final int[] intA, final int k) {
        int l = 0;
        int h = intA.length - 1;
        while (l <= h) {
            final int m = (l + h) / 2;
            if (k > intA[m]) {
                l = m + 1;
            }
            else if (k < intA[m]) {
                h = m - 1;
            }
            else {
                if (k == intA[m]) {
                    return m;
                }
                continue;
            }
        }
        return -1;
    }
    
    public static int binarySearch(final Comparable[] compA, final Comparable k) {
        int l = 0;
        int h = compA.length - 1;
        while (l <= h) {
            final int m = (l + h) / 2;
            if (k.compareTo(compA[m]) > 0) {
                l = m + 1;
            }
            else if (k.compareTo(compA[m]) < 0) {
                h = m - 1;
            }
            else {
                if (k.equals(compA[m])) {
                    return m;
                }
                continue;
            }
        }
        return -1;
    }
}
