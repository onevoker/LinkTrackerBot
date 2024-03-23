package edu.java.scrapper.domain.repositories.jpa.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "link")
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private URI url;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @Column(name = "last_api_check", nullable = false)
    private OffsetDateTime lastApiCheck;

    @Converter(autoApply = true) static
    class URIToStringConverter implements AttributeConverter<URI, String> {

        @Override
        public String convertToDatabaseColumn(URI attribute) {
            return attribute.toString();
        }

        @Override
        public URI convertToEntityAttribute(String dbData) {
            return URI.create(dbData);
        }
    }
}
