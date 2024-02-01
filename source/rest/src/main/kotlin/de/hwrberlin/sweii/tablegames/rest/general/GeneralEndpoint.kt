package de.hwrberlin.sweii.tablegames.rest.general

import de.hwrberlin.sweii.tablegames.general.Game
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games/general")
class GeneralEndpoint {

    @CrossOrigin(originPatterns = ["*"])
    @GetMapping("/list")
    fun gameList(): GameListResponse {
        return GameListResponse(Game.entries.map { game ->
            GameResponse(
                game.name,
                game.minPlayerCount,
                game.maxPlayerCount
            )
        }.toList())
    }
}