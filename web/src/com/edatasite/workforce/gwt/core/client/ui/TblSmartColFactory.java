//SmartTextCell helper class for rendering text in a cell with automatic CSS class handling

package com.edatasite.workforce.gwt.core.client.ui; // Убедитесь, что этот пакет соответствует вашему проекту
import com.google.gwt.user.cellview.client.Column;

public final class TblSmartColFactory {

    /**
     * Приватный конструктор, чтобы нельзя было создать экземпляр этого класса.
     */
    private TblSmartColFactory() {
    }

    /**
     * Интерфейс для простого получения текстового значения из любого объекта.
     *
     * @param <T> тип объекта строки (например, WfmTreeItem)
     */
    public interface ValueGetter<T> {
        String get(T object);
    }

    /**
     * Создает новую "умную" колонку, которая будет использовать нашу SmartTextCell.
     *
     * @param getter способ получить текстовое значение из объекта строки
     * @param <T>    тип объекта строки
     * @return новую "умную" колонку
     */
    public static <T> Column<T, String> create(final ValueGetter<T> getter) {
        return new Column<T, String>(new TblSmartTxtCell()) {
            @Override
            public String getValue(T object) {
                // Используем getter, чтобы извлечь текст из объекта
                return getter.get(object);
            }
        };
    }
}