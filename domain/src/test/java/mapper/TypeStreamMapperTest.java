package mapper;

import com.betfair.video.domain.dto.entity.TypeStream;
import com.betfair.video.domain.dto.valueobject.ContentType;
import com.betfair.video.domain.mapper.TypeStreamMapper;
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
    @DisplayName("Should convert ContentType to TypeStream ID")
    void shouldConvertContentTypeToStreamTypeId() {
        // Given
        ContentType vid = ContentType.VID;
        ContentType viz = ContentType.VIZ;
        ContentType preViz = ContentType.PRE_VID;
        ContentType unrecognized = ContentType.UNRECOGNIZED_VALUE;

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
