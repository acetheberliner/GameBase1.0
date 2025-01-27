package progetto.videogame.gamebase.models.signin

// Modello per la risposta della creazione di una sessione ospite
data class GuestSessionResponse(
    val guest_session_id: String
)

// Modello per la risposta della creazione di un token di richiesta
data class TokenResponse(
    val request_token: String
)