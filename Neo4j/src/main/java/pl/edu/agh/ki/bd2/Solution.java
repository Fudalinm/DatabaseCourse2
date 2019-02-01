package pl.edu.agh.ki.bd2;

public class Solution {

    private final GraphDatabase graphDatabase = GraphDatabase.createDatabase();

    public void databaseStatistics() {
        System.out.println(graphDatabase.runCypher("CALL db.labels()"));
        System.out.println(graphDatabase.runCypher("CALL db.relationshipTypes()"));
    }

    public void runAllTests() {
        System.out.println(findActorByName("Emma Watson"));
        System.out.println(findMovieByTitleLike("Star Wars"));
        System.out.println(findRatedMoviesForUser("maheshksp"));
        System.out.println(findCommonMoviesForActors("Emma Watson", "Daniel Radcliffe"));
        System.out.println(findMovieRecommendationForUser("emileifrem"));


    }

    private String findActorByName(final String actorName) {
        String cypher = "Match (a:Actor {name: '" + actorName + "'}) Return a.id,a.name";
        return graphDatabase.runCypher(cypher);
    }

    private String findMovieByTitleLike(final String movieName) {
        String cypher = "Match (m:Movie) " +
                "WHERE m.title =~ '.*" + movieName + ".*'" +
                "Return m.id,m.title";
        return graphDatabase.runCypher(cypher);
    }

    private String findRatedMoviesForUser(final String userLogin) {
        String cypher = String.format("MATCH (u:User)-[RATED]->(m:Movie) " +
                "Where u.login= \'%s\'" +
                " Return m.id,m.title", userLogin);
        return graphDatabase.runCypher(cypher);
    }

    private String findCommonMoviesForActors(String actorOne, String actrorTwo) {
        String cypher = String.format("MATCH (a1:Actor)-[:ACTS_IN]->(m:Movie)<-[:ACTS_IN]-(a2:Actor) " +
                "Where a1.name= \'%s\' and a2.name= \'%s\'" +
                " Return m.id,m.title", actorOne, actrorTwo);
        return graphDatabase.runCypher(cypher);
    }

    private String findMovieRecommendationForUser(final String userLogin) {
        String cypher = String.format("MATCH (u:User)-[:RATED]->(m1:Movie)<-[:ACTS_IN]-(a:Actor) -[:ACTS_IN]-> (m2:Movie) " +
                "Where u.login= \'%s\' and m1 <> m2" +
                " Return m2.id,m2.title", userLogin);
       return graphDatabase.runCypher(cypher);

    }

