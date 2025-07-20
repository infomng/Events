package com.events.auth.exception;

public class InvalidRefreshTokenEXception extends UnauthorizedException{
    public InvalidRefreshTokenEXception() {
        super("Le refresh invalide, veuillez vous reconnecter.");
    }
}
