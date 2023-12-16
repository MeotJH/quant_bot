package com.server.quant_bot.comm.security.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.comm.security.dto.UserDto;
import com.server.quant_bot.comm.security.mapper.UserMapper;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Comment("유저의 ID")
    @Column(updatable = false, unique = true, nullable = false)
    private String userId;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Comment("유저의 추세이동테이블 ID")
    @OneToMany(mappedBy = "user")
    private List<TrendFollow> trendFollows = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public BaseEntity update(Object BeforeCastedDto) {
        UserDto dto = (UserDto) BeforeCastedDto;
        return UserMapper.INSTANCE.userDTOToEntity(dto);
    }

}
