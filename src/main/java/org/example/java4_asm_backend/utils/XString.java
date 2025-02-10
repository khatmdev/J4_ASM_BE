package org.example.java4_asm_backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XString {
    public static String highlightKeyword(String title, String keyword) {
        // Kiểm tra nếu keyword trống để tránh lỗi
        if (keyword == null || keyword.isEmpty()) {
            return title;
        }

        // Chuẩn bị từ khóa highlight
        StringBuilder highlightedTitle = new StringBuilder();

        // Tạo pattern cho keyword (không phân biệt hoa/thường)
        Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(title);

        int lastEnd = 0;

        // Tìm và highlight các phần trùng khớp với keyword
        while (matcher.find()) {
            // Thêm phần trước khi từ khóa bắt đầu
            highlightedTitle.append(title, lastEnd, matcher.start());

            // Thêm từ khóa đã được highlight
            highlightedTitle.append("<span class='highlight'>")
                    .append(matcher.group())
                    .append("</span>");

            // Cập nhật vị trí kết thúc cho lần tìm tiếp theo
            lastEnd = matcher.end();
        }

        // Thêm phần còn lại của title sau từ khóa cuối cùng (nếu có)
        highlightedTitle.append(title.substring(lastEnd));

        return highlightedTitle.toString();
    }
}
