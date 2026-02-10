package academy.utils.exception;

public final class ErrorMessages {
    // FILE ошибки
    public static final String INVALID_FILEPATH = "Неверный путь к файлу";
    public static final String FILE_WRITE_ERROR = "Ошибка при записи изображения";
    public static final String FILE_ALREADY_EXISTS = "Файл уже существует, перезапись запрещена";
    public static final String UNSUPPORTED_FILE_EXTENSION = "Файл имеет неподдерживаемое расширение (ожидается .png)";
    public static final String CREATE_FORM_JSON_ERROR = "Ошибка при записи изображения";

    // Параметры изображения
    public static final String INVALID_IMAGE_SIZE = "Ширина и высота изображения должны быть положительными";
    public static final String INVALID_ITERATION_COUNT = "Количество итераций должно быть положительным";
    public static final String INVALID_THREADS = "Количество потоков должно быть >= 1";
    public static final String INVALID_IMAGE = "Изображение отсутствует";

    // Аффинные преобразования
    public static final String INVALID_AFFINE_PARAMS = "Неверный формат аффинных преобразований";

    // Функции трансформации
    public static final String INVALID_FUNCTIONS_FORMAT = "Неверный формат функций трансформации";
    public static final String UNSUPPORTED_FUNCTION = "Указана неподдерживаемая функция трансформации";

    // Симметрия
    public static final String INVALID_SYMMETRY_LEVEL = "Уровень симметрии должен быть >= 0";
}
