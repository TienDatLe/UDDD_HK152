package vn.edu.hcmut.uddd.common;

/**
 * Created by TRAN VAN HEN on 3/2/2016.
 */
public class ConstCommon {

    public static final int MAX_TOPIC = 2;
    public static final int MAX_HISTORY = 100;
    public static final int[] MAX_WORD = new int[]{1,1};
    public static final String[] DICTIONARY_TABLE = new String[]{"technology","medical","economic"};

    /// The label of word
    public static final String NOUNS_FORMAT_LABEL = "a";
    public static final String NOUNS_MEAN_LABEL = "A";
    public static final String PRONOUNS_FORMAT_LABEL = "b";
    public static final String PRONOUNS_MEAN_LABEL = "B";
    public static final String ADJECTIVE_FORMAT_LABEL = "c";
    public static final String ADJECTIVE_MEAN_LABEL = "C";
    public static final String VERB_FORMAT_LABEL = "d";
    public static final String VERB_MEAN_LABEL = "D";
    public static final String ADVERB_FORMAT_LABEL = "e";
    public static final String ADVERB_MEAN_LABEL = "E";
    public static final String PREPOSITION_FORMAT_LABEL = "f";
    public static final String PREPOSITION_MEAN_LABEL = "F";
    public static final String CONJUNCTION_FORMAT_LABEL = "g";
    public static final String CONJUNCTION_MEAN_LABEL = "G";
    public static final String INTERJECTION_FORMAT_LABEL = "h";
    public static final String INTERJECTION_MEAN_LABEL = "H";
    public static final String NOUNS_TYPE_LABEL = "n";
    public static final String NOUNS_STRING = "n";
    public static final String PRONOUNS_TYPE_LABEL = "p";
    public static final String PRONOUNS_STRING = "pro";
    public static final String ADJECTIVE_TYPE_LABEL = "j";
    public static final String ADJECTIVE_STRING = "adj";
    public static final String VERB_TYPE_LABEL = "v";
    public static final String VERB_STRING = "v";
    public static final String ADVERB_TYPE_LABEL = "d";
    public static final String ADVERB_STRING = "adv";
    public static final String PREPOSITION_TYPE_LABEL = "o";
    public static final String PREPOSITION_STRING = "pre";
    public static final String CONJUNCTION_TYPE_LABEL = "c";
    public static final String CONJUNCTION_STRING = "con";
    public static final String INTERJECTION_TYPE_LABEL = "i";
    public static final String INTERJECTION_STRING = "int";

    /// The delimiter
    public static final String SEMICOLON = ";";
    public static final String COLON = ":";
    public static final String EMPTY = "";
    public static final String SPACE = " ";

    /// The assets file
    public static final String DATABASE_NAME = "idic.db";
    public static final String DATABASE_SQL = "databases/idic.sql";
    public static final String DEFAULT_DICTIONARY_TABLE = "words";
    public static final String MAIL_SHARE_TEMPLATE = "mail/share.html";
    public static final String MAIL_ERROR_TEMPLATE = "mail/error.html";



    /// The share preferences
    public static final String SP_FILE_NAME = "sharePreferences";
    public static final String SP_DICTIONARY_NAME = "dictionary";
    public static final String SP_DB_VERSION = "version";
    public static final String SP_MAX_TOPIC_ID = "maxTopic";
    public static final String SP_MAX_HISTORY = "maxHistory";
    public static final String SP_CURRENT_HISTORY = "currentHistory";
    public static final String SP_MAX_WORD_OF_TOPIC = "maxWordOf";
    public static final String SP_CURRENT_WORD_OF_TOPIC = "currentWordOf";
    public static final String SP_DELETED_WORD_OF_TOPIC = "deletedWordOf";
    public static final String SP_CHECK_UPDATE_DATABASE = "checkDatabaseUpdate";
    public static final String SP_CHECK_UPDATE_APPLICATION = "checkApplicationUpdate";

