package soccerapp.webapi;

import com.google.gson.Gson;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import soccerapp.webapi.model.DtoLeague;
import soccerapp.webapi.model.DtoLeagueTable;
import soccerapp.webapi.model.DtoPlayersList;
import soccerapp.webapi.model.DtoTeam;

import java.util.concurrent.CompletableFuture;

/**
 * @author Miguel Gamboa
 *         created on 23-05-2016
 */
public class SoccerWebApi implements AutoCloseable {

    private static final String HOST = "http://api.football-data.org/";
    private static final String PATH_LEAGUES = "/v1/soccerseasons";
    private static final String PATH_TABLE = "/v1/soccerseasons/%d/leagueTable";
    private static final String API_TOKEN_VALUE = "PLACE HERE YOUR API KEY";
    private static final String API_TOKEN_KEY = "X-Auth-Token";

    private final AsyncHttpClient getter;
    private final Gson gson;

    public SoccerWebApi(AsyncHttpClient getter, Gson gson)
    {
        this.getter = getter;
        this.gson = gson;
    }

    public SoccerWebApi()
    {
        this.getter = new DefaultAsyncHttpClient();
        this.gson = new Gson();
    }

    public CompletableFuture<DtoLeague[]> getLeagues()
    {
        return httpGet(HOST + PATH_LEAGUES, DtoLeague[].class);
    }

    public CompletableFuture<DtoLeagueTable> getLeagueTable(int leagueId)
    {
        String path = String.format(PATH_TABLE, leagueId);
        return httpGet(HOST + path, DtoLeagueTable.class);
    }

    public CompletableFuture<DtoTeam> getTeam(String path)
    {
        return httpGet(path, DtoTeam.class);
    }

    public CompletableFuture<DtoPlayersList> getPlayers(String path)
    {
        return httpGet(path, DtoPlayersList.class);
    }

    private <T> CompletableFuture<T> httpGet(String path, Class<T> arrayType)
    {
        return getter
                .prepareGet(path)
                // .addHeader(API_TOKEN_KEY, API_TOKEN_VALUE) // Remove comment to enable your API KEY
                .execute()
                .toCompletableFuture()
                .thenApply(r -> gson.fromJson(r.getResponseBody(), arrayType));
    }

    @Override
    public void close() throws Exception
    {
        if (!getter.isClosed()) {
            getter.close();
        }
    }

    public boolean isClosed()
    {
        return getter.isClosed();
    }
}
