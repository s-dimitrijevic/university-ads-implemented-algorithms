public class Main{
	
	public static void main(String[] args){
		
		int[] array = {1,2,3,4,5,6,7,8,9,10};
		int target = 8;
		
		int targetIndex = binarySearch(array,target);
	}

	public static int binarySearch(int[] array, int target){
		
		int left = 0;
		int right = array.length-1;
		int index = -1;
		
		while (left <= right){
			
			int mid = left + (right - left)/2;
			
			//case 1: target found 
			if(array[mid] == target){
				index = mid;
				return mid;
			}
			
			//case 2: target > mid
			else if(target > array[mid]){
				left = mid + 1;
			}
			
			//case 3: target < mid
			else{
				right = mid - 1;
		}
	}
}