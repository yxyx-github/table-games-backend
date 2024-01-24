package de.hwrberlin.sweii.tablegames.rest.general

import de.hwrberlin.sweii.tablegames.general.Game
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GeneralEndpoint {

    @CrossOrigin(originPatterns = ["*"])
    @GetMapping("/games/general/list")
    fun gameList(): GameListResponse {
        return GameListResponse(Game.entries.map { game ->
            GameListResponse.Game(
                game.name,
                game.minPlayerCount,
                game.maxPlayerCount
            )
        }.toList())
    }
}