    /** DODATKOWE  */
//zadanie 4 i 5
    public String createActorAndMovieAndFindThem(String actorname, String movieTitle, String birthPlace) {
        String createActor = String.format("Create (a:Actor {name : '%s'}) -[:ACTS_IN]->   (m:movie {title : '%s'}) Return a.name,m.title ",
                actorname, movieTitle);
        String findActor = String.format("match (a:Actor {name : '%s'}) -[:ACTS_IN]->   (m:movie {title : '%s'}) Return a.name,m.title ",
                actorname, movieTitle);

        //ustawiamy parametry

        String setAttributes = String.format("match (a:Actor {name : '%s'}) \n Set a.birthplace = '%s' Return a", actorname, birthPlace);

        return graphDatabase.runCypher(createActor) + "\n" + graphDatabase.runCypher(findActor) + "\n" + graphDatabase.runCypher(setAttributes);
    }

//6. Zapytanie o aktorów którzy grali w conajmniej 6 filmach (użyć collect i length)
    public String ActorsInMoreThan6Movies() {
        String query = "Match (a:Actor) -[:ACTS_IN]-> (m:Movie) With a, collect(m) as movies where length(movies) > 6 Return a.name";
        return graphDatabase.runCypher(query);
    }

//7. Policzyć średnią wystąpień w filmach dla grupy aktorów, którzy wystąpili conajmniej 7 filmach
    public String ActorsInMoreThan7MoviesAverage() {
        String query = "Match (a:Actor) -[:ACTS_IN]-> (m:Movie) With a,1 as toCount, count(m) as movies where movies > 6 Return sum(movies)/count(toCount)";
        return graphDatabase.runCypher(query);
    }
//    8. Wyświetlić aktorów, którzy zagrali w conajmniej pięciu filmach i wyreżyserowali conajmniej
//    jeden film (z użyciem WITH), posortować ich wg liczby wystąpień w filmach
    public String ActorsDirectors() {
        String query = "Match (a:Actor) -[:ACTS_IN]-> (m:Movie) " +
                "With a, count(m) as movies " +
                "where movies > 4 " +
                "With a,movies " +
                "Match (a:Director) -[:DIRECTED]-> (m:Movie) " +
                "With a,movies,count(m.title) as directed, collect(m.title) as directedTitles " +
                "Where directed > 0 " +
                "Return a.name,movies,directed,directedTitles " +
                "Order By movies DESC ";
        return graphDatabase.runCypher(query);

    }

//    9. Zapytanie o znajomych wybranego usera którzy ocenili film na conajmniej trzy gwiazdki
//       (wyświetlić znajomego, tytuł, liczbę gwiazdek)
    public String FriendsWellRatedMovies(String userName) {
        String query = "Match (u:User{login: '" + userName + "' }) -[:FRIEND]-> (f:User) -[r:RATED]-> (m:Movie) " +
                "where r.stars >= 3 " +
                "Return u.login,f.login,r.stars,m.title ";
        return graphDatabase.runCypher(query);
    }

//    10. Zapytanie o ścieżki między wybranymi aktorami (np.2), w ścieżkach mają nie znajdować się filmy
//    (funkcja filter(), [x IN xs WHERE predicate | extraction])
    public String PathBetweenTwoActors(String actorOne, String actorTwo) {
        //jeszcze odfiltrowac ;p
        String query = "Match p = (a1:Actor{name: '" + actorOne + "' }) " +
                "-[:FRIEND|:DIRECTED|:ACTS_IN|:RATED*1..5 ]- " +
                "(a2:Actor{name: '" + actorTwo + "' }) " +
                "Return p";
        System.out.println(query);
        return graphDatabase.runCypher(query);

    }

//    11. Porównać czas wykonania zapytania o wybranego aktora bez oraz z indeksem w bazie
//    nałożonym na atrybut name (DROP INDEX i CREATE INDEX oraz użyć komendy EXPLAIN lub
//    PROFILE), dokonać porównania dla zapytania shortestPath pomiędzy dwoma wybranymi
//    aktorami.
    public void measureIndexTime() {
        System.out.println("###########################");
        long matchTimeDurationWithoutIndex = measureQueryTime("PROFILE MATCH (a:Actor) WHERE a.name ='Robert De Niro' RETURN a.name");
        System.out.println("Czas bez indeksu: \n " + matchTimeDurationWithoutIndex);
        System.out.println(graphDatabase.runCypher("CREATE INDEX ON :Actor(name) "));
        long matchTimeDurationWithIndex = measureQueryTime("PROFILE MATCH (a:Actor) WHERE a.name ='Robert De Niro' RETURN a.name");
        System.out.println("Czas z indeksem:  " + matchTimeDurationWithIndex);
        System.out.println("Roznica  " + (matchTimeDurationWithoutIndex - matchTimeDurationWithIndex));

        System.out.println("###########################");
        System.out.println(graphDatabase.runCypher("DROP INDEX ON :Actor(name) "));
        long shortestPathWithoutIndex = measureQueryTime("PROFILE MATCH p=shortestPath( (a:Actor {name: 'Emma Watson'})-[*]-(b:Actor {name: 'Daniel Radcliffe'} ))  RETURN p");
        System.out.println(graphDatabase.runCypher("CREATE INDEX ON :Actor(name) "));
        long shortestPathWithIndex = measureQueryTime("PROFILE MATCH p=shortestPath( (a:Actor {name: 'Emma Watson'})-[*]-(b:Actor {name: 'Daniel Radcliffe'} ))  RETURN p");
        System.out.println("Roznica  " + (shortestPathWithoutIndex - shortestPathWithIndex));
        System.out.println(graphDatabase.runCypher("DROP INDEX ON :Actor(name) "));
    }
    private long measureQueryTime(String query) {
        long startTime = System.nanoTime();
        System.out.println(graphDatabase.runCypher(query));
        long endTime = System.nanoTime();
        return (endTime - startTime);
    }
}
