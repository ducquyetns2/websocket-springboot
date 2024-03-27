package responseDto;

import com.ducquyet.websocket.entity.Conversation;
import com.ducquyet.websocket.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String fullName;
    private String username;
    private String avatarUrl;
    private String avatar;
    private boolean isOnline;
}
