package com.mile.mile.security.service;

import com.mile.mile.security.config.JwtTokenUtil;
import com.mile.mile.security.dao.UserDao;
import com.mile.mile.security.dao.UserRefreshTokenRepository;
import com.mile.mile.security.entity.UserEntity;
import com.mile.mile.security.entity.UserRefreshToken;
import com.mile.mile.security.model.MyUserDetails;
import com.mile.mile.security.model.TokenModel;
import com.mile.mile.security.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;
    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder customPasswordEncoder;
	private final UserRefreshTokenRepository userRefreshTokenRepository;


	public UserDetailsServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil,
								  UserRefreshTokenRepository userRefreshTokenRepository
	) {
        this.userDao = userDao;
        this.customPasswordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new MyUserDetails(userEntity);
    }

    public UserEntity loadUserEntityByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return userEntity;
    }

    @Transactional
    public UserEntity save(UserModel user) {
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setUsername(user.getUsername());
        newUserEntity.setPassword(customPasswordEncoder.encode(user.getPassword()));
        return userDao.save(newUserEntity);
    }

    public TokenModel login(String username) {
        UserEntity userEntity = loadUserEntityByUsername(username);
		String refreshToken = createRefreshToken(userEntity);
		return jwtTokenUtil.generateToken(userEntity, refreshToken);
    }

	/**
	 * @return newly generated access token or nothing, if the refresh token is not valid
	 */
	@Transactional
	public Optional<TokenModel> refreshAccessToken(String refreshToken) {
		return userRefreshTokenRepository.findByToken(refreshToken)
				.map(userRefreshToken -> {
					userRefreshTokenRepository.deleteById(userRefreshToken.getId());
					String refreshTokenNew = createRefreshToken(userRefreshToken.getUser());
					return jwtTokenUtil.generateToken(userRefreshToken.getUser(), refreshTokenNew);
				});
	}


	public String createRefreshToken(UserEntity user) {
		String token = UUID.randomUUID().toString();
		userRefreshTokenRepository.save(new UserRefreshToken(token, user));
		return token;
	}

    @Transactional
    public void logoutUser(String refreshToken) {
        userRefreshTokenRepository.findByToken(refreshToken)
                .ifPresent(userRefreshTokenRepository::delete);
    }
}