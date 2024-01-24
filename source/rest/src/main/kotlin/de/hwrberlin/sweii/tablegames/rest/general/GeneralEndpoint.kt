package de.hwrberlin.sweii.tablegames.rest.general

import de.hwrberlin.sweii.tablegames.general.Game
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games/general")
class GeneralEndpoint {

    @GetMapping("/list")
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