package cc.hongz.ztools.dfa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * StopChar 测试。
 *
 * @author hongz
 */
@DisplayName("StopChar 测试")
public class StopCharTest {

    @Test
    @DisplayName("判断停顿词")
    public void isStopCharTest() {
        assertTrue(StopChar.isStopChar(' '));
        assertTrue(StopChar.isStopChar('。'));
        assertTrue(StopChar.isStopChar('、'));
        assertFalse(StopChar.isStopChar('a'));
        assertFalse(StopChar.isStopChar('黑'));
    }

    @Test
    @DisplayName("判断合法字符")
    public void isNotStopCharTest() {
        assertTrue(StopChar.isNotStopChar('a'));
        assertTrue(StopChar.isNotStopChar('黑'));
        assertFalse(StopChar.isNotStopChar('。'));
        assertFalse(StopChar.isNotStopChar(' '));
    }
}
