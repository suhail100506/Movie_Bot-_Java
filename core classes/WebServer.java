import static spark.Spark.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class WebServer {

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    // Prefer environment variables; fallback to provided values so it works out-of-the-box.
    private static String TMDB_V4_TOKEN = System.getenv("TMDB_API_READ_TOKEN");
    private static String TMDB_V3_KEY = System.getenv("TMDB_API_KEY");
    static {
        if (TMDB_V4_TOKEN == null || TMDB_V4_TOKEN.isBlank()) {
            TMDB_V4_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyNGRlOTgyM2Y0YzFiNTczYzM0MmRmOTYyYzYwZTFhYyIsIm5iZiI6MTc1NzMxNTI3OC4wOTIsInN1YiI6IjY4YmU4MGNlODM1NzQzYjNjNzA2YTI1NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.2jiYncJNr7_10mQinwqrBwg5VhwvDc8zzf9yYj8xc8Y";
        }
        if (TMDB_V3_KEY == null || TMDB_V3_KEY.isBlank()) {
            TMDB_V3_KEY = "24de9823f4c1b573c342df962c60e1ac";
        }
    }

    public static void main(String[] args) {
        port(8080);

        // Serve static files from project root
        staticFiles.externalLocation("d:/project new/movie bot java");

        // Minimal CORS for API routes
        before("/api/*", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            res.header("Access-Control-Allow-Methods", "GET, OPTIONS");
        });
        options("/api/*", (req, res) -> {
            res.status(200);
            return "OK";
        });

        // Root redirect
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        // ========== TMDB Proxy Endpoints ==========
        // GET /api/tmdb/trending?page=1
        get("/api/tmdb/trending", (req, res) -> {
            String page = req.queryParams("page");
            if (page == null || page.isBlank()) page = "1";
            URI uri = URI.create("https://api.themoviedb.org/3/trending/movie/day?language=en-US&page=" + page);
            return forwardTmdb(res, uri);
        });

        // GET /api/tmdb/search?q=query&page=1
        get("/api/tmdb/search", (req, res) -> {
            String q = req.queryParams("q");
            String page = req.queryParams("page");
            if (q == null) q = "";
            if (page == null || page.isBlank()) page = "1";
            String qEnc = URLEncoder.encode(q, StandardCharsets.UTF_8);
            URI uri = URI.create("https://api.themoviedb.org/3/search/movie?include_adult=false&language=en-US&page=" + page + "&query=" + qEnc);
            return forwardTmdb(res, uri);
        });

        // GET /api/tmdb/movie/:id (include credits and keywords for richer details)
        get("/api/tmdb/movie/:id", (req, res) -> {
            String id = req.params(":id");
            URI uri = URI.create("https://api.themoviedb.org/3/movie/" + id + "?language=en-US&append_to_response=credits,keywords");
            return forwardTmdb(res, uri);
        });
    }

    private static String forwardTmdb(spark.Response res, URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Authorization", "Bearer " + TMDB_V4_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> resp = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        res.type("application/json");
        res.status(resp.statusCode());
        return resp.body();
    }
}
