package my.spring.framework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AZView {

    public final String DEFULAT_CONTENT_TYPE = "text/html;charset=utf-8";

    // 对应的页面
    File viewFile;
    public AZView(File viewFile) {
        this.viewFile = viewFile;
    }

    // 根据 viewFile 和 model 渲染页面
    public void render(Map<String, ?> model,
                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        RandomAccessFile file = new RandomAccessFile(viewFile, "r");
        StringBuffer sb = new StringBuffer();

        String line;
        while ((line = file.readLine()) != null) {
            // 转为utf-8
            line = new String(line.getBytes("ISO-8859-1"), "utf-8");

            // 匹配表达式
            Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            // 匹配到，则替换
            while (matcher.find()) {
                String paramName = matcher.group();
                paramName = paramName.replaceAll("￥\\{|\\}", "");
                // 从model获得值
                Object value = model.get(paramName);
                if (null == value) { continue; }
                // 将值替换进去(当前最前一个)
                line = matcher.replaceFirst(makeStringForRegExp(value.toString()));
                // 继续匹配
                matcher = pattern.matcher(line);
            }
            sb.append(line);
        }
        // 输出
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());

    }

    //处理特殊字符
    public static String makeStringForRegExp(String str) {
        return str.replace("\\", "\\\\").replace("*", "\\*")
                .replace("+", "\\+").replace("|", "\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }

}
