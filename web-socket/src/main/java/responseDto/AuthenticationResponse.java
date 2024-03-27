package responseDto;

import com.ducquyet.websocket.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Integer id;
    private String fullName;
    private String username;
    private String avatarUrl;
    private String avatar;
    private Set<Role> roles;
    private String accessToken;
}
