package cc.hongz.ztools.dfa;

import org.junit.jupiter.api.DisplayName;

import cc.hongz.ztools.core.collection.ListUtil;
import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("SensitiveUtil 测试")
public class SensitiveUtilTest {

    @Test
    @DisplayName("敏感词过滤")
    public void testSensitiveFilter() {
        List<String> wordList = new ArrayList<>();
        wordList.add("大");
        wordList.add("大土豆");
        wordList.add("土豆");
        wordList.add("刚出锅");
        wordList.add("出锅");
        TestBean bean = new TestBean();
        bean.setStr("我有一颗$大土^豆，刚出锅的");
        bean.setNum(100);
        SensitiveUtil.init(wordList);
        bean = SensitiveUtil.sensitiveFilter(bean, true, null);
        assertEquals(bean.getStr(), "我有一颗$****，***的");
    }

    @Data
    public static class TestBean {
        private String str;
        private Integer num;
    }

    @Test
    @DisplayName("issue2126")
    public void issue2126(){
        SensitiveUtil.init(ListUtil.of("赵", "赵阿", "赵阿三"));

        String result = SensitiveUtil.sensitiveFilter("赵阿三在做什么。", true, null);
        assertEquals("***在做什么。", result);
    }

    @Test
    @DisplayName("issue4182")
    void issue4182Test(){
        extracted();
        final String s = SensitiveUtil.sensitiveFilter("creator_user_id=2000907612345839744");
        assertEquals("creator_user_id=2000907612345839744", s);
    }

    private static void extracted() {
        SensitiveUtil.init(ListUtil.of("12宝宝龙", "34皮卡丘"));
    }

    @Test
    @DisplayName("是否已初始化")
    public void isInitedTest() {
        SensitiveUtil.init(ListUtil.of("黑", "红"));
        assertTrue(SensitiveUtil.isInited());
    }

    @Test
    @DisplayName("是否包含敏感词")
    public void containsSensitiveTest() {
        SensitiveUtil.init(ListUtil.of("黑", "红"));
        assertTrue(SensitiveUtil.containsSensitive("这件黑大衣"));
        assertFalse(SensitiveUtil.containsSensitive("这件大衣"));
    }

    @Test
    @DisplayName("查找第一个敏感词")
    public void getFoundFirstSensitiveTest() {
        SensitiveUtil.init(ListUtil.of("黑", "红"));
        final FoundWord foundWord = SensitiveUtil.getFoundFirstSensitive("这件黑大衣");
        assertEquals("黑", foundWord.getWord());
    }

    @Test
    @DisplayName("查找所有敏感词")
    public void getFoundAllSensitiveTest() {
        SensitiveUtil.init(ListUtil.of("黑", "红"));
        final List<FoundWord> foundWords = SensitiveUtil.getFoundAllSensitive("黑红");
        assertEquals(2, foundWords.size());
        assertEquals("黑", foundWords.get(0).getWord());
        assertEquals("红", foundWords.get(1).getWord());
    }

}
