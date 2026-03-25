public class Main {
    public static void main(String[] arrgs){
        System.out.println("H");

        int[] array = {3, 5, -5, 20, 0, 1, -123, 21, 57};
        bubbleSort(array);

        for(int i = 0; i < array.length; i++){
            System.out.println("i: "+i+" = " +array[i]);
        }
    }

    private static void bubbleSort(int[] array) {

        for(int i = 0; i < array.length; i++){

            for(int j = 0; j < array.length-i-1; j++){

                if(array[j] > array[j+1]){
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }
    }
}
