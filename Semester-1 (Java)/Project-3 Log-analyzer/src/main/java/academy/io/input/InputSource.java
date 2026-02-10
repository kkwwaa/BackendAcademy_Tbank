package academy.io.input;

import java.io.IOException;
import java.util.stream.Stream;

/** Общий контракт для всех источников логов. Реализации: FileSource, UrlSource. */
public sealed interface InputSource permits FileSource, UrlSource {
    Stream<String> lines() throws IOException;
}
