public class Main {
    public static void main(String[] args){

        int[] array = {3,2,5,-1,90,256, 32, -15, 0, 1};

        System.out.println("Unsorted array: ");
        printArray(array);

        mergeSort(array, 0, array.length);
        System.out.println("\n================================\n");

        System.out.println("Sorted array using Merge Sort: ");
        printArray(array);
    }

    static void mergeSort(int[] array, int low, int high) {
        if(high - low <= 1)
            return;

        int mid = (low + high) / 2;

        mergeSort(array, low, mid);
        mergeSort(array, mid, high);
        merge(array, low, mid, high);
    }

    static void merge(int[] array, int low, int mid, int high) {

        int[] temp = new int[high-low];
        int i = low, j = mid, k = 0;

        while(i < mid && j < high){

            if(array[i] <= array[j])
                temp[k++] = array[i++];
            else
                temp[k++] = array[j++];
        }

        while(i < mid)
            temp[k++] = array[i++];

        while(j < high)
            temp[k++] = array[j++];

        System.arraycopy(temp, 0, array, low, temp.length);
    }

    static void printArray(int[] array){
        for(int i: array){System.out.println("i: "+i);}
    }
}
