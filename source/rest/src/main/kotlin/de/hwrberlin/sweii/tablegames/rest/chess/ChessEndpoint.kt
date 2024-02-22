package de.hwrberlin.sweii.tablegames.rest.chess

import de.hwrberlin.sweii.tablegames.chess.Chess
import de.hwrberlin.sweii.tablegames.general.GameState
import de.hwrberlin.sweii.tablegames.rest.SseService
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidActionException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidGameException
import de.hwrberlin.sweii.tablegames.rest.exceptions.InvalidSessionTokenException
import de.hwrberlin.sweii.tablegames.rest.exceptions.NotEnoughUsersExecution
import de.hwrberlin.sweii.tablegames.rest.tictactoe.TicTacToeStateResponse
import de.hwrberlin.sweii.tablegames.session.SessionService
import de.hwrberlin.sweii.tablegames.session.entity.Session
import de.hwrberlin.sweii.tablegames.tictactoe.TicTacToe
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.system.exitProcess

@RestController
@RequestMapping("/api/games/chess")
class ChessEndpoint(
    private val sessionService: SessionService,
    private val sseService: SseService,
) {

    @CrossOrigin(originPatterns = ["*"])
    @GetMapping("/state")
    fun state(@RequestParam sessionToken: String): ChessStateResponse {
        val session: Session = sessionService.getSession(sessionToken) ?: throw InvalidSessionTokenException()
        val chess: GameState = session.gameState
        if (chess !is Chess) {
            throw InvalidGameException("Sessions game isn't chess")
        }
        val blackUserId: Long = session.users.find { it.id != session.host.id }?.id ?: throw NotEnoughUsersExecution()
        return ChessStateResponse(chess.board, chess.turn(blackUserId)!!, chess.state, chess.winner(blackUserId))
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/move")
    fun move(@RequestBody chessMoveRequest: ChessMoveRequest) {
        if (!sessionService.verifyUser(
                chessMoveRequest.sessionToken,
                chessMoveRequest.authToken,
                chessMoveRequest.userId
            )
        ) throw InvalidSessionTokenException()
        val session: Session =
            sessionService.getSession(chessMoveRequest.sessionToken) ?: throw InvalidSessionTokenException()
        val chess: GameState = session.gameState
        if (chess !is Chess) {
            throw InvalidGameException("Sessions game isn't chess")
        }
        if (!chess.move(
                chessMoveRequest.fromX,
                chessMoveRequest.fromY,
                chessMoveRequest.toX,
                chessMoveRequest.toY,
                chessMoveRequest.userId
            )
        ) {
            throw InvalidActionException()
        }
        sessionService.updateGameState(chessMoveRequest.sessionToken, chess)
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/promote")
    fun promote(@RequestBody chessPromotionRequest: ChessPromotionRequest) {
        if (!sessionService.verifyUser(
                chessPromotionRequest.sessionToken,
                chessPromotionRequest.authToken,
                chessPromotionRequest.userId
            )
        ) throw InvalidSessionTokenException()
        val session: Session =
            sessionService.getSession(chessPromotionRequest.sessionToken) ?: throw InvalidSessionTokenException()
        val chess: GameState = session.gameState
        if (chess !is Chess) {
            throw InvalidGameException("Sessions game isn't chess")
        }
        if (!chess.promotion(
                chessPromotionRequest.fromX,
                chessPromotionRequest.fromY,
                chessPromotionRequest.toX,
                chessPromotionRequest.toY,
                chessPromotionRequest.promoteTo,
                chessPromotionRequest.userId,
            )
        ) {
            throw InvalidActionException()
        }
        sessionService.updateGameState(chessPromotionRequest.sessionToken, chess)
    }

    @CrossOrigin(originPatterns = ["*"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/castle")
    fun castle(@RequestBody chessCastleRequest: ChessCastleRequest) {
        if (!sessionService.verifyUser(
                chessCastleRequest.sessionToken,
                chessCastleRequest.authToken,
                chessCastleRequest.userId
            )
        ) throw InvalidSessionTokenException()
        val session: Session =
            sessionService.getSession(chessCastleRequest.sessionToken) ?: throw InvalidSessionTokenException()
        val chess: GameState = session.gameState
        if (chess !is Chess) {
            throw InvalidGameException("Sessions game isn't chess")
        }
        if (!chess.castle(
                chessCastleRequest.kingside,
                chessCastleRequest.userId
            )
        ) {
            throw InvalidActionException()
        }
        sessionService.updateGameState(chessCastleRequest.sessionToken, chess)
    }
}