package academy.exception;

public final class ErrorMessages {
    // FILE ошибки
    public static final String INVALID_FILEPATH = "Неверный путь к файлу";
    public static final String FILE_READ_ERROR = "Ошибка чтения файла";
    public static final String FILE_WRITE_ERROR = "Ошибка при записи отчета в файл";
    public static final String FILE_ALREADY_EXISTS = "Файл уже существует, перезапись запрещена";
    public static final String NO_LOG_FILES_FOUND = "Ни один источник логов не был найден";
    public static final String UNSUPPORTED_FILE_EXTENSION = "Файл имеет неподдерживаемое расширение";
    public static final String INPUT_SOURCE_PATH_NULL = "Путь источника не найден";

    // URL ошибки
    public static final String ERROR_READING_LOGS = "Ошибка чтения логов из URL";
    public static final String UNEXPECTED_READ_URL_ERROR = "Неопределенная ошибка при чтении логов из URL";
    public static final String INVALID_URL = "Неверный формат URL";

    // Ошибки с датой
    public static final String INVALID_TIME_FORMAT = "Неверный формат даты (ожидается ISO8601)";
    public static final String INVALID_DATE_RANGE = "--from должен быть раньше --to";

    // Ошибки формата отчёта
    public static final String MISMATCH_REPORT_FORMAT =
            "По пути в аргументе --output указан файл с некорректным расширением";

    // Ошибки парсинга NGINX
    public static final String INVALID_FORMAT_NGINX = "Неверный формат строки лога NGINX";
}
