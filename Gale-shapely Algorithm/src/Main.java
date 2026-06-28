import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args){

        int[][] male_preferences = {
                {0, 1, 2, 3},  // momak 0: voli 0 > 1 > 2 > 3
                {0, 2, 1, 3},  // momak 1: voli 0 > 2 > 1 > 3
                {1, 0, 2, 3},  // momak 2: voli 1 > 0 > 2 > 3
                {1, 2, 0, 3}   // momak 3: voli 1 > 2 > 0 > 3
        };

        int[][] female_preferences = {
                {1, 0, 2, 3},  // devojka 0: voli 1 > 0 > 2 > 3
                {2, 3, 0, 1},  // devojka 1: voli 2 > 3 > 0 > 1
                {0, 1, 2, 3},  // devojka 2: voli 0 > 1 > 2 > 3
                {0, 1, 2, 3}   // devojka 3: voli 0 > 1 > 2 > 3
        };

        int[] happy_couples = find_stable_marriages(male_preferences, female_preferences);
        for(int i = 0; i < happy_couples.length; i++){
            System.out.println("Women " +i+ " is in love with a men " +happy_couples[i]);
        }

    }

    public static int[] find_stable_marriages(int[][] malePref, int[][] femalePref){

        //Slobodni momci — direktan pristup
        int n = malePref.length;
        Queue<Integer> freeMales = new LinkedList<>();
        for (int i = 0; i < n; i++) freeMales.add(i);

        //Verenik svake devojke
        //Indeks = devojka, vrednost = momak
        int[] partner = new int[4];
        Arrays.fill(partner, -1);

        //Pointer prosidbe svakog momka
        int[] next = new int[4];

        int male = next[0];
        int female = 0;

        //dok lista slobodnih momaka nije prazna
        while(!freeMales.isEmpty()){

           int m = freeMales.poll();
           int d = malePref[m][next[m]];
           next[m]++;

           //case - girl is free
            if(partner[d] == -1)
                partner[d] = m;

            //case - girl is taken
            else{
                int v = partner[d];
                boolean prefersM = false;

                //check female preferences
                for(int j = 0; j < n; j++){
                    if(femalePref[d][j] == m){
                        partner[d] = m;
                        freeMales.add(v);
                        break;
                    }
                    if(femalePref[d][j] == v){
                        freeMales.add(m);
                        break;
                    }
                }
            }
        } //end loop

        return partner;
    }
}
