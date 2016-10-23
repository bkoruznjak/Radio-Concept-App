package bkoruznjak.from.hr.antenazagreb.enums;

import java.io.Serializable;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public enum RadioStateEnum implements Serializable {
    BUFFERING,
    CRASH,
    ENDED,
    ERROR,
    IDLE,
    PREPARING,
    READY,
    UNKNOWN
}
