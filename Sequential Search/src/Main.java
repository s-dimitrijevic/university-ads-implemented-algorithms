public class Main {
	
	public static void main(String[] args){
		
		int[] array = {1,2,3,4,5,6,7,8,9,10};
		int target = 8;
		
		int targetIndex = sequentialSearch(array,target);
	}

	public static int sequentialSearch(int[] array, int target){
		
		int index = -1;
		
		for(int i = 0; i < array.length; i++){
			if(target == array[i]){
				index = array[i];
			}
		}

		return index;
	}
}