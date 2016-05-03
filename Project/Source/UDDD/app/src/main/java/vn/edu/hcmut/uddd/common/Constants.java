package vn.edu.hcmut.uddd.common;

/**
 * Created by TRAN VAN HEN on 3/2/2016.
 */
public class Constants {

    public static String PR_WORD = "word";
    public static String PR_MODE = "mode";
    public static String PR_TOPIC_ID = "topic_id";
    public static String PR_SOUND = "sound";
    public static int CREATE_MODE = 1;
    public static int UPDATE_MODE = 2;
    public static int SWITCH_MODE = 3;

    public static String TOPIC_DRAWABLE = "topics_";

    public static String TOAST_VTT_NOT_SUPPORT = "Voice to text hiện không hỗ trợ!";
    public static String TOAST_ADD_WORD_EMPTY = "Hãy nhập từ";
    public static String TOAST_ADD_PRONUNCIATION_EMPTY = "Hãy nhập phát âm cho từ <font color='#FF0000'>%s</font>";
    public static String TOAST_ADD_MEAN_EMPTY = "Hãy nhập ít nhất 1 nghĩa cho từ <font color='#FF0000'>%s</font>";

    /// Dialog
    public static String BUTTON_POSITIVE_OK = "Đồng ý";
    public static String BUTTON_NEGATIVE_CANCEL = "Đóng";
    public static String BUTTON_NEUTRAL_SAVE = "Lưu";
    public static String POPUP_TIT_ALERT = "Thông báo!";

    public static String SCHEDULE_POPUP_TITLE = "Đặt lịch học:";
    public static String CALENDAR_POPUP_TITLE = "Chọn giờ:";
    public static String CONTACT_POPUP_TITTLE = "Thông tin:";
    public static String SEARCH_POPUP_TITLE = "Tra từ:";
    public static String SEARCH_POPUP_MESSAGE = "Nhập từ cần tra.";
    public static String TOAST_SEARCH_INPUT_EMPTY = "Hãy nhập từ cần tra.";
    public static String SEARCH_NOT_EXIST_POPUP_MESSAGE = "Từ <font color='#FF0000'>%s</font> không tồn tại trong cơ sở dữ liệu, bạn có muốn thêm mới?";
    public static String GAME_NOT_EXIST_POPUP_MESSAGE = "Bạn không có từ để kiểm tra, bạn có muốn học từ mới?";
    public static String HISTORY_NOT_EXIST_POPUP_MESSAGE = "Bạn không có từ lịch sử, bạn có muốn học từ mới?";
    public static String MARKED_NOT_EXIST_POPUP_MESSAGE = "Bạn không có từ đánh dấu, bạn có muốn học từ mới?";
    public static String DELETE_WORD_POPUP_MESSAGE = "Từ <font color='#FF0000'>%s</font> sẽ bị xóa khỏi cơ sở dữ liệu, bạn có muốn xóa không?";
    public static String GAME_COMPLETE_POPUP_MESSAGE = "Bạn đã làm đúng %s/%s từ, bạn có muốn tiếp tục kiểm tra không?";
    public static String ADD_CANCEL_MESSAGE = "Từ <font color='#FF0000'>%s</font> chưa được lưu, bạn có muốn rời khỏi?";

    public static String ADD_NOTE_POPUP_TITLE = "Thêm gợi nhớ:";
    public static String EDIT_NOTE_POPUP_TITLE = "Sửa gợi nhớ:";
    public static String ADD_NOTE_POPUP_MESSAGE = "Thêm gợi nhớ mới cho từ <font color='#FF0000'>%s</font>.";
    public static String EDIT_NOTE_POPUP_MESSAGE = "Sửa gợi nhớ cho từ <font color='#FF0000'>%s</font>.";
    public static String TOAST_NOTE_INPUT_EMPTY = "Hãy nhập gợi nhớ cho từ <font color='#FF0000'>%s</font>.";
    public static String NOTE_POPUP_HINT = "Nhập gợi nhớ";

    public static String ADD_MEAN_POPUP_TITLE = "Thêm nghĩa:";
    public static String EDIT_MEAN_POPUP_TITLE = "Sửa nghĩa:";
    public static String ADD_MEAN_POPUP_MESSAGE = "Thêm nghĩa mới cho từ <font color='#FF0000'>%s</font>.";
    public static String EDIT_MEAN_POPUP_MESSAGE = "Sửa nghĩa cho từ <font color='#FF0000'>%s</font>.";
    public static String TOAST_MEAN_MEAN_EMPTY = "Hãy nhập nghĩa cho từ <font color='#FF0000'>%s</font>.";
    public static String TOAST_MEAN_EX_MEAN_EMPTY = "Hãy nhập nghĩa cho ví dụ";

    public static String ADD_FAMILY_POPUP_TITLE = "Thêm từ cùng gốc:";
    public static String EDIT_FAMILY_POPUP_TITLE = "Sửa từ cùng gốc:";
    public static String ADD_FAMILY_POPUP_MESSAGE = "Thêm từ cùng gốc mới cho từ <font color='#FF0000'>%s</font>.";
    public static String EDIT_FAMILY_POPUP_MESSAGE = "Sửa từ cùng gốc cho từ <font color='#FF0000'>%s</font>.";
    public static String TOAST_FAMILY_WORD_EMPTY = "Hãy nhập từ cùng gốc cho từ <font color='#FF0000'>%s</font>.";

