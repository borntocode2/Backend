package goodspace.backend.authorization.service;

import goodspace.backend.authorization.dto.request.AccessTokenReissueRequestDto;
import goodspace.backend.authorization.dto.request.SignInRequestDto;
import goodspace.backend.authorization.dto.request.SignUpRequestDto;
import goodspace.backend.authorization.dto.response.AccessTokenResponseDto;
import goodspace.backend.authorization.dto.response.TokenResponseDto;

public interface GoodSpaceAuthorizationService {
    TokenResponseDto signUp(SignUpRequestDto requestDto);

    TokenResponseDto signIn(SignInRequestDto requestDto);

    AccessTokenResponseDto reissueAccessToken(AccessTokenReissueRequestDto requestDto);
}
