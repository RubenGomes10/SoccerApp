package soccerapp;

import soccerapp.webapi.SoccerWebApi;
import soccerapp.webapi.model.DtoLeague;
import soccerapp.webapi.model.DtoLeagueTable;
import soccerapp.webapi.model.DtoPlayersList;
import soccerapp.webapi.model.DtoTeam;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class Program {

    public static void main(String[] args) throws Exception {
        try(SoccerWebApi api = new SoccerWebApi()){
            CompletableFuture<DtoLeague[]> leagues = api.getLeagues();
            CompletableFuture<DtoLeagueTable> table = leagues
                    .thenComposeAsync(arr -> api.getLeagueTable(arr[4].id));
            CompletableFuture<DtoTeam> tottenham = table
                    .thenComposeAsync( tbl -> api.getTeam(tbl.standing[3]._links.team.href));
            CompletableFuture<DtoPlayersList> players = tottenham
                    .thenComposeAsync( tot -> api.getPlayers(tot._links.players.href));

            leagues.thenApply(Arrays::toString).thenAccept(System.out::println).join();
            table.thenAccept(System.out::println).join();
            tottenham.thenAccept(System.out::println).join();
            players.thenAccept(System.out::println).join();
        }
    }
}
