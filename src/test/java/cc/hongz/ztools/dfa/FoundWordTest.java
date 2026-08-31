package cc.hongz.ztools.dfa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * FoundWord 测试。
 *
 * @author hongz
 */
@DisplayName("FoundWord 测试")
public class FoundWordTest {

    @Test
    @DisplayName("获取单词与匹配内容")
    public void getWordAndFoundWordTest() {
        final FoundWord foundWord = new FoundWord("黑", "黑色", 0, 1);
        assertEquals("黑", foundWord.getWord());
        assertEquals("黑色", foundWord.getFoundWord());
    }

    @Test
    @DisplayName("toString 返回匹配内容")
    public void toStringTest() {
        final FoundWord foundWord = new FoundWord("黑", "黑色", 0, 1);
        assertEquals("黑色", foundWord.toString());
    }

    @Test
    @DisplayName("获取起止下标")
    public void getStartAndEndIndexTest() {
        final FoundWord foundWord = new FoundWord("黑", "黑色", 2, 3);
        assertEquals(2, foundWord.getStartIndex().intValue());
        assertEquals(3, foundWord.getEndIndex().intValue());
    }
}