    public static String ADD_PHRASE_POPUP_TITLE = "Thêm cụm từ:";
    public static String EDIT_PHRASE_POPUP_TITLE = "Sửa cụm từ:";
    public static String ADD_PHRASE_POPUP_MESSAGE = "Thêm cụm từ mới cho từ <font color='#FF0000'>%s</font>.";
    public static String EDIT_PHRASE_POPUP_MESSAGE = "Sửa cụm từ cho từ <font color='#FF0000'>%s</font>.";
    public static String TOAST_PHRASE_WORD_EMPTY = "Hãy nhập cụm từ cho từ <font color='#FF0000'>%s</font>.";
    public static String TOAST_PHRASE_MEAN_EMPTY = "Hãy nhập nghĩa cho cụm từ.";

    public static String SAVE_WORD_POPUP_MESSAGE = "Bạn có muốn thêm từ <font color='#FF0000'>%s</font> vào cơ sở dữ liệu?";
    public static String SAVE_WORD_ESXIST_POPUP_MESSAGE = "Từ <font color='#FF0000'>%s</font> đã tồn tại trong cơ sở dữ liệu, bạn có muốn cập nhật?";
    public static String UPDATE_WORD_POPUP_MESSAGE = "Bạn có muốn cập nhật từ <font color='#FF0000'>%s</font> vào cơ sở dữ liệu?";
    public static String UPDATE_WORD_NOT_EXIST_POPUP_MESSAGE = "Từ <font color='#FF0000'>%s</font> chưa tồn tại trong cơ sở dữ liệu, bạn có muốn thêm?";

    /// HTML String
    public static String HTML_NEW_LINE = "<br/>";
    public static String HTML_MEAN_WORD_STRING = "<b>%s</b> <i>%s</i>";
    public static String HTML_MEAN_WORD_PRONUNCIATION = "<i>/%s/</i>";
    public static String HTML_MEAN_COMMA_SEPARATOR = "<font color='#439BF2'>, </font>";
    public static String HTML_MEAN_MEAN_HEADER = "<font color='#EA8C33'><b><i>• Nghĩa của từ:</i></b></font><br/>";
    public static String HTML_MEAN_SYNONYMS_HEADER = "<font color='#EA8C33'><b><i>• Từ đồng nghĩa:</i></b></font><br/>\t";
    public static String HTML_MEAN_ANTONYMS_HEADER = "<font color='#EA8C33'><b><i>• Từ trái nghĩa:</i></b></font><br/>\t";
    public static String HTML_MEAN_FAMILY_HEADER = "<font color='#EA8C33'><b><i>• Từ cùng gốc:</i></b></font><br/>\t";
    public static String HTML_MEAN_PHRASE_HEADER = "<font color='#EA8C33'><b><i>• Cụm từ:</i></b></font><br/>";
    public static String HTML_MEAN_MEAN_DETAIL_FULL = "<font color='#439BF2'>∙ %s %s</font><br/><font color='#089308'>\t%s</font><br/><font color='#999999'>\t%s</font><br/>";
    public static String HTML_MEAN_MEAN_DETAIL_NO_EX = "<font color='#439BF2'>∙ %s %s</font><br/>";
    public static String HTML_ADD_MEAN_FULL = "<font color='#439BF2'>∙ %s %s</font><br/><font color='#089308'>\t%s</font><br/><font color='#999999'>\t%s</font>";
    public static String HTML_ADD_MEAN_NO_EX = "<font color='#439BF2'>∙ %s %s</font>";
    public static String HTML_MEAN_SYNONYMS_LINK = "<a href='%s'>%s</a>";
    public static String HTML_MEAN_SYNONYMS_NO_LINK = "<font color='#439BF2'>%s</font>";
    public static String HTML_MEAN_ANTONYMS_LINK = "<a href='%s'>%s</a>";
    public static String HTML_MEAN_ANTONYMS_NO_LINK = "<font color='#439BF2'>%s</font>";
    public static String HTML_MEAN_WORD_FAMILY_LINK = "<a href='%s'>%s %s</a>";
    public static String HTML_MEAN_WORD_FAMILY_NO_LINK = "<font color='#439BF2'>%s %s</font>";
    public static String HTML_ADD_WORD_FAMILY = "∙ <font color='#439BF2'>%s %s</font>";
    public static String HTML_MEAN_PHRASE_DETAIL = "∙ <font color='#439BF2'>\t%s: </font><font color='#999999'>%s</font><br/>";
    public static String HTML_ADD_PHRASE_DETAIL = "∙ <font color='#439BF2'>\t%s: </font><font color='#999999'>%s</font>";
    public static String HTML_GAME_FILL = "%s %s";
    public static String HTML_TOPIC_ITEM = "<font color='#089308'>%s</font><br/><font color='#999999'>%s</font>";

    /// Error code
    public static String E001 = "E001"; //Database error
    public static String E002 = "E002"; // Pakage error
}
