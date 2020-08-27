package org.chang.springboot.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * In JPA 2.1 java.time API is not supported
 * this converter is needed to convert
 * Instant to db column timestamps
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
    return locDateTime == null ? null : Timestamp.valueOf(locDateTime);
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
    return sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime();
  }
}
