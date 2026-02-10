package academy.output.formatters;

import academy.config.ExitCode;
import academy.exception.ClassInspectorException;
import academy.exception.ErrorMessages;
import academy.models.ClassInfo;
import academy.output.Formatter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFormatter implements Formatter {

    private final ObjectMapper objectMapper;

    public JsonFormatter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Не включать null-поля
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Не падать на пустых бинах
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public String format(ClassInfo classInfo) {
        try {
            return objectMapper.writeValueAsString(classInfo);
        } catch (Exception exception) {
            throw new ClassInspectorException(ErrorMessages.JSON_SERIALIZE_ERROR, ExitCode.INVALID_USAGE);
        }
    }
}
