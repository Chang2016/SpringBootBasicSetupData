package org.chang.springboot.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class LocalDateTimeAttributeConverterTest {

  @Test
  void convertToDatabaseColumn() {
    // given
    LocalDateTime now = LocalDateTime.now();
    LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();
    // when
    Timestamp timestamp = converter.convertToDatabaseColumn(now);
    // then
    assertThat(timestamp.getTime()).isEqualTo(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
  }

  @Test
  void convertNullToDatabaseColumn() {
    // given
    LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();
    // when
    Timestamp timestamp = converter.convertToDatabaseColumn(null);
    // then
    assertThat(timestamp).isNull();
  }

  @Test
  void convertToEntityAttribute() {
    // given
    Timestamp timestamp = Timestamp.from(Instant.now());
    LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();
    // when
    LocalDateTime localDateTime = converter.convertToEntityAttribute(timestamp);
    // then
    assertThat(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).isEqualTo(timestamp.getTime());
  }

  @Test
  void convertNullToEntityAttribute() {
    // given
    LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();
    // when
    LocalDateTime localDateTime = converter.convertToEntityAttribute(null);
    // then
    assertThat(localDateTime).isNull();
  }
}
