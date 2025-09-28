import static spark.Spark.*;

public class WebServer {

    public static void main(String[] args) {
        port(8080);
        staticFiles.externalLocation("d:/project new/movie bot java");
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });
    }
}
