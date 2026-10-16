package b4e.ru.mtp.backend.ms.gateway.domain;

import lombok.Getter;

public enum CategoryTypeCode {
    REVENUE("Доход"), // доход
    EXPENSE("Расход") // расход
    ;

    @Getter
    private final String title;

    CategoryTypeCode(String title) {
        this.title = title;
    }
}
