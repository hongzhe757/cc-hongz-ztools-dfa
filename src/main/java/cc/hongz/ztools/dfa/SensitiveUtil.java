package cc.hongz.ztools.dfa;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.thread.ThreadUtil;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.json.JSONUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词工具类。
 *
 * @author Looly
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class SensitiveUtil {

    /**
     * 默认分隔符。
     */
    public static final char DEFAULT_SEPARATOR = StrUtil.C_COMMA;
    private static final WordTree sensitiveTree = new WordTree();

    /**
     * 是否已经被初始化。
     *
     * @return 是否已经被初始化
     */
    public static boolean isInited() {
        return false == sensitiveTree.isEmpty();
    }

    /**
     * 初始化敏感词树。
     *
     * @param isAsync        是否异步初始化
     * @param sensitiveWords 敏感词列表
     */
    public static void init(final Collection<String> sensitiveWords, final boolean isAsync) {
        if (isAsync) {
            ThreadUtil.execAsync(() -> {
                init(sensitiveWords);
                return true;
            });
        } else {
            init(sensitiveWords);
        }
    }

    /**
     * 初始化敏感词树。
     *
     * @param sensitiveWords 敏感词列表
     */
    public static void init(final Collection<String> sensitiveWords) {
        sensitiveTree.clear();
        sensitiveTree.addWords(sensitiveWords);
//        log.debug("Sensitive init finished, sensitives: {}", sensitiveWords);
    }

    /**
     * 初始化敏感词树。
     *
     * @param sensitiveWords 敏感词列表组成的字符串
     * @param isAsync        是否异步初始化
     * @param separator      分隔符
     */
    public static void init(final String sensitiveWords, final char separator, final boolean isAsync) {
        if (StrUtil.isNotBlank(sensitiveWords)) {
            init(StrUtil.split(sensitiveWords, separator), isAsync);
        }
    }

    /**
     * 初始化敏感词树，使用逗号分隔每个单词。
     *
     * @param sensitiveWords 敏感词列表组成的字符串
     * @param isAsync        是否异步初始化
     */
    public static void init(final String sensitiveWords, final boolean isAsync) {
        init(sensitiveWords, DEFAULT_SEPARATOR, isAsync);
    }

    /**
     * 设置字符过滤规则，通过定义字符串过滤规则，过滤不需要的字符。 <br>
     * 当accept为false时，此字符不参与匹配
     *
     * @param charFilter 过滤函数
     * @since 5.4.4
     */
    public static void setCharFilter(final Filter<Character> charFilter) {
        if (charFilter != null) {
            sensitiveTree.setCharFilter(charFilter);
        }
    }

    /**
     * 是否包含敏感词。
     *
     * @param text 文本
     * @return 是否包含
     */
    public static boolean containsSensitive(final String text) {
        return sensitiveTree.isMatch(text);
    }

    /**
     * 是否包含敏感词。
     *
     * @param obj bean，会被转为JSON字符串
     * @return 是否包含
     */
    public static boolean containsSensitive(final Object obj) {
        return sensitiveTree.isMatch(JSONUtil.toJsonStr(obj));
    }

    /**
     * 查找敏感词，返回找到的第一个敏感词。
     *
     * @param text 文本
     * @return 敏感词
     * @since 5.5.3
     */
    public static FoundWord getFoundFirstSensitive(final String text) {
        return sensitiveTree.matchWord(text);
    }

    /**
     * 查找敏感词，返回找到的第一个敏感词。
     *
     * @param obj bean，会被转为JSON字符串
     * @return 敏感词
     */
    public static FoundWord getFoundFirstSensitive(final Object obj) {
        return sensitiveTree.matchWord(JSONUtil.toJsonStr(obj));
    }

    /**
     * 查找敏感词，返回找到的所有敏感词。
     *
     * @param text 文本
     * @return 敏感词
     * @since 5.5.3
     */
    public static List<FoundWord> getFoundAllSensitive(final String text) {
        return sensitiveTree.matchAllWords(text);
    }

    /**
     * 查找敏感词，返回找到的所有敏感词。 <br>
     * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
     * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     *
     * @param text           文本
     * @param isDensityMatch 是否使用密集匹配原则
     * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
     * @return 敏感词
     */
    public static List<FoundWord> getFoundAllSensitive(final String text, final boolean isDensityMatch,
            final boolean isGreedMatch) {
        return sensitiveTree.matchAllWords(text, -1, isDensityMatch, isGreedMatch);
    }

    /**
     * 查找敏感词，返回找到的所有敏感词。
     *
     * @param bean 对象，会被转为JSON
     * @return 敏感词
     * @since 5.5.3
     */
    public static List<FoundWord> getFoundAllSensitive(final Object bean) {
        return sensitiveTree.matchAllWords(JSONUtil.toJsonStr(bean));
    }

    /**
     * 查找敏感词，返回找到的所有敏感词。 <br>
     * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
     * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     *
     * @param bean           对象，会被转为JSON
     * @param isDensityMatch 是否使用密集匹配原则
     * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
     * @return 敏感词
     * @since 5.5.3
     */
    public static List<FoundWord> getFoundAllSensitive(final Object bean, final boolean isDensityMatch,
            final boolean isGreedMatch) {
        return getFoundAllSensitive(JSONUtil.toJsonStr(bean), isDensityMatch, isGreedMatch);
    }

    /**
     * 敏感词过滤。
     *
     * @param bean               对象，会被转为JSON
     * @param isGreedMatch       贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     * @param sensitiveProcessor 敏感词处理器，默认按匹配内容的字符数替换成*
     * @param <T>                bean的class类型
     * @return 敏感词过滤处理后的bean对象
     */
    public static <T> T sensitiveFilter(final T bean, final boolean isGreedMatch,
            final SensitiveProcessor sensitiveProcessor) {
        final String jsonText = JSONUtil.toJsonStr(bean);
        @SuppressWarnings("unchecked") final Class<T> c = (Class<T>) bean.getClass();
        return JSONUtil.toBean(sensitiveFilter(jsonText, isGreedMatch, sensitiveProcessor), c);
    }

    /**
     * 处理过滤文本中的敏感词，默认替换成*。
     *
     * @param text 文本
     * @return 敏感词过滤处理后的文本
     * @since 5.7.21
     */
    public static String sensitiveFilter(final String text) {
        return sensitiveFilter(text, true, null);
    }

    /**
     * 处理过滤文本中的敏感词，默认替换成*。
     *
     * @param text               文本
     * @param isGreedMatch       贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     * @param sensitiveProcessor 敏感词处理器，默认按匹配内容的字符数替换成*
     * @return 敏感词过滤处理后的文本
     */
    public static String sensitiveFilter(final String text, final boolean isGreedMatch,
            final SensitiveProcessor sensitiveProcessor) {
        if (StrUtil.isEmpty(text)) {
            return text;
        }

        //敏感词过滤场景下，不需要密集匹配
        final List<FoundWord> foundWordList = getFoundAllSensitive(text, true, isGreedMatch);
        if (CollUtil.isEmpty(foundWordList)) {
            return text;
        }
        final SensitiveProcessor resolvedSensitiveProcessor = sensitiveProcessor == null ? new SensitiveProcessor() {
        } : sensitiveProcessor;

        final Map<Integer, FoundWord> foundWordMap = new HashMap<>(foundWordList.size(), 1);
        foundWordList.forEach(foundWord -> foundWordMap.put(foundWord.getStartIndex(), foundWord));
        final int length = text.length();
        final StringBuilder textStringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final FoundWord fw = foundWordMap.get(i);
            if (fw != null) {
                textStringBuilder.append(resolvedSensitiveProcessor.process(fw));
                i = fw.getEndIndex();
            } else {
                textStringBuilder.append(text.charAt(i));
            }
        }
        return textStringBuilder.toString();
    }
}
