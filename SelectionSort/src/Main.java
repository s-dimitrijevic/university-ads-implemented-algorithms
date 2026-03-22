public class Main {
    public static void main(String[] args){

        int[] array_1 = {3, 1, 0, 59, -1, 30, 29, 87, 6, -23};

        System.out.println("Numbers to be sorted: ");
        printArray(array_1);

        array_1 = useSelectionSort(array_1);

        System.out.println("\n\nSorted numbers using SELECTION SORT: ");
        printArray(array_1);
    }

    private static int[] useSelectionSort(int[] array1) {

        for(int i = 0; i < array1.length-1; i++){

            int smallestIndex = i;

            for(int j = i+1; j < array1.length; j++){

                if(array1[j] < array1[smallestIndex])
                    smallestIndex = j;
            }

            if(i != smallestIndex) {
                int temp = array1[i];
                array1[i] = array1[smallestIndex];
                array1[smallestIndex] = temp;
            }
        }
        return array1;
    }

    private static void printArray(int[] array1){

        for(int i = 0; i < array1.length; i++){
            if(i == array1.length-1)
                System.out.print(array1[i]);
            else
                System.out.print(+array1[i] + ", ");
        }
    }
}
