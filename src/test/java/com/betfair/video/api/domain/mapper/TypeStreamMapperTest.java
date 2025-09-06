package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;
import com.betfair.video.api.domain.entity.TypeStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("TypeStreamMapper Tests")
class TypeStreamMapperTest {

    @InjectMocks
    TypeStreamMapper typeStreamMapper;

    @Test
    @DisplayName("Should convert ContentTypeDto to TypeStream ID")
    void shouldConvertContentTypeToStreamTypeId() {
        // Given
        ContentTypeDto vid = ContentTypeDto.VID;
        ContentTypeDto viz = ContentTypeDto.VIZ;
        ContentTypeDto preViz = ContentTypeDto.PRE_VID;
        ContentTypeDto unrecognized = ContentTypeDto.UNRECOGNIZED_VALUE;

        // When
        Integer vidStreamType = typeStreamMapper.convertContentTypeToStreamTypeId(vid);
        Integer vizStreamType = typeStreamMapper.convertContentTypeToStreamTypeId(viz);
        Integer preVizStreamType = typeStreamMapper.convertContentTypeToStreamTypeId(preViz);
        Integer unrecognizedStreamType = typeStreamMapper.convertContentTypeToStreamTypeId(unrecognized);

        // Then
        assertThat(vidStreamType).isEqualTo(TypeStream.VID.getId());
        assertThat(vizStreamType).isEqualTo(TypeStream.VIZ.getId());
        assertThat(preVizStreamType).isEqualTo(TypeStream.PRE_VID.getId());
        assertThat(unrecognizedStreamType).isNull();
    }

}
