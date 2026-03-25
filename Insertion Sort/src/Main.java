public class Main {
    public static void main(String[] args){

        int[] array = {3, 1, 8, 20, 5, 0, -5, 1, 25};
        insertionSort(array);

        for(int i = 0; i < array.length; i++) {
            System.out.println("i: " + i + " = " + array[i]);
        }
    }

    private static void insertionSort(int[] array) {

        for(int i = 1; i < array.length; i++){

            int j = i-1;
            int temp_minValue = array[i];

            while(j>= 0 && array[j] > temp_minValue){
                array[j+1] = array[j];
                j--;
            }

            array[j+1] = temp_minValue;
        }

    }
}
