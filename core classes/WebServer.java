
import static spark.Spark.*;

public class WebServer {

    public static void main(String[] args) {
        port(8080); // Changed to avoid port conflict
        // Serve static files from the project root
        staticFiles.externalLocation("d:/project new/movie bot java");
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });
    }
}
// Commit 1: Update homepage layout