    /// The sql query
    public static final String SQL_GET_WORD_BY_ID = "SELECT word, content, note, picture_url FROM %s WHERE id = ? AND topic_id = ?";
    public static final String SQL_GET_WORD_BY_WORD = "SELECT id, topic_id, content, note, picture_url FROM %s WHERE word = ?";
    public static final String SQL_COUNT_WORD = "SELECT count(id) FROM %s WHERE word = ?";
    public static final String SQL_GET_LIST_TOPIC = "SELECT id, name, content FROM topics WHERE id <> 0";
    public static final String SQL_UPDATE_NOTE_WORD = "UPDATE %s SET note = ? WHERE word = ?";
    public static final String SQL_COUNT_IS_MARK = "SELECT count(word) FROM progressing WHERE type = 1 AND word = ?";
    public static final String SQL_INSERT_IS_MARK = "INSERT INTO progressing (word, type, created_at, point) values (?, 1, date('now'), 0)";
    public static final String SQL_DELETE_IS_MARK = "DELETE FROM progressing WHERE word = ? AND type = 1";
    public static final String SQL_INSERT_HISTORY = "INSERT INTO progressing (word, type, created_at, point) values (?, 2, date('now'), 0)";
    public static final String SQL_DELETE_HISTORY = "DELETE FROM progressing WHERE word = ? AND type = 2";
    public static final String SQL_UPDATE_POINT = "UPDATE progressing SET point = ? WHERE word = ? AND type = 2";
    public static final String SQL_GET_MAX_POINT_HISTORY = "SELECT word FROM progressing WHERE type = 2 ORDER BY point DESC LIMIT 1";
    public static final String SQL_INSERT_WORD = "INSERT INTO %s (id, topic_id, word, content, note, is_edited, picture_url) values (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE_WORD = "UPDATE %s SET content = ?, note = ?, is_edited = 1, picture_url = ? WHERE word = ?";
    public static final String SQL_GET_IS_EDITED = "SELECT is_edited FROM %s WHERE word = ?";
    public static final String SQL_GET_LIST_HISTORY = "SELECT word FROM progressing WHERE type = 2";
    public static final String SQL_GET_LIST_MARK = "SELECT word FROM progressing WHERE type = 1";
    public static final String SQL_GET_LIST_WORD_FOR_GAME = "SELECT w.topic_id, w.word, w.content, w.picture_url, p.point FROM %s AS w INNER JOIN progressing AS p ON (p.word = w.word) WHERE p.type = 2 ORDER BY RANDOM() LIMIT ?";
    public static final String SQL_DELETE_WORD = "DELETE FROM %s WHERE word = ?";
    public static final String SQL_GET_LIST_DICTIONARY = "SELECT table_name, name, is_offline FROM  dictionary";
    public static final String SQL_DELETE_ALL_HISTORY_MARK = "DELETE FROM progressing";
    public static final String SQL_GET_TOPIC_NAME_BY_ID = "SELECT name FROM topics WHERE id = ?";
    public static final String SQL_GET_TOPIC_ID_BY_NAME = "SELECT id FROM topics WHERE name = ?";
    public static final String SQL_GET_LIST_DOWNLOAD = "SELECT _rowid_, word, content, table_name, topic_name, picture_url FROM downloaded";
    public static final String SQL_GET_LIST_UPLOAD_PENDING = "SELECT _rowid_, word, content, table_name, topic_name, picture_url FROM upload_pending";
    public static final String SQL_DELETE_DOWNLOAD = "DELETE FROM downloaded WHERE _rowid_ = ?";
    public static final String SQL_DELETE_UPLOAD_PENDING = "DELETE FROM upload_pending WHERE _rowid_ = ?";
    public static final String SQL_INSERT_DOWNLOAD = "INSERT INTO downloaded (word, content, table_name, topic_name, picture_url) VALUES (?, ?, ?, ?, ?)";
    public static final String SQL_INSERT_UPLOAD_PENDING = "INSERT INTO upload_pending (word, content, table_name, topic_name, picture_url) VALUES (?, ?, ?, ?, ?)";

    public static final String EXCEPTION_NOT_EXIST = "Đối tượng không tồn tại trong database!";
}
