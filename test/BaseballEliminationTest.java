import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class BaseballEliminationTest {

    @Test
    public void test1TeamFile() {
        final String testTeam = "Turing";
        BaseballElimination baseballElimination = new BaseballElimination("teams1.txt");
        assertEquals("Wrong number of teams", 1, baseballElimination.numberOfTeams());
        assertEquals("Wrong number of wins", 100, baseballElimination.wins(testTeam));
        assertEquals("Wrong number of loses", 55, baseballElimination.losses(testTeam));
        assertEquals("Wrong number of remaining games", 0, baseballElimination.remaining(testTeam));
        assertEquals("Wrong against number of the same team", 0, baseballElimination.against(testTeam, testTeam));
        assertFalse("Wrong value of eliminated", baseballElimination.isEliminated(testTeam));
        Collection<String> expectedTeams = new ArrayList<String>(1);
        expectedTeams.add(testTeam);
        assertEquals("Wrong teams", expectedTeams, baseballElimination.teams());
        assertNull("Wrong certificated of elimination teams", baseballElimination.certificateOfElimination(testTeam));
    }

    @Test
    public void test4TeamFile() {
        final String testTeam = "Montreal";
        BaseballElimination baseballElimination = new BaseballElimination("teams4.txt");
        assertEquals("Wrong number of teams", 4, baseballElimination.numberOfTeams());
        assertEquals("Wrong number of wins", 77, baseballElimination.wins(testTeam));
        assertEquals("Wrong number of loses", 82, baseballElimination.losses(testTeam));
        assertEquals("Wrong number of remaining games", 3, baseballElimination.remaining(testTeam));
        assertEquals("Wrong against number of the same team", 0, baseballElimination.against(testTeam, testTeam));
        assertEquals("Wrong against number", 1, baseballElimination.against("Atlanta", testTeam));
        Collection<String> expectedTeams = new ArrayList<String>(4);
        expectedTeams.add("Atlanta");
        expectedTeams.add("Philadelphia");
        expectedTeams.add("New_York");
        expectedTeams.add(testTeam);
        assertEquals("Wrong teams", expectedTeams, baseballElimination.teams());

        //assert Montreal
        assertTrue("Wrong value of eliminated", baseballElimination.isEliminated(testTeam));
        Collection<String> eliminatedByExpected = new ArrayList<String>(1);
        eliminatedByExpected.add("Atlanta");
        assertEquals("Wrong certificated of elimination teams", eliminatedByExpected, baseballElimination.certificateOfElimination(testTeam));

        //assert Philadelphia
        assertTrue("Wrong value of eliminated", baseballElimination.isEliminated("Philadelphia"));
        eliminatedByExpected = new ArrayList<String>(2);
        eliminatedByExpected.add("Atlanta");
        eliminatedByExpected.add("New_York");
        assertEquals("Wrong certificated of elimination teams", eliminatedByExpected, baseballElimination.certificateOfElimination("Philadelphia"));

        //assert New_York
        assertFalse("Wrong value of eliminated", baseballElimination.isEliminated("New_York"));
        assertNull("Wrong certificated of elimination teams", baseballElimination.certificateOfElimination("New_York"));

        //assert Atlanta
        assertFalse("Wrong value of eliminated", baseballElimination.isEliminated("Atlanta"));
        assertNull("Wrong certificated of elimination teams", baseballElimination.certificateOfElimination("Atlanta"));
    }

    @Test
    public void test4aTeamFile() {
        final String testTeam = "Ghaddafi";
        Collection<String> eliminatedByExpected = new ArrayList<String>(1);
        BaseballElimination baseballElimination = new BaseballElimination("teams4a.txt");
        assertEquals("Wrong number of teams", 4, baseballElimination.numberOfTeams());
        assertEquals("Wrong number of wins", 2, baseballElimination.wins(testTeam));
        assertEquals("Wrong number of loses", 5, baseballElimination.losses(testTeam));
        assertEquals("Wrong number of remaining games", 2, baseballElimination.remaining(testTeam));
        assertEquals("Wrong against number of the same team", 0, baseballElimination.against(testTeam, testTeam));
        assertEquals("Wrong against number", 1, baseballElimination.against("CIA", testTeam));
        Collection<String> expectedTeams = new ArrayList<String>(4);
        expectedTeams.add("CIA");
        expectedTeams.add(testTeam);
        expectedTeams.add("Bin_Ladin");
        expectedTeams.add("Obama");
        assertEquals("Wrong teams", expectedTeams, baseballElimination.teams());

        //assert Ghaddafi
        assertFalse("Wrong value of eliminated", baseballElimination.isEliminated(testTeam));
        assertNull("Wrong certificated of elimination teams", baseballElimination.certificateOfElimination(testTeam));

        //assert Bin_Ladin
        assertTrue("Wrong value of eliminated", baseballElimination.isEliminated("Bin_Ladin"));
        eliminatedByExpected = new ArrayList<String>(1);
        eliminatedByExpected.add("Obama");
        assertEquals("Wrong certificated of elimination teams", eliminatedByExpected, baseballElimination.certificateOfElimination("Bin_Ladin"));

        //assert Obama
        assertFalse("Wrong value of eliminated", baseballElimination.isEliminated("Obama"));
        assertNull("Wrong certificated of elimination teams", baseballElimination.certificateOfElimination("Obama"));

        //assert CIA
        assertFalse("Wrong value of eliminated", baseballElimination.isEliminated("CIA"));
        assertNull("Wrong certificated of elimination teams", baseballElimination.certificateOfElimination("CIA"));
    }
}
