import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private final List<String> teams;
    private final int[] winGames;
    private final int[] loseGames;
    private final int[] remainingGames;
    private final int[][] againstGames;
    private Map<String, Iterable<String>> certifiedOfEliminated = new HashMap<String, Iterable<String>>();

    public BaseballElimination(String filename) {
        In in = new In(filename);
        final int numberOfTeams = in.readInt();
        teams = new ArrayList<String>(numberOfTeams);
        winGames = new int[numberOfTeams];
        loseGames = new int[numberOfTeams];
        remainingGames = new int[numberOfTeams];
        againstGames = new int[numberOfTeams][numberOfTeams];
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(i, in.readString());
            winGames[i] = in.readInt();
            loseGames[i] = in.readInt();
            remainingGames[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                againstGames[i][j] = in.readInt();
            }
        }
        for (int i = 0; i < numberOfTeams; i++) {
            int possibleWins = winGames[i] + remainingGames[i];
            List<String> eliminatedBy = new ArrayList<String>();
            for (int j = 0; j < numberOfTeams; j++) {
                if (possibleWins < winGames[j]) {
                    eliminatedBy.add(teams.get(j));

                }
            }
            if (!eliminatedBy.isEmpty()) {
                certifiedOfEliminated.put(teams.get(i), eliminatedBy);
            }
        }
        int flowNetworkSize = numberOfTeams + 1 + calculateNumberOfGames(numberOfTeams);
        for (int i = 0; i < numberOfTeams; i++) {
            final String eliminatedTeam = teams.get(i);
            if (!certifiedOfEliminated.containsKey(eliminatedTeam)) {
                FlowNetwork teamFlowNetwork = new FlowNetwork(flowNetworkSize);
                int nodeIndex = 1;
                int teamIndexInFlow = flowNetworkSize - numberOfTeams;
                int expectedTeamFlow = 0;
                int firstCommandIncrement = 0;
                for (int j = 0; j < numberOfTeams; j++) {
                    if (j != i) {
                        int secondCommandIncrement = firstCommandIncrement + 1;
                        for (int k = j + 1; k < numberOfTeams; k++) {
                            if (k != i) {
                                expectedTeamFlow += againstGames[j][k];
                                teamFlowNetwork.addEdge(new FlowEdge(0, nodeIndex, againstGames[j][k]));
                                teamFlowNetwork.addEdge(new FlowEdge(nodeIndex, teamIndexInFlow + firstCommandIncrement, Integer.MAX_VALUE));
                                teamFlowNetwork.addEdge(new FlowEdge(nodeIndex, teamIndexInFlow + secondCommandIncrement, Integer.MAX_VALUE));
                                nodeIndex++;
                                secondCommandIncrement++;
                            }
                        }
                        firstCommandIncrement++;
                    }
                }
                final int capacity = winGames[i] + remainingGames[i];
                int teamIndex = 0;
                firstCommandIncrement = 0;
                for (int j = 0; j < numberOfTeams; j++) {
                    if (teamIndex != i) {
                        teamFlowNetwork.addEdge(new FlowEdge(teamIndexInFlow + firstCommandIncrement, flowNetworkSize - 1, capacity - winGames[teamIndex]));
                        firstCommandIncrement++;
                    }
                    teamIndex++;
                }
                FordFulkerson fordFulkerson = new FordFulkerson(teamFlowNetwork, 0, flowNetworkSize - 1);
                if (fordFulkerson.value() < expectedTeamFlow) {
                    teamIndex = 0;
                    firstCommandIncrement = 0;
                    List<String> eliminatedByList = new ArrayList<String>();
                    for (int j = 0; j < numberOfTeams; j++) {
                        if (teamIndex != i) {
                            if (fordFulkerson.inCut(teamIndexInFlow + firstCommandIncrement)) {
                                eliminatedByList.add(teams.get(teamIndex));
                            }
                            firstCommandIncrement++;
                        }
                        teamIndex++;
                    }
                    certifiedOfEliminated.put(eliminatedTeam, eliminatedByList);
                }
            }
        }
    }                  // create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return teams.size();
    }                     // number of teams

    public Iterable<String> teams() {
        return teams;
    }                             // all teams

    public int wins(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Team is not exist: " + team);
        }
        return winGames[teams.indexOf(team)];
    }                   // number of wins for given team

    public int losses(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Team is not exist: " + team);
        }
        return loseGames[teams.indexOf(team)];
    }                  // number of losses for given team

    public int remaining(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Team is not exist: " + team);
        }
        return remainingGames[teams.indexOf(team)];
    }              // number of remaining games for given team

    public int against(String team1, String team2) {
        if (!teams.contains(team1)) {
            throw new IllegalArgumentException("Team is not exist: " + team1);
        }
        if (!teams.contains(team2)) {
            throw new IllegalArgumentException("Team is not exist: " + team2);
        }
        if (team1.equals(team2)) {
            return 0;
        }
        return againstGames[teams.indexOf(team1)][teams.indexOf(team2)];
    }   // number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Team is not exist: " + team);
        }
        return certifiedOfEliminated.containsKey(team);
    }          // is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("Team is not exist: " + team);
        }
        return certifiedOfEliminated.get(team);
    } // subset R of teams that eliminates given team; null if not eliminated

    private int calculateNumberOfGames(int numberOfTeams) {
        int sum = 0;
        for (int i = 1; i < numberOfTeams - 1; i++) {
            sum += i;
        }
        return sum;
    }
}
