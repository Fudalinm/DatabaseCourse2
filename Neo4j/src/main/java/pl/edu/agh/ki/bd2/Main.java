package pl.edu.agh.ki.bd2;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();
        solution.databaseStatistics();
        //solution.runAllTests();

        //System.out.println(solution.createActorAndMovieAndFindThem("aktor3","film3","Xdekolandia"));
        //System.out.println(solution.ActorsInMoreThan6Movies());
        //System.out.println(solution.ActorsDirectors());
        //System.out.println(solution.FriendsWellRatedMovies("maheshksp"));

        //System.out.println(solution.PathBetweenTwoActors("Robert De Niro", "Jackie Chan"));
        solution.measureIndexTime();
    }

}
