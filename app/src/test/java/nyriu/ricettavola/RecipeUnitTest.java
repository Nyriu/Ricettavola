package nyriu.ricettavola;

import org.junit.Test;

import nyriu.ricettavola.models.Recipe;

import static org.junit.Assert.assertEquals;

/**
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RecipeUnitTest {
    @Test
    public void recipe_toString() {
        //assertEquals(4, 2 + 2);
        Recipe r = new Recipe();
        System.out.println( r.toString());
    }
}