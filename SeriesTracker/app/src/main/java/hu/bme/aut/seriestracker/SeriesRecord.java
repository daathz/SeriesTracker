package hu.bme.aut.seriestracker;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Viktor on 2016. 12. 14..
 */

public class SeriesRecord extends SugarRecord implements Serializable {

    public enum State {
        WATCHING, COMPLETED, ONHOLD, PLANTOWATCH;

        public static State getByOrdinal(int ordinal) {
            State res = null;
            for (State state : State.values()) {
                if (state.ordinal() == ordinal) {
                    res = state;
                    break;
                }
            }
            return res;
        }
    }

    public String title;
    public State state;
    public int season;
    public int episode;
    public int score;
    public boolean isWatching;
}
