package com.natura.web.server.persistence.database;

import com.natura.web.server.model.Comment;
import com.natura.web.server.model.Identification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.natura.web.server.mapper.CommentMapper;
import com.natura.web.server.mapper.IdentificationMapper;
import com.natura.web.server.persistence.database.entity.CommentEntity;
import com.natura.web.server.persistence.database.entity.IdentificationEntity;
import com.natura.web.server.persistence.database.repository.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Database comment provider should")
@ExtendWith(MockitoExtension.class)
class DatabaseCommentProviderTest {

    @InjectMocks
    DatabaseCommentProvider provider;

    @Mock
    CommentMapper mapper;

    @Mock
    IdentificationMapper identificationMapper;

    @Mock
    CommentRepository repository;

    @Test
    @DisplayName("save comment.")
    void save() {
        // Given
        CommentEntity entity = new CommentEntity();
        Comment comment = new Comment();
        Comment saved = new Comment();
        when(mapper.map(comment)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(saved);

        // When
        Comment result = provider.save(comment);

        // Then
        verify(repository).save(entity);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("return comments related to given identification.")
    void getCommentsByIdentification() {
        // Given
        List<CommentEntity> entities = List.of(new CommentEntity());
        List<Comment> comments = List.of(new Comment());
        Identification identification = new Identification();
        IdentificationEntity identificationEntity = new IdentificationEntity();
        when(identificationMapper.map(identification)).thenReturn(identificationEntity);
        when(repository.findByIdentification(identificationEntity)).thenReturn(entities);
        when(mapper.map(entities)).thenReturn(comments);

        // When
        List<Comment> result = provider.getCommentsByIdentification(identification);

        // Then
        assertThat(result).isEqualTo(comments);
    }
}